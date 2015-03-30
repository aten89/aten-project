package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.dto.TaskPage;
import org.eapp.oa.flow.dto.TaskQueryParameters;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.exe.TaskInstance;


public class FlowInstCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private ITaskBiz taskBiz;
	
	private TaskDealCtrl taskDealCtrl;
	
	/**
	 * Constructor of the object.
	 */
	public FlowInstCtrl() {
		
		super();
	}

	/** 
	 * 
	 */
	public void init() throws ServletException {
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
			//查询未完成的任务
			queryFlowInst(request, response);
			return;
		}else if(SysConstants.ASSIGN.equalsIgnoreCase(action)){
			//授权
			saveAssing(request, response);
			return;
		}else if(SysConstants.DISPOSE.equalsIgnoreCase(action)){
			//初始化
			initTaskView(request, response);
			return;
		}
	}
	
	private void queryFlowInst(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String formId = HttpRequestHelper.getParameter(request, "id");
		String desc = HttpRequestHelper.getParameter(request, "desc");
		String beginCreateTimeDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "beginCreateTimeDateStr"));
		String endCreateTimeDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endCreateTimeDateStr"));
		String transactor = HttpRequestHelper.getParameter(request, "transactor");
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 20);
		TaskQueryParameters qp = new TaskQueryParameters(); 
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);
		qp.setFormId(formId);	
		qp.setDesc(desc);
		if(beginCreateTimeDateStr != null){
			qp.setBeginCreateTime(Timestamp.valueOf(beginCreateTimeDateStr));
		}
		
		if(endCreateTimeDateStr != null){//加一天
			Timestamp t = Timestamp.valueOf(endCreateTimeDateStr);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndCreateTime(t);
		}
		qp.setTransactor(transactor);
		qp.setStates(new String[]{TaskInstance.PROCESS_STATE_CREATE,TaskInstance.PROCESS_STATE_START});
		qp.addOrder("createTime", false);
		ListPage<Task> list = taskBiz.getUnFinishTask(qp);
		XMLResponse.outputXML(response, new TaskPage(list).toDocument());
	
	}
	private void saveAssing(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String tiid = HttpRequestHelper.getParameter(request, "tiid"); 	
		String transactor = HttpRequestHelper.getParameter(request, "transactor"); 
		if (tiid == null || transactor == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			taskBiz.txTaskAssign(tiid, transactor);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}
	}
	private void initTaskView(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		taskDealCtrl.turnToViewPage(request, response);
	}
}
