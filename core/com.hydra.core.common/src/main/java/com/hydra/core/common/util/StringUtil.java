package com.hydra.core.common.util;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * <p>Title: check</p>
	 * <p>Description: 按指定pattern检查目标字符串str格式是否合法</p>
	 * @param str 目标字符串
	 * @param pattern 指定样式
	 * @return boolean
	 */
	public static boolean check(String str, String pattern){
		Pattern pat = Pattern.compile(pattern);
		Matcher match = pat.matcher(str.trim());
		return match.matches();
	}
	
	/**
	 * <p>Title: isEmpty</p>
	 * <p>Description: 判断一个对象是否为空</p>
	 * @param str 目标对象
	 * @return boolean
	 */
	public static boolean isEmpty(Object str) {
		if(str instanceof String){
			return ((String) str).trim().length() == 0;
		}
		return str == null;
	}
	
	/**
	 * <p>Title: isEmpty</p>
	 * <p>Description: 判断一个字符串是否为空</p>
	 * @param str 目标字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
	
	/**
	 * <p>Title: isNotEmpty</p>
	 * <p>Description: 判断一个字符串是否不为空</p>
	 * @param str 目标字符串
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * <p>Title: createRandom</p>
	 * <p>Description: 获取指定长度的随机字符串，字符串取值范围：HEXADECIMAL_CHAR_UPPER</p>
	 * @param length 指定长度
	 * @return String 随机字符串
	 */
	public static String createRandom(int length) {
		Random random = RandomUtil.getRandom();
		StringBuilder result = new StringBuilder();
		int len = HEXADECIMAL_CHAR_UPPER.length - 1;
		for (int i = 0; i < length; i++) {
			int itmp = random.nextInt(len);
			char ctmp = HEXADECIMAL_CHAR_UPPER[itmp];
			result.append(ctmp);
		}
		return result.toString();
	}
	
	/**
	 * <p>Title: getGuid</p>
	 * <p>Description: TODO</p>
	 * @return String
	 */
	public static String getGuid(){
		String guid = UUID.randomUUID().toString();
		return guid.replace("-", "");
	}
	
	/**
	 * <p>Title: firstCharToUpperCase</p>
	 * <p>Description: 首字母小写</p>
	 * @param name
	 * @return String
	 */
	public static String firstCharToLowerCase(String name){
		StringBuilder sb = new StringBuilder();
		sb.append(name.substring(0, 1).toLowerCase());
		sb.append(name.substring(1));
		return sb.toString();
	}
	
	/**
	 * <p>Title: firstCharToUpperCase</p>
	 * <p>Description: 首字母大写</p>
	 * @param name
	 * @return String
	 */
	public static String firstCharToUpperCase(String name){
		StringBuilder sb = new StringBuilder();
		sb.append(name.substring(0, 1).toUpperCase());
		sb.append(name.substring(1));
		return sb.toString();
	}
	
	public static final char[] HEXADECIMAL_CHAR = { '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public static final char[] HEXADECIMAL_CHAR_UPPER = { '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
	public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	// 静态工具类，防误生成
	private StringUtil(){throw new UnsupportedOperationException();}
}
