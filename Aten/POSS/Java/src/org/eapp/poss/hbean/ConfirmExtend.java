package org.eapp.poss.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class ConfirmExtend implements java.io.Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 2180773728253568486L;
    private String id;
	private ProdInfo prodInfo;
	private String orgName;//所属机构
	private Integer custNums;//客户数
	private Integer confirmNums;//确认书数目
	private String expressName;//快递公司
	private String expressNo;//快递单号
	private String remark;//备注
	private String regUser;//登记人
	private Date regDate;//登记时间
	private String regDept;//登记部门
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JSON(serialize = false)
	public ProdInfo getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getCustNums() {
		return custNums;
	}
	public void setCustNums(Integer custNums) {
		this.custNums = custNums;
	}
	public Integer getConfirmNums() {
		return confirmNums;
	}
	public void setConfirmNums(Integer confirmNums) {
		this.confirmNums = confirmNums;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRegUser() {
		return regUser;
	}
	public void setRegUser(String regUser) {
		this.regUser = regUser;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getRegDept() {
		return regDept;
	}
	public void setRegDept(String regDept) {
		this.regDept = regDept;
	}
	public String getRegUserName(){
		if (StringUtils.isBlank(regUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(regUser);
	}
	public String getProdId() {
		return prodInfo.getId();
	}
	public String getProdName() {
		return prodInfo.getProdName();
	}
}