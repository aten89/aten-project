package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dto.StaffFlowApplyPage;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 处理归档请假单的请求
 * @version
 */
public class StaffFlowArchCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IStaffFlowApplyBiz staffFlowApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
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
			queryStaffFlowApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewStaffFlowApply(request, response);
		} else if ("toview".equalsIgnoreCase(action)) {
			//查看
			viewStaffFlowApply(request, response);
		}
	}
	
	private void queryStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		int applyType = HttpRequestHelper.getIntParameter(request, "applyType", 0);
		//构造查询条件对象
		StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		if (applyType != 0) {
			rqp.setApplyType(applyType);
		}
		rqp.addOrder("archiveDate", false);
		
		ListPage<StaffFlowApply> reis = staffFlowApplyBiz.getArchStaffFlowApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new StaffFlowApplyPage(reis).toDocument());
	}
	
	public void viewStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			StaffFlowApply rei = staffFlowApplyBiz.getStaffFlowApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "入职单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("staffFlowApply", rei);
			List<Task> tasks = staffFlowApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			if( rei.getApplyType() == StaffFlowApply.TYPE_ENTRY) {
				request.getRequestDispatcher("/page/hr/staffflow/view_entry.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/page/hr/staffflow/view_resign.jsp").forward(request, response);
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
