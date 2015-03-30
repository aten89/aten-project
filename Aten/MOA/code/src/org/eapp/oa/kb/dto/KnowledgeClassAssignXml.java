package org.eapp.oa.kb.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;
import org.eapp.oa.system.util.web.XMLResponse;

/**
 * 
 * @author Tim
 */
public class KnowledgeClassAssignXml {
	private Collection<KnowledgeClassAssign> knowledgeClassAssign;
	private HashMap<String ,HashMap<Integer , Integer>> assignMap = new HashMap<String ,HashMap<Integer , Integer>> ();
	
	public KnowledgeClassAssignXml(Collection<KnowledgeClassAssign> knowledgeClassAssign) {
		this.knowledgeClassAssign = new ArrayList<KnowledgeClassAssign>();
		for (KnowledgeClassAssign ila : knowledgeClassAssign) {
			HashMap<Integer , Integer> flagMap = null;
			if(!assignMap.containsKey(ila.getAssignKey())){
				flagMap = new HashMap<Integer, Integer>();
				this.knowledgeClassAssign.add(ila);
				flagMap.put(0, ila.getFlag());
				assignMap.put(ila.getAssignKey(), flagMap);
			} else {
				flagMap = assignMap.get(ila.getAssignKey());
				int size = flagMap.size();
				flagMap.put(size + 1, ila.getFlag());
				assignMap.put(ila.getAssignKey(),flagMap);
			}
		}
	}
	
	/**
	 *生成职务的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<assign type="0">部门经理</assign>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (knowledgeClassAssign == null || knowledgeClassAssign.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (KnowledgeClassAssign ila : knowledgeClassAssign) {
			proEle = contentEle.addElement("assign");
			proEle.addAttribute("id", ila.getId());
			proEle.addAttribute("type", String.valueOf(ila.getType()));
			HashMap<Integer, Integer> flagMap = assignMap.get(ila.getAssignKey());
			Collection<Integer> flags = flagMap.values();
			for(int flag : flags){
				Element flagEle = proEle.addElement("flag");							
				flagEle.addAttribute("id",String.valueOf(flag));
			}
			proEle.setText(ila.getAssignKey());				
		}
		return doc;
	}
}
