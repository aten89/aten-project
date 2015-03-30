/**
 * 
 */
package org.eapp.crm.dao.param;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 导入客户查询参数
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-27	lhg		新建
 * </pre>
 */
public class ImportCustomerQueryParameters extends QueryParameters {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7802258263149339008L;
    
    /**
     * @return the batchNumber
     */
    public String getBatchNumber() {
        return (String) this.getParameter("batchNumber");
    }

    /**
     * @param batchNumber the batchNumber to set
     */
    public void setBatchNumber(String batchNumber) {
        if (StringUtils.isNotEmpty(batchNumber)) {
            this.addParameter("batchNumber", batchNumber);
        }
    }
    
    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return (String) this.getParameter("customerName");
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        if (StringUtils.isNotEmpty(customerName)) {
            this.addParameter("customerName", customerName);
        }
    }
    
    /**
     * @return the tel
     */
    public String getTel() {
        return (String) this.getParameter("tel");
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        if (StringUtils.isNotEmpty(tel)) {
            this.addParameter("tel", tel);
        }
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return (String) this.getParameter("email");
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            this.addParameter("email", email);
        }
    }

    /**
     * @return allocateTimeBegin
     */
    public Date getAllocateTimeBegin() {
    	return (Date) this.getParameter("allocateTimeBegin");
    }
    
    /**
     * @param allocateTimeBegin
     */
    public void setAllocateTimeBegin(Date allocateTimeBegin) {
    	if(null != allocateTimeBegin) {
    		this.addParameter("allocateTimeBegin", allocateTimeBegin);
    	}
    }
    
    /**
     * @return allocateTimeEnd
     */
    public Date getAllocateTimeEnd() {
    	return (Date) this.getParameter("allocateTimeEnd");
    }
    
    /**
     * @param allocateTimeEnd
     */
    public void setAllocateTimeEnd(Date allocateTimeEnd) {
    	if(null != allocateTimeEnd) {
    		this.addParameter("allocateTimeEnd", allocateTimeEnd);
    	}
    }
    
    /**
     * @return allocateFlag
     */
    public Boolean getAllocateFlag() {
    	return (Boolean) this.getParameter("allocateFlag");
    }
    
    /**
     * @param allocateFlag
     */
    public void setAllocateFlag(Boolean allocateFlag) {
    	if(null != allocateFlag) {
    		this.addParameter("allocateFlag", allocateFlag);
    	}
    }
}
