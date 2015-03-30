package org.eapp.poss.blo.imp;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.ISupplierBiz;
import org.eapp.poss.dao.ISupplierDAO;
import org.eapp.poss.dao.param.SupplierQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Supplier;
import org.eapp.util.hibernate.ListPage;

public class SupplierBiz implements ISupplierBiz {
	
	private ISupplierDAO supplierDAO;
	
	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.ISupplierBiz#addSupplier(java.lang.String, java.lang.String)
	 */
	@Override
	public void addSupplier(String supplier, String remark)
			throws PossException {
		//TODO 校验数据
		
		Supplier supp = new Supplier();
		supp.setSupplier(supplier);
		supp.setRemark(remark);
		supplierDAO.save(supp);
	}
	
	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.ISupplierBiz#querySupplierListPage(org.eapp.poss.dao.param.SupplierQueryParameters)
	 */
	@Override
	public ListPage<Supplier> querySupplierListPage(SupplierQueryParameters qp)
			throws PossException {
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        ListPage<Supplier> supplierListPage = supplierDAO.querySupplierListPage(qp);
        
        return supplierListPage;
	}

	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.ISupplierBiz#modifySupplier(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void modifySupplier(String id, String supplier, String remark)
			throws PossException {
		//TODO 校验数据
		
		Supplier supp = supplierDAO.findById(id);
		supp.setSupplier(supplier);
		supp.setRemark(remark);
		supplierDAO.saveOrUpdate(supp);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.ISupplierBiz#deleteSupplier(java.lang.String)
	 */
	@Override
	public void deleteSupplier(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("非法参数：供应商ID为空");
		}
		Supplier supp = supplierDAO.findById(id);
		if (supp == null) {
			throw new PossException("该供应商不存在！");
		}
		//TODO 数据校验 有被产品引用则不能删除？
		supplierDAO.delete(supp);
		
	}

	/**
	 * @param supplierDAO the supplierDAO to set
	 */
	public void setSupplierDAO(ISupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}
	
}
