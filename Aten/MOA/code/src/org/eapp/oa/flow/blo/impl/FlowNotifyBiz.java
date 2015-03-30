package org.eapp.oa.flow.blo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.oa.flow.blo.IFlowNotifyBiz;
import org.eapp.oa.flow.dao.IFlowNotifyDAO;
import org.eapp.oa.flow.dto.FlowNotifyQueryParameters;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.util.hibernate.ListPage;


/**
 */
public class FlowNotifyBiz implements IFlowNotifyBiz {
	/**
     * 日志
     */
    private static Log log = LogFactory.getLog(FlowNotifyBiz.class);
	private IFlowNotifyDAO flowNotifyDAO;
	
	public void setFlowNotifyDAO(IFlowNotifyDAO flowNotifyDAO) {
		this.flowNotifyDAO = flowNotifyDAO;
	}

	public FlowNotify getFlowNotifyById(String id) {
		return flowNotifyDAO.findById(id);
	}
	
	public FlowNotify txSetViewFlag(String id) {
		FlowNotify notify = flowNotifyDAO.findById(id);
		notify.setStatus(FlowNotify.STATUS_READED);
		flowNotifyDAO.update(notify);
		return notify;
	}
	
	public ListPage<FlowNotify> queryUserNotify(FlowNotifyQueryParameters parms, String userAccountId) {
		return flowNotifyDAO.queryUserNotify(parms, userAccountId);
	}
	
	public void addFlowNotifys(FlowNotify flowNotify) {
		if (flowNotify == null) {
			return;
		}
		String notifyUserStr = flowNotify.getNotifyUser();
		if (StringUtils.isBlank(notifyUserStr)) {
			return;
		}
		String[] notifyUsers = notifyUserStr.split(",");
		Date now = new Date();
		for (String notifyUser : notifyUsers) {
			if (StringUtils.isBlank(notifyUser)) {
				continue;
			}
			FlowNotify fn = new FlowNotify();
			fn.setSubject(flowNotify.getSubject());
			fn.setNotifyUser(notifyUser);
			fn.setNotifyType(flowNotify.getNotifyType());
			fn.setCreator(flowNotify.getCreator());
			fn.setGroupFullName(flowNotify.getGroupFullName());
			fn.setCreateTime(now);
			if (FlowNotify.NOTIFYTYPE_NOW == flowNotify.getNotifyType()) {
				fn.setStatus(FlowNotify.STATUS_NOTIFY);
				fn.setNotifyTime(now);
			} else {
				fn.setStatus(FlowNotify.STATUS_CREATE);
			}
			fn.setFlowClass(flowNotify.getFlowClass());
			fn.setRefFormID(flowNotify.getRefFormID());
			fn.setViewDetailURL(flowNotify.getViewDetailURL());
			fn.setFlowStatus(FlowNotify.FLOW_STATUS_DEAL);
			flowNotifyDAO.save(fn);
			
			if (FlowNotify.NOTIFYTYPE_NOW == flowNotify.getNotifyType()) {
				//知会系统消息提示
				sendSysMsg(fn);
			}
		}
	}

	@Override
	public void txUpdateNotifyStatus(String refFormID, int flowStatus) {
		List<FlowNotify> fns = flowNotifyDAO.findEndNotify(refFormID);
		if (fns != null && !fns.isEmpty()) {
			for (FlowNotify fn : fns) {
				fn.setStatus(FlowNotify.STATUS_NOTIFY);
				fn.setNotifyTime(new Date());
				flowNotifyDAO.update(fn);
				//知会系统消息提示
				sendSysMsg(fn);
			}
		}
		flowNotifyDAO.updateNotifyStatus(refFormID, flowStatus);
		
	}
	
	/**
	 * 发送系统消息提示
	 * @param fn
	 */
	private void sendSysMsg(FlowNotify fn) {
		try {
			//知会系统消息提示
			UserAccountService userAccountService = new UserAccountService();
			userAccountService.sendSysMsg(SystemProperties.SYSTEM_ID, "系统", fn.getNotifyUser(), "知会信息：<span style='cursor:pointer;color:blue;' onclick=\"$.getMainFrame().addTab({id:'notify+' + new Date(),title:'知会信息',url:'/oa/m/flow_notify?act=view&id=" + fn.getId() + "'});\">" + fn.getSubject() + "</span>");
		} catch (Exception e) {
			log.error("流程知会通知失败" + e.getMessage());
		}
	}
}
