package org.eapp.poss.dao.param;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

public class ProdFaqQueryParameters extends QueryParameters {

    /**
     * 
     */
    private static final long serialVersionUID = 2840156723448665068L;
    
    public Date getCreateTimeBegin() {
        return (Date) this.getParameter("createTimeBegin");
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        if (createTimeBegin != null) {
            this.addParameter("createTimeBegin", createTimeBegin);
        }
    }
    
    public Date getCreateTimeEnd() {
        return (Date) this.getParameter("createTimeEnd");
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        if (createTimeEnd != null) {
            this.addParameter("createTimeEnd", createTimeEnd);
        }
    }
    
    public String getProdInfoId() {
        return (String) this.getParameter("prodInfoId");
    }

    public void setProdInfoId(String prodInfoId) {
        if (StringUtils.isNotEmpty(prodInfoId)) {
            this.addParameter("prodInfoId", prodInfoId);
        }
    }
    
    public String getCreator() {
        return (String) this.getParameter("creator");
    }

    public void setCreator(String creator) {
        if (StringUtils.isNotEmpty(creator)) {
            this.addParameter("creator", creator);
        }
    }
    
    public Boolean getHasAnswer() {
        return (Boolean) this.getParameter("hasAnswer");
    }

    public void setHasAnswer(Boolean hasAnswer) {
        if (hasAnswer != null) {
            this.addParameter("hasAnswer", hasAnswer);
        }
    }

}
