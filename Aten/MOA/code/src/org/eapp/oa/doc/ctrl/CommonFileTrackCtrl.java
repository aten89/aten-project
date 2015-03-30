package org.eapp.oa.doc.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.doc.dto.DocFormPage;
import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.hbean.Task;
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
public class CommonFileTrackCtrl extends HttpServlet {
	
	private static final long serialVersionUID = -6318596462504243853L;
	private IDocFormBiz docFormBiz;
//	private DocumentArchCtrl documentArchCtrl;

	public CommonFileTrackCtrl() {
		super();
	}

	public void init() throws ServletException {
		// Put your code here
		docFormBiz = (IDocFormBiz)SpringHelper.getBean("docFormBiz");
//		documentArchCtrl = new DocumentArchCtrl();
//		documentArchCtrl.init();
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
		}else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看详细
			viewDoc(request, response);
		}else if ("forceEnd".equalsIgnoreCase(action)) {
			//查看详细
			forceEnd(request, response);
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
		
		ListPage<DocForm> list = docFormBiz.getTrackDocForm(qp,user.getAccountID(),DocForm.COM_FILE);
		XMLResponse.outputXML(response,new DocFormPage(list).toDocument());
	}
	
	private void viewDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"您已超时，请重新登录。");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");//
		try {
			DocForm docForm = docFormBiz.getDocFormById(id);
			if (docForm == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			List<Task> tasks = docFormBiz.getEndedTasks(id);
//			if(tasks != null && tasks.size()>0){
//				Task t = tasks.get(0);
//				request.setAttribute("flowName",  t.getFlowName().split("-")[0]);
//			}
			
			//文件起草人，可以强制结束当前流程，进入归档状态
			String creator = docForm.getDraftsman();
			if (creator != null && creator.equals(user.getAccountID())){
				request.setAttribute("allowForceEnd", "1");
			}
			
			request.setAttribute("tasks", tasks);
			request.setAttribute("docForm", docForm);
	
			request.getRequestDispatcher("/page/doc/common/view_doc.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	private void forceEnd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "您已超时，请重新登录。");
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "缺少文件ID，无法执行此操作。");
			return;
		}
		
		String comment = HttpRequestHelper.getParameter(request, "comment");
		try {
			docFormBiz.txForceEnd(id, "强制结束流程，发起人 [ " + user.getDisplayName() + " ] 由于 [ " + comment + " ] 原因，强制结束本流程。");
			
			XMLResponse.outputStandardResponse(response, 1, "已成功结束本流程。");
			
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "试图结束本流程时出错，原因：" + e.getMessage() +"。");
		}
	}
}
