package com.sy.common.encript;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sy.common.encript.conf.AES;
import com.sy.common.encript.conf.RSA;
import com.sy.common.encript.conf.SecureRandomUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

import static com.sy.common.encript.EncryptUtils.CLIRNT_PUBLIC_KEY;
import static com.sy.common.encript.EncryptUtils.serverPrivateKey;

@ControllerAdvice
public class ResponseEncryptAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 返回值加密 总开关
     */
    @Value("${encrypt.open:false}")
    private boolean encryptKey;
    /**
     * encrypt
     */
    private boolean encrypt;
    /**
     * encryptLocal
     */
    private static final ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //总开关打开 返回值判断注解加密
        encrypt = encryptKey && methodParameter.getMethod().isAnnotationPresent(Encrypt.class);
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Boolean status = encryptLocal.get();
        if (null != status && !status) {
            encryptLocal.remove();
            return body;
        }
        if (encrypt) {
            try {
                String content = JSON.toJSONString(body);
                if (!StringUtils.hasText(serverPrivateKey)) {
                    throw new NullPointerException("Please configure rsa.encrypt.privatekeyc parameter!");
                }
                Map<String,Object> dataMap = JSON.parseObject(content,Map.class);
                //随机生成AES密钥
                String aesKey = SecureRandomUtil.getRandom(16);
                //AES加密数据
                String data=dataMap.get("data")+"";
                String enData = AES.encryptToBase64(data, aesKey);
                // 使用RSA算法将自己随机生成的AESkey加密
                String encryptkey = RSA.encrypt(aesKey, CLIRNT_PUBLIC_KEY);
                dataMap.put("data",enData);
                dataMap.put("encryptkey",encryptkey);
                dataMap.put("level",1);
                return dataMap;
            } catch (Exception e) {
                JSONObject object = new JSONObject();
                object.put("code",500);
                object.put("msg","出现异常");
                object.put("data","");
                return object;
            }
        }
        return body;
    }
}
