package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IHRFlowConfBiz;
import org.eapp.oa.hr.dto.HRFlowConfList;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

public class HRFlowConfCtrl extends HttpServlet {

	private static final long serialVersionUID = -494435719942011665L;

	private IHRFlowConfBiz hrFlowConfBiz;
	
	public HRFlowConfCtrl() {
		super();
	}

	public void init() throws ServletException {
		hrFlowConfBiz = (IHRFlowConfBiz) SpringHelper.getSpringContext().getBean("hrFlowConfBiz");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//查询
			queryHolidayFlow(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(action)) {
			//修改
			modifyHolidayFlow(request, response);
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除
			delHolidayFlow(request, response);
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//新增
			addHolidayFlow(request, response);
		} 
	}
	
	private void queryHolidayFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<HRFlowConf> holidayFlow = hrFlowConfBiz.getAllHolidayFlowConfs();
		XMLResponse.outputXML(response, new HRFlowConfList(holidayFlow).toDocument());
			
	}
	private void modifyHolidayFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{

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
        String holidayFlowKey = HttpRequestHelper.getParameter(request, "holflowkey");
        String canHolidayFlowKey = HttpRequestHelper.getParameter(request, "canflowkey");
        String entryFlowKey = HttpRequestHelper.getParameter(request, "entflowkey");
        String resignFlowKey = HttpRequestHelper.getParameter(request, "resflowkey");
        String transferFlowKey = HttpRequestHelper.getParameter(request, "tranflowkey");
        String positiveFlowKey = HttpRequestHelper.getParameter(request, "posiflowkey");
        String desc = HttpRequestHelper.getParameter(request, "desc");
	        
		try{
			HRFlowConf conf = hrFlowConfBiz.modifyHolidayFlowConf(id, groupName, holidayFlowKey, 
					canHolidayFlowKey, entryFlowKey, resignFlowKey, transferFlowKey, positiveFlowKey, desc);
			XMLResponse.outputStandardResponse(response, 1, conf.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
	}
	
	private void delHolidayFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try{
			hrFlowConfBiz.deleteHolidayFlowConf(id);
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
		}		
	}
	
	private void addHolidayFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		String groupName = HttpRequestHelper.getParameter(request, "groupname");
        if (groupName == null) {
            XMLResponse.outputStandardResponse(response, 0, "请选择部门名称");
            return;
        }
        String holidayFlowKey = HttpRequestHelper.getParameter(request, "holflowkey");
        String canHolidayFlowKey = HttpRequestHelper.getParameter(request, "canflowkey");
        String entryFlowKey = HttpRequestHelper.getParameter(request, "entflowkey");
        String resignFlowKey = HttpRequestHelper.getParameter(request, "resflowkey");
        String transferFlowKey = HttpRequestHelper.getParameter(request, "tranflowkey");
        String positiveFlowKey = HttpRequestHelper.getParameter(request, "posiflowkey");
        String desc = HttpRequestHelper.getParameter(request, "desc");
		try{
			HRFlowConf conf = hrFlowConfBiz.addHolidayFlowConf(groupName, holidayFlowKey, 
					canHolidayFlowKey, entryFlowKey, resignFlowKey, transferFlowKey, positiveFlowKey, desc);
			XMLResponse.outputStandardResponse(response, 1, conf.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
	}
}
