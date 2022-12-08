package com.info2soft.utils;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加解密算法 <br/>
 * 2.SHA-256摘要 <br/>
 */
public class EncryptUtil {

    /**
     * 密钥
     */
    private static final String DEFAULT_K = "@#$%^6a7";

    /**
     * 输出明文按sha-256加密后的密文
     *
     * @param inputStr 明文
     * @return
     */
    public static String encryptSha256(String inputStr) {
        try {
            byte[] digest = DigestUtil.sha256(inputStr);
            return new String(Base64.encodeBase64(digest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对称解密算法
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String decrypt(String message) {
        return aesDecrypt(message, DEFAULT_K);
    }

    /**
     * 对称加密算法
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String encrypt(String message) {
        return aesEncrypt(message, DEFAULT_K);
    }

    private static SecretKey getSecretKey(String password) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(SymmetricAlgorithm.AES.getValue());
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        keyGen.init(128, secureRandom);
        return keyGen.generateKey();
    }

    /**
     * aes解密-xx位
     */
    public static String aesDecryptBase64(String encryptContent, String key) {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), SymmetricAlgorithm.AES.getValue());
        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
        return symmetricCrypto.decryptStrFromBase64(encryptContent);
    }

    /**
     * aes解密-128位
     */
    public static String aesDecrypt(String encryptContent, String password, Boolean isBase64) {
        try {
            SecretKey secretKey = getSecretKey(password);
            SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
            return isBase64 ? symmetricCrypto.decryptStrFromBase64(encryptContent) : symmetricCrypto.decryptStr(encryptContent);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * aes解密-128位
     */
    public static String aesDecrypt(String encryptContent, String password) {
        return aesDecrypt(encryptContent, password, false);
    }

    /**
     * aes加密-128位
     */
    public static String aesEncrypt(String content, String password, Boolean rBase64) {
        try {
            SecretKey secretKey = getSecretKey(password);
            SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
            return rBase64 ? symmetricCrypto.encryptBase64(content) : symmetricCrypto.encryptHex(content);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }
    /**
     * aes加密-128位
     */
    public static String aesEncrypt(String content, String password) {
        return aesEncrypt(content, password, false);
    }
}
