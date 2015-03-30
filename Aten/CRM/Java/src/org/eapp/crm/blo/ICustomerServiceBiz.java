/**
 * 
 */
package org.eapp.crm.blo;

import org.eapp.comobj.SessionAccount;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.util.hibernate.ListPage;

/**
 * 客服管理业务逻辑层接口
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-12	黄云耿	新建
 * </pre>
 */
public interface ICustomerServiceBiz {

    /**
     * 根据当前客服人员过滤销售人员对应的客户列表
     * @param qp 查询参数
     * @param user 当前用户
     * @return 客户列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-12	黄云耿	新建
     * </pre>
     */
    public ListPage<CustomerInfo> queryCustomerInfoList(CustomerInfoQueryParameters qp, SessionAccount user) 
            throws CrmException ;
    
}
