package org.eapp.oa.meeting.dto;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class MeetingParticipantList {
	private Collection<MeetingParticipant> mps;
	
	public MeetingParticipantList(Collection<MeetingParticipant> mps) {
		this.mps = mps;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (mps == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");

		Element proEle = null;
		for (MeetingParticipant mp : mps) {
			proEle = contentEle.addElement("meeting-participant");		
			proEle.addAttribute("id", mp.getId());
			proEle.addElement("person-name").setText(DataFormatUtil.noNullValue(mp.getPersonName()));		
			proEle.addElement("account-or-email").setText(DataFormatUtil.noNullValue(mp.getAccountOrEmail()));
			proEle.addElement("type").setText(DataFormatUtil.noNullValue(mp.getType()));
		}
		return doc;
	}	
}
