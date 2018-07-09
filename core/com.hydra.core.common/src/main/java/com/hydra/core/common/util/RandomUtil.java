package com.hydra.core.common.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 * 用于随机服务的工具
 * <p>
 * Created on 2017年6月22日 上午11:18:53
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2017年6月22日
 */
public class RandomUtil {
	/**
	 * <p>Title: getRandom</p>
	 * <p>Description: 返回Random实例</p>
	 * @return Random
	 */
	public static Random getRandom(){
		return ThreadLocalRandom.current();
	}
	
	// 静态工具类，防误生成
	private RandomUtil(){throw new UnsupportedOperationException();}
}
