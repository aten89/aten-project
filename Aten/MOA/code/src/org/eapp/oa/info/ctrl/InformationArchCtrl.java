package org.eapp.oa.info.ctrl;

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
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.info.dto.InfoFormPage;
import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
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
public class InformationArchCtrl extends HttpServlet {
	
	private IInfoFormBiz infoFormBiz;
	
	private InformationDraftCtrl informationDraftCtrl;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	/**
	 * Constructor of the object.
	 */
	public InformationArchCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		infoFormBiz = (IInfoFormBiz) SpringHelper.getBean("infoFormBiz");
		
		informationDraftCtrl = new InformationDraftCtrl();
		informationDraftCtrl.init();
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
			queryArchInfoForm(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			initModifyInfo(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(action)) {
			//修改已归档表单
			modifyArchInfo(request, response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewInfo(request, response);
		} else if ("validate".equalsIgnoreCase(action)) {
			//验证是否已经做过修改操作
			validate(request, response);
		}
	}
	private void queryArchInfoForm(HttpServletRequest request,
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
		InfoFormQueryParameters qp = new InfoFormQueryParameters();
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
		ListPage<InfoForm> list = infoFormBiz.getArchInfoFrom(qp, user.getAccountID());
		XMLResponse.outputXML(response,new InfoFormPage(list).toDocument());
	}
	/**
	 * 初始化修改归档信息。
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initModifyInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String infoFormId = HttpRequestHelper.getParameter(request, "id");
		if (infoFormId == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			InfoForm modifyInfoForm = infoFormBiz.txCopyInfoForm(infoFormId, user.getAccountID());
			XMLResponse.outputStandardResponse(response, 1, modifyInfoForm.getId());
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "操作失败");
		}
		
	}
	
	public void modifyArchInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		informationDraftCtrl.publishOrStartFlow(request, response);
	}
	
	public void viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//
		try {
			InfoForm infoForm = infoFormBiz.getInfoFormById(id);
			if (infoForm == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			List<Task> tasks = infoFormBiz.getEndedTasks(id);
			request.setAttribute("tasks", tasks);
			request.setAttribute("infoForm", infoForm);
	
			request.getRequestDispatcher("/page/info/approval/view_info.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	public void validate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//
		String userName = null; 
		//根据判断数据库中是否存在复制的对象
		InfoForm infoForm = infoFormBiz.getInfoFormByCopyId(id);
		if(infoForm != null){
			userName = infoForm.getInformation().getDraftsManName();
			if(infoForm.getInformation().getInfoStatus().equals(Information.STATUS_UNPUBLISH)){
				//表示未进行审批，处于草稿状态
				XMLResponse.outputStandardResponse(response, 0, userName + "已对该信息进行修改,如需要再次修订请前往起草页面!");
			}if(infoForm.getInformation().getInfoStatus().equals(Information.STATUS_APPROVAL)){
				//表示进行审批
				XMLResponse.outputStandardResponse(response, 0, userName + "已对该信息进行修改,并且提交审批,暂不能对此信息进行操作!");
			}
		}else{
			XMLResponse.outputStandardResponse(response, 1, "可以修改");
		}
	}
}
