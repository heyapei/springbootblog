//当前在用
package com.hyp.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/11/13 12:26
 * @Description: TODO 加密算法
 */
@Slf4j
public class EnDecryption {


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] deCryptBase64(String key) {
        try {
            return (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("EnDecryption.deCryptBase64.Exception:" + e.toString());
        }
        return null;
    }

    /**
     * BASE64加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public String enCryptBase64(String str) {
        byte[] key = new byte[0];
        try {
            key = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("EnDecryption.enCryptBase64.Exception:" + e.toString());
        }
        return (new BASE64Encoder()).encodeBuffer(key);
    }


    /**
     * md5加密-32位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    public String md5_32Low(String encryptStr) {

        return encrypt32(encryptStr).toLowerCase();
    }

    /**
     * md5加密-32位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    public String md5_32Upp(String encryptStr) {

        return encrypt32(encryptStr).toUpperCase();
    }

    /**
     * md5加密-16位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    public String md5_16Low(String encryptStr) {

        return encrypt32(encryptStr).substring(8, 24).toLowerCase();
    }

    /**
     * md5加密-16位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    public String md5_16Upp(String encryptStr) {

        return encrypt32(encryptStr).substring(8, 24).toUpperCase();
    }

    /**
     * 加密-32位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    private String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes("utf-8"));
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            log.info("EnDecryption.encrypt32.Exception:" + e.toString());
            throw new RuntimeException(e);
        }
        return encryptStr;
    }


}
