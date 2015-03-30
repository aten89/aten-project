package org.eapp.poss.dao;

import org.eapp.poss.dao.param.ContractHandQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.hbean.ContractHand;
import org.eapp.util.hibernate.ListPage;

public interface IContractHandDAO extends IBaseHibernateDAO {
	
	ContractHand findById(String id);
	
	ContractHand findByProdId(String prodId);
	
	
	/**
	 * 统计汇总信息
	 * @param prodId
	 * @param orgName
	 * @return
	 */
	ContractHandStat findContractHandStat(String prodId, String orgName);
	
	void deleteAll(String prodId, String orgName);
	
	ListPage<ContractHandStat> findStatPage(ContractHandQueryParameters qp);
	
	ListPage<ContractHand> findPage(ContractHandQueryParameters qp);
}
