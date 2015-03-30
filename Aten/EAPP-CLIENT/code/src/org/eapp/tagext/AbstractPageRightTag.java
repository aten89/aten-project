/**
 * 
 */
package org.eapp.tagext;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.cas.AbstractCASFilter;

/**
 * JSP标签实现
 * 在JSP页面上取得有权限的三级菜单，以控制三级菜单的显示与隐藏
 * 在JSP页面上取得权限，以控制按扭的显示与隐藏
 * @author zsy
 * @version Dec 1, 2008
 */
public abstract class AbstractPageRightTag extends PageRightHelper {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7471362303224528624L;

	private static Log log = LogFactory.getLog(AbstractPageRightTag.class);
	
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int doStartTag() {
		String moduleKey = key;
		if (moduleKey == null) {
			//key为空时自动从Request获取
			moduleKey = (String)pageContext.getRequest().getAttribute(AbstractCASFilter.CURRENT_REQUEST_MODULEKEY);
			log.debug("从Request获取的moduleKey:" + moduleKey);
		}
		if (moduleKey != null) {
			try {
				JspWriter out = pageContext.popBody();
				out.print(getTagContent(pageContext.getSession(), moduleKey));
			} catch (IOException e) {
				log.error("输出标签信息失败", e);
			}
		}
		return SKIP_BODY;
	}
	
	@Override
	public final int doEndTag() throws JspException {
   	 	return EVAL_PAGE;
	}
	
	/**
	 * 获取标签输出内容
	 * @param session
	 * @param moduleKey
	 * @return
	 */
	protected abstract String getTagContent(HttpSession session, String moduleKey);
}
