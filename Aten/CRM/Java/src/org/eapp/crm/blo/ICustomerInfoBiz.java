/**
 * 
 */
package org.eapp.crm.blo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.dto.AutoCompleteData;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.crm.hbean.CustomerConsult;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.util.hibernate.ListPage;

/**
 * 客户信息业务逻辑层接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public interface ICustomerInfoBiz {
    
    /**
     * 根据查询参数查询客户信息
     * @param qp 查询参数
     * @return 客户信息ListPage
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-4	黄云耿	新建
     * </pre>
     */
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp);

    /**
     * 客户信息数据转移，改变其对应的客户经理
     * @param qp 查询参数
     * @param customerManage 客户经理
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-5	黄云耿	新建
     * </pre>
     */
    public void txChangeSaleMan(String[] custIds, String customerManage) throws CrmException;
    
    /**
     * 删除客户信息
     * @param customerId
     * @throws CrmException
     */
    public CustomerInfo txDeleteCustomerInfo(String customerId) throws CrmException;

    /**
     * 查询咨询记录List
     * @param qp 查询参数
     * @return 咨询记录List
     * @throws CrmException 
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-8 黄云耿 新建
     * </pre>
     */
    public ListPage<CustomerConsult> queryConsultRecordList(CustomerInfoQueryParameters qp) throws CrmException;
    /**
     * 查询预约记录List
     * @param qp
     * @return
     * @throws CrmException
     * 
     * <pre>
     * 修改日期     	修改人 	修改原因
     * 2014-5-10 	钟华杰 	新建
     * </pre>
     */
    public ListPage<CustomerAppointment> queryAppointmentRecordList(CustomerInfoQueryParameters qp) throws CrmException;
    
//    public ListPage<CustomerAppointment> queryAppointmentRecordList(CustomerInfoQueryParameters qp) throws CrmException;

    /**
     * 新增客户咨询记录
     * @param customerId 客户ID
     * @param consultContent 咨询内容
     * @param accountID 当前用户
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-9	黄云耿	新建
     * </pre>
     */
    public void addConsultRecord(String customerId, String consultContent, String accountID) throws CrmException;
    /**
     * 增加预约记录
     * @param customerId
     * @param customerAppointment
     * @throws CrmException
     */
    public CustomerAppointment addAppointmentRecord(String customerId, CustomerAppointment customerAppointment, String accountID) throws CrmException;
    /**
     * 修改预约记录
     * @param customerId
     * @param customerAppointment
     * @throws CrmException
     */
    public void modifyAppointmentRecord(CustomerAppointment customerAppointment) throws CrmException;

    /**
     * 根据id修改咨询记录
     * @param customerConsultId 咨询记录id
     * @param consultContent 咨询内容
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-10	黄云耿	新建
     * </pre>
     */
    public void modifyConsultRecord(String customerConsultId, String consultContent) throws CrmException;
   
    /**
     * 根据id删除咨询记录
     * @param customerConsultId 咨询记录ID
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-9	黄云耿	新建
     * </pre>
     */
    public void deleteConsultRecord(String customerConsultId) throws CrmException;
    /**
     * 删除预约记录
     * @param customerAppointmentId
     * @throws CrmException
     */
    public void deleteAppointmentRecord(String customerAppointmentId) throws CrmException;
    

    /**
     * 修改客户状态
     * @param customerConsultId 客户ID
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-10	黄云耿	新建
     * </pre>
     */
    public void txEditorCustomerInfo(String customerConsultId) throws CrmException;
    
    /**
     * 新增用户
     * @param customer 客户信息
     */
    public CustomerInfo addCustomerInfo(CustomerInfo customer) throws CrmException;
    
    /**
     * 根据id查询客户信息
     * @param customer 客户信息
     */
    public CustomerInfo findCustomerInfoById(String customerId);
    
    /**
     * 修改用户
     * @param customer 客户信息
     */
    public CustomerInfo modifyCustomer(CustomerInfo customer) throws CrmException;
    
    /**
     * 增加回访记录
     * @param customer
     * @param content
     * @return
     * @throws CrmException
     */
    public ReturnVist addReturnVist(CustomerInfo customer, String content, String customerConsultContent, String vistUser, Integer operType) throws CrmException;
    
    /**
     * 修改回访记录
     * @param customer
     * @param content
     * @param returnVistId
     * @return
     * @throws CrmException
     */
    public ReturnVist modifyReturnVist(CustomerInfo customer, String content, String customerConsultContent, String returnVistId, String vistUser, Integer operType) throws CrmException;
    
    /**
     * 修改预约记录
     * @param customerAppointment
     * @return
     * @throws CrmException
     */
    public CustomerAppointment modifyCustomerAppointment(CustomerAppointment customerAppointment) throws CrmException;
    
    /**
     * 查询回访记录列表
     * @param qp 查询参数
     * @return 回访记录列表
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-11	黄云耿	新建
     * </pre>
     */
    public ListPage<ReturnVist> queryVisitRecordList(CustomerInfoQueryParameters qp) throws CrmException;

    /**
     * 根据回访记录ID删除回访记录
     * @param returnVistId 回访记录ID
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-11	黄云耿	新建
     * </pre>
     */
    public void deleteVisitRecord(String returnVistId) throws CrmException;
    
    /**
     * 根据ID获取回访记录
     * @param returnVistId
     * @return
     * @throws CrmException
     */
    public ReturnVist findReturnVistById(String returnVistId) throws CrmException;
    
    /**
     * 根据ID获取咨询记录
     * @param returnVistId
     * @return
     * @throws CrmException
     */
    public CustomerConsult findCustomerConsultById(String id) throws CrmException;
    
    /**
     * 导入客户给当前登录者
     * @param file 文件
     * @param saleMan 销售人员
     * @throws CrmException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-15	lhg		新建
     * </pre>
     */
    public void txImportMyCustomer(File file, String saleMan) throws IOException, CrmException;
    
    public String txAddCust(String saleMan, String Name, String custProperty, String identityNum, String custStatus);

    public void txModifyCust(String id, String status, String identityNum);

	public CustomerInfo txCommitFromView(String customerId) throws CrmException;
	
	public void txChangeMemoMark(String customerId, Integer memoMark);
	
	/**
	 * 获取客户自动完成列表数据
	 * @param salMan
	 * @param pageSize
	 * @return
	 */
	public List<AutoCompleteData> queryAutoCompleteData(String salMan, String[] status, int pageSize);

}
