package org.eapp.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IModuleBiz;
import org.eapp.blo.IPortletBiz;
import org.eapp.blo.IShortCutMenuBiz;
import org.eapp.blo.ISubSystemBiz;
import org.eapp.blo.ISysMsgBiz;
import org.eapp.blo.IUserAccountBiz;
import org.eapp.blo.IUserPortalBiz;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.SessionAccount;
import org.eapp.dao.param.SysMsgQueryParameters;
import org.eapp.dto.ModuleMenuXml;
import org.eapp.exception.EappException;
import org.eapp.hbean.DataDictionary;
import org.eapp.hbean.Portlet;
import org.eapp.hbean.ShortCutMenu;
import org.eapp.hbean.SubSystem;
import org.eapp.hbean.SysMsg;
import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ServerStartupInit;
import org.eapp.sys.util.ServerStartupInit.SubSystemConfig;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * 主页宽框
 * @author zsy
 * @version
 */
public class MainFrameAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(MainFrameAction.class);
	
	//服务类
	private IModuleBiz moduleBiz;
	private ISubSystemBiz subSystemBiz;
	private IUserPortalBiz userPortalBiz;
	private IPortletBiz portletBiz;
	private IShortCutMenuBiz shortCutMenuBiz;
	private IUserAccountBiz userAccountBiz;
	private ISysMsgBiz sysMsgBiz;
	private ServerStartupInit serverStartupInit;
	
	//输入
	private String systemID;
	private String shortCutMenuID;
	private String moduleTitle;
	private String menuTitle;
	private String menuLink;
	private String logoURL;
	private String windowTarget;
	private String status;
	private String type;
	
	private String styleThemes;
	private String oldPassword;
	private String newPassword;
	
	private String portletConfig;
	
	private String enableScmConfig;
	private String disableScmConfig;
	
	private int pageNo;
	private int pageSize;
	private String msgSender;
	private String beginTime;
	private String endTime;
	private String viewFlag;
	private String msgID;
	private String[] msgIDs;
	
	//输出
	private String htmlTitle;
	private List<SubSystem> subSystems;
	private List<Portlet> portlets;
	private String indexPageUrl;
	private String xmlValue;
	private List<ShortCutMenu> shortCutMenus;
	private Set<ShortCutMenu> enableScms;
	private Set<ShortCutMenu> disableScms;
	private List<String> scmIcons;
	private ShortCutMenu shortCutMenu;
	private ListPage<SysMsg> sysMsgPage;
	private SysMsg sysMsg;
	private boolean forceChangePassword;
	

	public void setModuleBiz(IModuleBiz moduleBiz) {
		this.moduleBiz = moduleBiz;
	}

	public void setSubSystemBiz(ISubSystemBiz subSystemBiz) {
		this.subSystemBiz = subSystemBiz;
	}

	public void setUserPortalBiz(IUserPortalBiz userPortalBiz) {
		this.userPortalBiz = userPortalBiz;
	}

	public void setPortletBiz(IPortletBiz portletBiz) {
		this.portletBiz = portletBiz;
	}

	public void setShortCutMenuBiz(IShortCutMenuBiz shortCutMenuBiz) {
		this.shortCutMenuBiz = shortCutMenuBiz;
	}

	public void setUserAccountBiz(IUserAccountBiz userAccountBiz) {
		this.userAccountBiz = userAccountBiz;
	}

	public void setSysMsgBiz(ISysMsgBiz sysMsgBiz) {
		this.sysMsgBiz = sysMsgBiz;
	}

	public void setServerStartupInit(ServerStartupInit serverStartupInit) {
		this.serverStartupInit = serverStartupInit;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	
	public void setShortCutMenuID(String shortCutMenuID) {
		this.shortCutMenuID = shortCutMenuID;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
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

	public void setType(String type) {
		this.type = type;
	}

	public void setStyleThemes(String styleThemes) {
		this.styleThemes = styleThemes;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setPortletConfig(String portletConfig) {
		this.portletConfig = portletConfig;
	}

	public void setEnableScmConfig(String enableScmConfig) {
		this.enableScmConfig = enableScmConfig;
	}

	public void setDisableScmConfig(String disableScmConfig) {
		this.disableScmConfig = disableScmConfig;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void setMsgSender(String msgSender) {
		this.msgSender = msgSender;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public void setMsgIDs(String[] msgIDs) {
		this.msgIDs = msgIDs;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}
	
	public List<SubSystem> getSubSystems() {
		return subSystems;
	}

	public List<Portlet> getPortlets() {
		return portlets;
	}

	public String getIndexPageUrl() {
		return indexPageUrl;
	}

	public String getXmlValue() {
		return xmlValue;
	}

	public List<ShortCutMenu> getShortCutMenus() {
		return shortCutMenus;
	}

	public Set<ShortCutMenu> getEnableScms() {
		return enableScms;
	}

	public Set<ShortCutMenu> getDisableScms() {
		return disableScms;
	}

	public List<String> getScmIcons() {
		return scmIcons;
	}

	public ShortCutMenu getShortCutMenu() {
		return shortCutMenu;
	}

	public ListPage<SysMsg> getSysMsgPage() {
		return sysMsgPage;
	}

	public SysMsg getSysMsg() {
		return sysMsg;
	}
	
	public boolean getForceChangePassword() {
		return forceChangePassword;
	}

	//以配置文件中的子系统配置优先
	private boolean isSubSystemConfigVaild(String subSystemID) {
		SubSystemConfig sysConf = serverStartupInit.getSubSystemConfig().get(subSystemID);
		if (sysConf == null) {
			return true;
		} else {
			return sysConf.getIsValid();
		}
	}

	//进入主页框架
	public String initFramePage() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		try {
			forceChangePassword = userAccountBiz.getForceChangePassword(user.getAccountID());
			if (forceChangePassword) {
				//要强制修改密码就直接返回，不需要再加载系统信息
				return success();
			}
			subSystems = subSystemBiz.getHasRightSubSystems(user.getRoleIDs());
			if (subSystems != null && !subSystems.isEmpty()) {
				for (SubSystem sys : new ArrayList<SubSystem>(subSystems)) {
					if (SysConstants.EAPP_SUBSYSTEM_ID.equals(sys.getSubSystemID())) {
						htmlTitle = sys.getName();
					}
					if (!isSubSystemConfigVaild(sys.getSubSystemID())) {
						//移除配置文件中禁用的子系统
						subSystems.remove(sys);
					}
				}
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//进入首页门户
	public String initIndexPage() {
		//从数据字典里查URL
		Map<String, DataDictionary> typeMap = SysCodeDictLoader.getInstance().getDataDictsByType(SysConstants.DICT_KEY_INDEX_PAGE);
		if (typeMap != null && typeMap.size() > 0) {
			DataDictionary dict = typeMap.values().iterator().next();
			indexPageUrl = dict.getCeilValue();
		}
		if (indexPageUrl == null) {
			//默认
			indexPageUrl = SysConstants.DEFAULT_INDEX_PAGE;
		}
		return success();
	}
	
	//取得有权限的模块XML
	public String loadModuleMenuXml() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		systemID = StringUtils.trimToNull(systemID);
		if (systemID == null) {
			systemID = SysConstants.EAPP_SUBSYSTEM_ID;
		}
		if (!isSubSystemConfigVaild(systemID)) {
			return error("子系统已配置为禁用");
		}
		try {
			ModuleMenuTree tree = moduleBiz.getHasRightModuleTree(user.getRoleIDs(), systemID);
			xmlValue = new ModuleMenuXml(tree).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//取得固定的模块XML
	public String loadFixedModuleMenuXml() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		try {
			ModuleMenuTree tree = moduleBiz.getFixedModuleTree(user.getRoleIDs());
			xmlValue = new ModuleMenuXml(tree).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	//加载首页门户块
	public String loadMyPortals() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		try{
			portlets = userPortalBiz.getUserPortlets(user.getUserID(), user.getRoleIDs(), systemID);
			
			if (portlets != null && !portlets.isEmpty()) {
				for (Portlet port : new ArrayList<Portlet>(portlets)) {
					if (!isSubSystemConfigVaild(port.getSubSystem().getSubSystemID())) {
						//移除配置文件中禁用的子系统的门户
						portlets.remove(port);
					}
				}
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//快捷菜单
	public String loadMyShortCuts() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		Set<ShortCutMenu> custonShortCuts = shortCutMenuBiz.getEnableShortCutMenu(user.getUserID());
		List<ShortCutMenu> systemShortCuts = shortCutMenuBiz.getEnableSystemShortCutMenus();
		shortCutMenus = new ArrayList<ShortCutMenu>();
		shortCutMenus.addAll(custonShortCuts);
		shortCutMenus.addAll(systemShortCuts);
		
		return success();
	}
	
	//收藏夹
	public String loadMyFavorites()  {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		Set<ShortCutMenu> custonShortCuts = shortCutMenuBiz.getFavoriteShortCutMenu(user.getUserID());
		shortCutMenus = new ArrayList<ShortCutMenu>(custonShortCuts);
		return success();
	}
	
	//删除收藏夹/快捷菜单
	public String deleteUserShortCut() {
		if(StringUtils.isBlank(shortCutMenuID)){
			return error("快捷方式ID不能为空");
		}
		try {
			shortCutMenuBiz.deleteShortCutMenu(shortCutMenuID, true);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initEditUserShortCutMenu() {
		if (StringUtils.isNotBlank(shortCutMenuID)) {
			shortCutMenu = shortCutMenuBiz.getShortCutMenu(shortCutMenuID);
		}
		// 查询图标列表
		scmIcons = serverStartupInit.getShortCutIcons(getServletContext());
		return success();
	}
	
	//新增收藏夹/快捷菜单
	public String addUserShortCutMenu() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		
		if (StringUtils.isBlank(menuTitle) || StringUtils.isBlank(menuLink) || StringUtils.isBlank(windowTarget) || StringUtils.isBlank(type)) {
			return error("参数不能为空");
		}
		if (!ShortCutMenu.CUSTOM_TYPE.equals(type) && !ShortCutMenu.FAVORITE_TYPE.equals(type)) {
			return error("type参数不正确");
		}
		
		boolean isValid = true;
		if ("0".equals(status)) {
			isValid = false;
		}
		try {
			ShortCutMenu shortCut = shortCutMenuBiz.addShortCutMenu(user.getUserID(), menuTitle, moduleTitle, menuLink,
					windowTarget, logoURL, isValid, type);
			return success(shortCut.getShortCutMenuID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//新增收藏夹/快捷菜单
	public String modifyUserShortCutMenu() {
		if (StringUtils.isBlank(shortCutMenuID) || StringUtils.isBlank(menuTitle) || StringUtils.isBlank(menuLink) 
				|| StringUtils.isBlank(windowTarget) || StringUtils.isBlank(type)) {
			return error("参数不能为空");
		}

		if (!ShortCutMenu.CUSTOM_TYPE.equals(type) && !ShortCutMenu.FAVORITE_TYPE.equals(type)) {
			return error("type参数不正确");
		}
		boolean isValid = true;
		if ("0".equals(status)) {
			isValid = false;
		}
		try {
			shortCutMenuBiz.modifyShortCutMenu(shortCutMenuID, menuTitle, menuLink, windowTarget, logoURL, isValid, type);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//进入我的首页定制
	public String initMyPortal() {
		return success();
	}
	
	//加载我的所有门户块
	public String loadMyAllPortals() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		portlets = portletBiz.getPortlets(user.getRoleIDs());
		if (portlets != null && !portlets.isEmpty()) {
			for (Portlet port : new ArrayList<Portlet>(portlets)) {
				if (!isSubSystemConfigVaild(port.getSubSystem().getSubSystemID())) {
					//移除配置文件中禁用的子系统的门户
					portlets.remove(port);
				}
			}
		}
		
		return success();
	}

	//保存我的门户配置
	public String saveMyPortalConf() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}

		List<String[]> portletConfigList = new ArrayList<String[]>();
		if (StringUtils.isNotBlank(portletConfig)) {
			String[] configs = portletConfig.split("@");
			for (String config : configs) {
				String[] detail = config.split(",");
				if (detail.length != 3) {
					return error("配置信息格式不正确");
				}
				portletConfigList.add(detail);
			}
		}
		try {
			userPortalBiz.modifyUserPortlets(user.getAccountID(), portletConfigList);
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	//进入我的快捷菜单
	public String initMyShortcutMenu() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		enableScms = shortCutMenuBiz.getEnableShortCutMenu(user.getUserID());
		disableScms = shortCutMenuBiz.getDisableShortCutMenu(user.getUserID());
		return success();
	}
	
	//保存我的快捷菜单配置
	public String saveMyShortCutMenuConf() {
		Map<String, Integer> idAndOrderEnable = new HashMap<String, Integer>();
		Map<String, Integer> idAndOrderDisable = new HashMap<String, Integer>();
		
		if (enableScmConfig != null) {
			String[] publishArray = enableScmConfig.split(",");
			for (int i = 0; i < publishArray.length; i++) {
				idAndOrderEnable.put(publishArray[i], i + 1);
			}
		}
		int sort = idAndOrderEnable.size();
		if (disableScmConfig != null) {
			String[] cancelArray = disableScmConfig.split(",");
			for (int i = 0; i < cancelArray.length; i++) {
				idAndOrderDisable.put(cancelArray[i], sort + i + 1);
			}
		}
		try {
			shortCutMenuBiz.txManageShortCutMenus(idAndOrderEnable, idAndOrderDisable);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//进入皮肤更换
	public String initMySkin() {
		return success();
	}
	
	//皮肤更换
	public String changeSkin() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		if (StringUtils.isBlank(styleThemes)) {
			return error("参数不能为空");
		}
		
		try {
			userAccountBiz.txSetStyleThemes(user.getAccountID(), styleThemes);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//进入密码修改
	public String initMyPassword() {
		return success();
	}
	
	//密码修改
	public String changePasswrod() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			return error("参数不能为空");
		}

		try {
			userAccountBiz.txSetPassword(user.getAccountID(), oldPassword, newPassword);
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//进入系统信息
	public String initMySysMsg() {
		return success();
	}
	
	//查询系统信息
	public String queryMySysMsgs() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		
		SysMsgQueryParameters aqp = new SysMsgQueryParameters();
		aqp.setPageSize(pageSize);
		aqp.setPageNo(pageNo);
		aqp.setToAccountID(user.getAccountID());
		if (StringUtils.isNotBlank(msgSender)) {
			aqp.setMsgSender(msgSender);
		}
		if (StringUtils.isNotBlank(viewFlag)) {
			aqp.setViewFlag("Y".equals(viewFlag));
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			Timestamp _beginTime = Timestamp.valueOf(DataFormatUtil.formatTime(beginTime));
			aqp.setBeginTime(_beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			Timestamp _endTime = Timestamp.valueOf(DataFormatUtil.formatTime(endTime));
			Calendar c = Calendar.getInstance();
			c.setTime(_endTime);
			c.set(Calendar.HOUR_OF_DAY, 24);
			_endTime = new Timestamp(c.getTimeInMillis());
			aqp.setEndTime(_endTime);
		}
		aqp.addOrder("sendTime", false);
		try {
			sysMsgPage = sysMsgBiz.querySysMsgs(aqp);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//取最新未读的系统信息
	public String loadMyLastNoView() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		sysMsg = sysMsgBiz.txGetLastNoView(user.getAccountID());
		return success();
	}
	
	//删除系统信息
	public String deleteMySysMsgs() {
		if (msgIDs == null || msgIDs.length < 1) {
			return error("参数不能为空");
		}
		try {
			sysMsgBiz.deleteSysMsgs(msgIDs);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//标识已读系统信息
	public String ViewMySysMsg() {
		try {
			sysMsgBiz.modifyViewFlag(msgID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
