/**
 * 
 */
package org.eapp.client.tagext;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eapp.client.hessian.SessionAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.tagext.AbstractPageRightTag;


/**
 * 在JSP页面上取得权限，以控制按扭的显示与隐藏
 * @author zsy
 * @version Dec 1, 2008
 */
public class ModuleRightTag extends AbstractPageRightTag {

	private SessionAccountService service = new SessionAccountService();
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
		return service.getActionRightStr(SystemProperties.SYSTEM_ID, moduleKey, roleIDs);
	}

	@Override
	protected ModuleMenuTree getModuleMenuTree(List<String> roleIDs)
			throws Exception {
		return service.getModuleMenuTree(SystemProperties.SYSTEM_ID, roleIDs);
	}

	@Override
	protected String getSessionAccountBindKey() {
		return SystemProperties.SESSION_USER_KEY;
	}
	
}
