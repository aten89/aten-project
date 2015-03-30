package org.eapp.workflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eapp.workflow.WfmException;
import org.xml.sax.InputSource;


/**
 * 解析配置文件
 * @author 卓诗垚
 * @version 1.0
 */
public class ResourceReader {
	private Document document;
	private Resources resources;
	
	public ResourceReader(String resource) {
		InputStream resourceStream = null;
		try {
			resourceStream = ResourceReader.class.getResourceAsStream(resource);
			SAXReader saxReader = new SAXReader();//
			document = saxReader.read(new InputSource(resourceStream));
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (resourceStream != null) {
				try {
					resourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (document == null) {
			throw new WfmException("define xml parse error");
		}
		doRead();
	}
	
	/**
	 * 从document中读取并封装成Resources对像
	 */
	private void doRead() {
		resources = new Resources();
		if (document == null) {
			throw new WfmException("define xml parse error");
		}
		Element root = document.getRootElement();
	//	System.out.println(document.asXML());
		
		//读取String 参数
		readStringResources(root);
		
		//读取int 参数
		readIntResources(root);

		//读取boolean 参数
		readBooleanResources(root);
		
		//读取list 参数
		readListResources(root);
		
		//读取Map 参数
		readMapResources(root);
	}
	
	@SuppressWarnings("unchecked")
	private void readStringResources(Element root) {
		String key = null;
		String value = null;
		Element e = null;
		for (Iterator<Element> it = root.selectNodes("string").iterator(); it.hasNext();) {
			e = it.next();
			key = getElementAttribute(e, "name");
			if (key == null) {
				continue;
			}
			value = getElementAttribute(e, "value");
			resources.addValue(key, value);
	//		System.out.println(key + "---" + resources.getString(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readIntResources(Element root) {
		String key = null;
		String value = null;
		Element e = null;
		for (Iterator<Element> it = root.selectNodes("int").iterator(); it.hasNext();) {
			e = it.next();
			key = getElementAttribute(e, "name");
			if (key == null) {
				continue;
			}
			value = getElementAttribute(e, "value");
			resources.addValue(key, new Integer(value));
	//		System.out.println(key + "---" + resources.getInt(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readBooleanResources(Element root) {
		String key = null;
		String value = null;
		Element e = null;
		for (Iterator<Element> it = root.selectNodes("boolean").iterator(); it.hasNext();) {
			e = it.next();
			key = getElementAttribute(e, "name");
			if (key == null) {
				continue;
			}
			value = getElementAttribute(e, "value");
			resources.addValue(key, new Boolean(value));
	//		System.out.println(key + "---" + resources.getBoolean(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readListResources(Element root) {
		String key = null;
		Element e = null;
		List<String> list = null;
		Element se = null;
		for (Iterator<Element> it = root.selectNodes("list").iterator(); it.hasNext();) {
			e = it.next();
			key = getElementAttribute(e, "name");
			if (key == null)  {
				continue;
			}
			list = new ArrayList<String>();
			for (Iterator<Element> eit = e.selectNodes("element").iterator(); eit.hasNext();) {
				se = eit.next();
				list.add(getElementAttribute(se, "value"));
			}
			resources.addValue(key, list);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readMapResources(Element root) {
		String key = null;
		Element e = null;
		Map<String, String> map = null;
		Element se = null;
		String skey = null;
		for (Iterator<Element> it = root.selectNodes("map").iterator(); it.hasNext();) {
			e = it.next();
			key = getElementAttribute(e, "name");
			if (key == null)  {
				continue;
			}
			map = new HashMap<String, String>();
			for (Iterator<Element> eit = e.selectNodes("element").iterator(); eit.hasNext();) {
				se = eit.next();
				skey = getElementAttribute(se, "key");
				if (skey == null)  {
					continue;
				}
				map.put(skey, getElementAttribute(se, "value"));
			}
			resources.addValue(key, map);
			
		}
	}
	
	/**
	 * 取得配置信息
	 * @return Resources
	 */
	public Resources readResources() {
		return resources;
	}
	
	/**
	 * 取得节点的文本性值，若为空字窜或null返回默认值
	 * @param e document节点
	 * @return 节点文本
	 */
	public String getElementText(Element e) {
		if (e != null) {
			String str = e.getTextTrim();
			if (StringUtils.isNotBlank(str)) {
				return str;
			}
		}
		
		return null;
	}
	
	/**
	 * 取得节点的属性值，若为空字窜或null返回默认值
	 * @param e document节点
	 * @param name 属性名
	 * @return 属性值
	 */
	public String getElementAttribute(Element e, String name) {
		if (e != null && name != null) {
			String str = e.attributeValue(name);
			if (StringUtils.isNotBlank(str)) {
				return str.trim();
			}
		}
		return null;
	}
}
