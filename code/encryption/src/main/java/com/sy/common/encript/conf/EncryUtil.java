package com.sy.common.encript.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class EncryUtil {
	private EncryUtil(){}

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(EncryUtil.class);
	/**
	 * 生成RSA签名
	 * @param map map
	 * @param privateKey privateKey
	 * @return 结果集
	 */
	public static String handleRSA(Map<String, Object> map,
								   String privateKey) {
		StringBuilder sbuffer = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String signTemp = sbuffer.toString();

		String sign = "";
		if (!StringUtils.isEmpty(privateKey)) {
			sign = RSA.sign(signTemp, privateKey);
		}
		return sign;
	}

	/**
	 * 返回的结果进行验签
	 * 
	 * @param data
	 *            业务数据密文
	 * @param encryptKey
	 *            对ybAesKey加密后的密文
	 * @param clientPublicKey
	 *            客户端公钥
	 * @param serverPrivateKey
	 *            服务器私钥
	 * @return 验签是否通过
	 */
	public static boolean checkDecryptAndSign(String data, String encryptKey,
			String clientPublicKey, String serverPrivateKey){

		/** 1.使用serverPrivateKey解开aesEncrypt。 */
		String aesKey = "";
		try {
			aesKey = RSA.decrypt(encryptKey, serverPrivateKey);

			/** 2.用aeskey解开data。取得data明文 */
			String realData =  AES.decryptFromBase64(data, aesKey);
			LinkedHashMap<String, Object> contentMap = JSON.parseObject(realData, LinkedHashMap.class, Feature.OrderedField);
			/** 3.取得data明文sign。 */
			String sign = StringUtils.trimWhitespace(contentMap.get("sign").toString());
			/** 4.获取要验证数据 */
			StringBuilder signData=new StringBuilder();
			for (Map.Entry<String,Object> entry:contentMap.entrySet()) {
				if(!"sign".equals(entry.getKey())){
					String value= JSONObject.toJSONString(contentMap.get(entry.getKey()), SerializerFeature.WriteMapNullValue) ;
					signData.append(value);
				}
			}
			String signDataSign=signData.toString();
			/** 5. result为true时表明验签通过 */

			return RSA.checkSign(signDataSign, sign,
					clientPublicKey);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return false;
			}
	}

}
