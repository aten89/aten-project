package org.eapp.oa.address.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.dom4j.Document;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.address.blo.IAddressListBiz;
import org.eapp.oa.address.dto.AddressListPage;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.JSONHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;


/**
 * 通讯录 所有通讯录
 *
 */
public class AddressListCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6904963295374530562L;
	
	public static final String USERPHOTO_TEMP_PATH = "USERPHOTO_TEMP_PATH";
	
	private IAddressListBiz addressListBiz;

	private int maxUploadSize;
	
	public AddressListCtrl() {
		super();
		addressListBiz = (IAddressListBiz) SpringHelper.getBean("addressListBiz");
	}

	public void init(ServletConfig config) throws ServletException {
		try {
			maxUploadSize = Integer.parseInt(config.getInitParameter("maxUploadSize"));
		} catch(Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}
		
		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("act");
		
		if(SysConstants.QUERY.equals( action ) ){
			//查询
			queryAddrListPage( req, resp );		
			return;
		} else if(SysConstants.VIEW.equals( action ) ){
			//初始化详情查看页面			
			initPage(req, resp, "/page/address/alladdr/view_addr.jsp");
			return;
		} else if("initmodify".equals( action ) ){
			//初始化编辑页面			
			initPage(req, resp, "/page/address/alladdr/edit_addr.jsp");
			return;
		} else if( SysConstants.MODIFY.equals( action ) ){
			//保存或更新
			saveOrUpdateAddrList( req, resp );				
			return;
//		} else if("initusersex".equals(action)){
//			//性别下拉框数据
//			initUserSex(req, resp);
		} else if("uploadhd".equals(action)){
			//上传头像
			uploadHD(req, resp);
		} else if("resetpic".equals(action)){
			//重置图片
			resetPic(req, resp);
//		} else if("uploadaddresslist".equals(action)){
//			//导入通讯录数据
//			uploadAddressList(req, resp);
		} else if(SysConstants.EXPORT.equals(action)){
			//导出数据到CSV
			exportAddressListAsCSV(req, resp);
		}
	}
	
	
	public void destory(){
		super.destroy();
	}
	
	/**
	 * 搜索功能
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	public void queryAddrListPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userDeptName = HttpRequestHelper.getParameter( req, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( req, "userAccountId" );
		
		int pageNo = HttpRequestHelper.getIntParameter(req, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(req, "pagecount", 1);
		
		try {
			List<String> uids = addressListBiz.getUserAccountIdsByDeptAndAccount(userDeptName, queryString);
			List<AddressList> addressList_ = addressListBiz.getAddressListByAccountIds(uids, pageNo, pageSize);
			if(uids != null){
				Document doc = new AddressListPage(addressList_, uids.size(), pageNo, pageSize).toDocument();
				if (!JSONHelper.outputXmlJSON(req, resp, doc)) {//是否输出JSON
					XMLResponse.outputXML(resp, doc);
				}
			}
			
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(resp, 0, e.getMessage());
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(resp, 0, "系统出错");
			return;
		}
	}

	/**
	 * 新增或更新通讯录详情
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void saveOrUpdateAddrList(HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {		
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");//主键		
		String userAccountId = HttpRequestHelper.getParameter(request, "userAccountId");;//帐号id		
		
		String employeeNumber = HttpRequestHelper.getParameter(request, "employeeNumber");//工号		
		String seatNumber = HttpRequestHelper.getParameter(request, "seatNumber");//座位号		
		String userEnterCorpDate_ = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "userEnterCorpDate"));//入司时间
		Date userEnterCorpDate = null;
		if(userEnterCorpDate_ != null){
			userEnterCorpDate = new Date(Timestamp.valueOf(userEnterCorpDate_).getTime());		
		}
		String userMobile = HttpRequestHelper.getParameter(request, "userMobile");//移动电话		
		String userOfficeTel = HttpRequestHelper.getParameter(request, "userOfficeTel");//办公电话		
		String userEmail = HttpRequestHelper.getParameter(request, "userEmail");//E-mail
		String userQQ = HttpRequestHelper.getParameter(request, "userQQ");//QQ		
		String userMSN = HttpRequestHelper.getParameter(request, "userMSN");//用户msn帐号		
		String userNickName = HttpRequestHelper.getParameter(request, "userNickName");//昵称		
		String userSex = HttpRequestHelper.getParameter(request, "userSex");//性别
//		String userPhoto = HttpRequestHelper.getParameter(request, "userPhoto");//照片
//		Character userSex_ = null;
//		if(userSex != null){
//			userSex_ = userSex.toCharArray()[0];
//		}
		String userBirthDate_ = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "userBirthDate"));//出生日期
		Date userBirthDate = null;
		if(userBirthDate_ != null){
			userBirthDate = new Date(Timestamp.valueOf(userBirthDate_).getTime());		
		}
		String userNativePlace = HttpRequestHelper.getParameter( request, "userNativePlace" );//民族		
		String userNation = HttpRequestHelper.getParameter( request, "userNation" );//籍贯		
		String userCommAddr = HttpRequestHelper.getParameter( request, "userCommAddr" );//通讯地址
		String zipCode = HttpRequestHelper.getParameter( request, "zipCode" );//通讯地址邮编
		String userHomeAddr = HttpRequestHelper.getParameter( request, "userHomeAddr" );//家庭住址		
		String userHomeTel = HttpRequestHelper.getParameter( request, "userHomeTel" );//家庭电话		
		String remark = HttpRequestHelper.getParameter( request, "remark" );//备注
		
		//保存头像
		String tempPath = (String)request.getSession().getAttribute(USERPHOTO_TEMP_PATH);
		String path = null;
		if(tempPath != null){
			//重新上传
			String fileName = tempPath.replaceAll("\\\\", "/");  
	        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
	        path = SysConstants.ADDRLIST_HD_DIR + userAccountId + "/" + fileName;
//	        request.setAttribute("saveImgPath" , FileDispatcher.getAbsPath(path));
	        try {
				FileUtil.moveFile(tempPath, FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)));
			} catch (Exception e) {
				e.printStackTrace();
			}
//		} else {
//			path = userPhoto;
		}
    	
		try{
			AddressList addr = addressListBiz.addOrModifyAddressList(id, userAccountId, employeeNumber, 
					seatNumber, userEnterCorpDate, userMobile, 
					userOfficeTel, userEmail, userQQ, 
					userMSN, userNickName, userSex, 
					userBirthDate, userNativePlace, userNation, 
					userCommAddr, zipCode, userHomeAddr, userHomeTel, 
					remark, path);
			request.getSession().removeAttribute(USERPHOTO_TEMP_PATH);
			XMLResponse.outputStandardResponse(response, 1, addr.getId());
		} catch( Exception e){
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param pageUrl
	 * @throws ServletException
	 * @throws IOException
	 */
	public void initPage(HttpServletRequest request, HttpServletResponse response, String pageUrl)
			throws ServletException, IOException{
		String id = HttpRequestHelper.getParameter( request, "id" );//用户帐户id
		String userAccountId = HttpRequestHelper.getParameter( request, "userAccountId" );//用户帐户id
		AddressList addrList = null;
		if (id != null) {
			addrList = addressListBiz.getById(id);
		}
		if(addrList == null){
			//还没有该登录用户的通讯录时，也会有该登录用户的帐户id、姓名、所属机构、职位信息，这些信息通过设置帐户id即可取到
			addrList = new AddressList();
			addrList.setUserAccountId(userAccountId);
		}
		request.setAttribute("addrList", addrList);
		if(addrList.getUserPhoto() != null){
			request.setAttribute("saveImgPath" , addrList.getUserPhotoPath());
		}
		if(pageUrl != null){
			request.getRequestDispatcher( pageUrl ).forward( request, response );
		}
	}	
	
//	/**
//	 * 初始化性别下拉框
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	private void initUserSex(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		try {
//			List<DataDictionaryDTO> list = SysCodeDictLoader.getInstance().getUserSex();
//			HTMLResponse.outputHTML(response, new DataDictionarySelect(list).toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			XMLResponse.outputStandardResponse(response, 0, "系统出错");
//			return;
//		}
//	}
	
	/**
	 * 上传头像
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void uploadHD(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
		//fileUpload.setEncoding("gbk");
		String msg = null;
	    int code = 0;
		try {
			fileUpload.doParse();
			List<FileItem> fileList = fileUpload.getFileDataList();
			if(fileList == null || fileList.isEmpty()) {
				throw new OaException("请选择上传文件");
			}
            //获取列表中的第一个文件
            FileItem file = fileList.get(0);
            if (file.getSize() <= 0) {
            	throw new OaException("文件不存在");
        	}
            String fileName = file.getName();
            int i = fileName.lastIndexOf(".");
            if (i < 0) {
    			throw new OaException("文件格式不对");
    		}
            String ext = fileName.substring(i);
            if (!FileDispatcher.getConfig().isImgExt(ext)) {
            	throw new OaException("只能上传图片格式");
            }
    		String newName = SerialNoCreater.createUUID() + ext;
            String path = FileDispatcher.getTempDir() + newName;
            //保存
            FileUtil.saveFile(path, file.getInputStream());
            
            request.getSession().setAttribute(USERPHOTO_TEMP_PATH, path);
            request.setAttribute("tempImgPath" , FileDispatcher.getTempAbsDir() + newName);
            code = 1;
		} catch (SizeLimitExceededException e) {
			e.printStackTrace();
			msg = "上载文件大于" + maxUploadSize / 1024+ "K!";
		} catch (OaException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		request.setAttribute("msg" , msg);
        request.setAttribute("code" , new Integer(code));
        response.setContentType("text/html;charset=utf-8");
        request.getRequestDispatcher("/page/address/alladdr/upload_hd.jsp").forward(request , response);
	}
	
	/**
	 * 重置图片
	 * @param request
	 * @param response
	 */
	public void resetPic(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute(USERPHOTO_TEMP_PATH);
	}
	
	
	/**
	 * 导出通讯录数据到CSV文件中
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportAddressListAsCSV(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		String selectedAccountIds = HttpRequestHelper.getParameter( request, "selectedAccountIds" );
		
		try {
			List<String> uids = null;
			if(selectedAccountIds != null ){
				uids = Arrays.asList(selectedAccountIds.split(","));
			} else {
				uids = addressListBiz.getUserAccountIdsByDeptAndAccount(userDeptName, queryString);
			}
			
			//创建一个文件名
			String fileName = SerialNoCreater.createUUID() + "/oa.csv";
			String path = FileDispatcher.getTempDir() + fileName;
			addressListBiz.csEexportAsCSV(uids, path);
			XMLResponse.outputStandardResponse(response, 1, FileDispatcher.getTempAbsDir() + fileName);
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
}