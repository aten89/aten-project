/**
 * 
 */
package org.eapp.blo.imp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IActorAccountBiz;
import org.eapp.dao.IActorAccountDAO;
import org.eapp.dao.IServiceDAO;
import org.eapp.dao.param.ActorAccountQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;


/**
 * 
 * @author zsy
 * @version
 */
public class ActorAccountBiz implements IActorAccountBiz {

	private IActorAccountDAO actorAccountDAO;
	private IServiceDAO serviceDAO;

	public void setActorAccountDAO(IActorAccountDAO actorAccountDAO) {
		this.actorAccountDAO = actorAccountDAO;
	}

	/**
	 * @param serviceDAO the serviceDAO to set
	 */
	public void setServiceDAO(IServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	public ListPage<ActorAccount> queryActorAccount(ActorAccountQueryParameters userQP) {
		if (userQP == null) {
			throw new IllegalArgumentException();
		}
		return actorAccountDAO.queryActorAccount(userQP);
	}

	public List<ActorAccount> deleteActors(String[] accountIDs) {
		List<ActorAccount> actors = new ArrayList<ActorAccount>();
		if (accountIDs == null || accountIDs.length < 1) {
			return actors;
		}
		ActorAccount actor = null;
		for (String accountID : accountIDs) {
			actor = actorAccountDAO.findByAccountID(accountID);
			if (actor == null) {
				continue;
			}
			actor.setIsLogicDelete(true);
			actorAccountDAO.update(actor);
			actors.add(actor);
		}
		
		return actors;
	}

	public ActorAccount getActorByAccountID(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			throw new IllegalArgumentException();
		}
		return actorAccountDAO.findByAccountID(accountID);
	}

	public ActorAccount addActor(AccountSaveBean account, String credence) throws EappException {
		if (account == null) {
			throw new IllegalArgumentException();
		} else if (StringUtils.isBlank(account.getAccountID()) || StringUtils.isBlank(account.getDisplayName())) {
			throw new IllegalArgumentException("接口帐号与显示名称不能为空");
		}
		
		if (!actorAccountDAO.testAccountID(account.getAccountID())) {
			throw new EappException("用户帐号“" + account.getAccountID() + "”已经存在");
		}
		
//		Boolean isLogicDelete = actorAccountDAO.getIsLogicDelete(account.getAccountID());
//		if (isLogicDelete != null) {
//			if (isLogicDelete.booleanValue()) {
//				throw new EappException("接口帐号：" + account.getAccountID() + "已经被注销 ");
//			} else {
//				throw new EappException("接口帐号：" + account.getAccountID() + "已经存在");
//			}
//		}
		ActorAccount user = new ActorAccount();
		user.setAccountID(account.getAccountID());
		user.setDisplayName(account.getDisplayName());
		user.setIsLock(account.getIsLock());
		user.setChangePasswordFlag(account.getChangePasswordFlag());
		user.setCreateDate(new Timestamp(System.currentTimeMillis()));
		user.setInvalidDate(account.getInvalidDate());
		user.setDescription(account.getDescription());
		user.setCredence(credence);
		actorAccountDAO.save(user);
		return user;

	}

	public ActorAccount modifyActor(AccountSaveBean account, String credence) throws EappException {
		if (account == null) {
			throw new IllegalArgumentException();
		} else if (StringUtils.isBlank(account.getAccountID()) || StringUtils.isBlank(account.getDisplayName())) {
			throw new IllegalArgumentException("用户帐号与显示名称不能为空");
		}
		ActorAccount user = actorAccountDAO.findByAccountID(account.getAccountID());
		if (user == null) {
			throw new EappException("用户不存在");
		}
		if (user.getIsLogicDelete() != null && user.getIsLogicDelete().booleanValue()) {
			throw new EappException("用户已被注销");
		}
		user.setDisplayName(account.getDisplayName());
		user.setIsLock(account.getIsLock());
		user.setChangePasswordFlag(account.getChangePasswordFlag());
//		user.setCreateDate(new Timestamp(System.currentTimeMillis()));
		user.setInvalidDate(account.getInvalidDate());
		user.setDescription(account.getDescription());
		if (credence != null) {
			user.setCredence(credence);
		}
		actorAccountDAO.update(user);
		return user;
	}

	public Set<Service> getBindingServices(String accountID) {
		if (StringUtils.isBlank(accountID)) {
			return null;
		}
		ActorAccount user = actorAccountDAO.findByAccountID(accountID);
		if (user == null) {
			return null;
		}
		user.getServices().size();
		return user.getServices();
	}

	public ActorAccount txBindService(String accountID, String[] serviceIDs) {
		if (StringUtils.isBlank(accountID)) {
			throw new IllegalArgumentException();
		}
		ActorAccount user = actorAccountDAO.findByAccountID(accountID);
		if (user == null) {
			throw new IllegalArgumentException("accountID的对象不存在");
		}
		HashSet<Service> set = new HashSet<Service>();
		Service service = null;
		if (serviceIDs != null && serviceIDs.length > 0) {
			for (String gid : serviceIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				service = serviceDAO.findById(gid);
				if (service != null) {
					set.add(service);
				}
			//	set.add(new Service(gid));
			}
		}
		user.setServices(set);
		actorAccountDAO.update(user);
		return user;
	}

	public List<String> getServiceByAccount(String accountID) {
		ActorAccount actor = actorAccountDAO.findByAccountID(accountID);
		Set<Service> services = actor.getServices();
		List<String> serviceIDs = new ArrayList<String>();
		for (Service service : services) {
			if (service.getIsValid()) {
				serviceIDs.add(service.getServiceID());
			}
		}
		return serviceIDs;
	}

}
