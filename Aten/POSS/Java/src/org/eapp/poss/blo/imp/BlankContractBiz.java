package org.eapp.poss.blo.imp;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IBlankContractBiz;
import org.eapp.poss.dao.IBlankContractDAO;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.param.BlankContractQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRegDetail;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

public class BlankContractBiz implements IBlankContractBiz {
	
	private IBlankContractDAO blankContractDAO;
	private IProdInfoDAO prodInfoDAO;
	
	public void setBlankContractDAO(IBlankContractDAO blankContractDAO) {
		this.blankContractDAO = blankContractDAO;
	}

	public void setProdInfoDAO(IProdInfoDAO prodInfoDAO) {
		this.prodInfoDAO = prodInfoDAO;
	}
	
	@Override
	public BlankContract getBlankContract(String contractId) {
		return blankContractDAO.findById(contractId);
	}
	
	@Override
	public BlankContract getBlankContractByProdId(String prodId) {
		return blankContractDAO.findByProdId(prodId);
	}

	@Override
	public void addContractRegDetail(String prodId, Integer contractNums,Integer latestDas, Boolean returnFlag, String regUser)
			throws PossException {
		BlankContract cont = blankContractDAO.findByProdId(prodId);
		if (cont == null) {
			//新增第一条明细时为空，创建一条记录
			cont = new BlankContract();
			ProdInfo prodInfo = prodInfoDAO.findById(prodId);
			cont.setProdInfo(prodInfo);
			cont.setRegNums(1);
			cont.setContractNums(contractNums);
			cont.setRemainNums(contractNums);
			blankContractDAO.save(cont);
		} else {
			//第二次之后新增，更新次数与总数
			cont.setRegNums(cont.getRegNums() + 1);
			cont.setContractNums(cont.getContractNums() + contractNums);
			cont.setRemainNums(cont.getRemainNums() + contractNums);
		}
		//这两个以最后一次登记的为准
		cont.setLatestDas(latestDas);
		cont.setReturnFlag(returnFlag);
		
		ContractRegDetail detail = new ContractRegDetail();
		detail.setBlankContract(cont);
		detail.setContractNums(contractNums);
		detail.setRegUser(regUser);
		detail.setRegDate(new Date());
		blankContractDAO.save(detail);
	}
	
	@Override
	public ListPage<BlankContract> queryBlankContractPage(BlankContractQueryParameters qp) {
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        return blankContractDAO.findPage(qp);
	}
	
	@Override
	public void deleteBlankContract(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("非法参数：ID为空");
		}
		BlankContract supp = blankContractDAO.findById(id);
		if (supp != null) {
			blankContractDAO.delete(supp);
		}
	}

	@Override
	public void deleteContractRegDetail(String detailId) throws PossException {
		if (StringUtils.isEmpty(detailId)) {
			throw new IllegalArgumentException("非法参数：ID为空");
		}
		ContractRegDetail supp = blankContractDAO.findDetailById(detailId);
		if (supp != null) {
			//扣除合同数
			BlankContract bc = supp.getBlankContract();
			bc.setRegNums(bc.getRegNums() - 1);
			bc.setContractNums(bc.getContractNums() - supp.getContractNums());
			bc.setRemainNums(bc.getRemainNums() - supp.getContractNums());
			blankContractDAO.delete(supp);
		}
		
	}

	@Override
	public List<ContractRegDetail> queryContractRegDetails(String contractId) {
		return blankContractDAO.findContractRegDetails(contractId);
	}

	@Override
	public List<ProdInfo> queryProdInfos() {
		return blankContractDAO.findProdInfos();
	}

}
