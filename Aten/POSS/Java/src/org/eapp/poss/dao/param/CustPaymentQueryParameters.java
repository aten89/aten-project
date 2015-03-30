package org.eapp.poss.dao.param;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;


/**
 * 
 * 客户划款登记查询参数
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	黄云耿	新建
 * </pre>
 */
public class CustPaymentQueryParameters extends QueryParameters {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6034667683114634190L;
    
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
     * @return the flag
     */
    public String getFlag() {
        return (String) this.getParameter("flag");
    }
    
    /**
     * @param flag the flag to set
     */
    public void setFlag(String flag) {
        if (StringUtils.isNotEmpty(flag)) {
            this.addParameter("flag", flag);
        }
    }
    
    /**
     * @return the payType
     */
    public String getPayType() {
        return (String) this.getParameter("payType");
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        if (StringUtils.isNotEmpty(payType)) {
            this.addParameter("payType", payType);
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
    
    /**
     * 是否非标预约
     * @return Boolean
     */
    public Boolean getStandardFlag() {
        return (Boolean) this.getParameter("standardFlag");
    }
    
    /**
     * 设置是否非标预约
     * @param standardFlag 
     */
    public void setStandardFlag(Boolean standardFlag) {
        this.addParameter("standardFlag", standardFlag);
    }
    
    /**
     * @return the bgnTransferDate
     */
    public Date getBgnTransferDate() {
        return (Date) this.getParameter("bgnTransferDate");
    }

    /**
     * @param bgnTransferDate the bgnTransferDate to set
     */
    public void setBgnTransferDate(Date bgnTransferDate) {
        if (bgnTransferDate != null) {
            this.addParameter("bgnTransferDate", bgnTransferDate);
        }
    }

    /**
     * @return the endTransferDate
     */
    public Date getEndTransferDate() {
        return (Date) this.getParameter("endTransferDate");
    }

    /**
     * @param endTransferDate the endTransferDate to set
     */
    public void setEndTransferDate(Date endTransferDate) {
        if (endTransferDate != null) {
            this.addParameter("endTransferDate", endTransferDate);
        }
    }
    
    public static Date plusDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
    
    
    /**
     * @return the custId
     */
    public String getCustId() {
        return (String) this.getParameter("custId");
    }

    /**
     * @param custId the custId to set
     */
    public void setCustId(String custId) {
        if (StringUtils.isNotEmpty(custId)) {
            this.addParameter("custId", custId);
        }
    }
    
    /**
     * @return the custName
     */
    public String getCustName() {
        return (String) this.getParameter("custName");
    }

    /**
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        if (StringUtils.isNotEmpty(custName)) {
            this.addParameter("custName", custName);
        }
    }
    
    /**
     * @return the prodId
     */
    public String getProdId() {
        return (String) this.getParameter("prodId");
    }

    /**
     * @param prodId the prodId to set
     */
    public void setProdId(String prodId) {
        if (StringUtils.isNotEmpty(prodId)) {
            this.addParameter("prodId", prodId);
        }
    }
    
    /**
     * @return saleManId
     */
    public String getSaleManId() {
        return (String) this.getParameter("saleManId");
    }

    /**
     * @param saleManId
     */
    public void setSaleManId(String saleManId) {
        if (StringUtils.isNotEmpty(saleManId)) {
            this.addParameter("saleManId", saleManId);
        }
    }
    
    /**
     * @return formStatus
     */
    public String getFormStatus() {
        return (String) this.getParameter("formStatus");
    }

    /**
     * @param formStatus
     */
    public void setFormStatus(String formStatus) {
        if (StringUtils.isNotEmpty(formStatus)) {
            this.addParameter("formStatus", formStatus);
        }
    }
   
}
