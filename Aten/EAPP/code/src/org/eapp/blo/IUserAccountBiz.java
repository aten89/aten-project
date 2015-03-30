package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 用户管理相关的业务方法
 * @author zsy
 * @version
 */
public interface IUserAccountBiz {

	/**
	 * 根据条件查询用户
	 * @param userQP 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<UserAccount> queryUserAccount(UserAccountQueryParameters userQP);
	
	/**
	 * 逻辑删除用户，即isLogicDelete这段改为true
	 * 同时删除LDAP中用户
	 * @param accountIDs 用户帐号
	 * @throws EappException 删除超级管理员帐号时抛出异常
	 */
	public List<UserAccount> deleteUsers(String[] accountIDs) throws EappException;
	
	/**
	 * 保存用户,
	 * 同时保存到LDAP中，并设置密码为默认密码
	 * @param user 用户信息
	 * @throws EappException
	 */
	public UserAccount addUser(AccountSaveBean user) throws EappException;
	
	/**
	 * 通过用户帐号取得用户对象
	 * @param accountID 用户帐号
	 * @return
	 */
	public UserAccount getUserByAccountID(String accountID);
	
	/**
	 * 修改用户
	 * 财时修改LDAP中的相关信息
	 * @param user 用户信息
	 * @throws EappException
	 */
	public UserAccount modifyUser(AccountSaveBean user) throws EappException;
	
	/**
	 * 重置密码，同时修改LDAP中密码
	 * @param accountID 用户帐号
	 * @param password 密码
	 */
	public UserAccount txSetPassword(String accountID, String oldPassword, String newPassword) throws EappException;
	 
	 /**
	  * 设置为默认密码
	  * @param accountID
	  * @return
	  * @throws EappException
	  */
	 public UserAccount txSetDefaultPassword(String accountID) throws EappException;
	 
	 /**
	  * 设置页面主题风格
	  * @param styleThemes
	  * @throws EappException
	  */
	 public void txSetStyleThemes(String userAccound, String styleThemes) throws EappException;
	 
	 /**
	  * 取得绑定到用户的角色
	  * @param accountID 用户帐号
	  * @return
	  */
	 public Set<Role> getBindedRoles(String accountID);
	 
	 /**
	  * 取得绑定到用户的有效角色
	  * @param accountID 用户帐号
	  * @return
	  */
	 public Set<Role> getBindedValidRoles(String accountID);
	 
	 /**
	  * 取得绑定到用户的群组
	  * @param accountID 用户帐号
	  * @return
	  */
	 public Set<Group> getBindedGroups(String accountID);
	 
	 
	 /**
	  * 取得绑定到用户的岗位
	  * @param accountID 用户帐号
	  * @return
	  */
	 public Set<Post> getBindedPosts(String accountID);
	 
	 /**
	  * 绑定用户到角色
	  * @param accountID 用户帐号
	  * @param roleIDs 角色ID列表
	  */
	 public UserAccount txBindRole(String accountID, String[] roleIDs)  throws EappException;
	 
	 /**
	  * 绑定用户到群组
	  * @param accountID 用户帐号
	  * @param groupIDs 群组ID列表
	  */
	 public UserAccount txBindGroup(String accountID, String[] groupIDs);

	/**
	 * 根据部门Id查找用户，如果部门Id为空查找所有
	 * @param groupID
	 * @return
	 */
	public List<UserAccount> getCascadeAccountsByGroupId(String groupID);
	
	/**
	 * 是否强制用户修改密码
	 * @param accountID
	 * @return
	 */
	public boolean getForceChangePassword(String accountID);
}