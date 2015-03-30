package org.eapp.poss.blo.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IProdTypeBiz;
import org.eapp.poss.dao.IProdTypeDAO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdType;

public class ProdTypeBiz implements IProdTypeBiz {
	
	private IProdTypeDAO prodTypeDAO;

	@Override
	public ProdType getProdTypeByID(String id) {
		if(StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("ID不能为空");
		}
		
		return this.prodTypeDAO.findByID(id);
	}

	@Override
	public List<ProdType> getSubProdTypes(String id) {
		List<ProdType> groups = null;
	    if (StringUtils.isBlank(id)) {
	      groups = this.prodTypeDAO.findRootProdTypes();
	    }
	    else {
	      groups = this.prodTypeDAO.findSubProdTypes(id);
	    }
	    
	    return groups;
	}

	@Override
	public ProdType addProdType(String parentID, String prodType, String remark) throws PossException {
		ProdType parent = validateAdd(prodType, parentID);
	    
	    ProdType newProdType = new ProdType();
	    newProdType.setParentProdType(parent);

	    newProdType.setProdType(prodType);
	    newProdType.setRemark(remark);

	    this.prodTypeDAO.save(newProdType);
	    return newProdType;
	}
	
	private ProdType validateAdd(String prodType,String parentID) throws PossException{
		ProdType pg=validateCommon(prodType,parentID);
	    
	    if (this.prodTypeDAO.checkRepetition(prodType, parentID)) {
	    	throw new PossException("[产品分类:"+prodType+"]已存在");
	    }
	    
	    return pg;
	}

	private ProdType validateCommon(String prodType, String parentID)
			throws PossException {
		if (StringUtils.isBlank(prodType)) {
		    throw new IllegalArgumentException("[产品分类]不能为空");
		}
	    
	    ProdType pg = null;
	    if (StringUtils.isNotBlank(parentID)) {
	    	pg = this.prodTypeDAO.findByID(parentID);
	    	if (pg == null) {
	    		throw new IllegalArgumentException("父产品分类不存在");
	    	}
	    }
	    
	    return pg;
	}
	
	private ProdType validateModify(String prodTypeID,String prodType, String parentID) throws PossException{
		ProdType prodTypeEntity = this.prodTypeDAO.findByID(prodTypeID);
		if(prodTypeEntity == null) {
			throw new IllegalArgumentException(prodTypeID+"对象不存在");
		}
		
		validateCommon(prodType, parentID);
		
		if (!prodTypeEntity.getProdType().equals(prodType)
				&& this.prodTypeDAO.checkRepetition(prodType, parentID)) {
	    	throw new PossException("[产品分类:"+prodType+"]已存在");
	    }
		
		return prodTypeEntity;
	}

	@Override
	public ProdType modifyProdType(String prodTypeID, String parentID,
			String prodType, String remark) throws PossException {
		
		ProdType prodTypeEntity = validateModify(prodTypeID,prodType,parentID);
		
		prodTypeEntity.setProdType(prodType);
		prodTypeEntity.setRemark(remark);
		
		this.prodTypeDAO.update(prodTypeEntity);
		
		return prodTypeEntity;
	}

	@Override
	public void deleteProdType(String prodTypeID) throws PossException {
		ProdType prodTypeEntity = this.prodTypeDAO.findByID(prodTypeID);
		if(prodTypeEntity != null) {
			if(this.prodTypeDAO.existsSubProdTypes(prodTypeEntity)) {
				throw new PossException("产品分类"+prodTypeEntity.getProdType()+"存在子分类，不能直接删除。");
			}
			
			this.prodTypeDAO.delete(prodTypeEntity);
		}
	}

	@Override
	public void modifyOrder(String parentID, String[] orderSubIDs) {
		if (orderSubIDs == null) {
		      throw new IllegalArgumentException("orderSubIDs参数不能为空");
		}
	    if (orderSubIDs.length == 0) {
	      return;
	    }
	    
	    this.prodTypeDAO.saveOrder(parentID,orderSubIDs);
	}
	
	public IProdTypeDAO getProdTypeDAO() {
		return prodTypeDAO;
	}

	public void setProdTypeDAO(IProdTypeDAO prodTypeDAO) {
		this.prodTypeDAO = prodTypeDAO;
	}
}
