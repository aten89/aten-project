/**
 * 
 */
package org.eapp.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取WEB环境中Spring容器中的管理对象
 * 通过实现ApplicationContextAware接口
 * 获取ApplicationContext对象（此类要在SPRING注册管理）
 * 如：<bean class="org.eapp.util.spring.SpringHelper"></bean>
 * @author zsy
 *
 */
public class SpringHelper implements ApplicationContextAware {
	/**
	 * spring ApplicationContext
	 */
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	/**
	 * 取得spring的ApplicationContext
	 * @return 返回ApplicationContext对象
	 */
	public static ApplicationContext getSpringContext() {
		if (context == null) {
			throw new IllegalArgumentException("未初始化spring上下文");
		}
 		return context;
	}
	
	/**
	 * 取得spring环境的Bean
	 * @param beanName spring容器中Bean的名称
	 * @return 返回Bean对象
	 */
	public static Object getBean(String beanName) {
		return getSpringContext().getBean(beanName);
	}

}
