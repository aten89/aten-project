package org.eapp.workflow.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从配置文件中解析出来的元素，存放在此类中
 * @author 卓诗垚
 * @version 1.0
 */
public class Resources {
	Map<String, String> stringMap;//存放String 值
	Map<String, Integer> intMap;//存放int值
	Map<String, Boolean> booleanMap;//存放boolean值
	Map<String, List<String>> listMap;//存放list值
	Map<String, Map<String,String>> maps;//存放map值
	//配置文件中还有其他类型的值往这添加
	
	public Resources() {
		stringMap = new HashMap<String, String>();
		intMap = new HashMap<String, Integer>();
		booleanMap = new HashMap<String, Boolean>();
		listMap = new HashMap<String, List<String>>();
		maps = new HashMap<String, Map<String,String>>();
	}
	
	/**
	 * 取得配置文件中String值 
	 * @param key
	 * @return String
	 */
	public String getString(String key) {
		if (key == null) {
			return null;
		}
		return stringMap.get(key);
	}
	
	/**
	 * 取得配置文件中int值 
	 * @param key
	 * @return int
	 */
	public int getInt(String key) {
		if (key == null) {
			return 0;
		}
		Integer i = intMap.get(key);
		return i == null ? 0 : i.intValue();
	}
	
	/**
	 * 取得配置文件中boolean值 
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		if (key == null) {
			return false;
		}
		Boolean b = booleanMap.get(key);
		return b == null ? false : b.booleanValue();
	}
	
	/**
	 * 取得配置文件中List值
	 * @param key
	 * @return List
	 */
	public List<String> getList(String key) {
		if (key == null) {
			return null;
		}
		return listMap.get(key);
		
	}
	
	/**
	 * 取得配置文件中Map值
	 * @param key
	 * @return Map
	 */
	public Map<String, String> getMap(String key) {
		if (key == null) {
			return null;
		}
		return maps.get(key);
	}
	
	void addValue(String key, String value) {
		stringMap.put(key, value);
	}
		
	void addValue(String key, Integer value) {
		intMap.put(key, value);	
	}
	
	void addValue(String key, Boolean value) {
		booleanMap.put(key, value);
	}
	
	void addValue(String key, List<String> value) {
		listMap.put(key, value);
	}
	
	void addValue(String key, Map<String, String> value) {
		maps.put(key, value);
	}
	
}
