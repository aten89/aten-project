package org.eapp.oa.meeting.dto;

import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

public class MeetingInfoListPage {
	private ListPage<MeetingInfo> listPage;

	/**
	 * @return the infoFormListPage
	 */
	public ListPage<MeetingInfo> getMeetingInfoListPage() {
		return listPage;
	}

	/**
	 * @param infoFormListPage the infoFormListPage to set
	 */
	public void setMeetingInfoListPage(ListPage<MeetingInfo> listPage) {
		this.listPage = listPage;
	}
	public MeetingInfoListPage(){}
	public MeetingInfoListPage(ListPage<MeetingInfo> listPage) {
		super();
		this.listPage = listPage;
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
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(listPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(listPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(listPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(listPage.hasNextPage()) );
		
		List<MeetingInfo>  meetingInfos = listPage.getDataList();
		if(meetingInfos!=null && meetingInfos.size()>0){
			Element meetEle = null;
			for (MeetingInfo r : meetingInfos) {
				meetEle = contentEle.addElement("meet-info");				
				meetEle.addAttribute("id", r.getId());
				meetEle.addElement("apply-man").setText(DataFormatUtil.noNullValue(r.getApplyMan()));
				meetEle.addElement("apply-man-name").setText(r.getApplyManDisplayName());
        //		meetEle.addElement("group-name").setText(DataFormatUtil.noNullValue(r.getGroupName()));
		//		meetEle.addElement("reserve-time").setText(DataFormatUtil.noNullValue(r.getReserveTime(),SysConstants.STANDARD_TIME_PATTERN));
				if (r.getMeetingRoom() != null) {	
					meetEle.addElement("area-name").setText(r.getMeetingRoom().getAreaName());
					meetEle.addElement("room-id").setText(DataFormatUtil.noNullValue(r.getMeetingRoom().getId()));
					meetEle.addElement("room-name").setText(DataFormatUtil.noNullValue(r.getMeetingRoom().getName()));
				}
				meetEle.addElement("theme").setText(DataFormatUtil.noNullValue(r.getTheme()));
		//		meetEle.addElement("content").setText(DataFormatUtil.noNullValue(r.getContent()));
				meetEle.addElement("begin-time").setText(DataFormatUtil.noNullValue(r.getBeginTime(),SysConstants.STANDARD_TIME_PATTERN));
				meetEle.addElement("end-time").setText(DataFormatUtil.noNullValue(r.getEndTime(),SysConstants.STANDARD_TIME_PATTERN));
		//		meetEle.addElement("remark").addCDATA(r.getRemark());
				boolean flag1 = false;
				boolean flag2 = false;
				flag1 = (r.getBeginTime()).after(new Date());
				flag2 = (r.getEndTime()).before(new Date());
				if(flag1) {
					meetEle.addElement("status").setText("未开始");
				} else if(flag2) {
					meetEle.addElement("status").setText("已结束");
				} else {
					meetEle.addElement("status").setText("进行中");
				}
			}
		}
		return doc;
	}	
}
