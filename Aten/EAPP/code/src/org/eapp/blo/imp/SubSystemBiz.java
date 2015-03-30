/**
 * 
 */
package org.eapp.blo.imp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.ISubSystemBiz;
import org.eapp.dao.IDataDictionaryDAO;
import org.eapp.dao.IModuleDAO;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.exception.EappException;
import org.eapp.hbean.SubSystem;
import org.eapp.sys.config.SysConstants;


/**
 * @version 1.0
 */
public class SubSystemBiz implements ISubSystemBiz {
	// 子系统管理数据操作层
	private ISubSystemDAO subSystemDAO = null;
	// 模块管理数据操作层
	private IModuleDAO moduleDAO = null;

	private IDataDictionaryDAO dataDictionaryDAO = null;
	

	/**
	 * @param dataDictionaryDAO the dataDictionaryDAO to set
	 */
	public void setDataDictionaryDAO(IDataDictionaryDAO dataDictionaryDAO) {
		this.dataDictionaryDAO = dataDictionaryDAO;
	}
	/**
	 * @param subSystemDAO the subSystemDAO to set
	 */
	public void setSubSystemDAO(ISubSystemDAO subSystemDAO) {
		this.subSystemDAO = subSystemDAO;
	}

	/**
	 * @param moduleBiz the moduleBiz to set
	 */
	public void setModuleDAO(IModuleDAO moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	public List<SubSystem> getAllSubSystems() {
		return subSystemDAO.findAll();
	}

	public SubSystem getSubSystem(String systemId) {
		if (StringUtils.isBlank(systemId)) {
			throw new IllegalArgumentException("子系统ID不能为空");
		}
		return subSystemDAO.findById(systemId);

	}

	public SubSystem deleteSubSystem(String systemId) throws EappException {
		if (StringUtils.isBlank(systemId)) {
			throw new IllegalArgumentException("子系统ID不能为空");
		}
		if (SysConstants.EAPP_SUBSYSTEM_ID.equals(systemId)) {
			throw new EappException("不能删除此系统");
		}
		SubSystem subsystem = subSystemDAO.findById(systemId);
		
		int moduleNum = moduleDAO.getModuleCountBySubSystemID(systemId);
		if (moduleNum > 0) {
			throw new EappException("本系统绑定了模块无法删除，请删除相关子模块");
		} else {
			dataDictionaryDAO.deleteDataDictionaryBySubSystemId(systemId);
			subSystemDAO.delete(subsystem);
		}
		return subsystem;
	}

	public SubSystem addSubSystem(String name, String logoURL, String ipAddress,
			String serverName, String domainName, int port, boolean isValid, String description) throws EappException {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("子系统名不能为空");
		}
		if (port<SysConstants.PORT_MIN || port>SysConstants.PORT_MAX) {
			throw new EappException("端口号必须在1-65535区间");
		}
		List<SubSystem> list = subSystemDAO.findByName(name);
		if(!list.isEmpty()){
			throw new EappException("子系统名不能重复");
		}
		SubSystem subSystem = new SubSystem();
		subSystem.setName(name);
		subSystem.setLogoURL(logoURL);
		subSystem.setIpAddress(ipAddress);
		subSystem.setServerName(serverName);
		subSystem.setDomainName(domainName);
		subSystem.setPort(port);
//		subSystem.setLogoutURL(logoutUrl);
		subSystem.setIsValid(isValid);
		subSystem.setDescription(description);
		subSystem.setDisplayOrder(subSystemDAO.getMaxOrder() + 1);
		subSystemDAO.save(subSystem);
		return subSystem;
	}

	public SubSystem modifySubSystem(String subSystemId, String name, String logoURL,
			String ipAddress, String serverName, String domainName, int port, boolean isValid, String description) throws EappException {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(name)) {
			throw new IllegalArgumentException();
		}
		if(port < SysConstants.PORT_MIN || port > SysConstants.PORT_MAX){
			throw new EappException("端口号必须在1-65535区间");
		}
		
		SubSystem subSystem = subSystemDAO.findById(subSystemId);
		if (subSystem == null) {
			throw new IllegalArgumentException("子系统为空");
		}
		
		List<SubSystem> list = subSystemDAO.findByName(name);
		if(list.size() > 1){
			throw new EappException("子系统名不能重复");
		}
		
		subSystem.setSubSystemID(subSystemId);
		subSystem.setName(name);
		subSystem.setLogoURL(logoURL);
		subSystem.setIpAddress(ipAddress);
		subSystem.setServerName(serverName);
		subSystem.setDomainName(domainName);
		subSystem.setPort(port);
//		subSystem.setLogoutURL(logoutUrl);
		subSystem.setIsValid(isValid);
		subSystem.setDescription(description);
		subSystemDAO.saveOrUpdate(subSystem);
		return subSystem;
	}
	
	/**
	 * @author zsy
	 * @param roleIDs
	 * @return
	 */
	@Override
	public List<SubSystem> getHasRightSubSystems(List<String> roleIDs) {
		if (roleIDs == null || roleIDs.isEmpty()) {
			return null;
		}
		List<SubSystem> sys = subSystemDAO.getSubSystemByRoleIDs(roleIDs);
//		if (sys == null || sys.isEmpty()) {
//			return sys;
//		}
//		List<Module> quoteModules = moduleDAO.getQuoteModules(null);
//		if (quoteModules != null && !quoteModules.isEmpty()) {
//			for (Module m : quoteModules) {
//				SubSystem quotedSys = m.getQuoteModule().getSubSystem();
//				SubSystem quoteSys = m.getSubSystem();
//				if (sys.contains(quotedSys) && !sys.contains(quoteSys)) {
//					sys.add(quoteSys);
//				}
//			}
//		}
		return sys;
	}

	@Override
	public void modifySubSystemSort(Map<String, Integer> idsort) {
		if(idsort == null){
			throw new IllegalArgumentException();
		}
		subSystemDAO.sortSubSystems(idsort);
		
	}

}
