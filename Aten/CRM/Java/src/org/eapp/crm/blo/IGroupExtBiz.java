package org.eapp.crm.blo;

import java.util.List;

import org.eapp.crm.dto.GroupExtInfo;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

public interface IGroupExtBiz {

    public ListPage<GroupExt> getGroupExtsByBusinessType(String businessType);
    
    public ListPage<GroupExt> getGroupExtsByBusinessType(String saleDeptID,String businessType);

    public GroupExtInfo getGroupByID(String groupID) throws CrmException;

	public void txSaveGroupExt(String groupID, String groupName,
			String businessType);

	/***
	 * 通过用户账号查找该用户管理的及下属的销售部门
	 * @param userAccountId 用户账号
	 * @return 销售部门
	 * @throws CrmException 异常
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2014-5-20	lhg		新建
	 * </pre>
	 */
    List<GroupExt> querySaleGroup(String userAccountId) throws CrmException;

    /**
     * 通过组织机构ID查找该机构的员工
     * @param groupId
     * @param userAccountId
     * @return
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	lhg		新建
     * </pre>
     */
    List<UserAccountInfo> queryGroupStaffByGroupName(String groupName) throws CrmException;

    /**
     * 查找所有的销售员工
     * @param userAccountId 当前用户账号
     * @return
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	lhg		新建
     * </pre>
     */
    List<UserAccountInfo> queryManSaleStaffs(String userAccountId) throws CrmException;

    /**
     * 获取所有的销售人员
     * @return 销售人员
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-15	黄云耿	新建
     * </pre>
     */
    List<UserAccountInfo> queryAllSaleStaff() throws CrmException;
    

    /**
     * 
     * @param businessType
     * @return
     * @throws CrmException
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-16	hyg 	新建
     * </pre>
     */
    List<GroupExt> queryAllSaleGroupSel(String businessType) throws CrmException;
}
