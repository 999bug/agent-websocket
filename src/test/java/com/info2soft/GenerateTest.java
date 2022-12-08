package com.info2soft;

import com.info2soft.constant.ConfigFileGenerator;
import com.info2soft.utils.EncryptUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: Lisy
 * @Date: 2022/09/26/14:02
 * @Description:
 */
public class GenerateTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigFileGenerator.generator();
    }

    @Test
    public void  test() {
        String encrypt = EncryptUtil.encrypt("123");
        System.out.println("EncryptUtil.encrypt(\"977721Fsdfdfdfdfd\") = " + encrypt);
        System.out.println(encrypt.length());
    }

}
