package org.eapp.poss.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.IProdTypeBiz;
import org.eapp.poss.dto.ProdTypeSelect;
import org.eapp.poss.dto.ProdTypeTree;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdType;
import org.eapp.poss.util.HTMLResponse;

public class ProdTypeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7178734255133247816L;
	private static final Log log = LogFactory.getLog(ProdTypeAction.class);

	//para
	private String prodTypeID;
	private String prodType;
	private String parentID;
	private String remark;
	private String orderIDs;

	//result
	private ProdType prodTypeEntity;
	private String htmlValue;
	private List<ProdType> prodTypeEntitys;

	//service
	private IProdTypeBiz prodTypeBiz;

	public String initQuery() {
	    return success();
	}
	
	public String initOrder() {
		return success();
	}
	
	public String viewProdType() {
	    if (StringUtils.isBlank(this.prodTypeID)) {
	      return error("ID不能为空");
	    }
	    
	    try {
	    	prodTypeEntity = prodTypeBiz.getProdTypeByID(this.prodTypeID);
		    
		    return success();
	    }catch(Exception ex) {
	    	log.error(ex.getMessage(),ex);
	    }
	    
	    return error();
	}
	
	public String loadProdTypesTree() {
	    try {
	      List<ProdType> prodTypes = this.prodTypeBiz.getSubProdTypes(this.prodTypeID);
	      this.htmlValue = new ProdTypeTree(prodTypes).toString();
	      return success();
	    } catch (Exception e) {
	      log.error(e.getMessage(), e);
	    }return error();
	}
	
	public String initAdd() {
	    return success();
	}
	
	public String addProdType() {
		if ((StringUtils.isBlank(this.prodType))) {
		      return error("[产品分类]不能为空");
		}
		
	    try {
	      ProdType group = this.prodTypeBiz.addProdType(this.parentID, this.prodType, this.remark);

	      return success(group.getId());
	    } catch (PossException e) {
	      return error(e.getMessage());
	    } catch (Exception e) {
	      log.error(e.getMessage(), e);
	    }return error();
	}
	
	public String modifyProdType() {
		if ((StringUtils.isBlank(this.prodTypeID)) || (StringUtils.isBlank(this.prodType))) {
			return error("参数不能为空");
		}
		try {
			this.prodTypeBiz.modifyProdType(this.prodTypeID, this.parentID, this.prodType, this.remark);
			return success();
		} catch (PossException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}return error();
	}
	
	public String deleteProdType() {
		if(StringUtils.isBlank(this.prodTypeID)) {
			return error("prodTypeID参数不能为空");
		}
		
		try {
			this.prodTypeBiz.deleteProdType(this.prodTypeID);
			
			return success();
		}catch(PossException e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadSubProdTypes() {
		try {
			this.prodTypeEntitys = this.prodTypeBiz.getSubProdTypes(this.prodTypeID);
			return success();
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			return error(e.getMessage());
		}
	}
	
	public String saveProdTypeOrder() {
		if (StringUtils.isBlank(this.orderIDs)) {
		      return error("orderIDs参数不能为空");
		}
	    try {
	      String[] orderSubIDs = this.orderIDs.split(",");
	      
	      if ((orderSubIDs != null) && (orderSubIDs.length > 0)) {
	    	  this.prodTypeBiz.modifyOrder(this.parentID,orderSubIDs);
	      }
	      
	      return success();
	    } catch (Exception e) {
	      log.error(e.getMessage(), e);
	    }return error();
	}
	
	public String initProdPortlet() {
		try {
			prodTypeEntitys = prodTypeBiz.getSubProdTypes(null);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return error();
	}
	
    public void initProdTypeSel() {
        try {
            List<ProdType> prodTypes = this.prodTypeBiz.getSubProdTypes(this.prodTypeID);
            if (prodTypes == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new ProdTypeSelect(prodTypes).toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }
	
	public ProdType getProdTypeEntity() {
		return prodTypeEntity;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	public List<ProdType> getProdTypeEntitys() {
		return prodTypeEntitys;
	}

	public void setProdTypeID(String prodTypeID) {
		this.prodTypeID = prodTypeID;
	}
	
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public void setProdTypeBiz(IProdTypeBiz prodTypeBiz) {
		this.prodTypeBiz = prodTypeBiz;
	}
}
