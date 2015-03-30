package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
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


/**
 * 处理跟踪报销单的请求
 * @author zsy
 * @version
 */
public class ReimbursementTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IReimbursementBiz reimbursementBiz;
	
	private ReimbursementArchCtrl reimbursementArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public ReimbursementTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean(
				"reimbursementBiz");
		
		reimbursementArchCtrl = new ReimbursementArchCtrl();
		reimbursementArchCtrl.init();
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
			//查询跟踪的报销单
			queryTrackReimbursement(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看的报销单
			viewReimbursement(request, response);
		}
	}
	
	private void queryTrackReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
		boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
		
		String id = HttpRequestHelper.getParameter(request, "id");
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String beginApplyDate = DataFormatUtil.formatTime( request.getParameter("beginApplyDate"));
		String endApplyDate = DataFormatUtil.formatTime( request.getParameter("endApplyDate"));
		
		
		
		//构造查询条件对象
		ReimbursementQueryParameters rqp = new ReimbursementQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		//增加对排序列的处理
		if (sortCol != null && !sortCol.trim().equals("")){
			rqp.addOrder(sortCol, ascend);
		}
		
		rqp.setID(id);
		rqp.setApplicant(applicant);
		if(beginApplyDate != null){
			rqp.setBeginApplyDate(Timestamp.valueOf(beginApplyDate));
		}
		if(endApplyDate != null){//加一天
			Timestamp t = Timestamp.valueOf(endApplyDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			rqp.setEndApplyDate(t);
		}
		ListPage<Reimbursement> reis = reimbursementBiz.getTrackReimbursement(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new ReimbursementPage(reis).toDocument());
	}
	
	private void viewReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		reimbursementArchCtrl.viewReimbursement(request, response);
	}
}
