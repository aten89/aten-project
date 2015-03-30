package org.eapp.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eapp.workflow.asyn.exec.AsynJobRunner;
import org.eapp.workflow.config.ResourceReader;
import org.eapp.workflow.config.Resources;
import org.eapp.workflow.db.HibernateSessionFactory;
import org.eapp.workflow.expression.ExpressionEngine;
import org.hibernate.SessionFactory;


/**
 * 读取流程引擎的配置文件，做成单子模式
 * 简单的属性配置信息可以放在集合（map、set等）里，也可以进行分类放在多个集合里
 * 相对复杂的配置信息可以封装在对像；
 * 流程引擎全局的配置信息在这个类里体现
 * @author 卓诗垚
 * @version 1.0
 */
public class WfmConfiguration {
	private static WfmConfiguration singleton;//流程引擎配置环境单子

	private HibernateSessionFactory sessionFactory;//Hibernate Session工厂
	private Resources resources;//配置信息
	private Properties parameters;//系统配置属性
	
	private static final ThreadLocal<WfmContext> localContext = new ThreadLocal<WfmContext>();//线程变量
	
	private WfmConfiguration(String resource) {
		ResourceReader resourceReader = new ResourceReader(resource);
		resources = resourceReader.readResources();
		
		parameters = new Properties();
		//加载配置属性
		String propsFile = resources.getString("resource.system.properties");
		if (propsFile != null) {
			InputStream pin = null;
			try {
				pin = WfmConfiguration.class.getResourceAsStream(propsFile);
				if (pin != null) {
					parameters.load(pin);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (pin != null) {
					try {
						pin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 使用默认配置文件构造实例
	 */
	public static WfmConfiguration getInstance() {
    	return getInstance("/org/eapp/workflow/wfm.cfg.xml");
    }
	
	/**
	 * 使用指定配置文件构造实例
	 * @param resource
	 * @return WfmConfiguration
	 */
	public static WfmConfiguration getInstance(String resource) {
    	if (singleton == null) {
    		synchronized(WfmConfiguration.class){
    			if(singleton == null){
    				singleton = new WfmConfiguration(resource);
    			}
    		}
    	}
    	return singleton;
    }
	
	/**
	 * 使用外部的SessionFactory
	 * @param wfSessionFactory
	 */
	public void init(SessionFactory wfSessionFactory, Properties props) {
		//初始化Hibernate
		synchronized(this){
			if (wfSessionFactory != null) {
				sessionFactory = new HibernateSessionFactory(wfSessionFactory);
			} else {
				sessionFactory = new HibernateSessionFactory(resources.getString("resource.hibernate.cfg.xml"));
			}
		}
		Properties currentProps = parameters;
		if (props != null) {
			currentProps = props;
		}
		
		//初始化异步任务服务
		AsynJobRunner.getInstance().init(currentProps);
		
		//添加扩展函数
		String scriptFunPrefix = "SCRIPT_FUN_";//表达式函数属性名称前缀
		Map<String, Object> objFuns = new HashMap<String, Object>();
		try {
			for (Object oKey : currentProps.keySet()) {
				String key = (String)oKey;
				if (key.startsWith(scriptFunPrefix)) {
					String funClassName = currentProps.getProperty(key);
					Class<?> funClass = Class.forName(funClassName);
					objFuns.put(key.substring(scriptFunPrefix.length()), funClass.newInstance());
				}
			}
			ExpressionEngine.setFunctions(objFuns);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 系统启动时，使用流程API之前必须要初始化一下
	 */
	public void init() {
		init(null, null);
	}
	
	
	/**
	 * 取得hibernate session factory
	 * 本类为单例模式，则sessionFactory对像也只存在一个
	 * @return Hibernate的Session工厂
	 */
	public HibernateSessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			init();
		}
		return sessionFactory;
	}
	
	/**
	 * 取得配置信息
	 * @return resources
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * 取得配置属性
	 * @return parameters
	 */
	public Properties getParameters() {
		return parameters;
	}

	/**
	 * 取得流程的上下文
	 * 使用本地线程关联
	 * 
	 * @return WfmContext
	 */
	public  WfmContext getWfmContext() {		
		WfmContext wfmContext = localContext.get();
		if(wfmContext == null || wfmContext.isClosed){
			wfmContext = new WfmContext(this);			
			localContext.set(wfmContext);
		}
		return wfmContext;
	}
	
	/**
	 * 取得本地线程关联流程的上下文,并关闭它
	 * 
	 */
	public void closeWfmContext(){
		WfmContext wfmContext = localContext.get();
		if(wfmContext != null && !wfmContext.isClosed){
			wfmContext.close();
		}
		localContext.set(null);
	}
	
	/**
	 * 释放流程引擎配置实例
	 * 
	 */
	public void release(){
		//关闭异步执行器
		AsynJobRunner.getInstance().stop();
		//关闭Hibernate服务
		sessionFactory.close();
	}
}
