package com.info2soft.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lisy
 * @Date: 2022/11/16/14:27
 * @Description: 代理返回消息接收类
 */
@Getter
@Setter
@Builder
public class CallBack implements Serializable {
    private static final long serialVersionUID = -2432980795477900173L;

    /**
     * 返回结果
     */
    private String result;
    /**
     * 区分每次发送消息的唯一标记，用于接收返回消息
     */
    private Long key;
    /**
     * 代理节点 IP
     */
    private String ip;
    /**
     * drm 主动发送
     */
    private boolean drmActive;

    /**
     * 该主机检测状态，0-正常，1-异常，2-未知：drm与agent通信异常
     */
    private Integer status;
    /**
     * 业务系统ID
     */
    private String bsId;
    /**
     * 保存业务系统中需要代理检测的所有的主机IP，不包含自己
     */
    private List<String> hostIps;
    /**
     * agent返回的所有检测记录，等待批量插入DRM数据库
     */
    private List<DrmDetectionRecord> drmDetectionRecords;
}
