package org.eapp.workflow.db;

import org.eapp.util.security.SymmetricAlgorithm;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * 提供了方便的session及Transaction获取，关闭方法。
 * 使用ThreadLocal对象维护session及Transaction在一个线程操作（通常也是事务操作）中的唯一性
 * @author 卓诗垚
 * @version 1.0
 */
public class HibernateSessionFactory {

	private String configFile;
	
	private final ThreadLocal<Session> localSession = new ThreadLocal<Session>();
	private final ThreadLocal<Transaction> localTransaction = new ThreadLocal<Transaction>();
	private final ThreadLocal<Boolean> localRollback = new ThreadLocal<Boolean>();//当前事务是否回滚
	
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public HibernateSessionFactory() {
    	this("/hibernate.cfg.xml");
    }
    
    public  HibernateSessionFactory(String configFile) {
    	this.configFile = configFile;
    	if (configFile == null) {
    		configFile = "/hibernate.cfg.xml";
    	}
    	initSessionFactory();
    }
    
    public  HibernateSessionFactory( SessionFactory sessionFactory) {
    	this.sessionFactory = sessionFactory;
    }
    
	/**
     *  初始化 hibernate session factory
     *
     */
	private void initSessionFactory() {
		synchronized(this){
			if (sessionFactory == null) {
				try {
					configuration = new Configuration().configure(configFile);
					String encrypt = configuration.getProperty("encrypt");
					if ("true".equals(encrypt)) {
						String password = configuration.getProperty("hibernate.connection.password");
						password = new SymmetricAlgorithm(password).desDecrypt();
						configuration.setProperty("hibernate.connection.password",password);
					
					}
//				    	sessionFactory = configuration.buildSessionFactory();
					ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
					sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				} catch (Exception e) {
					System.err.println("%%%% Error Creating SessionFactory %%%%");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *	@param isTransactionEnabled --is begin transaction
     *  @return Session
     *  @throws HibernateException
     */
    public Session getSession(boolean isTransactionEnabled) throws HibernateException {
        Session session = (Session) localSession.get();
		if (session == null || !session.isOpen()) {
			session = sessionFactory.openSession();
			localSession.set(session);
		}
		if (isTransactionEnabled) {
			Transaction tx = (Transaction)localTransaction.get();
	    	if (tx == null || !tx.isActive()) {
	    		tx = session.beginTransaction();
	    		localTransaction.set(tx);
	    	}
		}
        return session;
    }
    
    public Session getSession() throws HibernateException {
    	return getSession(false);
    }

	/**
	 * 关闭session 并处理事务
	 * @throws HibernateException
	 */
    public void closeSession() throws HibernateException {
        Session session = (Session) localSession.get();
        if (session != null && session.isOpen()) {
        	try {
	        	//处理事务
	        	Transaction tx = (Transaction)localTransaction.get();
	        	if(tx != null && tx.isActive()){
	        		if (isRollback()) {
        				tx.rollback();
        				setRollback(false);
        			} else {
        				try {
        			  	    tx.commit();
        				} catch(Exception e) {
    	        			e.printStackTrace();
    	        			tx.rollback();
    	        			//2013-04-16 增加往外抛出异常，在各个业务系统，才能捕获到异常信息，进行回滚！
    	        			throw new HibernateException (e);
    	        		}
        			}
	        		
	        	}
        	} catch(HibernateException e) {
        		e.printStackTrace();
        		throw e;
        	} finally {
        		session.close();
        	}
        }
        localSession.set(null);
        localTransaction.set(null);
    }

    /**
     * 设置当前线程事务回滚
     * @param isRollback
     */
    public void setRollback(boolean isRollback) {
    	localRollback.set(Boolean.valueOf(isRollback));
    }
    
    /**
     * 当前线程的事务是否要回滚
     * @return
     */
    public boolean isRollback() {
    	Boolean isRollback = localRollback.get();
    	if (isRollback == null) {
    		return false;
    	}
    	return isRollback;
    }
	
    /**
     * 关闭Hibernate服务
     */
    public void close(){
    	sessionFactory.close();
    }
	    
}