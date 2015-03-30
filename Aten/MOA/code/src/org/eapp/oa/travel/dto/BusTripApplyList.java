package org.eapp.oa.travel.dto;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.oa.travel.hbean.BusTripApplyDetail;
import org.eapp.util.web.DataFormatUtil;


public class BusTripApplyList {
	
	private List<BusTripApply> busTripApplys;
	
	
	
	public BusTripApplyList(List<BusTripApply> busTripApplys) {
		this.busTripApplys = busTripApplys;
	}


	public List<BusTripApply> getBusTripApplys() {
		return busTripApplys;
	}


	public void setBusTripApplys(List<BusTripApply> busTripApplys) {
		this.busTripApplys = busTripApplys;
	}

	/**
	 *生成系统日志
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 		<busTripApply-draft id="草稿单号">
	 * 			<trip-sche>出差日程</trip-sche>
	 * 			<days>天数</days>
	 * 			<apply-date>申请时间</apply-date>
	 * 		</busTripApply-draft>
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
		Element proEle = null;
		for (BusTripApply r : busTripApplys) {
			String tripStr = "";
			proEle = contentEle.addElement("busTripApply");				
			proEle.addAttribute("id", r.getId());
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
			proEle.addElement("apply-date").setText(DataFormatUtil.noNullValue(r.getApplyDate(),SysConstants.STANDARD_TIME_PATTERN));
		}
		return doc;
	}
}
