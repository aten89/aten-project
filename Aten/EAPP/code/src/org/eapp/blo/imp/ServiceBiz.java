/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IServiceBiz;
import org.eapp.dao.IActorAccountDAO;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.IServiceDAO;
import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;


/**
 * @author zsy
 * @version 
 */
public class ServiceBiz implements IServiceBiz {

	private IServiceDAO serviceDAO;
	private IModuleActionDAO moduleActionDAO;
	private IActorAccountDAO actorAccountDAO;

	public IServiceDAO getServiceDAO() {
		return serviceDAO;
	}

	public void setServiceDAO(IServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
		
	}

	/**
	 * @param moduleActionDAO the moduleActionDAO to set
	 */
	public void setModuleActionDAO(IModuleActionDAO moduleActionDAO) {
		this.moduleActionDAO = moduleActionDAO;
	}

	/**
	 * @param actorAccountDAO the actorAccountDAO to set
	 */
	public void setActorAccountDAO(IActorAccountDAO actorAccountDAO) {
		this.actorAccountDAO = actorAccountDAO;
	}
	

	@Override
	public ListPage<Service> queryService(ServiceQueryParameters qp) {
		return serviceDAO.queryService(qp);
	}

	@Override
	public List<Service> deleteServices(String[] serviceIDs) throws EappException {
		List<Service> services = new ArrayList<Service>();
		if (serviceIDs == null || serviceIDs.length < 1) {
			return services;
		}
		Service service = null;
		for (String id : serviceIDs) {
			if (id == null) {
				continue;
			}
			service = serviceDAO.findById(id);
			if (service == null) {
				continue;
			}
			if (serviceDAO.existUserAccount(service)) {
				throw new EappException("角色”" + service.getServiceName() + "“已经被接口帐号关联");
			}
			serviceDAO.delete(service);
			services.add(service);
		}
		return services;
	}

	@Override
	public Service addService(String serviceName, boolean isValid, 
			String description) throws EappException {
		if (StringUtils.isBlank(serviceName)) {
			throw new IllegalArgumentException();
		}
		serviceName = serviceName.trim();
		if (serviceDAO.checkRepetition(serviceName)) {
			throw new EappException("服务名称已存在");
		}
		Service role = new Service();
		role.setServiceName(serviceName);
		role.setIsValid(isValid);
		role.setDescription(description);
		serviceDAO.save(role);
		return role;
	}
	
	@Override
	public Service modifyService(String serviceID, String serviceName, boolean isValid, 
			String description)throws EappException {
		if (StringUtils.isBlank(serviceID) || StringUtils.isBlank(serviceName)) {
			throw new IllegalArgumentException();
		}
		Service service = serviceDAO.findById(serviceID);
		if (service == null)  {
			throw new IllegalArgumentException("serviceID的对象不存在");
		}
		serviceName = serviceName.trim();
		if (!serviceName.equals(service.getServiceName())) {
			if (serviceDAO.checkRepetition(serviceName)) {
				throw new EappException("服务名称已存在");
			}
		}
		service.setServiceID(serviceID);
		service.setServiceName(serviceName);
		service.setIsValid(isValid);
		service.setDescription(description);
		serviceDAO.update(service);
		return service;
	}
	
	@Override
	public Service getServiceByID(String serviceID) {
		if (StringUtils.isBlank(serviceID)) {
			throw new IllegalArgumentException();
		}
		return serviceDAO.findById(serviceID);
	}

	
	@Override
	public Service txBindActor(String serviceID, String[] actorIDs) {
		if (StringUtils.isBlank(serviceID)) {
			throw new IllegalArgumentException();
		}
		Service service = serviceDAO.findById(serviceID);
		if (service == null)  {
			throw new IllegalArgumentException("serviceID的对象不存在");
		}
		HashSet<ActorAccount> set = new HashSet<ActorAccount>();
		ActorAccount account = null;
		if (actorIDs != null && actorIDs.length > 0) {
			for (String uid : actorIDs) {
				if (StringUtils.isBlank(uid)) {
					continue;
				}
				account = actorAccountDAO.findById(uid);
				if (account != null) {
					set.add(account);
				}else{
					throw new IllegalArgumentException("数据库中不存在该帐号ID:"+uid);
				}
		//		set.add(new ActorAccount(uid));
			}
		}
		service.setActorAccounts(set);
		serviceDAO.update(service);
		return service;
	}

	@Override
	public Service txBindRight(String serviceID, String[] moduleActionIDs) {
		if (StringUtils.isBlank(serviceID)) {
			throw new IllegalArgumentException();
		}
		Service role = serviceDAO.findById(serviceID);
		if (role == null)  {
			throw new IllegalArgumentException("serviceID的对象不存在");
		}
		HashSet<ModuleAction> set = new HashSet<ModuleAction>();
		ModuleAction ma = null;
		if (moduleActionIDs != null && moduleActionIDs.length > 0) {
			for (String uid : moduleActionIDs) {
				if (StringUtils.isBlank(uid)) {
					continue;
				}
				ma = moduleActionDAO.findById(uid);
				if (ma != null) {
					set.add(ma);
				}else{
					throw new IllegalArgumentException("数据库中不存在该权限ID:"+uid);
				}
			//	set.add(new ModuleAction(uid));
			}
		}
		role.setModuleActions(set);
		serviceDAO.update(role);
		return role;
	}

	@Override
	public Set<ActorAccount> getBindedActors(String serviceID) {
		if (StringUtils.isBlank(serviceID)) {
			return null;
		}
		Service role = serviceDAO.findById(serviceID);
		if (role == null) {
			return null;
		}
		role.getActorAccounts().size();//加载延迟内容
		return role.getActorAccounts();
	}

	@Override
	public Set<ModuleAction> getBindedRights(String serviceID) {
		if (StringUtils.isBlank(serviceID)) {
			return null;
		}
		Service role = serviceDAO.findById(serviceID);
		if (role == null) {
			return null;
		}
		Set<ModuleAction> rights = role.getModuleActions();
		rights.size();//加载延迟
		return rights;
	}
}
