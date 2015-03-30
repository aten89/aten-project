package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.GroupQueryParameters;
import org.eapp.hbean.Group;
import org.eapp.util.hibernate.ListPage;

/**
 * 群组管理DAO
 * @author zsy
 * @version
 */
public interface IGroupDAO extends IBaseHibernateDAO {


	public Group findById(String id);

	public List<Group> findByGroupName(String groupName);

	/**
	 * 根据类型查找第一级群组，即parentGroupID为空的记录
	 * @param type 类型
	 * @return
	 */
	public List<Group> findRootGroups(String type);
	
	/**
	 * 根据类型查找下级群组
	 * 类型为空时查找所有类型
	 * @param groupID 群组ID
	 * @param type 类型
	 * @return
	 */
	public List<Group> findSubGroups(String groupID, String type);

	/**
	 * 通过用户ID取得他管理的所有部门
	 * @param managerPostIDs 管理者ID
	 * @return
	 */
	public List<Group> getGroupsByManagerPost(List<String> managerPostIDs);
	
	/**
	 * 根据条件查询群组
	 * @param groupQP 查询参数
	 * @return
	 */
	public ListPage<Group> queryGroup(GroupQueryParameters groupQP);
	
	/**
	 * 取得群组的子群组的下一个序号
	 * @param parentGroupID
	 * @return
	 */
	public int getNextDisplayOrder(String groupID);
	
	/**
	 * 取得群组的子群组的树深度
	 * @param parentGroupID
	 * @return
	 */
	public int getNextTreeLevel(String groupID);
	
	/**
	 * 是否存在下级群组
	 * @param group
	 * @return
	 */
	public boolean existSubGroup(Group group);
	
	/**
	 * 是否存在用户
	 * @param group
	 * @return
	 */
	public boolean existUserAccount(Group group);
	
	
	/**
	 * 根据ID删除群组
	 * 有子机构的不能删除
	 * 有人员绑定的不能删除
	 * 有角色绑定的，要级联删除对角色的关联
	 * 
	 * @param groupID
	 */
	public void deleteByID(String groupID);
	
	/**
	 * 修改群组的排序号
	 * @param groupIDs 群组ID
	 * @param parentGroupID 父群组ID
	 */
	public void saveOrder(String[] groupIDs, String parentGroupID);
	
	/**
	 * 修改指定父ID下的群组的排序号，当排序号大于给定值时减1
	 * @param parentGroupID 旧的父ID
	 * @param theOrder 排序号
	 */
	public void updateOrder(String parentGroupID, Integer theOrder);
	
	/**
	 * 检查名称是否重复
	 * @param parentGroupID 父Id
	 * @param name 名称
	 * @return
	 */
	public boolean checkRepetition(String parentGroupID, String name);
	
	/**
	 * 通过类型查找所有群组
	 * @param type
	 * @return
	 */
	public List<Group> findGroupsByType(String type);

}