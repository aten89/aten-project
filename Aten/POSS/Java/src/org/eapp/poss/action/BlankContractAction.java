package org.eapp.poss.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IBlankContractBiz;
import org.eapp.poss.dao.param.BlankContractQueryParameters;
import org.eapp.poss.dto.ProdInfoSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRegDetail;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.util.hibernate.ListPage;

public class BlankContractAction extends BaseAction {

	private static final long serialVersionUID = 5483351324361348773L;
	private static final Log log = LogFactory.getLog(BlankContractAction.class);
	
	private IBlankContractBiz blankContractBiz;
	
	//输入
	private int pageNo;
    private int pageSize;
	private String contractId;
	private String prodId;
	private int contractNums;
	private int latestDas;
	private boolean returnFlag;
	private String prodName;
	private String detailId;
	
	
	//输出
	private ListPage<BlankContract> blankContractPage;
	private BlankContract blankContract;
	private List<ContractRegDetail> contractRegDetails;
	
	
	public ListPage<BlankContract> getBlankContractPage() {
		return blankContractPage;
	}

	public BlankContract getBlankContract() {
		return blankContract;
	}

	public List<ContractRegDetail> getContractRegDetails() {
		return contractRegDetails;
	}

	public void setBlankContractBiz(IBlankContractBiz blankContractBiz) {
		this.blankContractBiz = blankContractBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setContractNums(int contractNums) {
		this.contractNums = contractNums;
	}

	public void setLatestDas(int latestDas) {
		this.latestDas = latestDas;
	}

	public void setReturnFlag(boolean returnFlag) {
		this.returnFlag = returnFlag;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	public String initAddBlankContract() {
		if (StringUtils.isNotEmpty(contractId)) {
			blankContract = blankContractBiz.getBlankContract(contractId);
		}
		return success();
	}
	
	public String addBlankContract() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			blankContractBiz.addContractRegDetail(prodId, contractNums, latestDas, returnFlag, user.getAccountID());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	public String initQueryBlankContract() {
		return success();
	}
	
    public String queryBlankContractPage() {
        try {
        	BlankContractQueryParameters qp = new BlankContractQueryParameters();
        	
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            
            blankContractPage = blankContractBiz.queryBlankContractPage(qp);
            return success();
        } catch (Exception e) {
        	log.error("queryBlankContractPage failed: ", e);
            return error();
        }
    }
    
    public String initQueryContractRegDetail() {
		return success();
	}
    
    public String queryContractRegDetail() {
    	try {
    		contractRegDetails = blankContractBiz.queryContractRegDetails(contractId);
    		return success();
        } catch (Exception e) {
        	log.error("queryContractRegDetail failed: ", e);
            return error();
        }
    }
    
    public String deleteBlankContract() {
    	if (StringUtils.isEmpty(contractId)) {
			return error("contractId不能为空");
		}
		try {
			blankContractBiz.deleteBlankContract(contractId);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String deleteContractRegDetail() {
    	if (StringUtils.isEmpty(detailId)) {
			return error("detailId不能为空");
		}
		try {
			blankContractBiz.deleteContractRegDetail(detailId);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String loadBlankContract() {
    	if (StringUtils.isNotEmpty(prodId)) {
			blankContract = blankContractBiz.getBlankContractByProdId(prodId);
		}
		return success();
    }
    
    public void loadProdInfoSel() {
    	try {
    		List<ProdInfo> prods = blankContractBiz.queryProdInfos();
            HTMLResponse.outputHTML(getResponse(), new ProdInfoSelect(prods).toString());
        } catch (Exception e) {
            log.error("initProdInfoSel failed: ", e);
        }
    }
}
