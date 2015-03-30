package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;


/**
 * 与“处理任务”相关的事务
 * 以前是散落在各个模块的Servlet中，但具有共性，所以把它提取出来，作为一个公用类
 * @author richfans
 * @version
 */
public class TaskDealCtrl extends HttpServlet {

	private static final long serialVersionUID = 4094373270296215788L;

	private ITaskBiz taskBiz;
//	private IPeriodTaskBiz periodTaskBiz;
	/**
	 * Constructor of the object.
	 */
	public TaskDealCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
//		periodTaskBiz = (IPeriodTaskBiz) SpringHelper.getSpringContext().getBean("periodTaskBiz");
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
		String act = HttpRequestHelper.getParameter(request, "act");
		if ("deal_task".equals(act)) {
			dealApprove(request, response);
			return;
		}

	}
	
	public void turnToViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
//		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		String taskId = HttpRequestHelper.getParameter(request, "taskid");//任务ID
//		String tiid = HttpRequestHelper.getParameter(request, "tiid");//任务实例ID
//		String formType = HttpRequestHelper.getParameter(request, "formType");//任务ID
		if (taskId == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "任务不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String viewUrl;
		try {
//			Task task = taskBiz.findById(taskId);
//			if(task !=null){
//				if(!task.getViewFlag()){
//					PeriodTask pt = periodTaskBiz.findById(task.getId());
//					if(pt!=null){
//						String message = "此文件被要求在"+sdf.format(pt.getExecutionTime())+"之前完成审批，如逾期未审批，则自动按照<同意>处理。请在"+sdf.format(pt.getExecutionTime())+"之前及时完成审批工作，谢谢！";
//						request.setAttribute("message", message);
//					}
//				}
//			}
			List<String> userRoles = new ArrayList<String>();
			List<Name> roles = user.getRoles();
			if (roles != null && !roles.isEmpty()) {
				for (Name role : roles) {
					userRoles.add(role.getName());
				}
			}
			
			viewUrl = taskBiz.txStartTask(taskId, user.getAccountID(), userRoles);
//            request.setAttribute("task", task);
			request.getRequestDispatcher(viewUrl).forward(request, response);
		} catch (OaException e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
		
	}
	
	public void dealApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
		if (taskInstanceId == null || transitionName == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		if (comment != null) {
			comment = transitionName + "，" + comment;
		} else {
			comment = transitionName;
		}
		try { 
			taskBiz.txDealApproveTask(taskInstanceId, comment, transitionName);
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败" );
		}
		
	}
}
