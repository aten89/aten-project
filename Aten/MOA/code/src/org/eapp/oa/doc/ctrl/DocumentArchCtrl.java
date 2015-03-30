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
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

/**
 * 公文归档
 * @author Tim
 * @version 2009-May 13, 2009
 */
public class DocumentArchCtrl extends HttpServlet {

	private static final long serialVersionUID = -6311647042326072445L;
	private IDocFormBiz docFormBiz; 
	
	public DocumentArchCtrl() {
		super();
	}
	public void init() throws ServletException {
		// Put your code here
		docFormBiz = (IDocFormBiz)SpringHelper.getBean("docFormBiz");
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
			//查询归档公文
			queryArchDocForm(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//初始化归档公文的修改
			initModifyInfo(request, response);
			return;
		}
        // else if (SysConstants.MODIFY.equalsIgnoreCase(action)) {
        // //修改已归档表单
        // // modifyArchDoc(request, response);
        // }
		else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看详细
			viewDoc(request, response);
		} else if ("validate".equalsIgnoreCase(action)) {
			//验证是否已经做过修改操作
			validate(request, response);
		}
	}
	
	private void queryArchDocForm(HttpServletRequest request,
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
		String beginArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("beginArchiveDate"));
		String endArchiveDateStr = DataFormatUtil.formatTime( request.getParameter("endArchiveDate"));
		String passed = HttpRequestHelper.getParameter(request, "passed");
		DocFormQueryParameters qp = new DocFormQueryParameters();
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);		
		qp.setSubject(subject);
		if(beginArchiveDateStr != null){
			qp.setBeginArchDate(Timestamp.valueOf(beginArchiveDateStr));
		}
		if(endArchiveDateStr != null){//加一天
			Timestamp t = Timestamp.valueOf(endArchiveDateStr);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndArchDate(t);
		}
		if (passed != null) {
			qp.setPassed("Y".equals(passed));
		}
		qp.addOrder("archiveDate", false);
		ListPage<DocForm> list = docFormBiz.getArchDocForm(qp, user.getAccountID(),DocForm.DIS_FILE);
		XMLResponse.outputXML(response,new DocFormPage(list).toDocument());
	}

	public void viewDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//
		try {
			DocForm docForm = docFormBiz.getDocFormById(id);
			if (docForm == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			List<Task> tasks = docFormBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.setAttribute("docForm", docForm);
	
			request.getRequestDispatcher("/page/doc/dispatch/view_doc.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	private void initModifyInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null ){
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}

		try{			
			DocForm modifyDocForm =docFormBiz.txCopyDocForm(id, user.getAccountID());//保存信息审批单
			
			XMLResponse.outputStandardResponse(response, 1, modifyDocForm.getId());
		}catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "操作失败");
		} 
	}
	
	public void validate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//
		String userName = null; 
		//根据判断数据库中是否存在复制的对象
		List<DocForm> docForms = docFormBiz.getDocFormByCopyId(id);
		if(docForms != null && docForms.size() > 0){
			DocForm docForm = docForms.get(0);
			userName = docForm.getDraftsmanName();
			if(docForm.getDocStatus().equals(DocForm.STATUS_UNPUBLISH)){
				//表示未进行审批，处于草稿状态
				XMLResponse.outputStandardResponse(response, 1, userName + "已对该公文进行修改,如需要再次修订请前往起草页面!");
			}if(docForm.getDocStatus().equals(DocForm.STATUS_APPROVAL)){
				//表示进行审批
				XMLResponse.outputStandardResponse(response, 1, userName + "已对该公文进行修改,并且提交审批,暂不能对此信息进行操作!");
			}
		}else{
			XMLResponse.outputStandardResponse(response, 0, "");
		}
	}
}
