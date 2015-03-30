/**
 * 
 */
package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.blo.IFlowMetaBiz;
import org.eapp.oa.flow.dto.CreateFlowDraftXml;
import org.eapp.oa.flow.dto.FlowConfigPage;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.dto.MetaDefineXml;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.flow.hbean.FlowMeta;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.workflow.WfmException;
import org.eapp.workflow.expression.ExpressionEvaluator;


/**
 * <p>Title: </p>
 * <p>Description: 流程配置</p>
 * @version 1.0 
 */
public class FlowDraftCtrl extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2808266533132339526L;

//	private static final String SESSION_CREDENCE = FlowConfigCtrl.class.getName() + "_upoladCredence";

	private IFlowConfigBiz flowConfigBiz;
	
	private IFlowMetaBiz flowMetaBiz;
	
	/**
	 * Constructor of the object.
	 */
	public FlowDraftCtrl() {
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
		flowMetaBiz = (IFlowMetaBiz) SpringHelper.getBean("flowMetaBiz");
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
			//查询流程草稿
			queryFlow(request, response);
			return;
		}else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//添加流程草稿
			addFlow(request, response);
			return;
		}else if ("init_modify".equalsIgnoreCase(action)) {
			//修改流程草稿
			initModifyFlow(request, response);
			return;
		}else if (SysConstants.MODIFY.equalsIgnoreCase(action)) {
			//保存修改的流程草稿
			modifyFlowDraft(request, response);
			return;
		}else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除流程草稿
			deleteFlow(request, response);
			return;
		}else if (SysConstants.ENABLE.equalsIgnoreCase(action)) {
			//发布流程草稿
			publishFlow(request, response);
			return;
		}else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//获取流程详情
			initViewFlow(request, response);
			return;
		}else if ("flowxml".equalsIgnoreCase(action)) {
			//获取流程草稿详情
			getFlowDraftXmlDefine(request, response);
			return;
		}else if ("flow_class".equalsIgnoreCase(action)) {
			//获取流程类别列表
			getFlowClass(request, response);
			return;
		}else if("get_meta_xml".equalsIgnoreCase(action)){
			//获取元数据定义XML文件
			getMetaDefineXml(request,response);
		}else if("valid_expression".equalsIgnoreCase(action)){
			validExpression(request,response);
		} 
	}


	/**
	 * 初始化查询页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void initQueryFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = "/page/system/flowconf/query_draft.jsp";		
		request.getRequestDispatcher(page).forward(request, response);
	}

	/**
	 * 查询流程草稿
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
		Integer draftFlag = FlowConfig.FLOW_FLAG_DRAFT;//只查询为草稿的流程
		
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
		}catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	/**
	 * 新增流程草稿
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void addFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		String workflowXml = HttpRequestHelper.getParameter(request, "workflowXml");
		if (workflowXml == null) {
			XMLResponse.outputStandardResponse(response, 0, "非法参数：所保存的工作流定义xml文件不能为空！");
			return;
		}
		
		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		if (flowClass == null) {
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程类别不能为空！");
			return;
		}

		//保存流程
		try {
			FlowConfig flowConfig = flowConfigBiz.addFlowDraft(workflowXml, flowClass);
			//保存成功
			XMLResponse.outputXML(response, new CreateFlowDraftXml(flowConfig).toDocument());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统异常！");
		}
	
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
		
		FlowConfig flowConfig = flowConfigBiz.getFlowByFlowKey(flowKey, FlowConfig.FLOW_FLAG_DRAFT);
		if (flowConfig == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "流程标识为"+ flowKey +"的流程在数据库不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		
		//设置流程类别
		request.setAttribute("flowCategory", flowConfig.getFlowClass());	
		
		String queryString = "?act=" + SysConstants.MODIFY;
		String page = "/page/system/flowconf/edit_flow.jsp";
		page += queryString;
		
		request.getRequestDispatcher(page).forward(request, response);
		
	}
	
	/**
	 * 获取流程草稿的XML定义
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void getFlowDraftXmlDefine(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null || "".equalsIgnoreCase(flowKey)){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		
		try {
			String workflowString = flowConfigBiz.getFlowDraftXmlDefine(flowKey);
			if(workflowString == null || "".equalsIgnoreCase(workflowString)){
				XMLResponse.outputStandardResponse(response, 0, "加载失败：流程定义文件不存在！");
				return;
			}
			
			XMLResponse.outputXML(response,workflowString);		
		} catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错！获取流程定义失败！");
		}
		
	}
	
	/**
	 * 修改流程草稿
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void modifyFlowDraft(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		
		String workflowXml = HttpRequestHelper.getParameter(request, "workflowXml");
		if (workflowXml == null) {
			XMLResponse.outputStandardResponse(response, 0, "非法参数：所保存的工作流定义xml文件不能为空！");
			return;
		}

		String flowClass = HttpRequestHelper.getParameter(request, "flowClass");
		if (flowClass == null) {
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程类别不能为空！");
			return;
		}
		
		//保存流程
		try {
			flowConfigBiz.modifyFlowDraft(flowKey,workflowXml,flowClass);
			//保存成功
			XMLResponse.outputStandardResponse(response, 1, "保存成功！");
		}catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统异常！");
		}
		
	}
	
	/**
	 * 删除流程草稿
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void deleteFlow(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		try {
			flowConfigBiz.deleteFlowDraft(flowKey);
			XMLResponse.outputStandardResponse(response, 1, "删除成功！");
		}catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统异常！");
		}
	}
	
	/**
	 * 发布流程草稿
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void publishFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String flowKey = HttpRequestHelper.getParameter(request, "flowKey");
		if(flowKey == null){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程标识不能为空！");
			return;
		}
		
		//发布流程
		try {
			flowConfigBiz.txPublishFlowDraft(flowKey);
			XMLResponse.outputStandardResponse(response, 1, "发布成功！");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (WfmException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统异常！");
		}
		
	}

	/**
	 * 初始化查看流程详情
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initViewFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = "/page/system/flowconf/view_flow.jsp";
		request.getRequestDispatcher(page).forward(request, response);
	}
	
	


	/**
	 * 	获取流程类别列表
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getFlowClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, DataDictInfo> dmap = SysCodeDictLoader.getInstance().getFlowClass();
		TreeSet<DataDictInfo> flowClass = null;
		if (dmap != null) {
			flowClass = new TreeSet<DataDictInfo>(dmap.values());
		}
		HTMLResponse.outputHTML(response, new DataDictionarySelect(flowClass).toString());
	}
	
	/**
	 * 获取元数据定义XML文件
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void getMetaDefineXml(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String flowCategory = HttpRequestHelper.getParameter(request, "flowCategory");
		if(flowCategory == null){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：流程类别不能为空！");
			return;
		}
		
		List<FlowMeta> metaList = flowMetaBiz.getByFlowCategory(flowCategory);
		XMLResponse.outputXML(response, new MetaDefineXml(metaList).toDocument());
	}
	
	/**
	 * 验证表达式
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void validExpression(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String metaXml = HttpRequestHelper.getParameter(request, "metaXml");
		if(metaXml == null || "".equalsIgnoreCase(metaXml)){
			XMLResponse.outputStandardResponse(response, 0, "非法参数：提交的表达式XML不能为空！");
			return;
		}
		
		if( ExpressionEvaluator.isValidExpression(metaXml)){
			XMLResponse.outputStandardResponse(response, 1, "表达式验证成功！");
		}else{
			XMLResponse.outputStandardResponse(response, 0, "表达式验证失败！");
		}		
	}
	
}
