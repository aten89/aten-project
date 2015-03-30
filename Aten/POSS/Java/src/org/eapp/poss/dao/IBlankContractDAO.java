package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.dao.param.BlankContractQueryParameters;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRegDetail;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

public interface IBlankContractDAO extends IBaseHibernateDAO {
	
	BlankContract findById(String id);
	
	BlankContract findByProdId(String prodId);
	
	ContractRegDetail findDetailById(String detailId);
	
	ListPage<BlankContract> findPage(BlankContractQueryParameters qp);
	
	List<ContractRegDetail> findContractRegDetails(String contractId);
	
	List<ProdInfo> findProdInfos();
}
