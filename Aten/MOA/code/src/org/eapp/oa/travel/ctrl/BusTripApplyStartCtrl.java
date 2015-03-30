package org.eapp.oa.travel.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dto.BusTripApplyList;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BusTripApplyStartCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6856515205098265366L;

	private IBusTripApplyBiz busTripApplyBiz;
	
	public BusTripApplyStartCtrl() {
		super();
	}

	public void init() throws ServletException {
		busTripApplyBiz = (IBusTripApplyBiz) SpringHelper.getSpringContext().getBean("busTripApplyBiz");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(action)) {
			// 草稿列表查询
			queryBusTripApplyDraft(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			// 转到新增草稿页面
			initAddBusTripApplyDraft(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			// 转到修改草稿页面
			initModifyBusTripApplyDraft(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			// 删除草稿
			deleteBusTripApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			// 启动流程
			startFlow(request, response);
			return;
		}
	}
	
	private void initAddBusTripApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		BusTripApply busTripApplyDraft = new BusTripApply();
		busTripApplyDraft.setApplyDate(new Date());
		busTripApplyDraft.setApplicant(user.getAccountID());
		busTripApplyDraft.setRegional(Reimbursement.DEFAULT_FINACE);
		request.setAttribute("depts", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("busTripApply", busTripApplyDraft);
		request.getRequestDispatcher("/page/travel/approval/draft_travel.jsp").forward(request, response);
	}
	
	private void queryBusTripApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}		
		List<BusTripApply> busTripApplyDrafts = busTripApplyBiz.getBusTripApplys(user.getAccountID(), BusTripApply.STATUS_DRAFT);		
		XMLResponse.outputXML(response, new BusTripApplyList(busTripApplyDrafts).toDocument());
	}
	
	private void initModifyBusTripApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		BusTripApply busTripApply = busTripApplyBiz.getBusTripApplyById(id);
		if (busTripApply == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("busTripApply", busTripApply);
		request.setAttribute("depts", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("tripJson", JSON.toJSONStringWithDateFormat(busTripApply, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
		request.getRequestDispatcher("/page/travel/approval/draft_travel.jsp").forward(request, response);
	}
	
	
	private void deleteBusTripApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		busTripApplyBiz.txDelBusTripApply(id);
		XMLResponse.outputStandardResponse(response, 1, "删除成功");
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
		BusTripApply reDraft = JSON.parseObject(json, BusTripApply.class);
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启去流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写出差申请单！");
			return;
		}

		try {
			if (reDraft.getBusTripApplyDetail() == null || reDraft.getBusTripApplyDetail().size() < 1) {
				XMLResponse.outputStandardResponse(response, 0, "请添加出差明细!");
				return ;
			}
			//启动流程
			if ("Y".equals(flag)) {
				BusTripApply holi = busTripApplyBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (holi != null) {
					ActionLogger.log(request, holi.getId(), holi.toString());
				}
				XMLResponse.outputStandardResponse(response, 1, "出差申请单提交成功");
			} else {
				busTripApplyBiz.txAddOrModifyApplyDraft(reDraft, user.getAccountID());
				XMLResponse.outputStandardResponse(response, 1, "出差申请单草稿保存成功");
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
