package org.eapp.dao;

import org.eapp.dao.param.ActorAccountQueryParameters;
import org.eapp.hbean.ActorAccount;
import org.eapp.util.hibernate.ListPage;

/**
 * 接用户管理DAO
 * @author zsy
 * @version
 */
public interface IActorAccountDAO extends IBaseHibernateDAO {

	public ActorAccount findById(java.lang.String id);
	
	/**
	 * 根据条件查询接口用户
	 * @param actorQP 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<ActorAccount> queryActorAccount(ActorAccountQueryParameters actorQP);
	
	/**
	 * 检查接口帐号是否存在
	 * @param accountID 接口帐号
	 * @return
	 */
	public boolean testAccountID(String accountID);
	
	/**
	 * 通过帐号ID取得接口用户
	 * @param accountID 帐号ID
	 * @return
	 */
	public ActorAccount findByAccountID(String accountID);

}