package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.ISalaryBillBiz;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 我的工资条的请求
 */
public class MySalaryBillCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private ISalaryBillBiz salaryBillBiz;
	private  SalaryBillCtrl salaryBillCtrl;
	/**
	 * Constructor of the object.
	 */
	public MySalaryBillCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		salaryBillBiz = (ISalaryBillBiz) SpringHelper.getSpringContext().getBean("salaryBillBiz");
		salaryBillCtrl = new SalaryBillCtrl();
		salaryBillCtrl.init();
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
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
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

		salaryBillCtrl.querySalaryBill(request, response, user.getAccountID());
	}
	
	public void viewStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		salaryBillCtrl.viewSalaryBill(request, response);
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		int month = HttpRequestHelper.getIntParameter(request, "month", 0);
		if (month == 0) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "参数不能为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			SalaryBill salaryBill = salaryBillBiz.getSalaryBill(user.getAccountID(), month);
			request.setAttribute("salaryBill", salaryBill);
			request.getRequestDispatcher("/page/hr/salarybill/my_salary.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
