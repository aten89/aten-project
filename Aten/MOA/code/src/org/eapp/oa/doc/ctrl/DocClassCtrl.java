package org.eapp.oa.doc.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.doc.blo.IDocClassAssignBiz;
import org.eapp.oa.doc.blo.IDocClassBiz;
import org.eapp.oa.doc.dto.DocClassAssignXml;
import org.eapp.oa.doc.dto.DocClassList;
import org.eapp.oa.doc.dto.DocClassSelect;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.doc.hbean.DocClassAssign;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassCtrl extends HttpServlet {

	private static final long serialVersionUID = -5839760672218706774L;

	private IDocClassBiz docClassBiz;
	private IDocClassAssignBiz docClassAssignBiz;
	private int maxUploadSize;

	/**
	 * Constructor of the object.
	 */
	public DocClassCtrl() {
		super();
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
			maxUploadSize = Integer.parseInt(config
					.getInitParameter("maxUploadSize"));
		} catch (Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}

		docClassBiz = (IDocClassBiz) SpringHelper.getBean("docClassBiz");
		docClassAssignBiz = (IDocClassAssignBiz) SpringHelper.getBean("docClassAssignBiz");
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

		if (SysConstants.ADD.equalsIgnoreCase(act)) {
			// 新增公文类别信息
			addDocClass(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除公文类别信息
			delDocClass(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改公文类别信息
			modifyDocClass(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询公文类别信息
			queryDocClass(request, response);
			return;
		} else if ("initsort".equalsIgnoreCase(act)) {
			// 初始化排序
			initsort(request, response);
			return;
		} else if (SysConstants.ORDER.equalsIgnoreCase(act)) {
			// 保存排序
			saveSort(request, response);
			return;
//		} else if ("docclassSelect".equalsIgnoreCase(act)) {
//			// 数据字典读取信息类别
//			makeDocClassSelect(request, response);
//			return;
//			
		}else if("getassigndoc".equalsIgnoreCase(act)){
			//取得用户有权限的公文版块
			getAssignDoc(request,response);
		}else if ("init_assign".equalsIgnoreCase(act)) {
			// 初始化授权页面
			initAssign(request, response);
			return;
		} else if ("init_assign_detail".equalsIgnoreCase(act)) {
			// 初始化详细授权页面
			initAssignDetail(request, response);
			return;
		} else if ("get_binding_users".equalsIgnoreCase(act)) {
			// 已绑定的用户
			getBindUser(request, response);
			return;
		} else if (SysConstants.BIND_USER.equals(act)) {
			// 绑定用户
			saveBindUser(request, response);
			return;
		} else if ("get_binding_groups".equalsIgnoreCase(act)) {
			// 已绑定的机构
			getBindGroup(request, response);
			return;
		} else if (SysConstants.BIND_GROUP.equals(act)) {
			// 绑定机构
			saveBindGroup(request, response);
			return;
		} else if ("get_binding_post".equalsIgnoreCase(act)) {
			// 已绑定的职位
			getBindPost(request, response);
			return;
		} else if (SysConstants.BIND_POST.equals(act)) {
			// 绑定职位
			saveBindPost(request, response);
			return;
		} else if ("findall".equals(act)) {
			// 取得所有的信息配置
			queryDocClass(request, response);
			return;
		} else if ("edittemp".equalsIgnoreCase(act)) {
			// 编辑正文模板
			editDocTemp(request, response);
		} else if ("uploadtemplate".equalsIgnoreCase(act)) {
			// 上传模板DOC
			uploadBodyTemplate(request, response);
		}else if ("select".equalsIgnoreCase(act)) {
			// 生成公文类别下拉框
			docClassSelect(request, response);
		}
	}
	private void docClassSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DocClass> list =docClassBiz.getByFileClass(DocForm.COM_FILE);
			HTMLResponse.outputHTML(response, new DocClassSelect(list).toString());
		}catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.toString());
		}
	}
	private void initAssignDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 信息类别
		String page = HttpRequestHelper.getParameter(request, "page");
		String title = HttpRequestHelper.getParameter(request, "title");
		request.setAttribute("title", title);
		request.getRequestDispatcher("/page/doc/paramconf/" + page).forward(request, response);

	}

	private void initAssign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 信息类别
		String title = HttpRequestHelper.getParameter(request, "title");
		request.setAttribute("title", title);
		request.getRequestDispatcher(
				"/page/doc/paramconf/frame_assign.jsp").forward(
				request, response);
	}

	private void addDocClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 公文类别
		String name = HttpRequestHelper.getParameter(request, "docclassname");
		if (name == null) {
			XMLResponse.outputStandardResponse(response, 0, "请选择分类名称");
			return;
		}
		// 公文类别
		String fileClass = HttpRequestHelper.getParameter(request, "fileClass");
		if (fileClass == null) {
			XMLResponse.outputStandardResponse(response, 0, "请文件分类名称");
			return;
		}
		// 流程类别
		String flowClass = HttpRequestHelper.getParameter(request, "flowclass");
		// 说明
		String desc = HttpRequestHelper.getParameter(request, "desc");
		
//		String isAssign = HttpRequestHelper.getParameter(request, "isAssign");

		try {
			DocClass docClass = docClassBiz
					.addDocClass(name, flowClass, Integer.parseInt(fileClass), desc);
			XMLResponse.outputStandardResponse(response, 1, docClass.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void delDocClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
			return;
		}
		try {
			DocClass docClass = docClassBiz.deleteDocClass(id);
			XMLResponse.outputStandardResponse(response, 1, docClass.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void modifyDocClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为空");
			return;
		}

		// 公文类别名称
		String name = HttpRequestHelper.getParameter(request, "docclassname");
		if (name == null) {
			XMLResponse.outputStandardResponse(response, 0, "请选择分类名称");
			return;
		}
		// 公文类别
		String fileClass = HttpRequestHelper.getParameter(request, "fileClass");
		if (fileClass == null) {
			XMLResponse.outputStandardResponse(response, 0, "请文件分类名称");
			return;
		}
		// 流程类别
		String flowClass = HttpRequestHelper.getParameter(request, "flowclass");

		// 说明
		String desc = HttpRequestHelper.getParameter(request, "desc");
//		String isAssign = HttpRequestHelper.getParameter(request, "isAssign");
		try {
			DocClass docClass = docClassBiz.modifyDocClass(id, name,
					flowClass, Integer.parseInt(fileClass),desc);
			XMLResponse.outputStandardResponse(response, 1, docClass.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void queryDocClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			List<DocClass> list = docClassBiz.getAllDocClass();
			XMLResponse.outputXML(response, new DocClassList(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}

	private void initsort(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/page/doc/paramconf/sort_class.jsp").forward(request, response);
	}

	private void saveSort(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String orderIDs = HttpRequestHelper.getParameter(request, "orderids");
		if (orderIDs == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
			return;
		}

		try {
			String[] docClassIDs = orderIDs.split(",");
			if (docClassIDs != null && docClassIDs.length > 0) {
				docClassBiz.txSaveOrder(docClassIDs);
			}

			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}

	}

//	private void makeDocClassSelect(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		List<DataDictionaryDTO> rootList = SysCodeDictLoader.getInstance().getDocClass();
//		// 1.取根目录
//		if (rootList == null) {
//			return;
//		}
//		HTMLResponse.outputHTML(response, new DataDictionarySelect(rootList).toString());
//	}
	
	/**
	 * 获得授权公文
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getAssignDoc(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		
		int fileClass = HttpRequestHelper.getIntParameter(request, "fileClass", 0);
		
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
		List<DocClass> layouts = docClassBiz.getAssignClass(user.getAccountID(), groupNames, postNames, fileClass);
		XMLResponse.outputXML(response, new DocClassList(layouts).toDocument());
	}
	/**
	 * 获得绑定的用户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getBindUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DocClassAssign> list = docClassAssignBiz.getBindingUsers(id);
			XMLResponse.outputXML(response, new DocClassAssignXml(list)
					.toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void saveBindUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] userIDs = HttpRequestHelper.getParameters(request, "user_ids");
		try {
			docClassAssignBiz.txBindingUsers(id, userIDs);
			XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
		}
	}

	private void getBindGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DocClassAssign> list = docClassAssignBiz.getBindingGroups(id);
			XMLResponse.outputXML(response, new DocClassAssignXml(list)
					.toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void saveBindGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] groupIDs = HttpRequestHelper.getParameters(request, "group_ids");
		try {
			docClassAssignBiz.txBindingGroups(id, groupIDs);
			XMLResponse.outputStandardResponse(response, 1, "绑定机构成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定机构失败！");
		}
	}

	private void getBindPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DocClassAssign> list = docClassAssignBiz.getBindingPosts(id);
			XMLResponse.outputXML(response, new DocClassAssignXml(list)
					.toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void saveBindPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
//		int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] postIDs = HttpRequestHelper.getParameters(request, "post_ids");
		try {
			docClassAssignBiz.txBindingPosts(id, postIDs);
			XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
		}
	}

	private void editDocTemp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");

		if (id == null) {
			request.setAttribute("errorMsg", "参数不正确");
			request.getRequestDispatcher("/page/error.jsp").forward(request,
					response);
			return;
		}
		SessionAccount user = (SessionAccount) request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,
					response);
			return;
		}

		DocClass docClass = docClassBiz.getDocClassById(id);
		request.setAttribute("docClass", docClass);
		request.getRequestDispatcher(
				"/page/doc/paramconf/edit_bodytemp.jsp")
				.forward(request, response);
	}

	@SuppressWarnings("unchecked")
	private void uploadBodyTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleFileUpload fileUpload = new SimpleFileUpload(request,
				maxUploadSize);
		try {
			fileUpload.doParse();
			String referId = FileUploadHelper.getParameter(fileUpload,"referid");
			if (referId == null) {
				XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
				return;
			}

			List<FileItem> fileList = fileUpload.getFileDataList();
			if (fileList == null || fileList.isEmpty()) {
				XMLResponse.outputStandardResponse(response, 0, "文件不存在");
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
			String dir = SysConstants.DOC_ATTACHMENT_DIR
					+ sdf.format(new Date());
			FileItem file = fileList.get(0);
			String fileName = fileUpload.getFileName(file);
			if (fileName == null || fileName.trim().equals("")) {
				XMLResponse.outputStandardResponse(response, 0, "文件不存在");
				return;
			}
			Attachment am = new Attachment(fileName, file.getSize());
			String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
			am.setFilePath(path);

			// 保存附件
			FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher
					.getAbsPath(path)), file.getInputStream());
			docClassBiz.addBodyTemplate(referId, am);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (SizeLimitExceededException e) {
			XMLResponse.outputStandardResponse(response, 0, "上载文件大于"
					+ maxUploadSize / 1024 + "K!");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
		}

	}
}
