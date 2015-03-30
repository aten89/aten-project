/**
 * 
 */
package org.eapp.oa.hr.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.WorkExperience;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * @author ZFC
 */
public class StaffFlowApplyQueryPage {
	
	private ListPage<StaffFlowApply> staffFlowApplys;
	
	public StaffFlowApplyQueryPage(ListPage<StaffFlowApply> staffFlowApplys) {
		super();
		this.staffFlowApplys = staffFlowApplys;
	}

	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (staffFlowApplys == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(staffFlowApplys.getTotalCount()));
		contentEle.addAttribute("page-size", Long.toString(staffFlowApplys.getCurrentPageSize()));
		contentEle.addAttribute("page-count", Long.toString(staffFlowApplys.getTotalPageCount()));
		contentEle.addAttribute("current-page", Long.toString(staffFlowApplys.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", Boolean.toString(staffFlowApplys.hasPreviousPage()));
		contentEle.addAttribute("next-page", Boolean.toString(staffFlowApplys.hasNextPage()));
		Element proEle = null;
		if(staffFlowApplys!=null && staffFlowApplys.getDataList()!=null){
			for (StaffFlowApply r : staffFlowApplys.getDataList()) {
				proEle = contentEle.addElement("staff-flow");
				proEle.addAttribute("id", r.getId());
				proEle.addElement("applyType").setText(DataFormatUtil.noNullValue(r.getApplyType()));
				
				if (r.getApplyType() == StaffFlowApply.TYPE_RESIGN && r.getRefStaffFlowApply() != null) {
					//如果是离职单，列表显示入职单的信息
					r = r.getRefStaffFlowApply();
				}
				
				proEle.addElement("employeeNumber").setText(DataFormatUtil.noNullValue(r.getEmployeeNumber()));
				proEle.addElement("userName").setText(DataFormatUtil.noNullValue(r.getUserName()));
				proEle.addElement("sex").setText(r.getSex() != null && r.getSex() == 0 ? "男" : "女");
				proEle.addElement("staffStatus").setText(DataFormatUtil.noNullValue(r.getStaffStatus()));
				proEle.addElement("groupName").setText(DataFormatUtil.noNullValue(r.getGroupName()));
				proEle.addElement("groupFullName").setText(DataFormatUtil.noNullValue(r.getGroupFullName()));
				proEle.addElement("post").setText(DataFormatUtil.noNullValue(r.getPost()));
				proEle.addElement("level").setText(DataFormatUtil.noNullValue(r.getLevel()));
				proEle.addElement("entryDate").setText(DataFormatUtil.noNullValue(r.getEntryDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("formalDate").setText(DataFormatUtil.noNullValue(r.getFormalDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("idCard").setText(DataFormatUtil.noNullValue(r.getIdCard()));
				proEle.addElement("birthdate").setText(DataFormatUtil.noNullValue(r.getBirthdate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("age").setText(DataFormatUtil.noNullValue(r.getAge()));
				proEle.addElement("education").setText(DataFormatUtil.noNullValue(r.getEducation()));
				proEle.addElement("finishSchool").setText(DataFormatUtil.noNullValue(r.getFinishSchool()));
				proEle.addElement("professional").setText(DataFormatUtil.noNullValue(r.getProfessional()));
				proEle.addElement("degree").setText(DataFormatUtil.noNullValue(r.getDegree()));
				proEle.addElement("contractType").setText(DataFormatUtil.noNullValue(r.getContractType()));
				proEle.addElement("contractStartDate").setText(DataFormatUtil.noNullValue(r.getContractStartDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("contractEndDate").setText(DataFormatUtil.noNullValue(r.getContractEndDate(), SysConstants.STANDARD_DATE_PATTERN));
			
				proEle.addElement("mobile").setText(DataFormatUtil.noNullValue(r.getMobile()));
				proEle.addElement("officeTel").setText(DataFormatUtil.noNullValue(r.getOfficeTel()));
				proEle.addElement("email").setText(DataFormatUtil.noNullValue(r.getEmail()));
				proEle.addElement("politicalStatus").setText(DataFormatUtil.noNullValue(r.getPoliticalStatus()));
				proEle.addElement("nation").setText(DataFormatUtil.noNullValue(r.getNation()));
				proEle.addElement("nativePlace").setText(DataFormatUtil.noNullValue(r.getNativePlace()));
				proEle.addElement("homeAddr").setText(DataFormatUtil.noNullValue(r.getHomeAddr()));
				proEle.addElement("zipCode").setText(DataFormatUtil.noNullValue(r.getZipCode()));
				proEle.addElement("domicilePlace").setText(DataFormatUtil.noNullValue(r.getDomicilePlace()));
				proEle.addElement("domicileType").setText(DataFormatUtil.noNullValue(r.getDomicileType()));
				proEle.addElement("maritalStatus").setText(DataFormatUtil.noNullValue(r.getMaritalStatus()));
				proEle.addElement("hadKids").setText(r.getHadKids() == 1 ? "是" : "否");
				
				proEle.addElement("salesExperience").setText(r.getSalesExperience() == 1 ? "是" : "否");
				proEle.addElement("financialExperience").setText(r.getFinancialExperience() == 1 ? "是" : "否");
				proEle.addElement("financialQualification").setText(r.getFinancialQualification() == 1 ? "是" : "否");
				proEle.addElement("workStartDate").setText(DataFormatUtil.noNullValue(r.getWorkStartDate(), SysConstants.STANDARD_DATE_PATTERN));
				proEle.addElement("supervisor").setText(DataFormatUtil.noNullValue(r.getSupervisor()));
				proEle.addElement("recruitmentType").setText(DataFormatUtil.noNullValue(r.getRecruitmentType()));
				proEle.addElement("recommended").setText(DataFormatUtil.noNullValue(r.getRecommended()));
				proEle.addElement("seniority").setText(DataFormatUtil.noNullValue(r.getSeniority()));
				proEle.addElement("emergencyContact").setText(DataFormatUtil.noNullValue(r.getEmergencyContact()));
				proEle.addElement("emergencyContactTel").setText(DataFormatUtil.noNullValue(r.getEmergencyContactTel()));
				proEle.addElement("bankCardNO").setText(DataFormatUtil.noNullValue(r.getBankCardNO()));
				proEle.addElement("bankType").setText(DataFormatUtil.noNullValue(r.getBankType()));
				
				proEle.addElement("trainInfo").setText(DataFormatUtil.noNullValue(r.getTrainInfo()));
				proEle.addElement("skillsInfo").setText(DataFormatUtil.noNullValue(r.getSkillsInfo()));
				StringBuffer works = new StringBuffer();
				if (r.getWorkExperiences() != null) {
					for (WorkExperience we : r.getWorkExperiences()) {
						works.append(DataFormatUtil.noNullValue(we.getStartDate(), SysConstants.STANDARD_DATE_PATTERN)).append(" / ")
						.append(DataFormatUtil.noNullValue(we.getEndDate(), SysConstants.STANDARD_DATE_PATTERN)).append(" / ")
						.append(DataFormatUtil.noNullValue(we.getCompanyName())).append(" / ")
						.append(DataFormatUtil.noNullValue(we.getPostName())).append(" / ")
						.append(DataFormatUtil.noNullValue(we.getPostDuty())).append("<br/>");
					}
				}
				proEle.addElement("workExperiences").setText(works.toString());
				
				proEle.addElement("statDays").setText(DataFormatUtil.noNullValue(r.getStatDatys()));
			}
		}
		return doc;
	}
}





