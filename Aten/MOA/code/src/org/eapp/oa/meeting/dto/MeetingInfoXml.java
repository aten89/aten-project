package org.eapp.oa.meeting.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

public class MeetingInfoXml {
	private MeetingInfo meeting;
	public MeetingInfoXml(){}
	public MeetingInfoXml(MeetingInfo meeting) {
		this.meeting = meeting;
	}
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<meet-info id="会议id">
	 * 			<apply-man>预订人员帐号</apply-man>	
	 *          <display-name>预订人员姓名</display-name>
	 * 			<group-name>预订部门</group-name>		
	 * 			<reserve-time>预订时间</reserve-time>
	 * 			<room-id>预订会议室</room-id>
	 *          <room-name>会议室名称</room-name>
	 * 			<device-detail>预订设备</info-class>
	 * 			<theme>会议主题</theme>	
	 * 			<content>会议内容</content>
	 * 			<begin-time>会议开始时间</begin-time>
	 * 			<end-time>会议结束时间</end-time>
	 * 			<remark>备注</remark>
	 * 			<status>状态</status>
	 * 		</meet-info>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (meeting == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");

		Element meetEle = null;
		meetEle = contentEle.addElement("meet-info");				
		meetEle.addAttribute("id", meeting.getId());
		meetEle.addElement("apply-man").setText(DataFormatUtil.noNullValue(meeting.getApplyMan()));
		meetEle.addElement("apply-man-name").setText(meeting.getApplyManDisplayName());
		meetEle.addElement("group-name").setText(DataFormatUtil.noNullValue(meeting.getGroupName()));
		meetEle.addElement("reserve-time").setText(DataFormatUtil.noNullValue(meeting.getReserveTime(),SysConstants.STANDARD_DATE_PATTERN));
		if (meeting.getMeetingRoom() != null) {	
			meetEle.addElement("area-name").setText(DataFormatUtil.noNullValue(meeting.getMeetingRoom().getAreaName()));
			meetEle.addElement("room-id").setText(DataFormatUtil.noNullValue(meeting.getMeetingRoom().getId()));
			meetEle.addElement("room-name").setText(DataFormatUtil.noNullValue(meeting.getMeetingRoom().getName()));
		}
		meetEle.addElement("theme").setText(DataFormatUtil.noNullValue(meeting.getTheme()));
		meetEle.addElement("content").setText(DataFormatUtil.noNullValue(meeting.getContent()));
		meetEle.addElement("begin-time").setText(DataFormatUtil.noNullValue(meeting.getBeginTime(),SysConstants.STANDARD_TIME_PATTERN));
		meetEle.addElement("end-time").setText(DataFormatUtil.noNullValue(meeting.getEndTime(),SysConstants.STANDARD_TIME_PATTERN));
		
		return doc;
	}	
}
