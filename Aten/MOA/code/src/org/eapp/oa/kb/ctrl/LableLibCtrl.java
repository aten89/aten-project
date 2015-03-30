package org.eapp.oa.kb.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.kb.blo.ILabelLibBiz;
import org.eapp.oa.kb.dto.LabelLibPage;
import org.eapp.oa.kb.dto.LabelLibQueryParameters;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;

public class LableLibCtrl extends HttpServlet {


	private static final long serialVersionUID = -1330245250999006112L;
	
	private ILabelLibBiz labelLibBiz;

	/**
	 * Constructor of the object.
	 */
	public LableLibCtrl() {
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		labelLibBiz = (ILabelLibBiz) SpringHelper.getSpringContext().getBean("labelLibBiz");
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request , response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String act = HttpRequestHelper.getParameter(request, "act");
		
		if(SysConstants.ADD.equalsIgnoreCase(act)){
			//新增类别配置信息
			addLabel(request,response);
			return;
		} else if(SysConstants.MODIFY.equalsIgnoreCase(act)){
			//修改标签信息
			modifyLable(request,response);
			return;
		} else if(SysConstants.DELETE.equalsIgnoreCase(act)){
			//删除类别配置信息
			delLabel(request,response);
			return;
		} else if(SysConstants.QUERY.equalsIgnoreCase(act)){
			//查询标签信息
			queryLable(request,response);
			return;
		} else if("autocomplete".equals(act)){
			//自动关联关键字
			autocompleteLable(request,response);
			return;
		}
	}

	private void addLabel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String name = HttpRequestHelper.getParameter(request, "labelName");
		int property =  HttpRequestHelper.getIntParameter(request, "property", 0);
		
		if (name == null) {
			XMLResponse.outputStandardResponse(response, 0, "标签名称不能为空");
			return;
		}
		try {
			labelLibBiz.txAddOrModLabelLib(null, name.trim(), property);
			XMLResponse.outputStandardResponse(response, 1, "添加成功!");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
		
	}
	
	private void modifyLable(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String name = HttpRequestHelper.getParameter(request, "labelName");
		int property =  HttpRequestHelper.getIntParameter(request, "property", 0);
		
		if (id == null || name == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			labelLibBiz.txAddOrModLabelLib(id, name.trim(), property);
			XMLResponse.outputStandardResponse(response, 1, "修改成功!");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
		
	}
	
	private void delLabel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String name = HttpRequestHelper.getParameter(request, "name");
		String startCountStr = HttpRequestHelper.getParameter(request, "startCount");
		String endCountStr = HttpRequestHelper.getParameter(request, "endCount");
		Long startCount = null;
		Long endCount = null;
		
		if(startCountStr != null){
			startCount = Long.parseLong(startCountStr);
		}
		if(endCountStr != null){
			endCount = Long.parseLong(endCountStr);
		}
		
		try {
			labelLibBiz.txDelLabelLibs(id, name, startCount, endCount);
			XMLResponse.outputStandardResponse(response, 1, "删除成功!");
		}catch (OaException ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
		
	}
	private void queryLable(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
 		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageCount = HttpRequestHelper.getIntParameter(request, "pagecount", 15);
		
		String name = HttpRequestHelper.getParameter(request, "name");
		String startCountStr =  HttpRequestHelper.getParameter(request, "startCount");
		String endCountStr =  HttpRequestHelper.getParameter(request, "endCount");
		Long startCount = null;
		Long endCount = null;
		
		if(startCountStr != null){
			startCount = Long.parseLong(startCountStr);
		}
		if(endCountStr != null){
			endCount =  Long.parseLong(endCountStr);
		}
		
		LabelLibQueryParameters qp = new LabelLibQueryParameters();
		qp.setPageNo(pageNo);
		qp.setPageSize(pageCount);
		qp.setName(name);
		qp.setStartCount(startCount);
		qp.setEndCount(endCount);
		qp.addOrder("count",false);
		qp.addOrder("id", true);
		ListPage<LabelLib> labelLib = labelLibBiz.queryLabelLibs(qp);
		XMLResponse.outputXML(response,new LabelLibPage(labelLib).toDocument());
	}
	
	private void autocompleteLable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<String> list = labelLibBiz.getLabelName();
			String jsonArray = JSON.toJSONString(list);
			response.setContentType("text/javascript;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
