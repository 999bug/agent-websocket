package com.info2soft.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Lisy
 * @Date: 2022/11/25/11:03
 * @Description: 每一个检测条件
 */
@Getter
@Setter
public class AgentListItem implements Serializable {

    private static final long serialVersionUID = 3671277664949105396L;

    /**
     * ip地址
     */
    private String address;

    /**
     * 端口
     */
    private int port;
    /**
     * 检测类型：0:‘端口检测’1:‘ping检测
     */
    private int type;
    /**
     * 检测状态：0:‘正常’1:‘异常'
     */
    private int status;


}