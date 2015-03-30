package org.eapp.oa.doc.ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.doc.dto.DocFormList;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.ctrl.TaskDealCtrl;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;


/**
 * @author Tim
 * @version 2009-May 13, 2009
 * 公文处理
 */
public class CommonFileDealCtrl extends HttpServlet {
	
	private static final long serialVersionUID = -1219757119019364618L;
	private IDocFormBiz docFormBiz;
	private ITaskBiz taskBiz;
	private TaskDealCtrl taskDealCtrl;
//	private IPeriodTaskBiz periodTaskBiz;
	/**
     * 通讯录模块中的我的资料子模块的业务逻辑处理接口
     */
//    private IAddressListBiz addressListBiz;

	public CommonFileDealCtrl() {
		super();
	}
	
	public void init() throws ServletException {
		// Put your code here
		docFormBiz = (IDocFormBiz)SpringHelper.getBean("docFormBiz");
		taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
//		periodTaskBiz = (IPeriodTaskBiz) SpringHelper.getSpringContext().getBean("periodTaskBiz");
		/**
         * 通讯录模块中的我的资料子模块的业务逻辑处理接口
         */
//        addressListBiz = (IAddressListBiz) SpringHelper.getSpringContext().getBean("addressListBiz");
		taskDealCtrl = new TaskDealCtrl();
		taskDealCtrl.init();
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
			//查询待处理的公文
			queryDealingDoc(request, response);
		} else if (SysConstants.DISPOSE.equalsIgnoreCase(action)) {
			//转到任务绑定的URL,并认领
			turnToViewPage(request, response);
		} else if ("view_approve".equalsIgnoreCase(action)) {
			//任务处理页面——审批
			initDocApprove(request, response);
		} else if ("deal_approve".equalsIgnoreCase(action)) {
			//处理审批公文任务
			dealApprove(request, response);
			return;
		} else if ("rejected_approve".equalsIgnoreCase(action)) {
			//驳回公文任务
			rejectedApprove(request, response);
			return;
//		}else if ("get_comment".equalsIgnoreCase(action)) {
//			//获取审批意见
//			getComment(request, response);
//			return;
//		}else if ("deal_Assign".equalsIgnoreCase(action)) {
//			//处理指定审批
//			dealAssign(request, response);
//			return;
		}
	}
//	private void dealAssign(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		
//		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
//		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
//		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
//		String users = HttpRequestHelper.getParameter(request, "users");//转向
//		if (taskInstanceId == null || transitionName == null) {
//			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
//			return;
//		}
//		if (comment != null) {
//			comment = transitionName + "，" + comment;
//		} else {
//			if(transitionName.equals("审批通过")){
//				comment = transitionName + "，同意!";
//			}else{
//				comment = transitionName;
//			}
//		}
//		
//		try {
//			docFormBiz.txDealApproveAssign(taskInstanceId, comment, transitionName,users);
//			XMLResponse.outputStandardResponse(response, 1, "提交成功");
//		} catch(Exception e) {
//			e.printStackTrace();
//			XMLResponse.outputStandardResponse(response, 0, "提交失败" );
//		}
//		
//	}
//	private void getComment(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		
//		String formId =  HttpRequestHelper.getParameter(request, "id");
//		String nodeName = HttpRequestHelper.getParameter(request, "nodeName");
//		
//		List<Task> tasks = taskBiz.getComment(formId, nodeName);
//		
//		XMLResponse.outputXML(response,new CommentList(tasks).toDocument());
//		
//	}
	private void queryDealingDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<DocForm> docForm = docFormBiz.queryDealingDocFrom(user.getAccountID(),DocForm.COM_FILE);
		XMLResponse.outputXML(response,new DocFormList(docForm).toDocument());
	}
	
	private void turnToViewPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		taskDealCtrl.turnToViewPage(request, response);
	}
	
	private void initDocApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			String id = HttpRequestHelper.getParameter(request, "id");
			String flag = HttpRequestHelper.getParameter(request, "type");
//			//用来记录按钮的动作选项
//			String args =  HttpRequestHelper.getParameter(request, "args");
			String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID,从上一个地址转过来的参数
			if (id == null || taskInstanceId == null) {
				throw new OaException("参数不能为空");
			}
			DocForm docForm = docFormBiz.getDocFormById(id);
			if (docForm == null) {
				throw new OaException("参数不能为空");
			}
			
			List<Task> tasks = docFormBiz.getEndedTasks(id);
			
//			Task t = taskBiz.getByTaskInstanceId(taskInstanceId);
//			
//			if(t!=null){
//				PeriodTask pt = periodTaskBiz.findById(t.getId());
//				if(pt!=null){
//					request.setAttribute("pt", pt);
//				}
//				
//			}
			request.setAttribute("docForm", docForm);
			request.setAttribute("groups", user.getGroups());
			request.setAttribute("tasks", tasks);
			List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
			request.setAttribute("transitions", transitions);

	
			
			String pageUrl = null;
			if ("m".equals(flag)) {
				pageUrl = "/page/doc/common/tview_modify.jsp";
			}else if("r".equals(flag)){
				pageUrl = "/page/doc/common/tview_rejmodify.jsp";
			}else {
				pageUrl = "/page/doc/common/tview_dispose.jsp";
			}
//			if( args!=null ){
//				request.setAttribute("args", args);
//			}else{
//				request.setAttribute("args", "");
//			}
			request.getRequestDispatcher(pageUrl).forward(request, response);
		} catch (OaException e1) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e1.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
		
	}
	
	private void dealApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String appointTo = HttpRequestHelper.getParameter(request, "appointTo");
		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
//		String executionTime =  HttpRequestHelper.getParameter(request,"endTime");
		String docFormId =  HttpRequestHelper.getParameter(request,"docFormId");
//		String executionClass = null;
//		String endTime = null;
//		
//		if(	executionTime!=null){
//			endTime = DataFormatUtil.formatTime(executionTime);
//			DataDictionaryDTO dto = SysCodeDictLoader.getInstance().getExecutionClassByKey(SysConstants.MODULE_VARNAME_COMMONFILE);
//			executionClass = dto.getDictValue();
//		}
		
		
		if (taskInstanceId == null || transitionName == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		if (comment != null) {
			comment = transitionName + "，" + comment;
		} else {
			if(transitionName.equals("审批通过")){
				comment = transitionName + "，同意!";
			}else{
				comment = transitionName;
			}
		}
		try {
			Map<String, String> vars = new HashMap<String, String>();
			vars.put(SysConstants.FLOW_VARNAME_APPOINTTO, appointTo);
			taskBiz.txDealApproveTask(taskInstanceId, comment, transitionName, vars);
			
			DocForm docForm = docFormBiz.getDocFormById(docFormId);
			//写入日志
			if (docForm != null) {
				ActionLogger.log(request, docForm.getId(), docForm.toString());
			}
			//发送邮件通知
//			if(appointTo!=null && executionTime!=null){
//				String toAddress = "";
//				String [] user = appointTo.split(",");
//				if(user.length > 0){
//					for(String s : user){
//					    //2012-04-24修改：取邮箱地址改成从通讯录里面去，没有在用账号拼接邮箱地址
//					    String emailAddr = "";
//					    if (StringUtils.isNotEmpty(s)) {
//                            AddressList addressList = addressListBiz.getByAccountId(s);
//                            if (addressList != null) {
//                                emailAddr = addressList.getUserEmail();
//                            }
//                        }
//					    if (StringUtils.isEmpty(emailAddr)) {
//					        emailAddr = TransformTool.getEmail(s);
//					    }
//						toAddress += emailAddr + ",";
//					}
//				}
//				//得到下一天的时间 modify zhubiyong 2010-7-28
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String toTime = "";
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(Timestamp.valueOf(endTime + " 00:00:00"));
//				cal.add(Calendar.DAY_OF_YEAR, 1);
//				toTime = sdf.format(cal.getTime());
//				
//				String subject = "OA系统提醒：您有一个文件《"+ docForm.getSubject()+"》，要求于" + toTime + "之前办理！";
//				String content = "您在OA系统有一个文件《"+ docForm.getSubject()+"》需要审批，请于" + toTime + "之前办理。如果逾期未办理，系统将自动按照“同意”处理，详情请登录OA系统查阅！<br><br>系统自动发出，请勿回复。";
//				if(toAddress.length()>0){
//					MailMessage msg = new MailMessage(toAddress, subject, content);	
//					JMailProxy.daemonSend(msg);	
//				}
//			}
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败" );
		}
	}
	
	private void rejectedApprove(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");//任务ID
		String comment = HttpRequestHelper.getParameter(request, "comment");//意见
		String transitionName = HttpRequestHelper.getParameter(request, "transition");//转向
		String docFormId = HttpRequestHelper.getParameter(request, "docFormId"); //重新选择部门
		String appointTo = HttpRequestHelper.getParameter(request, "appointTo"); 
		
//		String executionTime =  HttpRequestHelper.getParameter(request,"endTime");
//		String executionClass = null;
//		String endTime = null;
//		
//		if(	executionTime!=null){
//			endTime = DataFormatUtil.formatTime(executionTime);
//			DataDictionaryDTO dto = SysCodeDictLoader.getInstance().getExecutionClassByKey(SysConstants.MODULE_VARNAME_COMMONFILE);
//			executionClass = dto.getDictValue();
//		}
		if (taskInstanceId == null || transitionName == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		if (comment != null) {
			comment = transitionName + "，" + comment;
		} else {
			if(transitionName.equals("审批通过")){
				comment = transitionName + "，同意!";
			}else{
				comment = transitionName;
			}
		}
		try {
			DocForm docForm = docFormBiz.getDocFormById(docFormId);
			
			Map<String, String> vars = new HashMap<String, String>();
			vars.put(SysConstants.FLOW_VARNAME_APPOINTTO, appointTo);
			vars.put(SysConstants.FLOW_VARNAME_GROUPNAME, docForm.getGroupName());
			vars.put(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, docForm.getDocClassName() + "《" + docForm.getSubject() + "》");
			vars.put(SysConstants.FLOW_VARNAME_SIGNGROUPNAMES, docForm.getSignGroupNames());
			taskBiz.txDealApproveTask(taskInstanceId, comment, transitionName, vars);
			
			//写入日志
			if (docForm != null) {
				ActionLogger.log(request, docForm.getId(), docForm.toString());
			}
			//发送邮件通知
//			if(appointTo!=null && executionTime!=null){
//				String toAddress = "";
//				String [] user = appointTo.split(",");
//				if(user.length > 0){
//					for(String s : user){
//					    //2012-04-24修改：取邮箱地址改成从通讯录里面去，没有在用账号拼接邮箱地址
//                        String emailAddr = "";
//                        if (StringUtils.isNotEmpty(s)) {
//                            AddressList addressList = addressListBiz.getByAccountId(s);
//                            if (addressList != null) {
//                                emailAddr = addressList.getUserEmail();
//                            }
//                        }
//                        if (StringUtils.isEmpty(emailAddr)) {
//                            emailAddr = TransformTool.getEmail(s);
//                        }
//                        toAddress += emailAddr + ",";
//					}
//				}
//				//得到下一天的时间 modify zhubiyong 2010-7-28
//				//2012-11-30 将"yyyy-mm-dd" 改为 "yyyy-MM-dd"
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String toTime = "";
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(sdf.parse(endTime));
//				cal.add(Calendar.DATE, 1);
//				toTime = sdf.format(cal.getTime());
//				String subject = "OA系统提醒：您有一个文件《"+ docForm.getSubject()+"》，要求于" + toTime + "之前办理！";
//				String content = "您在OA系统有一个文件《"+ docForm.getSubject()+"》需要审批，请于" + toTime + "之前办理。如果逾期未办理，系统将自动按照“同意”处理，详情请登录OA系统查阅！<br><br>系统自动发出，请勿回复。";
//				if(toAddress.length()>0){
//					MailMessage msg = new MailMessage(toAddress, subject, content);	
//					JMailProxy.daemonSend(msg);	
//				}
//			}
			XMLResponse.outputStandardResponse(response, 1, "提交成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "提交失败" );
		}
	}

}
