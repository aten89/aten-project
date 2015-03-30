/**
 * 
 */
package org.eapp.crm.dao;

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
 * 客户信息DAO接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public interface ICustomerInfoDAO extends IBaseHibernateDAO {

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
     * 根据ID获取对象
     * @param id
     * @return
     * @throws CrmException
     */
    public CustomerInfo findById(String id);

    /**
     * 查询咨询记录List
     * @param qp 查询参数
     * @return 咨询记录List
     * @throws CrmException 
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-8	黄云耿	新建
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
     * 修改日期		修改人	修改原因
     * 2014-5-10	钟华杰	新建
     * </pre>
     */
    public ListPage<CustomerAppointment> queryAppointmentRecordList(CustomerInfoQueryParameters qp) throws CrmException;

    /**
     * 根据ID获取咨询记录
     * @param id 咨询记录ID
     * @return 咨询记录
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-9	黄云耿	新建
     * </pre>
     */
    public CustomerConsult findByConsultId(String id) throws CrmException;

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
    ListPage<ReturnVist> queryVisitRecordList(CustomerInfoQueryParameters qp) throws CrmException;

    /**
     * 根据回访记录ID查找回访记录
     * @param id 回访记录ID
     * @return 回访记录
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-11	黄云耿	新建
     * </pre>
     */
    public ReturnVist findByReturnVistId(String id) throws CrmException;

    /**
	 * 获取客户自动完成列表数据
	 * @param salMan
	 * @param pageSize
	 * @return
	 */
	public List<AutoCompleteData> queryAutoCompleteData(String salMan, String[] status, int pageSize);
	
	public boolean checkTel(String tel);
}
