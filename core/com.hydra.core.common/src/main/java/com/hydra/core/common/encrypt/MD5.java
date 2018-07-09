package com.hydra.core.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static String sign(String text, String key, String input_charset) {
		text = text + key;
		return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}
	
	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param sign 签名结果
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if(mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException 
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}
	
	/**
	 * MD5加密。32位
	 * 
	 * @param inStr
	 * @return
	 */
	public static String encrypt(String inStr) throws Exception {
		MessageDigest md5 = null;
		StringBuffer hexValue = new StringBuffer();
		try {
			md5 = MessageDigest.getInstance("MD5");
			char[] charArray = inStr.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}
			byte[] md5Bytes = md5.digest(byteArray);
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
		} catch (Exception e) {
			throw new Exception("MD5加密失败！");
		}
		return hexValue.toString();
	}
	
	/**
	 * MD5加密。32位
	 * 
	 * @param inStr
	 * @return
	 */
	public static String encrypt(Object objStr) throws Exception {
		MessageDigest md5 = null;
		StringBuffer hexValue = new StringBuffer();
		String inStr = ""; 
		try {
			if(objStr == null || "".equals(objStr.toString())) {
				throw new Exception("字符串为空，MD5加密失败！");
			} else {
				inStr = objStr.toString();
			}
			md5 = MessageDigest.getInstance("MD5");
			char[] charArray = inStr.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}
			byte[] md5Bytes = md5.digest(byteArray);
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
		} catch (Exception e) {
			throw new Exception("MD5加密失败！");
		}
		return hexValue.toString();
	}
}
