package com.sy.common.encript.conf;

/*
 --------------------------------------------**********--------------------------------------------

 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal Rivest，Adi Shamir和Len Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，是一个支持变长密钥的公共密钥算法，需要加密的文件快的长度也是可变的!

 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。它的算法如下：

 1.找两个非常大的质数p和q（通常p和q都有155十进制位或都有512十进制位）并计算n=pq，k=(p-1)(q-1)。

 2.将明文编码成整数M，保证M不小于0但是小于n。

 3.任取一个整数e，保证e和k互质，而且e不小于0但是小于k。加密钥匙（称作公钥）是(e, n)。

 4.找到一个整数d，使得ed除以k的余数是1（只要e和n满足上面条件，d肯定存在）。解密钥匙（称作密钥）是(d, n)。

 加密过程： 加密后的编码C等于M的e次方除以n所得的余数。

 解密过程： 解密后的编码N等于C的d次方除以n所得的余数。

 只要e、d和n满足上面给定的条件。M等于N。

 --------------------------------------------**********--------------------------------------------
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    private RSA() {
    }

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(RSA.class);

    /**
     * 加密方法 source： 源数据
     *
     * @param source    source
     * @param publicKey publicKey
     * @return 结果集
     */
    public static String encrypt(String source, String publicKey) {
        String result = "";
        try {
            Key key = getPublicKey(publicKey);
            /** 得到Cipher对象来实现对源数据的RSA加密 */
            Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = source.getBytes();
            /** 执行加密操作 */
            byte[] b1 = cipher.doFinal(b);
            String encrypt = new String(Base64.encode(b1),
                    ConfigureEncryptAndDecrypt.CHAR_ENCODING);

            return new String(Base64.encode(encrypt.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return result;
        }

    }

    /**
     * 解密算法 cryptograph:密文
     *
     * @param crypToGraph cryptograph
     * @param privateKey  privateKey
     * @return
     * @throws InvalidKeySpecException      InvalidKeySpecException
     * @throws NoSuchAlgorithmException     NoSuchAlgorithmException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws NoSuchPaddingException       NoSuchPaddingException
     * @throws InvalidKeyException          InvalidKeyException
     * @throws BadPaddingException          BadPaddingException
     * @throws IllegalBlockSizeException    IllegalBlockSizeException
     */
    public static String decrypt(String crypToGraph, String privateKey) throws
            InvalidKeySpecException,
            NoSuchAlgorithmException,
            UnsupportedEncodingException,
            NoSuchPaddingException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        String newCryptograph = new String(Base64.decode(crypToGraph.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING)));
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(newCryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * sign
     *
     * @param content    content
     * @param privateKey privateKey
     * @return 结果集
     */
    public static String sign(String content, String privateKey) {
        String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * checkSign
     *
     * @param content   content
     * @param sign      sign
     * @param publicKey publicKey
     * @return 结果集
     */
    public static boolean checkSign(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode2(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            return signature.verify(Base64.decode2(sign));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

}