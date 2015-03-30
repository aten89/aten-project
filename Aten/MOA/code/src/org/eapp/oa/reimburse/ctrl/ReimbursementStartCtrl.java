package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.reimburse.blo.IReimbursementBiz;
import org.eapp.oa.reimburse.dto.ReimbursementList;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.ctrl.ActionLogger;
import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.spring.SpringHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;



/**
 * 处理启动报销单的请求
 * 包括报销单草稿
 * @author zsy
 * @version
 */
public class ReimbursementStartCtrl extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	private IReimbursementBiz reimbursementBiz;
	
	/**
	 * Constructor of the object.
	 */
	public ReimbursementStartCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
        reimbursementBiz = (IReimbursementBiz) SpringHelper.getSpringContext().getBean("reimbursementBiz");
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
			//草稿报销列表
			queryReimbursement(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增报销单草稿页面
			initAddReimbursement(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改报销单草稿页面
			initModifyReimbursement(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(action)) {
			//删除报销单草稿
			deleteReimbursement(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//启动流程
			startFlow(request, response);
			return;
		} else if ("costclass".equalsIgnoreCase(action)) {
			//费用类别
			getCostClassSel(request, response);
			return;
		} else if ("costitem".equalsIgnoreCase(action)) {
			//费用子类别
			getCostItemSel(request, response);
			return;
		}
	}
	
	
	private void getCostClassSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, DataDictInfo> dmap = SysCodeDictLoader.getInstance().getCostClassMap();
		HTMLResponse.outputHTML(response, new DataDictionarySelect(dmap.values()).toString());
	}
	
	private void getCostItemSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String costClass = HttpRequestHelper.getParameter(request, "costclass");
		Map<String, DataDictInfo> dmap = SysCodeDictLoader.getInstance().getCostClassMap();
		DataDictInfo dict = dmap.get(costClass);
		HTMLResponse.outputHTML(response, new DataDictionarySelect(dict != null ? dict.getChildDataDicts() : null).toString());
	}
	
	private void queryReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		List<Reimbursement> reis = reimbursementBiz.getReimbursements(user.getAccountID(), Reimbursement.STATUS_DRAFT);
		XMLResponse.outputXML(response, new ReimbursementList(reis).toDocument());
	}
	
	private void initAddReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		Reimbursement reDraft = new Reimbursement();
		reDraft.setApplyDate(new Timestamp(System.currentTimeMillis()));
		reDraft.setApplicant(user.getAccountID());
		reDraft.setPayee(user.getAccountID());
//		reDraft.setApplyDept(user.getGroupNames());
		reDraft.setFinance(Reimbursement.DEFAULT_FINACE);
		
		request.setAttribute("reimDraft", reDraft);
		request.setAttribute("groups", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.getRequestDispatcher("/page/cost/reimburse/draft_reim.jsp").forward(request, response);
	}
	
	private void deleteReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		reimbursementBiz.txDeleteReimbursement(id);
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
		
		Reimbursement reDraft = JSON.parseObject(json, Reimbursement.class);
		
		String flag = HttpRequestHelper.getParameter(request, "flag");//是否立即启动流程
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写报销单！");
			return;
		}
		try {
			if (reDraft.getReimItems() == null || reDraft.getReimItems().size() < 1) {
				XMLResponse.outputStandardResponse(response, 0, "请添加费用明细!");
				return ;
			}
			//启动流程
			if ("Y".equals(flag)) {
			    
				Reimbursement reis = reimbursementBiz.txStartFlow(reDraft, user.getAccountID());
				//写入日志
				if (reis != null) {
					ActionLogger.log(request, reis.getId(), reis.toString());
				}
				XMLResponse.outputStandardResponse(response, 1, "报销单提交成功");
				
			} else {
				
				reimbursementBiz.txAddOrModifyReimbursement(reDraft, user.getAccountID());
				
				XMLResponse.outputStandardResponse(response, 1, "保存成功");
			}
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch(NumberFormatException e) {
			XMLResponse.outputStandardResponse(response, 0, "数字格式出错");
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
			return;
		}
	}
	
	
	private void initModifyReimbursement(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		
		if (user == null) {
			request.setAttribute("errorMsg", "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		
		Reimbursement reDraft = reimbursementBiz.getReimbursementById(id);
		if (reDraft == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG,"记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("reimDraft", reDraft);
		request.setAttribute("groups", user.getDepts());
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
//		System.out.println(JSON.toJSONStringWithDateFormat(reDraft, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
		request.setAttribute("reimJson", JSON.toJSONStringWithDateFormat(reDraft, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
		request.getRequestDispatcher("/page/cost/reimburse/draft_reim.jsp").forward(request, response);
	}

}
