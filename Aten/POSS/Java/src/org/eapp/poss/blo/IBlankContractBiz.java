package org.eapp.poss.blo;

import java.util.List;

import org.eapp.poss.dao.param.BlankContractQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRegDetail;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

public interface IBlankContractBiz {

	BlankContract getBlankContract(String contractId);
	
	BlankContract getBlankContractByProdId(String prodId);
	
	void addContractRegDetail(String prodId, Integer contractNums,Integer latestDas, Boolean returnFlag, String regUser) throws PossException;

	ListPage<BlankContract> queryBlankContractPage(BlankContractQueryParameters qp);

	void deleteBlankContract(String id) throws PossException;
	
	void deleteContractRegDetail(String detailId) throws PossException;
	
	List<ContractRegDetail> queryContractRegDetails(String contractId);
	
	List<ProdInfo> queryProdInfos();
}
