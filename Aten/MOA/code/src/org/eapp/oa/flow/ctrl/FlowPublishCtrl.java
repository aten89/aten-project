package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.dto.FlowConfigPage;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.dto.FlowSelectXml;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.JSONHelper;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.WfmException;


/**
 * <p>Title: </p>
 * <p>Description: 已发布流程管理</p>
 * @version 1.0 
 */
public class FlowPublishCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1404311480994679012L;

//	private static final String SESSION_CREDENCE = FlowConfigCtrl.class.getName() + "_upoladCredence";

	private IFlowConfigBiz flowConfigBiz;
	
	/**
	 * Constructor of the object.
	 */
	public FlowPublishCtrl() {
		super();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		flowConfigBiz = (IFlowConfigBiz) SpringHelper.getBean("flowConfigBiz");
//		flowAssignBiz = (IFlowAssignBiz) SpringHelper.getBean("flowAssignBiz");
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("act");
		if ("init_query".equalsIgnoreCase(action)) {
			//初始化查询页面
			initQueryFlow(request, response);
			return;
		}else if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询已发布流程
			queryFlow(request, response);
			return;
		}else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//获取流程详情
			initViewFlow(request, response);
			return;
		}else if ("flowxml".equalsIgnoreCase(action)) {
			//查看流程图
			getXmlFlowDefine(request, response);
			return;
		}else if ("init_modify".equalsIgnoreCase(action)) {
			//修改流程草稿
			initModifyFlow(request, response);
			return;
		}else if (SysConstants.DISABLE.equalsIgnoreCase(action)) {
			//禁用流程
			disableFlow(request, response);
			return;
//		}else if ("init_authorise".equalsIgnoreCase(action)) {
//			// 进入授权页面
//			initAuthorise(request, response);
//			return;
		}else if ("get_flow_select".equalsIgnoreCase(action)) {
			//获取已发布流程列表
			getPublishedFlowSelectByCategory(request, response);
			return;
//		}else if("init_assign".equalsIgnoreCase(action)){
//			//初始化授权页面
//			initAssign(request,response);
//			return;
//		}else if("init_assign_detail".equalsIgnoreCase(action)){
//			//初始化详细授权页面
//			initAssignDetail(request,response);
//			return;
		}else if("getByKey".equalsIgnoreCase(action)){
			//初始化详细授权页面
			getPublishedFlowSelectByKey(request,response);
			return;
		}
	}
	
//	private void initAssignDetail(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		//信息类别
//		String page = HttpRequestHelper.getParameter(request, "page");
//		String title = HttpRequestHelper.getParameter(request, "title");
//		request.setAttribute("title", title);
//		request.getRequestDispatcher("/page/system/flowconf/"+page).forward(request, response);
//	
//	}
	/**
	 * 初始化查询页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void initQueryFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = "/page/system/flowconf/query_pub.js.jsp";		
		request.getRequestDispatcher(page).forward(request, response);
	}

	/**
	 * 查询已发布流程
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void queryFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageCount = HttpRequestHelper.getIntParameter(request, "pagecount", 20);
		

		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		String flowName = HttpRequestHelper.getParameter(request, "flowName");
		Long flowVersion = HttpRequestHelper.getLongParameter(request, "flowVersion", -1);
		flowVersion = (flowVersion == -1)?null:flowVersion;
		Integer draftFlag = FlowConfig.FLOW_FLAG_PUBLISHED;//只查询为已发布流程
		
		FlowConfigQueryParameters flowConfigQP = new FlowConfigQueryParameters();
		flowConfigQP.setPageNo(pageNo);
		flowConfigQP.setPageSize(pageCount);
		
		flowConfigQP.setFlowClass(flowClass);
		flowConfigQP.setFlowKey(flowKey);
		flowConfigQP.setFlowName(flowName);
		flowConfigQP.setFlowVersion(flowVersion);
		flowConfigQP.setDraftFlag(draftFlag);
		
		flowConfigQP.addOrder("flowClass", true);
		flowConfigQP.addOrder("flowName", true);
		
		try {
			ListPage<FlowConfig> page = flowConfigBiz.queryFlow(flowConfigQP, true);
			XMLResponse.outputXML(response, new FlowConfigPage(page).toDocument());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	
	/**
	 * 初始化查看流程详情
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void initViewFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = "/page/system/flowconf/view_flow.jsp";
		request.getRequestDispatcher(page).forward(request, response);
	}
	
	
	/**
	 * 进入修改流程页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initModifyFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null){
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "流程标识不能为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		
		//获取已发布流程的流程草稿
		FlowConfig flowConfig;
		try {
			flowConfig = flowConfigBiz.addOrGetFlowDraft(flowKey);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		
		if(flowConfig == null){
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "创建已发布流程的草稿失败");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
 		
		//设置流程类别
		request.setAttribute("flowCategory", flowConfig.getFlowClass());		
		String queryString = "?act=" + SysConstants.MODIFY;
		queryString += "&flowKey=" + flowConfig.getFlowKey();//更新flowKey
		
		String page = "/page/system/flowconf/edit_flow.jsp";
		page += queryString;
		
		request.getRequestDispatcher(page).forward(request, response);
		
	}
	
	/**
	 * 禁用流程
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void disableFlow(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		try {
			flowConfigBiz.txDisableFlow(flowKey);
			XMLResponse.outputStandardResponse(response, 1, "禁用流程成功！");
		} catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	
	/**
	 * 进入授权页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
//	private void initAuthorise(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String page = "/page/system/flowconf/frame_flow.jsp";
//		request.getRequestDispatcher(page).forward(request, response);
//		
//	}

	
	/**
	 * 获取已发布流程的XML定义 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getXmlFlowDefine(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null || "".equalsIgnoreCase(flowKey)){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		
		String workflowString = flowConfigBiz.getFlowXmlDefine(flowKey);
		if(workflowString == null || "".equalsIgnoreCase(workflowString)){
			XMLResponse.outputStandardResponse(response, 0, "加载失败：流程定义文件不存在！");
			return;
		}
		
		XMLResponse.outputXML(response,workflowString);	
		
	}

	/**
	 * 根据流程类别获取已发布流程列表
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getPublishedFlowSelectByCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		
		FlowConfigQueryParameters qp = new FlowConfigQueryParameters();
		qp.setFlowClass(flowClass);
		qp.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);
		qp.setPageSize(0);//设置为不分页
		try {
			List<FlowConfig> flowList = flowConfigBiz.queryFlow(qp, false).getDataList();
			ListPage<FlowConfig> listPage = new ListPage<FlowConfig>();
			listPage.setDataList(flowList);
			Document doc = new FlowConfigPage(listPage).toDocument();
			if (!JSONHelper.outputXmlJSON(request, response, doc)) {//是否输出JSON
				XMLResponse.outputXML(response, doc);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
		
	}
	
	private void getPublishedFlowSelectByKey(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		
		FlowConfigQueryParameters qp = new FlowConfigQueryParameters();
		qp.setFlowClass(flowClass);
		qp.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);
		qp.setPageSize(0);//设置为不分页
		try {
			List<FlowConfig> flowList = flowConfigBiz.queryFlow(qp, false).getDataList();
			HTMLResponse.outputHTML(response, new FlowSelectXml(flowList).toString());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
		
	}

}
