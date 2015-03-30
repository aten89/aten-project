
package org.eapp.oa.meeting.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.meeting.blo.IMeetingRoomBiz;
import org.eapp.oa.meeting.dto.MeetingRoomList;
import org.eapp.oa.meeting.dto.MeetingRoomSelect;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 
 * 
 * @author sds
 * @version Jul 8, 2009
 */
public class MeetingRoomCtrl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IMeetingRoomBiz meetingRoomBiz;

	/**
	 * Constructor of the object.
	 */
	public MeetingRoomCtrl() {
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
	public void init() throws ServletException {
		meetingRoomBiz = (IMeetingRoomBiz) SpringHelper.getSpringContext()
				.getBean("meetingRoomBiz");
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
			// 新增会议室资料
			addMeetingRoom(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除会议室资料
			delMeetingRoom(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改会议室资料
			modifyMeetingRoom(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询会议室资料
			queryMeetingRoom(request, response);
			return;
		} else if ("initsort".equalsIgnoreCase(act)) {
			// 初始化排序
			initsort(request, response);
			return;
		} else if (SysConstants.ORDER.equalsIgnoreCase(act)) {
			// 保存排序
			saveSort(request, response);
			return;
		} else if ("findall".equals(act)) {
			// 取得所有的信息配置
			queryMeetingRoom(request, response);
			return;
		} else if("meetingroomselect".equalsIgnoreCase(act)){
			//生成会议室的项目下拉
			selectDivMeetingRoom(request, response);
			return;
		} else if("meetingroomenv".equalsIgnoreCase(act)){
			//取得会议室的环境信息
			getMeetingRoomEnv(request, response);
			return;
		}
	}
	
	/**
	 * 生成下拉片段
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void selectDivMeetingRoom(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			boolean isOrder = HttpRequestHelper.getBooleanParameter(request, "isOrder", false);
			String areaCode = HttpRequestHelper.getParameter(request,"areaCode");
			List<MeetingRoom> meetingRooms = meetingRoomBiz.getMeetingRoomByAreaCode(areaCode);
			HTMLResponse.outputHTML(response, new MeetingRoomSelect(meetingRooms, isOrder).toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void addMeetingRoom(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String name = HttpRequestHelper.getParameter(request, "name");
		int seatNum = HttpRequestHelper.getIntParameter(request, "seatNum", 0);
		String environment = HttpRequestHelper.getParameter(request,"environment");
		int powerNum = HttpRequestHelper.getIntParameter(request, "powerNum", 0);
		int cableNum = HttpRequestHelper.getIntParameter(request, "cableNum", 0);
		int lineNum = HttpRequestHelper.getIntParameter(request, "lineNum", 0);
		String phoneNumber = HttpRequestHelper.getParameter(request,"phoneNumber");
		boolean flag = HttpRequestHelper.getBooleanParameter(request, "status", true);
		String remark = HttpRequestHelper.getParameter(request, "remark");
		
		if (name == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		try {
			MeetingRoom meetingRoom = meetingRoomBiz.txAddMeetingRoom(areaCode, name,
					seatNum, environment,powerNum, cableNum, lineNum, 
					phoneNumber, flag, remark);
			
			XMLResponse.outputStandardResponse(response, 1, meetingRoom.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void delMeetingRoom(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
			return;
		}
		try {
			meetingRoomBiz.txDelMeetingRoom(id);
			XMLResponse.outputStandardResponse(response, 1, "会议室删除成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void modifyMeetingRoom(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String name = HttpRequestHelper.getParameter(request, "name");
		int seatNum = HttpRequestHelper.getIntParameter(request, "seatNum", 0);
		String environment = HttpRequestHelper.getParameter(request,"environment");
		int powerNum = HttpRequestHelper.getIntParameter(request, "powerNum", 0);
		int cableNum = HttpRequestHelper.getIntParameter(request, "cableNum", 0);
		int lineNum = HttpRequestHelper.getIntParameter(request, "lineNum", 0);
		String phoneNumber = HttpRequestHelper.getParameter(request,"phoneNumber");
		boolean flag = HttpRequestHelper.getBooleanParameter(request, "status", false);
		if (id == null || name == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		String remark = HttpRequestHelper.getParameter(request, "remark");

		try {
			MeetingRoom meetingRoom = meetingRoomBiz.txModifyMeetingRoom(id, areaCode, name,
					seatNum, environment, powerNum, 
					cableNum, lineNum, phoneNumber, flag, remark);
			XMLResponse.outputStandardResponse(response, 1, meetingRoom.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	protected void queryMeetingRoom(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			
			// 查询
			String areaCode = HttpRequestHelper.getParameter(request,"areaCode");
			List<MeetingRoom> list = meetingRoomBiz.getMeetingRoomByAreaCode(areaCode);
			XMLResponse.outputXML(response, new MeetingRoomList(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}

	private void initsort(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/page/meeting/paramconf/sort_room.jsp")
				.forward(request, response);
	}

	private void saveSort(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String orderIDs = HttpRequestHelper.getParameter(request, "orderids");
		if (orderIDs == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
			return;
		}

		try {
			String[] meetingRoomIDs = orderIDs.split(",");
			if (meetingRoomIDs != null && meetingRoomIDs.length > 0) {
				meetingRoomBiz.txSaveOrder(meetingRoomIDs);
			}

			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}

	}
	
	private void getMeetingRoomEnv(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		try {
			MeetingRoom mr = meetingRoomBiz.getMeetingRoomById(id);
			if (mr != null) {
				XMLResponse.outputStandardResponse(response, 1, mr.getEnvironment());
			} else {
				XMLResponse.outputStandardResponse(response, 1, "");
			}
		} catch (Exception e){
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

}
