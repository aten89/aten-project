package org.eapp.oa.doc.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.doc.blo.IDocClassBiz;
import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.doc.blo.IDocNumberBiz;
import org.eapp.oa.doc.dto.DocFormPage;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.dto.FlowSelect;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.info.blo.IInfoLayoutBiz;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.dto.AttachmentList;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;


public class DocumentDraftCtrl extends HttpServlet {
	
	/**
	 * @author Tim
	 * 2009-May 4, 2009
	 */
	private static final long serialVersionUID = -5372258649409034868L;
	
	private int maxUploadSize;
	private IDocFormBiz docFormBiz;
	private IDocClassBiz docClassBiz;
	private IFlowConfigBiz flowConfigBiz;
	private IDocNumberBiz docNumberBiz;
	private IInfoLayoutBiz infoLayoutBiz;
	/**
     * 通讯录模块中的我的资料子模块的业务逻辑处理接口
     */
//    private IAddressListBiz addressListBiz;
	
	public IDocFormBiz getDocFormBiz() {
		return docFormBiz;
	}

	public void setDocFormBiz(IDocFormBiz docFormBiz) {
		this.docFormBiz = docFormBiz;
	}

	/**
	 * Constructor of the object.
	 */
	public DocumentDraftCtrl() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	public void init(ServletConfig config) throws ServletException {
		try {
			maxUploadSize = Integer.parseInt(config.getInitParameter("maxUploadSize"));
		} catch(Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}
		docFormBiz = (IDocFormBiz)SpringHelper.getBean("docFormBiz");
		docClassBiz = (IDocClassBiz)SpringHelper.getBean("docClassBiz");
		flowConfigBiz = (IFlowConfigBiz)SpringHelper.getBean("flowConfigBiz");
		docNumberBiz = (IDocNumberBiz) SpringHelper.getBean("docNumberBiz");
		infoLayoutBiz = (IInfoLayoutBiz) SpringHelper.getBean("infoLayoutBiz");
		/**
         * 通讯录模块中的我的资料子模块的业务逻辑处理接口
         */
//        addressListBiz = (IAddressListBiz) SpringHelper.getSpringContext().getBean("addressListBiz");
	}



	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询公文草稿
			queryDocument(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//初始化公文新增页面
			initAddDoc(request, response);
			return;
		} else if("initmodify".equalsIgnoreCase(action)){
			//初始化公文修改页面
			initModifyDoc(request, response);
			return;
		} else if(SysConstants.DELETE.equalsIgnoreCase(action)){
			//删除公文
			deleteDoc(request, response);
			return;
		} else if ("savedraftdoc".equalsIgnoreCase(action)) {
			//保存信息主体
			saveDraftDoc(request, response);
			return;			
		} else if ("uploadcontent".equalsIgnoreCase(action)) {
			//上传内容DOC
			uploadContentDoc(request, response);
			return;
		} else if("getcontent".equalsIgnoreCase(action)){
			//取得内容DOC
			getContentDoc(request, response);
			return;
		} else if("uploadhtml".equalsIgnoreCase(action)){
			//上传内容DOC生成的HTML页面
			uploadHtmlPage(request, response);
			return;
		} else if("upload".equalsIgnoreCase(action)){
			//上传附件
			uploadAttachments(request, response);
			return;
		} else if("getfiles".equalsIgnoreCase(action)){
			//取得附件列表
			getAttachments(request, response);
			return;
		} else if("chackdocno".equalsIgnoreCase(action)){
			//检查文档编号
			checkDocNomber(request, response);
			return;
		} else if("savefinal".equalsIgnoreCase(action)){
			//保存正式文档
			saveFinalDoc(request, response);
			return;
		} else if ("initflowselect".equalsIgnoreCase(action)) {
			//初始化流程选择
			initFlowSelect(request, response);
			return;
		}else if ("flowselect".equalsIgnoreCase(action)) {
			//流程选择
			flowSelect(request, response);
			return;
		}  else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//提交并启动流程
			publishOrStartFlow(request, response);
			return;
		}  else if("initinfolayoutselect".equalsIgnoreCase(action)){
			//选择公文发布版块页面
			initInfoLayout(request, response);
			return;
		}  
		
	}
	
	
	public void queryDocument(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<DocForm> list = docFormBiz.getDocForm(user.getAccountID(), DocForm.STATUS_UNPUBLISH, DocForm.DIS_FILE);
		ListPage<DocForm> page = new ListPage<DocForm>();
		page.setDataList(list);
		XMLResponse.outputXML(response,new DocFormPage(page).toDocument());
	}
	
	public void initAddDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String docClassName = HttpRequestHelper.getParameter(request, "doc");
		if (docClassName == null) {
			request.setAttribute("errorMsg", "参数不正确");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
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
		//对该类别的发布权限验证
		DocClass docClass = docClassBiz.getAssignDocClassByName(user.getAccountID(), 
				groupNames, postNames, docClassName, DocClass.DIS_FILE);
		if (docClass == null) {
			request.setAttribute("errorMsg", "没有发布“" + docClassName + "”的权限");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		
		DocForm docForm = new DocForm();
		docForm.setDraftsman(user.getAccountID());
		docForm.setDraftDate(new Date());
		docForm.setDocClassName(docClassName);
		
		request.setAttribute("docForm", docForm);
		request.setAttribute("groups", user.getGroups());	
		request.setAttribute("docClassUrl", docClass.getBodyTemplateUrl());
		Map<String, DataDictInfo> securityMap = SysCodeDictLoader.getInstance().getDocSecurityClass();		
		if (securityMap != null) {
			request.setAttribute("docSecurityClasses", new TreeSet<DataDictInfo>(securityMap.values()));
		}
		Map<String, DataDictInfo> urgencyMap =SysCodeDictLoader.getInstance().getDocUrgency();
		if(urgencyMap != null){
			request.setAttribute("docUrgencys", new TreeSet<DataDictInfo>(urgencyMap.values()));			
		}
		request.getRequestDispatcher("/page/doc/dispatch/draft_doc.jsp").forward(request, response);
	}
	
	public void initModifyDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		DocForm docForm = docFormBiz.getDocFormById(id);
		if (docForm == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		if (!user.getAccountID().equals(docForm.getDraftsman()) ){
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "此消息不是您发布的不允许修改");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("docForm", docForm);
		request.setAttribute("groups", user.getGroups());
		Map<String, DataDictInfo> securityMap = SysCodeDictLoader.getInstance().getDocSecurityClass();		
		if (securityMap != null) {
			request.setAttribute("docSecurityClasses", new TreeSet<DataDictInfo>(securityMap.values()));
		}
		Map<String, DataDictInfo> urgencyMap =SysCodeDictLoader.getInstance().getDocUrgency();
		if(urgencyMap != null){
			request.setAttribute("docUrgencys", new TreeSet<DataDictInfo>(urgencyMap.values()));			
		}
		request.getRequestDispatcher("/page/doc/dispatch/draft_doc.jsp").forward(request, response);
	}
	
	public void deleteDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
        	XMLResponse.outputStandardResponse(response, 0, "id不能为空");
        	return;
        }
		try {
			docFormBiz.deleteDocForm(id, user.getAccountID());
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
		}
		
	}
	
	public void saveDraftDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String docFormId = HttpRequestHelper.getParameter(request, "docFormId");
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String groupName = HttpRequestHelper.getParameter(request, "groupname");
		String toDept = HttpRequestHelper.getParameter(request, "toDept");
		String ccDept = HttpRequestHelper.getParameter(request, "ccDept");
		String docClassName = HttpRequestHelper.getParameter(request, "docClassName"); 
		String docSecurity = HttpRequestHelper.getParameter(request, "docSecurity");
		String docUrgency = HttpRequestHelper.getParameter(request, "docUrgency");
		String signGroupNames = HttpRequestHelper.getParameter(request, "signGroupNames");
		
		if (subject == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			DocForm docForm = docFormBiz.addOrModifyDocForm(docFormId, user.getAccountID(), subject,  groupName, toDept, ccDept, docClassName, docSecurity, docUrgency, signGroupNames,DocForm.DIS_FILE);
			XMLResponse.outputStandardResponse(response, 1, docForm.getId());
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void uploadContentDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload,"referid");
            if (referId == null) {
            	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
            	return;
            }
            
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList == null || fileList.isEmpty()) {
            	XMLResponse.outputStandardResponse(response, 0, "文件不存在");
        		return;
            }
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
        	String dir = SysConstants.DOC_ATTACHMENT_DIR + sdf.format(new Date());
        	FileItem file = fileList.get(0);
        	String fileName = fileUpload.getFileName(file);
        	if (fileName == null || fileName.trim().equals("")) {
        		XMLResponse.outputStandardResponse(response, 0, "文件不存在");
        		return;
        	}
        	Attachment am = new Attachment(fileName, file.getSize());
            String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
            am.setFilePath(path);
            	
            //保存附件
            FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
            docFormBiz.addContentDoc(referId, am);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
        	XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024+ "K!");
        } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
	}
	
	private void getContentDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String referId = HttpRequestHelper.getParameter(request, "referid");
		 if (referId == null) {
        	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
        	return;
        }
		Attachment am = docFormBiz.getContentDoc(referId);
		List<Attachment> ams = new ArrayList<Attachment>();
		ams.add(am);
		XMLResponse.outputXML(response, new AttachmentList(ams).toDocument());
	}
	
	@SuppressWarnings("unchecked")
	private void uploadHtmlPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload,"referid");
            String indexFileName = FileUploadHelper.getParameter(fileUpload,"filename");
            String infoLayoutIdStr = FileUploadHelper.getEncodedParameter(fileUpload, "infoLayout", "utf-8");
            String[] layouts = null;
            if (infoLayoutIdStr != null) {
            	layouts = infoLayoutIdStr.split(",");
            }
           
            if (referId == null || indexFileName == null) {
            	XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            	return;
            }
            
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList != null && !fileList.isEmpty()) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            	String dir = SysConstants.DOC_HTMLPAGE_DIR + sdf.format(new Date()) + SerialNoCreater.createUUID() + "/";
            	for (FileItem file : fileList) {
            		String fileName = fileUpload.getFileName(file);
            		if (fileName == null || fileName.trim().equals("")) {
            			continue;
            		}
                	String path = dir + fileName;
                	//保存附件
                	FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
  
            	}
            	
            	docFormBiz.txUpdateContentUrl(referId, dir , indexFileName, layouts);
            }
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
        	XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024+ "K!");
        } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
	}
	
	private void getAttachments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String referId = HttpRequestHelper.getParameter(request, "referid");
		 if (referId == null) {
         	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
         	return;
         }
		 Set<Attachment> ams = docFormBiz.getInfoAttachments(referId);
		
		XMLResponse.outputXML(response, new AttachmentList(ams).toDocument());
	}
	
	@SuppressWarnings("unchecked")
	private void uploadAttachments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload,"referid");
            if (referId == null) {
            	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
            	return;
            }
            
            String[] deletedFiles = FileUploadHelper.getParameters(fileUpload,"delFilenames");
            if(deletedFiles != null){
            	for(int fileIndex = 0; fileIndex < deletedFiles.length; fileIndex++){
            		if(deletedFiles[fileIndex] != null && !"".equals(deletedFiles[fileIndex])){
            			deletedFiles[fileIndex] = new String(deletedFiles[fileIndex].getBytes("iso-8859-1"),"utf-8");
            		}
            	}
            }
            
            List<Attachment> files = new ArrayList<Attachment>();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList != null && !fileList.isEmpty()) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            	String dir = SysConstants.DOC_ATTACHMENT_DIR + sdf.format(new Date());
            	for (FileItem file : fileList) {
            		String fileName = fileUpload.getFileName(file);
            		if (fileName == null || fileName.trim().equals("")) {
            			continue;
            		}
            		Attachment am = new Attachment(fileName, file.getSize());
                	String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
                	am.setFilePath(path);
                	
                	//保存附件
                	FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
                	
                	files.add(am);
            	}
            }
            
            docFormBiz.txUpdateAttachment(referId, deletedFiles, files);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
        	XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024+ "K!");
        } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
	}
	
	private void initFlowSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docClassName = HttpRequestHelper.getParameter(request, "docclass");
		if (docClassName == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "参数为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("docClassName", docClassName);
		request.getRequestDispatcher("/page/doc/dispatch/dialog_flow.jsp").forward(request, response);
	}
	
	private void flowSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docClassName = HttpRequestHelper.getParameter(request, "docclass");
		if (docClassName == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "参数为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		DocClass docClass = docClassBiz.getDocClassByName(docClassName);	
		if (docClass != null && docClass.getFlowClass() != null) {
			FlowConfigQueryParameters flowConfigQP = new FlowConfigQueryParameters();
			flowConfigQP.setPageSize(QueryParameters.ALL_PAGE_SIZE);
			flowConfigQP.setFlowClass(docClass.getFlowClass());
			flowConfigQP.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);//只查询为已发布流程
			
			flowConfigQP.addOrder("flowClass", true);
			flowConfigQP.addOrder("flowName", true);
	
			ListPage<FlowConfig> page = flowConfigBiz.queryFlow(flowConfigQP, true);
			if (page != null) {
				request.setAttribute("flows", page.getDataList());
				XMLResponse.outputXML(response, new FlowSelect(page).toDocument());
				return;
			}
		}
		XMLResponse.outputStandardResponse(response, 0, "无数据！");
	}
	
	public void publishOrStartFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String id = HttpRequestHelper.getParameter(request, "id");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "flag", false);
		String flowKey = HttpRequestHelper.getParameter(request, "flowkey");
		String signGroupNames = HttpRequestHelper.getParameter(request, "signGroupNames");
		String users = HttpRequestHelper.getParameter(request, "users");
//		String executionTime =  HttpRequestHelper.getParameter(request,"endTime");
//		String executionClass = null;
//		String endTime = null;
//		
//		if(	executionTime!=null){
//			endTime = DataFormatUtil.formatTime(executionTime);
//			DataDictionaryDTO dto = SysCodeDictLoader.getInstance().getExecutionClassByKey(SysConstants.MODULE_VARNAME_COMMONFILE);
//			executionClass = dto.getDictValue();
//		}
//		
		if (isStartFlow && flowKey == null || id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
        	return;
		}
		try {
			DocForm docForm = docFormBiz.txStartFlow(id, isStartFlow ? flowKey : null,signGroupNames,users);
			//写入日志
			if (docForm != null) {
				ActionLogger.log(request, docForm.getId(), docForm.toString());
			}
			//发送邮件通知
//			if(users!=null && executionTime!=null){
//				String toAddress = "";
//				String [] user = users.split(",");
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
//				
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
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "操作失败");
		}
	}
	
	public void initInfoLayout(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}

//		List<DataDictionaryDTO> rootList = SysCodeDictLoader.getInstance().getInfoClass();
		List<InfoLayout> infoLayout = infoLayoutBiz.getAllInfoLayout();
		request.setAttribute("infoLayout", infoLayout);
		request.getRequestDispatcher("/page/doc/dispatch/dialog_ilayout.jsp").forward(request, response);
	}
	
	public void checkDocNomber(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		String headerStr = HttpRequestHelper.getParameter(request, "headerStr");
		List<DocForm> headerLst = docFormBiz.getDocFormByDocNum(headerStr);
        if(headerLst != null && headerLst.size()>0){
        	XMLResponse.outputStandardResponse(response, 0, "公文字号已经存在");
        	return;
        }
        XMLResponse.outputStandardResponse(response, 1, "公文字号可以使用");
	}
	
	public void saveFinalDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload,"referid");
            String headerTmpId = FileUploadHelper.getParameter(fileUpload,"headerTmpId");
            String docNumStr = FileUploadHelper.getParameter(fileUpload,"docNum");
            String yearStr = FileUploadHelper.getParameter(fileUpload,"year");
            String headerStr = FileUploadHelper.getEncodedParameter(fileUpload, "headerStr", "utf-8");
            if (referId == null) {
            	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
            	return;
            }
            
            List<DocForm> headerLst = docFormBiz.getDocFormByDocNum(headerStr);
            if(headerLst != null && headerLst.size()>0){
            	XMLResponse.outputStandardResponse(response, 0, "公文字号已经存在");
            	return;
            }
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList == null || fileList.isEmpty()) {
            	XMLResponse.outputStandardResponse(response, 0, "文件不存在");
        		return;
            }
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
        	String dir = SysConstants.DOC_ATTACHMENT_DIR + sdf.format(new Date());
        	FileItem file = fileList.get(0);
        	String fileName = fileUpload.getFileName(file);
        	if (fileName == null || fileName.trim().equals("")) {
        		XMLResponse.outputStandardResponse(response, 0, "文件不存在");
        		return;
        	}
        	Attachment am = new Attachment(fileName, file.getSize());
            String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
            am.setFilePath(path);           	
            //保存附件
            FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
           
            if (headerTmpId != null) {//选择了红头模板
                int docNum = Integer.parseInt(docNumStr);
                int year = Integer.parseInt(yearStr);
                //更新红头模板的值
                docNumberBiz.txUpdateOrder(headerTmpId, docNum + 1, year);
            } else {//未选择红头模板，之前必须已套过红头
            	headerStr = null;
            }
            docFormBiz.addFinalContentDoc(referId, am, headerStr);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
        	XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024+ "K!");
        } catch (OaException e) {
        	XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
	}
}
