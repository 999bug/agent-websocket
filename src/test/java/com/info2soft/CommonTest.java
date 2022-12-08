package com.info2soft;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.info2soft.model.AgentCondition;
import com.info2soft.model.AgentParam;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lisy
 * @Date: 2022/11/21/18:26
 * @Description:
 */
public class CommonTest {

    @Test
    public void test() throws IOException, InterruptedException {
        String cron = "4 0/1 * * * ? ";
        lunchTask("111", cron);
//        lunchTask("222", "0/3 * * * * ? ");
//        lunchTask("333", "0/4 * * * * ? ");
        TimeUnit.SECONDS.sleep(4);
//        AgentSchedule.getInstance().stopScheduleTask("111");
        System.in.read();
    }


    @Test
    public void testAgentC() {
        String json;
        json = "";
        json = "{\"operator\":\"||\",\"lv1\":[{\"operater\":\"&&\",\"list\":[{\"type\":0,\"address\":\"192.168.46.11\",\"port\":1,\"status\":1}]},{\"operater\":\"&&\",\"list\":[{\"type\":0,\"address\":\"192.168.46.11\",\"port\":1,\"status\":1},{\"type\":0,\"address\":\"192.168.46.11\",\"port\":1,\"status\":1}]}]}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        AgentCondition agentC = JSONObject.parseObject(json, AgentCondition.class);
        System.out.println("");
    }

    @Test
    public void testAgentC_() {
        List<String> list = new ArrayList<>();
        list.add("32");
        list.add("33");

        List<String> list1 = new ArrayList<>(list);
        String remove = list.remove(0);
        System.out.println(list.size());
        int size = list1.size();
        System.out.println("size = " + size);
    }

    @Test
    public void testAgentC1() throws IOException, InterruptedException {
        Runnable runnable = () -> {
            while (true) {
                // dojobs

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {}
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("我运行了");
        TimeUnit.SECONDS.sleep(5);
        thread.interrupt();

    }

    private Runnable getRunnable() {
        return () -> {
            while (true) {

            }
        };
    }


    private void lunchTask(String bsId, String cron) {
        AgentParam agentParam = new AgentParam();
        agentParam.setBsId(bsId);
//        AgentSchedule.getInstance().startScheduleTask(agentParam, cron);
    }

    @Test
    public void testboolean() throws ScriptException {
//        String str = "value>8 || (value <= 5 && value > 1)";
//        String str1 = "(true && false) && (true)";
//        String str = "false && false || false && true ";
//        boolean result = calcExpression(str);
//        System.out.println("计算结果:" + result);
//        boolean a = false && false || true && false ;
//        System.out.println("a = " + a);
        String str = "123456YY&&";
        StringBuilder sb = new StringBuilder(str);
        int length = str.length();
        sb.delete(length -2, length);
        System.out.println(sb);
    }

    private boolean calcExpression(String expression) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            return (boolean) (Boolean) engine.eval(expression);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void map() {
        System.out.println("434985356204179457".length());
        Snowflake snowflake = IdUtil.createSnowflake(0, 0);
        long l = snowflake.nextId();
        System.out.println("l = " + l);
        System.out.println((l+"").length());
    }
}
