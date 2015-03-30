/**
 * 
 */
package org.eapp.sys.tagext;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eapp.blo.IModuleBiz;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.hbean.Module;
import org.eapp.rpc.session.ISessionAccountInfoManage;
import org.eapp.sys.config.SysConstants;
import org.eapp.sys.util.ModuleTreeFacory;
import org.eapp.tagext.AbstractPageRightTag;
import org.eapp.util.spring.SpringHelper;


/**
 * 在JSP页面上取得权限，以控制按扭的显示与隐藏
 * @author zsy
 * @version Dec 1, 2008
 */
public class ModuleRightTag extends AbstractPageRightTag {

	private ISessionAccountInfoManage sessionAccountInfoManage = (ISessionAccountInfoManage) SpringHelper.getBean("sessionAccountInfoBiz");
	private IModuleBiz moduleBiz = (IModuleBiz) SpringHelper.getBean("moduleBiz");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6212051814059636542L;

	@Override
	protected String getTagContent(HttpSession session, String moduleKey) {
		return getModuleRights(session, moduleKey);
	}

	@Override
	protected String[] getActionRightStr(String moduleKey, List<String> roleIDs)
			throws Exception {
		return sessionAccountInfoManage.getActions(moduleKey, roleIDs);
	}

	@Override
	protected ModuleMenuTree getModuleMenuTree(List<String> roleIDs)
			throws Exception {
		List<Module> modules = moduleBiz.getHasRightModules(roleIDs, SysConstants.EAPP_SUBSYSTEM_ID);
		return ModuleTreeFacory.createModueMenuTree(modules);
	}

	@Override
	protected String getSessionAccountBindKey() {
		return SysConstants.SESSION_USER_KEY;
	}
	
}
