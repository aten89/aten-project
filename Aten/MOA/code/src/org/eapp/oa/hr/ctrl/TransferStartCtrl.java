package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.ITransferApplyBiz;
import org.eapp.oa.hr.dto.TransferApplyList;
import org.eapp.oa.hr.hbean.TransferApply;
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
public class TransferStartCtrl extends HttpServlet {

	private static final long serialVersionUID = -5572557105969757507L;
	private ITransferApplyBiz transferApplyBiz;
	/**
	 * Constructor of the object.
	 */
	public TransferStartCtrl() {
		super();
	}

	public void init() throws ServletException {
		transferApplyBiz = (ITransferApplyBiz) SpringHelper.getSpringContext().getBean(
				"transferApplyBiz");
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
			queryTransferApplyDraft(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增草稿页面
			initAddTransferApplyDraft(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			initModifyTransferApplyDraft(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除草稿
			deleteTransferApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//启动流程
			startFlow(request, response);
			return;
		}
	}
	
	private void queryTransferApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		List<TransferApply> holidayApplyDrafts = transferApplyBiz.getTransferApplys(user.getAccountID(), TransferApply.STATUS_DRAFT);
		
		XMLResponse.outputXML(response, new TransferApplyList(holidayApplyDrafts).toDocument());
	}
	
	private void initAddTransferApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		TransferApply holidayApplyDraft = new TransferApply();
		holidayApplyDraft.setApplyDate(new Date());
		holidayApplyDraft.setApplicant(user.getAccountID());
		request.setAttribute("transferApply", holidayApplyDraft);
		request.getRequestDispatcher("/page/hr/transfer/draft_tran.jsp").forward(request, response);
	}
	
	private void deleteTransferApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		transferApplyBiz.txDelTransferApply(id);
		XMLResponse.outputStandardResponse(response, 1, "删除成功");
	}
	
	private void initModifyTransferApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request, "id");
		TransferApply holidayApply = transferApplyBiz.getTransferApplyById(id);
		if (holidayApply == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("transferApply", holidayApply);
		request.getRequestDispatcher("/page/hr/transfer/draft_tran.jsp").forward(request, response);
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
		
		TransferApply reDraft = JSON.parseObject(json, TransferApply.class);
		
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启去流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写请假单！");
			return;
		}

		try {
			//启动流程
			if ("Y".equals(flag)) {
				TransferApply holi = transferApplyBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (holi != null) {
					ActionLogger.log(request, holi.getId(), holi.toString());
				}
				XMLResponse.outputStandardResponse(response, 1,  holi.getId());
			} else {
				TransferApply holi = transferApplyBiz.txAddOrModifyTransferApplyDraft(reDraft, user.getAccountID());
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
