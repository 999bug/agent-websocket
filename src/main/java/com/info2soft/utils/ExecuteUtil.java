package com.info2soft.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info2soft.constant.I2AgentConst;
import com.info2soft.constant.I2AgentEnv;
import com.info2soft.enums.AgentResultMsgEnum;
import com.info2soft.enums.OperateTypeEnum;
import com.info2soft.model.*;
import com.info2soft.websocket.SocketClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: lisy
 * @Date: 2022/09/20/22:40
 * @Description: 执行命令工具类
 */
@Slf4j
public class ExecuteUtil {

    private ExecuteUtil() {}

    private static final String F_TRUE = "true";
    private static final String F_FALSE = "false";
    private static final String F_LEFT = "(";
    private static final String F_RIGHT = ")";
    private static final Snowflake SNOW_FLAKE = IdUtil.createSnowflake(0, 0);

    public static String execute(AgentParam agentParam) {
        checkAuth(agentParam);
        switch (OperateTypeEnum.fromTypeValue(agentParam.getType())) {
            case EXECUTE_SCRIPT:
                return executeScript(agentParam);
            case EXECUTE_AGENT_STATUS:
                return executeDetection(agentParam);
            default:
                return "未知类型";
        }
    }

    private static String executeDetection(AgentParam agentParam) {
        log.info("业务系统: {} 执行了: {}", agentParam.getBsId(), new Date());
        CallBack callBack = CallBack.builder()
                .hostIps(agentParam.getHostIps())
                .bsId(agentParam.getBsId())
                .ip(agentParam.getAgentIp()).build();

        CompletableFuture.runAsync(() -> {
            try {
                String command = agentParam.getCommand();
                if (StringUtils.isBlank(command)) {
                    throw new RuntimeException("检测条件为空 bsId:" + agentParam.getBsId());
                }

                AgentCondition agentCondition = JSONObject.parseObject(command, AgentCondition.class);
                List<DrmDetectionRecord> drmDetectionRecords = new LinkedList<>();

                int status = executeCondition(agentCondition, drmDetectionRecords, agentParam);
                log.info("业务系统：{} 检测完毕，结果：{}", agentParam.getBsId(), status);

                callBack.setStatus(status);
                callBack.setDrmDetectionRecords(drmDetectionRecords);
                SocketClient.getInstance().send(JSON.toJSONString(callBack));
            } catch (Exception e) {
                callBack.setResult(e.getMessage());
                SocketClient.getInstance().send(JSON.toJSONString(callBack));
                log.error(e.getMessage(), e);
            }
        });
       return AgentResultMsgEnum.OPERATE_SUCCESS.getMsg();
    }

    /**
     * 获取此业务系统的检测状态
     *
     * @param agentCondition      检测条件对象包含所有检测条件
     * @param drmDetectionRecords 每次检测记录集合
     * @return 检测状态 0-正常，1-异常
     */
    private static int executeCondition(AgentCondition agentCondition, List<DrmDetectionRecord> drmDetectionRecords, AgentParam agentParam) {
        StringBuilder sb = new StringBuilder();
        List<AgentConditionListItem> agentConditionLists = agentCondition.getAgentListItems();
        // 外部逻辑表达式 && 或者 ||
        String outerOperator = agentCondition.getOperator();

        agentConditionLists.forEach(c -> {
            sb.append(F_LEFT);
            List<AgentListItem> agentListItems = c.getAgentListItems();
            // 内部逻辑表达式 && 或者 ||
            String innerOperator = c.getOperator();

            agentListItems.forEach(a -> {
                Integer retry = agentParam.getRetry();
                String result = F_FALSE;
                while (retry >= 0) {
                    int type = a.getType();
                    int executeResult;
                    String errorType;
                    String content = a.getAddress();
                    try {
                        if (type == I2AgentConst.DETECTION_PORT) {
                            executeResult = CommandUtil.portDetection(a.getAddress(), a.getPort());
                            content = content + ":" + a.getPort();
                            errorType = "端口检测:" + content;
                        } else {
                            executeResult = CommandUtil.pingDetection(a.getAddress());
                            errorType = "ping检测" + content;
                        }

                        // 处理检测结果
                        boolean detectionResult = executeResult == a.getStatus();
                        int recordResult;
                        if (detectionResult) {
                            result = F_TRUE;
                            recordResult = I2AgentConst.DETECTION_NORMAL;
                            // 结束重试循环
                            retry = -1;
                        } else {
                            result = F_FALSE;
                            recordResult = I2AgentConst.DETECTION_EXCEPTION;
                            log.warn("业务系统 {} {} 检测结果异常", agentParam.getBsId(), errorType);
                        }
                        DrmDetectionRecord drmDetectionRecord = generateRecord(agentParam, recordResult, type, content);

                        drmDetectionRecords.add(drmDetectionRecord);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        retry--;
                    }
                }

                sb.append(result).append(innerOperator);
            });
            deleteLast2char(sb);
            sb.append(F_RIGHT);
            sb.append(outerOperator);
        });
        deleteLast2char(sb);
        boolean b = CommandUtil.calcExpression(sb.toString());
        return b ? I2AgentConst.DETECTION_NORMAL : I2AgentConst.DETECTION_EXCEPTION;
    }

    private static void deleteLast2char(StringBuilder sb) {
        int length = sb.length();
        sb.delete(length - 2, length);
    }

    private static DrmDetectionRecord generateRecord(AgentParam agentParam, int status, int detectionType, String content) {
        return DrmDetectionRecord.builder()
                .id(SNOW_FLAKE.nextId() + "")
                .busId(agentParam.getBsId())
                .hostId(agentParam.getHostId())
                .detectionType(detectionType)
                .detectionContent(content)
                .createTime(new Date())
                .status(status).build();
    }

    private static String executeScript(AgentParam agentParam) {
        String result;
        try {
            String command = agentParam.getCommand();
            if (StringUtils.isBlank(command)) {
                result = "command is empty!";
            } else {
                int timeout = agentParam.getTimeout();
                switch (OperateTypeEnum.fromTypeValue(agentParam.getOperateType())) {
                    case EXECUTE:
                        result = new CommandUtil(command, timeout).execute();
                        break;
                    case PING:
                        result = "pong;" + new CommandUtil(command, timeout).execute();
                        break;
                    default:
                        result = "未知类型"+ agentParam.getOperateType();
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 检查格式是否来自信任源
     */
    private static void checkAuth(AgentParam agentParam) {
        if (Objects.nonNull(agentParam)) {
            String authName = agentParam.getUsername();
            String authSecret = agentParam.getSecret();
            if (!(authName.equals(I2AgentEnv.authUserName) && authSecret.equals(I2AgentEnv.authSecret))) {
                throw new RuntimeException("username or password incorrect");
            }
        } else {
            throw new RuntimeException("agentParam is null");
        }

    }

}
