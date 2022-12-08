package com.info2soft.utils;

import com.info2soft.constant.I2AgentConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lisy
 * @Date: 2022/09/20/10:20
 * @Description: 执行命令工具类
 */
public class CommandUtil {

    private static final Logger log = LoggerFactory.getLogger(CommandUtil.class);

    /**
     * 当前操作系统是否为Windows
     */
    static final boolean WINDOWS = System.getProperty("os.name").toLowerCase(Locale.ROOT).startsWith("windows");


    private final String scriptPth;
    /**
     * 单位;秒
     */
    private final int timeout;
    private Process process;

    public CommandUtil(String scriptPth, int timeout) {
        this.scriptPth = scriptPth;
        if (timeout < 0) {
            throw new IllegalArgumentException("script timeout must be greater than or equal to 0.");
        }
        this.timeout = timeout;
    }

    public String execute() {
        String[] cmdCommand;
        if (WINDOWS) {
            cmdCommand = new String[]{"cmd.exe", "/c", scriptPth};
        } else {
            cmdCommand = new String[]{"/bin/sh", "-c", scriptPth};
        }
        return getString(cmdCommand, timeout);
    }

    public static int pingDetection(String ipAddress) {
        int timeOut = 3000;
        boolean status = Boolean.FALSE;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);

        } catch (IOException ignored) {}
        return status ? I2AgentConst.DETECTION_NORMAL : I2AgentConst.DETECTION_EXCEPTION;
    }

    public static int portDetection(String ip, int port) {
        boolean result = Boolean.FALSE;
        try (Socket connect = new Socket()) {
            connect.connect(new InetSocketAddress(ip, port), 100);
            result = connect.isConnected();
        } catch (IOException ignored) {
        }
        return result ? I2AgentConst.DETECTION_NORMAL : I2AgentConst.DETECTION_EXCEPTION;
    }

    /**
     * 计算布尔表达式
     * @param expression “true && false || true”
     */
    public static boolean calcExpression(String expression) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            return (boolean) (Boolean)engine.eval(expression);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private String getString(String[] command, int timeout) {
        try {
            log.info("{} will be executed", Arrays.toString(command));
            process = Runtime.getRuntime().exec(command);

            Callable<String> stdOutStream = () -> {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                return readStream(bufferedReader);
            };

            Callable<String> errOutStream = () -> {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                return readStream(errorReader);
            };

            FutureTask<String> stdStreamTask = new FutureTask<>(stdOutStream);
            FutureTask<String> errStreamTask = new FutureTask<>(errOutStream);
            new Thread(stdStreamTask).start();
            new Thread(errStreamTask).start();

            if (timeout != 0) {
                boolean execEnd = process.waitFor(timeout, TimeUnit.SECONDS);
                if (!execEnd) {
                    process.destroyForcibly();
                    throw new RuntimeException("script " + command + " execution timed out");
                }
            }
            return stdStreamTask.get();
        } catch (Exception e) {
            process.destroyForcibly();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String readStream(BufferedReader bufferedReader) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return stringBuilder.toString();
    }

}
