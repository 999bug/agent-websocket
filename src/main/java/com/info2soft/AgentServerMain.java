package com.info2soft;

import com.info2soft.constant.ConfigFileGenerator;
import com.info2soft.constant.I2AgentEnv;
import com.info2soft.websocket.SocketClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lisy
 * @Date: 2022/09/20/20:47
 * @Description:
 */
public class AgentServerMain {

    private static final Logger log = LoggerFactory.getLogger(AgentServerMain.class);

    private AgentServerMain() {
    }

    public static void main(String[] args) {
        if (args.length == 1 && StringUtils.isNotBlank(args[0])) {
            String configPath = args[0];
            try {
                if ("--configure".equalsIgnoreCase(configPath)) {
                    ConfigFileGenerator.generator();
                    return;
                }
                I2AgentEnv.getInstance(configPath);
                log.info("config path {}", configPath);
                // 初始化Session
                SocketClient.getInstance().init();

                TimeUnit.DAYS.sleep(Integer.MAX_VALUE);
            } catch (IOException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        } else {
            log.info("please input config path!");
            System.exit(-1);
        }
    }


}
