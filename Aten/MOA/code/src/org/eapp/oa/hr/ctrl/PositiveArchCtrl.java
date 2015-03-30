package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IPositiveApplyBiz;
import org.eapp.oa.hr.dto.PositiveApplyPage;
import org.eapp.oa.hr.dto.PositiveQueryParameters;
import org.eapp.oa.hr.hbean.PositiveApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 处理归档请假单的请求
 * @version
 */
public class PositiveArchCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IPositiveApplyBiz positiveApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public PositiveArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		positiveApplyBiz = (IPositiveApplyBiz) SpringHelper.getSpringContext().getBean("positiveApplyBiz");
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
			queryArchPositiveApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewArchPositiveApply(request, response);
		} else if ("toview".equalsIgnoreCase(action)) {
			//查看
			viewArchPositiveApply(request, response);
		}
	}
	
	private void queryArchPositiveApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		//构造查询条件对象
		PositiveQueryParameters rqp = new PositiveQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		rqp.addOrder("archiveDate", false);
		
		ListPage<PositiveApply> reis = positiveApplyBiz.getArchPositiveApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new PositiveApplyPage(reis).toDocument());
	}
	
	public void viewArchPositiveApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			PositiveApply rei = positiveApplyBiz.getPositiveApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请假单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("positiveApply", rei);
			List<Task> tasks = positiveApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.getRequestDispatcher("/page/hr/positive/view_posi.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
