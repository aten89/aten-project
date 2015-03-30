package org.eapp.oa.meeting.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.eapp.comobj.SessionAccount;
import org.eapp.oa.meeting.blo.IMeetingInfoBiz;
import org.eapp.oa.meeting.dto.MeetingInfoListPage;
import org.eapp.oa.meeting.dto.MeetingInfoQueryParameters;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * 
 * @author sds
 * @version Jul 13, 2009
 */
public class MeetingQueryCtrl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IMeetingInfoBiz meetingInfoBiz;
	
	private MeetingInfoCtrl meetingInfoCtrl;

//	private static final String MEET_ALL = "all"; // 所有会议
	private static final String MEET_IRESERVED = "ireserved"; // 我预订的会议
	private static final String MEET_IATTENDED = "iattended"; // 我参加过的会议
	private static final String MEET_INOTSTART = "inotstart"; // 我未开始的会议

	/**
	 * Constructor of the object.
	 */
	public MeetingQueryCtrl() {
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
		meetingInfoBiz = (IMeetingInfoBiz) SpringHelper.getSpringContext().getBean("meetingInfoBiz");
		meetingInfoCtrl = new MeetingInfoCtrl();
		meetingInfoCtrl.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = HttpRequestHelper.getParameter(request, "act");

		if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询会议信息
			query(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			// 点击详情查看会议信息
			viewMeetingDetail(request, response);
			return;
		} else if ("meetingorderinfo".equals(act)){
			queryMeetingOrderMsg(request, response);
			return;
		}
	}

	private void query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取登录用户信息
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String userAccountId = user.getAccountID();

		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize",12);
		String inputBeginDate = DataFormatUtil.formatTime(request.getParameter("inputBeginDate"));
		String inputEndDate = DataFormatUtil.formatTime(request.getParameter("inputEndDate"));
		String roomId = HttpRequestHelper.getParameter(request, "roomId");
		String theme = HttpRequestHelper.getParameter(request, "theme");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		// 构造查询参数
		MeetingInfoQueryParameters miqp = new MeetingInfoQueryParameters();
		miqp.setRoomId(roomId);
		miqp.setAreaCode(areaCode);
		miqp.setTheme(theme);
		miqp.setPageNo(pageNo);
		miqp.setPageSize(pageSize);
		if (inputBeginDate != null) {
			miqp.setInputBeginDate(Timestamp.valueOf(inputBeginDate));
		}
		if (inputEndDate != null) {
			Timestamp t = Timestamp.valueOf(inputEndDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			miqp.setInputEndDate(t);
		}
		String queryType = HttpRequestHelper.getParameter(request, "queryType");
		
		if (MEET_IRESERVED.equalsIgnoreCase(queryType)) {
			miqp.setApplyMan(userAccountId);
			miqp.addOrder("beginTime", false);
		} else if (MEET_IATTENDED.equalsIgnoreCase(queryType)) {
			miqp.setParticipant(userAccountId);
			miqp.setStart(true);
		} else if (MEET_INOTSTART.equalsIgnoreCase(queryType)) {
			miqp.setParticipant(userAccountId);
			miqp.setStart(false);
		}
		miqp.addOrder("beginTime", false);
		// 查询
		ListPage<MeetingInfo> meetingInfoListPage = meetingInfoBiz.queryMeetingInfo(miqp);
		
		// 转DOC
		MeetingInfoListPage meetingInfoListPageDTO = new MeetingInfoListPage(meetingInfoListPage);
		Document xmlDoc = meetingInfoListPageDTO.toDocument();
		XMLResponse.outputXML(response, xmlDoc);
	}

	private void viewMeetingDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		meetingInfoCtrl.viewMeetingDetail(request, response);
	}
	
	private void queryMeetingOrderMsg(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取登录用户信息
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String inputBeginDate = DataFormatUtil.formatTime(request.getParameter("inputBeginDate"));
		String inputEndDate = DataFormatUtil.formatTime(request.getParameter("inputEndDate"));
		// 构造查询参数
		MeetingInfoQueryParameters miqp = new MeetingInfoQueryParameters();
		if (inputBeginDate != null) {
			miqp.setInputBeginDate(Timestamp.valueOf(inputBeginDate));
		}
		if (inputEndDate != null) {
			miqp.setInputEndDate(Timestamp.valueOf(inputEndDate));
		}
		miqp.addOrder("beginTime", false);
		// 查询
		ListPage<MeetingInfo> meetingInfoListPage = meetingInfoBiz.queryMeetingOrderInfo(miqp);

		// 转DOC
		MeetingInfoListPage meetingInfoListPageDTO = new MeetingInfoListPage(meetingInfoListPage);
		Document xmlDoc = meetingInfoListPageDTO.toDocument();
		XMLResponse.outputXML(response, xmlDoc);
	}
}
