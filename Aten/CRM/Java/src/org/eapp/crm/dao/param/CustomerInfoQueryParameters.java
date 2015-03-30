package org.eapp.crm.dao.param;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.crm.util.Tools;
import org.eapp.util.hibernate.QueryParameters;


/**
 * 客户信息查询参数
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public class CustomerInfoQueryParameters extends QueryParameters {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5177178023520789287L;

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
     * @return the saleMan
     */
    public String getSaleMan() {
        return (String) this.getParameter("saleMan");
    }

    /**
     * @param saleMan the saleMan to set
     */
    public void setSaleMan(String saleMan) {
        if (StringUtils.isNotEmpty(saleMan)) {
            this.addParameter("saleMan", saleMan);
        }
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return (String) this.getParameter("status");
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        if (StringUtils.isNotEmpty(status)) {
            this.addParameter("status", status);
        }
    }

    /**
     * 获得multipleStatus列表
     * 
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getMultipleStatus() {
        return (List<String>) this.getParameter("multipleStatus");
    }

    /**
     * 设置multipleStatus列表
     * 
     * @param multipleStatus multipleStatus列表
     */
    public void setMultipleStatus(List<String> multipleStatus) {
        if (multipleStatus != null && !multipleStatus.isEmpty()) {
            this.addParameter("multipleStatus", multipleStatus);
        }
    }

    public String getDataSource() {
        return (String) this.getParameter("dataSource");
    }

    public void setDataSource(String dataSource) {
        if (StringUtils.isNotEmpty(dataSource)) {
            this.addParameter("dataSource", dataSource);
        }
    }

    /**
     * 获得id列表
     * 
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getIds() {
        return (List<String>) this.getParameter("ids");
    }

    /**
     * 设置id列表
     * 
     * @param ids id列表
     */
    public void setIds(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.addParameter("ids", ids);
        }
    }

    /**
     * @return recommendProduct
     */
    public String getRecommendProduct() {
        return (String) this.getParameter("recommendProduct");
    }

    /**
     * @param recommendProduct
     */
    public void setRecommendProduct(String recommendProduct) {
        if (StringUtils.isNotEmpty(recommendProduct)) {
            this.addParameter("recommendProduct", recommendProduct);
        }
    }

    public String getEmail() {
        return (String) this.getParameter("email");
    }

    public void setEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            this.addParameter("email", email);
        }
    }

    public String getCustName() {
        return (String) this.getParameter("custName");
    }

    public void setCustName(String custName) {
        if (StringUtils.isNotEmpty(custName)) {
            this.addParameter("custName", custName);
        }
    }

    /**
     * 获得saleMans列表
     * 
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getSaleMans() {
        return (List<String>) this.getParameter("saleMans");
    }

    /**
     * 设置saleMans列表
     * 
     * @param saleMans saleMans列表
     */
    public void setSaleMans(List<String> saleMans) {
        if (saleMans != null && !saleMans.isEmpty()) {
            this.addParameter("saleMans", saleMans);
        }
    }

    public String getTel() {
        return (String) this.getParameter("tel");
    }

    public void setTel(String tel) {
        if (StringUtils.isNotEmpty(tel)) {
            this.addParameter("tel", tel);
        }
    }
    
    /**
     * @return the bgnSubmitTime
     */
    public Date getBgnSubmitTime() {
        return (Date) this.getParameter("bgnSubmitTime");
    }

    /**
     * @param bgnSubmitTime the bgnSubmitTime to set
     */
    public void setBgnSubmitTime(Date bgnSubmitTime) {
        if (bgnSubmitTime != null) {
            this.addParameter("bgnSubmitTime", bgnSubmitTime);
        }
    }

    /**
     * @return the endSubmitTime
     */
    public Date getEndSubmitTime() {
        return (Date) this.getParameter("endSubmitTime");
    }

    /**
     * @param endSubmitTime the endSubmitTime to set
     */
    public void setEndSubmitTime(Date endSubmitTime) {
        if (endSubmitTime != null) {
            this.addParameter("endSubmitTime", Tools.plusDay(endSubmitTime, 1));
        }
    }

    /**
     * @return the id
     */
    public String getSaleGroupId() {
        return (String) this.getParameter("saleGroupId");
    }

    /**
     * @param id the id to set
     */
    public void setSaleGroupId(String saleGroupId) {
        if (StringUtils.isNotEmpty(saleGroupId)) {
            this.addParameter("saleGroupId", saleGroupId);
        }
    }
    
    /**
     * @return the bgnSubmitTime
     */
    public Date getBgnVistTime() {
        return (Date) this.getParameter("bgnVistTime");
    }

    /**
     * @param bgnSubmitTime the bgnSubmitTime to set
     */
    public void setBgnVistTime(Date bgnVistTime) {
        if (bgnVistTime != null) {
            this.addParameter("bgnVistTime", bgnVistTime);
        }
    }

    /**
     * @return the endSubmitTime
     */
    public Date getEndVistTime() {
        return (Date) this.getParameter("endVistTime");
    }

    /**
     * @param endSubmitTime the endSubmitTime to set
     */
    public void setEndVistTime(Date endVistTime) {
        if (endVistTime != null) {
            this.addParameter("endVistTime", Tools.plusDay(endVistTime, 1));
        }
    }

}
