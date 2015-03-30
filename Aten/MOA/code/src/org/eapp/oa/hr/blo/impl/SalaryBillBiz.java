/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eapp.oa.hr.blo.ISalaryBillBiz;
import org.eapp.oa.hr.dao.ISalaryBillDAO;
import org.eapp.oa.hr.dao.IStaffFlowApplyDAO;
import org.eapp.oa.hr.dto.SalaryBillQueryParameters;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.ExcelImportTools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class SalaryBillBiz implements ISalaryBillBiz {

	private ISalaryBillDAO salaryBillDAO;
	private IStaffFlowApplyDAO staffFlowApplyDAO;
	private int impStartRowNum = 2;
	private int impSheetIndex = 0;

	public void setSalaryBillDAO(ISalaryBillDAO salaryBillDAO) {
		this.salaryBillDAO = salaryBillDAO;
	}
	
	public void setStaffFlowApplyDAO(IStaffFlowApplyDAO staffFlowApplyDAO) {
        this.staffFlowApplyDAO = staffFlowApplyDAO;
    }

	public void setImpStartRowNum(int impStartRowNum) {
		this.impStartRowNum = impStartRowNum;
	}

	public void setImpSheetIndex(int impSheetIndex) {
		this.impSheetIndex = impSheetIndex;
	}

	@Override
	public SalaryBill getSalaryBillById(String id) {
		return salaryBillDAO.findById(id);
	}
	
	@Override
	public SalaryBill getSalaryBill(String userAccountID, int month) {
	 return salaryBillDAO.findSalaryBill(userAccountID, month);
	}

	@Override
	public ListPage<SalaryBill> getSalaryBillPage(SalaryBillQueryParameters qp) {
		return salaryBillDAO.querySalaryBillPage(qp);
	}

	@Override
	public int getSalaryBillCount(int month) {
		return salaryBillDAO.countByMonth(month);
	}
	
	@Override
	public void txImpSalaryBill(int month, InputStream excelData) throws IOException, OaException {
//		 HSSFWorkbook wb = new HSSFWorkbook(excelData);
		try {
			Workbook wb = WorkbookFactory.create(excelData);
		
			Sheet sheet = wb.getSheetAt(impSheetIndex);
			Row row = null;
			SalaryBill sbill = null;
			Date now = new Date();
			Map<String, String> accMap = getUserAccountMap();
			salaryBillDAO.deletByMonth(month);
			 
			 for (int r = impStartRowNum; r <= sheet.getLastRowNum(); r++) {
				 row = sheet.getRow(r);
				 sbill = new SalaryBill();
				 sbill.setMonth(month);
				 sbill.setImportDate(now);
				 String employeeNumber = ExcelImportTools.cellValueToString(row.getCell(2));
				 if (StringUtils.isBlank(employeeNumber)) {
					 continue;
				 }
				 sbill.setEmployeeNumber(employeeNumber);//工号
				 String userAccountID = accMap.get(employeeNumber);
				 if (StringUtils.isBlank(userAccountID)) {
					 throw new OaException(employeeNumber + "系统帐号不存在");
				 }
				 sbill.setUserAccountID(userAccountID);//用户账号
				 sbill.setUserName(ExcelImportTools.cellValueToString(row.getCell(3)));//姓名
				 
				 sbill.setDept(ExcelImportTools.cellValueToString(row.getCell(0)));//部门
				 sbill.setPost(ExcelImportTools.cellValueToString(row.getCell(1)));//职务
				 
				 sbill.setEntryDate(ExcelImportTools.cellValueToDate(row.getCell(4)));//入职日期
				 sbill.setPeopleNums(ExcelImportTools.cellValueToInt(row.getCell(5)));//人数
				 sbill.setStatus(ExcelImportTools.cellValueToString(row.getCell(6)));//状态
				 sbill.setWageTotal(ExcelImportTools.cellValueToFloat(row.getCell(7)));//工资总额
				 sbill.setWageBasic(ExcelImportTools.cellValueToFloat(row.getCell(8)));//基本工资
				 sbill.setWagePerformance(ExcelImportTools.cellValueToFloat(row.getCell(9)));//绩效工资
				 sbill.setScorePerformance(ExcelImportTools.cellValueToInt(row.getCell(10)));//绩效得分
				 sbill.setWagePerformanceReal(ExcelImportTools.cellValueToFloat(row.getCell(11)));//实际绩效工资
				 sbill.setAllowance(ExcelImportTools.cellValueToFloat(row.getCell(12)));//补助
				 sbill.setCommission(ExcelImportTools.cellValueToFloat(row.getCell(13)));//提成
				 sbill.setLessMonthDays(ExcelImportTools.cellValueToInt(row.getCell(14)));//不足月天数
				 sbill.setDeductLessMonth(ExcelImportTools.cellValueToFloat(row.getCell(15)));//不足月扣款
				 sbill.setLeaveCompassionate(ExcelImportTools.cellValueToInt(row.getCell(16)));//事假天数
				 sbill.setDeductCompassionate(ExcelImportTools.cellValueToFloat(row.getCell(17)));//事假扣款
				 sbill.setLeaveSick(ExcelImportTools.cellValueToInt(row.getCell(18)));//病假天数
				 sbill.setDeductSick(ExcelImportTools.cellValueToFloat(row.getCell(19)));//病假扣款
				 sbill.setDeductLate(ExcelImportTools.cellValueToFloat(row.getCell(20)));//迟到扣款
				 sbill.setDeductElse(ExcelImportTools.cellValueToFloat(row.getCell(21)));//其它补/扣款
				 sbill.setWagePayable(ExcelImportTools.cellValueToFloat(row.getCell(22)));//应发工资总额
				 sbill.setPension(ExcelImportTools.cellValueToFloat(row.getCell(23)));//养老个人
				 sbill.setLostJob(ExcelImportTools.cellValueToFloat(row.getCell(24)));//失业个人
				 sbill.setMedical(ExcelImportTools.cellValueToFloat(row.getCell(25)));//医疗个人
				 sbill.setInsurancePayment(ExcelImportTools.cellValueToFloat(row.getCell(26)));//社保、公积金补缴
				 sbill.setCostSocialSecurity(ExcelImportTools.cellValueToFloat(row.getCell(27)));//本月社保扣款
				 sbill.setCostAccumulationFund(ExcelImportTools.cellValueToFloat(row.getCell(28)));//本月公积金扣款
				 sbill.setCostFiveInsurance(ExcelImportTools.cellValueToFloat(row.getCell(29)));//五险一金扣款
				 sbill.setTaxWage(ExcelImportTools.cellValueToFloat(row.getCell(30)));//应税工资
				 sbill.setWagePreTax(ExcelImportTools.cellValueToFloat(row.getCell(31)));//税前工资
				 sbill.setTaxPersonal(ExcelImportTools.cellValueToFloat(row.getCell(32)));//个税
				 sbill.setWageReal(ExcelImportTools.cellValueToFloat(row.getCell(33)));//工资实发
				 sbill.setWageAllowance(ExcelImportTools.cellValueToFloat(row.getCell(34)));//补发工资
				 salaryBillDAO.save(sbill);
			 }
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}
	
	private Map<String, String> getUserAccountMap() {
		Map<String, String> nameMap = new HashMap<String, String>();
		
		StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
		rqp.setPageSize(QueryParameters.ALL_PAGE_SIZE);
		rqp.setApplyType(StaffFlowApply.TYPE_ENTRY);
		//获取所有在职用户
		ListPage<StaffFlowApply> sfpages = staffFlowApplyDAO.queryStaffFlowApply(rqp);
			
    	if (sfpages != null && sfpages.getDataList() != null) {
    		for (StaffFlowApply u : sfpages.getDataList()) {
    			nameMap.put(u.getEmployeeNumber(), u.getUserAccountID());
    		}
    	}
    	return nameMap;
	}
}
