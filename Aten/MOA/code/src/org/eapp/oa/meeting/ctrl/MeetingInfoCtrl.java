package org.eapp.oa.meeting.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.eapp.comobj.SessionAccount;
import org.eapp.oa.kb.dto.RichTextImageSelList;
import org.eapp.oa.meeting.blo.IMeetingInfoBiz;
import org.eapp.oa.meeting.blo.IMeetingRoomBiz;
import org.eapp.oa.meeting.dto.MeetingInfoXml;
import org.eapp.oa.meeting.dto.MeetingParticipantList;
import org.eapp.oa.meeting.hbean.MeetingAttachment;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dto.AttachmentList;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

/**
 * 
 * 
 * @author sds
 * @version Jul 13, 2009
 */
public class MeetingInfoCtrl extends HttpServlet {

	private static final long serialVersionUID = 926036968132893321L;

	private int maxUploadSize;

	private IMeetingInfoBiz meetingInfoBiz;
	private MeetingRoomCtrl meetingRoomCtrl;
	private IMeetingRoomBiz meetingRoomBiz;
	
	
	/**
	 * Constructor of the object.
	 */
	public MeetingInfoCtrl() {
		super();
		meetingInfoBiz = (IMeetingInfoBiz) SpringHelper.getSpringContext().getBean("meetingInfoBiz");
		meetingRoomBiz = (IMeetingRoomBiz) SpringHelper.getSpringContext().getBean("meetingRoomBiz");
		meetingRoomCtrl = new MeetingRoomCtrl();
		try {
			meetingRoomCtrl.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			String str = config.getInitParameter("maxUploadSize");
			if(str != null) {
				maxUploadSize = Integer.parseInt(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}
		
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

		String act = HttpRequestHelper.getParameter(request, "act");
		if("toReservePage".equalsIgnoreCase(act)) {
			//转向会议预订页面
			toReservePage(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(act)) {
			// 预订会议
			saveMeetingInfo(request, response);
			return;
		} else if("toModifyPage".equalsIgnoreCase(act)) {
			//转向会议变更页面
			toModifyPage(request, response);
			return;
		} else if("getParticipants".equalsIgnoreCase(act)) {
			//加载参会人员
			getParticipants(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			// 点击详情查看会议信息
			viewMeetingDetail(request, response);
		} else  if("uploadAttachments".equalsIgnoreCase(act)) {
			//上传附件,包括会议资料附件、会议纪要附件
			uploadAttachments(request, response);
		} else if("getattachments".equalsIgnoreCase(act)){
			//取得附件列表
			getAttachments(request, response);
			return;
		} else if("toCancelPage".equalsIgnoreCase(act)) {
			//转向会议取消页面
			toCancelPage(request, response);
			return;
		} else if ("cancelMeeting".equalsIgnoreCase(act)) {
			// 取消会议
			delMeetingInfo(request, response);
			return;
		} else if("toMinutePage".equalsIgnoreCase(act)) {
			//转向会议纪要页面
			toMinutePage(request, response);
			return;
		} else if("sendMinutes".equalsIgnoreCase(act)) {
			//发送会议纪要
			sendMinutes(request, response);
			return;
		} else if("getMeetingInfo".equalsIgnoreCase(act)) {
			//取得会议预订信息
			getMeetingInfo(request, response);
			return;
		} else if("uploadimage".equals(act)){
			//上传内容图片
			uploadImages(request, response);
			return;	
		} else if("meetimagelist".equals(act)){
			//内容图片列表
			meetingImageList(request, response);
			return;
		} else if("delimage".equals(act)){
			//删除内容图片
			delMeetingImage(request, response);
			return;
//		} else if("meetingroomdevice".equals(act)){
//			//会议设备列表
//			meetingRoomDevice(request, response);
//			return;
		} else if("savemeetingcontent".equals(act)){
			//保存会议内容
			saveMeetingContent(request, response);
			return;
		} else if("checkmeetingroom".equals(act)){
			//检查会议冲突
			checkMeetingRoom(request, response);
			return;
		} else if("meetroomlist".equals(act)){
			meetingRoomList(request, response);
			return;
		}
	}

	private void toReservePage(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		// 获取登录用户信息
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String roomId = HttpRequestHelper.getParameter(request, "roomId");
		String defaultOrderDate = DataFormatUtil.formatTime(request.getParameter("orderDate"));
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		Date reserveTime = new Date();
		MeetingInfo meetingInfo = new MeetingInfo();
		meetingInfo.setApplyMan(user.getAccountID());
		meetingInfo.setGroupName(user.getGroupNames());
		meetingInfo.setReserveTime(reserveTime);		
		request.setAttribute("meetingInfo", meetingInfo);
		request.setAttribute("roomId", roomId);
		request.setAttribute("areaCode", areaCode);
		if (defaultOrderDate != null) {
			request.setAttribute("defaultOrderDate", Timestamp.valueOf(defaultOrderDate));//格式化时间
		} else {
			request.setAttribute("defaultOrderDate", new Timestamp(System.currentTimeMillis()));//格式化时间
		}
		request.getRequestDispatcher("/page/meeting/order/edit_meeting.jsp").forward(request, response);
		
	}
	
	private void saveMeetingInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String userAccountId = user.getAccountID();
		String groupName = user.getGroupNames();
		String meetingInfoId = HttpRequestHelper.getParameter(request, "id");
		String roomId = HttpRequestHelper.getParameter(request, "roomId");
		String beginTimeStr = DataFormatUtil.formatTime(request.getParameter("beginTime"));
		String endTimeStr = DataFormatUtil.formatTime(request.getParameter("endTime"));
		String theme = HttpRequestHelper.getParameter(request, "theme");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String[] persons = HttpRequestHelper.getParameters(request, "person");
		String[] devices = null;
		if(roomId == null || theme == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		if(beginTimeStr == null || endTimeStr == null) {
			XMLResponse.outputStandardResponse(response, 0, "会议时间不正确");
			return;
		}
		MeetingRoom meetingRoom = meetingRoomBiz.getMeetingRoomById(roomId);
		if(meetingRoom==null){
			XMLResponse.outputStandardResponse(response, 0, "预定的会议室不存在");
			return;
		}
		String remarkStr="";
		if( meetingRoom.getRemark()!=null && meetingRoom.getRemark()!=""){
			remarkStr=meetingRoom.getRemark()+"，";
		}
		if(!meetingRoom.getStatus()){
			XMLResponse.outputStandardResponse(response, 0, remarkStr+"该会议室不能预订！");
			return;
		}
		Date beginTime = Timestamp.valueOf(beginTimeStr);
		Date endTime = Timestamp.valueOf(endTimeStr);
		
		//先判断一下，看看会议室在时间上是否冲突
		//原来仅仅在页面上进行判断，这是不够的。当两个人同时预订会议时，页面上是不会提示冲突的。 
		List<MeetingInfo> list = meetingInfoBiz.getMeetingInfos(meetingInfoId, roomId, (Timestamp)beginTime, (Timestamp)endTime);
		boolean roomFlag = false;
		if(list!=null && !list.isEmpty()){
			roomFlag = true;
		}
		if(roomFlag){
			XMLResponse.outputStandardResponse(response, 0, "该会议室在“" + beginTimeStr + "”到“" + endTimeStr + "”之间已经被预订，请修改会议时间。");
			return;
		}
		
		//不冲突的情况下，我们再来处理预订的工作
		try {
			MeetingInfo meeting = meetingInfoBiz.txSaveMeetingInfo(meetingInfoId, roomId, userAccountId, groupName, theme, beginTime, 
					endTime, remark, devices,persons);
			XMLResponse.outputXML(response , new MeetingInfoXml(meeting).toDocument());
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void toModifyPage(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			request.setAttribute("errorMsg", "会议ID为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			MeetingInfo meetingInfo = meetingInfoBiz.getMeetingInfoById(id);
			request.setAttribute("meetingInfo", meetingInfo);
			MeetingRoom meetingRoom = meetingInfo.getMeetingRoom();
			if(null != meetingRoom){
				request.setAttribute("roomId", meetingRoom.getId());
			}
			if(null != meetingRoom){
				request.setAttribute("areaCode", meetingRoom.getAreaCode());
			}
			request.getRequestDispatcher("/page/meeting/order/edit_meeting.jsp").forward(request, response);
		} catch (OaException e) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
		
		
	}
	
	private void getParticipants(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			XMLResponse.outputStandardResponse(response, 0, "id不能为空");
        	return;
		}
		try {
			Collection<MeetingParticipant> mps = meetingInfoBiz.getParticipants(id);
			XMLResponse.outputXML(response , new MeetingParticipantList(mps).toDocument());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
	       	e.printStackTrace();
	       	XMLResponse.outputStandardResponse(response, 0, "系统错误");
	    }
	}

	protected void viewMeetingDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		// 获取登录用户信息
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			MeetingInfo meetingInfo = meetingInfoBiz.getMeetingInfoById(id);
			if(meetingInfo == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "会议信息不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("isParticipant", meetingInfo.isParticipant(user.getAccountID()));
			request.setAttribute("meetingInfo", meetingInfo);

			request.getRequestDispatcher("/page/meeting/order/view_meeting.jsp").forward(request, response);
		} catch(OaException oe){
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, oe.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	private void uploadAttachments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		int attachType = HttpRequestHelper.getIntParameter(request, "attachType", MeetingAttachment.TYPE_ATTACH);
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
            	String dir = SysConstants.MEET_ATTACHMENT_DIR + sdf.format(new Date());
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
            
			meetingInfoBiz.txUpdateAttachment(referId, deletedFiles, files, attachType);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (SizeLimitExceededException e) {
			XMLResponse.outputStandardResponse(response, 0, "上载文件大于"
					+ maxUploadSize / 1024 + "K!");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
		}
	}
	
	private void getAttachments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String referId = HttpRequestHelper.getParameter(request,"referid");
		int attachType = HttpRequestHelper.getIntParameter(request, "attachType", MeetingAttachment.TYPE_ATTACH);
		if (referId == null) {
			XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
        	return;
        }
		
		try{			 
			List<Attachment> list = meetingInfoBiz.getAttachments(referId, attachType);
			XMLResponse.outputXML(response , new AttachmentList(list).toDocument());
		} catch (Exception e) {
	       	e.printStackTrace();
	       	XMLResponse.outputStandardResponse(response, 0, "获取附件失败");
	    }
	}
	
	private void toCancelPage(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "会议ID为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			MeetingInfo meetingInfo = meetingInfoBiz.getMeetingInfoById(id);
			request.setAttribute("meetingInfo", meetingInfo);
			request.getRequestDispatcher("/page/meeting/order/dialog_cancel.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	private void delMeetingInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String content = HttpRequestHelper.getParameter(request, "content");
		boolean sendEmailFlag = HttpRequestHelper.getBooleanParameter(request, "sendEmailFlag", true);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为空");
			return;
		}
		if (subject == null) {
			XMLResponse.outputStandardResponse(response, 0, "邮件标题不能为空");
			return;
		}
		try {
			meetingInfoBiz.txDelMeetingInfo(id, subject, content, sendEmailFlag);
			
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
		
	}
	
	private void toMinutePage(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "会议ID为空");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		try {
			MeetingInfo meetingInfo = meetingInfoBiz.getMeetingInfoById(id);
			request.setAttribute("meetingInfo", meetingInfo);
			request.getRequestDispatcher("/page/meeting/order/dialog_minute.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}	
		
	}
	
	private void sendMinutes(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String subject = HttpRequestHelper.getParameter(request, "subject");
		String content = HttpRequestHelper.getParameter(request, "content");
		if(id == null) {
			XMLResponse.outputStandardResponse(response, 0, "会议ID为空");
			return;
		}
		if (subject == null) {
			XMLResponse.outputStandardResponse(response, 0, "邮件标题不能为空");
			return;
		}
		try {
			meetingInfoBiz.csSendMeetingMinutes(id, subject, content);
			XMLResponse.outputStandardResponse(response, 1, "发送成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "获取会议信息失败");
		} 
		
	}
	
	private void getMeetingInfo(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			XMLResponse.outputStandardResponse(response, 0, "会议ID为空");
			return;
		}
		try {
			MeetingInfo meetingInfo = meetingInfoBiz.getMeetingInfoById(id);
			XMLResponse.outputXML(response , new MeetingInfoXml(meetingInfo).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void uploadImages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
		
        try {
            fileUpload.doParse();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if(fileList != null && !fileList.isEmpty()) {
            	String dir = FileDispatcher.getTempAbsDir();
            	FileItem file = fileList.get(0);
        		String fileName = fileUpload.getFileName(file);
//        		if (fileName == null || fileName.trim().equals("")) {
//        			
//        		}
        		Attachment am = new Attachment(fileName, file.getSize());
        		if(!FileDispatcher.getConfig().isImgExt(am.getFileExt())) {
        			XMLResponse.outputStandardResponse(response, 0, "只能上传图片格式");
        			return;
        		}
        		String newFileName = SerialNoCreater.createUUID();
            	String tempPath = dir + newFileName + am.getFileExt();
            	am.setId(newFileName);
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            	String path = SysConstants.MEET_ATTACHMENT_DIR + sdf.format(new Date())+ newFileName + am.getFileExt();
            	am.setFilePath(path);   	
            	//保存附件
            	FileUtil.saveFile(FileDispatcher.getSaveDir(tempPath), file.getInputStream());
            	Map<String, Attachment> imgMap = new HashMap<String, Attachment>();
            	imgMap =(Map<String, Attachment>)request.getSession().getAttribute(SysConstants.SESSION_MEET_NOTICE_IMG);
            	if(imgMap==null){
            		imgMap = new HashMap<String, Attachment>();       		
            	}
            	imgMap.put(am.getId(), am);
            	request.getSession().setAttribute(SysConstants.SESSION_MEET_NOTICE_IMG,imgMap);  
            }                
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
        	XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024+ "K!");
        } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
	}
	
	@SuppressWarnings("unchecked")
	private void meetingImageList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 String referId = HttpRequestHelper.getParameter(request,"referId");
		 try{		
			 Map<String, Attachment> imgMap = new HashMap<String, Attachment>();
         	 imgMap =(Map<String, Attachment>)request.getSession().getAttribute(SysConstants.SESSION_MEET_NOTICE_IMG);
			 List<Attachment> list = new ArrayList<Attachment>();
			 if (imgMap != null) {
				 list.addAll(imgMap.values());
			 }
			 if(referId !=null){			
				 Collection<Attachment> attas = meetingInfoBiz.getContentAttachments(referId);
				 if (attas != null) {
					 list.addAll(attas);
				 }
			 }
			 XMLResponse.outputXML(response , new RichTextImageSelList(list).toDocument());
		 } catch (Exception e) {
        	e.printStackTrace();
        	XMLResponse.outputStandardResponse(response, 0, "查询失败");
	     }
	}
	
	@SuppressWarnings("unchecked")
	private void delMeetingImage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String imgId = HttpRequestHelper.getParameter(request, "imgId");
		String referId = HttpRequestHelper.getParameter(request,"referid");
		Map<String, Attachment> imgMap =(Map<String, Attachment>)request.getSession().getAttribute(SysConstants.SESSION_MEET_NOTICE_IMG);
		if (imgMap != null) {
			imgMap.remove(imgId);
		}
    	try{
    		if(referId !=null){    			
    			meetingInfoBiz.txDelMeetingContentImg(imgId, referId);
    		}
    		XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (Exception e) {
	       	e.printStackTrace();
	       	XMLResponse.outputStandardResponse(response, 0, "删除失败");
	     }
	}
	
//	private void meetingRoomDevice(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String deviceClass = Device.DEVICECLASS_MEET;
//		String beginStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "beginTime"));
//		String endStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endTime"));
//		if(beginStr == null ||endStr == null ){
//			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
//			return;
//		}
//		String id = HttpRequestHelper.getParameter(request, "id");
//		
//		Timestamp beginTime = Timestamp.valueOf(beginStr);			
//		Timestamp endTime = Timestamp.valueOf(endStr);				
//		List<Device> list = deviceBiz.queryDeviceBorrowInfo(id, deviceClass, beginTime, endTime);
//		MeetingInfo meetingInfo = null;
//		try {
//			if(id !=null){
//				meetingInfo = meetingInfoBiz.getMeetingInfoById(id);				
//			}
//			XMLResponse.outputXML(response, new MeetingDeviceList(list, meetingInfo).toDocument());
//		} catch (OaException e) {
//			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
//		} catch (Exception e) {
//	       	e.printStackTrace();
//	       	XMLResponse.outputStandardResponse(response, 0, "查询失败");
//	    }
//	}

	@SuppressWarnings("unchecked")
	private void saveMeetingContent(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if(id == null) {
			XMLResponse.outputStandardResponse(response, 0, "会议ID为空");
			return;
		}
		String title = HttpRequestHelper.getParameter(request, "title");
		String content = HttpRequestHelper.getParameter(request, "content");
		boolean flag = HttpRequestHelper.getBooleanParameter(request, "flag", false);
		Map<String, Attachment> imgMap =(Map<String, Attachment>)request.getSession().getAttribute(SysConstants.SESSION_MEET_NOTICE_IMG);
		Collection<Attachment> atts = null;
		if (imgMap != null) {
			atts = imgMap.values();
		}

		String base = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort();
		String basePath = base + request.getContextPath() + "/";
		//转换邮件内容中的图片地址
		String emailContent = content.replaceAll("image/upload/meeting", basePath + "image/upload/meeting");
		
		try {
			meetingInfoBiz.txSaveMeetingContent(id, title, content, atts, flag, emailContent);
			request.getSession().removeAttribute(SysConstants.SESSION_MEET_NOTICE_IMG);
			XMLResponse.outputStandardResponse(response, 1, "会议内容保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "会议内容保存失败");
		}
	}
	
	private void checkMeetingRoom(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String roomId = HttpRequestHelper.getParameter(request, "roomId");
		String beginStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "beginTime"));
		String endStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endTime"));
		if(roomId == null || beginStr == null ||endStr==null ){
			XMLResponse.outputStandardResponse(response, 0, "参数为空");
			return;
		}
		Timestamp beginTime = Timestamp.valueOf(beginStr);			
		Timestamp endTime = Timestamp.valueOf(endStr);	
		List<MeetingInfo> list = meetingInfoBiz.getMeetingInfos(id, roomId, beginTime, endTime);
		boolean roomFlag=true;
		String meetingTime="";
		if(list!=null && !list.isEmpty()){
			roomFlag = false;
			MeetingInfo meetingInfo= list.get(0);
			
			meetingTime = "该会议时间与“"+DataFormatUtil.noNullValue(meetingInfo.getBeginTime(),SysConstants.STANDARD_TIME_PATTERN)+" 到 "+DataFormatUtil.noNullValue(meetingInfo.getEndTime(),SysConstants.STANDARD_TIME_PATTERN)+"”冲突！";
		}
		if(roomFlag){
			XMLResponse.outputStandardResponse(response, 1, "会议室在该时间段可预订");
		}else{
			XMLResponse.outputStandardResponse(response, 0, meetingTime);
		}
	}
	
	private void meetingRoomList(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		meetingRoomCtrl.queryMeetingRoom(request, response);
	}

}
