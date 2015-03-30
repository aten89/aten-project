package org.eapp.oa.hr.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dto.StaffFlowApplyQueryPage;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;


/**
 * 处理归档请假单的请求
 * @version
 */
public class StaffFlowPromptQueryCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IStaffFlowApplyBiz staffFlowApplyBiz;
//	private IAddressListBiz addressListBiz;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowPromptQueryCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
//		addressListBiz = (IAddressListBiz) SpringHelper.getBean("addressListBiz");
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
		if ("query_contr".equalsIgnoreCase(action)) {
			//查询
			queryContractPrompt(request, response);
			return;
		} else if ("query_formal".equalsIgnoreCase(action)) {
			//查询
			queryFormalPrompt(request, response);
		} else if ("query_birth".equalsIgnoreCase(action)) {
			//查询
			queryBirthdayPrompt(request, response);
		}
	}
	
	private void queryContractPrompt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		int beforeDays = HttpRequestHelper.getIntParameter( request, "bdays", 30);
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffFlowApply> page = null;
			StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
			rqp.setPageNo(pageNo);
			rqp.setPageSize(pageSize);
			
			rqp.setGroupName(userDeptName);
			rqp.setUserKeyword(queryString);
				
			rqp.addOrder("contractEndDate", true);
			
			page = staffFlowApplyBiz.queryContractPrompt(rqp, beforeDays);
			XMLResponse.outputXML(response, new StaffFlowApplyQueryPage(page).toDocument());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	private void queryFormalPrompt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		int beforeDays = HttpRequestHelper.getIntParameter( request, "bdays", 15);
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffFlowApply> page = null;
			StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
			rqp.setPageNo(pageNo);
			rqp.setPageSize(pageSize);
			
			rqp.setGroupName(userDeptName);
			rqp.setUserKeyword(queryString);
			rqp.setStaffStatus("试用");//试用的才提醒
				
			rqp.addOrder("formalDate", true);
			
			page = staffFlowApplyBiz.queryFormalPrompt(rqp, beforeDays);
			XMLResponse.outputXML(response, new StaffFlowApplyQueryPage(page).toDocument());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	private void queryBirthdayPrompt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		int beforeDays = HttpRequestHelper.getIntParameter( request, "bdays", 7);
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffFlowApply> page = null;
			StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
			rqp.setPageNo(pageNo);
			rqp.setPageSize(pageSize);
			
			rqp.setGroupName(userDeptName);
			rqp.setUserKeyword(queryString);
				
			rqp.addOrder("birthdate", true);
			
			page = staffFlowApplyBiz.queryBirthdayPrompt(rqp, beforeDays);
			XMLResponse.outputXML(response, new StaffFlowApplyQueryPage(page).toDocument());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
}
