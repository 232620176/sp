package com.hydra.core.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapUtil {
	private MapUtil(){throw new UnsupportedOperationException();}
	
	public static <T, K, V> T get(Map<K, V> map, K key){
		return CommonUtil.transform(map.get(key));
	}
	
	public static <K, V> Map<K, V> getMap(){
		return new HashMap<>(16);
	}
	
	public static <K, V> Map<K, V> getMap(int init){
		return new HashMap<>(init);
	}
	
	public static<K, V> Map<K, V> removeEmptyValue(Map<K, V> map){
		Map<K, V> res = getMap();
		Set<K> keySet = map.keySet();
		for(K k : keySet){
			V v = map.get(k);
			if(!StringUtil.isEmpty(v)){
				if(v instanceof String){
					String sv = (String)v;
					if(sv.trim().length() > 0){
						res.put(k, v);
					}
				}else{
					res.put(k, v);
				}
			}
		}
		return res;
	}
	
	public static <K, V> void rename(Map<K, V> map, K key1, K key2){
		Set<K> keySet = map.keySet();
		if(keySet.contains(key1)){
			map.put(key2, map.get(key1));
			map.remove(key1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> str2Map(String str) {
		Map<String, Object> map;
		try {
			map = JsonUtil.objectFromJson(str, Map.class);
			return map;
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static <T> Map<?, ?> toMap(T t){
		if(null == t){
			return  MapUtil.getMap();
		}
		Map<?, ?> tmp = new BeanMap(t);
		Map<String, Object> res = getMap();
		Set<?> keySet = tmp.keySet();
		for(Object k : keySet){
			if("class".equals(k)){
				continue;
			}
			Object v = tmp.get(k);
			if(null != v && !"null".equals(v)){
				res.put(k.toString(), v);
			}
		}
		return res;
	}
	
	public static <T> T toObject(Map<String, Object> map, Class<T> cla){
		if(null == map){
			return null;
		}else{
			T t = null;
			try {
				t = cla.newInstance();
				BeanUtils.populate(t, map);
			} catch (Exception e) {
				logger.error("{}", e);
			}
			return t;
		}
	}
	
	private static Logger logger = LoggerFactory.getLogger(MapUtil.class);
}
