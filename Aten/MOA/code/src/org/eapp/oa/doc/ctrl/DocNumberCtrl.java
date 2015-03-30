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
import org.eapp.oa.doc.blo.IDocNumberBiz;
import org.eapp.oa.doc.dto.DocNumberList;
import org.eapp.oa.doc.hbean.DocNumber;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

public class DocNumberCtrl extends HttpServlet {

	private static final long serialVersionUID = -5839760672218706774L;

	private IDocNumberBiz docNumberBiz;
	private int maxUploadSize;

	/**
	 * Constructor of the object.
	 */
	public DocNumberCtrl() {
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
			maxUploadSize = Integer.parseInt(config.getInitParameter("maxUploadSize"));
		} catch (Exception e) {
			e.printStackTrace();
			maxUploadSize = -1;
		}

		docNumberBiz = (IDocNumberBiz)SpringHelper.getBean("docNumberBiz");
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
			// 新增公文编号
			addDocNumber(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除公文编号
			delDocNumber(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改公文编号
			modifyDocNumber(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询公文编号
			queryDocNumber(request, response);
			return;
		} else if ("edittemp".equalsIgnoreCase(act)) {
			// 编辑红头模板
			editHeadTemplate(request, response);
			return;
		} else if ("uploadtemplate".equalsIgnoreCase(act)) {
			// 上传模板DOC
			uploadHeadTemplate(request, response);
			return;
			
			
			
			
			
		} else if ("findall".equals(act)) {
			// 取得所有的信息配置
			queryDocNumber(request, response);
			return;
		} else if("initredheaderselect".equalsIgnoreCase(act)){
			//套红头页面
			initHeadSelect(request, response);
			return;
		} else if("finddocinfo".equalsIgnoreCase(act)){
			//查询红头内容
			findDocNumInfo(request, response);
			return;
		}
	}

	private void addDocNumber(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String docWord = HttpRequestHelper.getParameter(request, "docWord");
		String yearPrefix = HttpRequestHelper.getParameter(request, "yearPrefix");
		String yearPostfix = HttpRequestHelper.getParameter(request, "yearPostfix");
		String orderPrefix = HttpRequestHelper.getParameter(request, "orderPrefix");
		String orderPostfix = HttpRequestHelper.getParameter(request, "orderPostfix");
		
		if (docWord == null) {
			XMLResponse.outputStandardResponse(response, 0, "文件字不能为空");
			return;
		}
		if (yearPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "年份前缀不能为空");
			return;
		}
		if (yearPostfix == null) {
			XMLResponse.outputStandardResponse(response, 0, "年份后缀不能为空");
			return;
		}
		if (orderPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号前缀不能为空");
			return;
		}
		if (orderPostfix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号后缀不能为空");
			return;
		}

		try {
			DocNumber docNumber = docNumberBiz.addDocNumber(docWord,
					yearPrefix, yearPostfix, orderPrefix, orderPostfix);
			XMLResponse.outputStandardResponse(response, 1, docNumber.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void delDocNumber(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
			return;
		}
		try {
			DocNumber docNumber = docNumberBiz.deleteDocNumber(id);
			XMLResponse.outputStandardResponse(response, 1, docNumber.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void modifyDocNumber(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为空");
			return;
		}
		String docWord = HttpRequestHelper.getParameter(request, "docWord");
		String yearPrefix = HttpRequestHelper.getParameter(request, "yearPrefix");
		String yearPostfix = HttpRequestHelper.getParameter(request, "yearPostfix");
		String orderPrefix = HttpRequestHelper.getParameter(request, "orderPrefix");
		String orderPostfix = HttpRequestHelper.getParameter(request, "orderPostfix");
		
		if (docWord == null) {
			XMLResponse.outputStandardResponse(response, 0, "文件字不能为空");
			return;
		}
		if (yearPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "年份前缀不能为空");
			return;
		}
		if (yearPostfix == null) {
			XMLResponse.outputStandardResponse(response, 0, "年份后缀不能为空");
			return;
		}
		if (orderPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号前缀不能为空");
			return;
		}
		if (orderPostfix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号后缀不能为空");
			return;
		}
		try {
			DocNumber docNumber = docNumberBiz.modifyDocNumber(id, docWord,
					yearPrefix, yearPostfix, orderPrefix, orderPostfix);
			XMLResponse.outputStandardResponse(response, 1, docNumber.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void queryDocNumber(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			List<DocNumber> list = docNumberBiz.getAllDocNumber();
			XMLResponse.outputXML(response, new DocNumberList(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}

	private void editHeadTemplate(HttpServletRequest request,
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

		DocNumber docNumber = docNumberBiz.getDocNumber(id);
		request.setAttribute("docNumber", docNumber);
		request.getRequestDispatcher("/page/doc/paramconf/edit_headtemp.jsp").forward(request, response);
	}

	@SuppressWarnings("unchecked")
	private void uploadHeadTemplate(HttpServletRequest request,
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
			if (fileList == null || fileList.isEmpty()) {
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

			// 保存附件
			FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher
					.getAbsPath(path)), file.getInputStream());
			docNumberBiz.addHeadTemplate(referId, am);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (SizeLimitExceededException e) {
			XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024 + "K!");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
		}

	}
	private void initHeadSelect(HttpServletRequest request,
	HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,
					response);
			return;
		}
		// 查询
		List<DocNumber> list = docNumberBiz.getAllDocNumber();
		request.setAttribute("docNums", list);
		request.getRequestDispatcher("/page/doc/dispatch/dialog_header.jsp").forward(request, response);
	}
	private void findDocNumInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		String id = HttpRequestHelper.getParameter(request, "id");
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,
					response);
			return;
		}
		if(id ==null || id.length()==0){
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "缺少参数");
			request.getRequestDispatcher("/page/error.jsp").forward(request,
					response);
			return;
		}
		DocNumber docNum = docNumberBiz.getDocNumber(id);
		List<DocNumber> list = new ArrayList<DocNumber>();
		list.add(docNum);
		XMLResponse.outputXML(response, new DocNumberList(list).toDocument());		
	}
}
