package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
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
 * 处理跟踪请假单的请求
 * @version
 */

public class PositiveTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IPositiveApplyBiz positiveApplyBiz;
	private PositiveArchCtrl positiveArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public PositiveTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		positiveApplyBiz = (IPositiveApplyBiz) SpringHelper.getSpringContext().getBean("positiveApplyBiz");
		positiveArchCtrl= new PositiveArchCtrl();
		positiveArchCtrl.init();
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
			queryTrackPositiveApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewPositiveApply(request, response);
		}
	}
	
	private void queryTrackPositiveApply(HttpServletRequest request,
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
		
		ListPage<PositiveApply> reis = positiveApplyBiz.getTrackPositiveApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new PositiveApplyPage(reis).toDocument());
	}
	
	private void viewPositiveApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		positiveArchCtrl.viewArchPositiveApply(request, response);
	}
}
