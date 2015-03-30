package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dto.StaffFlowApplyList;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;

/**
 * 处理启动请假单的请求
 * @version
 */
public class StaffFlowStartCtrl extends HttpServlet {

	private static final long serialVersionUID = -5572557105969757507L;
	private IStaffFlowApplyBiz staffFlowApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowStartCtrl() {
		super();
	}

	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
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
			queryStaffFlowApplyDraft(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增草稿页面
			initAddStaffFlowApplyDraft(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			initModifyStaffFlowApplyDraft(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除草稿
			deleteStaffFlowApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//启动流程
			startFlow(request, response);
			return;
		} else if ("entryinfo".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			loadEntryApplyInfo(request, response);
			return;
		}
	}
	

	
	private void queryStaffFlowApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		List<StaffFlowApply> staffFlowApplyDrafts = staffFlowApplyBiz.getStaffFlowApplys(user.getAccountID(), StaffFlowApply.STATUS_DRAFT);
		
		XMLResponse.outputXML(response, new StaffFlowApplyList(staffFlowApplyDrafts).toDocument());
	}
	
	private void initAddStaffFlowApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int applyType = HttpRequestHelper.getIntParameter(request, "applyType", 1);
		StaffFlowApply staffFlowApply = new StaffFlowApply();
		staffFlowApply.setApplyDate(new Date());
		staffFlowApply.setApplicant(user.getAccountID());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("staffFlowApply", staffFlowApply);
		if (StaffFlowApply.TYPE_ENTRY == applyType) {
			staffFlowApply.setEmployeeNumber(staffFlowApplyBiz.getMaxEmployeeNumber());
			request.getRequestDispatcher("/page/hr/staffflow/draft_entry.jsp").forward(request, response);
		} else if (StaffFlowApply.TYPE_RESIGN == applyType) {
			request.getRequestDispatcher("/page/hr/staffflow/draft_resign.jsp").forward(request, response);
		} else {
			//个人离职申请
			request.getRequestDispatcher("/page/hr/staffflow/draft_myresign.jsp").forward(request, response);
		}
	}

	private void deleteStaffFlowApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		staffFlowApplyBiz.txDelStaffFlowApply(id);
		XMLResponse.outputStandardResponse(response, 1, "删除成功");
	}
	
	private void initModifyStaffFlowApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		StaffFlowApply staffFlowApply = staffFlowApplyBiz.getStaffFlowApplyById(id);
		if (staffFlowApply == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("staffFlowApply", staffFlowApply);
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		if (StaffFlowApply.TYPE_ENTRY == staffFlowApply.getApplyType()) {
			request.getRequestDispatcher("/page/hr/staffflow/draft_entry.jsp").forward(request, response);
		} else {
			if (staffFlowApply.getApplicant().equals(staffFlowApply.getUserAccountID())) {
				//个人离职修改
				request.getRequestDispatcher("/page/hr/staffflow/draft_myresign.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/page/hr/staffflow/draft_resign.jsp").forward(request, response);
			}
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
		
		StaffFlowApply reDraft = JSON.parseObject(json, StaffFlowApply.class);
		
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启去流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写申请单！");
			return;
		}

		try {
			//启动流程
			if ("Y".equals(flag)) {
				StaffFlowApply holi = staffFlowApplyBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (holi != null) {
					ActionLogger.log(request, holi.getId(), holi.toString());
				}
				XMLResponse.outputStandardResponse(response, 1, holi.getId());
			} else {
				StaffFlowApply holi = staffFlowApplyBiz.txAddOrModifyStaffFlowApplyDraft(reDraft, user.getAccountID());
				XMLResponse.outputStandardResponse(response, 1, holi.getId());
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
	
	private void loadEntryApplyInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userAccountID = HttpRequestHelper.getParameter(request, "userAccountID");
		StaffFlowApply entry = staffFlowApplyBiz.getEntryApplyInfo(userAccountID);
		List<StaffFlowApply> entrys = null;
		if (entry != null) {
			entrys = new ArrayList<StaffFlowApply>();
			entrys.add(entry);
		}
		XMLResponse.outputXML(response, new StaffFlowApplyList(entrys).toDocument());
	}
}
