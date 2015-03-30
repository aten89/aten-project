package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IHolidayTypeBiz;
import org.eapp.oa.hr.dto.HolidayTypeList;
import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 假种信息
 * @version
 */

public class HolidayTypeCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IHolidayTypeBiz holidayTypeBiz;
	/**
	 * Constructor of the object.
	 */
	public HolidayTypeCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		holidayTypeBiz = (IHolidayTypeBiz) SpringHelper.getSpringContext().getBean("holidayTypeBiz");
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
			//查询
			queryHolidayType(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(action)) {
			//修改
			modifyHolidayType(request, response);
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除
			delHolidayType(request, response);
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//新增
			addHolidayType(request, response);
		} 
	}
	
	
	private void queryHolidayType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<HolidayType> holidayType = holidayTypeBiz.getAllHolidayTypes();
		XMLResponse.outputXML(response, new HolidayTypeList(holidayType).toDocument());
	}
	
	
	private void modifyHolidayType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
            return;
        }
		String holidayName = HttpRequestHelper.getParameter(request, "holidayname");
		if (holidayName == null) {
            XMLResponse.outputStandardResponse(response, 0, "假期名称不能为空");
            return;
        }
        Double maxDays = HttpRequestHelper.getDoubleParameter(request, "maxdays", null);
        String expression = HttpRequestHelper.getParameter(request, "exp");
        String description = HttpRequestHelper.getParameter(request, "desc");
	        
		try{
			HolidayType conf = holidayTypeBiz.modifyHolidayType(id, holidayName, maxDays, expression, description);
			XMLResponse.outputStandardResponse(response, 1, conf.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
	}
	
	private void delHolidayType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try{
			holidayTypeBiz.deleteHolidayType(id);
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
		}	
	}
	
	private void addHolidayType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String holidayName = HttpRequestHelper.getParameter(request, "holidayname");
        if (holidayName == null) {
            XMLResponse.outputStandardResponse(response, 0, "假期名称不能为空");
            return;
        }
        Double maxDays = HttpRequestHelper.getDoubleParameter(request, "maxdays", null);
        String expression = HttpRequestHelper.getParameter(request, "exp");
        String description = HttpRequestHelper.getParameter(request, "desc");
		try{
			HolidayType conf = holidayTypeBiz.addHolidayType(holidayName, maxDays, expression, description);
			XMLResponse.outputStandardResponse(response, 1, conf.getId());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
	}
}
