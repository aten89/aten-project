/**
 * 
 */
package org.eapp.sys.tagext;

import javax.servlet.http.HttpSession;



/**
 * 在JSP页面上取得有权限的三级菜单，以控制三级菜单的显示与隐藏
 * @author zsy
 * @version Dec 1, 2008
 */
public class MenuRightTag extends ModuleRightTag {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6212051814059636542L;

	@Override
	protected String getTagContent(HttpSession session, String moduleKey) {
		return getMenuModuleKeys(session, moduleKey);
	}
}
