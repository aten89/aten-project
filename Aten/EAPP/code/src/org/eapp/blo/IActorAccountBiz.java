package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.ActorAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;


/**
 * 接口用户管理相关的业务方法
 * @author zsy
 * @version
 */
public interface IActorAccountBiz {

	/**
	 * 根据条件查询接口用户
	 * @param actorQP 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<ActorAccount> queryActorAccount(ActorAccountQueryParameters actorQP);
	
	/**
	 * 删除接口用户
	 * @param accountIDs 接口用户ID
	 */
	public List<ActorAccount> deleteActors(String[] accountIDs);
	
	/**
	 * 保存接口用户，并设置授权证书
	 * @param user 接口用户信息
	 * @param credence 授权证书
	 * @throws EappException
	 */
	public ActorAccount addActor(AccountSaveBean user, String credence) throws EappException;
	
	/**
	 * 通过接口用户帐号取得接口用户对象
	 * @param accountID 接口用户帐号
	 * @return
	 */
	public ActorAccount getActorByAccountID(String accountID);
	
	/**
	 * 修改接口用户，并设置授权证书
	 * @param user 用户信息
	 * @throws EappException
	 */
	public ActorAccount modifyActor(AccountSaveBean user, String credence) throws EappException;
	
	/**
	  * 取得已绑定到接口用户的服务
	  * @param accountID 接口帐号
	  * @return
	  */
	 public Set<Service> getBindingServices(String accountID);
	 
	 /**
	  * 绑定接口用户到服务
	  * @param accountID 接口帐号
	  * @param serviceIDs 服务ID列表
	  */
	 public ActorAccount txBindService(String accountID, String[] serviceIDs);
	 /**
	  * 根据帐号ID获取帐号服务
	  * @param accountID 接口帐号
	  * @return list 帐号服务列表
	  */	 
	 public List<String> getServiceByAccount(String accountID);
	 
	 /**
	  * 通过ID取得接口帐号
	  * @param actorIDs
	  * @return
	  */
//	 public List<ActorAccount> csGetActors(String[] actorIDs);

}