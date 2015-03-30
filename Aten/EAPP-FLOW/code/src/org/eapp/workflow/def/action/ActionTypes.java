/**
 * 
 */
package org.eapp.workflow.def.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmException;


/**
 * 动作类型对照
 * @author 卓诗垚
 * @version 1.0
 */
public class ActionTypes {
	private static Map<String, Class<?>> actionTypes;//动作类型名与class映射
	private static Map<Class<?>, String> actionNames;//动作class与类型名映射
	
	static {
		//从配置中取得动作类型
		WfmConfiguration wfmConfiguration = WfmConfiguration.getInstance();
		Map<String, String> configTypes = wfmConfiguration.getResources().getMap("action.types");
		if (configTypes == null) {
			throw new WfmException("no enable action type");
		}
		//组成动作类型名与class映射
		actionTypes = new HashMap<String, Class<?>>();
		Iterator<String> it = configTypes.keySet().iterator();
		String key = null;
		while (it.hasNext()) {
			try {
				key = it.next();
		        Class<?> nodeClass = Class.forName(configTypes.get(key));
		        actionTypes.put(key, nodeClass);
			} catch (Exception e) {
		    	e.printStackTrace();
			}
		}
		//组成动作class与类型名映射
		actionNames = new HashMap<Class<?>, String>();
		Iterator<Map.Entry<String,Class<?>>> iter = actionTypes.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry<String,Class<?>> entry = (Map.Entry<String,Class<?>>) iter.next();
	    	actionNames.put(entry.getValue(), entry.getKey());
	    }
	}
	
	public static Set<String> getActionTypes() {
		return actionTypes.keySet();
	}

	public static Set<Class<?>> getActionNames() {
		return actionNames.keySet();
	}

	/**
	 * 通过动作名称取得对应的Class
	 * @param name
	 * @return 动作类
	 */
	public static Class<?> getActionType(String name) {
		return (Class<?>) actionTypes.get(name);
	}

	/**
	 * 通过Class取得对应的动作名称
	 * @param type
	 * @return 动作名称
	 */
	public static String getActionName(Class<?> type) {
		return (String) actionNames.get(type);
	}
	
	/**
	 * 是正确动作名称
	 * @param name
	 * @return boolean
	 */
	public static boolean hasActionName(String name) {
		return actionTypes.containsKey(name);
	}
	 
	public static void main(String[] a) {
		System.out.println(getActionType("action"));
		System.out.println(getActionName(org.eapp.workflow.def.Action.class));
	}
}
