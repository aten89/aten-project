package org.eapp.oa.hr.dao;

import java.util.List;

import org.eapp.oa.hr.dto.TransferQueryParameters;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


public interface ITransferApplyDAO extends IBaseHibernateDAO {

	public TransferApply findById(java.lang.String id);

	public List<TransferApply> findTransferApplys(String userAccountId, int formStatus);
	
	public List<TransferApply> getDealingTransferApply(String userAccountId, List<String> userRoles);
	
	public ListPage<TransferApply> getTrackOrArchTransferApply(TransferQueryParameters rqp,
			String userAccountId,Boolean isArch);
	
	public String getMaxID();
}