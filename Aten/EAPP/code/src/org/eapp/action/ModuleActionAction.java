package org.eapp.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IModuleActionBiz;
import org.eapp.blo.IModuleBiz;
import org.eapp.dao.param.ModuleActionQueryParameters;
import org.eapp.hbean.Module;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 处理模块动作模块的请求
 * @author zsy
 * @version
 */
public class ModuleActionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(ModuleActionAction.class);
	
	private IModuleActionBiz moduleActionBiz;
	private IModuleBiz moduleBiz;
	
	private String moduleID;
	private String[] valid;
	private String[] rpc;
	private String[] http;
	
	private String isValid;
	private String isRPC;
	private String isHTTP;
	
	private ListPage<ModuleAction> moduleActionPage;

	public ListPage<ModuleAction> getModuleActionPage() {
		return moduleActionPage;
	}

	public void setModuleActionBiz(IModuleActionBiz moduleActionBiz) {
		this.moduleActionBiz = moduleActionBiz;
	}

	public void setModuleBiz(IModuleBiz moduleBiz) {
		this.moduleBiz = moduleBiz;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public void setValid(String[] valid) {
		this.valid = valid;
	}

	public void setRpc(String[] rpc) {
		this.rpc = rpc;
	}

	public void setHttp(String[] http) {
		this.http = http;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setIsRPC(String isRPC) {
		this.isRPC = isRPC;
	}

	public void setIsHTTP(String isHTTP) {
		this.isHTTP = isHTTP;
	}

	public String initQueryPage() {
		return success();
	}
	
	public String saveModuleActionConfig() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		try {
			moduleActionBiz.addOptions(moduleID, valid, rpc, http);
			Module module = moduleBiz.getModule(moduleID);
			//写入日志
			if (module != null) {
				StringBuffer sbf = new StringBuffer(module.toString());
				List<ModuleAction> validMAs = moduleActionBiz.getModuleActions(valid);
				if (validMAs != null) {
					sbf.append("\n设置有效：");
					for (ModuleAction s : validMAs) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				List<ModuleAction> rpcMAs = moduleActionBiz.getModuleActions(rpc);
				if (rpcMAs != null) {
					sbf.append("\n设置为RPC服务：");
					for (ModuleAction s : rpcMAs) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				List<ModuleAction> httpMAs = moduleActionBiz.getModuleActions(http);
				if (httpMAs != null) {
					sbf.append("\n设置为HTTP服务：");
					for (ModuleAction s : httpMAs) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), module.getModuleID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String queryModuleAction() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		ModuleActionQueryParameters qp = new ModuleActionQueryParameters();
		qp.setPageSize(QueryParameters.ALL_PAGE_SIZE);
		qp.setModuleID(moduleID);
		if (StringUtils.isNotBlank(isRPC)) {
			qp.setIsRPC("Y".equals(isRPC));
		}
		if (StringUtils.isNotBlank(isHTTP)) {
			qp.setIsHTTP("Y".equals(isHTTP));
		}
		if (StringUtils.isNotBlank(isValid)) {
			qp.setIsValid("Y".equals(isValid));
		}
		
		try {
			moduleActionPage = moduleActionBiz.queryModuleAction(qp);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
