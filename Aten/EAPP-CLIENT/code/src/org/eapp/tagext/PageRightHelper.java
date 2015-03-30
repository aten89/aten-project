/**
 * 
 */
package org.eapp.tagext;

import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.ModuleMenuTree.ModuleMenu;


/**
 * <p>Title: </p>
 * @author zsy
 * @version 
 */
public abstract class PageRightHelper extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923640391894178294L;
	private static Log log = LogFactory.getLog(PageRightHelper.class);
	private static final long CACHE_TIME = 600000;//最大缓存时间
	private static final String SESSION_MENU_KEY = "sessinModuleMenu";
	
	/**
	 * 在JSP页面上取得权限，以控制按扭的显示与隐藏
	 * @param session
	 * @param moduleKey
	 * @return
	 */
	protected String getModuleRights(HttpSession session, String moduleKey) {
		if (session == null || moduleKey == null || moduleKey.trim().equals("")) {
			return "";
		}
		SessionAccount user = (SessionAccount)session.getAttribute(getSessionAccountBindKey());
		if (user == null) {
			return "";
		}
		String[] actionList = user.getModuleActions().get(moduleKey);
		if (actionList == null) {
			//如果为空立即加载
			try {
				actionList = getActionRightStr(moduleKey, user.getRoleIDs());
			} catch (Exception e) {
				log.error("加载用户动作权限失败", e);
			}
			synchronized(user) {
				user.getModuleActions().put(moduleKey, actionList);
			}
		}
		
		if (actionList == null || actionList.length < 2) {
			return "";
		}
		return actionList[1] == null ? "" : actionList[1];
	}
	
	/**
	 * 在JSP页面上取得有权限的三级菜单，以控制三级菜单的显示与隐藏
	 * 并将远程取得的整个菜单在session中缓存10分钟
	 * @param session
	 * @param parentModuleKey
	 * @return
	 */
	public String getMenuModuleKeys(HttpSession session, String parentModuleKey) {
		if (session == null || parentModuleKey == null || parentModuleKey.trim().equals("")) {
			return "";
		}
		ModuleMenuTree tree = null;
		ModuleMenuTreeCache treeCache = (ModuleMenuTreeCache)session.getAttribute(SESSION_MENU_KEY);
		if (treeCache != null && !treeCache.isOvertime()) {
			//有缓存且缓存时间没超过10分钟
			tree = treeCache.menuTree;
		} else {
			SessionAccount user = (SessionAccount)session.getAttribute(getSessionAccountBindKey());
			if (user == null) {
				return "";
			}
			
			try {
				tree = getModuleMenuTree(user.getRoleIDs());
				//缓存到session中
				session.setAttribute(SESSION_MENU_KEY, new ModuleMenuTreeCache(tree));
			} catch (Exception e) {
				log.error("加载用户模块菜单树失败", e);
			}
		}
		if (tree == null) {
			return "";
		}
		ModuleMenuTree findm = find(tree.getSubModuleMenu(), parentModuleKey);
		if (findm != null) {
			StringBuffer sb = new StringBuffer(",");
			for (ModuleMenuTree st : findm.getSubModuleMenu().values()) {
				sb.append(st.getModuleMenu().getModuleKey()).append(",");
			}
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 递归树查找
	 * @param tm
	 * @param moduleKey
	 * @return
	 */
	private ModuleMenuTree find(TreeMap<ModuleMenu, ModuleMenuTree> tm, String moduleKey) {
		if (tm == null || tm.size() < 1) {
			return null;
		}
		for (ModuleMenuTree st : tm.values()) {
			if (moduleKey.equals(st.getModuleMenu().getModuleKey())) {
				return st;
			}
			ModuleMenuTree finded = find(st.getSubModuleMenu(), moduleKey);
			if (finded != null) {
				return finded;
			}
		}
		return null;
	}
	
	/**
	 * 菜单树缓存
	 * @author zsy
	 * @version Nov 25, 2008
	 */
	private class ModuleMenuTreeCache {
		private ModuleMenuTree menuTree;
		private long cacheTime;
		public ModuleMenuTreeCache(ModuleMenuTree menuTree) {
			this.menuTree = menuTree;
			this.cacheTime = System.currentTimeMillis();
		}
		
		//缓存是否超时
		public boolean isOvertime() {
			return System.currentTimeMillis() - cacheTime > CACHE_TIME;
		}
	}
	
	/**
     * 获取SessionAccount对象绑定在Session中Key值
     * @return
     */
    protected abstract String getSessionAccountBindKey();
    
    /**
     * 加载权限动作
     * @param moduleKey
     * @param roleIDs
     * @return
     * @throws Exception
     */
    protected abstract String[] getActionRightStr(String moduleKey, List<String> roleIDs) throws Exception;
    
    /**
     * 加载模块菜单树
     * @param roleIDs
     * @return
     * @throws Exception
     */
    protected abstract ModuleMenuTree getModuleMenuTree(List<String> roleIDs) throws Exception;
}
