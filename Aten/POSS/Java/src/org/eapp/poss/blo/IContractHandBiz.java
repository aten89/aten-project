package org.eapp.poss.blo;

import java.util.Date;

import org.eapp.poss.dao.param.ContractHandQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ContractHand;
import org.eapp.util.hibernate.ListPage;

public interface IContractHandBiz {

	ContractHand getContractHand(String contractId);
	
	ContractHand getContractHandByProdId(String prodId);
	
	void addContractHand(String prodId, Integer signNums, Integer blankNums, Integer invalidNums, 
			String expressName, String expressNo, Date handDate, String handRemark,
			String regUser, String regDept) throws PossException;
	
	void modifyContractHand(String id, Integer signNums, Integer blankNums, Integer invalidNums, 
			String expressName, String expressNo, Date handDate, String handRemark) throws PossException;

	void deleteContractHand(String id) throws PossException;
	
	void deleteAllContractHand(String prodId, String orgName) throws PossException;
	
	ListPage<ContractHand> queryContractHandPage(ContractHandQueryParameters qp);

	ListPage<ContractHandStat> queryContractHandStatPage(ContractHandQueryParameters qp);
	
	void txCheckHand(String id, Integer signNums, Integer blankNums, 
			Integer invalidNums, Integer unPassNums, String checkRemark) throws PossException;
}
