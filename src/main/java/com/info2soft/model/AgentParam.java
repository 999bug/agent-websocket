package com.info2soft.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lisy
 * @Date: 2022/09/27/10:39
 * @Description: 连接 agent 所需要参数
 */
@Getter
@Setter
@ToString
public class AgentParam implements Serializable {
    private static final long serialVersionUID = 3326615605946068549L;

    // ------通用------
    /**
     * 0-执行脚本命令 1-检测主机状态：ping..
     */
    private Integer type;
    /**
     * 代理用户名
     */
    private String username;
    /**
     * 代理服务密码
     */
    private String secret;
    /**
     * 执行命令:脚本、ping或者执行监听规则
     */
    private String command;
    /**
     * 操作类型:0-create新增, 1-remove删除, 2-update更新
     */
    private Integer operateType;
    /**
     * 用于回调函数
     */
    private Long flag;
    /**
     * 代理节点IP
     */
    private String agentIp;
    /**
     * drm 主动发送
     */
    private boolean drmActive;

    // ------执行脚本------
    /**
     * 超时时间，秒
     */
    private Integer timeout;

    // ------状态检测------
    /**
     * 业务系统ID
     */
    private String bsId;
    /**
     * drm_host主机ID,与 agentIp 一一对应
     */
    private String hostId;
    /**
     * 保存业务系统中需要代理检测的所有的主机IP，不包含自己
     */
    private List<String> hostIps;
    /**
     * 重试次数
     */
    private Integer retry;

}
