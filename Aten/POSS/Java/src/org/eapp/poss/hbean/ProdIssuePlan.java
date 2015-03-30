package org.eapp.poss.hbean;

import java.util.Date;

/**
 * ProdFaq entity. @author MyEclipse Persistence Tools
 */

public class ProdIssuePlan implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6314790236041334776L;
	private String id;
	private ProdInfo prodInfo;
	private int stdOrderFlag; //是否允许进行标准预约 SMALLINT
	private String salesModel; //销售模式 VARCHAR(200)
	private String lowestStd; //最低成立标准（万） VARCHAR(50)
	private String remainAmountWarn; //剩余金额预警上限（%） VARCHAR(50)
	private String remainAmountStop; //剩余金额叫停上限（%） VARCHAR(50)
	private String smallCaps; //小额上限 VARCHAR(50)
	private String smallNumCaps; //控制小额数目上限 VARCHAR(50)
	private String description; //说明 VARCHAR(2000)
	private String managerTakeRatio; //职业经理人提成比例 VARCHAR(50)
	private String partnerTakeRatio; //合伙人提成比例 VARCHAR(50)
	private String bigAmountPoint; //大额起点（万） VARCHAR(50)
	private String lowestPayAmount; //最低打款额度（万） VARCHAR(50)
	private String sizeVolumeRatio; //大小额配比 VARCHAR(50)
	private int bigToSmallFlag; //有无大额换小额 SMALLINT
	private int smallAppointFlag; //有无小额指定 SMALLINT
	private Date preheatStartTime; //预热开始时间 TIMESTAMP
	private Date preheatEndTime; //预热结束时间 TIMESTAMP
	private Date estimateEndTime; //预计结束时间 TIMESTAMP
	private Date contractSendTime; //合同寄送时间 TIMESTAMP
	private Date dataSendTime; //募集账号和有账号资料发送时间 TIMESTAMP
	private Date prodTrainTime; //产品视频培训时间 TIMESTAMP
	
	private Date nonStdStartTime; //非标开始时间 TIMESTAMP
	private String nonStdAmount; //预计非标额度 VARCHAR(50)
	private String nonStdLimitRatio; //非标额度限制比例 VARCHAR(50)
	private String orderTimeLimit; //排序时间限制(分钟) VARCHAR(50)
	private String smallNumLimit; //小额数量限制 VARCHAR(50)
	private String nonStdCfmMin; //非标确认时间(分钟) VARCHAR(50)
	private String orderRule; //排序规则 VARCHAR(4000)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ProdInfo getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(ProdInfo prodInfo) {
		this.prodInfo = prodInfo;
	}
	public int getStdOrderFlag() {
		return stdOrderFlag;
	}
	public void setStdOrderFlag(int stdOrderFlag) {
		this.stdOrderFlag = stdOrderFlag;
	}
	public String getSalesModel() {
		return salesModel;
	}
	public void setSalesModel(String salesModel) {
		this.salesModel = salesModel;
	}
	public String getLowestStd() {
		return lowestStd;
	}
	public void setLowestStd(String lowestStd) {
		this.lowestStd = lowestStd;
	}
	public String getRemainAmountWarn() {
		return remainAmountWarn;
	}
	public void setRemainAmountWarn(String remainAmountWarn) {
		this.remainAmountWarn = remainAmountWarn;
	}
	public String getRemainAmountStop() {
		return remainAmountStop;
	}
	public void setRemainAmountStop(String remainAmountStop) {
		this.remainAmountStop = remainAmountStop;
	}
	public String getSmallCaps() {
		return smallCaps;
	}
	public void setSmallCaps(String smallCaps) {
		this.smallCaps = smallCaps;
	}
	public String getSmallNumCaps() {
		return smallNumCaps;
	}
	public void setSmallNumCaps(String smallNumCaps) {
		this.smallNumCaps = smallNumCaps;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getManagerTakeRatio() {
		return managerTakeRatio;
	}
	public void setManagerTakeRatio(String managerTakeRatio) {
		this.managerTakeRatio = managerTakeRatio;
	}
	public String getPartnerTakeRatio() {
		return partnerTakeRatio;
	}
	public void setPartnerTakeRatio(String partnerTakeRatio) {
		this.partnerTakeRatio = partnerTakeRatio;
	}
	public String getBigAmountPoint() {
		return bigAmountPoint;
	}
	public void setBigAmountPoint(String bigAmountPoint) {
		this.bigAmountPoint = bigAmountPoint;
	}
	public String getLowestPayAmount() {
		return lowestPayAmount;
	}
	public void setLowestPayAmount(String lowestPayAmount) {
		this.lowestPayAmount = lowestPayAmount;
	}
	public int getBigToSmallFlag() {
		return bigToSmallFlag;
	}
	public void setBigToSmallFlag(int bigToSmallFlag) {
		this.bigToSmallFlag = bigToSmallFlag;
	}
	public int getSmallAppointFlag() {
		return smallAppointFlag;
	}
	public void setSmallAppointFlag(int smallAppointFlag) {
		this.smallAppointFlag = smallAppointFlag;
	}
	public Date getPreheatStartTime() {
		return preheatStartTime;
	}
	public void setPreheatStartTime(Date preheatStartTime) {
		this.preheatStartTime = preheatStartTime;
	}
	public Date getPreheatEndTime() {
		return preheatEndTime;
	}
	public void setPreheatEndTime(Date preheatEndTime) {
		this.preheatEndTime = preheatEndTime;
	}
	public Date getEstimateEndTime() {
		return estimateEndTime;
	}
	public void setEstimateEndTime(Date estimateEndTime) {
		this.estimateEndTime = estimateEndTime;
	}
	public Date getContractSendTime() {
		return contractSendTime;
	}
	public void setContractSendTime(Date contractSendTime) {
		this.contractSendTime = contractSendTime;
	}
	public Date getDataSendTime() {
		return dataSendTime;
	}
	public void setDataSendTime(Date dataSendTime) {
		this.dataSendTime = dataSendTime;
	}
	public Date getProdTrainTime() {
		return prodTrainTime;
	}
	public void setProdTrainTime(Date prodTrainTime) {
		this.prodTrainTime = prodTrainTime;
	}
	public String getSizeVolumeRatio() {
		return sizeVolumeRatio;
	}
	public void setSizeVolumeRatio(String sizeVolumeRatio) {
		this.sizeVolumeRatio = sizeVolumeRatio;
	}
	public Date getNonStdStartTime() {
		return nonStdStartTime;
	}
	public void setNonStdStartTime(Date nonStdStartTime) {
		this.nonStdStartTime = nonStdStartTime;
	}
	public String getNonStdAmount() {
		return nonStdAmount;
	}
	public void setNonStdAmount(String nonStdAmount) {
		this.nonStdAmount = nonStdAmount;
	}
	public String getNonStdLimitRatio() {
		return nonStdLimitRatio;
	}
	public void setNonStdLimitRatio(String nonStdLimitRatio) {
		this.nonStdLimitRatio = nonStdLimitRatio;
	}
	public String getOrderTimeLimit() {
		return orderTimeLimit;
	}
	public void setOrderTimeLimit(String orderTimeLimit) {
		this.orderTimeLimit = orderTimeLimit;
	}
	public String getSmallNumLimit() {
		return smallNumLimit;
	}
	public void setSmallNumLimit(String smallNumLimit) {
		this.smallNumLimit = smallNumLimit;
	}
	public String getNonStdCfmMin() {
		return nonStdCfmMin;
	}
	public void setNonStdCfmMin(String nonStdCfmMin) {
		this.nonStdCfmMin = nonStdCfmMin;
	}
	public String getOrderRule() {
		return orderRule;
	}
	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}
	
	public String toString() {
    	StringBuffer info = new StringBuffer();
    	info.append("prodInfoID=").append(this.prodInfo.getId()).append(",");
    	info.append("prodName=").append(this.prodInfo.getProdName()).append(",");
    	return info.toString();
    }
	
}