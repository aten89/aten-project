/**
 * 
 */
package org.eapp.blo.imp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.eapp.blo.ILoginLogBiz;
import org.eapp.dao.ILoginLogDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.LogQueryParameters;
import org.eapp.hbean.LoginLog;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 系统日志相关的业务方法
 * 
 * @author zsy
 * @version 
 */
public class LoginLogBiz implements ILoginLogBiz {
	
	private ILoginLogDAO loginLogDAO;
	private IUserAccountDAO userAccountDAO;
	
	public void setLoginLogDAO(ILoginLogDAO loginLogDAO) {
		this.loginLogDAO = loginLogDAO;		
	}
	
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public void addLogTable(int year) {
		if (loginLogDAO.existTable(year)) {
			return;
		}
		loginLogDAO.createLogTable(year);
	}

	public ListPage<LoginLog> queryLog(LogQueryParameters qp) {
		return loginLogDAO.queryLog(qp);
	}
	
	public ListPage<LoginLog> queryOnlineLog(LogQueryParameters qp) {
		return loginLogDAO.queryOnLineLog(qp);
	}
	
	public int getTotalOnlineCount() {
		return loginLogDAO.findTotalOnlineCount();
	}
	
	public LoginLog getLogByID(String logID) {
		return loginLogDAO.findByID(logID);
	}

	public void csExportLog(LogQueryParameters qp, String dirName, String fileName) throws IOException {
		if (qp == null || dirName == null || fileName == null) {
			throw new IllegalArgumentException();
		}
		qp.setPageSize(10000);
		ListPage<LoginLog> page = queryLog(qp);
		List<LoginLog> data = page.getDataList();
		PrintWriter out = null;
		try {
			File dir = new File(dirName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			out = new PrintWriter(new File(dir, fileName), "gbk");
			writeLog(data, out);
			for (long pageNo = 2; pageNo <= page.getTotalPageCount(); pageNo++) {
				qp.setPageNo((int)pageNo);
				page = queryLog(qp);
				writeLog(data, out);
			}
		} finally {
			if(data != null){
				data.clear();
				data = null;
			}
			out.close();
			out = null;
		}
	}
	
	/**
	 * 导出系统日志
	 * @param data 系统日志列表
	 * @param out 输也流
	 */
	private void writeLog(List<LoginLog> data, PrintWriter out) {
		if(out == null){
			throw new IllegalArgumentException("PrintWriter out参数不能为Null");
		}
		if (data == null || data.size() < 0) {
			return;
		}
		for (LoginLog log : data) {
			out.println(log.getLoginTime() + "\t" + log.getIpAddress() + "\t" + log.getAccountID() 
					+ "\t" + log.getAccountName() + "\t" + log.getIsSuccess() + "\t" + log.getLoginInfo());
		}
	}
	
	public void txBackUpLastDayLog() {
		loginLogDAO.backUpLastDayLog();
	}
	
	public void deleteLastYearLogs() {
		loginLogDAO.delLastYearLogs();
	}
	
	public void txStartUpBackUp() {
		loginLogDAO.startUpBackUp();
	}
	
	public void addLog(String sessionID, String accountID, String accountName, String ipAddress, 
			boolean isSuccess, String loginInfo) {
		if (isSuccess) {
			UserAccount user = userAccountDAO.findByAccountID(accountID);
			//登录成功后更新用户信息
			user.setLastLoginTime(new Date());
			user.addLoginCount();
			userAccountDAO.update(user);
		}
		
		//记录日志
		LoginLog lLog = new LoginLog();
		lLog.setSessionID(sessionID);
		lLog.setAccountID(accountID);
		lLog.setAccountName(accountName);
		lLog.setIpAddress(ipAddress);
		lLog.setIsSuccess(isSuccess);
		lLog.setLoginInfo(loginInfo);
		lLog.setLoginTime(new Timestamp(System.currentTimeMillis()));
		loginLogDAO.save(lLog);
	}
	
	public void txSetLogoutTime(String sessionID) {
		LoginLog lLog = loginLogDAO.findBySessionID(sessionID);
		lLog.setLogoutTime(new Timestamp(System.currentTimeMillis()));
		loginLogDAO.update(lLog);
	}
	
	public void txSetAllLogoutTime() {
		loginLogDAO.updateAllLogoutTime();
	}
}
