package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.hbean.ReportQueryAssign;

public interface IReportQueryAssignDAO extends IBaseHibernateDAO {
    
    /**
     * 
     * @param rptID
     * @param userAccountId
     * @return
     */
	public List<ReportQueryAssign> findQueryAssigns(String rptID, String userAccountId);

	public void deleteQueryAssign(String rptID, String userAccountId);
}
