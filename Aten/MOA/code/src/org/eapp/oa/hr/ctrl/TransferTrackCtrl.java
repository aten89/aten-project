package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.ITransferApplyBiz;
import org.eapp.oa.hr.dto.TransferApplyPage;
import org.eapp.oa.hr.dto.TransferQueryParameters;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 处理跟踪请假单的请求
 */

public class TransferTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private ITransferApplyBiz transferApplyBiz;
	private TransferArchCtrl transferArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public TransferTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		transferApplyBiz = (ITransferApplyBiz) SpringHelper.getSpringContext().getBean("transferApplyBiz");
		transferArchCtrl= new TransferArchCtrl();
		transferArchCtrl.init();
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
			queryTrackTransferApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewTransferApply(request, response);
		}
	}
	
	private void queryTrackTransferApply(HttpServletRequest request,
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
		TransferQueryParameters rqp = new TransferQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		
		ListPage<TransferApply> reis = transferApplyBiz.getTrackTransferApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new TransferApplyPage(reis).toDocument());
	}
	
	private void viewTransferApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		transferArchCtrl.viewArchTransferApply(request, response);
	}
}
