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
import org.eapp.blo.IShortCutMenuBiz;
import org.eapp.comobj.SessionAccount;
import org.eapp.exception.EappException;
import org.eapp.hbean.ShortCutMenu;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ServerStartupInit;

/**
 * @version 1.0
 */
public class ShortCutMenuAction extends BaseAction {

	/**
	 * 自动生成的序列号 5310494645619763510L
	 */
	private static final long serialVersionUID = 5310494645619763510L;
	private static final Log log = LogFactory.getLog(ShortCutMenuAction.class);
	
	private IShortCutMenuBiz shortCutMenuBiz;
	private ServerStartupInit serverStartupInit;
	private String shortCutMenuID;
	private String menuTitle;
	private String menuLink;
	private String logoURL;
	private String windowTarget;
	private String status;
	private String orderIDs;
	
//	private ListPage<ShortCutMenu> scmPage;
	private List<ShortCutMenu> scms;
	private ShortCutMenu shortCutMenu;
	private List<String> icons;
	
//	public ListPage<ShortCutMenu> getScmPage() {
//		return scmPage;
//	}

	public List<ShortCutMenu> getScms() {
		return scms;
	}

	public ShortCutMenu getShortCutMenu() {
		return shortCutMenu;
	}

	public List<String> getIcons() {
		return icons;
	}

	public void setShortCutMenuBiz(IShortCutMenuBiz shortCutMenuBiz) {
		this.shortCutMenuBiz = shortCutMenuBiz;
	}

	public void setServerStartupInit(ServerStartupInit serverStartupInit) {
		this.serverStartupInit = serverStartupInit;
	}
	
	public void setShortCutMenuID(String shortCutMenuID) {
		this.shortCutMenuID = shortCutMenuID;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public void setWindowTarget(String windowTarget) {
		this.windowTarget = windowTarget;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	/**
	 * 初始化列表页面
	 */
	public String initFrame() {
		return success();
	}
	
	/**
	 * 初始化列表页面
	 */
	public String initQueryPage() {
		return success();
	}
	
	/**
	 * 查询系统级的快捷方式列表
	 */
	public String queryShortCutMenu() {
//		scms = shortCutMenuBiz.getSystemShortCutMenus();
//		QueryParameters qp = new QueryParameters();
//		qp.setPageSize(pageSize);
//		qp.setPageNo(pageNo);
//		qp.addParameter("type", ShortCutMenu.SYSTEM_TYPE);
//		qp.addOrder("displayOrder", true);
		try {
//			scmPage = shortCutMenuBiz.queryShortCutMenus(qp);
			scms = shortCutMenuBiz.getSystemShortCutMenus();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化系统级快捷方式的新增页面
	 */
	public String initAdd() {
		// 查询图标列表
		icons = serverStartupInit.getShortCutIcons(getServletContext());
		return success();
	}
	
	/**
	 * 新增快捷方式
	 */
	public String addShortCutMenu() {
		SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (StringUtils.isBlank(menuTitle) || StringUtils.isBlank(menuLink) || StringUtils.isBlank(windowTarget)) {
			return error("参数不能为空");
		}
		boolean isValid = true;
		if ("0".equals(status)) {
			isValid = false;
		}
		
		try {
			ShortCutMenu shortCut = shortCutMenuBiz.addShortCutMenu(user.getUserID(), menuTitle, null, menuLink,
					windowTarget, logoURL, isValid, ShortCutMenu.SYSTEM_TYPE);
			return success(shortCut.getShortCutMenuID());
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
		if (StringUtils.isBlank(shortCutMenuID)) {
			return error("参数不能为空");
		}

		// 查询图标列表
		icons = serverStartupInit.getShortCutIcons(getServletContext());
		shortCutMenu = shortCutMenuBiz.getShortCutMenu(shortCutMenuID);
		return success();
	}
	
	/**
	 * 修改快捷方式
	 */
	public String modifyShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID) || StringUtils.isBlank(menuTitle) || StringUtils.isBlank(menuLink) 
				|| StringUtils.isBlank(windowTarget)) {
			return error("参数不能为空");
		}
		boolean isValid = true;
		if ("0".equals(status)) {
			isValid = false;
		}
		
		try {
			shortCutMenuBiz.modifyShortCutMenu(shortCutMenuID, menuTitle, menuLink, windowTarget, logoURL, isValid, ShortCutMenu.SYSTEM_TYPE);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 查看快捷方式详细信息
	 */
	public String viewShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID)) {
			return error("参数不能为空");
		}
		// 查询图标列表
		icons = serverStartupInit.getShortCutIcons(getServletContext());
		shortCutMenu = shortCutMenuBiz.getShortCutMenu(shortCutMenuID);
		return success();
	}
	
	/**
	 * 删除快捷方式 参数:快捷方式ID 返回:删除结果
	 */
	public String deleteShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID)) {
			return error("参数不能为空");
		}
		try {
			shortCutMenuBiz.deleteShortCutMenu(shortCutMenuID, false);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 撤销快捷方式
	 */
	public String disableShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID)) {
			return error("参数不能为空");
		}
		try {
			shortCutMenuBiz.txDisableShortCutMenu(shortCutMenuID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 发布快捷方式
	 */
	public String enableShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID)) {
			return error("参数不能为空");
		}
		try {
			shortCutMenuBiz.txEnableShortCutMenu(shortCutMenuID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化排序页面
	 */
	public String initOrder() {
		scms = shortCutMenuBiz.getSystemShortCutMenus();
		return success();
	}

	/**
	 * 排序快捷方式
	 */
	public String orderShortCutMenu() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		Map<String, Integer> idAndOrder = new HashMap<String, Integer>();
		String[] ids = orderIDs.split(",");
		for (int i = 0; i < ids.length; i++) {
			idAndOrder.put(ids[i], i + 1);
		}
		try {
			shortCutMenuBiz.modifyShortCutMenuSort(idAndOrder);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
