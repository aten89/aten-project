/**
 * 
 */
package org.eapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IPortletBiz;
import org.eapp.dao.param.PortletQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.DefaultPortlet;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.Role;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * @version 1.0
 */
public class PortletAction extends BaseAction {

	/**
	 * 自动产生的serialVersionUID:-8260334325420494476L
	 */
	private static final long serialVersionUID = -8260334325420494476L;
	private static final Log log = LogFactory.getLog(PortletAction.class);
	private IPortletBiz portletBiz;

	private int pageNo;
	private int pageSize;
	private String portletID;
	private String portletName;
	private String url;
	private String moreUrl;
	private String style;
	private String subSystemID;
	private String portletConfig;
	private String[] roleIDs;
	
	private ListPage<Portlet> portletPage;
	private List<Portlet> portlets;
	private Portlet portlet;
	private Set<Role> roles;
	
	public ListPage<Portlet> getPortletPage() {
		return portletPage;
	}
	
	public List<Portlet> getPortlets() {
		return portlets;
	}

	public Portlet getPortlet() {
		return portlet;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setPortletBiz(IPortletBiz portletBiz) {
		this.portletBiz = portletBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public void setPortletConfig(String portletConfig) {
		this.portletConfig = portletConfig;
	}

	public void setRoleIDs(String[] roleIDs) {
		this.roleIDs = roleIDs;
	}

	/**
	 * 初始化页面
	 */
	public String initFrame() {
		return success();
	}
	
	/**
	 * 初始化页面
	 */
	public String initQuery() {
		return success();
	}
	
	/**
	 * 查询板块列表
	 */
	public String queryPortlet() {
		PortletQueryParameters pqp = new PortletQueryParameters();
		pqp.setPageSize(pageSize);
		pqp.setPageNo(pageNo);
		if (StringUtils.isNotBlank(portletName)) {
			pqp.setPortletName(portletName);
		}
		
		try {
			portletPage = portletBiz.queryPortlets(pqp);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化新增页面
	 */
	public String initAdd() {
		return success();
	}
	
	/**
	 * 增加板块
	 */
	public String addPortlet() {
		if (StringUtils.isBlank(portletName) || StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		try {
			Portlet portlet = portletBiz.addPortlet(subSystemID, portletName, url, moreUrl, style, Boolean.TRUE, Boolean.TRUE);
			ActionLogger.log(getRequest(), portlet.getPortletID(), portlet.toString());
			return success(portlet.getPortletID());
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
		if (StringUtils.isBlank(portletID)) {
			return error("参数不能为空");
		}
		
		portlet = portletBiz.getPortlet(portletID);
		return success();
	}

	

	/**
	 * 修改板块
	 * @throws IOException
	 */
	public String modifyPortlet() {
		if (StringUtils.isBlank(portletID) || StringUtils.isBlank(portletName) || StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}

		try {
			portletBiz.modifyPortlet(subSystemID, portletID, portletName, url, moreUrl, style, Boolean.TRUE, Boolean.TRUE);
			ActionLogger.log(getRequest(), portletID, "portletID="+portletID+";subSystemID="+subSystemID+";portletName="+portletName+";url="+url+";moreUrl="+moreUrl+";style="+style);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 查看板块的信息
	 */
	public String viewPortlet() {
		if (StringUtils.isBlank(portletID)) {
			return error("参数不能为空");
		}
		portlet = portletBiz.getPortlet(portletID);
		return success();
	}
	
	/**
	 * 删除板块,如果板块有用户在使用中则不允删除
	 */
	public String deletePortlet() {
		if (StringUtils.isBlank(portletID)) {
			return error("参数不能为空");
		}
		try {
			portletBiz.deletePortlet(portletID);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 显示默认文件页面
	 */
	public String initDefaulePortal(){
		return success();
	}
	
	public String loadAllPortal() {
		PortletQueryParameters queryParameters = new PortletQueryParameters();
		queryParameters.setPageNo(1);
		queryParameters.setPageSize(QueryParameters.ALL_PAGE_SIZE);
		try {
			ListPage<Portlet> page = portletBiz.queryPortlets(queryParameters);
			if (page != null) {
				portlets = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 得到默认的门户
	 * @throws IOException
	 */
	public String loadDefaultPortal() {
		try {
			portlets = portletBiz.getDefaultPortlets();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 设置默认的门户
	 */
	public String setDefaultPortal() {
		List<DefaultPortlet> portletConfigList = new ArrayList<DefaultPortlet>(0);
		if (StringUtils.isNotBlank(portletConfig)) {
			String[] configs = portletConfig.split("@");
			for (String config : configs) {
				String[] detail = config.split(",");
				int len = detail.length;
				if (len != 3) {
					return error("配置信息格式不正确");
				} else {
					DefaultPortlet userPortlet = new DefaultPortlet();
					userPortlet.setPortletId(detail[0]);
					userPortlet.setPageContainerID(detail[1]);
					userPortlet.setPositionIndex(Integer.valueOf(detail[2]));
					portletConfigList.add(userPortlet);
				}
			}
		}
		try {
			portletBiz.txSetDefaultPortlets(portletConfigList);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String initBindRole() {
		return success();
	}
	
	public String loadBindedRoles() {
		if (StringUtils.isBlank(portletID)) {
			return error("参数不能为空");
		}
		try {
			roles = portletBiz.getBindedRoles(portletID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindRole() {
		if (StringUtils.isBlank(portletID)) {
			return error("参数不能为空");
		}
		try {
			Portlet portlet = portletBiz.txBindRole(portletID, roleIDs);
			//写入日志
			if (portlet != null) {
				StringBuffer sbf = new StringBuffer();
				if (portlet.getRoles() != null) {
					for (Role r : portlet.getRoles()) {
						sbf.append(r.toString()).append("\n");
					}
					
				}
				ActionLogger.log(getRequest(), portlet.getPortletID(), portlet.toString() + "\n绑定对象：\n" + sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
