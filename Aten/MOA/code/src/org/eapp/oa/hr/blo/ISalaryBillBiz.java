package org.eapp.oa.hr.blo;

import java.io.IOException;
import java.io.InputStream;

import org.eapp.oa.hr.dto.SalaryBillQueryParameters;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;

public interface ISalaryBillBiz{

	public SalaryBill getSalaryBillById(java.lang.String id);
	
	public SalaryBill getSalaryBill(String userAccountID, int month);
	
	public ListPage<SalaryBill> getSalaryBillPage(SalaryBillQueryParameters qp);
	
	public int getSalaryBillCount(int month);
	
	public void txImpSalaryBill(int month, InputStream excelData) throws IOException, OaException;
}