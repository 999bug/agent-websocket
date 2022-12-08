package com.info2soft;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: Lisy
 * @Date: 2022/11/24/11:41
 * @Description:
 */
public class PortTest {


    @Test
    public void testPort() {
        boolean b = portDetection("192.168.46.11", 8082);
        System.out.println("b = " + b);
    }



    @Test
    public void testPing() throws Exception {
//        pingDetection();
        boolean ping = pingDetection("192.168.46.14");
        System.out.println("ping = " + ping);
    }


    private static boolean pingDetection(String ipAddress) {
        int timeOut = 3000;
        boolean status = Boolean.FALSE;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);

        } catch (IOException ignored) {}
        return status;
    }

    private static boolean portDetection(String ip, int port) {
        boolean result = Boolean.FALSE;
        try (Socket connect = new Socket()) {
            connect.connect(new InetSocketAddress(ip, port), 100);
            result = connect.isConnected();
        } catch (IOException ignored) {
        }
        return result;
    }
}
