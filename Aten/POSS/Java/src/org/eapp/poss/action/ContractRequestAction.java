package org.eapp.poss.action;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IContractRequestBiz;
import org.eapp.poss.dao.param.ContractRequestQueryParameters;
import org.eapp.poss.dto.ContractRequestStat;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ContractRequest;
import org.eapp.poss.util.UserAccountInfoHelper;
import org.eapp.util.hibernate.ListPage;

public class ContractRequestAction extends BaseAction {

	private static final long serialVersionUID = 5483351324361348773L;
	private static final Log log = LogFactory.getLog(ContractRequestAction.class);
	
	private IContractRequestBiz contractRequestBiz;
	
	//输入
	private int pageNo;
    private int pageSize;
	private String id;
	private String prodId;
	private String prodName;
	private Integer regStatus;
	private Integer reqNums;//所需合同数
	private Integer extendNums;//实际发放数
	private String expressName;//快递公司名称
	private String expressNo;//配送快递单号
	private Date sendDate;//配送时间
	private String reqRemark;//需求备注
	private String extendRemark;//发放备注
	
	
	//输出
	private ListPage<ContractRequest> contractRequestPage;
	private ListPage<ContractRequestStat> contractRequestStatPage;
	private ContractRequest contractRequest;
	private String orgName;//所属机构
	
	
	public ListPage<ContractRequest> getContractRequestPage() {
		return contractRequestPage;
	}

	public ListPage<ContractRequestStat> getContractRequestStatPage() {
		return contractRequestStatPage;
	}

	public ContractRequest getContractRequest() {
		return contractRequest;
	}

	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public void setContractRequestBiz(IContractRequestBiz contractRequestBiz) {
		this.contractRequestBiz = contractRequestBiz;
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

	public void setRegStatus(Integer regStatus) {
		this.regStatus = regStatus;
	}
	
	public void setReqNums(Integer reqNums) {
		this.reqNums = reqNums;
	}

	public void setExtendNums(Integer extendNums) {
		this.extendNums = extendNums;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;
	}

	public void setExtendRemark(String extendRemark) {
		this.extendRemark = extendRemark;
	}

	public String initEditContractRequest() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		if (StringUtils.isNotEmpty(id)) {
			contractRequest = contractRequestBiz.getContractRequest(id);
		} else {
			contractRequest = new ContractRequest();
			contractRequest.setRegUser(user.getAccountID());
			contractRequest.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
		}
		return success();
	}
	
	public String addContractRequest() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			contractRequestBiz.addContractRequest(prodId, reqNums, reqRemark, user.getAccountID(), user.getDeptNames());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String modifyContractRequest() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			contractRequestBiz.modifyContractRequest(id, reqNums, reqRemark);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String initQueryContractRequest() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        orgName = UserAccountInfoHelper.getOrgName(user.getAccountID());
		return success();
	}
	
    public String queryContractRequestPage() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        try {
        	ContractRequestQueryParameters qp = new ContractRequestQueryParameters();
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            if (regStatus != null) {
            	qp.setRegStatus(regStatus);
            }
            qp.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
            
            contractRequestStatPage = contractRequestBiz.queryContractRequestStatPage(qp);
            return success();
        } catch (Exception e) {
        	log.error("queryBlankContractPage failed: ", e);
            return error();
        }
    }
    
    public String initQueryContractRequestDetail() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	orgName = UserAccountInfoHelper.getOrgName(user.getAccountID());
		return success();
	}
    
    public String queryContractRequestDetail() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
        	return error("请先登录");
        }
    	try {
    		ContractRequestQueryParameters qp = new ContractRequestQueryParameters();
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            if (regStatus != null) {
            	qp.setRegStatus(regStatus);
            }
            if (orgName != null) {
            	qp.setOrgName(orgName);
            }
//            qp.setOrgName(UserAccountInfoHelper.getOrgName(user.getAccountID()));
            contractRequestPage = contractRequestBiz.queryContractRequestPage(qp);
    		return success();
        } catch (Exception e) {
        	log.error("queryContractRegDetail failed: ", e);
            return error();
        }
    }
    
    public String deleteContractRequest() {
    	if (StringUtils.isEmpty(id)) {
			return error("id不能为空");
		}
		try {
			contractRequestBiz.deleteContractRequest(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String deleteAllContractRequest() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
        	return error("请先登录");
        }
    	if (StringUtils.isEmpty(prodId)) {
			return error("prodId不能为空");
		}
		try {
			contractRequestBiz.deleteAllContractRequest(prodId, UserAccountInfoHelper.getOrgName(user.getAccountID()));
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    public String initQueryCheckContractRequest() {
		return success();
	}
    
    public String initCheckContractRequest() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		contractRequest = contractRequestBiz.getContractRequest(id);
		return success();
	}
    
    public String checkContractRequest() {
    	try {
    		contractRequestBiz.txCheckExtend(id, extendNums, expressName, expressNo, sendDate, extendRemark);
    		 return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
	}
    
    public String confirmContractRequest() {
    	try {
    		contractRequestBiz.txConfirmReceive(id);
    		return success();
    	} catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
}
