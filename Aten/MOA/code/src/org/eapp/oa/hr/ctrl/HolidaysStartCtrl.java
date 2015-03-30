package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.hr.blo.IHolidayTypeBiz;
import org.eapp.oa.hr.dto.HolidayApplyList;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 处理启动请假单的请求
 * @version
 */
public class HolidaysStartCtrl extends HttpServlet {

	private static final long serialVersionUID = -5572557105969757507L;
	private IHolidayTypeBiz holidayTypeBiz;
	private IHolidayApplyBiz holidayApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public HolidaysStartCtrl() {
		super();
	}

	public void init() throws ServletException {
		holidayApplyBiz = (IHolidayApplyBiz) SpringHelper.getSpringContext().getBean(
				"holidayApplyBiz");
		holidayTypeBiz = (IHolidayTypeBiz) SpringHelper.getSpringContext().getBean(
				"holidayTypeBiz");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			//草稿列表查询
			queryHolidayApplyDraft(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增草稿页面
			initAddHolidayApplyDraft(request, response);
			return;
		} else if("initHolidayEdit".equals(action)) {
			//初始化添加假期明细页面
			initHolidayEdit(request, response);
			return;
		} else if ("calcDays".equalsIgnoreCase(action)) {
			//计算日期
			calcDays(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			initModifyHolidayApplyDraft(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除草稿
			deleteHolidayApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//启动流程
			startFlow(request, response);
			return;
		} else if ((SysConstants.ADD + "_can").equalsIgnoreCase(action)) {
			//启动销假流程
			startCancolFlow(request, response);
			return;
//		} else if("initCancelHolidayEdit".equals(action)) {
//			//初始化添加假期明细页面
//			initCancelHolidayEdit(request, response);
//			return;
		}
	}
	
//	private void initCancelHolidayEdit(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String id = HttpRequestHelper.getParameter(request, "id");
//		HolidayApply holidayApply = holidayApplyBiz.findById(id);
//		request.setAttribute("holidayApply", holidayApply);
//		request.getRequestDispatcher("/page/hr/holidays/CancelHolidayEdit.jsp").forward(request, response);
//	}
	
	private void queryHolidayApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		List<HolidayApply> holidayApplyDrafts = holidayApplyBiz.getHolidayApplys(user.getAccountID(), HolidayApply.STATUS_DRAFT);
		
		XMLResponse.outputXML(response, new HolidayApplyList(holidayApplyDrafts).toDocument());
	}
	
	private void initAddHolidayApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		HolidayApply holidayApplyDraft = new HolidayApply();
		holidayApplyDraft.setApplyDate(new Date());
		holidayApplyDraft.setApplicant(user.getAccountID());
		holidayApplyDraft.setRegional(Reimbursement.DEFAULT_FINACE);
		request.setAttribute("depts", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("holidayApply", holidayApplyDraft);
		request.getRequestDispatcher("/page/hr/holidays/draft_holi.jsp").forward(request, response);
	}
	
	private void initHolidayEdit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<HolidayType> holiTypes = holidayTypeBiz.getAllHolidayTypes();
		request.setAttribute("holiTypes", holiTypes);
		request.getRequestDispatcher("/page/hr/holidays/edit_holi.jsp").forward(request, response);
	}
	
	/**
	 * 根据假期类型和请假时间，计算请假的天数
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void calcDays(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String holidayName = HttpRequestHelper.getParameter(request, "holidayName");
		String startDate = HttpRequestHelper.getParameter(request, "startDate");
		String startTime = HttpRequestHelper.getParameter(request, "startTime");
		
		String endDate = HttpRequestHelper.getParameter(request, "endDate");
		String endTime = HttpRequestHelper.getParameter(request, "endTime");
		
		//将时间参数转换为Timestamp对象
		Date start = Timestamp.valueOf(DataFormatUtil.formatTime(startDate));
		Date end = Timestamp.valueOf(DataFormatUtil.formatTime(endDate));
		

		Double days = holidayTypeBiz.getDaysOfHoliday(holidayName, start, startTime, end, endTime);
		XMLResponse.outputStandardResponse(response, 1, days.toString());
	}
	
	private void deleteHolidayApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		holidayApplyBiz.txDelHolidayApply(id);
		XMLResponse.outputStandardResponse(response, 1, "删除成功");
	}
	
	private void initModifyHolidayApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");
		HolidayApply holidayApply = holidayApplyBiz.getHolidayApplyById(id);
		if (holidayApply == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("holidayApply", holidayApply);
		request.setAttribute("depts", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("holiJson", JSON.toJSONStringWithDateFormat(holidayApply, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
		request.getRequestDispatcher("/page/hr/holidays/draft_holi.jsp").forward(request, response);
	}
	
	
	private void startCancolFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String holiId = HttpRequestHelper.getParameter(request, "id");
		if (holiId == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String json = HttpRequestHelper.getParameter(request, "json");
		
		List<HolidayDetail> details = JSON.parseArray(json, HolidayDetail.class);

		if (details == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写销假信息！");
			return;
		}
		try {
			//启动流程

			HolidayApply holi = holidayApplyBiz.txStartCancelFlow(holiId, details, user.getAccountID());
			//写入日志
			if (holi != null) {
				ActionLogger.log(request, holi.getId(), holi.toString());
			}
			XMLResponse.outputStandardResponse(response, 1, "请假单提交成功");
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
			return;
		}
	}
	
	private void startFlow(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String json = HttpRequestHelper.getParameter(request, "json");
		
		HolidayApply reDraft = JSON.parseObject(json, HolidayApply.class);
		
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启去流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写请假单！");
			return;
		}

		try {
			if (reDraft.getHolidayDetail() == null || reDraft.getHolidayDetail().size() < 1) {
				XMLResponse.outputStandardResponse(response, 0, "请添加假期明细!");
				return ;
			}
			//启动流程
			if ("Y".equals(flag)) {
				HolidayApply holi = holidayApplyBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (holi != null) {
					ActionLogger.log(request, holi.getId(), holi.toString());
				}
				XMLResponse.outputStandardResponse(response, 1,  holi.getId());
			} else {
				HolidayApply holi = holidayApplyBiz.txAddOrModifyHolidayApplyDraft(reDraft, user.getAccountID());
				XMLResponse.outputStandardResponse(response, 1,  holi.getId());
			}
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
			return;
		}
	}
}
