package org.eapp.crm.dao.imp;

import java.util.List;
import org.eapp.crm.dao.IReportQueryAssignDAO;
import org.eapp.crm.hbean.ReportQueryAssign;
import org.hibernate.Query;

public class ReportQueryAssignDAO extends BaseHibernateDAO implements IReportQueryAssignDAO {
    
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportQueryAssign> findQueryAssigns(String rptID, String userAccountId) {
		Query query = getSession().createQuery("from ReportQueryAssign as sqa where sqa.rptID=:rptID and sqa.userAccountID=:userAccountID");
		query.setString("rptID", rptID);
		query.setString("userAccountID", userAccountId);
		return query.list();
	}
	
	@Override
	public void deleteQueryAssign(String rptID, String userAccountId) {
		Query query = getSession().createQuery("delete from ReportQueryAssign as sqa where sqa.rptID=:rptID and  sqa.userAccountID=:userAccountID");
		query.setString("rptID", rptID);
		query.setString("userAccountID", userAccountId);
		query.executeUpdate();
	}
}
