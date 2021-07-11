package com.sy.common.encript;

import com.sy.common.encript.conf.AES;
import com.sy.common.encript.conf.EncryUtil;
import com.sy.common.encript.conf.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EncryptUtils {
    private EncryptUtils(){}

    /**
     * 日志
     */
    public static final Logger logg = LoggerFactory.getLogger(EncryptUtils.class);

    /**
     * serverPrivateKey
     */
    static   String serverPrivateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALO9U5gAzl7ht2Oh\n" +
            "+59iOeNJfaNj2xjyWjgm5U8+bjFXN62jQfEZ7VSOUlnU1NgTPYSzAq8W3CRE0QtA\n" +
            "6IQqOrWF2oIT4R3s4PkCk2VhIihYXCUpGtm+BgOJKnJKw0KEoenFvDruUIcruuTe\n" +
            "6LRdgWGTQZKVGyBTuF23XlD392MBAgMBAAECgYEAnYLUtug98bgsVgulzk7uG9hT\n" +
            "WdQwNPRtS7gKTB+RwXuFN41SRE9MREVUtZRl0dVKm/ej6nxF/yKvbwU6vlejiefa\n" +
            "jKnxe4mN7jY7UfcCfr+IHvmSx2rZrpYbynrfjY1TkcjAXIfRDIpdpZS6VoakF+0s\n" +
            "EcmekpsOXRJDjMBwtIECQQDc8Y/05+R+MJGRYuusZahIQqgTnp9EeVfguk/ic9so\n" +
            "RW6PxLKVgG1ES8nYoPOw51Stv2TS95ci7Tjw3s6rUr61AkEA0EIcNwQu3RWkgigP\n" +
            "EtJw3OXA1FzzWDPxcdUnwyljhgGyLbhSAD7OAVlIOvMnVXAoIoET831zUvN5W2je\n" +
            "mq12nQJBANdUlniZiJdzvmtzfT0H1nqWfDii35ZcrMjYzAJn56/X+2kiyunpwJRP\n" +
            "SOlX9Cj1TBIJ9rmllCb2DAi1HbbbbgECQHWPTCQWXWn55KKAgHnMh1965o46Zje9\n" +
            "Iqdyfv6hJ4gm/vDmIov26A1E3AxC5EpLhKxtltiVUXta63+ZREemcdUCQD+iiIw0\n" +
            "0XQwPGHZXfOKk1PIXJulhzp83yyf67susrXwSMxBh/9NP0/o6jliXkGl9fP9kMLJ\n" +
            "pm1hx7gs2kPToB0=";
    /**
     * clientPublicKey
     */
    public static final String CLIRNT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmwvsh81sBgDp7RbIw7VzmOqX0\n" +
            "xMri/1GmGt34jAi6m69GRmXBxDHZ2JmxM9XLyCF0NNRY8miptcq/981S1TqlCyQI\n" +
            "bfVtDD8e81YBjR/E7KP+QDuP8eIBFBqlwDu1Ro6YPuWwHMyNPBLfkFEzgUbAlpyL\n" +
            "XJkSVW0BuDwPScc7mQIDAQAB".replace("\n","");

    /**
     * 将客户端请求数据进行解密操作
     * @param data data
     * @param encryptkey encryptkey
     * @param isSign isSign
     * @return
     */
    public static   String requestDeclassifiedData(String data, String encryptkey, boolean isSign)  {
        String resultData="";
        try{
            if (isSign){
                // 验签
                boolean passSign = EncryUtil.checkDecryptAndSign(data,
                        encryptkey,CLIRNT_PUBLIC_KEY, serverPrivateKey);
                if (passSign) {
                    // 验签通过
                    String aeskey = RSA.decrypt(encryptkey,serverPrivateKey);
                    String dataJson = AES.decryptFromBase64(data,aeskey);
                    resultData=dataJson;
                } else {
                    logg.error("签名校验失败");
                }
            }else{
                String aeskey = RSA.decrypt(encryptkey,serverPrivateKey);
                String dataJson = AES.decryptFromBase64(data,aeskey);
                resultData=dataJson;
            }
        }catch (Exception ex){
            logg.error(ex.getMessage());
        }
        return  resultData;
    }
}
