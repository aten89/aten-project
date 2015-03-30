package org.eapp.crm.dao.imp;

import java.util.List;

import org.eapp.crm.dao.IReportNormConfDAO;
import org.eapp.crm.hbean.ReportNormConf;
import org.hibernate.Query;

public class ReportNormConfDAO extends BaseHibernateDAO implements IReportNormConfDAO {
    
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportNormConf> findByRptID(String rptID) {
		Query query = getSession().createQuery("from ReportNormConf as rnc where rnc.rptID=:rptID");
		query.setString("rptID", rptID);
		return query.list();
	}
	
}
