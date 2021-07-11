package com.sy.common.encript.conf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Description:
 *
 */
public class AES {
    /**
     * 日志
     */
    public static final Logger log = LoggerFactory.getLogger(AES.class);
    /**
     * AES 构造函数
     */
    private AES(){

    }

    /**
     * 加密
     * @param data 需要加密的内容
     * @param key 加密密码
     * @return
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        CheckUtils.notEmpty(data, "data");
        CheckUtils.notEmpty(key, "key");
        if (key.length != 16) {
            log.error("Invalid AES key length (must be 16 bytes)");
        }

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.AES_ALGORITHM);// 创建密码器
        IvParameterSpec iv = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, seckey, iv);// 初始化
        return cipher.doFinal(data); // 加密

    }

    /**
     * 解密
     * @param data 待解密内容
     * @param key 解密密钥
     * @return
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        CheckUtils.notEmpty(key, "key");
        CheckUtils.notEmpty(data, "data");
        if (key.length != 16) {
            log.error("Invalid AES key length (must be 16 bytes)");
        }

        SecretKeySpec secretKeys = new SecretKeySpec(key, "AES");
        byte[] enCodeFormat = secretKeys.getEncoded();
        SecretKeySpec seckeys = new SecretKeySpec(enCodeFormat, "AES");
        Cipher ciphers = Cipher.getInstance(ConfigureEncryptAndDecrypt.AES_ALGORITHM);// 创建密码器
        IvParameterSpec ivs = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        ciphers.init(Cipher.DECRYPT_MODE, seckeys, ivs);// 初始化
        return ciphers.doFinal(data); // 解密
    }

    /**
     * encryptToBase64
     * @param data data
     * @param key key
     * @return
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String encryptToBase64(String data, String key) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        byte[] valueByte = encrypt(data.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING), key.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING));
        String enc=ConvertUtils.byteToHexString(valueByte);

        return new String(Base64.encode(enc.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING)));



    }

    /**
     * decryptFromBase64
     * @param data data
     * @param key key
     * @return
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     */
    public static String decryptFromBase64(String data, String key) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String  originalData = new String(Base64.decode(data.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING)));
        byte[] dataByte=ConvertUtils.hexStringToBytes(originalData);
        byte[] valueByte = decrypt(dataByte, key.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING));
        return new String(valueByte, ConfigureEncryptAndDecrypt.CHAR_ENCODING);

    }


}
