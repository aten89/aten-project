/**
 * 
 */
package org.eapp.dao;

import org.eapp.dao.param.LogQueryParameters;
import org.eapp.hbean.LoginLog;
import org.eapp.util.hibernate.ListPage;


/**
 * @author zsy
 * @version 
 */
public interface ILoginLogDAO extends IBaseHibernateDAO {

	
	/**
	 * 根据年份创建一张日志备份表，表结构与ACTIONLOG日志表一致，若当年的表已存在则忽略
	 * 如创建表：ACTIONLOG_2008
	 * @param year 为4位数的年份长度，如：2008
	 */
	public void createLogTable(int year);
	
	/**
	 * 根据年份判断该年的系统日志表是否存在
	 * @param year 年份
	 * @return
	 */
	public boolean existTable(int year);
	
	/**
	 * 根据查询条件查询日志
	 * 查询日志，使用Hibernate的本地SQL查询，并通过查询时间判定从哪张日志表里查询
	 * 查询时间不能跨年度
	 * @param qp 查询条件
	 * @return ListPage -> dataList<ActionLog> 对象
	 */
	public ListPage<LoginLog> queryLog(LogQueryParameters qp);
	
	/**
	 * 查找在线日志（即未登出时间为空的）
	 * @param qp
	 * @return
	 */
	public ListPage<LoginLog> queryOnLineLog(LogQueryParameters qp);
	
	/**
	 * 统计所有在线总数
	 * @return
	 */
	public int findTotalOnlineCount();
	
	/**
	 * 根据年份从日志备份表里通过ID查找实例，若年份与当前一样则从当前日志表查找
	 * 通过日志ID的前四位取得年份
	 * @param logID 日志ID
	 * @return
	 */
	public LoginLog findByID(String logID);
	
	/**
	 * 通过SessionID查找
	 * @param sessionID
	 * @return
	 */
	public LoginLog findBySessionID(String sessionID);
	
	/**
	 * 更新所有登出时间
	 */
	public void updateAllLogoutTime();
	
	/**
	 * 备份当前时间前一天从0（包含）到24点的日志
	 */
	public void backUpLastDayLog();
	
	/**
	 * 删除当前日志表中已备份的去年日志
	 */
	public void delLastYearLogs();
	
	/**
	 * 系统启动时调用此方法，
	 * 备份所有未备份的数据到相应年份的备份表中
	 */
	public void startUpBackUp();
}
