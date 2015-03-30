package org.eapp.oa.travel.dto;

import java.text.SimpleDateFormat;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.oa.travel.hbean.BusTripApplyDetail;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


public class BusTripApplyTrack {
	
	private ListPage<BusTripApply> busTripApplys;
	
	public BusTripApplyTrack(ListPage<BusTripApply> busTripApplys) {
		super();
		this.busTripApplys = busTripApplys;
	}

	public ListPage<BusTripApply> getBusTripApplys() {
		return busTripApplys;
	}

	public void setBusTripApplys(ListPage<BusTripApply> busTripApplys) {
		this.busTripApplys = busTripApplys;
	}




	/**
	 *生成系统日志
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 		<busTripApply id="单号">
	 * 			<apply-account>申请人</apply-account>
	 * 			<dept>申请部门</dept>
	 * 			<trip-date>申请日期</trip-date>
	 * 			<days>天数</days>
	 * 			<apply-date>申请时间</apply-date>
	 *   		<task-id>任务ID</task-id>
	 * 			<taskinstance-id>任务实例ID</taskinstance-id>
	 *  		<view-flag>是否已读</view-flag>
	 * 		</holidayApply>
	 * 	</content>
	 * </root>
	 * @return
	 */
	
	
	public Document toDocument() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (busTripApplys == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(busTripApplys.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(busTripApplys.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(busTripApplys.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(busTripApplys.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(busTripApplys.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(busTripApplys.hasNextPage()));
		Element proEle = null;
		if(busTripApplys!=null && busTripApplys.getDataList()!=null){
			for (BusTripApply r : busTripApplys.getDataList()) {
				String tripStr = "";
				proEle = contentEle.addElement("busTripApply");				
				proEle.addAttribute("id", r.getId());
				proEle.addElement("apply-account").setText(DataFormatUtil.noNullValue(r.getApplicantName()));
				proEle.addElement("dept").setText(DataFormatUtil.noNullValue(r.getApplyDept()));
				Set<BusTripApplyDetail> tripDetail = r.getBusTripApplyDetail();
				if(tripDetail != null){
					for (BusTripApplyDetail h : tripDetail){
						if(tripStr.equals("")){
							tripStr += h.getFromPlace() + "—" + h.getToPlace() + "：" + " 从 " + sdf.format(h.getStartDate()) + " 到 " + sdf.format(h.getEndDate()) ;
						}else{
							tripStr += "</br>"+ h.getFromPlace() + "—" + h.getToPlace() + "：" + " 从 " + sdf.format(h.getStartDate()) + " 到 " + sdf.format(h.getEndDate());
						}
					}
				}
				proEle.addElement("trip-sche").setText(tripStr);
				proEle.addElement("days").setText(r.getTotalDays().toString());
				proEle.addElement("apply-date").setText(sdf.format(r.getApplyDate()));
				if(r.getTask()!=null){
					proEle.addElement("node-name").setText(DataFormatUtil.noNullValue(
							r.getTask().getNodeName()));
					proEle.addElement("create-time").setText(DataFormatUtil.noNullValue(
							r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
					proEle.addElement("deal-man").setText(DataFormatUtil.noNullValue(
							UsernameCache.getDisplayName(r.getTask().getTransactor())));
				}
			}
		}
		return doc;
	}
}
