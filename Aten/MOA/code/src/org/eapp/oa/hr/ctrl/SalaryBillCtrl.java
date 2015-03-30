package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.eapp.oa.hr.blo.ISalaryBillBiz;
import org.eapp.oa.hr.dto.SalaryBillPage;
import org.eapp.oa.hr.dto.SalaryBillQueryParameters;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 工资条的请求
 */
public class SalaryBillCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private int maxUploadSize;
	private ISalaryBillBiz salaryBillBiz;
	/**
	 * Constructor of the object.
	 */
	public SalaryBillCtrl() {
		super();
		salaryBillBiz = (ISalaryBillBiz) SpringHelper.getSpringContext().getBean("salaryBillBiz");
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
		if (SysConstants.IMPORT.equalsIgnoreCase(action)) {
			//导入
			importSalaryBill(request, response);
		} else if ("count".equalsIgnoreCase(action)) {
			//统计
			countImportSalaryBill(request, response);
		} else if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询
			querySalaryBill(request, response, null);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewSalaryBill(request, response);
		}
	}
	
	private void importSalaryBill(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Integer code = 0;
		String msg;
		SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
		InputStream fin = null;
        try {
            fileUpload.doParse();
            String month = FileUploadHelper.getParameter(fileUpload, "month");
            if (month == null) {
            	msg = "请设置要导入月份";
            } else {
	            List<FileItem> fileList = fileUpload.getFileDataList();
	            if(fileList == null || fileList.isEmpty()) {
	            	msg = "上传文件不存在";
	            } else {
		        	FileItem file = fileList.get(0);
		        	fin = file.getInputStream();
		        	salaryBillBiz.txImpSalaryBill(Integer.parseInt(month), fin);
		        	code = 1;
		            msg = "导入成功";
	            }
            }
        } catch (SizeLimitExceededException e) {
        	msg = "上载文件大于" + maxUploadSize / 1024+ "K!"; 
        } catch (OaException e) {
        	msg = e.getMessage();
        } catch (Exception e) {
        	e.printStackTrace();
        	msg = "上载文件失败"; 
        } finally {
        	if (fin != null) {
        		fin.close();
        	}
        }
        request.setAttribute("code", code); 
        request.setAttribute("msg", msg); 
        request.getRequestDispatcher("/page/hr/salarybill/import_salary.jsp").forward(request, response);
	}
	
	private void countImportSalaryBill(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int month = HttpRequestHelper.getIntParameter(request, "month", 0);
		if (month == 0) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		XMLResponse.outputStandardResponse(response, 1, Integer.toString(salaryBillBiz.getSalaryBillCount(month)));
	}
	
	void querySalaryBill(HttpServletRequest request,
			HttpServletResponse response, String userAccountID) throws ServletException, IOException {
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String userKeyword = HttpRequestHelper.getParameter(request, "userkeyword");
		String employeeNumber = HttpRequestHelper.getParameter(request, "employeeno");
		int month = HttpRequestHelper.getIntParameter(request, "month", 0);
		//构造查询条件对象
		SalaryBillQueryParameters rqp = new SalaryBillQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
		rqp.setUserKeyword(userKeyword);
		rqp.setEmployeeNumber(employeeNumber);
		if (month != 0) {
			rqp.setMonth(month);
		}
		if (userAccountID != null) {
			rqp.setUserAccountID(userAccountID);
		}
		rqp.addOrder("userName", true);
		
		ListPage<SalaryBill> reis = salaryBillBiz.getSalaryBillPage(rqp);
		XMLResponse.outputXML(response, new SalaryBillPage(reis).toDocument());
	}
	
	void viewSalaryBill(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			SalaryBill salaryBill = salaryBillBiz.getSalaryBillById(id);
			if (salaryBill == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("salaryBill", salaryBill);
			request.getRequestDispatcher("/page/hr/salarybill/view_salary.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
}
