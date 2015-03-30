/**
 * 
 */
package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.dao.param.ImportCustomerQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.ImportCustomer;
import org.eapp.util.hibernate.ListPage;

/**
 * 导入客户DAO接口
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-27	lhg		新建
 * </pre>
 */
public interface IImportCustomerDAO extends IBaseHibernateDAO {

    /**
     * 根据ID查找导入客户
     * 
     * @param id id
     * @return 导入客户
     * @throws CrmException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    ImportCustomer findById(String id) throws CrmException;

    /**
     * 根据多个ID查找导入客户
     * 
     * @param ids ids
     * @param allocateFlag 是否分配
     * @return 多个导入客户
     * @throws CrmException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    List<ImportCustomer> findByIds(List<String> ids, Boolean allocateFlag) throws CrmException;

    /**
     * 根据条件查询导入客户列表
     * 
     * @param icqp 参数
     * @return 导入客户列表
     * @throws CrmException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    ListPage<ImportCustomer> queryImportCustomers(ImportCustomerQueryParameters icqp) throws CrmException;

}
