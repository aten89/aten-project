package org.eapp.poss.blo.imp;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IContractRequestBiz;
import org.eapp.poss.dao.IBlankContractDAO;
import org.eapp.poss.dao.IContractHandDAO;
import org.eapp.poss.dao.IContractRequestDAO;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.param.ContractRequestQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.dto.ContractRequestStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRequest;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.UserAccountInfoHelper;
import org.eapp.util.hibernate.ListPage;

public class ContractRequestBiz implements IContractRequestBiz {
	
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
	public ContractRequest getContractRequest(String id) {
		return contractRequestDAO.findById(id);
	}

	@Override
	public void addContractRequest(String prodId, Integer reqNums, String reqRemark, 
			String regUser, String regDept) throws PossException {
		BlankContract bc = blankContractDAO.findByProdId(prodId);
		if (bc == null) {
			throw new IllegalArgumentException("对象为空");
		}
		if (reqNums > bc.getRemainNums()) {
			throw new PossException("所需合同数不能大于剩余合同数");
		}
		
		String orgName = UserAccountInfoHelper.getOrgName(regUser);
		ContractRequest req = new ContractRequest();
		ProdInfo prodInfo = prodInfoDAO.findById(prodId);
		req.setProdInfo(prodInfo);
		req.setRegStatus(ContractRequest.REG_STATUS_REGIEST);
		req.setOrgName(orgName);
		req.setReqNums(reqNums);
		req.setRegUser(regUser);
		req.setRegDept(regDept);
		req.setRegDate(new Date());
		req.setReqRemark(reqRemark);
		int regNums = contractRequestDAO.findRegNums(prodId, orgName);
		req.setFirstFlag(regNums == 0);
		contractRequestDAO.save(req);
	}
	
	@Override
	public void modifyContractRequest(String id, Integer reqNums, String reqRemark) throws PossException {
		ContractRequest req = contractRequestDAO.findById(id);
		if (req == null) {
			throw new IllegalArgumentException("对象不存在");
		}
		
		BlankContract bc = blankContractDAO.findByProdId(req.getProdId());
		if (bc == null) {
			throw new IllegalArgumentException("对象为空");
		}
		if (req.getRegStatus() != ContractRequest.REG_STATUS_REGIEST) {
			throw new PossException("已发放需求信息不能修改");
		}
		if (reqNums > bc.getRemainNums()) {
			throw new PossException("所需合同数不能大于剩余合同数");
		}
		
		req.setReqNums(reqNums);
		req.setReqRemark(reqRemark);
		contractRequestDAO.update(req);
	}
	
	@Override
	public void deleteContractRequest(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("非法参数：ID为空");
		}
		ContractRequest supp = contractRequestDAO.findById(id);
		if (supp == null) {
			return;
		}
		if (supp.getRegStatus() != ContractRequest.REG_STATUS_REGIEST) {
			BlankContract bc = blankContractDAO.findByProdId(supp.getProdId());
			if (bc != null) {
				bc.setRemainNums(bc.getRemainNums() + supp.getExtendNums());//加上实际发放数
				blankContractDAO.update(bc);
			}
		}
		contractRequestDAO.delete(supp);
	}
	
	@Override
	public void deleteAllContractRequest(String prodId, String orgName) throws PossException {
		BlankContract bc = blankContractDAO.findByProdId(prodId);
		if (bc != null) {
			int extendNums = contractRequestDAO.findExtendNums(prodId, orgName);//实际发放数
			bc.setRemainNums(bc.getRemainNums() + extendNums);//加上已配送合同数
			blankContractDAO.update(bc);
		}
		contractRequestDAO.deleteAll(prodId, orgName);
	}
	
	@Override
	public ListPage<ContractRequest> queryContractRequestPage(ContractRequestQueryParameters qp) {
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        return contractRequestDAO.findPage(qp);
	}

	@Override
	public ListPage<ContractRequestStat> queryContractRequestStatPage(ContractRequestQueryParameters qp) {
		ListPage<ContractRequestStat> page = contractRequestDAO.findStatPage(qp);
		if (page != null && page.getDataList() != null) {
			for (ContractRequestStat stat : page.getDataList()) {
				stat.setExtendNums(contractRequestDAO.findExtendNums(stat.getProdId(), stat.getOrgName()));
				ContractHandStat hs = contractHandDAO.findContractHandStat(stat.getProdId(), stat.getOrgName());
				if (hs != null) {
					stat.setSignNums(hs.getSignNums());
					stat.setRemainNums(stat.getExtendNums() - hs.getHandNums());
				}
			}
		}
		return page;
	}

	@Override
	public void txCheckExtend(String id, int extendNums, String expressName, String expressNo, 
			Date sendDate, String extendRemark) throws PossException {
		ContractRequest cr = contractRequestDAO.findById(id);
		if (cr == null) {
			throw new IllegalArgumentException("对象为空");
		}
		BlankContract bc = blankContractDAO.findByProdId(cr.getProdId());
		if (bc == null) {
			throw new IllegalArgumentException("对象为空");
		}
		if (extendNums > bc.getRemainNums()) {
			throw new PossException("实际发放数不能大于剩余合同数");
		}
		cr.setRegStatus(ContractRequest.REG_STATUS_SEND);
		cr.setExtendNums(extendNums);
		cr.setExpressName(expressName);
		cr.setExpressNo(expressNo);
		cr.setSendDate(sendDate);
		cr.setExtendRemark(extendRemark);
		cr.setExtendDate(new Date());
		contractRequestDAO.update(cr);
		
		bc.setRemainNums(bc.getRemainNums() - extendNums);
		blankContractDAO.update(bc);
	}

	@Override
	public void txConfirmReceive(String id) throws PossException {
		ContractRequest cr = contractRequestDAO.findById(id);
		if (cr == null) {
			throw new IllegalArgumentException("对象为空");
		}
		if (cr.getRegStatus() != ContractRequest.REG_STATUS_SEND) {
			throw new PossException("只有已发放合同才可以确认领取");
		}
		cr.setRegStatus(ContractRequest.REG_STATUS_RECEIVE);
		contractRequestDAO.update(cr);
	}

}
