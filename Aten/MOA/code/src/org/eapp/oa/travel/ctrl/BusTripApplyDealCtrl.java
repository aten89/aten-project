package org.eapp.oa.travel.ctrl;

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
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dto.BusTripApplyDealList;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BusTripApplyDealCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -552746956367371938L;
	
	private IBusTripApplyBiz busTripApplyBiz;
	private ITaskBiz taskBiz;
	private TaskDealCtrl taskDealCtrl;
	
	public BusTripApplyDealCtrl() {
		super();
	}

	public void init() throws ServletException {
		busTripApplyBiz = (IBusTripApplyBiz) SpringHelper.getSpringContext().getBean("busTripApplyBiz");
		taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
		taskDealCtrl = new TaskDealCtrl();
		taskDealCtrl.init();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询待处理的出差单
			queryDealingBusTripApply(request, response);
			return;
		} else if (SysConstants.DISPOSE.equalsIgnoreCase(action)) {
			//转到任务绑定的URL,并认领
			turnViewPage(request, response);
		} else if ("view_approve".equalsIgnoreCase(action)) {
			//任务处理页面——审批
			initBusTripApprove(request, response);
		} else if ("deal_approve".equalsIgnoreCase(action)) {
			//处理审批任务
			dealTaskApprove(request, response);
			return;
		}else if ("deal_modify".equalsIgnoreCase(action)) {
			//申请人处理修改出差单任务
			dealModifyBusTripApply(request, response);
			return;
		} 
	}
	private void queryDealingBusTripApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<BusTripApply> busTripApply = busTripApplyBiz.getDealingTripApply(user.getAccountID());
		XMLResponse.outputXML(response, new BusTripApplyDealList(busTripApply).toDocument());
	}	
	
	public void turnViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.turnToViewPage(request, response);		
	}
	
	private void initBusTripApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		try {
			String id = HttpRequestHelper.getParameter(request, "id");//出差单号
			String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID,从上一个地址转过来的参数
			if (id == null || taskInstanceId == null) {
				throw new OaException("参数不能为空");
			}
			BusTripApply busTripApply = busTripApplyBiz.getBusTripApplyById(id);
			if (busTripApply == null) {
				throw new OaException("出差申请单不存在！");
			}
			request.setAttribute("busTripApply", busTripApply);
			request.setAttribute("depts", user.getDepts());
			request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
			request.setAttribute("tripJson", JSON.toJSONStringWithDateFormat(busTripApply, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
			
			List<Task> tasks = busTripApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
			request.setAttribute("transitions", transitions);
			String flag = HttpRequestHelper.getParameter(request, "type");
			if("m".equals(flag)){
				//驳回修改
				request.getRequestDispatcher("/page/travel/approval/tview_modify.jsp").forward(request, response);
			}else{
				//一般审批
				request.getRequestDispatcher("/page/travel/approval/tview_dispose.jsp").forward(request, response);
			}	
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
	
	private void dealModifyBusTripApply(HttpServletRequest request,
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
		BusTripApply reDraft = JSON.parseObject(json, BusTripApply.class);
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写出差申请单！");
			return;
		}
		try {
			if (reDraft.getBusTripApplyDetail() == null || reDraft.getBusTripApplyDetail().size() < 1) {
				XMLResponse.outputStandardResponse(response, 0, "请添加出差明细!");
				return ;
			}
			
			busTripApplyBiz.txDealApproveTask(taskInstanceId, comment, transitionName, reDraft);
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
