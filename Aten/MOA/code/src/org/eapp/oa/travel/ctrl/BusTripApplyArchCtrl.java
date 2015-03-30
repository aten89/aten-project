package org.eapp.oa.travel.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dto.BusTripApplyArch;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

public class BusTripApplyArchCtrl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5297129729783116115L;
	
	private IBusTripApplyBiz busTripApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public BusTripApplyArchCtrl() {
		super();
	}

	public void init() throws ServletException {
		busTripApplyBiz = (IBusTripApplyBiz) SpringHelper.getSpringContext().getBean(
				"busTripApplyBiz");
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
			queryArchBusTripApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewBusTripApply(request, response);
		} 
	}
	
	private void queryArchBusTripApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		//构造查询条件对象
		BusTripQueryParameters rqp = new BusTripQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		ListPage<BusTripApply> trip = busTripApplyBiz.getArchTripApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new BusTripApplyArch(trip).toDocument());
	}
	
	void viewBusTripApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//出差单号
		if (id == null || id.length()==0) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			BusTripApply trip = busTripApplyBiz.getBusTripApplyById(id);
			if (trip == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "出差申请单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("busTripApply", trip);
			List<Task> tasks = busTripApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.getRequestDispatcher("/page/travel/approval/view_travel.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
}
