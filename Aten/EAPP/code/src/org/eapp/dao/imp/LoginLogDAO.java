package org.eapp.dao.imp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.ILoginLogDAO;
import org.eapp.dao.param.LogQueryParameters;
import org.eapp.hbean.LoginLog;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.hibernate.Session;


/**
 * @author zsy
 * @version
 */
public class LoginLogDAO extends BaseHibernateDAO implements ILoginLogDAO {
    private static final Log log = LogFactory.getLog(LoginLogDAO.class);
	
	/**
	 * 日志表名
	 */
	private static final String LOG_TABLE_NAME = "EAPP_LOGIN_LOG";

	/**
	 * 获取日志表名
	 * @param year 
	 * @return
	 */
	private String getTableName(int year) {
		return LOG_TABLE_NAME +"_" + year;
	}

    
    @Override
    public void createLogTable(int year) {
    	String tableName = getTableName(year);
		//创建备份表
		Query query = getSession().createSQLQuery("create table " + tableName + " as select * from " + LOG_TABLE_NAME + " where 1<>1");
		query.executeUpdate();
		//创建ACTIONLOG_ID索引
		query = getSession().createSQLQuery("create index LOGIN_LOG_ID_A"+ year + " on " + tableName + " (LOG_ID_ ASC)");
		query.executeUpdate();
		//创建SESSION_ID_索引
		query = getSession().createSQLQuery("create index SESSION_ID_A" + year + " on " + tableName + " (SESSION_ID_ ASC)");
		//创建ACTORACCOUNTID_索引
		query = getSession().createSQLQuery("create index LOGIN_ACCOUNT_ID_A" + year + " on " + tableName + " (ACCOUNT_ID_ ASC)");
		query.executeUpdate();
		//创建ACTORDISPLAYNAME_索引
		query = getSession().createSQLQuery("create index LOGIN_ACCOUNT_NAME_A" + year + " on " + tableName + " (ACCOUNT_NAME_ ASC)");
		query.executeUpdate();
		//创建OPERATETIME_索引
		query = getSession().createSQLQuery("create index LOGIN_TIME_A" + year + " on " + tableName + " (LOGIN_TIME_ ASC)");
		//创建LOGOUT_TIME_索引
		query = getSession().createSQLQuery("create index LOGOUT_TIME_A" + year + " on " + tableName + " (LOGOUT_TIME_ ASC)");
		query.executeUpdate();
    }
    
    @Override
    public boolean existTable(int year) {
    	try {
    		Query query = getSession().createSQLQuery("select count(*) from " + getTableName(year) + " where 1<>1");
    		query.uniqueResult();
    		return true;
    	 } catch (RuntimeException re) {
    		 log.warn("当年接口日志表不存在" + getTableName(year));
             return false;
         }
    	 
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public ListPage<LoginLog> queryLog(LogQueryParameters logQP) {
		if (logQP == null)  {
			throw new IllegalArgumentException();
		}
		ListPage<LoginLog> listPage = new ListPage<LoginLog>();
		
		Timestamp beginTime = logQP.getBeginTime();
		Timestamp endTime = logQP.getEndeTime();
		if (beginTime == null || endTime == null) {
			throw new IllegalArgumentException("查询时间不能为空");
		}
		Calendar c = Calendar.getInstance();
		//当年
		int cYear = c.get(Calendar.YEAR);

		c.setTime(beginTime);
		//开始年
		int bYear = c.get(Calendar.YEAR);
		c.setTime(endTime);
		//结束年
		int eYear = c.get(Calendar.YEAR);
		if (bYear != eYear) {
			throw new IllegalArgumentException("不能跨年度查询");
		}
		
		String tableName = LOG_TABLE_NAME;
		if (bYear < cYear) {
			if (!existTable(bYear)) {
				log.warn("日志查询：无当年数据");
				return listPage;
			}
			tableName = getTableName(bYear);
		}
		
		StringBuffer hql = new StringBuffer("from ").append(tableName)
				.append(" log where log.LOGIN_TIME_>=:beginTime and log.LOGIN_TIME_ <=:endTime");
		
		if (logQP.getAccountID() != null) {
			hql.append(" and log.ACCOUNT_ID_=:accountID");
		}
		if (logQP.getAccountName() != null) {
			hql.append(" and log.ACCOUNT_NAME_ like :accountName");
			logQP.toArountParameter("accountName");
		}

		Session session = getSession();
		
        int pageNo = logQP.getPageNo();
        int pageSize = logQP.getPageSize();
        int firstResultIndex = (pageNo - 1) * pageSize;
        Query query = session.createSQLQuery("select count(*) " + hql.toString());
        query.setProperties(logQP.getParameters());
//	        CommQuery.launchParamValues(query, logQP.getParameters());
        long totalCount = ( (Number)query.uniqueResult() ).longValue();
        if (totalCount > 0 && firstResultIndex < totalCount) {
          	query = session.createSQLQuery("select log.* " + hql.toString() 
          			+ " order by log.LOGIN_TIME_ desc").addEntity(LoginLog.class);
          	query.setProperties(logQP.getParameters());
//	          	CommQuery.launchParamValues(query, logQP.getParameters());
          	query.setFirstResult(firstResultIndex);
          	if (pageSize > 0) {
          		query.setMaxResults(pageSize);
          	}
          	List<LoginLog> result = query.list();
          	
          	listPage.setTotalCount(totalCount);
          	listPage.setDataList(result);
        }
        listPage.setCurrentPageNo(pageNo);
      	listPage.setCurrentPageSize(pageSize);
	        
		return listPage;
	}
    
	@Override
	public ListPage<LoginLog> queryOnLineLog(LogQueryParameters logQP) {
		if (logQP == null) {
			throw new IllegalArgumentException();
		}
		//只要从当前表查找即可
		try {
			StringBuffer hql = new StringBuffer("select ll from LoginLog as ll where ll.logoutTime is null and ll.isSuccess=1");
			if (logQP.getAccountID() != null) {
				hql.append(" and ll.accountID=:accountID");
			}
			if (logQP.getAccountName() != null) {
				hql.append(" and ll.accountName like :accountName");
				logQP.toArountParameter("accountName");
			}
			hql.append(" order by ll.loginTime desc");
			return new CommQuery<LoginLog>().queryListPage(logQP, 
					logQP.appendOrders(hql, "ll" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<LoginLog>();
		}
	}
	
	@Override
	public int findTotalOnlineCount() {
		//只要从当前表查找即可
		Query query = getSession().createQuery("select count(ll) from LoginLog as ll where ll.logoutTime is null and ll.isSuccess=1");
		Long count = (Long) query.uniqueResult();
		return count == null ? 0 : count.intValue();
	}
	
	@Override
	public LoginLog findByID(String logID) {
		int year;
		try {
			year = Integer.parseInt(logID.substring(0, 4));
		} catch (Exception e) {
			log.error("取得日志时间错误");
			return null;
		}
		Calendar c = Calendar.getInstance();
		int cYear = c.get(Calendar.YEAR);
		String tableName = LOG_TABLE_NAME;
		if (year < cYear) {
			tableName = getTableName(year);
		}
		Query query = getSession().createSQLQuery("select log.* from " + tableName 
				+ " log where log.LOG_ID_=:logID").addEntity(LoginLog.class);
		query.setString("logID", logID);
		return (LoginLog)query.uniqueResult();
	}
	
	@Override
	public LoginLog findBySessionID(String sessionID) {
		//只要从当前表查找即可
		Query query = getSession().createQuery("from LoginLog as ll where ll.sessionID=:sessionID");
		query.setString("sessionID", sessionID);
		query.setMaxResults(1);
		return (LoginLog)query.uniqueResult();
	}
	
	@Override
	public void updateAllLogoutTime() {
		Query query = getSession().createQuery("update LoginLog as ll set ll.logoutTime=:logoutTime where ll.logoutTime is null");
		query.setTimestamp("logoutTime", new Timestamp(System.currentTimeMillis()));
		query.executeUpdate();
	}
	
	@Override
	public void backUpLastDayLog() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		backUpLog(c);
	}
	
	/**
	 * 备份给定时间的当天的日志到相应的备份表
	 * @param time 时间
	 */
	private void backUpLog(Calendar time) {
		if (time == null) {
			throw new IllegalArgumentException();
		}
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);	
		time.set(Calendar.MILLISECOND, 0);
		Timestamp beginTime = new Timestamp(time.getTimeInMillis());
		time.set(Calendar.HOUR_OF_DAY, 24);
		time.add(Calendar.MILLISECOND, -1);//12月31日时小时设为24就变为跨年了，所以要毫秒-1
		
		Timestamp endTime = new Timestamp(time.getTimeInMillis());
		int year = time.get(Calendar.YEAR);
		
		if (!existTable(year)) {
			createLogTable(year);
		}
		String tableName = getTableName(year);
		
		Session session = getSession();
		Query query = session.createSQLQuery("insert into " + tableName + " select l.* from " + LOG_TABLE_NAME + " l " +
				"where l.LOGIN_TIME_>=:beginTime and l.LOGIN_TIME_<=:endTime and l.IS_BACKUP_=0");
		query.setTimestamp("beginTime", beginTime);
		query.setTimestamp("endTime", endTime);
		query.executeUpdate();
		query = session.createSQLQuery("update " + LOG_TABLE_NAME + " set IS_BACKUP_=1 where " +
				"LOGIN_TIME_>=:beginTime and LOGIN_TIME_<=:endTime and IS_BACKUP_=0");
		query.setTimestamp("beginTime", beginTime);
		query.setTimestamp("endTime", endTime);
		query.executeUpdate();
	}
	
	@Override
	public void delLastYearLogs() {
		//重置时间到当年01月01日 01时01分01秒
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, 1);
		now.set(Calendar.AM_PM, Calendar.AM);
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);	
		now.set(Calendar.MILLISECOND, 0);

		Query query = getSession().createQuery("delete from LoginLog as l where l.loginTime<:time and l.isBackUp=1");
		query.setTimestamp("time", new Timestamp(now.getTimeInMillis()));
		query.executeUpdate();
	}
	
	@Override
	public void startUpBackUp() {
		//获取日志表中未备份的最晚日志时间			
		Query query = getSession().createQuery("select max(l.loginTime) from LoginLog as l where l.isBackUp=0 ");
		Timestamp opTime = (Timestamp)query.uniqueResult();
		while(opTime != null) {
			//备份当天内所有的记录
			Calendar c = Calendar.getInstance();
			c.setTime(opTime);
			backUpLog(c);
			//继续查找尚未备份的最晚日志时间
			opTime = (Timestamp)query.uniqueResult();
		}
		//删除现在时间一年前所有已备份过的数据
		delLastYearLogs();			
	}
}