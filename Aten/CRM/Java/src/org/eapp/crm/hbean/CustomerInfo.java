package org.eapp.crm.hbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;
import org.eapp.crm.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * CustomerInfo entity. @author MyEclipse Persistence Tools
 */

public class CustomerInfo implements java.io.Serializable {
	public static final int MAX_REJECT_TIMES = 3;//最多驳回次数
	
	/**
     * 不通过客户状态
     */
    public static final String UNPASS_STATUS = "-1";

    /**
     * 未提交客户状态
     */
    public static final String UNCOMMIT_STATUS = "0";
    /**
     * 回访中客户状态
     */
    public static final String RETURNVISIT_STATUS = "1";
    /**
     * 驳回客户状态
     */
    public static final String REJECT_STATUS = "2";
    /**
     * 潜在客户状态
     */
    public static final String POTENTIAL_STATUS = "3";
    /**
     *待完善客户状态
     */
    public static final String TOPERFECT_STATUS = "4";
    /**
     * 正式客户状态
     */
    public static final String OFFICIAL_STATUS = "5";
    /**
     * 数据来源--公司分配
     */
    public static final String DATA_SOURCE_COMPANY_ALLOT = "company_allot";
    /**
     * 数据来源--人工录入
     */
    public static final String DATA_SOURCE_MANUAL_ENTRY = "manual_entry";
    /**
     * statusMap
     */
    public static final Map<String, String> STATUS_MAP = new HashMap<String, String>();

    static {
    	STATUS_MAP.put(UNPASS_STATUS, "不通过");
        STATUS_MAP.put(UNCOMMIT_STATUS, "未提交");
        STATUS_MAP.put(RETURNVISIT_STATUS, "回访中");
        STATUS_MAP.put(REJECT_STATUS, "驳回");
        STATUS_MAP.put(POTENTIAL_STATUS, "潜在");
        STATUS_MAP.put(TOPERFECT_STATUS, "待完善");
        STATUS_MAP.put(OFFICIAL_STATUS, "正式");
    }
	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -924231753371700266L;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	
	private String id;
	private String custName;
	private String tel;
	private Character sex;
	private Integer age;
	private Date birthday;
	private String email;
	private String address;
	private Integer custProperty;
	private Double financingAmount;
	private String investExperience;
	private String expectedProduct;
	private String recommendProduct;
	private Integer expectedInvestAgelimit;
	private String communicationType;
	private String communicationResult;
	private Integer returnVistRejctTimes;
	private String saleMan;
	private String dataSource;
	// 0:未提交；1:回访中；2:驳回；3:潜在；4:待完善；5:正式;
	private String status;
	private Date recentContactTime;
	private Date createTime;
	private Date submitTime;
	private Set<CustomerAppointment> customerAppointments = new HashSet<CustomerAppointment>(0);
	private Set<ReturnVist> returnVists = new HashSet<ReturnVist>(0);
	private Set<CustomerConsult> customerConsults = new HashSet<CustomerConsult>(0);
	private String identityNum;
	private String bankName;
	private String bankBranch;
	private String bankAccount;
	
	private Integer memoMark;//备忘标记
	
	private Date passTime;//通过时间
	
	//投资顾问部门
	private transient String fullDeptName;
	private transient int returnVisitCount;
	private transient Date lastReturnVisitTime;
	
	private transient TelPartArea telPartArea;

	// Constructors

	/** default constructor */
	public CustomerInfo() {
	}
	
	// Property accessors

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the sex
	 */
	public Character getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(Character sex) {
		this.sex = sex;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the custProperty
	 */
	public Integer getCustProperty() {
		return custProperty;
	}

	/**
	 * @param custProperty the custProperty to set
	 */
	public void setCustProperty(Integer custProperty) {
		this.custProperty = custProperty;
	}

	/**
	 * @return the financingAmount
	 */
	public Double getFinancingAmount() {
		return financingAmount;
	}

	/**
	 * @param financingAmount the financingAmount to set
	 */
	public void setFinancingAmount(Double financingAmount) {
		this.financingAmount = financingAmount;
	}

	/**
	 * @return the investExperience
	 */
	public String getInvestExperience() {
		return investExperience;
	}

	/**
	 * @param investExperience the investExperience to set
	 */
	public void setInvestExperience(String investExperience) {
		this.investExperience = investExperience;
	}

	/**
	 * @return the expectedProduct
	 */
	public String getExpectedProduct() {
		return expectedProduct;
	}

	/**
	 * @param expectedProduct the expectedProduct to set
	 */
	public void setExpectedProduct(String expectedProduct) {
		this.expectedProduct = expectedProduct;
	}

	/**
	 * @return the recommendProduct
	 */
	public String getRecommendProduct() {
		return recommendProduct;
	}

	/**
	 * @param recommendProduct the recommendProduct to set
	 */
	public void setRecommendProduct(String recommendProduct) {
		this.recommendProduct = recommendProduct;
	}

	/**
	 * @return the expectedInvestAgelimit
	 */
	public Integer getExpectedInvestAgelimit() {
		return expectedInvestAgelimit;
	}

	/**
	 * @param expectedInvestAgelimit the expectedInvestAgelimit to set
	 */
	public void setExpectedInvestAgelimit(Integer expectedInvestAgelimit) {
		this.expectedInvestAgelimit = expectedInvestAgelimit;
	}

	/**
	 * @return the communicationType
	 */
	public String getCommunicationType() {
		return communicationType;
	}

	/**
	 * @param communicationType the communicationType to set
	 */
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	/**
	 * @return the communicationResult
	 */
	public String getCommunicationResult() {
		return communicationResult;
	}

	/**
	 * @param communicationResult the communicationResult to set
	 */
	public void setCommunicationResult(String communicationResult) {
		this.communicationResult = communicationResult;
	}

	/**
	 * @return the returnVistRejctTimes
	 */
	public Integer getReturnVistRejctTimes() {
		return returnVistRejctTimes;
	}

	/**
	 * @param returnVistRejctTimes the returnVistRejctTimes to set
	 */
	public void setReturnVistRejctTimes(Integer returnVistRejctTimes) {
		this.returnVistRejctTimes = returnVistRejctTimes;
	}

	/**
	 * @return the saleMan
	 */
	public String getSaleMan() {
		return saleMan;
	}

	/**
	 * @param saleMan the saleMan to set
	 */
	public void setSaleMan(String saleMan) {
		this.saleMan = saleMan;
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the recentContactTime
	 */
	public Date getRecentContactTime() {
		return recentContactTime;
	}

	/**
	 * @param recentContactTime the recentContactTime to set
	 */
	public void setRecentContactTime(Date recentContactTime) {
		this.recentContactTime = recentContactTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	/**
	 * @return the customerAppointments
	 */
	@JSON(serialize=false)
	public Set<CustomerAppointment> getCustomerAppointments() {
		return customerAppointments;
	}

	/**
	 * @param customerAppointments the customerAppointments to set
	 */
	public void setCustomerAppointments(
			Set<CustomerAppointment> customerAppointments) {
		this.customerAppointments = customerAppointments;
	}

	/**
	 * @return the returnVists
	 */
	@JSON(serialize=false)
	public Set<ReturnVist> getReturnVists() {
		return returnVists;
	}

	/**
	 * @param returnVists the returnVists to set
	 */
	public void setReturnVists(Set<ReturnVist> returnVists) {
		this.returnVists = returnVists;
	}

	/**
	 * @return the customerConsults
	 */
	@JSON(serialize=false)
	public Set<CustomerConsult> getCustomerConsults() {
		return customerConsults;
	}

	/**
	 * @param customerConsults the customerConsults to set
	 */
	public void setCustomerConsults(Set<CustomerConsult> customerConsults) {
		this.customerConsults = customerConsults;
	}

	/**
	 * @return the sexName
	 */
	public String getSexName() {
		Map<String, DataDictInfo> map = SysCodeDictLoader.getInstance().getSexMap();
        if (map != null && sex != null) {
        	DataDictInfo dict = map.get(String.valueOf(sex));
            if (dict != null) {
            	return dict.getDictName();
            }
        }
		return "";
	}


	/**
	 * @return the custPropertyName
	 */
	public String getCustPropertyName() {
		Map<String, DataDictInfo> map = SysCodeDictLoader.getInstance().getCustomerNatureMap();
        if (map != null && custProperty != null) {
        	DataDictInfo dict = map.get(Integer.toString(custProperty));
            if (dict != null) {
            	return dict.getDictName();
            }
        }
		return "";
	}


	/**
	 * @return the communicationTypeName
	 */
	public String getCommunicationTypeName() {
		Map<String, DataDictInfo> map = SysCodeDictLoader.getInstance().getCommTypeMap();
        if (map != null && communicationType != null) {
        	DataDictInfo dict = map.get(communicationType);
            if (dict != null) {
            	return dict.getDictName();
            }
        }
		return "";
	}

	
	 /**
     * @return the statusName
     */
    public String getStatusName() {
        return STATUS_MAP.get(status);
    }

	/**
	 * @return the saleManName
	 */
	public String getSaleManName() {
		return UsernameCache.getDisplayName(saleMan);
	}

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

	/**
	 * @return the fullDeptName
	 */
	public String getFullDeptName() {
		return fullDeptName;
	}

	/**
	 * @param fullDeptName the fullDeptName to set
	 */
	public void setFullDeptName(String fullDeptName) {
		this.fullDeptName = fullDeptName;
	}

	/**
	 * @return the submitTime
	 */
	public Date getSubmitTime() {
		return submitTime;
	}

	/**
	 * @param submitTime the submitTime to set
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankBranch
	 */
	public String getBankBranch() {
		return bankBranch;
	}

	/**
	 * @param bankBranch the bankBranch to set
	 */
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	

    public Integer getMemoMark() {
		return memoMark;
	}

	public void setMemoMark(Integer memoMark) {
		this.memoMark = memoMark;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public String getSubmitTimeStr() {
		if (submitTime == null) {
			return "";
		}
        return df.format(submitTime);
    }
	
	public String getCreateTimeStr() {
		if (createTime == null) {
			return "";
		}
        return df.format(createTime);
    }


    public int getReturnVisitCount() {
        return returnVisitCount;
    }

    public void setReturnVisitCount(int returnVisitCount) {
        this.returnVisitCount = returnVisitCount;
    }

    @JSON(format="yyyy-MM-dd")
	public Date getLastReturnVisitTime() {
		return lastReturnVisitTime;
	}

	public void setLastReturnVisitTime(Date lastReturnVisitTime) {
		this.lastReturnVisitTime = lastReturnVisitTime;
	}

	public TelPartArea getTelPartArea() {
		return telPartArea;
	}

	public void setTelPartArea(TelPartArea telPartArea) {
		this.telPartArea = telPartArea;
	}
	
	public String toString() {
		StringBuffer info = new StringBuffer();
		info.append("custName=").append(this.custName).append(",");
		info.append("tel=").append(this.tel).append(",");
		info.append("saleMan=").append(this.saleMan).append(",");
		info.append("status=").append(this.status).append(",");
		return info.toString();
	}
}