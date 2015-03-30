package org.eapp.poss.blo;

import java.util.List;

import org.eapp.poss.dao.param.ConfirmExtendQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ConfirmExtend;
import org.eapp.util.hibernate.ListPage;

public interface IConfirmExtendBiz {

	ConfirmExtend getConfirmExtend(String contractId);
	
	void addConfirmExtend(String prodId, String orgName, Integer custNums, Integer confirmNums, 
			String expressName, String expressNo, String remark,
			String regUser, String regDept) throws PossException;
	
	void modifyConfirmExtend(String id, Integer custNums, Integer confirmNums, 
			String expressName, String expressNo, String remark) throws PossException;

	void deleteConfirmExtend(String id) throws PossException;
	
	ListPage<ConfirmExtend> queryConfirmExtendPage(ConfirmExtendQueryParameters qp);

	List<String> getOrgNames();
}
