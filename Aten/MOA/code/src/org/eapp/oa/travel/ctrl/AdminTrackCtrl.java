package org.eapp.oa.travel.ctrl;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dto.BusTripApplyArch;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

public class AdminTrackCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IBusTripApplyBiz busTripApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public AdminTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		busTripApplyBiz = (IBusTripApplyBiz) SpringHelper.getSpringContext().getBean(
				"busTripApplyBiz");
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
			queryTrackAllArchTrip(request, response);
			return;
		}
	}
	
	private void queryTrackAllArchTrip(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String id = HttpRequestHelper.getParameter(request, "id");
		String startTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "startDate"));
		String endTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endDate"));
		String applyDept = HttpRequestHelper.getParameter(request, "applyDept");
		//构造查询条件对象
		BusTripQueryParameters rqp = new BusTripQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		rqp.setApplyDept(applyDept);
		if(id!=null && id.length()>0){
			rqp.setID(id);
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(startTime != null){
			startDate = Timestamp.valueOf(startTime) ;
		}
		if(endTime != null){
//			Timestamp t = Timestamp.valueOf(endTime);
//			Calendar ca = Calendar.getInstance();
//			ca.setTimeInMillis(t.getTime());
//			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
//			t.setTime(ca.getTimeInMillis());
			endDate = Timestamp.valueOf(endTime);		
		}
		ListPage<BusTripApply> reis = busTripApplyBiz.queryArchTripApply(rqp,startDate,endDate);
		XMLResponse.outputXML(response, new BusTripApplyArch(reis).toDocument());
	}
}
