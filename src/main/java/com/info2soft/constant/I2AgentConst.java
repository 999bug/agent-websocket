package com.info2soft.constant;

/**
 * @Author: Lisy
 * @Date: 2022/11/15/17:17
 * @Description:
 */
public interface I2AgentConst {

    // 配置文件前缀
    String AUTH_USERNAME_SPN = "auth.username";
    String AUTH_SECRET_SPN = "auth.secret";
    String DRM_ADDRESS_SPN = "drm.address";
    String DRM_PORT_SPN = "drm.port";
    String DRM_RETRY_INTERVAL_SPN = "drm.retry.interval";
    String DRM_LOCAL_IP_SPN = "drm.local.ip";

    String TEMPLATE = "%s[%s]:";

    // 默认配置项
    String USERNAME = "";
    String PASSWORD = "";
    String DRM_PORT = "58065";
    String DRM_ADDRESS = "127.0.0.1";
    String LOCAL_IP = "";
    /**
     * 重新连接drm 间隔
     */
    int DRM_RETRY_INTERVAL = 30;

    String PREFIX = "/drmWsAgentConnect/";
    String WS = "ws://";
    String COLON = ":";

    /**
     * 检测类型 0-端口，1-ping
     */
    int DETECTION_PORT = 0;
    int DETECTION_PING = 1;
    /**
     * 检测状态 0-正常，1-异常
     */
    Integer DETECTION_NORMAL = 0;
    Integer DETECTION_EXCEPTION = 1;
}
