/**
 * 
 */
package org.eapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.ISubSystemBiz;
import org.eapp.dto.SubSystemSelect;
import org.eapp.exception.EappException;
import org.eapp.hbean.SubSystem;

/**
 * 子系统管理控制层
 * @author surefan
 * @version 1.0
 */
public class SubSystemAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8174612595997424220L;
	private static final Log log = LogFactory.getLog(SubSystemAction.class);
	
	// 子系统管理的业务逻辑层
	private ISubSystemBiz subSystemBiz;

	private String subSystemID;
	private String name;
	private String logoURL;
	private String ipAddress;
	private String serverName;
	private String domainName;
	private Integer port;
	private String description;
	private Boolean isValid;
	private String orderIDs;
	
	private List<SubSystem> subSystems;
	private SubSystem subSystem;
	private String htmlValue;//输出HTML内容
	
	public void setSubSystemBiz(ISubSystemBiz subSystemBiz) {
		this.subSystemBiz = subSystemBiz;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public List<SubSystem> getSubSystems() {
		return subSystems;
	}

	public SubSystem getSubSystem() {
		return subSystem;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	/**
	 * 初始化列表页面
	 */
	public String initQueryPage() {
		return success();
	}
	
	public String querySubSystem() {
		try {
			subSystems = subSystemBiz.getAllSubSystems();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initFrame() {
		return success();
	}
	
	/**
	 * 初始化新增页面
	 */
	public String initAdd() {
		return success();
	}
	
	/**
	 * 新增子系统操作
	 */
	public String addSubSystem() {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(domainName)) {
			return error("参数不能为空");
		}
		if (port == null) {
			port = 80;
		}
		if (isValid == null) {
			isValid = true;
		}
		
		try {
			SubSystem subSystem = subSystemBiz.addSubSystem(name, logoURL, ipAddress, serverName, domainName,
					port, isValid, description);
			ActionLogger.log(getRequest(), subSystem.getSubSystemID(), subSystem.toString());
			return success(subSystem.getSubSystemID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 初始化修改页面
	 */
	public String initModify() {
		if (StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		subSystem = subSystemBiz.getSubSystem(subSystemID);
		return success();
	}
	
	/**
	 * 修改子系统信息
	 */
	public String modifySubSystem() {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(name) || StringUtils.isBlank(domainName)) {
			return error("参数不能为空");
		}
		if (port == null) {
			port = 80;
		}
		if (isValid == null) {
			isValid = true;
		}
		try {
			SubSystem subSystem = subSystemBiz.modifySubSystem(subSystemID, name, logoURL, ipAddress, serverName,
					domainName, port,isValid, description);
			ActionLogger.log(getRequest(), subSystem.getSubSystemID(), subSystem.toString());
			return success(subSystem.getSubSystemID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化查看页面
	 */
	public String viewSubSystem() {
		if (StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		subSystem = subSystemBiz.getSubSystem(subSystemID);
		return success();
	}
	
	/**
	 * 删除子系统操作，返回XML格式的结果
	 */
	public String deleteSubSystem() {
		if (StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		try {
			SubSystem system = subSystemBiz.deleteSubSystem(subSystemID);
			ActionLogger.log(getRequest(), subSystemID, system.toString());
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String initOrder() {
		subSystems = subSystemBiz.getAllSubSystems();
		return success();
	}
	
	public String orderSubSystem() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		
		Map<String, Integer> idAndOrder = new HashMap<String, Integer>();
		String[] ids = orderIDs.split(",");
		for (int i = 0; i < ids.length; i++) {
			idAndOrder.put(ids[i], i + 1);
		}
		try {
			subSystemBiz.modifySubSystemSort(idAndOrder);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//JOSNP
	public String loadSubSystemSelect() {
		List<SubSystem> sys = subSystemBiz.getAllSubSystems();
		htmlValue = new SubSystemSelect(sys).toString();
		return success();
	}
	
}
