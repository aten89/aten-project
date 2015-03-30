package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IPositiveApplyBiz;
import org.eapp.oa.hr.dto.PositiveApplyList;
import org.eapp.oa.hr.hbean.PositiveApply;
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
public class PositiveStartCtrl extends HttpServlet {

	private static final long serialVersionUID = -5572557105969757507L;
	private IPositiveApplyBiz positiveApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public PositiveStartCtrl() {
		super();
	}

	public void init() throws ServletException {
		positiveApplyBiz = (IPositiveApplyBiz) SpringHelper.getSpringContext().getBean(
				"positiveApplyBiz");
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
			queryPositiveApplyDraft(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增草稿页面
			initAddPositiveApplyDraft(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			initModifyPositiveApplyDraft(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除草稿
			deletePositiveApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//启动流程
			startFlow(request, response);
			return;
		}
	}
	
	private void queryPositiveApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		List<PositiveApply> holidayApplyDrafts = positiveApplyBiz.getPositiveApplys(user.getAccountID(), PositiveApply.STATUS_DRAFT);
		
		XMLResponse.outputXML(response, new PositiveApplyList(holidayApplyDrafts).toDocument());
	}
	
	private void initAddPositiveApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		PositiveApply holidayApplyDraft = new PositiveApply();
		holidayApplyDraft.setApplyDate(new Date());
		holidayApplyDraft.setApplicant(user.getAccountID());
		request.setAttribute("positiveApply", holidayApplyDraft);
		request.getRequestDispatcher("/page/hr/positive/draft_posi.jsp").forward(request, response);
	}
	
	private void deletePositiveApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		positiveApplyBiz.txDelPositiveApply(id);
		XMLResponse.outputStandardResponse(response, 1, "删除成功");
	}
	
	private void initModifyPositiveApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");
		PositiveApply holidayApply = positiveApplyBiz.getPositiveApplyById(id);
		if (holidayApply == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("positiveApply", holidayApply);
		request.getRequestDispatcher("/page/hr/positive/draft_posi.jsp").forward(request, response);
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
		
		PositiveApply reDraft = JSON.parseObject(json, PositiveApply.class);
		
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启去流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写请假单！");
			return;
		}

		try {
			//启动流程
			if ("Y".equals(flag)) {
				PositiveApply holi = positiveApplyBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (holi != null) {
					ActionLogger.log(request, holi.getId(), holi.toString());
				}
				XMLResponse.outputStandardResponse(response, 1,  holi.getId());
			} else {
				PositiveApply holi = positiveApplyBiz.txAddOrModifyPositiveApplyDraft(reDraft, user.getAccountID());
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
