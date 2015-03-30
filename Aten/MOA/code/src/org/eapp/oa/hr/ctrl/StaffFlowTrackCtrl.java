package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
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
 */

public class StaffFlowTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IStaffFlowApplyBiz staffFlowApplyBiz;
	private StaffFlowArchCtrl staffFlowArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
		staffFlowArchCtrl= new StaffFlowArchCtrl();
		staffFlowArchCtrl.init();
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
		
		//构造查询条件对象
		StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		
		ListPage<StaffFlowApply> reis = staffFlowApplyBiz.getTrackStaffFlowApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new StaffFlowApplyPage(reis).toDocument());
	}
	
	private void viewStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		staffFlowArchCtrl.viewStaffFlowApply(request, response);
	}
}
