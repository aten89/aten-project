package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.reimburse.blo.IReimFlowConfBiz;
import org.eapp.oa.reimburse.dto.ReimFlowConfList;
import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;


public class ReimFlowConfCtrl extends HttpServlet {
	
	private IReimFlowConfBiz reimFlowConfBiz;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6294000416727176193L;

	/**
	 * Constructor of the object.
	 */
	public ReimFlowConfCtrl() {
		super();
	}
	
	/**
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		reimFlowConfBiz =  (IReimFlowConfBiz) SpringHelper.getSpringContext().getBean("reimFlowConfBiz");
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
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request , response);

	}

	/**
	 * The doPost method of the servlet. <br>
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = HttpRequestHelper.getParameter(request, "act");	
		
		if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			//查询
			queryConf(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(act)) {
			//删除
			addConf(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			//修改
			modifyConf(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			//新增
			deleteConf(request, response);
			return;
		} 
	}
	
	/**
     * 新增
     */
    private void addConf(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // 信息类别
        String groupName = HttpRequestHelper.getParameter(request, "groupname");
        if (groupName == null) {
            XMLResponse.outputStandardResponse(response, 0, "请选择部门名称");
            return;
        }
      
        String flowKey = HttpRequestHelper.getParameter(request, "flowkey");
        String desc = HttpRequestHelper.getParameter(request, "desc");

        try {
            ReimFlowConf conf = reimFlowConfBiz.addReimFlowConf(groupName, flowKey, desc);
            XMLResponse.outputStandardResponse(response, 1, conf.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }
	
    /**
     * 修改
     */
    private void modifyConf(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
            return;
        }
        String groupName = HttpRequestHelper.getParameter(request, "groupname");
        if (groupName == null) {
            XMLResponse.outputStandardResponse(response, 0, "请选择部门名称");
            return;
        }
        String flowKey = HttpRequestHelper.getParameter(request, "flowkey");
        String desc = HttpRequestHelper.getParameter(request, "desc");

        try {
        	ReimFlowConf rfc = reimFlowConfBiz.modifyReimFlowConf(id, groupName, flowKey, desc);
            XMLResponse.outputStandardResponse(response, 1, rfc.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 删除
     */
    private void deleteConf(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
            return;
        }
        try {
        	ReimFlowConf rfc = reimFlowConfBiz.deleteReimFlowConf(id);
            XMLResponse.outputStandardResponse(response, 1, rfc.getId());
        } catch (Exception ex) {
        	ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }
    
    /**
     * 查询类别信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void queryConf(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            // 查询
            List<ReimFlowConf> list = reimFlowConfBiz.getAllReimFlowConfs();
            XMLResponse.outputXML(response, new ReimFlowConfList(list).toDocument());
        } catch (Exception e) {
        	e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }
}
