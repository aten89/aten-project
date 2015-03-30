/**
 * 
 */
package org.eapp.blo.imp;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.ISysMsgBiz;
import org.eapp.dao.ISysMsgDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.SysMsgQueryParameters;
import org.eapp.hbean.SysMsg;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 业务逻辑层
 * @version 
 */
public class SysMsgBiz implements ISysMsgBiz {
	
	private ISysMsgDAO sysMsgDAO;
	private IUserAccountDAO userAccountDAO;

	public void setSysMsgDAO(ISysMsgDAO sysMsgDAO) {
		this.sysMsgDAO = sysMsgDAO;
	}
	
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	public SysMsg addSysMsg(String fromSystemID, String msgSender, String toAccountID, String msgContent) {
		if (StringUtils.isBlank(toAccountID)) {
			throw new IllegalArgumentException();
		}
		UserAccount user = userAccountDAO.findByAccountID(toAccountID);
		if (user == null) {
			throw new IllegalArgumentException("接收用户帐号不存在：" + toAccountID);
		}
		SysMsg msg = new SysMsg();
		msg.setFromSystemID(fromSystemID);
		msg.setMsgSender(msgSender);
		msg.setToAccountID(toAccountID);
		msg.setMsgContent(msgContent);
		msg.setSendTime(new Timestamp(System.currentTimeMillis()));
		msg.setViewFlag(false);
		sysMsgDAO.save(msg);
		return msg;
	}

	@Override
	public SysMsg getAction(String id) {
		return sysMsgDAO.findById(id);
	}

	@Override
	public SysMsg txGetLastNoView(String toAccountID) {
		SysMsg msg = sysMsgDAO.findLastNoView(toAccountID);
		if (msg != null) {
			msg.setViewFlag(true);
			sysMsgDAO.update(msg);
		}
		return msg;
	}
	
	@Override
	public void deleteSysMsgs(String[] msgIDs) {
		if (msgIDs == null || msgIDs.length < 1) {
			return;
		}
		for (String msgID : msgIDs) {
			if (StringUtils.isBlank(msgID)) {
				continue;
			}
			SysMsg msg = sysMsgDAO.findById(msgID);
			if (msg != null) {
				sysMsgDAO.delete(msg);
			}
		}
	}
	
	@Override
	public void modifyViewFlag(String msgID) {
		if (StringUtils.isBlank(msgID)) {
			return;
		}
		SysMsg msg = sysMsgDAO.findById(msgID);
		msg.setViewFlag(true);
		sysMsgDAO.update(msg);
	}
	
	@Override
	public ListPage<SysMsg> querySysMsgs(SysMsgQueryParameters aqp) {
		return sysMsgDAO.querySysMsgs(aqp);
	}

	
}
