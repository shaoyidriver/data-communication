package com.sy.common.encript.conf;


import java.security.SecureRandom;

public class SecureRandomUtil {
	private SecureRandomUtil(){}
	/**
	 * random
	 */
	public static final SecureRandom random = new SecureRandom();

	/**
	 * getRandom
	 * @param length length
	 * @return
	 */
	public static String getRandom(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
			if (isChar) { // 字符串
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				ret.append((char) (choice + random.nextInt(26)));
			} else { // 数字
				ret.append(Integer.toString(random.nextInt(10)));
			}
		}
		return ret.toString();
	}

	/**
	 * getRandomNum
	 * @param length length
	 * @return 结果集
	 */
	public static String getRandomNum(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			ret.append(Integer.toString(random.nextInt(10)));
		}
		return ret.toString();
	}
	
}
