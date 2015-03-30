package org.eapp.poss.hbean;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.eapp.poss.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * ProdInfo entity. @author MyEclipse Persistence Tools
 */

public class ProdInfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = 2081168951024877470L;
    /**
     * 简版附件
     */
    public static final String ATTCH_TYPE_JANE_EDITION = "J";
    /**
     * 非简版附件
     */
    public static final String ATTCH_TYPE_OTHER_EDITION = "O";
    
    public static final String PROD_STATUS_STATUS_FOUND = "STATUS_FOUND";//已成立
    
    private String id;
	private String prodSecondaryClassify;
	private String prodType;
	private String prodCode;
	private String prodName;
	private String financeCompanyName;
	private String prodStatus;
	private Double pjtTotalAmount;
	private String pjtAmountRemark;
	private Double sellAmount;
	private Integer sellTimeLimit;
	private String timeLimitRemark;
	private String sellRank;
	private String operationWarning;
	// 核算系数
	private Double accountCoefficient;
	private Date sellDate;
	private Date transferDeadline;
	private Date raiseFundsEndTime;
	private Date prodSetUpDate;
	private Date prodCashDate;
	private Date actualCashDate;
	private String payInterestMethod;
	private String trustBank;
	private String raiseBank;
	private String raiseAccount;
	private String prodManager;
	private String prodManagerTel;
	private String prodManagerEmail;
	private String prodSupervisor;
	private String supervisorContactWay;
	private String supervisorRemark;
	private Date expectSellDate;
	@SuppressWarnings("unused")
	private String operationPeriod;
	private Double totalAppointmentAmount;
	private Double transferAmount;
	private Double toAccountAmount;
	private Integer toAccountSmallAmount;
	private Boolean needGradeFlag;
	private String videoLectures;
	private String creator;
	private Date createTime;
	
	private Double remainAmount;//项目剩余额度（用作可用额度）
	private Boolean redeemFlag;//是否赎回
	
	/**
	 * 供应商
	 */
	private Supplier supplier;
	private Attachment otherEditionAttch;
    private Attachment janeEditionAttch;
	private Set<ProdFaq> prodFaqs = new HashSet<ProdFaq>(0);
	private Set<ExpectYearYield> expectYearYields = new HashSet<ExpectYearYield>(0);
	private Set<ProdPayDate> prodPayDates = new HashSet<ProdPayDate>(0);
	private Set<Message> messages = new HashSet<Message>(0);
	private Set<CustRefund> custRefunds = new HashSet<CustRefund>(0);
	private Set<CustPayment> custPayments = new HashSet<CustPayment>(0);
	
	private transient String prodTypeName;
	
	private transient String secondClassifyName;

	// Constructors

	/** default constructor */
	public ProdInfo() {
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProdSecondaryClassify() {
		return this.prodSecondaryClassify;
	}

	public void setProdSecondaryClassify(String prodSecondaryClassify) {
		this.prodSecondaryClassify = prodSecondaryClassify;
	}

	public String getProdType() {
		return this.prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getProdCode() {
		return this.prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdName() {
		return this.prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getFinanceCompanyName() {
		return this.financeCompanyName;
	}

	public void setFinanceCompanyName(String financeCompanyName) {
		this.financeCompanyName = financeCompanyName;
	}

	public String getProdStatus() {
		return this.prodStatus;
	}

	public void setProdStatus(String prodStatus) {
		this.prodStatus = prodStatus;
	}

	public Double getPjtTotalAmount() {
		return this.pjtTotalAmount;
	}

	public void setPjtTotalAmount(Double pjtTotalAmount) {
		this.pjtTotalAmount = pjtTotalAmount;
	}

	public String getPjtAmountRemark() {
		return this.pjtAmountRemark;
	}

	public void setPjtAmountRemark(String pjtAmountRemark) {
		this.pjtAmountRemark = pjtAmountRemark;
	}

	public Double getSellAmount() {
		return this.sellAmount;
	}

	public void setSellAmount(Double sellAmount) {
		this.sellAmount = sellAmount;
	}

	public Integer getSellTimeLimit() {
		return this.sellTimeLimit;
	}

	public void setSellTimeLimit(Integer sellTimeLimit) {
		this.sellTimeLimit = sellTimeLimit;
	}

	public String getTimeLimitRemark() {
		return this.timeLimitRemark;
	}

	public void setTimeLimitRemark(String timeLimitRemark) {
		this.timeLimitRemark = timeLimitRemark;
	}

	public String getSellRank() {
		return this.sellRank;
	}

	public void setSellRank(String sellRank) {
		this.sellRank = sellRank;
	}

	public String getOperationWarning() {
		return this.operationWarning;
	}

	public void setOperationWarning(String operationWarning) {
		this.operationWarning = operationWarning;
	}

	/**
     * get the accountCoefficient
     * @return the accountCoefficient
     */
    public Double getAccountCoefficient() {
        return accountCoefficient;
    }

    /**
     * set the accountCoefficient to set
     * @param accountCoefficient the accountCoefficient to set
     */
    public void setAccountCoefficient(Double accountCoefficient) {
        this.accountCoefficient = accountCoefficient;
    }

    @JSON(format="yyyy-MM-dd")
	public Date getSellDate() {
		return this.sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getTransferDeadline() {
		return this.transferDeadline;
	}

	public void setTransferDeadline(Date transferDeadline) {
		this.transferDeadline = transferDeadline;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getRaiseFundsEndTime() {
		return this.raiseFundsEndTime;
	}

	public void setRaiseFundsEndTime(Date raiseFundsEndTime) {
		this.raiseFundsEndTime = raiseFundsEndTime;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getProdSetUpDate() {
		return this.prodSetUpDate;
	}

	public void setProdSetUpDate(Date prodSetUpDate) {
		this.prodSetUpDate = prodSetUpDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getProdCashDate() {
		return this.prodCashDate;
	}

	public void setProdCashDate(Date prodCashDate) {
		this.prodCashDate = prodCashDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getActualCashDate() {
		return this.actualCashDate;
	}

	public void setActualCashDate(Date actualCashDate) {
		this.actualCashDate = actualCashDate;
	}

	public String getPayInterestMethod() {
		return this.payInterestMethod;
	}

	public void setPayInterestMethod(String payInterestMethod) {
		this.payInterestMethod = payInterestMethod;
	}

	public String getTrustBank() {
		return this.trustBank;
	}

	public void setTrustBank(String trustBank) {
		this.trustBank = trustBank;
	}

	public String getRaiseBank() {
		return this.raiseBank;
	}

	public void setRaiseBank(String raiseBank) {
		this.raiseBank = raiseBank;
	}

	public String getRaiseAccount() {
		return this.raiseAccount;
	}

	public void setRaiseAccount(String raiseAccount) {
		this.raiseAccount = raiseAccount;
	}

	public String getProdManager() {
		return this.prodManager;
	}

	public void setProdManager(String prodManager) {
		this.prodManager = prodManager;
	}

	public String getProdManagerTel() {
		return this.prodManagerTel;
	}

	public void setProdManagerTel(String prodManagerTel) {
		this.prodManagerTel = prodManagerTel;
	}

	public String getProdManagerEmail() {
		return this.prodManagerEmail;
	}

	public void setProdManagerEmail(String prodManagerEmail) {
		this.prodManagerEmail = prodManagerEmail;
	}

	public String getProdSupervisor() {
		return this.prodSupervisor;
	}

	public void setProdSupervisor(String prodSupervisor) {
		this.prodSupervisor = prodSupervisor;
	}

	public String getSupervisorContactWay() {
		return this.supervisorContactWay;
	}

	public void setSupervisorContactWay(String supervisorContactWay) {
		this.supervisorContactWay = supervisorContactWay;
	}

	public String getSupervisorRemark() {
		return this.supervisorRemark;
	}

	public void setSupervisorRemark(String supervisorRemark) {
		this.supervisorRemark = supervisorRemark;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getExpectSellDate() {
		return this.expectSellDate;
	}

	public void setExpectSellDate(Date expectSellDate) {
		this.expectSellDate = expectSellDate;
	}

	private void clertTime(Calendar cal) {
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    }
	
	public String getOperationPeriod() {
		if (expectSellDate == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
    	clertTime(cal);
    	long nowMil = cal.getTimeInMillis();
    	cal.setTime(expectSellDate);
    	clertTime(cal);
    	long days = (nowMil - cal.getTimeInMillis()) / (1000 * 60 * 60 * 24);
    	return Long.toString(days);
    	
//		return this.operationPeriod;
	}

	public void setOperationPeriod(String operationPeriod) {
		this.operationPeriod = operationPeriod;
	}

	public Double getTotalAppointmentAmount() {
		return this.totalAppointmentAmount;
	}

	public void setTotalAppointmentAmount(Double totalAppointmentAmount) {
		this.totalAppointmentAmount = totalAppointmentAmount;
	}

	public Double getTransferAmount() {
		return this.transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Double getToAccountAmount() {
		return this.toAccountAmount;
	}

	public void setToAccountAmount(Double toAccountAmount) {
		this.toAccountAmount = toAccountAmount;
	}

	public Integer getToAccountSmallAmount() {
		return this.toAccountSmallAmount;
	}
	
	public void setToAccountSmallAmount(Integer toAccountSmallAmount) {
		this.toAccountSmallAmount = toAccountSmallAmount;
	}

	public Boolean getNeedGradeFlag() {
		return this.needGradeFlag;
	}

	public void setNeedGradeFlag(Boolean needGradeFlag) {
		this.needGradeFlag = needGradeFlag;
	}

	public String getVideoLectures() {
		return this.videoLectures;
	}

	public void setVideoLectures(String videoLectures) {
		this.videoLectures = videoLectures;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public Double getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	public Boolean getRedeemFlag() {
		return redeemFlag;
	}

	public void setRedeemFlag(Boolean redeemFlag) {
		this.redeemFlag = redeemFlag;
	}

	/**
     * get the supplier
     * @return the supplier
     */
//	@JSON(serialize = false)
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * set the supplier to set
     * @param supplier the supplier to set
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * get the otherEditionAttch
     * @return the otherEditionAttch
     */
    @JSON(serialize = false)
    public Attachment getOtherEditionAttch() {
        return otherEditionAttch;
    }

    /**
     * set the otherEditionAttch to set
     * @param otherEditionAttch the otherEditionAttch to set
     */
    public void setOtherEditionAttch(Attachment otherEditionAttch) {
        this.otherEditionAttch = otherEditionAttch;
    }

    /**
     * get the janeEditionAttch
     * @return the janeEditionAttch
     */
    @JSON(serialize = false)
    public Attachment getJaneEditionAttch() {
        return janeEditionAttch;
    }

    /**
     * set the janeEditionAttch to set
     * @param janeEditionAttch the janeEditionAttch to set
     */
    public void setJaneEditionAttch(Attachment janeEditionAttch) {
        this.janeEditionAttch = janeEditionAttch;
    }

    @JSON(serialize = false)
    public Set<ProdFaq> getProdFaqs() {
		return this.prodFaqs;
	}

	public void setProdFaqs(Set<ProdFaq> prodFaqs) {
		this.prodFaqs = prodFaqs;
	}

    /**
     * get the expectYearYields
     * @return the expectYearYields
     */
	@JSON(serialize = false)
    public Set<ExpectYearYield> getExpectYearYields() {
        return expectYearYields;
    }

    /**
     * set the expectYearYields to set
     * @param expectYearYields the expectYearYields to set
     */
    public void setExpectYearYields(Set<ExpectYearYield> expectYearYields) {
        this.expectYearYields = expectYearYields;
    }

    /**
     * get the prodPayDates
     * @return the prodPayDates
     */
    @JSON(serialize = false)
    public Set<ProdPayDate> getProdPayDates() {
        return prodPayDates;
    }

    /**
     * set the prodPayDates to set
     * @param prodPayDates the prodPayDates to set
     */
    public void setProdPayDates(Set<ProdPayDate> prodPayDates) {
        this.prodPayDates = prodPayDates;
    }

    /**
     * get the messages
     * @return the messages
     */
    @JSON(serialize = false)
    public Set<Message> getMessages() {
        return messages;
    }

    /**
     * set the messages to set
     * @param messages the messages to set
     */
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    /**
     * get the custRefunds
     * @return the custRefunds
     */
    @JSON(serialize = false)
    public Set<CustRefund> getCustRefunds() {
        return custRefunds;
    }

    /**
     * set the custRefunds to set
     * @param custRefunds the custRefunds to set
     */
    public void setCustRefunds(Set<CustRefund> custRefunds) {
        this.custRefunds = custRefunds;
    }

    /**
     * get the custPayments
     * @return the custPayments
     */
    @JSON(serialize = false)
    public Set<CustPayment> getCustPayments() {
        return custPayments;
    }

    /**
     * set the custPayments to set
     * @param custPayments the custPayments to set
     */
    public void setCustPayments(Set<CustPayment> custPayments) {
        this.custPayments = custPayments;
    }

    /**
     * get the prodTypeName
     * @return the prodTypeName
     */
    public String getProdTypeName() {
        return prodTypeName;
    }

    /**
     * set the prodTypeName to set
     * @param prodTypeName the prodTypeName to set
     */
    public void setProdTypeName(String prodTypeName) {
        this.prodTypeName = prodTypeName;
    }

    /**
     * get the secondClassifyName
     * @return the secondClassifyName
     */
    public String getSecondClassifyName() {
        return secondClassifyName;
    }

    /**
     * set the secondClassifyName to set
     * @param secondClassifyName the secondClassifyName to set
     */
    public void setSecondClassifyName(String secondClassifyName) {
        this.secondClassifyName = secondClassifyName;
    }

    /**
     * get the prodStatusName
     * @return the prodStatusName
     */
    public String getProdStatusName() {
        if (StringUtils.isNotEmpty(prodStatus)) {
            Map<String, DataDictInfo> prodStatusMap = SysCodeDictLoader.getInstance().getProdStatusMap();
            if (prodStatusMap != null && prodStatusMap.containsKey(prodStatus)) {
                return prodStatusMap.get(prodStatus).getDictName();
            }
        }
        return "";
    }

    /**
     * get the sellRankName
     * @return the sellRankName
     */
    public String getSellRankName() {
    	if (StringUtils.isNotEmpty(sellRank)) {
            Map<String, DataDictInfo> sellRankMap = SysCodeDictLoader.getInstance().getSellRankMap();
            if (sellRankMap != null && sellRankMap.containsKey(sellRank)) {
               	return sellRankMap.get(sellRank).getDictName();
            }
        }
        return "";
    }

    
    /**
     * 计算真实的项目剩余额度
     * 发行额度-划款金额
     * @return
     */
    public Double getRealRemainAmount() {
    	if (this.sellAmount == null) {
    		return 0.0;
    	} else {
    		BigDecimal b1 = new BigDecimal(Double.toString(this.sellAmount));  
        	BigDecimal b2 = new BigDecimal(Double.toString(this.transferAmount==null?0.0:this.transferAmount));  
        	return b1.subtract(b2).doubleValue();
    	}
	}
    
    /**
     * 计算虚拟的项目剩余额度
     * 可用额度-划款金额
     * @return
     */
    public Double getVirtualRemainAmount() {
    	//
    	Double amount = remainAmount == null ? sellAmount : remainAmount;
    	if (amount == null) {
    		return 0.0;
    	} else {
    		BigDecimal b1 = new BigDecimal(Double.toString(amount));  
        	BigDecimal b2 = new BigDecimal(Double.toString(this.transferAmount==null?0.0:this.transferAmount));  
        	return b1.subtract(b2).doubleValue();
    	}
	}
    
    public String toString() {
    	StringBuffer info = new StringBuffer();
    	info.append("prodCode=").append(this.prodCode).append(",");
    	info.append("prodName=").append(this.prodName).append(",");
    	info.append("prodStatus=").append(this.prodStatus).append(",");
    	return info.toString();
    }
}