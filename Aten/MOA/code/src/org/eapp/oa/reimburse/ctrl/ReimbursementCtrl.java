package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.eapp.oa.reimburse.blo.IReimbursementBiz;
import org.eapp.oa.reimburse.dto.ReimbursementPage;
import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;


public class ReimbursementCtrl extends HttpServlet {

	private IReimbursementBiz  reimbursementBiz; 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1456416693746643089L;

	/**
	 * Constructor of the object.
	 */
	public ReimbursementCtrl() {
		super();
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		reimbursementBiz = (IReimbursementBiz)SpringHelper.getSpringContext().getBean("reimbursementBiz");
	}
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request , response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = HttpRequestHelper.getParameter(request, "act");	
		
		if(SysConstants.QUERY.equalsIgnoreCase(act)){
			query(request , response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)){
			viewReimbursement(request , response);
			return;
		}

	}
	
	private void query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 12);
		
		String id = HttpRequestHelper.getParameter(request, "id");
		String budgetItemName = HttpRequestHelper.getParameter(request, "budgetItemName");
		String beginArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("beginArchiveDate"));
		String endArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("endArchiveDate"));
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String passed = HttpRequestHelper.getParameter(request, "passed");
		//构造查询条件对象
		ReimbursementQueryParameters rqp = new ReimbursementQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		
		rqp.setID(id);
		rqp.setBudgetItem(budgetItemName);
		rqp.setApplicant(applicant);
		if(beginArchiveDateStr != null){
			rqp.setBeginArchiveDate(Timestamp.valueOf(beginArchiveDateStr));
		}
		if(endArchiveDateStr != null){
			Timestamp t = Timestamp.valueOf(endArchiveDateStr);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			rqp.setEndArchiveDate(t);
		}
		if (passed != null) {
			rqp.setPassed("Y".equals(passed));
		}
		//查询
		ListPage<Reimbursement> reimbursementPage = reimbursementBiz.queryReimbursement(rqp);
		//转成DOC
		ReimbursementPage rlPage = new ReimbursementPage(reimbursementPage);
		Document xmlDoc = rlPage.toDocument();
		XMLResponse.outputXML(response, xmlDoc);
	}

	private void viewReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ReimbursementArchCtrl s = new ReimbursementArchCtrl();
		s.init();
		s.viewReimbursement(request, response);
	}

}
