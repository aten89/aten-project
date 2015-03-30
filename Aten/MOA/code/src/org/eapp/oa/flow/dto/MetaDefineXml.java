package org.eapp.oa.flow.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.flow.hbean.FlowMeta;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * @version 1.0 
 */
public class MetaDefineXml {

	private List<FlowMeta> metaList;

	public List<FlowMeta> getMetaList() {
		return metaList;
	}

	public void setMetaList(List<FlowMeta> metaList) {
		this.metaList = metaList;
	}
	
	
	public MetaDefineXml(List<FlowMeta> metaList){
		this.metaList = metaList;
	}
	
	
	/**
	 * 获取元数据定义XML文件
	 * @return 元数据定义的XML文件
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <!--
	 * 		META名称
	 * 		显示名称
	 * 		数据类型（不能超出以下范围：DATATYPE_STRING | DATATYPE_BOOLEAN | DATATYPE_INT | DATATYPE_LONG | DATATYPE_FLOAT | DATATYPE_DOUBLE | DATATYPE_DATE）
	 * 		是否必填（true 或 false)
	 * -->
	 * <metas>
	 * 		<meta name="admin"  display-name="客户类型"  type="DATATYPE_STRING"  not-null="false" />
	 * 		<meta name="audit"  display-name="审核"    type="DATATYPE_BOOLEAN" not-null="false" />
	 * 		<meta name="person" display-name="人数"    type="DATATYPE_INT"     not-null="false" />
	 * 		<meta name="sn"     display-name="序列号"   type="DATATYPE_LONG"   not-null="false" />
	 * 		<meta name="meney"  display-name="金额"    type="DATATYPE_FLOAT"   not-null="false" />
	 * 		<meta name="rate"   display-name="比率"    type="DATATYPE_DOUBLE"  not-null="false" />
	 * 		<meta name="type"   display-name="类型"    type="DATATYPE_STRING"  not-null="false" />
	 * 		<meta name="date"   display-name="日期"    type="DATATYPE_DATE"    not-null="false" />
	 * 		<meta name="date"   display-name="日期"    type="DATATYPE_DATE"    not-null="false" />
	 * </metas>
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("metas");
		
		for (FlowMeta meta : metaList) {
			Element metaElement = root.addElement("meta");		
			metaElement.addAttribute("name",DataFormatUtil.noNullValue(meta.getMetaName()));
			metaElement.addAttribute("display-name", DataFormatUtil.noNullValue(meta.getDisplayName()));
			metaElement.addAttribute("type", DataFormatUtil.noNullValue(meta.getType()));
			metaElement.addAttribute("not-null", DataFormatUtil.noNullValue(meta.getIsNull()?meta.getIsNull():false));
		}
		
		return doc;
	}
}
