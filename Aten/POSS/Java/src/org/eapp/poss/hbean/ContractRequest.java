package org.eapp.poss.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class ContractRequest implements java.io.Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 2180773728253568486L;
    
    public static final int REG_STATUS_REGIEST = 1;//未发放
    public static final int REG_STATUS_SEND = 2;//已发放
    public static final int REG_STATUS_RECEIVE = 3;//已领取
    
    private String id;
	private ProdInfo prodInfo;
	private int regStatus;//登记状态
	private String orgName;//所属机构
	private Integer reqNums;//所需合同数
	private Integer extendNums;//实际发放数
	private String regUser;//登记人
	private Date regDate;//登记时间
	private String regDept;//登记部门
	private String expressName;//快递公司名称
	private String expressNo;//配送快递单号
	private Date sendDate;//配送时间
	private String reqRemark;//需求备注
	private String extendRemark;//发放备注
	private Date extendDate;//发放时间
//	private Boolean scopeFlag;//是否在发行范围
	private Boolean firstFlag;//是否首次登记
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
	public int getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(int regStatus) {
		this.regStatus = regStatus;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getReqNums() {
		return reqNums;
	}
	public void setReqNums(Integer reqNums) {
		this.reqNums = reqNums;
	}
	public Integer getExtendNums() {
		return extendNums;
	}
	public void setExtendNums(Integer extendNums) {
		this.extendNums = extendNums;
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
	@JSON(format="yyyy-MM-dd")
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getReqRemark() {
		return reqRemark;
	}
	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;
	}
	public String getExtendRemark() {
		return extendRemark;
	}
	public void setExtendRemark(String extendRemark) {
		this.extendRemark = extendRemark;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getExtendDate() {
		return extendDate;
	}
	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}
	public Boolean getFirstFlag() {
		return firstFlag;
	}
	public void setFirstFlag(Boolean firstFlag) {
		this.firstFlag = firstFlag;
	}
	public String getRegUserName(){
		if (StringUtils.isBlank(regUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(regUser);
	}
	public String getProdId() {
		if (prodInfo == null) {
			return null;
		}
		return prodInfo.getId();
	}
	public String getProdName() {
		return prodInfo.getProdName();
	}
}