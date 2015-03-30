/**
 * 
 */
package org.eapp.blo;

import java.io.IOException;

import org.eapp.dao.param.LogQueryParameters;
import org.eapp.hbean.LoginLog;
import org.eapp.util.hibernate.ListPage;


/**
 * 定义系统日志相关的业务方法
 * @author zsy
 * @version 
 */
public interface ILoginLogBiz {
	
	/**
	 * 根据年份创建一张日志备份表，表结构与ACTIONLOG日志表一致，若当年的表已存在则忽略
	 * 如创建表：ACTIONLOG_2008
	 * @param year 为4位数的年份长度，如：2008
	 */
	public void addLogTable(int year);
	
	
	/**
	 * 根据查询条件查询日志
	 * @param qp 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<LoginLog> queryLog(LogQueryParameters qp);
	
	/**
	 * 查询在线的日志
	 * @param qp
	 * @return
	 */
	public ListPage<LoginLog> queryOnlineLog(LogQueryParameters qp);
	
	/**
	 * 统计所有在线总数
	 * @return
	 */
	public int getTotalOnlineCount();
	
	/**
	 * 从日志备份表里通过ID查找实例，若年份与当前一样则从当前日志表查找
	 * 
	 * @param logID 日志ID
	 * @return
	 */
	public LoginLog getLogByID(String logID);
	
	/**
	 * 根据查询条件导出日志到临时文件
	 * @param qp 查询条件
	 * @param filePath 临时文件夹绝对路径
	 */
	public void csExportLog(LogQueryParameters qp, String dirName, String fileName) throws IOException;
	
	/**
	 * 备份当前时间前一天从0（包含）到24点的日志
	 */
	public void txBackUpLastDayLog();
	
	/**
	 * 删除当前日志表中已备份的去年日志
	 */
	public void deleteLastYearLogs();
	
	/**
	 * 系统启动时调用此方法，
	 * 备份所有未备份的数据到相应年份的备份表中
	 */
	public void txStartUpBackUp();
	
	/**
	 * 添加系统日志
	 * @param log 日志参数DTO
	 */
	public void addLog(String sessionID, String accountID, String accountName, String ipAddress, 
			boolean isSuccess, String loginInfo);
	
	/**
	 * 注销时设置登出时间
	 * @param sessionID
	 */
	public void txSetLogoutTime(String sessionID);
	
	/**
	 * 系统启动时设置所有，上次关闭系统时未退出的记录
	 */
	public void txSetAllLogoutTime();
}
