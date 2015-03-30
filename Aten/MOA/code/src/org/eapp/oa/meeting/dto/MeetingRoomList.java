package org.eapp.oa.meeting.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * @author sds
 * @version Jul 7, 2009
 */
public class MeetingRoomList {


	private List<MeetingRoom> list;

	/**
	 * @return the list
	 */
	public List<MeetingRoom> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<MeetingRoom> list) {
		this.list = list;
	}
	
	public MeetingRoomList(List<MeetingRoom> list) {
		this.list = list;
	}
	
	/**
	 *生成费用的XML信息，格式如下：
	 *<?xml version="1.0" encoding="utf-8" ?> 
	 *<root>
	 *	<message code="1"/>
	 *	<content>
	 *     <meet-param id="id">
	 *       <name>会议室名称</name> 
	 *       <seatNum>座位</seatNum> 
	 *       <environment>环境</environment>
	 *       <powerNum>电源插孔</powerNum>
	 *       <cableNum>网线</cableNum>
	 *       <lineNum>电话线</lineNum>
	 *       <phoneNumber>电话号码</phoneNumber>
	 *       <status>可否预订</status>
	 *       <remark>备注</remark>
	 *    </meet-param>
	 *	</content>
	 *</root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");
		if (list == null || list.size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (MeetingRoom dn : list){
			proEle = contentEle.addElement("meet-param");
			proEle.addAttribute("id", dn.getId());
			proEle.addElement("areaCode").setText(DataFormatUtil.noNullValue(dn.getAreaCode()));
			proEle.addElement("areaName").setText(DataFormatUtil.noNullValue(dn.getAreaName()));
			proEle.addElement("name").setText(DataFormatUtil.noNullValue(dn.getName()));
			proEle.addElement("seatNum").setText(DataFormatUtil.noNullValue(dn.getSeatNum()));
			proEle.addElement("environment").addCDATA(DataFormatUtil.noNullValue(dn.getEnvironment()));
			proEle.addElement("powerNum").setText(DataFormatUtil.noNullValue(dn.getPowerNum()));
			proEle.addElement("cableNum").setText(DataFormatUtil.noNullValue(dn.getCableNum()));
			proEle.addElement("lineNum").setText(DataFormatUtil.noNullValue(dn.getLineNum()));
			proEle.addElement("phoneNumber").setText(DataFormatUtil.noNullValue(dn.getPhoneNumber()));
			proEle.addElement("status").setText(DataFormatUtil.noNullValue(dn.getStatus() == true ? "是" : "否"));
			proEle.addElement("remark").addCDATA(dn.getRemark());
		} 
		
		return doc;
	}
}
