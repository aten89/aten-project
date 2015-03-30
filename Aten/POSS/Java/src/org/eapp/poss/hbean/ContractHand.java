package org.eapp.poss.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class ContractHand implements java.io.Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 2180773728253568486L;
    
    public static final int CHECK_STATUS_YES = 1;//已审核
    public static final int CHECK_STATUS_NO = 0;//未审核
    
    private String id;
	private ProdInfo prodInfo;
	private int checkStatus;//审核状态
	private String orgName;//所属机构
	private String regUser;//登记人
	private Date regDate;//登记时间
	private String regDept;//登记部门
	private String expressName;//快递公司名称
	private String expressNo;//配送快递单号
	private Date handDate;//上交时间
	private String handRemark;//上交备注
//	private Integer handNums;//上交合同数
	private Integer signNums;//签署合同数
	private Integer blankNums;//空白合同数
	private Integer invalidNums;//签废合同数
	private Integer unPassNums;//不通过合同数
	private String checkRemark;//审核备注
	private Date checkDate;//审核时间
	
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
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public Date getHandDate() {
		return handDate;
	}
	public void setHandDate(Date handDate) {
		this.handDate = handDate;
	}
	public String getHandRemark() {
		return handRemark;
	}
	public void setHandRemark(String handRemark) {
		this.handRemark = handRemark;
	}
//	public Integer getHandNums() {
//		return handNums;
//	}
//	public void setHandNums(Integer handNums) {
//		this.handNums = handNums;
//	}
	public Integer getSignNums() {
		return signNums;
	}
	public void setSignNums(Integer signNums) {
		this.signNums = signNums;
	}
	public Integer getBlankNums() {
		return blankNums;
	}
	public void setBlankNums(Integer blankNums) {
		this.blankNums = blankNums;
	}
	public Integer getInvalidNums() {
		return invalidNums;
	}
	public void setInvalidNums(Integer invalidNums) {
		this.invalidNums = invalidNums;
	}
	
	public Integer getUnPassNums() {
		return unPassNums;
	}
	public void setUnPassNums(Integer unPassNums) {
		this.unPassNums = unPassNums;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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
		if (prodInfo == null) {
			return null;
		}
		return prodInfo.getProdName();
	}
	
	public Integer getHandNums() {
		return signNums + blankNums + invalidNums;
	}
}