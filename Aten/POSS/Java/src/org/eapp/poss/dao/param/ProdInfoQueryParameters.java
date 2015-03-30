/**
 * 
 */
package org.eapp.poss.dao.param;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdInfoQueryParameters extends QueryParameters {

    /**
     * 
     */
    private static final long serialVersionUID = -4991227586623723865L;

    public Date getSellDateBegin() {
        return (Date) this.getParameter("sellDateBegin");
    }

    public void setSellDateBegin(Date sellDateBegin) {
        if (sellDateBegin != null) {
            this.addParameter("sellDateBegin", sellDateBegin);
        }
    }

    public Date getSellDateEnd() {
        return (Date) this.getParameter("sellDateEnd");
    }

    public void setSellDateEnd(Date sellDateEnd) {
        if (sellDateEnd != null) {
            this.addParameter("sellDateEnd", sellDateEnd);
        }
    }

    public String getSupplierId() {
        return (String) this.getParameter("supplierId");
    }

    public void setSupplierId(String supplierId) {
        if (supplierId != null) {
            this.addParameter("supplierId", supplierId);
        }
    }

    public String getProdType() {
        return (String) this.getParameter("prodType");
    }

    public void setProdType(String prodType) {
        if (StringUtils.isNotEmpty(prodType)) {
            this.addParameter("prodType", prodType);
        }
    }

    public String getProdSecondaryClassify() {
        return (String) this.getParameter("prodSecondaryClassify");
    }

    public void setProdSecondaryClassify(String prodSecondaryClassify) {
        if (StringUtils.isNotEmpty(prodSecondaryClassify)) {
            this.addParameter("prodSecondaryClassify", prodSecondaryClassify);
        }
    }

    public String getProdStatus() {
        return (String) this.getParameter("prodStatus");
    }

    public void setProdStatus(String prodStatus) {
        if (StringUtils.isNotEmpty(prodStatus)) {
            this.addParameter("prodStatus", prodStatus);
        }
    }
    
    public String getExcProdStatus() {
        return (String) this.getParameter("excProdStatus");
    }

    public void setExcProdStatus(String excProdStatus) {
        if (StringUtils.isNotEmpty(excProdStatus)) {
            this.addParameter("excProdStatus", excProdStatus);
        }
    }
    
    public String getProdCode() {
        return (String) this.getParameter("prodCode");
    }

    public void setProdCode(String prodCode) {
        if (StringUtils.isNotEmpty(prodCode)) {
            this.addParameter("prodCode", prodCode);
        }
    }
    
    public String getProdName() {
        return (String) this.getParameter("prodName");
    }

    public void setProdName(String prodName) {
        if (StringUtils.isNotEmpty(prodName)) {
            this.addParameter("prodName", prodName);
        }
    }
    
    public String getExcludeProdId() {
        return (String) this.getParameter("excludeProdId");
    }

    public void setExcludeProdId(String excludeProdId) {
        if (StringUtils.isNotEmpty(excludeProdId)) {
            this.addParameter("excludeProdId", excludeProdId);
        }
    }

}
