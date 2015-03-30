package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.hessian.GroupService;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.flow.blo.IFlowNotifyBiz;
import org.eapp.oa.flow.dto.FlowNotifyPage;
import org.eapp.oa.flow.dto.FlowNotifyQueryParameters;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

import com.alibaba.fastjson.JSON;


public class FlowNotifyCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private IFlowNotifyBiz flowNotifyBiz;
	private GroupService groupService;
	
	
	/**
	 * Constructor of the object.
	 */
	public FlowNotifyCtrl() {
		super();
	}

	/** 
	 * 
	 */
	public void init() throws ServletException {
		flowNotifyBiz = (IFlowNotifyBiz) SpringHelper.getBean("flowNotifyBiz");
		groupService = new GroupService();
	}

	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询
			queryFlowNotify(request, response);
			return;
		} else if("initadd".equalsIgnoreCase(action)){
			//初始化新增
			initAddFlowNotify(request, response);
			return;
		} else if(SysConstants.ADD.equalsIgnoreCase(action)){
			//新增
			addFlowNotify(request, response);
			return;
		} else if(SysConstants.VIEW.equalsIgnoreCase(action)){
			//查看
			viewFlowNotify(request, response);
			return;
		}
	}
	
	private void queryFlowNotify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
	
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 20);
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String creator = HttpRequestHelper.getParameter(request, "creator");
		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		int flowStatus = HttpRequestHelper.getIntParameter(request, "flowStatus", -2);
		String beginNotifyTimeStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "beginNotifyTimeStr"));
		String endNotifyTimeStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endNotifyTimeStr"));
		
		FlowNotifyQueryParameters qp = new FlowNotifyQueryParameters(); 
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);
		qp.setSubject(subject);
		qp.setCreator(creator);
		qp.setFlowClass(flowClass);
		if (flowStatus != -2) {
			qp.setFlowStatus(flowStatus);
		}
		if(beginNotifyTimeStr != null){
			qp.setBeginNotifyTime(Timestamp.valueOf(beginNotifyTimeStr));
		}
		
		if(endNotifyTimeStr != null){//加一天
			Timestamp t = Timestamp.valueOf(endNotifyTimeStr);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndNotifyTime(t);
		}
		qp.addOrder("notifyTime", false);
		ListPage<FlowNotify> list = flowNotifyBiz.queryUserNotify(qp, user.getAccountID());
		XMLResponse.outputXML(response, new FlowNotifyPage(list).toDocument());
	
	}
	
	private void initAddFlowNotify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		FlowNotify flowNotify = new FlowNotify();
		flowNotify.setCreator(user.getAccountID());
		
		// 初始化投资顾问部门
        List<Name> groups = user.getDepts();
        List<String> deptNameList = new ArrayList<String>();
        if (groups != null && !groups.isEmpty()) {
        	try {
	        	Name deptName = groups.get(0);
	        	deptNameList.add(deptName.getName());
	        	GroupInfo group = groupService.getGroupByID(deptName.getId());
	        	getParentDept(group, deptNameList );
	        	
	            String fullDeptName = "";
	            for (String dept : deptNameList) {
	            	fullDeptName += dept + "/";
				}
	            if (StringUtils.isNotEmpty(fullDeptName)) {
	            	flowNotify.setGroupFullName(fullDeptName.substring(0, fullDeptName.length() - 1));
	            }
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        
		request.setAttribute("flowNotify", flowNotify);
		request.getRequestDispatcher("/page/system/flownotify/add_notify.jsp").forward(request, response);
	}
	
	private void getParentDept(GroupInfo g, List<String> deptNameList) throws Exception{
		if (g != null && StringUtils.isNotEmpty(g.getParentGroupID())) {
			GroupInfo group = groupService.getGroupByID(g.getParentGroupID());
			deptNameList.add(group.getGroupName());
			getParentDept(group, deptNameList);
		}
	}
	
	private void addFlowNotify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String json = HttpRequestHelper.getParameter(request, "json");
		FlowNotify flowNotify = JSON.parseObject(json, FlowNotify.class);
		if (flowNotify == null) {
			XMLResponse.outputStandardResponse(response, 0, "无知会人信息！");
			return;
		}	
		
//		String notifyUsers = HttpRequestHelper.getParameter(request, "notifyUsers");
//		String subject = HttpRequestHelper.getParameter(request, "subject"); 
//		int notifyType = HttpRequestHelper.getIntParameter(request, "notifyType", FlowNotify.NOTIFYTYPE_NOW);
//		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
//		String refFormID = HttpRequestHelper.getParameter(request, "refFormID");
//		String viewDetailURL = HttpRequestHelper.getParameter(request, "viewDetailURL");
//		if (notifyUsers == null || flowClass == null || refFormID == null) {
//			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
//			return;
//		}
		try {
			flowNotifyBiz.addFlowNotifys(flowNotify);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}
	}
	
	private void viewFlowNotify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		FlowNotify flowNotify = flowNotifyBiz.txSetViewFlag(id);
		request.getRequestDispatcher(flowNotify.getViewDetailURL()).forward(request, response);
	}
}
