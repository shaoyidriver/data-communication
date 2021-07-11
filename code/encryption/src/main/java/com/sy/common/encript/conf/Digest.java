package com.sy.common.encript.conf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Digest {
	private Digest(){}

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(Digest.class);
	/**
	 * ENCODE
	 */
	public static final String ENCODE = "UTF-8";

	/**
	 * signMD5
	 * @param aValue aValue
	 * @param encoding encoding
	 * @return 结果集
	 */
	public static String signMD5(String aValue, String encoding) {
		try {
			byte[] input = aValue.getBytes(encoding);
			MessageDigest md = MessageDigest.getInstance("MD5");
			return ConvertUtils.toHex(md.digest(input));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * hmacSign
	 * @param aValue aValue
	 * @return 结果集
	 */
	public static String hmacSign(String aValue) {
		try {
			byte[] input = aValue.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			return ConvertUtils.toHex(md.digest(input));
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * hmacSign
	 * @param aValue aValue
	 * @param aKey aKey
	 * @return 结果集
	 */
	public static String hmacSign(String aValue, String aKey) {
		return hmacSign(aValue, aKey, ENCODE);
	}

	/**
	 * hmacSign
	 * @param aValue aValue
	 * @param aKey aKey
	 * @param encoding encoding
	 * @return 结果集
	 */
	public static String hmacSign(String aValue, String aKey, String encoding) {
		byte[] keyb;
		byte[] value;
		byte[] kipad = new byte[64];
		byte[] kopad = new byte[64];
		try {
			value = aValue.getBytes(encoding);
			keyb = aKey.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = aValue.getBytes();
			keyb = aKey.getBytes();
		}
		Arrays.fill(kopad, keyb.length, 64, (byte) 92);
		Arrays.fill(kipad, keyb.length, 64, (byte) 54);
		for (int i = 0; i < keyb.length; i++) {
			kopad[i] = (byte) (keyb[i] ^ 0x5c);
			kipad[i] = (byte) (keyb[i] ^ 0x36);
		}

		MessageDigest mds = null;
		try {
			mds = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		mds.update(value);
		mds.update(kipad);
		byte[] dg = mds.digest();
		mds.reset();
		mds.update(dg, 0, 16);
		mds.update(kopad);
		dg = mds.digest();
		return ConvertUtils.toHex(dg);
	}

	/**
	 * hmacSHASign
	 * @param aValue aValue
	 * @param aKey aKey
	 * @param encoding encoding
	 * @return 结果集
	 */
	public static String hmacSHASign(String aValue, String aKey, String encoding) {
		byte[] kipad = new byte[64];
		byte[] kopad = new byte[64];
		byte[] keyb;
		byte[] value;
		try {
			keyb = aKey.getBytes(encoding);
			value = aValue.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		Arrays.fill(kipad, keyb.length, 64, (byte) 54);
		Arrays.fill(kopad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			kipad[i] = (byte) (keyb[i] ^ 0x36);
			kopad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		md.update(kipad);
		md.update(value);
		byte[] dg = md.digest();
		md.reset();
		md.update(kopad);
		md.update(dg, 0, 20);
		dg = md.digest();
		return ConvertUtils.toHex(dg);
	}

	/**
	 * digest
	 * @param aValue aValue
	 * @return 结果集
	 */
	public static String digest(String aValue) {
		return digest(aValue, ENCODE);

	}

	/**
	 * digest
	 * @param aValue aValue
	 * @param encoding encoding
	 * @return 结果集
	 */
	public static String digest(String aValue, String encoding) {
		String newAValue = aValue.trim();
		byte[] value;
		try {
			value = newAValue.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = newAValue.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return ConvertUtils.toHex(md.digest(value));
	}

	/**
	 * digest
	 * @param aValue aValue
	 * @param alg alg
	 * @param encoding encoding
	 * @return 结果集
	 */
	public static String digest(String aValue, String alg, String encoding) {
		String newAValue = aValue.trim();
		byte[] value;
		try {
			value = newAValue.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = newAValue.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(alg);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return ConvertUtils.toHex(md.digest(value));
	}

	/**
	 * udpSign
	 * @param aValue aValue
	 * @return 结果集
	 */
	public static String udpSign(String aValue) {
		try {
			byte[] input = aValue.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Base64.encode(md.digest(input)), ENCODE);
		} catch (Exception e) {
			return null;
		}
	}

}
