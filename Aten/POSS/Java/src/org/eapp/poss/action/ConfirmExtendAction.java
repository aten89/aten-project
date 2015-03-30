package org.eapp.poss.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IConfirmExtendBiz;
import org.eapp.poss.dao.param.ConfirmExtendQueryParameters;
import org.eapp.poss.dto.OrgNameSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ConfirmExtend;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.util.hibernate.ListPage;

public class ConfirmExtendAction extends BaseAction {

	private static final long serialVersionUID = 5483351324361348773L;
	private static final Log log = LogFactory.getLog(ConfirmExtendAction.class);
	
	private IConfirmExtendBiz confirmExtendBiz;
	
	//输入
	private int pageNo;
    private int pageSize;
	private String id;
	private String prodId;
	private String prodName;
	private String orgName;//所属机构
	private Integer custNums;//客户数
	private Integer confirmNums;//确认书数目
	private String expressName;//快递公司
	private String expressNo;//快递单号
	private String remark;//备注
	
	
	//输出
	private ListPage<ConfirmExtend> confirmExtendPage;
	private ConfirmExtend confirmExtend;
	
	

	public ListPage<ConfirmExtend> getConfirmExtendPage() {
		return confirmExtendPage;
	}

	public ConfirmExtend getConfirmExtend() {
		return confirmExtend;
	}

	public void setConfirmExtendBiz(IConfirmExtendBiz confirmExtendBiz) {
		this.confirmExtendBiz = confirmExtendBiz;
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

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setCustNums(Integer custNums) {
		this.custNums = custNums;
	}

	public void setConfirmNums(Integer confirmNums) {
		this.confirmNums = confirmNums;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String initEditConfirmExtend() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		if (StringUtils.isNotEmpty(id)) {
			confirmExtend = confirmExtendBiz.getConfirmExtend(id);
		}
		return success();
	}
	
	public String addConfirmExtend() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			confirmExtendBiz.addConfirmExtend(prodId, orgName, custNums, confirmNums, expressName, 
					expressNo, remark, user.getAccountID(), user.getDeptNames());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String modifyConfirmExtend() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			confirmExtendBiz.modifyConfirmExtend(id, custNums, confirmNums, expressName, expressNo, remark);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("addSupplier failed: ", e);
            return error();
        }
	}
	
	public String initQueryConfirmExtend() {
		return success();
	}
	
    public String queryConfirmExtend() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        try {
        	ConfirmExtendQueryParameters qp = new ConfirmExtendQueryParameters();
        	qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(prodName)) {
                qp.setProdName(prodName);
            }
            if (StringUtils.isNotEmpty(orgName)) {
                qp.setOrgName(orgName);
            }
            qp.addOrder("regDate", false);
            confirmExtendPage = confirmExtendBiz.queryConfirmExtendPage(qp);
            return success();
        } catch (Exception e) {
        	log.error("queryBlankContractPage failed: ", e);
            return error();
        }
    }
    
    public String deleteConfirmExtend() {
    	if (StringUtils.isEmpty(id)) {
			return error("id不能为空");
		}
		try {
			confirmExtendBiz.deleteConfirmExtend(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
            return error();
        }
    }
    
    
    public void loadOrgNameSel() {
    	try {
    		List<String> orgNames = confirmExtendBiz.getOrgNames();
    		HTMLResponse.outputHTML(getResponse(), new OrgNameSelect(orgNames).toString());
        } catch (Exception e) {
        	log.error("deleteSupplier failed: ", e);
        }
	}

}
