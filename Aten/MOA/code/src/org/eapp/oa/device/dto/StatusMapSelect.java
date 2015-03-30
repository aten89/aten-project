package org.eapp.oa.device.dto;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.dto.HTMLOptionsDTO;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * @author jxs
 *
 */
public class StatusMapSelect extends HTMLOptionsDTO {

	private Map<Integer,String> map;
	
	public StatusMapSelect(Map<Integer,String> map) {
		this.map = map;
	}
	
	/**
	 * 有默认值的情况
	 * 格式：
	 * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (map == null || map.size() < 1) {
			return out.toString();
		}
		for(Integer o : map.keySet()){
			out.append(createOption(o.toString(), map.get(o)));
		}
		return out.toString();
	}
	

	/**
	 *生成状态属性的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<status-name>id</status-name>
	 *      <status-value>设备属性名称</status-value>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (map == null || map.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for(Integer o : map.keySet()){
			proEle = contentEle.addElement("status");				
				proEle.addElement("status-name").setText(DataFormatUtil.noNullValue(map.get(o)));
				proEle.addElement("status-value").setText(DataFormatUtil.noNullValue(o.toString()));
				
		}
		return doc;
	}
}

