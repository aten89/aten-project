package org.eapp.poss.dao.param;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

public class MessageQueryParameters extends QueryParameters {

	private static final long serialVersionUID = -8177277267465790979L;

	public Date getSendTimeBegin() {
        return (Date) this.getParameter("sendTimeBegin");
    }

    public void setSendTimeBegin(Date sendTimeBegin) {
        if (sendTimeBegin != null) {
            this.addParameter("sendTimeBegin", sendTimeBegin);
        }
    }

    public Date getSendTimeEnd() {
        return (Date) this.getParameter("sendTimeEnd");
    }

    public void setSellDateEnd(Date sendTimeEnd) {
        if (sendTimeEnd != null) {
            this.addParameter("sendTimeEnd", sendTimeEnd);
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

    public String getSalesManager() {
        return (String) this.getParameter("salesManager");
    }

    public void setSalesManager(String salesManager) {
        if (StringUtils.isNotEmpty(salesManager)) {
            this.addParameter("salesManager", salesManager);
        }
    }
    
    public String getReceiverNo() {
    	return (String) this.getParameter("receiverNo");
    }
    
    public void setReceiverNo(String receiverNo) {
    	if (StringUtils.isNotEmpty(receiverNo)) {
    		this.addParameter("receiverNo", receiverNo);
    	}
    }
}
