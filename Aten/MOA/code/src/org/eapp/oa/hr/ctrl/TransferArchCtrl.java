package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
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
 * 处理归档请假单的请求
 * @version
 */
public class TransferArchCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private ITransferApplyBiz transferApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public TransferArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		transferApplyBiz = (ITransferApplyBiz) SpringHelper.getSpringContext().getBean("transferApplyBiz");
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
			queryArchTransferApply(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewArchTransferApply(request, response);
		} else if ("toview".equalsIgnoreCase(action)) {
			//查看
			viewArchTransferApply(request, response);
		}
	}
	
	private void queryArchTransferApply(HttpServletRequest request,
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
		rqp.addOrder("archiveDate", false);
		
		ListPage<TransferApply> reis = transferApplyBiz.getArchTransferApply(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new TransferApplyPage(reis).toDocument());
	}
	
	public void viewArchTransferApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			TransferApply rei = transferApplyBiz.getTransferApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请假单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("transferApply", rei);
			List<Task> tasks = transferApplyBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.getRequestDispatcher("/page/hr/transfer/view_tran.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
