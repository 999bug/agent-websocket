package com.info2soft.enums;

/**
 * @Author: Lisy
 * @Date: 2022/11/16/10:19
 * @Description: 操作类型
 */
public enum OperateTypeEnum {

    /**
     * 代理主机执行脚本
     */
    EXECUTE_SCRIPT(0),

    /**
     * 代理主机ping主机或者端口
     */
   EXECUTE_AGENT_STATUS(1),

    /**
     * ping 目标节点是否畅通
     */
    PING(2),

    /**
     * 执行脚本
     */
    EXECUTE(3),

    /**
     * 未知操作类型
     */
    ERROR(-999);

    private final Integer type;

    OperateTypeEnum(Integer value) {
        this.type = value;
    }

    public Integer getType() {
        return type;
    }

    public static OperateTypeEnum fromTypeValue(Integer type) {
        for (OperateTypeEnum operateType : OperateTypeEnum.values()) {
            if (operateType.getType().equals(type)) {
                return operateType;
            }
        }
        return ERROR;
    }

}
