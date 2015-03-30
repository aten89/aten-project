package org.eapp.poss.dao.param;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 
 * 退款表单查询参数
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	钟华杰	新建
 * </pre>
 */
public class CustRefundQueryParameters extends QueryParameters {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6038667683114634190L;
    
    /**
     * @return the id
     */
    public String getId() {
        return (String) this.getParameter("id");
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        if (StringUtils.isNotEmpty(id)) {
            this.addParameter("id", id);
        }
    }
    
    /**
     * @return the currentUserId
     */
    public String getCurrentUserId() {
        return (String) this.getParameter("currentUserId");
    }

    /**
     * @param currentUserId the currentUserId to set
     */
    public void setCurrentUserId(String currentUserId) {
        if (StringUtils.isNotEmpty(currentUserId)) {
            this.addParameter("currentUserId", currentUserId);
        }
    }
    
    public Date getApplyTimeBegin() {
    	return (Date) this.getParameter("applyTimeBegin");
    }
    
    public void setApplyTimeBegin(Date applyTimeBegin) {
    	if (null != applyTimeBegin) {
            this.addParameter("applyTimeBegin", applyTimeBegin);
        }
    }
    
    public Date getApplyTimeEnd() {
    	return (Date) this.getParameter("applyTimeEnd");
    }
    
    public void setApplyTimeEnd(Date applyTimeEnd) {
    	if (null != applyTimeEnd) {
    		this.addParameter("applyTimeEnd", applyTimeEnd);
    	}
    }
    
    public String getCustId() {
        return (String) this.getParameter("custId");
    }

    public void setCustId(String custId) {
        if (StringUtils.isNotEmpty(custId)) {
            this.addParameter("custId", custId);
        }
    }
    
    public String getSaleManId() {
    	return (String) this.getParameter("saleManId");
    }
    
    public void setSaleManId(String saleManId) {
    	if (StringUtils.isNotEmpty(saleManId)) {
    		this.addParameter("saleManId", saleManId);
    	}
    }
    
    public String getProdId() {
    	return (String) this.getParameter("prodId");
    }
    
    public void setProdId(String prodId) {
    	if (StringUtils.isNotEmpty(prodId)) {
    		this.addParameter("prodId", prodId);
    	}
    }
}
