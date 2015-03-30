package org.eapp.poss.blo;

import java.util.Date;

import org.eapp.poss.dao.param.ContractRequestQueryParameters;
import org.eapp.poss.dto.ContractRequestStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ContractRequest;
import org.eapp.util.hibernate.ListPage;

public interface IContractRequestBiz {

	ContractRequest getContractRequest(String id);
	
	void addContractRequest(String prodId, Integer reqNums, String reqRemark,
			String regUser, String regDept) throws PossException;
	
	void modifyContractRequest(String id, Integer reqNums, String reqRemark) throws PossException;

	void deleteContractRequest(String id) throws PossException;
	
	void deleteAllContractRequest(String prodId, String orgName) throws PossException;
	
	ListPage<ContractRequest> queryContractRequestPage(ContractRequestQueryParameters qp);

	ListPage<ContractRequestStat> queryContractRequestStatPage(ContractRequestQueryParameters qp);
	
	/**
	 * 发放审核
	 * @param id
	 * @param extendNums
	 * @param expressName
	 * @param expressNo
	 * @param sendDate
	 */
	void txCheckExtend(String id, int extendNums, String expressName, String expressNo, 
			Date sendDate, String extendRemark) throws PossException;
	
	/**
	 * 确认领取
	 * @param id
	 */
	void txConfirmReceive(String id) throws PossException;
}
