package com.info2soft.enums;

/**
 * @Author: Lisy
 * @Date: 2022/11/16/10:19
 * @Description: 代理节点返回消息枚举类
 */
public enum AgentResultMsgEnum {
    /**
     * 操作成功
     */
    OPERATE_SUCCESS("操作成功"),
    /**
     * 操作失败
     */
    OPERATE_FAIL("操作失败！！"),


    /**
     * 未知操作类型
     */
    ERROR("-999");

    private final String msg;

    AgentResultMsgEnum(String value) {
        this.msg = value;
    }

    public String getMsg() {
        return msg;
    }

    public static AgentResultMsgEnum fromTypeValue(String type) {
        for (AgentResultMsgEnum operateType : AgentResultMsgEnum.values()) {
            if (operateType.getMsg().equals(type)) {
                return operateType;
            }
        }
        return ERROR;
    }

}
