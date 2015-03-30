package org.eapp.oa.system.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.eapp.client.util.SystemProperties;
import org.eapp.oa.lucene.ConfigFactory;
import org.eapp.oa.lucene.IndexBuilder;
import org.eapp.oa.lucene.IndexTaskRunner;
import org.eapp.oa.lucene.LuceneConfig;
import org.eapp.oa.lucene.builder.FinalKBIndexBuilder;
import org.eapp.oa.lucene.builder.TempKBIndexBuilder;
import org.eapp.oa.lucene.db.DBIndexTaskQueue;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailConfig;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.UploadConfig;
import org.eapp.workflow.db.HibernateSessionFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 报表中心，WEB服务器启动初始化内容
 * 通过Spring的IoC容器的初始化来加载
 * 在Spring配置文件中配置实现，将Bean的初始化方法设为init，销毁方法设为destroy
 * 初始化文件组件的配置等信息
 * 
 * @author zsy
 *
 */
public class ServerStartupInit {
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(ServerStartupInit.class);
	private static final int TIMER_INTREVAL = 3600000; //定时周期一个小时
	private Timer timer;//定时器
	
	/**
	 * 流程引擎SessionFactory
	 */
	private SessionFactory wfSessionFactory;
	/**
	 * uploadConfig
	 * 上传文件配置信息
	 */
	private UploadConfig uploadConfig;
	/**
	 * mailConfig
	 * 邮件配置信息
	 */
	private MailConfig mailConfig;
	/**
	 * 索引配置
	 */
	private LuceneConfig luceneConfig;
	
	
	public void setWfSessionFactory(SessionFactory wfSessionFactory) {
		this.wfSessionFactory = wfSessionFactory;
	}

	public void setUploadConfig(UploadConfig uploadConfig) {
		this.uploadConfig = uploadConfig;
	}

	public void setMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}

	public void setLuceneConfig(LuceneConfig luceneConfig) {
		this.luceneConfig = luceneConfig;
	}

	/**
	 * 初始化方法
	 * 配置为Spring的init-method
	 */
	public void init() {
		logger.info("EAPP Server start...");
		
		SysConstants.SYSTEM_ID = SystemProperties.SYSTEM_ID;
		SysConstants.SESSION_USER_KEY = SystemProperties.SESSION_USER_KEY;
//		SysConstants.SESSION_USER_MENU = SystemProperties.getSessionMenuKey();
//		SysConstants.REQUEST_ERROR_MSG = SystemProperties.REQUEST_ERROR_MSG;
		
		//启动定时器
		timer = new Timer(true);
		timer.schedule(new PeriodTask(), TIMER_INTREVAL, TIMER_INTREVAL);
		
		HibernateSessionFactory.setSessionFactory(wfSessionFactory);
		//初始化上传文件管理的配置信息
		FileDispatcher.setConfig(uploadConfig);
		//初始化邮件服务的配置信息
		List<MailConfig> mailConfigs = new ArrayList<MailConfig>();
		mailConfigs.add(mailConfig);
		JMailProxy.setMailConfigs(mailConfigs);

		//初始化luCENE的配置信息
		ConfigFactory.init(luceneConfig);//初始化中文分词器
		IndexBuilder[] builders = new IndexBuilder[] {//初始化索引构建者
				new TempKBIndexBuilder(ConfigFactory.getIndexDir(SysConstants.TEMPKB_INDEX_DIR)),
				new FinalKBIndexBuilder(ConfigFactory.getIndexDir(SysConstants.FINALKB_INDEX_DIR)),
		};
		IndexTaskRunner.start(new DBIndexTaskQueue(), builders);//启动索引更新器
		logger.info("索引更新器已经启动..");
	}
	
	/**
	 * 销毁
	 * 配置为Spring的destroy-method
	 */
	public void destroy() {
		timer.cancel();
	}

}
