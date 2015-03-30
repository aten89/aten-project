package org.eapp.oa.info.ctrl;

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
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.info.dto.InfoFormList;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 
 * @author zsy
 * @version Mar 23, 2009
 */
public class InformationDealCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private IInfoFormBiz infoFormBiz;
	private ITaskBiz taskBiz;
	
	private TaskDealCtrl taskDealCtrl;
	/**
	 * Constructor of the object.
	 */
	public InformationDealCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		infoFormBiz =  (IInfoFormBiz) SpringHelper.getBean("infoFormBiz");
		taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
		
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
			//查询待处理的信息
			queryDealingInfo(request, response);
		} else if (SysConstants.DISPOSE.equalsIgnoreCase(action)) {
			//转到任务绑定的URL,并认领
			turnToViewPage(request, response);
		} else if ("view_approve".equalsIgnoreCase(action)) {
			//任务处理页面——审批
			initInfoApprove(request, response);
		} else if ("deal_approve".equalsIgnoreCase(action)) {
			//处理审批单任务
			dealApprove(request, response);
			return;
		} else if ("rejected_approve".equalsIgnoreCase(action)) {
			//驳回审批单任务
			rejectedApprove(request, response);
			return;
//		} else if ("view_modify".equalsIgnoreCase(action)) {
//			//任务处理页面——填单人修改
//			initInfoModify(request, response);
//		} else if ("deal_modify".equalsIgnoreCase(action)) {
//			//申请人处理修改报销单任务
//			dealModifyInfo(request, response);
//			return;
		}
	}
	
	private void queryDealingInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<InfoForm> info = infoFormBiz.getDealingInfoFrom(user.getAccountID());
		XMLResponse.outputXML(response,new InfoFormList(info).toDocument());
	}
	
	private void turnToViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.turnToViewPage(request, response);
	}
	
	private void initInfoApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			String id = HttpRequestHelper.getParameter(request, "id");//报销单号
			String flag = HttpRequestHelper.getParameter(request, "type");//报销单号
			String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID,从上一个地址转过来的参数
			if (id == null || taskInstanceId == null) {
				throw new OaException("参数不能为空");
			}
			InfoForm infoForm = infoFormBiz.getInfoFormById(id);
			if (infoForm == null) {
				throw new OaException("参数不能为空");
			}
			request.setAttribute("infoForm", infoForm);
			request.setAttribute("groups", user.getGroups());
//			request.setAttribute("infoClasses", SysCodeDictLoader.getInstance().getSubInfoClass(infoForm.getInformation().getInfoLayout()));
//			Map<String, DataDictionaryDTO> dict = SysCodeDictLoader.getInstance().getInfoSubjectColor();
//			if (dict != null) {
//				request.setAttribute("subjectColor", new TreeSet<DataDictionaryDTO>(dict.values()));
//			}
			List<Task> tasks = infoFormBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
			request.setAttribute("transitions", transitions);
			String pageUrl = null;
			if ("m".equals(flag)) {
				pageUrl = "/page/info/approval/tview_modify.jsp";
			} else if ("p".equals(flag)) {
				pageUrl = "/page/info/approval/tview_publish.jsp";
			} else {
				pageUrl = "/page/info/approval/tview_dispose.jsp";
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
	
	private void dealApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.dealApprove(request, response);
	}
	
	private void rejectedApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
		String infoFormId = HttpRequestHelper.getParameter(request, "ifid");
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
			infoFormBiz.txDealApproveTask(taskInstanceId, comment, transitionName, infoFormId);
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败" );
		}
	}
	
}
