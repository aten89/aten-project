/**
 * 
 */
package org.eapp.poss.blo.imp;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IProdIssuePlanBiz;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.IProdIssuePlanDAO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.hbean.ProdIssuePlan;

/**
 */
public class ProdIssuePlanBiz implements IProdIssuePlanBiz {
    
    //DAO
    private IProdIssuePlanDAO prodIssuePlanDAO;
    private IProdInfoDAO prodInfoDAO;

	public void setProdIssuePlanDAO(IProdIssuePlanDAO prodIssuePlanDAO) {
		this.prodIssuePlanDAO = prodIssuePlanDAO;
	}
	public void setProdInfoDAO(IProdInfoDAO prodInfoDAO) {
        this.prodInfoDAO = prodInfoDAO;
    }

	@Override
	public ProdIssuePlan addOrModifyProdIssuePlan(ProdIssuePlan transPlan) throws PossException {
		if (StringUtils.isBlank(transPlan.getId())) {
			//空字符转null
			transPlan.setId(null);
		}
		String prodId = transPlan.getProdInfo().getId();
		if (StringUtils.isBlank(prodId)) {
			throw new IllegalArgumentException("产品不存在");
		}
		ProdInfo prod = prodInfoDAO.findById(prodId);
		if (prod == null) {
			throw new IllegalArgumentException("产品不存在");
		}
		transPlan.setProdInfo(prod);
		prodIssuePlanDAO.saveOrUpdate(transPlan);
		return transPlan;
	}

	@Override
	public ProdIssuePlan getProdIssuePlanById(String id) throws PossException {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return prodIssuePlanDAO.findById(id);
	}
    
	@Override
	public ProdIssuePlan getProdIssuePlanByProdId(String prodId) throws PossException {
		if (StringUtils.isBlank(prodId)) {
			return null;
		}
		return prodIssuePlanDAO.findByProdId(prodId);
	}
   
}
