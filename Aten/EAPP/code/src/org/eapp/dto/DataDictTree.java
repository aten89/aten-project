/**
 * 
 */
package org.eapp.dto;

import java.io.Serializable;
import java.util.Collection;

import org.eapp.hbean.DataDictionary;
import org.eapp.hbean.SubSystem;


/**
 * @version 
 */
public class DataDictTree implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -875720077934930667L;
	private SubSystem subSystem;
	private Collection<DataDictionary> dataDicts;
//	private String path;

//	public String getPath() {
//		return path;
//	}
//
//
//	public void setPath(String path) {
//		this.path = path;
//	}

	/**
	 * @param subSystem the subSystem to set
	 */
	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}
	
	/**
	 * @param dataDicts the dataDicts to set
	 */
	public void setDataDicts(Collection<DataDictionary> dataDicts) {
		this.dataDicts = dataDicts;
	}


	@Override
	public String toString() {
		String url = "m/datadict/expanddatadict?dataDictID=";
		StringBuffer out = new StringBuffer();
		if(subSystem != null){
			//树的根点节
			out.append("<li class=\"root\"  id=\"" + subSystem.getSubSystemID() + "\" subSystemID=\"" + subSystem.getSubSystemID() + "\"><span class=\"text\" subSystemID=\"" + subSystem.getSubSystemID() + "\">"
					+ subSystem.getName() + "</span>");
			
			if(dataDicts != null && !dataDicts.isEmpty()){
				out.append("<ul>");
				dataDictHtml(out, dataDicts, url);
				out.append("</ul>");
			}
			
			out.append("</li>");
		} else {
			//树的下级节点
			if (dataDicts != null && !dataDicts.isEmpty()) {
				dataDictHtml(out, dataDicts, url);
			}
		}
		return out.toString();
	}
	
	private void dataDictHtml(StringBuffer out, Collection<DataDictionary> dataDicts, String url) {
		for (DataDictionary dataDict : dataDicts) {
			String dataDictId = dataDict.getDataDictID();
			String dictType = dataDict.getDictType();
			out.append("<li class=\"folder-close\" type=\""+dictType+"\" id=\"" + dataDictId + "\" dataDictId=\"" + dataDictId + "\"><span class=\"text\" type=\""+dictType+"\" dataDictId=\"" + dataDictId + "\">"
					+ dataDict.getDictName() + "</span>");
			int childDatDictCount = dataDict.getChildDataDictionaries().size();
			if (childDatDictCount > 0) {
				out.append("<ul class = \"ajax\">");
				out.append("<li>{url:" + url + dataDictId + "}</li>");
				out.append("</ul>");
			}
			out.append("</li>");
		}
	}

}
