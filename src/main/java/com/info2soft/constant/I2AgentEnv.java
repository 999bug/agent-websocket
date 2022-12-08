package com.info2soft.constant;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @Author: lisy
 * @Date: 2022/09/20/20:37
 * @Description:
 */
public class I2AgentEnv {
    private static final Logger log = LoggerFactory.getLogger(I2AgentEnv.class);

    private static volatile I2AgentEnv INSTANCE;

    /**
     * 代理用户名
     */
    public static String authUserName;
    /**
     * 代理服务密码
     */
    public static String authSecret;
    /**
     * drm地址
     */
    public static String drmAddress = "127.0.0.1";
    /**
     * drm端口
     */
    public static int drmPort = 26888;

    /**
     * 本机IP
     */
    public static String LOCAL_IP = "";
    /**
     * 重试间隔
     */
    public static int DRM_RETRY_INTERVAL = 30;
    /**
     * 配置文件路径
     */
    public static String CONFIG_PATH;

    private I2AgentEnv(String configPath) {
        init(configPath);
    }

    public static void getInstance(String configPath) {
        if (INSTANCE == null) {
            synchronized (I2AgentEnv.class) {
                if (INSTANCE == null) {
                    INSTANCE = new I2AgentEnv(configPath);
                }
            }
        }
    }

    private void init(String confFile) {
        Properties prop = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(confFile));
             BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            prop.load(bf);
        } catch (Throwable t) {
            log.error("Fail to read the program config file.");
            log.error(t.getMessage(), t);
            System.exit(-1);
        }

        CONFIG_PATH = new File(confFile).getParent() + File.separator;
        String drmAddressStr = prop.getProperty(I2AgentConst.DRM_ADDRESS_SPN, drmAddress).trim();
        String portStr = prop.getProperty(I2AgentConst.DRM_PORT_SPN, String.valueOf(drmPort)).trim();
        checkParamsAndSet(drmAddressStr, portStr);
        authUserName = prop.getProperty(I2AgentConst.AUTH_USERNAME_SPN, authUserName).trim();
        authSecret = prop.getProperty(I2AgentConst.AUTH_SECRET_SPN, authSecret).trim();
        LOCAL_IP = prop.getProperty(I2AgentConst.DRM_LOCAL_IP_SPN, LOCAL_IP).trim();

        DRM_RETRY_INTERVAL = Integer.parseInt(prop.getProperty(I2AgentConst.DRM_RETRY_INTERVAL_SPN, authSecret).trim());
//
//        try {
//            LOCAL_IP = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void checkParamsAndSet(String addressStr, String portStr) {
        if (StringUtils.isBlank(addressStr)) {
            throw new IllegalArgumentException("address is empty");
        } else {
            // TODO: 2022/11/15 检查 ip 是否合法
            drmAddress = addressStr;
        }

        if (StringUtils.isNotBlank(portStr)) {
            drmPort = checkPort(Integer.parseInt(portStr));
        }
    }

    private int checkPort(int port) {
        if (port < 0 || port > 0xFFFF) {
            throw new IllegalArgumentException("port out of range:" + port);
        }
        return port;
    }

}
