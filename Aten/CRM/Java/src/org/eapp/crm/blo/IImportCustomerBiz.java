/**
 * 
 */
package org.eapp.crm.blo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eapp.crm.dao.param.ImportCustomerQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.ImportCustomer;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.ListPage;

/**
 * 导入客户业务逻辑层接口
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-26	lhg		新建
 * </pre>
 */
public interface IImportCustomerBiz {
    
    /**
     * 手动分配导入的客户
     * @param userAccountId 销售人员账号
     * @param importCustIds 导入客户IDS
     * @throws CrmException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-26	lhg		新建
     * </pre>
     */
    void txManualAllotImportCustomer(String userAccountId, List<String> importCustIds) throws CrmException;

    /**
     * 自动分配
     * @param userAccountExts 各个销售人员所分配的客户数
     * @throws CrmException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-26	lhg		新建
     * </pre>
     */
    void txAutoAllotImportCustomer(List<UserAccountExt> userAccountExts) throws CrmException;
    
    /**
     * 通过excel导入客户资料
     * @param excel excel文件
     * @param fileName 文件名
     * @param dir 文件路径
     * @param importUser 导入人
     * @return 导入成功或失败的值
     * @throws IOException IO异常
     * @throws CrmException crm异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-28	黄云耿	新建
     * </pre>
     */
    public String txImportCustomer(File excel, String fileName, File dir, String importUser)throws IOException, CrmException;

    /**
     * 查询客户List
     * @param qp 查询参数对象
     * @return 导入客户List
     * @throws CrmException crm异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-28	黄云耿	新建
     * </pre>
     */
    public ListPage<ImportCustomer> queryCustomerList(ImportCustomerQueryParameters qp)throws CrmException;
    
    /**
     * 根据id删除导入客户
     * @param id 导入客户id
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-29	黄云耿	新建
     * </pre>
     * @throws CrmException 
     */
    public void txDeleteImportCustomer(String id) throws CrmException;

    /**
     * 根据id获取导入客户
     * @param id 导入客户id
     * @return 导入客户
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-29	黄云耿	新建
     * </pre>
     */
    public ImportCustomer getSimpleImportCustomerById(String id) throws CrmException;
    
}
