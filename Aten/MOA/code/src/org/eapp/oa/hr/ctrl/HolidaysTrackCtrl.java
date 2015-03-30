package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.hr.dto.HolidayApplyTrach;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 处理跟踪请假单的请求
 * @version
 */

public class HolidaysTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IHolidayApplyBiz holidayApplyBiz;
	private HolidaysArchCtrl holidayApplyArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public HolidaysTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		holidayApplyBiz = (IHolidayApplyBiz) SpringHelper.getSpringContext().getBean("holidayApplyBiz");
		holidayApplyArchCtrl= new HolidaysArchCtrl();
		holidayApplyArchCtrl.init();
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
			queryTrackHolidayApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewHolidayApply(request, response);
		}
	}
	
	private void queryTrackHolidayApply(HttpServletRequest request,
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
		
		ListPage<HolidayApply> reis = holidayApplyBiz.getTrackHolidayApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new HolidayApplyTrach(reis).toDocument());
	}
	
	private void viewHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		holidayApplyArchCtrl.viewArchHolidayApply(request, response);
	}
}
