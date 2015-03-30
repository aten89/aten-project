package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.hbean.Task;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * 处理归档报销单的请求
 * @author zsy
 * @version
 */
public class ReimbursementArchCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IReimbursementBiz reimbursementBiz;
	/**
	 * Constructor of the object.
	 */
	public ReimbursementArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean(
				"reimbursementBiz");
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
			//查询归档的报销单
			queryArchReimbursement(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看报销单
			viewReimbursement(request, response);
		}
	}
	
	private void queryArchReimbursement(HttpServletRequest request,
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
		
		String beginArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("beginArchiveDate"));
		String endArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("endArchiveDate"));
		String passed = HttpRequestHelper.getParameter(request, "passed");
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
		if(beginArchiveDateStr != null){
			rqp.setBeginArchiveDate(Timestamp.valueOf(beginArchiveDateStr));
		}
		if(endArchiveDateStr != null){//加一天
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
		//当前年份
//		Calendar cal = Calendar.getInstance();
//		int tmpYear = cal.get(Calendar.YEAR);
//		rqp.setYear(tmpYear);
		ListPage<Reimbursement> reis = reimbursementBiz.getArchReimbursement(rqp, user.getAccountID());
		XMLResponse.outputXML(response, new ReimbursementPage(reis).toDocument());
	}
	
	public void viewReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//报销单号
		try {
			Reimbursement rei = reimbursementBiz.getReimbursementById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "报销单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.getSession().setAttribute(id, rei);
			request.setAttribute("reimbusement", rei);
			List<Task> tasks = reimbursementBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.setAttribute("reimJson", JSON.toJSONStringWithDateFormat(rei, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
			request.getRequestDispatcher("/page/cost/reimburse/view_reim.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
