package com.sy.common.encript.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Description: 加密工具类
 */
public class EncryptionUtil {
    private EncryptionUtil(){}

    /**
     * 日志
     */
    static Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

    /**
     * 对字符串进行md5加密
     *
     * @param str str
     * @return 结果集
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * 对字符串进行sha256加密
     *
     * @param str str
     * @return 结果集
     */
    public static String sha256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * 对字符串进行sha1加密
     *
     * @param str str
     * @return 结果集
     */
    public static String sha1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param data data
     * @return 结果集
     */
    public static String byteToHex(byte[] data) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : data) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
