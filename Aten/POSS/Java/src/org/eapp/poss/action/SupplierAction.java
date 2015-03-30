package org.eapp.poss.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.ISupplierBiz;
import org.eapp.poss.dao.param.SupplierQueryParameters;
import org.eapp.poss.dto.SupplierSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Supplier;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.util.hibernate.ListPage;

public class SupplierAction extends BaseAction {

	private static final long serialVersionUID = 5483351324361348773L;
	private static final Log log = LogFactory.getLog(SupplierAction.class);
	
	private ISupplierBiz supplierBiz;
	
	//输入
	private String id;
	private String supplier;
	private String remark;
	
    private int pageNo;
    private int pageSize;
    private String sortCol;
    private boolean ascend;
	
	//输出
	private ListPage<Supplier> supplierListPage;
	
	
	public String addSupplier() {
		if (StringUtils.isEmpty(supplier)) {
			return error("供应商名称不能为空");
		}
		try {
            supplierBiz.addSupplier(supplier, remark);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	// method
    public String queryProdInfoListPage() {
        try {
        	SupplierQueryParameters qp = new SupplierQueryParameters();
        	
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            
        	supplierListPage = supplierBiz.querySupplierListPage(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("queryProdInfoListPage failed: ", e);
            return error();
        }
    }
    
    public String modifySupplier() {
    	if (StringUtils.isEmpty(id)) {
			return error("供应商id不能为空");
		}
    	if (StringUtils.isEmpty(supplier)) {
			return error("供应商名称不能为空");
		}
		try {
            supplierBiz.modifySupplier(id, supplier, remark);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("modifySupplier failed: ", e);
            return error();
        }
    }	
    
    public String deleteSupplier() {
    	if (StringUtils.isEmpty(id)) {
			return error("供应商id不能为空");
		}
		try {
            supplierBiz.deleteSupplier(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public void initSupplierSel() {
        try {
            SupplierQueryParameters qp = new SupplierQueryParameters();
            qp.setPageNo(1);
            qp.setPageSize(0);
            supplierListPage = supplierBiz.querySupplierListPage(qp);
            if (supplierListPage == null || supplierListPage.getDataList() == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new SupplierSelect(supplierListPage.getDataList()).toString());
        } catch (Exception e) {
            log.error("initCustomerInfoSel failed: ", e);
            return;
        }
    }

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @param supplierBiz the supplierBiz to set
	 */
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
	}

	/**
	 * @return the supplierListPage
	 */
	public ListPage<Supplier> getSupplierListPage() {
		return supplierListPage;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param sortCol the sortCol to set
	 */
	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}

	/**
	 * @param ascend the ascend to set
	 */
	public void setAscend(boolean ascend) {
		this.ascend = ascend;
	}
	
}
