package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.hbean.ReportNormConf;

public interface IReportNormConfDAO extends IBaseHibernateDAO {
    
	public List<ReportNormConf> findByRptID(String rptID);
	
}
