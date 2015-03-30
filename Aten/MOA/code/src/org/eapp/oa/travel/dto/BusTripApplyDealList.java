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


public class BusTripApplyDealList {

	private List<BusTripApply> busTripApplys;

	public BusTripApplyDealList(List<BusTripApply> busTripApplys) {
		this.busTripApplys = busTripApplys;
	}

	public List<BusTripApply> getBusTripApplys() {
		return busTripApplys;
	}

	public void setBusTripApplys(List<BusTripApply> busTripApplys) {
		this.busTripApplys = busTripApplys;
	}

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
		if(busTripApplys!=null){
			for (BusTripApply r : busTripApplys) {
				String tripStr = "";
				proEle = contentEle.addElement("busTripApply");
				proEle.addAttribute("id", r.getId());
				proEle.addElement("apply-account").setText(
						DataFormatUtil.noNullValue(r.getApplicantName()));
				proEle.addElement("dept").setText(
						DataFormatUtil.noNullValue(r.getApplyDept()));
				Set<BusTripApplyDetail> busTripDetail = r.getBusTripApplyDetail();
				if (busTripDetail != null) {
					for (BusTripApplyDetail h : busTripDetail) {
						if (tripStr.equals("")) {
							tripStr += h.getFromPlace() + "—" + h.getToPlace()
									+ "：" + " 从 " + sdf.format(h.getStartDate())
									+ " 到 " + sdf.format(h.getEndDate());
						} else {
							tripStr += "</br>" + h.getFromPlace() + "—"
									+ h.getToPlace() + "：" + " 从 "
									+ sdf.format(h.getStartDate()) + " 到 "
									+ sdf.format(h.getEndDate());
						}
					}
				}
				proEle.addElement("trip-sche").setText(tripStr);
				proEle.addElement("days").setText(r.getTotalDays().toString());
				proEle.addElement("apply-date").setText(
						sdf.format(r.getApplyDate()));
				proEle.addElement("task-id").setText(
						DataFormatUtil.noNullValue(r.getTask().getId()));
				proEle.addElement("taskinstance-id")
						.setText(
								DataFormatUtil.noNullValue(r.getTask()
										.getTaskInstanceID()));
				proEle.addElement("task-start").setText(
						DataFormatUtil.noNullValue(r.getTask().getCreateTime(), SysConstants.STANDARD_TIME_PATTERN));
				proEle.addElement("flag").setText(
						DataFormatUtil.noNullValue(r.getTask().getViewFlag()));
			}			
		}

		return doc;
	}
}
