package org.eapp.oa.info.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.info.dto.InfoFormPage;
import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * @author zsy
 * @version Mar 23, 2009
 */
public class InformationTrackCtrl extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	IInfoFormBiz infoFormBiz;
	
	private InformationArchCtrl informationArchCtrl;
	/**
	 * Constructor of the object.
	 */
	public InformationTrackCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		infoFormBiz =  (IInfoFormBiz) SpringHelper.getBean("infoFormBiz");
		
		informationArchCtrl = new InformationArchCtrl();
		informationArchCtrl.init();
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
			queryTrackInformation(request,response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewInfo(request, response);
		}
	}
	
	private void queryTrackInformation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String beginDraftDate = DataFormatUtil.formatTime( request.getParameter("beginDraftDate"));
		String endDraftDate = DataFormatUtil.formatTime( request.getParameter("endDraftDate"));
		
		InfoFormQueryParameters qp = new InfoFormQueryParameters();
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);
		qp.setSubject(subject);
		if(beginDraftDate != null){
			qp.setBeginDraftDate(Timestamp.valueOf(beginDraftDate));
		}
		if(endDraftDate != null){
			Timestamp t = Timestamp.valueOf(endDraftDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndDraftDate(t);
		}
		
	//	qp.addOrder("information.draftDate", false);
		
		ListPage<InfoForm> list = infoFormBiz.getTrackInfoFrom(qp, user.getAccountID());
		XMLResponse.outputXML(response,new InfoFormPage(list).toDocument());
	}
	
	private void viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		informationArchCtrl.viewInfo(request, response);
	}
}
