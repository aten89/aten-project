package org.eapp.poss.dao.param;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

public class BlankContractQueryParameters extends QueryParameters {

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
}
