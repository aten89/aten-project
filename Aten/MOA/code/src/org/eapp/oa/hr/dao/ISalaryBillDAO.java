package org.eapp.oa.hr.dao;

import org.eapp.oa.hr.dto.SalaryBillQueryParameters;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

public interface ISalaryBillDAO extends IBaseHibernateDAO {

	public SalaryBill findById(java.lang.String id);
	
	public SalaryBill findSalaryBill(String userAccountID, int month);
	
	public ListPage<SalaryBill> querySalaryBillPage(SalaryBillQueryParameters qp);
	
	public int countByMonth(int month);
	
	public void deletByMonth(int month);
	
}