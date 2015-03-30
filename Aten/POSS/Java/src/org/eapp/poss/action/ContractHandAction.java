package org.eapp.poss.action;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IContractHandBiz;
import org.eapp.poss.dao.param.ContractHandQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ContractHand;
import org.eapp.poss.util.UserAccountInfoHelper;
import org.eapp.util.hibernate.ListPage;

public class ContractHandAction extends BaseAction {

	private static final long serialVersionUID = 5483351324361348773L;
	private static final Log log = LogFactory.getLog(ContractHandAction.class);
	
	private IContractHandBiz contractHandBiz;
	
	//输入
	private int pageNo;
    private int pageSize;
	private String id;
	private String prodId;
	private String prodName;
	private Integer checkStatus;
	private String expressName;//快递公司名称
	private String expressNo;//配送快递单号
	private Date handDate;//上交时间
	private String handRemark;//上交备注
	private Integer signNums;//签署合同数
	private Integer blankNums;//空白合同数
	private Integer invalidNums;//签废合同数
	private Integer unPassNums;//不通过合同数
	private String checkRemark;//审核备注
	
	
	//输出
	private ListPage<ContractHand> contractHandPage;
	private ListPage<ContractHandStat> contractHandStatPage;
	private ContractHand contractHand;
	private String orgName;//所属机构
	
	public ListPage<ContractHand> getContractHandPage() {
		return contractHandPage;
	}

	public ListPage<ContractHandStat> getContractHandStatPage() {
		return contractHandStatPage;
	}

	public ContractHand getContractHand() {
		return contractHand;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setContractHandBiz(IContractHandBiz contractHandBiz) {
		this.contractHandBiz = contractHandBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public void setHandDate(Date handDate) {
		this.handDate = handDate;
	}

	public void setHandRemark(String handRemark) {
		this.handRemark = handRemark;
	}

	public void setSignNums(Integer signNums) {
		this.signNums = signNums;
	}

	public void setBlankNums(Integer blankNums) {
		this.blankNums = blankNums;
	}

	public void setInvalidNums(Integer invalidNums) {
		this.invalidNums = invalidNums;
	}

	public void setUnPassNums(Integer unPassNums) {
		this.unPassNums = unPassNums;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}

	public String initEditContractHand() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		if (StringUtils.isNotEmpty(id)) {
			contractHand = contractHandBiz.getContractHand(id);
		} else {
			contractHand = new ContractHand();
			contractHand.setRegUser(user.getAccountID());
			contractHand.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
		}
		return success();
	}
	
	public String addContractHand() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			contractHandBiz.addContractHand(prodId, signNums, blankNums, invalidNums, expressName, expressNo, handDate, handRemark, user.getAccountID(), user.getDeptNames());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String modifyContractHand() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			contractHandBiz.modifyContractHand(id, signNums, blankNums, invalidNums, expressName, expressNo, handDate, handRemark);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String initQueryContractHand() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        orgName = UserAccountInfoHelper.getOrgName(user.getAccountID());
		return success();
	}
	
    public String queryContractHandPage() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        try {
        	ContractHandQueryParameters qp = new ContractHandQueryParameters();
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            if (checkStatus != null) {
            	qp.setCheckStatus(checkStatus);
            }
            qp.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
            
            contractHandStatPage = contractHandBiz.queryContractHandStatPage(qp);
            return success();
        } catch (Exception e) {
        	log.error("queryBlankContractPage failed: ", e);
            return error();
        }
    }
    
    public String initQueryContractHandDetail() {
		return success();
	}
    
    public String queryContractHandDetail() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
        	return error("请先登录");
        }
    	try {
    		ContractHandQueryParameters qp = new ContractHandQueryParameters();
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            if (checkStatus != null) {
            	qp.setCheckStatus(checkStatus);
            }
            qp.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
            contractHandPage = contractHandBiz.queryContractHandPage(qp);
    		return success();
        } catch (Exception e) {
        	log.error("queryContractRegDetail failed: ", e);
            return error();
        }
    }
    
    public String deleteContractHand() {
    	if (StringUtils.isEmpty(id)) {
			return error("id不能为空");
		}
		try {
			contractHandBiz.deleteContractHand(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String deleteAllContractHand() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
        	return error("请先登录");
        }
    	if (StringUtils.isEmpty(prodId)) {
			return error("prodId不能为空");
		}
		try {
			contractHandBiz.deleteAllContractHand(prodId, UserAccountInfoHelper.getOrgName(user.getAccountID()));
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String initQueryCheckContractHand() {
		return success();
	}
    
    public String initCheckContractHand() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		contractHand = contractHandBiz.getContractHand(id);
		return success();
	}
    
    public String checkContractHand() {
    	try {
    		contractHandBiz.txCheckHand(id, signNums, blankNums, invalidNums, unPassNums, checkRemark);
    		 return success();
    	} catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
	}

}
