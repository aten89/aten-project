package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.ctrl.TaskDealCtrl;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.reimburse.blo.IReimbursementBiz;
import org.eapp.oa.reimburse.dto.ReimbursementPage;
import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * 处理审批报销单的请求
 * @author zsy
 * @version
 */
public class ReimbursementDealCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IReimbursementBiz reimbursementBiz;
	private ITaskBiz taskBiz;
	private TaskDealCtrl taskDealCtrl;
	
	/**
	 * Constructor of the object.
	 */
	public ReimbursementDealCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean("reimbursementBiz");
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
			//查询待处理的报销单
			queryDealingReimbursement(request, response);
			return;
		} else if (SysConstants.DISPOSE.equalsIgnoreCase(action)) {
			//转到任务绑定的URL,并认领
			turnViewPage(request, response);
			
		} else if ("view_approve".equalsIgnoreCase(action)) {
			//任务处理页面——审批
			initReimbursementApprove(request, response);
		} else if ("deal_approve".equalsIgnoreCase(action)) {
			//处理审批报销单任务
			dealTaskApprove(request, response);
			return;
		} else if ("deal_modify".equalsIgnoreCase(action)) {
			//申请人处理修改报销单任务
			dealModifyReimbursement(request, response);
			return;
		}
	}
	
	private void queryDealingReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
		boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
		String id = HttpRequestHelper.getParameter(request, "id");
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String beginApplyDate = DataFormatUtil.formatTime( request.getParameter("beginApplyDate"));
		String endApplyDate = DataFormatUtil.formatTime( request.getParameter("endApplyDate"));
		//构造查询条件对象
		ReimbursementQueryParameters rqp = new ReimbursementQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		//增加对排序列的处理
		if (sortCol != null && !sortCol.trim().equals("")){
			rqp.addOrder(sortCol, ascend);
		}
		
		rqp.setID(id);
//		rqp.setBudgetId(budgetItemId);
		rqp.setApplicant(applicant);
		if(beginApplyDate != null){
			rqp.setBeginApplyDate(Timestamp.valueOf(beginApplyDate));
		}
		if(endApplyDate != null){//加一天
			Timestamp t = Timestamp.valueOf(endApplyDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			rqp.setEndApplyDate(t);
		}
		ListPage<Reimbursement> listPage = reimbursementBiz.getDealingReimbursement(rqp, user.getAccountID());
		if (listPage != null) {
			XMLResponse.outputXML(response, new ReimbursementPage(listPage).toDocument());
		}
	}
	
	public void turnViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		taskDealCtrl.turnToViewPage(request, response);		
	}
	
	private void initReimbursementApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String id = HttpRequestHelper.getParameter(request, "id");//报销单号
			String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID,从上一个地址转过来的参数
			String flag = HttpRequestHelper.getParameter(request, "type");
			if (id == null || taskInstanceId == null) {
				throw new OaException("参数不能为空");
			}
			Reimbursement rei = reimbursementBiz.getReimbursementById(id);
			if (rei == null) {
				throw new OaException("报销单不存在");
			}
			
			//这里对报销单里面的费用项目按照
			request.setAttribute("reimbusement", rei);
			
			List<Task> tasks = reimbursementBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
			request.setAttribute("transitions", transitions);
			request.setAttribute("reimJson", JSON.toJSONStringWithDateFormat(rei, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
			request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
			String pageUrl = null;
			if ("m".equals(flag)) {//发起人导修改
				pageUrl = "/page/cost/reimburse/tview_modify.jsp";
			}else {
				pageUrl = "/page/cost/reimburse/tview_dispose.jsp";
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
	
	
	private void dealModifyReimbursement(HttpServletRequest request,
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

		try {
			
			Reimbursement reim = JSON.parseObject(json, Reimbursement.class);
			if(reim == null){
				XMLResponse.outputStandardResponse(response, 0, "没找到报销单!");
				return;
			}
			reimbursementBiz.txDealApproveTask(taskInstanceId, comment, transitionName, reim);
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
			
		} catch(NumberFormatException e) {
			XMLResponse.outputStandardResponse(response, 0, "数字格式出错");
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败：" + e.getMessage());
			return;
		}
	}
}
