/**
 * 
 */
package org.eapp.workflow.def.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmException;


/**
 * 节点类型对照
 * @author 卓诗垚
 * @version 1.0
 */
public class NodeTypes {
	private static Map<String, Class<?>> nodeTypes;//节点类型名与class映射
	private static Map<Class<?>, String> nodeNames;//节点class与类型名映射
	
	static {
		//从配置中取得结点类型
		WfmConfiguration wfmConfiguration = WfmConfiguration.getInstance();
		Map<String, String> configTypes = wfmConfiguration.getResources().getMap("node.types");
		if (configTypes == null) {
			throw new WfmException("no enable note type");
		}
		//组成节点类型名与class映射
		nodeTypes = new HashMap<String, Class<?>>();
		Iterator<String> it = configTypes.keySet().iterator();
		String key = null;
		while (it.hasNext()) {
			try {
				key = it.next();
		        Class<?> nodeClass = Class.forName(configTypes.get(key));
		        nodeTypes.put(key, nodeClass);
			} catch (Exception e) {
		    	e.printStackTrace();
			}
		}
		//组成节点class与类型名映射
		nodeNames = new HashMap<Class<?>, String>();
		Iterator<Map.Entry<String,Class<?>>> iter = nodeTypes.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry<String,Class<?>> entry = (Map.Entry<String,Class<?>>) iter.next();
	    	nodeNames.put(entry.getValue(), entry.getKey());
	    }
	}
	
	public static Set<String> getNodeTypes() {
		return nodeTypes.keySet();
	}

	public static Set<Class<?>> getNodeNames() {
		return nodeNames.keySet();
	}

	/**
	 * 通过结点名称取得对应的Class
	 * @param name
	 * @return 节点类
	 */
	public static Class<?> getNodeType(String name) {
		return (Class<?>) nodeTypes.get(name);
	}

	/**
	 * 通过Class取得对应的结点名称
	 * @param type
	 * @return 节点名称
	 */
	public static String getNodeName(Class<?> type) {
		return (String) nodeNames.get(type);
	}
	
	public static void main(String[] a) {
		System.out.println(getNodeType("node"));
		System.out.println(getNodeName(org.eapp.workflow.def.Node.class));
	}
}
