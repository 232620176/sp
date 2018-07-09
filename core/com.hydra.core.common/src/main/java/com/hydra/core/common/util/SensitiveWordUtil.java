package com.hydra.core.common.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hydra.core.common.cons.CONSMSG;
import com.hydra.core.common.cons.Dict;

/**
 * 敏感词处理工具 - DFA算法实现
 * 
 * @author Hydra wangshuang@lakala.com
 * @since 2018年5月18日
 */
public class SensitiveWordUtil {
	/**
	 * 初始化敏感词库，构建DFA算法模型
	 * @param sensitiveWordSet
	 */
	public static synchronized void init(Set<String> sensitiveWordSet) {
		initSensitiveWordMap(sensitiveWordSet);
	}
	
	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return 如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes"})
	public static int checkSensitiveWord(String txt, int beginIndex, int matchType){
		//敏感词结束标识位：用于敏感词只有1位的情况
		boolean flag = false;
		//匹配标识数默认为0
		int matchFlag = 0;
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length(); i++){
			word = txt.charAt(i);
			//获取指定key
			nowMap = CommonUtil.transform(nowMap.get(word));
			if(nowMap != null){
				//存在，则判断是否为最后一个
				//找到相应key，匹配标识+1
				matchFlag++;
				//如果为最后一个匹配规则,结束循环，返回匹配标识数
				if(CONSMSG.VALUE_1.equals(nowMap.get(Dict.IS_END))){
					//结束标志位为true
					flag = true;
					//最小规则，直接返回,最大规则还需继续查找
					if(MIN_MATCH_TYPE == matchType){
						break;
					}
				}
			}else{
				//不存在，直接返回
				break;
			}
		}
		//长度必须大于等于1，为单词
		if(matchFlag < 2 || !flag){
			matchFlag = 0;
		}
		return matchFlag;
	}
	
	/**
	 * 获取文字中的敏感词
	 * @param txt 文字
	 * @param matchType 匹配规则：1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 1.0
	 */
	public static Set<String> getSensitiveWords(String txt , int matchType){
		Set<String> sensitiveWords = new HashSet<String>();
		for(int i = 0; i < txt.length(); i++){
			//判断是否包含敏感字符
			int length = checkSensitiveWord(txt, i, matchType);
			//存在,加入list中
			if(length > 0){
				sensitiveWords.add(txt.substring(i, i+length));
				//减1的原因，是因为for会自增
				i = i + length - 1;
			}
		}
		return sensitiveWords;
	}
	
	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 * 	isEnd = 0
	 * 	国 = {<br>
	 * 		isEnd = 1
	 * 		人 = {
	 * 			isEnd = 0
	 * 			民 = {isEnd = 1}
	 * 		}
	 * 		男  = {
	 * 			isEnd = 0
	 * 			人 = {isEnd = 1}
	 * 		}
	 * 	}
	 * }
	 * 五 = {
	 * 	isEnd = 0
	 * 	星 = {
	 * 		isEnd = 0
	 * 		红 = {
	 * 			isEnd = 0
	 * 			旗 = {isEnd = 1}
	 * 		}
	 * 	}
	 * }
	 * @param keyWordSet 敏感词库
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void initSensitiveWordMap(Set<String> keyWordSet) {
		//初始化敏感词容器，减少扩容操作
		sensitiveWordMap = MapUtil.getMap(keyWordSet.size());
		String key = null;  
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			//关键字
			key = iterator.next();
			nowMap = sensitiveWordMap;
			for(int i = 0; i < key.length(); i++){
				//转换成char型
				char keyChar = key.charAt(i);
				//获取
				Object wordMap = nowMap.get(keyChar);
				//如果存在该key，直接赋值
				if(wordMap != null){
					nowMap = CommonUtil.transform(wordMap);
				}else{
					//不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, String>();
					//不是最后一个
					newWorMap.put(Dict.IS_END, CONSMSG.VALUE_0);
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				if(i == key.length() - 1){
					//最后一个
					nowMap.put(Dict.IS_END, CONSMSG.VALUE_1);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Set<String> words = new HashSet<>();
		words.add("三级片");
		words.add("自杀");
		words.add("红客联盟");
		words.add("手机卡复制器");
		words.add("主人");
		words.add("主人公");
		init(words);
		
		System.out.println("敏感词的数量：" + sensitiveWordMap.size());
		String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
						+ "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
						+ "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三.级.片 深人静的晚上，关上电话静静的发呆着。";
		System.out.println("待检测语句字数：" + string.length());
		long beginTime = System.nanoTime();
		Set<String> set = getSensitiveWords(string, MIN_MATCH_TYPE);
		long endTime = System.nanoTime();
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		System.out.println("总共消耗时间为：" + (endTime - beginTime));
		System.out.println("总共消耗时间为：" + (endTime - beginTime)/1000000);
	}
	
	/**
	 * 敏感词集合
	 */
	@SuppressWarnings("rawtypes")
	public static Map sensitiveWordMap;
	
	public static final int MAX_MATCH_TYPE = 0; //最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]
	public static final int MIN_MATCH_TYPE = 1; //最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
	public SensitiveWordUtil() {throw new UnsupportedOperationException();}
}
