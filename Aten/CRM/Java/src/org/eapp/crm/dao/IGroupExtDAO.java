package org.eapp.crm.dao;

import java.util.List;

import org.eapp.crm.dao.param.GroupExtQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.util.hibernate.ListPage;

public interface IGroupExtDAO extends IBaseHibernateDAO {
    
    /**
     * 通过ID获取组织机构扩展信息
     * @param id ID
     * @return 组织机构扩展信息
     * @throws CrmException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	lhg		新建
     * </pre>
     */
    public GroupExt findById(String id) throws CrmException;

    public ListPage<GroupExt> queryGroupExt(GroupExtQueryParameters groupExtQP);

    /**
     * 通过业务类型查找组织机构扩展信息
     * 
     * @param businessType 业务类型
     * @return 组织机构扩展信息
     * @throws CrmException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-6	lhg		新建
     * </pre>
     */
    List<GroupExt> findByBusinessType(String businessType) throws CrmException;

}
