package com.info2soft.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info2soft.constant.I2AgentEnv;
import com.info2soft.model.AgentParam;
import com.info2soft.model.CallBack;
import com.info2soft.utils.ExecuteUtil;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.info2soft.constant.I2AgentConst.*;
import static com.info2soft.constant.I2AgentEnv.*;

@ClientEndpoint
@Slf4j
public class SocketClient {

    private static volatile SocketClient INSTANCE;
    /**
     * 默认超时时间 5分钟
     */
    private static final int DEFAULT_TIMEOUT = 5;
    private static String drmInfo = WS + drmAddress + COLON + drmPort + PREFIX + I2AgentEnv.LOCAL_IP;
    /**
     * 重试次数
     */
    private static final AtomicLong RETRY_COUNT = new AtomicLong();
    private static Session session;

    public SocketClient() {}

    public static SocketClient getInstance() {
        if (INSTANCE == null) {
            synchronized (SocketClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SocketClient();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 打开连接
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("connection to the DRM is successfully: {}", drmInfo);
        SocketClient.session = session;
    }

    /**
     * 接收消息,每个消息都启动新线程处理
     */
    @OnMessage
    public void onMessage(String text) {
        log.info("receive server msg: {}", text);
        AgentParam agentParam = JSONObject.parseObject(text).toJavaObject(AgentParam.class);
        String execute;
        try {
            execute = CompletableFuture.supplyAsync(() -> ExecuteUtil.execute(agentParam)).get(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            execute = e.getMessage();
        }

        CallBack callBack = CallBack.builder()
                .ip(agentParam.getAgentIp())
                .key(agentParam.getFlag())
                .result(execute)
                .bsId(agentParam.getBsId())
                .drmActive(agentParam.isDrmActive())
                .build();

        send(JSON.toJSONString(callBack));
    }

    /**
     * 异常处理
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClosing()  {
        try {
            session.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        log.warn("drm关闭连接，正在重试...");
        restartSession();

    }



    public void init() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri1 = URI.create(drmInfo);
        try {
            session = container.connectToServer(SocketClient.class, uri1);
        } catch (Exception e) {
            if (e instanceof DeploymentException || e instanceof IOException) {
                log.warn("与drm断开连接，正在重试...");
                restartSession();
            } else {
                log.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 主动发送消息
     */
    public void send(String json) {
        log.info("send msg {}", json);
        session.getAsyncRemote().sendText(json);
    }

    private void restartSession() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri1 = URI.create(drmInfo);
        boolean success = Boolean.FALSE;
        try {
            session = container.connectToServer(SocketClient.class, uri1);
            success = Boolean.TRUE;
        } catch (Exception e) {
            if (e instanceof DeploymentException || e instanceof IOException) {
                log.info("重试第{}次失败", RETRY_COUNT.addAndGet(1));
            } else {
                log.warn(e.getMessage(), e);
            }
        } finally {
            if (!success) {
                try {
                    TimeUnit.SECONDS.sleep(I2AgentEnv.DRM_RETRY_INTERVAL);
                } catch (InterruptedException ignored) {}
                restartSession();
            } else {
               RETRY_COUNT.set(0);
            }

        }
    }

}