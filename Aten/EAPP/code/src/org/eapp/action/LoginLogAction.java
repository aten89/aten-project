package org.eapp.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.ILoginLogBiz;
import org.eapp.dao.param.LogQueryParameters;
import org.eapp.hbean.LoginLog;
import org.eapp.util.hibernate.ListPage;

import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * 处理系统日志模块的请求
 * @author zsy
 * @version
 */
public class LoginLogAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(LoginLogAction.class);

	private ILoginLogBiz loginLogBiz;

	private int pageNo;
	private int pageSize;
	private String accountID;
	private String accountName;
	private String beginTime;
	private String endTime;
	
	private ListPage<LoginLog> loginLogPage;
	private int totalOnlineCount;
	
	public ListPage<LoginLog> getLoginLogPage() {
		return loginLogPage;
	}

	public int getTotalOnlineCount() {
		return totalOnlineCount;
	}

	public void setLoginLogBiz(ILoginLogBiz loginLogBiz) {
		this.loginLogBiz = loginLogBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 页面初始化
	 */
	public String initQueryPage() {
		return success();
	}

	public String queryLog() {
		//查询时间不能为空
		if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime) || beginTime.compareTo(endTime) > 0) {
			return error("输入时间错误");
		}
		
		//判断是否跨年度查询
		Timestamp _beginTime = Timestamp.valueOf(DataFormatUtil.formatTime(beginTime));
		Timestamp _endTime = Timestamp.valueOf(DataFormatUtil.formatTime(endTime));
		Calendar c = Calendar.getInstance();
		c.setTime(_beginTime);
		int bYear = c.get(Calendar.YEAR);
		c.setTime(_endTime);
		int eYear = c.get(Calendar.YEAR);
		if (bYear != eYear) {
			return error("不能跨年度查询");
		}
		c.set(Calendar.HOUR_OF_DAY, 24);
		c.add(Calendar.MILLISECOND, -1);//12月31日时小时设为24就变为跨年了，所以要毫秒-1
		_endTime = new Timestamp(c.getTimeInMillis());
		LogQueryParameters logQP = new LogQueryParameters();
		logQP.setPageNo(pageNo);
		logQP.setPageSize(pageSize);

		if (StringUtils.isNotBlank(accountID)) {
			logQP.setAccountID(accountID);
		}
		if (StringUtils.isNotBlank(accountName)) {
			logQP.setAccountName(accountName);
		}
		logQP.setBeginTime(_beginTime);
		logQP.setEndTime(_endTime);
		try {
			loginLogPage = loginLogBiz.queryLog(logQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 页面初始化
	 */
	public String initQueryOnlinePage() {
		return success();
	}
	
	public String queryOnlineLog() {
		//查询时间不能为空
		LogQueryParameters logQP = new LogQueryParameters();
		logQP.setPageNo(pageNo);
		logQP.setPageSize(pageSize);

		if (StringUtils.isNotBlank(accountID)) {
			logQP.setAccountID(accountID);
		}
		if (StringUtils.isNotBlank(accountName)) {
			logQP.setAccountName(accountName);
		}
		try {
			loginLogPage = loginLogBiz.queryOnlineLog(logQP);
			totalOnlineCount = loginLogBiz.getTotalOnlineCount();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	public String exportLog() throws ServletException, IOException {
		//查询时间不能为空
		if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime) || beginTime.compareTo(endTime) > 0) {
			return error("输入时间错误");
		}
		
		//判断是否跨年度查询
		Timestamp _beginTime = Timestamp.valueOf(DataFormatUtil.formatTime(beginTime));
		Timestamp _endTime = Timestamp.valueOf(DataFormatUtil.formatTime(endTime));
		Calendar c = Calendar.getInstance();
		c.setTime(_beginTime);
		int bYear = c.get(Calendar.YEAR);
		c.setTime(_endTime);
		int eYear = c.get(Calendar.YEAR);
		if (bYear != eYear) {
			return error("不能跨年度查询");
		}
		c.set(Calendar.HOUR_OF_DAY, 24);
		_endTime = new Timestamp(c.getTimeInMillis());
		LogQueryParameters logQP = new LogQueryParameters();
		logQP.setPageNo(pageNo);
		logQP.setPageSize(pageSize);
		if (StringUtils.isNotBlank(accountName)) {
			logQP.setAccountName(accountName);
		}
		logQP.setBeginTime(_beginTime);
		logQP.setEndTime(_endTime);
		String fileName = getSession().getId()
				+ System.currentTimeMillis() / 10000 + ".txt";
		try {
			loginLogBiz.csExportLog(logQP, FileDispatcher.getTempDir(), fileName);
			return success(FileDispatcher.getTempAbsDir() + fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
