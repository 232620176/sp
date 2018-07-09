package com.hydra.core.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * 数组操作工具类
 * <p>
 * Created on 2017年6月20日 下午5:36:24
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2017年6月20日
 */
public class ArrayUtil {
	
	/**
	 * <p>方法名: addElement</p>
	 * <p>描述: 将目标对象target加入到数组source中</p>
	 * @param <T> 数组source的类型
	 * @param source 需添加对象的数组
	 * @param target 需添加的对象
	 * @return T[]
	 */
	public static <T> T[] addElement(T[] source, T target){
		final T tTarget = target;
		return template(source, target, new Callable<T>() {
			@Override
			public void call(List<T> tmp) {
				tmp.add(tTarget);
			}
		});
	}
	
	/**
	 * <p>方法名: changeElement</p>
	 * <p>描述: 将数组source中的对象from替换为to，如from不存在则不进行操作</p>
	 * @param <T> 数组source的类型
	 * @param source 需做对象变更的数组
	 * @param from 需被替换的对象
	 * @param to 需用来替换的对象
	 * @return T[]
	 */
	public static <T> T[] changeElement(T[] source, T from, T to){
		final T tFrom = from;
		final T tTo = to;
		return template(source, from, new Callable<T>() {
			@Override
			public void call(List<T> tmp) {
				int index = tmp.indexOf(tFrom);
				if(index > -1){
					tmp.set(index, tTo);
				}
			}
		});
	}
	
	/**
	 * <p>方法名: contains</p>
	 * <p>描述: 数组source是否包含对象target</p>
	 * @param <T> 数组source的类型
	 * @param source 需检查的数组
	 * @param target 需检查的对象
	 * @return boolean
	 */
	public static <T> boolean contains(T[] source, T target){
		List<T> tmp = new ArrayList<T>(Arrays.asList(source));
		return tmp.contains(target);
	}
	
	/**
	 * <p>方法名: newArrayByArrayClass</p>
	 * <p>描述: 按数组类型clazz和数组长度length生成新数组</p>
	 * @param <T> clazz泛型的数组类型
	 * @param clazz 数组对象类型
	 * @param length 数组长度
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArrayByArrayClass(Class<T[]> clazz, int length){
		return (T[])Array.newInstance(clazz.getComponentType(), length);
	}
	
	/**
	 * <p>方法名: newArrayByClass</p>
	 * <p>描述: 按对象类型clazz和数组长度length生成新数组</p>
	 * @param <T> clazz泛型的对象类型
	 * @param clazz 对象类型
	 * @param length 数组长度
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArrayByClass(Class<T> clazz, int length){
		return (T[])Array.newInstance(clazz, length);
	}
	
	/**
	 * <p>方法名: removeElement</p>
	 * <p>描述: 删除数组source中的target对象</p>
	 * @param <T> 数组source的类型
	 * @param source 需做元素删除操作的数组
	 * @param target 需删除的对象
	 * @return T[]
	 */
	public static <T> T[] removeElement(T[] source, T target){
		final T tTarget = target;
		return template(source, target, new Callable<T>() {
			@Override
			public void call(List<T> tmp) {
				tmp.remove(tTarget);
			}
		});
	}
	
	private static<T> T[] template(T[] source, T target, Callable<T> call){
		List<T> tmp = new LinkedList<T>(Arrays.asList(source));
		call.call(tmp);
		@SuppressWarnings("unchecked")
		T[] res = newArrayByClass((Class<T>)target.getClass(), 0);
		return tmp.toArray(res);
	}
	
	interface Callable<T> {
		public void call(List<T> tmp);
	}
	
	// 静态工具类，防误生成
	private ArrayUtil(){throw new UnsupportedOperationException();}
}
