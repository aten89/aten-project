/**
 * 
 */
package org.eapp.crm.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 作为监听器配置在web.xml，如：
 * <listener>
        <listener-class>org.eapp.oa.util.SpringHelper</listener-class>
    </listener>
 * 
 * @author zsy
 */
public class SpringHelper extends ContextLoaderListener {
	private static ApplicationContext context;
	
	/**
	 * 初始化SpringHelper，确保外部取到的是同一个ApplicationContext
	 */
	@Override
	public void contextInitialized(ServletContextEvent event){
		super.contextInitialized(event);
		ServletContext servletCtx = event.getServletContext();
	   	//获取web环境下的ApplicationContext
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletCtx);
	} 

	/**
	 * 取得spring 的ApplicationContext
	 * @return
	 */
	public static ApplicationContext getSpringContext(){
		if (context == null) {
			throw new IllegalArgumentException("未初始化spring上下文");
		}
 		return context;
	}
	
	   /**
     * add by Tim 
     * 为了单元测试需要
     * @param appContext
     */
    public static void setSpringContext(ApplicationContext appContext){
        if (context == null) {
            context = appContext;
        }
    }
	
	/**
	 * 取得spring环境的Bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return getSpringContext().getBean(beanName);
	}
}
