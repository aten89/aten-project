/**
 * 
 */
package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.ListPage;

/**
 * 客服管理DAO接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-12	黄云耿	新建
 * </pre>
 */
public interface ICustomerServiceDAO extends IBaseHibernateDAO {
    /**
     * 查询客户列表
     * @param qp 查询参数
     * @return 客户列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-12	黄云耿	新建
     * </pre>
     */
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp) throws CrmException;

    /**
     * 根据当前客服人员查询对应的销售人员
     * @param serviceAccountId 当前客服人员账号
     * @return 客服人员查询对应的销售人员List
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-12	黄云耿	新建
     * </pre>
     */
    public List<UserAccountExt> queryUserAccountExtList(String serviceAccountId) throws CrmException;

    /**
     * 
     * @param custId
     * @param returnVistUser
     * @return
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-16	hyg	新建
     * </pre>
     */
    public List<ReturnVist> queryVisitRecordList(String custId, String returnVistUser);

}
