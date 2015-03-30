package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.ContractRequestQueryParameters;
import org.eapp.poss.dto.ContractRequestStat;
import org.eapp.poss.hbean.ContractRequest;
import org.eapp.util.hibernate.ListPage;

public interface IContractRequestDAO extends IBaseHibernateDAO {
	
	ContractRequest findById(String id);
	
	List<ContractRequest> findByProdId(String prodId);
	
	/**
	 * 查询登记次数
	 * @param prodId
	 * @param orgName
	 * @return
	 */
	int findRegNums(String prodId, String orgName);
	
	/**
	 * 统计配送合同数
	 * @param prodId
	 * @param orgName
	 * @return
	 */
	int findExtendNums(String prodId, String orgName);
	
	void deleteAll(String prodId, String orgName);
	
	ListPage<ContractRequestStat> findStatPage(ContractRequestQueryParameters qp);
	
	ListPage<ContractRequest> findPage(ContractRequestQueryParameters qp);
}
