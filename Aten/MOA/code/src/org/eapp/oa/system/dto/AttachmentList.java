package org.eapp.oa.system.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * 
 * @author zsy
 * @version Feb 3, 2009
 */
public class AttachmentList {
	
	private Collection<Attachment> attachments;
	
	/**
	 *构造函数
	 * @param users
	 */
	public AttachmentList(Collection<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	/**
	 *生成部门的XML信息
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 *   <message code="1" /> 
	 *   <content>
	 * 	 	<account>leidagan</account>
	 *   </content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (attachments == null || attachments.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
	
		Element proEle = null;
		for (Attachment am : attachments) {
			proEle = contentEle.addElement("attachment");
			proEle.addAttribute("id", am.getId());
			proEle.addElement("file-path").addCDATA(FileDispatcher.getAbsPath(am.getFilePath()));
			proEle.addElement("display-name").addCDATA(am.getDisplayName());
			proEle.addElement("file-ext").addCDATA(DataFormatUtil.noNullValue(am.getFileExt()));
			proEle.addElement("size").addCDATA(DataFormatUtil.noNullValue(am.getSize()));
			proEle.addElement("upload-date").addCDATA(DataFormatUtil.noNullValue(am.getUploadDate(), 
					SysConstants.STANDARD_TIME_PATTERN));
		}
		return doc;
	}
}
