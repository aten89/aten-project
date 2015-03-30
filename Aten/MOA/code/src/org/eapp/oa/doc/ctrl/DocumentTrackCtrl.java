package org.eapp.oa.doc.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.doc.dto.DocFormPage;
import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

/**
 * @author Tim
 * @version 2009-05-10
 */
public class DocumentTrackCtrl extends HttpServlet {
	
	private static final long serialVersionUID = -6318596462504243853L;
	private IDocFormBiz docFormBiz;
	private DocumentArchCtrl documentArchCtrl;

	public DocumentTrackCtrl() {
		super();
	}

	public void init() throws ServletException {
		// Put your code here
		docFormBiz = (IDocFormBiz)SpringHelper.getBean("docFormBiz");
		documentArchCtrl = new DocumentArchCtrl();
		documentArchCtrl.init();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询跟踪公文
			queryTrackDocument(request,response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看详细
			viewDoc(request, response);
		}
		
	}
	
	private void queryTrackDocument(HttpServletRequest request,
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
		DocFormQueryParameters qp = new DocFormQueryParameters();
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
		qp.addOrder("draftDate", false);
		
		ListPage<DocForm> list = docFormBiz.getTrackDocForm(qp,user.getAccountID(),DocForm.DIS_FILE);
		XMLResponse.outputXML(response,new DocFormPage(list).toDocument());
	}
	
	private void viewDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		documentArchCtrl.viewDoc(request, response);
		
	}


}
