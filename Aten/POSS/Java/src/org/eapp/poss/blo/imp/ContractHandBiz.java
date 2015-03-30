package org.eapp.poss.blo.imp;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IContractHandBiz;
import org.eapp.poss.dao.IBlankContractDAO;
import org.eapp.poss.dao.IContractHandDAO;
import org.eapp.poss.dao.IContractRequestDAO;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.param.ContractHandQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractHand;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.UserAccountInfoHelper;
import org.eapp.util.hibernate.ListPage;

public class ContractHandBiz implements IContractHandBiz {
	private IContractRequestDAO contractRequestDAO;
	private IContractHandDAO contractHandDAO;
	private IBlankContractDAO blankContractDAO;
	private IProdInfoDAO prodInfoDAO;
	
	public void setContractRequestDAO(IContractRequestDAO contractRequestDAO) {
		this.contractRequestDAO = contractRequestDAO;
	}
	public void setContractHandDAO(IContractHandDAO contractHandDAO) {
		this.contractHandDAO = contractHandDAO;
	}
	public void setBlankContractDAO(IBlankContractDAO blankContractDAO) {
		this.blankContractDAO = blankContractDAO;
	}
	public void setProdInfoDAO(IProdInfoDAO prodInfoDAO) {
		this.prodInfoDAO = prodInfoDAO;
	}
	
	@Override
	public ContractHand getContractHand(String contractId) {
		return contractHandDAO.findById(contractId);
	}
	
	@Override
	public ContractHand getContractHandByProdId(String prodId) {
		return contractHandDAO.findByProdId(prodId);
	}

	@Override
	public void addContractHand(String prodId, Integer signNums, Integer blankNums, Integer invalidNums, 
			String expressName, String expressNo, Date handDate, String handRemark,
			String regUser, String regDept) throws PossException {
		String orgName = UserAccountInfoHelper.getOrgName(regUser);
		ContractHand req = new ContractHand();
		ProdInfo prodInfo = prodInfoDAO.findById(prodId);
		req.setProdInfo(prodInfo);
		req.setCheckStatus(ContractHand.CHECK_STATUS_NO);
		req.setOrgName(orgName);
		req.setRegUser(regUser);
		req.setRegDept(regDept);
		req.setRegDate(new Date());
		req.setSignNums(signNums);
		req.setBlankNums(blankNums);
		req.setInvalidNums(invalidNums);
		req.setExpressName(expressName);
		req.setExpressNo(expressNo);
		req.setHandDate(handDate);
		req.setHandRemark(handRemark);
		
		//配送合同总数
		int extendNums = contractRequestDAO.findExtendNums(prodId, orgName);
		ContractHandStat hs = contractHandDAO.findContractHandStat(prodId, orgName);
		if (extendNums - hs.getHandNums() < req.getHandNums()) {
			throw new PossException("上交合同数不能大于本机构剩余空白合同数");
		}
		
		contractHandDAO.save(req);
	}
	
	@Override
	public void modifyContractHand(String id, Integer signNums, Integer blankNums, Integer invalidNums, 
			String expressName, String expressNo, Date handDate, String handRemark) throws PossException {
		ContractHand req = contractHandDAO.findById(id);
		if (req == null) {
			throw new IllegalArgumentException("对象不存在");
		}
		if (req.getCheckStatus() == ContractHand.CHECK_STATUS_YES) {
			throw new PossException("已通过审核的上交信息不能修改");
		}
		//原来上交总数
		int oldHandNums = req.getHandNums();
		
		req.setSignNums(signNums);
		req.setBlankNums(blankNums);
		req.setInvalidNums(invalidNums);
		req.setExpressName(expressName);
		req.setExpressNo(expressNo);
		req.setHandDate(handDate);
		req.setHandRemark(handRemark);
		//增加的上交总数
		int addHandNums = req.getHandNums() - oldHandNums;
		if (addHandNums > 0) {
			//如果修改后增加的上交总数有增加。判断是否超过机构剩余空白合同数
			
			//配送合同总数
			int extendNums = contractRequestDAO.findExtendNums(req.getProdId(), req.getOrgName());
			ContractHandStat hs = contractHandDAO.findContractHandStat(req.getProdId(), req.getOrgName());
			if (extendNums - hs.getHandNums() < addHandNums) {
				throw new PossException("上交合同数不能大于本机构剩余空白合同数");
			}
		}
		contractHandDAO.update(req);
	}
	
	@Override
	public void deleteContractHand(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("非法参数：ID为空");
		}
		ContractHand supp = contractHandDAO.findById(id);
		if (supp == null) {
			return;
		}
		if (supp.getCheckStatus() == ContractHand.CHECK_STATUS_YES && supp.getBlankNums() > 0) {
			BlankContract bc = blankContractDAO.findByProdId(supp.getProdId());
			if (bc != null) {
				bc.setRemainNums(bc.getRemainNums() - supp.getBlankNums());//减去上交空白合同数
				blankContractDAO.update(bc);
			}
		}
		contractHandDAO.delete(supp);
		
	}
	
	@Override
	public void deleteAllContractHand(String prodId, String orgName) throws PossException {
		ContractHandStat hs = contractHandDAO.findContractHandStat(prodId, orgName);
		if (hs != null) {
			BlankContract bc = blankContractDAO.findByProdId(prodId);
			if (bc != null) {
				bc.setRemainNums(bc.getRemainNums() - hs.getBlankNums());//减去上交空白合同数
			}
		}
		contractHandDAO.deleteAll(prodId, orgName);
	}
	
	@Override
	public ListPage<ContractHand> queryContractHandPage(ContractHandQueryParameters qp) {
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        return contractHandDAO.findPage(qp);
	}

	@Override
	public ListPage<ContractHandStat> queryContractHandStatPage(ContractHandQueryParameters qp) {
		ListPage<ContractHandStat> page = contractHandDAO.findStatPage(qp);
		if (page != null && page.getDataList() != null) {
			for (ContractHandStat stat : page.getDataList()) {
				ContractHandStat hs = contractHandDAO.findContractHandStat(stat.getProdId(), stat.getOrgName());
				if (hs != null) {
					stat.setSignNums(hs.getSignNums());
					stat.setBlankNums(hs.getBlankNums());
					stat.setInvalidNums(hs.getInvalidNums());
				}
			}
		}
		return page;
	}
	
	@Override
	public void txCheckHand(String id, Integer signNums, Integer blankNums, 
			Integer invalidNums, Integer unPassNums, String checkRemark) throws PossException {
		ContractHand ch = contractHandDAO.findById(id);
		if (ch == null) {
			throw new IllegalArgumentException("对象为空");
		}
		BlankContract bc = blankContractDAO.findByProdId(ch.getProdId());
		if (bc == null) {
			throw new IllegalArgumentException("对象为空");
		}
		//配送合同总数
		int extendNums = contractRequestDAO.findExtendNums(ch.getProdId(), ch.getOrgName());
		ContractHandStat hs = contractHandDAO.findContractHandStat(ch.getProdId(), ch.getOrgName());
		if (extendNums - hs.getHandNums() < ch.getHandNums()) {
			throw new PossException("上交合同数不能大于本机构剩余空白合同数");
		}
		ch.setCheckStatus(ContractHand.CHECK_STATUS_YES);
		ch.setSignNums(signNums);
		ch.setBlankNums(blankNums);
		ch.setInvalidNums(invalidNums);
		ch.setUnPassNums(unPassNums);
		ch.setCheckRemark(checkRemark);
		ch.setCheckDate(new Date());
		contractHandDAO.update(ch);
		
		bc.setRemainNums(bc.getRemainNums() + ch.getBlankNums());
		blankContractDAO.update(bc);
	}
}
