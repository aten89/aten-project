package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.ctrl.TaskDealCtrl;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dto.StaffFlowApplyList;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;


/**
 * 处理请假单的待办
 * @version
 */
public class StaffFlowDealCtrl extends HttpServlet {

	
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IStaffFlowApplyBiz staffFlowApplyBiz;
	private ITaskBiz taskBiz;
	private TaskDealCtrl taskDealCtrl;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowDealCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
		taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
		taskDealCtrl = new TaskDealCtrl();
		taskDealCtrl.init();
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
			//查询待处理
			queryDealingHolidayApply(request, response);
			return;
		} else if (SysConstants.DISPOSE.equalsIgnoreCase(action)) {
			//转到任务绑定的URL,并认领
			turnViewPage(request, response);
		} else if ("view_approve".equalsIgnoreCase(action)) {
			//任务处理页面——审批
			initHolidayApprove(request, response);
		} else if ("deal_approve".equalsIgnoreCase(action)) {
			//处理审批任务
			dealTaskApprove(request, response);
			return;
		} else if ("deal_modify".equalsIgnoreCase(action)) {
			//申请人处理修改
			dealModifyHolidayApply(request, response);
			return;
		} else if ("deal_addacc".equalsIgnoreCase(action)) {
			//处理确认（添加系统账号）
			dealCfmHolidayApply(request, response);
			return;
		} 
	}
	
	private void queryDealingHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<StaffFlowApply> staffs = staffFlowApplyBiz.getDealingStaffFlowApply(user.getAccountID());
		XMLResponse.outputXML(response, new StaffFlowApplyList(staffs).toDocument());
	}
	
	
	public void turnViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.turnToViewPage(request, response);		
	}
	
	private void initHolidayApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		try {
			
			String id = HttpRequestHelper.getParameter(request, "id");//报销单号
			String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID,从上一个地址转过来的参数
			String flag = HttpRequestHelper.getParameter(request, "type");
			if (id == null || taskInstanceId == null) {
				throw new OaException("参数不能为空");
			}
			StaffFlowApply staffFlowApply = staffFlowApplyBiz.getStaffFlowApplyById(id);
			if (staffFlowApply == null) {
				throw new OaException("申请单不存在！");
			}
			boolean isEntry = staffFlowApply.getApplyType() == StaffFlowApply.TYPE_ENTRY;
			request.setAttribute("staffFlowApply", staffFlowApply);
			List<Task> tasks = staffFlowApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
			request.setAttribute("transitions", transitions);
			request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
			String pageUrl = null;
			if ("m".equals(flag)) {//发起人修改
				if (isEntry) {
					pageUrl = "/page/hr/staffflow/tview_modify_en.jsp";
				} else {
					if (staffFlowApply.getApplicant().equals(staffFlowApply.getUserAccountID())) {
						//本人离职
						pageUrl = "/page/hr/staffflow/tview_modify_myre.jsp";
					} else {
						pageUrl = "/page/hr/staffflow/tview_modify_re.jsp";
					}
				}
			} else if ("mm".equals(flag)) {//个人离职人力专员修改
				if (isEntry) {
					pageUrl = "/page/hr/staffflow/tview_modify_en.jsp";
				} else {
					pageUrl = "/page/hr/staffflow/tview_modify_re.jsp";
				}
			} else if ("a".equals(flag)) {
				if (isEntry) {
					pageUrl = "/page/hr/staffflow/tview_addacc.jsp";
				} else {
					pageUrl = "/page/hr/staffflow/tview_delacc.jsp";
				}
			} else {
				if (isEntry) {
					pageUrl = "/page/hr/staffflow/tview_dispose_en.jsp";
				} else {
					pageUrl = "/page/hr/staffflow/tview_dispose_re.jsp";
				}
			}
			
			request.getRequestDispatcher(pageUrl).forward(request, response);
			
		} catch (OaException e1) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e1.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}

	
	public void dealTaskApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.dealApprove(request, response);
	}
	
	private void dealModifyHolidayApply(HttpServletRequest request,
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
		String json = HttpRequestHelper.getParameter(request, "json");
		StaffFlowApply reDraft = JSON.parseObject(json, StaffFlowApply.class);
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "申请单不存在！");
			return;
		}
		try {
			staffFlowApplyBiz.txDealApproveTask(taskInstanceId, comment, transitionName, reDraft);
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		}catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败");
			return;
		}
	}
	
	private void dealCfmHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
		
		String staffId = HttpRequestHelper.getParameter(request, "id");
		String userAccountID = HttpRequestHelper.getParameter(request, "userAccountID");
		if (taskInstanceId == null || transitionName == null || staffId == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		if (comment != null) {
			comment = transitionName + "，" + comment;
		} else {
			comment = transitionName;
		}
		
		try {
			staffFlowApplyBiz.txDealApproveTask(taskInstanceId, comment, transitionName, staffId, userAccountID);
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		}catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败");
			return;
		}
	}
}
