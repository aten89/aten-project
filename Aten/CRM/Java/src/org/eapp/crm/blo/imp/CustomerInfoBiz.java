/**
 * 
 */
package org.eapp.crm.blo.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.UsernameCache;
import org.eapp.crm.blo.ICustomerInfoBiz;
import org.eapp.crm.config.SysCodeDictLoader;
import org.eapp.crm.dao.ICustomerAppointmentDAO;
import org.eapp.crm.dao.ICustomerInfoDAO;
import org.eapp.crm.dao.ICustomerServiceDAO;
import org.eapp.crm.dao.ITelPartAreaDAO;
import org.eapp.crm.dao.IUserAccountExtDAO;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.dto.AutoCompleteData;
import org.eapp.crm.excelimp.ExcelCellToValue;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.crm.hbean.CustomerConsult;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.crm.hbean.TelPartArea;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

/**
 * 客户信息业务逻辑层实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public class CustomerInfoBiz implements ICustomerInfoBiz {

    /**
     * 导入客户DAO接口
     */
    private ICustomerInfoDAO customerInfoDAO;
    /**
     * 预约记录DAO借口接口
     */
    private ICustomerAppointmentDAO customerAppointmentDAO;
    
    private IUserAccountExtDAO userAccountExtDAO;
    
    private ITelPartAreaDAO telPartAreaDAO;
    /**
     * 客服管理DAO接口
     */
    private ICustomerServiceDAO customerServiceDAO;

	public void setCustomerInfoDAO(ICustomerInfoDAO customerInfoDAO) {
        this.customerInfoDAO = customerInfoDAO;
    }

	public void setCustomerAppointmentDAO(
			ICustomerAppointmentDAO customerAppointmentDAO) {
		this.customerAppointmentDAO = customerAppointmentDAO;
	}

	/**
	 * @param userAccountExtDAO the userAccountExtDAO to set
	 */
	public void setUserAccountExtDAO(IUserAccountExtDAO userAccountExtDAO) {
		this.userAccountExtDAO = userAccountExtDAO;
	}

	public void setTelPartAreaDAO(ITelPartAreaDAO telPartAreaDAO) {
		this.telPartAreaDAO = telPartAreaDAO;
	}
	
	public void setCustomerServiceDAO(ICustomerServiceDAO customerServiceDAO) {
        this.customerServiceDAO = customerServiceDAO;
    }
	
    /**
     * 组织机构
     */
    private GroupService gs = new GroupService();
    /**
     * 用户账号服务接口
     */
    private UserAccountService uas = new UserAccountService();

    @Override
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp) {
        ListPage<CustomerInfo> customerInfos = customerInfoDAO.queryCustomerInfoList(qp);
        if(customerInfos != null && customerInfos.getDataList() != null){
            for(CustomerInfo customerInfo : customerInfos.getDataList()){
                Hibernate.initialize(customerInfo.getCustomerAppointments());
                Hibernate.initialize(customerInfo.getCustomerConsults());
                Hibernate.initialize(customerInfo.getReturnVists());
              //回访次数
                List<ReturnVist> returnVists = customerServiceDAO.queryVisitRecordList(customerInfo.getId(), null);
                customerInfo.setReturnVisitCount(returnVists.size());
            }
        }
        
        return customerInfos;
    }
    
    @Override
    public void txChangeSaleMan(String[] custIds, String customerManage) throws CrmException{
    	CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
        qp.setIds(Arrays.asList(custIds));
        ListPage<CustomerInfo> customerInfos = customerInfoDAO.queryCustomerInfoList(qp);
        //转移客户
        if(customerInfos != null && customerInfos.getDataList() != null){
            for(CustomerInfo customer : customerInfos.getDataList()){
            	if (customerManage.equals(customer.getSaleMan())) {
            		continue;
            	}
                customer.setSaleMan(customerManage);
                customerInfoDAO.update(customer);
            }
        }
    }
    
    @Override
    public CustomerInfo txDeleteCustomerInfo(String customerId) throws CrmException {
        if (StringUtils.isEmpty(customerId)) {
            throw new IllegalArgumentException("非法参数：客户ID为空");
        }
        CustomerInfo customerInfo = customerInfoDAO.findById(customerId);
        if(customerInfo == null){
            throw new CrmException("客户不存在！");
        }
        //客户底下相应的记录也需要同步删除
        Set<CustomerAppointment> customerAppointments = customerInfo.getCustomerAppointments();
        Set<ReturnVist> returnVists = customerInfo.getReturnVists();
        Set<CustomerConsult> customerConsults = customerInfo.getCustomerConsults();
        if(customerAppointments != null){
            for(CustomerAppointment customerAppointment : customerAppointments){
                customerInfoDAO.delete(customerAppointment);
            }
        }
        if(returnVists != null){
            for(ReturnVist returnVist : returnVists){
                customerInfoDAO.delete(returnVist);
            }
        }
        if(customerConsults != null){
            for(CustomerConsult customerConsult : customerConsults){
                customerInfoDAO.delete(customerConsult);
            }
        }
        
    	customerInfoDAO.delete(customerInfo);
    	return customerInfo;
    }
    
    @Override
    public ListPage<CustomerConsult> queryConsultRecordList(CustomerInfoQueryParameters qp) throws CrmException {
        ListPage<CustomerConsult> consultRecords = customerInfoDAO.queryConsultRecordList(qp);
        if(consultRecords != null && consultRecords.getDataList() != null){
            for(CustomerConsult consultRecord : consultRecords.getDataList()){
                Date consultTime = consultRecord.getConsultTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                if(consultTime != null){
                    consultRecord.setConsultTimeStr(df.format(consultTime));
                }
                
                CustomerInfo customerInfo = consultRecord.getCustomerInfo();
                if(customerInfo != null){
                    Hibernate.initialize(customerInfo.getCustomerAppointments());
                    Hibernate.initialize(customerInfo.getCustomerConsults());
                    Hibernate.initialize(customerInfo.getReturnVists());
                }
            }
        }
        return consultRecords;
    }
    
    @Override
    public ListPage<ReturnVist> queryVisitRecordList(CustomerInfoQueryParameters qp) throws CrmException {
        ListPage<ReturnVist> visitRecords = customerInfoDAO.queryVisitRecordList(qp);
        if(visitRecords != null && visitRecords.getDataList() != null){
            for(ReturnVist returnVist : visitRecords.getDataList()){
                Date returnVistTime = returnVist.getReturnVistTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                if(returnVistTime != null){
                    returnVist.setReturnVistTimeStr(df.format(returnVistTime));
                }
                
                String returnVistUserName= UsernameCache.getDisplayName(returnVist.getReturnVistUser());
                returnVist.setReturnVistUserName(returnVistUserName);

                CustomerInfo customerInfo = returnVist.getCustomerInfo();
                if(customerInfo != null){
                    Hibernate.initialize(customerInfo.getCustomerAppointments());
                    Hibernate.initialize(customerInfo.getCustomerConsults());
                    Hibernate.initialize(customerInfo.getReturnVists());
                }
            }
        }
        return visitRecords;
    }
    
    @Override
    public ListPage<CustomerAppointment> queryAppointmentRecordList(
    		CustomerInfoQueryParameters qp) throws CrmException {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	ListPage<CustomerAppointment> appointmentRecords = customerInfoDAO.queryAppointmentRecordList(qp);
        if(appointmentRecords != null && appointmentRecords.getDataList() != null){
            for(CustomerAppointment appointmentRecord : appointmentRecords.getDataList()){
                CustomerInfo customerInfo = appointmentRecord.getCustomerInfo();
                if(customerInfo != null){
                    Hibernate.initialize(customerInfo.getCustomerAppointments());
                    Hibernate.initialize(customerInfo.getCustomerConsults());
                    Hibernate.initialize(customerInfo.getReturnVists());
                }
                
                //设置时间格式
                if(appointmentRecord.getAppointmentTime() != null){
                	appointmentRecord.setAppointmentTimeStr(df.format(appointmentRecord.getAppointmentTime()));
                }
                
                //设置通知类型
                if(null != appointmentRecord.getAppointmentType()) {
                	Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getAppointmentTypeMap().values();
                	String appointmentTypeStr = "";
                	String[] appointmentTypeArry = appointmentRecord.getAppointmentType().split(",");
                	for(String appointmentType : appointmentTypeArry) {
                		for(DataDictInfo dataDictInfo : list) {
                			if(appointmentType.equals(dataDictInfo.getDictCode())) {
                				appointmentTypeStr += dataDictInfo.getDictName();
                				break;
                			}
                		}
                		appointmentTypeStr += "+";
                	}
                	if(!StringUtils.isEmpty(appointmentTypeStr)) {
                		appointmentTypeStr = appointmentTypeStr.substring(0, appointmentTypeStr.length() - 1);
                	}
                	
                	appointmentRecord.setAppointmentTypeStr(appointmentTypeStr);
                	
                	//设置提交人名称
                    appointmentRecord.setCreateorName(UsernameCache.getDisplayName(appointmentRecord.getCreateor()));
                }
            }
        }
        return appointmentRecords;
    }
    
    @Override
    public void addConsultRecord(String customerId, String consultContent, String accountID) throws CrmException {
        if (StringUtils.isEmpty(customerId)) {
            throw new IllegalArgumentException("非法参数：客户ID为空");
        }
        if (StringUtils.isEmpty(accountID)) {
            throw new IllegalArgumentException("非法参数：客户accountID为空");
        }
        CustomerConsult consultRecord = new CustomerConsult();
        consultRecord.setCreateor(accountID);
        consultRecord.setConsultTime(new Date());
        consultRecord.setContent(consultContent);
        CustomerInfo customerInfo = getSimpleCustomerInfoById(customerId);
        consultRecord.setCustomerInfo(customerInfo);
        customerInfoDAO.save(consultRecord);
    }
    
    @Override
    public CustomerAppointment addAppointmentRecord(String customerId,
    		CustomerAppointment customerAppointment, String accountID) throws CrmException {
    	if (StringUtils.isEmpty(customerId)) {
            throw new IllegalArgumentException("非法参数：客户ID为空");
        }
        if (StringUtils.isEmpty(accountID)) {
            throw new IllegalArgumentException("非法参数：客户accountID为空");
        }
//        CustomerInfo customerInfo = getSimpleCustomerInfoById(customerId);
        CustomerInfo customerInfo = this.findCustomerInfoById(customerId);
        customerAppointment.setCustomerInfo(customerInfo);
        customerAppointment.setCreateor(accountID);
        customerAppointmentDAO.save(customerAppointment);
        return customerAppointment;
    }
    
    @Override
    public void modifyConsultRecord(String customerConsultId, String consultContent) throws CrmException {
        if (StringUtils.isEmpty(customerConsultId)) {
            throw new IllegalArgumentException("非法参数：咨询记录ID为空");
        }
        
        CustomerConsult customerConsult = customerInfoDAO.findByConsultId(customerConsultId);
        if(customerConsult == null){
            throw new CrmException("客户咨询记录不存在！");
        }
        customerConsult.setContent(consultContent);
        
        customerInfoDAO.update(customerConsult);
    }
    
    @Override
    public void modifyAppointmentRecord(CustomerAppointment customerAppointment)
    		throws CrmException {
    	if (null == customerAppointment || StringUtils.isEmpty(customerAppointment.getId())) {
            throw new IllegalArgumentException("非法参数：预约记录ID为空");
        }
    	customerAppointmentDAO.update(customerAppointment);
    }
    
    public CustomerInfo getSimpleCustomerInfoById(String id) throws CrmException {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id 不能为空");
        }
        CustomerInfo customerInfo = customerInfoDAO.findById(id);
        if (customerInfo == null) {
            throw new CrmException("客户不存在！");
        }
        return customerInfo;
    }
    
    @Override
    public void deleteConsultRecord(String customerConsultId) throws CrmException {
        if (StringUtils.isEmpty(customerConsultId)) {
            throw new IllegalArgumentException("非法参数：ID为空");
        }
        CustomerConsult customerConsult = customerInfoDAO.findByConsultId(customerConsultId);
        if(customerConsult == null){
            throw new CrmException("客户咨询记录不存在！");
        }
        customerInfoDAO.delete(customerConsult);
    }
    
    @Override
    public void deleteVisitRecord(String returnVistId) throws CrmException {
        if (StringUtils.isEmpty(returnVistId)) {
            throw new IllegalArgumentException("非法参数：ID为空");
        }
        ReturnVist returnVist = customerInfoDAO.findByReturnVistId(returnVistId);
        if(returnVist == null){
            throw new CrmException("回访记录不存在！");
        }
        customerInfoDAO.delete(returnVist);
    }
    
    @Override
    public void deleteAppointmentRecord(String customerAppointmentId)
    		throws CrmException {
    	if (StringUtils.isEmpty(customerAppointmentId)) {
            throw new IllegalArgumentException("非法参数：ID为空");
        }
        CustomerAppointment customerAppointment = customerAppointmentDAO.findById(customerAppointmentId);
        if(customerAppointment == null){
            throw new CrmException("预约记录不存在！");
        }
        customerAppointmentDAO.delete(customerAppointment);
    }
    
    @Override
    public void txEditorCustomerInfo(String customerId) throws CrmException {
        if (StringUtils.isEmpty(customerId)) {
            throw new IllegalArgumentException("非法参数：客户ID为空");
        }
        CustomerInfo customerInfo = customerInfoDAO.findById(customerId);
        if(customerInfo == null){
            throw new CrmException("客户不存在！");
        }
        
        customerInfo.setStatus(CustomerInfo.OFFICIAL_STATUS);
        customerInfoDAO.update(customerInfo);
    }
    
    /* (non-Javadoc)
	 * @see org.eapp.crm.blo.ICustomerInfoBiz#addCustomerInfo(org.eapp.crm.hbean.CustomerInfo)
	 */
	@Override
	public CustomerInfo addCustomerInfo(CustomerInfo customer) throws CrmException {
		if (customer.getTel() == null) {
			throw new CrmException("手机号码不能为空");
		}
		if (!customerInfoDAO.checkTel(customer.getTel())) {
			throw new CrmException("该手机号码已存在");
		}
		customer.setCreateTime(new Date());
		customer.setDataSource(CustomerInfo.DATA_SOURCE_MANUAL_ENTRY);
		customerInfoDAO.save(customer);
		return customer;
	}

	/* (non-Javadoc)
	 * @see org.eapp.crm.blo.ICustomerInfoBiz#getCustomerInfoById(java.lang.String)
	 */
	@Override
	public CustomerInfo findCustomerInfoById(String customerId) {
		if (StringUtils.isEmpty(customerId)) {
            throw new IllegalArgumentException("非法参数:id 不能为空！");
        }
		CustomerInfo customer = customerInfoDAO.findById(customerId);
		if (customer == null) {
            throw new IllegalArgumentException("该客户不存在！");
        }
		//加载子表信息
		Hibernate.initialize(customer.getCustomerAppointments());
        Hibernate.initialize(customer.getCustomerConsults());
        Hibernate.initialize(customer.getReturnVists());

        try {
        	// 初始化投资顾问部门
            List<GroupInfo> groups = uas.getBindedGroups(customer.getSaleMan());
            List<String> deptNameList = new ArrayList<String>();
            if (!CollectionUtils.isEmpty(groups)) {
            	GroupInfo g = groups.get(0);
            	deptNameList.add(g.getGroupName());
            	getParentDept(g, deptNameList );
            	
//            	Collections.reverse(deptNameList);
                String fullDeptName = "";
                for (String dept : deptNameList) {
                	fullDeptName += dept + "/";
    			}
                if (StringUtils.isNotEmpty(fullDeptName)) {
                	customer.setFullDeptName(fullDeptName.substring(0, fullDeptName.length()-1));
                }
            }
		} catch (Exception e) {
			throw new IllegalArgumentException("初始化投资顾问部门失败");
		}
        
		//初始化电话归属
		String tel = customer.getTel();
		TelPartArea tpe = null;
		if (StringUtils.isNotBlank(tel) && tel.length() >= TelPartArea.TEL_PART_LENGTH) {
			try {
				tel = tel.substring(0, TelPartArea.TEL_PART_LENGTH);
				tpe = telPartAreaDAO.findByTelPart(tel);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		customer.setTelPartArea(tpe != null ? tpe : TelPartArea.UNKNOW_AREA);
        return customer;
	}
	
	private void getParentDept(GroupInfo g, List<String> deptNameList) throws RpcAuthorizationException, MalformedURLException{
		if (g != null && StringUtils.isNotEmpty(g.getParentGroupID())) {
			GroupInfo group = gs.getGroupByID(g.getParentGroupID());
			deptNameList.add(group.getGroupName());
			getParentDept(group, deptNameList);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eapp.crm.blo.ICustomerInfoBiz#modifyCustomer(org.eapp.crm.hbean.CustomerInfo)
	 */
	@Override
	public CustomerInfo modifyCustomer(CustomerInfo customer) throws CrmException {
		Date now = new Date();
		if(StringUtils.isEmpty(customer.getId()) && CustomerInfo.RETURNVISIT_STATUS.equals(customer.getStatus())){
			//新增页面时直接提交操作
//			customer.setCreateTime(now);
			customer.setSubmitTime(now);
//			customer.setDataSource(CustomerInfo.DATA_SOURCE_MANUAL_ENTRY);
//			customerInfoDAO.save(customer);
			return addCustomerInfo(customer);
		} else {
			CustomerInfo cust = customerInfoDAO.findById(customer.getId());
			if (cust == null) {
	            throw new CrmException("该客户不存在！");
	        }
			if (customer.getTel() == null) {
				throw new CrmException("手机号码不能为空");
			}
			if (!cust.getTel().equals(customer.getTel()) && !customerInfoDAO.checkTel(customer.getTel())) {
				throw new CrmException("该手机号码已存在");
			}
			cust.setCustName(customer.getCustName());
			cust.setSex(customer.getSex());
			cust.setCustProperty(customer.getCustProperty());
			cust.setBirthday(customer.getBirthday());
			cust.setAge(customer.getAge());
			cust.setTel(customer.getTel());
			cust.setEmail(customer.getEmail());
			cust.setAddress(customer.getAddress());
			if(StringUtils.isNotEmpty(customer.getStatus())){
				if(customer.getStatus().equals(CustomerInfo.RETURNVISIT_STATUS) 
						&& cust.getStatus().equals(CustomerInfo.REJECT_STATUS) 
						&& cust.getReturnVistRejctTimes() != null 
						&& cust.getReturnVistRejctTimes() >= CustomerInfo.MAX_REJECT_TIMES) {
					//从驳回状态改为回访状态，说明是重新提交。要判断超过三次不让提交
					throw new CrmException("该客户信息已经被驳回三次,不能再提交");
				}
				if (CustomerInfo.RETURNVISIT_STATUS.equals(customer.getStatus()) && cust.getSubmitTime() == null) {
					//为空时才写入提交时间（以第一次提交时间为准）
					cust.setSubmitTime(now);
				}
				cust.setStatus(customer.getStatus());
				
			}
			cust.setFinancingAmount(customer.getFinancingAmount());
			cust.setExpectedInvestAgelimit(customer.getExpectedInvestAgelimit());
			cust.setInvestExperience(customer.getInvestExperience());
			cust.setExpectedProduct(customer.getExpectedProduct());
			cust.setRecommendProduct(customer.getRecommendProduct());
			cust.setCommunicationType(customer.getCommunicationType());
			cust.setCommunicationResult(customer.getCommunicationResult());
			cust.setIdentityNum(customer.getIdentityNum());
			
			if (CustomerInfo.TOPERFECT_STATUS.equals(cust.getStatus()) || CustomerInfo.OFFICIAL_STATUS.equals(cust.getStatus())) {
				cust.setBankName(customer.getBankName());
				cust.setBankBranch(customer.getBankBranch());
				cust.setBankAccount(customer.getBankAccount());
			}
			
			customerInfoDAO.saveOrUpdate(cust);
			return cust;
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eapp.crm.blo.ICustomerInfoBiz#txCommitFromView(java.lang.String)
	 */
	@Override
	public CustomerInfo txCommitFromView(String customerId) throws CrmException {
		CustomerInfo cust = customerInfoDAO.findById(customerId);
		if (cust == null) {
            throw new CrmException("该客户不存在！");
        }
		if(StringUtils.isNotEmpty(cust.getStatus())){
			if(cust.getStatus().equals(CustomerInfo.REJECT_STATUS) 
					&& cust.getReturnVistRejctTimes() != null 
					&& cust.getReturnVistRejctTimes() >= 3) {
				throw new CrmException("该客户信息已经被驳回三次,不能再提交");
			}
		}
		if (cust.getSubmitTime() == null) {
			cust.setSubmitTime(new Date());
		}
		cust.setStatus(CustomerInfo.RETURNVISIT_STATUS);
		customerInfoDAO.saveOrUpdate(cust);
		return cust;
	}

	@Override
	public ReturnVist addReturnVist(CustomerInfo customer, String content, String customerConsultContent, String vistUser, Integer operType)
			throws CrmException {
		customer = this.modifyCustomer(customer);
		Date now = new Date();
		if (operType == 2) {
			//驳回
			customer.setReturnVistRejctTimes((null == customer.getReturnVistRejctTimes() ? 0 : customer.getReturnVistRejctTimes()) + 1);
			if (customer.getReturnVistRejctTimes() >= CustomerInfo.MAX_REJECT_TIMES) {
				//不通过
				customer.setStatus(CustomerInfo.UNPASS_STATUS);
			} else {
				customer.setStatus(CustomerInfo.REJECT_STATUS);
			}
		} else if(operType == 1) {
			//通过
			customer.setStatus(CustomerInfo.POTENTIAL_STATUS);
			//通过时间
			customer.setPassTime(now);
		} else if(operType == 3) {
			//不通过
			customer.setStatus(CustomerInfo.UNPASS_STATUS);
		}
		
		// 回访
		ReturnVist returnVist = new ReturnVist();
		returnVist.setContent(content);
		returnVist.setCustomerInfo(customer);
		returnVist.setReturnVistTime(now);
		returnVist.setReturnVistUser(vistUser);
		customerInfoDAO.saveOrUpdate(returnVist);
		
		// 咨询
		if(StringUtils.isNotEmpty(customerConsultContent)) {
			CustomerConsult customerConsult = new CustomerConsult();
			customerConsult.setContent(customerConsultContent);
			customerConsult.setConsultTime(now);
			customerConsult.setCreateor(vistUser);
			//判断vistUser是客服还是销售
			customerConsult.setInpnutType(checkInputType(vistUser));
			customerConsult.setCustomerInfo(customer);
			customerInfoDAO.saveOrUpdate(customerConsult);
		}
		
		return returnVist;
	}
	
	@Override
	public ReturnVist modifyReturnVist(CustomerInfo customer, String content, String customerConsultContent,
			String returnVistId, String vistUser, Integer operType) throws CrmException {
		customer = this.modifyCustomer(customer);
		//修改回访记录时没有决定通不通过
//		if (operType == 2) {
//			//驳回
//			customer.setStatus(CustomerInfo.REJECT_STATUS);
//			customer.setReturnVistRejctTimes((null == customer.getReturnVistRejctTimes() ? 0 : customer.getReturnVistRejctTimes()) + 1);
//		} else if(operType == 1) {
//			//通过
//			customer.setStatus(CustomerInfo.POTENTIAL_STATUS);
//		} else if(operType == 3) {
//			//不通过
//			customer.setStatus(CustomerInfo.UNPASS_STATUS);
//		}
		
		ReturnVist returnVist = customerInfoDAO.findByReturnVistId(returnVistId);
		returnVist.setContent(content);
		customerInfoDAO.saveOrUpdate(returnVist);
		
		// 咨询
		if(StringUtils.isNotEmpty(customerConsultContent)) {
			CustomerConsult customerConsult = new CustomerConsult();
			customerConsult.setContent(customerConsultContent);
			customerConsult.setConsultTime(new Date());
			customerConsult.setCreateor(vistUser);
			//判断vistUser是客服还是销售
			customerConsult.setInpnutType(checkInputType(vistUser));
			customerConsult.setCustomerInfo(customer);
			customerInfoDAO.saveOrUpdate(customerConsult);
		}
		return returnVist;
	}
	
	private String checkInputType(String userAccountId) {
		String inputType = "";
		ListPage<UserAccountExt> userAccountExtListPage = userAccountExtDAO.getAll();
		List<UserAccountExt> userAccountExtList = userAccountExtListPage.getDataList();
		if(!CollectionUtils.isEmpty(userAccountExtList)) {
			for(UserAccountExt userAccountExt : userAccountExtList) {
				if(userAccountExt.getAccountId().equals(userAccountId)) {
					inputType = "销售";
					break;
				} else if(userAccountExt.getServiceAccountId().equals(userAccountId)) {
					inputType = "客服";
					break;
				}
			}
		}
		return inputType;
	}
	
	@Override
	public CustomerAppointment modifyCustomerAppointment(
			CustomerAppointment customerAppointment) throws CrmException {
		CustomerAppointment cAppointment = customerAppointmentDAO.findById(customerAppointment.getId());
		if (cAppointment == null) {
            throw new CrmException("该客户不存在！");
        }
		
		if(null != customerAppointment.getAppointmentTime()) {
			cAppointment.setAppointmentTime(customerAppointment.getAppointmentTime());
		}
		
		if(null != customerAppointment.getWarnOpportunity()) {
			cAppointment.setWarnOpportunity(customerAppointment.getWarnOpportunity());
		}
		
		if(null != customerAppointment.getAppointmentType()) {
			cAppointment.setAppointmentType(customerAppointment.getAppointmentType());
		}
		
		if(null != customerAppointment.getRemark()) {
			cAppointment.setRemark(customerAppointment.getRemark());
		}
//		BeanUtils.copyProperties(customerAppointment, cAppointment);
		customerInfoDAO.saveOrUpdate(cAppointment);
		return cAppointment;
	}
	
	@Override
    public void txImportMyCustomer(File file, String saleMan) throws IOException,
            CrmException {
	    // 参数判断
	    if (file == null) {
	        throw new IllegalArgumentException("非法参数：文件为空");
	    }
	    if (StringUtils.isEmpty(saleMan)) {
	        throw new IllegalArgumentException("非法参数：销售人员为空");
	    }
	    FileInputStream in = new FileInputStream(file);
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (Exception e) {
            throw new IOException("系统目前只支持导入Excel2003文件", e.getCause());
        }
        
        HSSFSheet childSheet = wb.getSheetAt(0);
        if (childSheet != null) {
            int rowNum = childSheet.getLastRowNum();
            CustomerInfo customerInfo = null;
            for (int i = 1; i <= rowNum; i++) {

                HSSFRow row = childSheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String name = ExcelCellToValue.cellValueToString(row.getCell(0));
                String sex = ExcelCellToValue.cellValueToString(row.getCell(1));
                String tel = ExcelCellToValue.cellValueToString(row.getCell(2));
                String email = ExcelCellToValue.cellValueToString(row.getCell(3));

                customerInfo = new CustomerInfo();
                // 客户名称
                customerInfo.setCustName(name);
                // 性别
                if(sex != null && sex != ""){
                   if(sex.equals("男") || sex.equals("M")){
                       customerInfo.setSex('M');
                   } else if (sex.equals("女") || sex.equals("F")){
                       customerInfo.setSex('F');
                   } 
                }
                // 电话
                customerInfo.setTel(tel);
                // 邮箱
                customerInfo.setEmail(email);
                // 设置销售人员
                customerInfo.setSaleMan(saleMan);
                // 数据来源
                customerInfo.setDataSource(CustomerInfo.DATA_SOURCE_COMPANY_ALLOT);
                // 设置为未提交
                customerInfo.setStatus(CustomerInfo.UNCOMMIT_STATUS);
                // 创建时间
                customerInfo.setCreateTime(new Date());
                customerInfoDAO.saveOrUpdate(customerInfo);
            }
        }
    }
	
	@Override
	public CustomerConsult findCustomerConsultById(String id)
			throws CrmException {
		return customerInfoDAO.findByConsultId(id);
	}
	
	@Override
	public ReturnVist findReturnVistById(String returnVistId)
			throws CrmException {
		return customerInfoDAO.findByReturnVistId(returnVistId);
	}
	
    @Override
    public String txAddCust(String saleMan, String Name, String custProperty, String identityNum, String custStatus) {
        Date now = new Date();
        CustomerInfo c = new CustomerInfo();
        c.setSaleMan(saleMan);
        c.setCustName(Name);
        c.setCustProperty(Integer.valueOf(custProperty));
        c.setCreateTime(now);
        c.setDataSource(CustomerInfo.DATA_SOURCE_MANUAL_ENTRY);
        c.setIdentityNum(identityNum);
        c.setStatus(custStatus);
        if (CustomerInfo.POTENTIAL_STATUS == custStatus 
        		|| CustomerInfo.TOPERFECT_STATUS == custStatus 
        		|| CustomerInfo.OFFICIAL_STATUS == custStatus) {
        	//划款单过来的客户通过时间与提交时间，默认为当前
        	c.setPassTime(now);
        	c.setSubmitTime(now);
        }
        
        customerInfoDAO.save(c);
        
        return c.getId();
    }

    @Override
    public void txModifyCust(String id, String status, String identityNum) {
        CustomerInfo c = customerInfoDAO.findById(id);
        c.setStatus(CustomerInfo.OFFICIAL_STATUS);
        if (CustomerInfo.OFFICIAL_STATUS.equals(status)) {
	        c.setIdentityNum(identityNum);
        }
        customerInfoDAO.update(c);
    }
    
    @Override
    public void txChangeMemoMark(String customerId, Integer memoMark) {
    	CustomerInfo c = customerInfoDAO.findById(customerId);
    	if (c == null) {
    		throw new IllegalArgumentException();
    	}
    	c.setMemoMark(memoMark);
    	customerInfoDAO.update(c);
    }

	@Override
	public List<AutoCompleteData> queryAutoCompleteData(String salMan, String[] status, int pageSize) {
		return customerInfoDAO.queryAutoCompleteData(salMan, status, pageSize);
	}
}
