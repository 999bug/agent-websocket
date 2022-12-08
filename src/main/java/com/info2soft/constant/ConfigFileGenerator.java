package com.info2soft.constant;

import com.info2soft.utils.EncryptUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;

/**
 * 配置生成器
 */
public class ConfigFileGenerator {
    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8.name());

    private static final Properties AGENT_CONF = new Properties();
    private static final String BASE_DIR = new File("").getAbsolutePath();

    public static void generator() throws IOException {
        System.out.println("使用指南：[]内为默认值，未填写新值时自动使用默认值");
        i2bbConfigGenerator();
    }

    /**
     * 生成i2bb.conf
     */
    private static void i2bbConfigGenerator() throws IOException {

        System.out.print("请设置代理服务用户名: ");
        AGENT_CONF.setProperty(I2AgentConst.AUTH_USERNAME_SPN, readString(I2AgentConst.USERNAME));
        System.out.print("请设置代理服务密码: ");
        AGENT_CONF.setProperty(I2AgentConst.AUTH_SECRET_SPN, EncryptUtil.encrypt(readString(I2AgentConst.PASSWORD)));
        System.out.print("请输入本机IP地址: ");
        AGENT_CONF.setProperty(I2AgentConst.DRM_LOCAL_IP_SPN, readString(I2AgentConst.LOCAL_IP));

        System.out.printf(I2AgentConst.TEMPLATE, "请输入DRM服务地址：", I2AgentConst.DRM_ADDRESS);
        AGENT_CONF.setProperty(I2AgentConst.DRM_ADDRESS_SPN, readString(I2AgentConst.DRM_ADDRESS));
        System.out.printf(I2AgentConst.TEMPLATE, "请输入DRM服务端口：", I2AgentConst.DRM_PORT);
        AGENT_CONF.setProperty(I2AgentConst.DRM_PORT_SPN, readString(I2AgentConst.DRM_PORT));

        System.out.printf(I2AgentConst.TEMPLATE, "请输入连接DRM重试间隔，单位(秒)", I2AgentConst.DRM_RETRY_INTERVAL);
        AGENT_CONF.setProperty(I2AgentConst.DRM_RETRY_INTERVAL_SPN, readString(String.valueOf(I2AgentConst.DRM_RETRY_INTERVAL)));

        String i2bbInstallPath = BASE_DIR + File.separator + "conf";
        saveConfigFile(i2bbInstallPath, "agent.conf");
    }


    /**
     * 保存配置文件，存在旧的配置文件时，旧配置文件将会被重命名
     * @param configSavePath 保存路径
     * @param configName     配置文件名
     * @throws IOException io操作出现异常
     */
    private static void saveConfigFile(String configSavePath, String configName) throws IOException {
        System.out.println("正在保存：" + configName);
        if (!new File(configSavePath).exists()) {
            FileUtils.forceMkdir(new File(configSavePath));
        }
        configSavePath = pathNormalizer(configSavePath) + File.separator + configName;
        File configFile = new File(configSavePath);
        if (configFile.exists()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String time = formatter.format(LocalDateTime.now());
            File backupConfigFile = new File(configFile.getAbsolutePath() + "_" + time + ".bak");
            FileUtils.moveFile(configFile, backupConfigFile);
            System.out.println("原配置文件重命名为：" + backupConfigFile.getAbsolutePath());
        }
        Writer writer = Files.newBufferedWriter(configFile.toPath());
        ConfigFileGenerator.AGENT_CONF.store(writer, "");
        writer.flush();
        writer.close();
        System.out.println("配置文件：" + configName + " " + "生成成功");
    }

    /**
     * 从标准输入读取一个字符串，读取到空时返回默认值
     *
     * @param defaultVal 默认值
     * @return 读取到的值或默认值
     */
    private static String readString(String defaultVal) {
        String input = SCANNER.nextLine();
        if (StringUtils.isNotBlank(input)) {
            input = input.trim();
        } else {
            input = defaultVal;
        }
        return input;
    }

    /**
     * 处理路径中的斜杠
     *
     * @param path 待处理的路径
     * @return 处理后的路径
     */
    private static String pathNormalizer(String path) {
        path = path.replace("\\", "/").replace("//", "/").replace("/", File.separator);
        path = path.endsWith(File.separator) ? path.substring(0, path.length() - 1) : path;
        return path;
    }

}
