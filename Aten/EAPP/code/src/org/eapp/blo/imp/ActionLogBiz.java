/**
 * 
 */
package org.eapp.blo.imp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.eapp.blo.IActionLogBiz;
import org.eapp.dao.IActionDAO;
import org.eapp.dao.IActionLogDAO;
import org.eapp.dao.IActorAccountDAO;
import org.eapp.dao.IModuleDAO;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.LogQueryParameters;
import org.eapp.dto.LogSaveBean;
import org.eapp.hbean.Action;
import org.eapp.hbean.ActionLog;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.Module;
import org.eapp.hbean.SubSystem;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 系统日志相关的业务方法
 * 
 * @author zsy
 * @version 
 */
public class ActionLogBiz implements IActionLogBiz {
	
	private IActionLogDAO actionLogDAO;
	private ISubSystemDAO subSystemDAO;
	private IModuleDAO moduleDAO;
	private IActionDAO actionDAO;
	private IUserAccountDAO userAccountDAO;
	private IActorAccountDAO actorAccountDAO;
	
	public void setActionLogDAO(IActionLogDAO actionLogDAO) {
		this.actionLogDAO = actionLogDAO;		
	}
	
	public void setSubSystemDAO(ISubSystemDAO subSystemDAO) {
		this.subSystemDAO = subSystemDAO;
	}

	public void setModuleDAO(IModuleDAO moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	public void setActionDAO(IActionDAO actionDAO) {
		this.actionDAO = actionDAO;
	}

	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public void setActorAccountDAO(IActorAccountDAO actorAccountDAO) {
		this.actorAccountDAO = actorAccountDAO;
	}

	public void addLogTable(int year) {
		if (actionLogDAO.existTable(year)) {
			return;
		}
		actionLogDAO.createLogTable(year);
	}

	public ListPage<ActionLog> queryLog(LogQueryParameters qp) {
		return actionLogDAO.queryLog(qp);
	}
	
	public ActionLog getLogByID(String logID) {
		return actionLogDAO.getByID(logID);
	}

	public void csExportLog(LogQueryParameters qp, String dirName, String fileName) throws IOException {
		if (qp == null || dirName == null || fileName == null) {
			throw new IllegalArgumentException();
		}
		qp.setPageSize(10000);
		ListPage<ActionLog> page = queryLog(qp);
		List<ActionLog> data = page.getDataList();
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
	private void writeLog(List<ActionLog> data, PrintWriter out) {
		if(out == null){
			throw new IllegalArgumentException("PrintWriter out参数不能为Null");
		}
		if (data == null || data.size() < 0) {
			return;
		}
		for (ActionLog log : data) {
			out.println(log.getOperateTime() + "\t" + log.getIpAddress() + "\t" + log.getSystemID() 
					+ "\t" + log.getSystemName() + "\t" + log.getModuleKey() + "\t" + log.getModuleName() 
					+ "\t" + log.getActionKey() + "\t" + log.getActionName() + "\t" + log.getAccountID() 
					+ "\t" + log.getAccountName() + "\t" + log.getObjectID() + "\t" + log.getObject() + "\t" + log.getResultStatus());
		}
	}
	
	public void txBackUpLastDayLog() {
		actionLogDAO.backUpLastDayLog();
	}
	
	public void deleteLastYearLogs() {
		actionLogDAO.delLastYearLogs();
	}
	
	public void txStartUpBackUp() {
		actionLogDAO.startUpBackUp();
	}
	
	public void addLog(LogSaveBean log, boolean isServiceLog) {
		if (log == null) {
			throw new IllegalArgumentException();
		}
		ActionLog iLog = new ActionLog();
		if (log.getSystemID() != null) {
			iLog.setSystemID(log.getSystemID());
			SubSystem sys = subSystemDAO.findById(log.getSystemID());
			iLog.setSystemName(sys != null ? sys.getName() : null);
		}
		if (log.getModuleKey() != null) {
			iLog.setModuleKey(log.getModuleKey());
			Module m = moduleDAO.findModuleByModuleKey(log.getSystemID(), log.getModuleKey());
			iLog.setModuleName(m != null ? m.getName() : null);
		}
		if (log.getActionKey() != null) {
			iLog.setActionKey(log.getActionKey());
			Action a = actionDAO.findByActionKey(log.getActionKey());
			iLog.setActionName(a != null ? a.getName() : null);
		}
		if (log.getAccountID() != null) {
			iLog.setAccountID(log.getAccountID());
			if (isServiceLog) {//查找接口账号
				ActorAccount aa = actorAccountDAO.findByAccountID(log.getAccountID());
				iLog.setAccountName(aa != null ? aa.getDisplayName() : null);
			} else {//查找用户账号
				UserAccount ua = userAccountDAO.findByAccountID(log.getAccountID());
				iLog.setAccountName(ua != null ? ua.getDisplayName() : null);
			}
		}
		iLog.setObjectID(log.getObjectID());
		if (log.getObject() != null) {
			//日志内容超长截取（数据库字段长度为4000）
			byte[] ob = log.getObject().getBytes();
			if (ob.length > 3500){
				iLog.setObject(new String(Arrays.copyOf(ob, 3500)));
			} else {
				iLog.setObject(log.getObject());
			}
		}
		iLog.setIpAddress(log.getIpAddress());
		iLog.setResultStatus(log.getResultStatus());
		iLog.setOperateTime(new Timestamp(System.currentTimeMillis()));
		iLog.setIsServiceLog(isServiceLog);
		actionLogDAO.save(iLog);
	}

}
