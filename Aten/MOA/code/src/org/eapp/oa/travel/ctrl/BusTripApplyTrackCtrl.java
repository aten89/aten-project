package org.eapp.oa.travel.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dto.BusTripApplyTrack;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

public class BusTripApplyTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4781835033627833898L;

	private IBusTripApplyBiz busTripApplyBiz;
	private BusTripApplyArchCtrl busTripApplyArchCtrl;
	
	public BusTripApplyTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		busTripApplyBiz = (IBusTripApplyBiz) SpringHelper.getSpringContext().getBean(
				"busTripApplyBiz");
		busTripApplyArchCtrl = new BusTripApplyArchCtrl();
		busTripApplyArchCtrl.init();
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
			queryTrackBusTripApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewBusTripApply(request, response);
		}
	}
	
	private void queryTrackBusTripApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 15);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		
		//构造查询条件对象
		BusTripQueryParameters rqp = new BusTripQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		
		ListPage<BusTripApply> trip = busTripApplyBiz.getTrackTripApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new BusTripApplyTrack(trip).toDocument());
	}
	
	private void viewBusTripApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		busTripApplyArchCtrl.viewBusTripApply(request, response);
	}
}
