package com.info2soft.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * @Author: Lisy
 * @Date: 2022/11/25/11:03
 * @Description: 检测记录实体
 */
@Getter
@Setter
@Builder
public class DrmDetectionRecord {
    private static final long serialVersionUID = 2334364335704567099L;

    private String id;
    /**
     * 业务系统ID
     */
    private String busId;
    /**
     * 检测点主机ID
     */
    private String hostId;
    /**
     * 检测类型： 0-端口检测 1-ping检测
     */
    private Integer detectionType;
    /**
     * 检测内容
     */
    private String detectionContent;
    /**
     * 状态： 0-正常 1-异常 2-未知
     */
    private Integer status;
    /**
     * 详情
     */
    private String detail;
    private Date createTime;

}