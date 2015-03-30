package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.hr.dto.HolidayDetailPage;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * 请假单查询
 */
public class HolidaysQueryCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IHolidayApplyBiz holidayApplyBiz;
	private HolidaysArchCtrl holidayApplyArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public HolidaysQueryCtrl() {
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
			queryHolidayApply(request, response);
			return;
		} else if (SysConstants.EXPORT.equalsIgnoreCase(action)) {
			//导出
			exportHolidayApply(request, response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewHolidayApply(request, response);
		}
	}
	
	private void queryHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String bgnQueryDate = DataFormatUtil.formatTime(request.getParameter("bgnquerydate"));
		String endQueryDate = DataFormatUtil.formatTime(request.getParameter("endquerydate"));
		//构造查询条件对象
		HolidayQueryParameters rqp = new HolidayQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		if(bgnQueryDate != null){
			rqp.setBgnQueryDate(Timestamp.valueOf(bgnQueryDate));
		}
		if(endQueryDate != null){
			Timestamp t = Timestamp.valueOf(endQueryDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			rqp.setEndQueryDate(t);
		}
		
		rqp.addOrder("startDate", false);
		
		ListPage<HolidayDetail> reis = holidayApplyBiz.getHolidayDetail(rqp);
		XMLResponse.outputXML(response, new HolidayDetailPage(reis).toDocument());
	}
	
	private void exportHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", -1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String bgnQueryDate = DataFormatUtil.formatTime(request.getParameter("bgnquerydate"));
		String endQueryDate = DataFormatUtil.formatTime(request.getParameter("endquerydate"));
		//构造查询条件对象
		HolidayQueryParameters rqp = new HolidayQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setApplicant(applicant);
		if(bgnQueryDate != null){
			rqp.setBgnQueryDate(Timestamp.valueOf(bgnQueryDate));
		}
		if(endQueryDate != null){
			Timestamp t = Timestamp.valueOf(endQueryDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			rqp.setEndQueryDate(t);
		}
		
		rqp.addOrder("startDate", false);
		
		// 创建一个文件名
		String fileName = request.getSession().getId() + System.currentTimeMillis() / 10000 + ".xls";
		try {
			holidayApplyBiz.csExportHolidayDetail(rqp, FileDispatcher.getTempDir() + fileName);
		    XMLResponse.outputStandardResponse(response, 1, FileDispatcher.getTempAbsDir() + fileName);
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		} catch (Exception e) {
		    e.printStackTrace();
		    XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}
	}
	
	private void viewHolidayApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		holidayApplyArchCtrl.viewArchHolidayApply(request, response);
	}
	
}
