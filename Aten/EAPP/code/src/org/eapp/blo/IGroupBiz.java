/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.GroupQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 群组管理相关的业务方法
 * @author zsy ， 林良益
 * @version 1.1
 * Modify 2008-11-24
 */
public interface IGroupBiz {
	
	public Group getGroupByName(String groupName);

	/**
	 * 通过群组名称取得下级的给定深度的群组列表
	 * 当群组ID为空时从根级取起
	 * 类型为空时查找取得所有类型
	 * @param groupName
	 * @param type (部门/项目组)
	 * @param depth 立即加载的深度
	 * @return
	 */
	public List<Group> getSubGroupsByName(String groupName, String type);
	
	/**
	 * 通过群组ID取得下级给定深度的组群组列表，
	 * 当群组ID为空时从根级取起
	 * 类型为空时查找取得所有类型
	 * @param type (部门/项目组)
	 * @param groupID 群组ID
	 * @param depth 立即加载的深度
	 * @return
	 */
	public List<Group> getSubGroups(String groupID, String type);
	
	/**
	 * 通过用户职位ID取得他管理的所有部门
	 * @param managerPostIDs 管理者职位ID
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
	 * 通过ID取得群组对象
	 * @param groupID 群组ID
	 * @return
	 */
	public Group getGroupByID(String groupID);
	
	/**
	 * 新增群组
	 * @param parentGroupID 父ID
	 * @param managerID 管理者ID
	 * @param groupName 群组名称
	 * @param type 群组类型
	 * @param description 描述
	 */
	public Group addGroup(String parentGroupID, String managerID, 
			String groupName, String type, String description) throws EappException;
	
	/**
	 * 修改群组
	 * @param groupID 群组ID
	 * @param parentGroupID 父ID
	 * @param managerID 管理者ID
	 * @param groupName 群组名称
	 * @param type 群组类型
	 * @param description 描述
	 */
	public Group modifyGroup(String groupID, String parentGroupID, String managerID, 
			String groupName, String type, String description) throws EappException;
	
	/**
	 * 删除群组
	 * 根据ID删除群组
	 * 有子机构的不能删除
	 * 有人员绑定的不能删除
	 * 有角色绑定的，要级联删除对角色的关联
	 * @param groupIDs 群组ID
	 */
	public List<Group> deleteGroups(String[] groupIDs) throws EappException;
	
	/**
	 * 对群组下级进行排序，按orderIDs数组顺序
	 * @param orderIDs 排序后的ID列表
	 * @param parentGroupID 部门ID
	 */
	public void modifyOrder(String[] orderIDs, String parentGroupID);
	
	/**
	 * 取得群组已绑定的角色列表
	 * @param groupID 群组ID
	 * @return
	 */
	public Set<Role> getBindedRoles(String groupID);
	
	/**
	 * 取得群组已绑定的用户列表
	 * @param groupID 群组ID
	 * @return 
	 */
	public Set<UserAccount> getBindedUsers(String groupID);
	
	/**
	 * 取得群组已绑定的职位
	 * @param groupID
	 * @return
	 */
	public List<Post> getBindedPosts(String groupID);
	/**
	 * 绑定角色
	 * @param groupID 群组ID
	 * @param roleIDs 角色ID列表
	 */
	public Group txBindRole(String groupID, String[] roleIDs);
	
	/**
	 * 绑定用户
	 * 删除原有后，再绑定新的
	 * @param groupID 群组ID
	 * @param userIDs 用户ID列表
	 */
	public Group txBindUser(String groupID, String[] userIDs);
	
	/**
	 * 绑定职位
	 * @param groupID 群组ID
	 * @param postIDs 职位ID列表
	 * @return
	 */
	public Group txBindPost(String groupID, String[] postIDs);
	/**
	 * 绑定用户帐号
	 * 追加绑定
	 * @param groupID
	 * @param accountIDs
	 * @return
	 */
	public Group addBindUser(String groupID, String[] accountIDs);
	
	/**
	 * 解除绑定用户帐号
	 * @param groupID
	 * @param accountIDs
	 * @return
	 */
	public Group txUnBindUser(String groupID, String[] accountIDs);

	/**
	 * 通过群组查找关联的职位列表
	 * @param groupID 群组ID
	 * @return
	 */
	public Set<Post> getPostsByGroup(String groupID);
	
	/**
	 * 通过类型查找所有群组
	 * @param type
	 * @return
	 */
	public List<Group> getGroupsByType(String type);
}
