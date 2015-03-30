package org.eapp.oa.info.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;

import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.info.blo.IInfoLayoutBiz;
import org.eapp.oa.info.blo.IInformationBiz;
import org.eapp.oa.info.dto.InfoFormPage;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.hbean.FlowConfig;
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

/**
 * 
 * @author zsy
 * @version Mar 23, 2009
 */
public class InformationDraftCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IInfoFormBiz infoFormBiz;
	private IInformationBiz informationBiz;
	private IInfoLayoutBiz infoLayoutBiz;
	private int maxUploadSize;
	private IFlowConfigBiz flowConfigBiz;
	
	/**
	 * Constructor of the object.
	 */
	public InformationDraftCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			maxUploadSize = Integer.parseInt(config.getInitParameter("maxUploadSize"));
		} catch(Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}
		
		infoFormBiz = (IInfoFormBiz)SpringHelper.getBean("infoFormBiz");
		informationBiz = (IInformationBiz)SpringHelper.getBean("informationBiz");
		infoLayoutBiz = (IInfoLayoutBiz) SpringHelper.getBean("infoLayoutBiz");
		flowConfigBiz = (IFlowConfigBiz) SpringHelper.getBean("flowConfigBiz");
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
			queryInformation(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			initAddInfo(request, response);
			return;
		} else if ("savedraftinfo".equalsIgnoreCase(action)) {
			//保存信息主体
			saveDraftInfo(request, response);
			return;
		} else if ("savehtmlcontent".equalsIgnoreCase(action)) {
			//保存HTML内容
			saveHtmlContent(request, response);
			return;
		} else if ("uploadcontent".equalsIgnoreCase(action)) {
			//上传内容DOC
			uploadContentDoc(request, response);
			return;
		} else if("getcontent".equalsIgnoreCase(action)){
			//取得内容DOC
			getContentDoc(request, response);
			return;
		} else if("upload".equalsIgnoreCase(action)){
			//上传附件
			uploadAttachments(request, response);
			return;
		} else if("getfiles".equalsIgnoreCase(action)){
			//取得附件列表
			getAttachments(request, response);
			return;
		} else if("uploadhtml".equalsIgnoreCase(action)){
			//上传内容DOC生成的HTML页面
			uploadHtmlPage(request, response);
			return;
		} else if ("initflowselect".equalsIgnoreCase(action)) {
			//初始化流程选择
			initFlowSelect(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//提交并启动流程
			publishOrStartFlow(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			initModifyInfo(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			deleteInfo(request, response);
			return;
		
		}
	}
	
	private void queryInformation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<InfoForm> list = infoFormBiz.getInfoForm(user.getAccountID(), Information.STATUS_UNPUBLISH);
		ListPage<InfoForm> page = new ListPage<InfoForm>();
		page.setDataList(list);
		XMLResponse.outputXML(response,new InfoFormPage(page).toDocument());
	}
	
	private void initAddInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String layout = HttpRequestHelper.getParameter(request, "layout");
		if (layout == null) {
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
		//对该版块的发布权限验证
		InfoLayout infoLayout = infoLayoutBiz.getAssignLayoutByName(user.getAccountID(), 
				groupNames, postNames, layout, InfoLayoutAssign.FLAG_PUBLISH);
		if (infoLayout == null) {
			request.setAttribute("errorMsg", "没有发布“" + layout + "”的权限");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		Information info = new Information();
		info.setInfoLayout(layout);
		info.setDraftsMan(user.getAccountID());
		info.setDraftDate(new Date());
		InfoForm infoForm = new InfoForm();
		infoForm.setInformation(info);
		request.setAttribute("infoForm", infoForm);
		request.setAttribute("groups", user.getGroups());
//		request.setAttribute("infoClasses", SysCodeDictLoader.getInstance().getSubInfoClass(layout));
//		Map<String, DataDictionaryDTO> dict = SysCodeDictLoader.getInstance().getInfoSubjectColor();
//		if (dict != null) {
//			request.setAttribute("subjectColor", new TreeSet<DataDictionaryDTO>(dict.values()));
//		}
		request.getRequestDispatcher("/page/info/approval/draft_info.jsp").forward(request, response);
	}
	
	private void saveDraftInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String infoFormId = HttpRequestHelper.getParameter(request, "id");
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String subjectColor = HttpRequestHelper.getParameter(request, "subjectcolor");
		String infoLayout = HttpRequestHelper.getParameter(request, "layout");
		String infoClass = HttpRequestHelper.getParameter(request, "infoclass");
		String groupName = HttpRequestHelper.getParameter(request, "groupname");
		int displayMode = HttpRequestHelper.getIntParameter(request, "displaymode", Information.DISPLAYMODE_URL);
		String content = HttpRequestHelper.getParameter(request, "content");
		if (subject == null || infoLayout == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			InfoForm infoForm = infoFormBiz.addOrModifyInfoForm(infoFormId, user.getAccountID(), subject, subjectColor, infoLayout, infoClass, groupName, displayMode, content);
			XMLResponse.outputStandardResponse(response, 1, infoForm.getId());
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
		}
		
	}
	
	private void saveHtmlContent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String infoFormId = HttpRequestHelper.getParameter(request, "id");
		String content = HttpRequestHelper.getParameter(request, "content");
		try {
			infoFormBiz.txSaveContent(infoFormId, content);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
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
            String referId = FileUploadHelper.getParameter(fileUpload, "referid");
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
        	String dir = SysConstants.INFO_ATTACHMENT_DIR + sdf.format(new Date());
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
            infoFormBiz.addContentDoc(referId, am);
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
		String referId = request.getParameter("referid");
		 if (referId == null) {
        	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
        	return;
        }
		Attachment am = infoFormBiz.getContentDoc(referId);
		List<Attachment> ams = new ArrayList<Attachment>();
		ams.add(am);
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
            	String dir = SysConstants.INFO_ATTACHMENT_DIR + sdf.format(new Date());
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
            
            informationBiz.txUpdateAttachment(referId, deletedFiles, files);
            
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
		String referId = request.getParameter("referid");
		 if (referId == null) {
         	XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
         	return;
         }
		 Set<Attachment> ams = informationBiz.getInfoAttachments(referId);
		
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
            
            if (referId == null || indexFileName == null) {
            	XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            	return;
            }
            
            //删除旧的文件
            informationBiz.deleteContentFile(referId);
            
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList != null && !fileList.isEmpty()) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            	String dir = SysConstants.INFO_HTMLPAGE_DIR + sdf.format(new Date()) + referId + "/";
            	for (FileItem file : fileList) {
            		String fileName = fileUpload.getFileName(file);
            		if (fileName == null || fileName.trim().equals("")) {
            			continue;
            		}
                	String path = dir + fileName;
                	//保存附件
                	FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
  
            	}
            	informationBiz.txUpdateContentUrl(referId, dir + indexFileName);
            }
            
            
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
		String layout = HttpRequestHelper.getParameter(request, "layout");
		if (layout == null) {
			request.setAttribute("errorMsg", "参数为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		InfoLayout infoLayout = infoLayoutBiz.getLayoutByName(layout);
		if (infoLayout != null && infoLayout.getFlowClass() != null) {
			FlowConfigQueryParameters flowConfigQP = new FlowConfigQueryParameters();
			flowConfigQP.setPageSize(QueryParameters.ALL_PAGE_SIZE);
			flowConfigQP.setFlowClass(infoLayout.getFlowClass());
			flowConfigQP.setDraftFlag(FlowConfig.FLOW_FLAG_PUBLISHED);//只查询为已发布流程
			
			flowConfigQP.addOrder("flowClass", true);
			flowConfigQP.addOrder("flowName", true);
	
			ListPage<FlowConfig> page = flowConfigBiz.queryFlow(flowConfigQP, true);
			if (page != null) {
				request.setAttribute("flows", page.getDataList());
			}
		}
		request.setAttribute("noFlowClass", infoLayout == null || infoLayout.getFlowClass() == null);
//		else{
//			//如果没有绑定流程就显示直接发布
//			List<FlowConfig> list = new ArrayList<FlowConfig>();
//			FlowConfig flowConfig =new FlowConfig();
//			flowConfig.setFlowName("直接发布");
//			flowConfig.setFlowKey("DEFAULT_VALUE_ATONCE");
//			list.add(flowConfig);
//			request.setAttribute("flows", list);
//		}
		request.getRequestDispatcher("/page/info/approval/dialog_flow.jsp").forward(request, response);
	}
	
	public void publishOrStartFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String id = HttpRequestHelper.getParameter(request, "id");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "flag", false);
		String flowKey = HttpRequestHelper.getParameter(request, "flowkey");

		if (isStartFlow && flowKey == null || id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
        	return;
		}
		try {
			InfoForm infoForm = infoFormBiz.txStartFlow(id, isStartFlow ? flowKey : null);
			//写入日志
			if (infoForm != null) {
				ActionLogger.log(request, infoForm.getId(), infoForm.toString());
				XMLResponse.outputStandardResponse(response, 1, "操作成功");
			}else{
				XMLResponse.outputStandardResponse(response, 0, "该文档已经被删除!");
			}
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "操作失败");
		}
	}
	
	private void initModifyInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		InfoForm infoForm = infoFormBiz.getInfoFormById(id);
		if (infoForm == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		if (!user.getAccountID().equals(infoForm.getInformation().getDraftsMan())) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "此消息不是您发布的不允许修改");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("infoForm", infoForm);
		request.setAttribute("groups", user.getGroups());
//		request.setAttribute("infoClasses", SysCodeDictLoader.getInstance().getSubInfoClass(infoForm.getInformation().getInfoLayout()));
//		Map<String, DataDictionaryDTO> dict = SysCodeDictLoader.getInstance().getInfoSubjectColor();
//		if (dict != null) {
//			request.setAttribute("subjectColor", new TreeSet<DataDictionaryDTO>(dict.values()));
//		}
		request.getRequestDispatcher("/page/info/approval/draft_info.jsp").forward(request, response);
	}
	
	private void deleteInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
        	XMLResponse.outputStandardResponse(response, 0, "id不能为空");
        	return;
        }
		try {
			infoFormBiz.deleteInfoForm(id, user.getAccountID());
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
		}
	}
}
