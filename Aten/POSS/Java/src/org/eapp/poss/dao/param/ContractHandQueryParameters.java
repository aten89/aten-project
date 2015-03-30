package org.eapp.poss.dao.param;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

public class ContractHandQueryParameters extends QueryParameters {

	private static final long serialVersionUID = -2309916323836707996L;

	/**
     * @return the id
     */
    public String getProdId() {
        return (String) this.getParameter("prodId");
    }

    /**
     * @param id the id to set
     */
    public void setProdId(String prodId) {
        if (StringUtils.isNotEmpty(prodId)) {
            this.addParameter("prodId", prodId);
        }
    }
    /**
     * @return the id
     */
    public String getProdName() {
        return (String) this.getParameter("prodName");
    }

    /**
     * @param id the id to set
     */
    public void setProdName(String prodName) {
        if (StringUtils.isNotEmpty(prodName)) {
            this.addParameter("prodName", prodName);
        }
    }
    
    /**
     * @return the id
     */
    public String getOrgName() {
        return (String) this.getParameter("orgName");
    }

    /**
     * @param id the id to set
     */
    public void setOrgName(String orgName) {
        if (StringUtils.isNotEmpty(orgName)) {
            this.addParameter("orgName", orgName);
        }
    }
    
    /**
     * @return the id
     */
    public Integer getCheckStatus() {
        return (Integer) this.getParameter("checkStatus");
    }

    /**
     * @param id the id to set
     */
    public void setCheckStatus(Integer checkStatus) {
    	this.addParameter("checkStatus", checkStatus);
    }
}
