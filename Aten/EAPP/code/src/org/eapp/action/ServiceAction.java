package org.eapp.action;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IServiceBiz;
import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理服务管理的请求
 * @author zsy
 * @version
 */
public class ServiceAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(ServiceAction.class);
	
	private IServiceBiz serviceBiz;
	
	private int pageNo;
	private int pageSize;
	private String serviceID;
	private String serviceName;
	private String isValid;
	private String description;
	private String[] serviceIDs;
	private String[] actorIDs;
	private String[] moduleActionIDs;
	
	private ListPage<Service> servicePage;
	private List<Service> services;
	private Service service;
	private Set<ModuleAction> moduleActions;
	private Set<ActorAccount> actorAccounts;

	public ListPage<Service> getServicePage() {
		return servicePage;
	}

	public List<Service> getServices() {
		return services;
	}

	public Service getService() {
		return service;
	}

	public Set<ModuleAction> getModuleActions() {
		return moduleActions;
	}

	public Set<ActorAccount> getActorAccounts() {
		return actorAccounts;
	}

	public void setServiceBiz(IServiceBiz serviceBiz) {
		this.serviceBiz = serviceBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setServiceIDs(String[] serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	public void setActorIDs(String[] actorIDs) {
		this.actorIDs = actorIDs;
	}

	public void setModuleActionIDs(String[] moduleActionIDs) {
		this.moduleActionIDs = moduleActionIDs;
	}

	//初始化编辑页面
	public String initFrame() {
		return success();
	}

	public String initQuery() {
		return success();
	}
	
	public String queryService() {
		ServiceQueryParameters serviceQP = new ServiceQueryParameters();
		serviceQP.setPageNo(pageNo);
		serviceQP.setPageSize(pageSize);
		
		if (StringUtils.isNotBlank(serviceName)) {
			serviceQP.setServiceName(serviceName);
		}
		if (StringUtils.isNotBlank(isValid)) {
			serviceQP.SetIsValid("Y".equals(isValid));
		}

		serviceQP.addOrder("serviceName", true);
		try {
			servicePage = serviceBiz.queryService(serviceQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String initAdd() {
		return success();
	}
	
	public String addService() {
		if (StringUtils.isBlank(serviceName)) {
			return error("参数不能为空");
		}
		
		try {
			Service service = serviceBiz.addService(serviceName, "Y".equals(isValid), description);
			//写入日志
			if (service != null) {
				ActionLogger.log(getRequest(), service.getServiceID(), service.toString());
			}
			return success(service.getServiceID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		service = serviceBiz.getServiceByID(serviceID);
		return success();
	}
	
	public String modifyService() {
		if (StringUtils.isBlank(serviceID) || StringUtils.isBlank(serviceName)) {
			return error("参数不能为空");
		}
		try {
			Service service = serviceBiz.modifyService(serviceID, serviceName, "Y".equals(isValid), description);
			//写入日志
			if (service != null) {
				ActionLogger.log(getRequest(), service.getServiceID(), service.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String deleteServices() {
		if (serviceIDs == null || serviceIDs.length < 1) {
			return error("参数不能为空");
		}
		try {
			List<Service> services = serviceBiz.deleteServices(serviceIDs);
			//写入日志
			if (services != null) {
				for (Service service : services) {
					ActionLogger.log(getRequest(), service.getServiceID(), service.toString());
				}
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewService() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		service = serviceBiz.getServiceByID(serviceID);
		return success();
	}
	
	public String initBindActor() {
		return success();
	}
	
	public String loadBindedActors() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		try {
			actorAccounts = serviceBiz.getBindedActors(serviceID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindActor() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		try {
			Service service = serviceBiz.txBindActor(serviceID, actorIDs);
			//写入日志
			if (service != null) {
				StringBuffer sbf = new StringBuffer(service.toString());
				if (service.getActorAccounts() != null) {
					sbf.append("\n绑定对象：");
					for (ActorAccount s : service.getActorAccounts()) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), service.getServiceID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindRight() {
		return success();
	}
	
	public String loadBindedRights() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		try {
			moduleActions = serviceBiz.getBindedRights(serviceID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	
	public String saveBindRight() {
		if (StringUtils.isBlank(serviceID)) {
			return error("参数不能为空");
		}
		try {
			Service service = serviceBiz.txBindRight(serviceID, moduleActionIDs);
			//写入日志
			if (service != null) {
				StringBuffer sbf = new StringBuffer(service.toString());
				if (service.getModuleActions() != null) {
					sbf.append("\n绑定对象：");
					for (ModuleAction s : service.getModuleActions()) {
						sbf.append("\n").append(s.toString());
					}
				}
				ActionLogger.log(getRequest(), service.getServiceID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadUserServices() {
		ServiceQueryParameters serviceQP = new ServiceQueryParameters();
		serviceQP.setPageSize(ServiceQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(serviceName)) {
			serviceQP.setServiceName(serviceName);
		}
		
		serviceQP.SetIsValid(true);
		serviceQP.addOrder("serviceName", true);
		try {
			ListPage<Service> page = serviceBiz.queryService(serviceQP);
			if (page != null) {
				services = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
