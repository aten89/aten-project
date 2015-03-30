/**
 * 
 */
package org.eapp.poss.dto;

import java.util.Collection;

import org.eapp.rpc.dto.DataDictInfo;


/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class DataDictionarySelect extends HTMLSelect {

	private Collection<DataDictInfo> dataDicts;
	
	public DataDictionarySelect(Collection<DataDictInfo> dataDicts) {
		this.dataDicts = dataDicts;
	}
	
	/**
	 * 有默认值的情况
	 * 格式：
	 * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (dataDicts == null || dataDicts.size() < 1) {
			return out.toString();
		}
		for (DataDictInfo d : dataDicts) {
			out.append(createOption(d.getDictCode(), d.getDictName()));
		}
		return out.toString();
	}
	
	
//	public Document toDocument() {
//		Document doc = DocumentHelper.createDocument();
//		doc.setXMLEncoding("utf-8");
//		Element root = doc.addElement("root");
//		if (dataDicts == null || dataDicts.size() < 1) {
//			root.addElement("message").addAttribute("code", "1");
//			return doc;
//		}
//		root.addElement("message").addAttribute("code", "1");
//		Element contentEle = root.addElement("content");
//		if(dataDicts != null && dataDicts.size() > 0){
//			Element proEle = null;
//			for (DataDictionaryDTO d : dataDicts) {
//				proEle = contentEle.addElement("dataDict");				
//				proEle.addAttribute("id", d.getDictCode());
//				proEle.addElement("data-value").setText(DataFormatUtil.noNullValue(d.getDictCode()));
//				proEle.addElement("data-key").setText(DataFormatUtil.noNullValue(d.getDictName()));
//				
//			}
//		}
//		return doc;
//	}		
}
