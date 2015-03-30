package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.hr.dto.HolidayApplyArch;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 处理归档请假单的请求
 */
public class HolidaysArchCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IHolidayApplyBiz holidayApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public HolidaysArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		holidayApplyBiz = (IHolidayApplyBiz) SpringHelper.getSpringContext().getBean(
				"holidayApplyBiz");
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
			queryArchHolidayApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewArchHolidayApply(request, response);
		} else if ("toview".equalsIgnoreCase(action)) {
			//查看
			viewArchHolidayApply(request, response);
		} else if ("initcancel".equalsIgnoreCase(action)){
			//销假
			initCancelArchHolidayApply(request, response);
		}
	}
	
	private void queryArchHolidayApply(HttpServletRequest request,
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
		HolidayQueryParameters rqp = new HolidayQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		rqp.addOrder("archiveDate", false);
		
		ListPage<HolidayApply> reis = holidayApplyBiz.getArchHolidayApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new HolidayApplyArch(reis, user.getAccountID()).toDocument());
	}
	
	public void viewArchHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			HolidayApply rei = holidayApplyBiz.getHolidayApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请假单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("holidayApply", rei);
			List<Task> tasks = holidayApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.getRequestDispatcher("/page/hr/holidays/view_holi.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	public void initCancelArchHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//
		try {
			HolidayApply rei = holidayApplyBiz.getHolidayApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请假单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("holidayApply", rei);
			request.getRequestDispatcher("/page/hr/holidays/draft_holi_can.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
