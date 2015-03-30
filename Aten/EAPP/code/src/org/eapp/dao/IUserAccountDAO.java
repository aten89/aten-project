package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;

/**
 * 用户管理DAO
 * @author zsy
 * @version
 */
public interface IUserAccountDAO extends IBaseHibernateDAO {

	public UserAccount findById(java.lang.String id);

	public List<UserAccount> findAll();
	
	/**
	 * 根据条件查询用户
	 * @param userQP 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<UserAccount> queryUserAccount(UserAccountQueryParameters userQP);

	/**
	 * 检查用户帐号是否存在
	 * @param accountID 用户帐号
	 * @return
	 */
	public boolean testAccountID(String accountID);
	
	/**
	 * 通过用户帐号取得用户对象
	 * @param accountID 用户帐号
	 * @return
	 */
	public UserAccount findByAccountID(String accountID);
	
	/**
	 * 通过部门ID列表查找所有用户
	 */
	public List<UserAccount> findByGroupIDs(List<String> groupIDs);
}