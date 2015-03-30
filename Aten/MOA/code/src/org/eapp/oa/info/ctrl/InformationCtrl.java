package org.eapp.oa.info.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.info.blo.IInfoLayoutBiz;
import org.eapp.oa.info.blo.IInformationBiz;
import org.eapp.oa.info.dto.InfoLayoutNameSelect;
import org.eapp.oa.info.dto.InformationPage;
import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dto.AttachmentList;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;


/**
 * 
 * @author zsy
 * @version Mar 23, 2009
 */
public class InformationCtrl extends HttpServlet {

	/**
	 * 
	 */
	
	 private IInformationBiz informationBiz;
	 private IInfoLayoutBiz infoLayoutBiz;
	
	private static final long serialVersionUID = -5572557105969757507L;
	
	/**
	 * Constructor of the object.
	 */
	public InformationCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		informationBiz =  (IInformationBiz) SpringHelper.getBean("informationBiz");
		infoLayoutBiz = (IInfoLayoutBiz) SpringHelper.getBean("infoLayoutBiz");
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
		if ("initquery".equalsIgnoreCase(action)) {
			initQueryInfo(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			queryInformation(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			view(request, response);
			return;
		} else if (SysConstants.TO_TOP.equalsIgnoreCase(action)) {
			toTop(request, response);
			return;
		} else if (SysConstants.SHIELD.equalsIgnoreCase(action)) {
			shield(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			delete(request, response);
			return;
		} else if ("getfiles".equalsIgnoreCase(action)){
			//取得附件列表
			getAttachments(request, response);
			return;
		} else if ("layoutselect".equalsIgnoreCase(action)){
			//取得板块列表
			infoLayoutSelect(request, response);
			return;
		}
	}
	
	private void initQueryInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/page/info/published/query_info.jsp").forward(request, response);
	}
	
	private void queryInformation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String infoLayout = HttpRequestHelper.getParameter(request, "infolayout");
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
		//对该版块的管理权限验证
		InfoLayout layout = infoLayoutBiz.getAssignLayoutByName(user.getAccountID(), 
				groupNames, postNames, infoLayout, InfoLayoutAssign.FLAG_INFOMAN);
		if (layout == null) {
			XMLResponse.outputStandardResponse(response, 0, "没有管理“" + infoLayout + "”的权限");
			return;
		}
	
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize",12);
		String property = HttpRequestHelper.getParameter(request, "property");
		String title = HttpRequestHelper.getParameter(request, "title");
		
		String type = HttpRequestHelper.getParameter(request, "type");
		if (infoLayout == null) {
			XMLResponse.outputStandardResponse(response, 0, "版块名称不能为空");
			return;
		}	
		InformationQueryParameters qp = new InformationQueryParameters();
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);
		qp.setInfoLayout(infoLayout);
		if (property != null) {
			qp.setInfoPropertys(new Integer[]{Integer.valueOf(property)});
		}
		qp.setInfoClass(type);
		qp.setSubject(title);
		qp.setInfoStatus(Information.STATUS_PUBLISH);
		qp.addOrder("infoProperty", true);
		qp.addOrder("publicDate", false);
		ListPage<Information> list = informationBiz.queryInformation(qp);
		InformationPage listPage = new InformationPage(list);
		XMLResponse.outputXML(response, listPage.toDocument());
	}
	
	private void toTop(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "id不能为空");
			return;
		}	
		boolean flag = HttpRequestHelper.getBooleanParameter(request, "flag", false);
		try {
			Information infor = informationBiz.txUpdateState(user.getAccountID(), 
					groupNames, postNames, id, flag ? Information.PROPERTY_COMMON : Information.PROPERTY_TOTOP);
			XMLResponse.outputStandardResponse(response, 1, infor.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	private void shield(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);

		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
	
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "id不能为空");
			return;
		}	
		boolean flag = HttpRequestHelper.getBooleanParameter(request, "flag", false);
		try {
			Information infor = informationBiz.txUpdateState(user.getAccountID(), 
					groupNames, postNames, id, flag ? Information.PROPERTY_COMMON : Information.PROPERTY_SHIELD);
			XMLResponse.outputStandardResponse(response, 1, infor.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	private void delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);

		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
	
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "id不能为空");
			return;
		}
		try {
			Information infor = informationBiz.txUpdateState(user.getAccountID(), 
					groupNames, postNames, id, Information.PROPERTY_DELETE);
			XMLResponse.outputStandardResponse(response, 1, infor.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	public void view(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		Information info = informationBiz.getInformationById(id);
		request.setAttribute("info", info);
//		String url = null;
//		if (Information.DISPLAYMODE_CONTENT == info.getDisplayMode()) {
//		    url = info.getContentAccUrl();
//			//url = "/page/InformationManage/InfoPublish/InfoDetail.jsp";
////		} else if (Information.DISPLAYMODE_CSMURL == info.getDisplayMode()) {
////			RMIClientConfig csmRmiClientConfig = (RMIClientConfig)SpringHelper.getBean("csmRMIClientConfig");
////			url = csmRmiClientConfig.getServiceBasePath() + info.getContentUrl();
////			response.sendRedirect(url);
//		} else {
//			//显示内容地址
//			url = "/page/info/published/view_info.jsp";
//		}
		request.getRequestDispatcher("/page/info/published/view_info.jsp").forward(request, response);
	}
	
	private void getAttachments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String referId = HttpRequestHelper.getParameter(request, "referid");
		 if (referId == null) {
         	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
         	return;
         }
		Set<Attachment> ams = informationBiz.getInfomationAttachments(referId);
		XMLResponse.outputXML(response, new AttachmentList(ams).toDocument());
	}
	
	private void infoLayoutSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try{
			//查询
			List<String> list = informationBiz.getAllInfoLayout();
			HTMLResponse.outputHTML(response,new InfoLayoutNameSelect(list).toString());
		}catch(Exception e){
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
}
