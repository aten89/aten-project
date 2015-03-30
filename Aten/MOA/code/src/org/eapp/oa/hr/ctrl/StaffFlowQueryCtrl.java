package org.eapp.oa.hr.ctrl;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dto.StaffAssignInfo;
import org.eapp.oa.hr.dto.StaffFlowApplyQueryPage;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.dto.StaffAssignInfoPage;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.upload.FileDispatcher;

import com.alibaba.fastjson.JSON;

/**
 * 处理归档请假单的请求
 * @version
 */
public class StaffFlowQueryCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572557105969757507L;
	
	private IStaffFlowApplyBiz staffFlowApplyBiz;
//	private IAddressListBiz addressListBiz;
	/**
	 * Constructor of the object.
	 */
	public StaffFlowQueryCtrl() {
		super();
	}

	/**
	 * 
	 */
	public void init() throws ServletException {
		staffFlowApplyBiz = (IStaffFlowApplyBiz) SpringHelper.getSpringContext().getBean("staffFlowApplyBiz");
//		addressListBiz = (IAddressListBiz) SpringHelper.getBean("addressListBiz");
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
			queryStaffFlowApply(request, response);
			return;
		} else if (SysConstants.EXPORT.equalsIgnoreCase(action)) {
			//导出
			exportStaffFlowApply(request, response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(action)) {
			//查看
			viewStaffFlowApply(request, response);
		} else if ("initadd".equalsIgnoreCase(action)) {
			//转到新增草稿页面
			initAddStaffFlowApplyDraft(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(action)) {
			//转到修改草稿页面
			initModifyStaffFlowApplyDraft(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(action)) {
			//新增
			addStaffFlowApply(request, response);
			return;
		} else if ("loadusers".equalsIgnoreCase(action)) {
			//查询系统用户
			queryAllUsers(request, response);
			return;
//		} else if ("loadassigns".equalsIgnoreCase(action)) {
//			//查询系统用户
//			queryStaffFlowAssigns(request, response);
//			return;
		} else if (SysConstants.ASSIGN.equalsIgnoreCase(action)) {
			//保存授权
			saveStaffFlowAssign(request, response);
			return;
		} else if ("query_my".equalsIgnoreCase(action)) {
			//查询
			queryMyStaffFlowApply(request, response);
			return;
		}
	}
	
	private void queryStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String queryType = HttpRequestHelper.getParameter( request, "type" );
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffFlowApply> page = null;
//			if ("1".equals(queryType)) {
				StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
				rqp.setPageNo(pageNo);
				rqp.setPageSize(pageSize);
//				rqp.setApplyType(StaffFlowApply.TYPE_RESIGN);
				//直接以本地入职表为准
				rqp.setApplyType("1".equals(queryType) ? StaffFlowApply.TYPE_RESIGN : StaffFlowApply.TYPE_ENTRY);
				
				rqp.setGroupName(userDeptName);
				rqp.setUserKeyword(queryString);
					
				rqp.addOrder("userAccountID", true);
				
				page = staffFlowApplyBiz.queryStaffFlowApply(rqp);
//			} else {
//				List<String> uids = addressListBiz.getUserAccountIdsByDeptAndAccount(userDeptName, queryString);
//				List<StaffFlowApply> staffList = staffFlowApplyBiz.getStaffFlowApplyByAccountIds(uids, pageNo, pageSize);
//				page = new ListPage<StaffFlowApply>();
//				page.setTotalCount(uids.size());
//				page.setDataList(staffList);
//				page.setCurrentPageNo(pageNo);
//				page.setCurrentPageSize(pageSize);
//			}
			XMLResponse.outputXML(response, new StaffFlowApplyQueryPage(page).toDocument());
//		} catch(OaException e) {
//			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
//			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
    private void exportStaffFlowApply(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    		IOException {

		// 获取参数
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", -1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
		String queryType = HttpRequestHelper.getParameter( request, "type" );
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		
		String expNameAndValue = HttpRequestHelper.getParameter(request, "expNameAndValue");
		// 构造查询条件
		StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
		rqp.setPageNo(pageNo);
		rqp.setPageSize(pageSize);
//		rqp.setApplyType(StaffFlowApply.TYPE_RESIGN);
		//直接以本地入职表为准
		rqp.setApplyType("1".equals(queryType) ? StaffFlowApply.TYPE_RESIGN : StaffFlowApply.TYPE_ENTRY);
		
		rqp.setGroupName(userDeptName);
		rqp.setUserKeyword(queryString);
			
		rqp.addOrder("userAccountID", true);
		
		// 创建一个文件名
		String fileName = request.getSession().getId() + System.currentTimeMillis() / 10000 + ".xls";
		try {
			staffFlowApplyBiz.csExportStaffFlowApply(rqp, expNameAndValue, FileDispatcher.getTempDir() + fileName);
		    XMLResponse.outputStandardResponse(response, 1, FileDispatcher.getTempAbsDir() + fileName);
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		} catch (Exception e) {
		    e.printStackTrace();
		    XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}

}
	
	public void viewStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");//请假单号
		try {
			StaffFlowApply rei = staffFlowApplyBiz.getStaffFlowApplyById(id);
			if (rei == null) {
				request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "入职单不存在");
				request.getRequestDispatcher("/page/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("staffFlowApply", rei);
			if( rei.getApplyType() == StaffFlowApply.TYPE_ENTRY) {
				request.getRequestDispatcher("/page/hr/staffflow/query/view_entry.jsp").forward(request, response);
			} else {
				//如果是离职单，查找对应在的入职单信息
				StaffFlowApply resign = staffFlowApplyBiz.getStaffFlowApplyByUserAccountID(rei.getUserAccountID());
				request.setAttribute("refStaffFlowApply", resign);
				request.getRequestDispatcher("/page/hr/staffflow/query/view_resign.jsp").forward(request, response);
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
	private void initAddStaffFlowApplyDraft(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		StaffFlowApply staffFlowApply = new StaffFlowApply();
		staffFlowApply.setApplyDate(new Date());
		staffFlowApply.setApplicant(user.getAccountID());
		staffFlowApply.setApplyType(StaffFlowApply.TYPE_ENTRY);
		request.setAttribute("areas", SysCodeDictLoader.getInstance().getAreaMap().values());
		request.setAttribute("staffFlowApply", staffFlowApply);
		request.getRequestDispatcher("/page/hr/staffflow/query/draft_entry.jsp").forward(request, response);
		
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
			request.getRequestDispatcher("/page/hr/staffflow/query/draft_entry.jsp").forward(request, response);
		}
	}
	
	private void addStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String json = HttpRequestHelper.getParameter(request, "json");
		
		StaffFlowApply reDraft = JSON.parseObject(json, StaffFlowApply.class);
		
		if (reDraft == null) {
			XMLResponse.outputStandardResponse(response, 0, "请填写申请单！");
			return;
		}

		try {
			staffFlowApplyBiz.txAddOrModifyStaffFlowApplyDraft(reDraft, user.getAccountID());
			XMLResponse.outputStandardResponse(response, 1, "草稿保存成功");
		} catch(OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
			return;
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
			return;
		}
	}
	
	public void queryAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffAssignInfo> page = staffFlowApplyBiz.getUserAccountIdsByDeptAndAccount(userDeptName, queryString, pageNo, pageSize);
			XMLResponse.outputXML(response, new StaffAssignInfoPage(page).toDocument());
			
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
//	public void queryStaffFlowAssigns(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String userAccountId = HttpRequestHelper.getParameter( request, "userAccountId" );
//		
//		try {
//			List<StaffFlowQueryAssign> assigns = staffFlowApplyBiz.queryStaffFlowQueryAssigns(userAccountId);
//			StringBuffer assignStr = new StringBuffer();
//			if (assigns != null && !assigns.isEmpty()) {
//				for (StaffFlowQueryAssign as : assigns) {
//					assignStr.append(as.getGroupName()).append(",");
//				}
//			}
//			
//			XMLResponse.outputStandardResponse(response, 1, assignStr.toString());
//		} catch(Exception e) {
//			e.printStackTrace();
//			XMLResponse.outputStandardResponse(response, 0, "系统出错");
//			return;
//		}
//	}
	
	private void saveStaffFlowAssign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userAccountId = HttpRequestHelper.getParameter( request, "userAccountId" );
		String groupNameStr = HttpRequestHelper.getParameter( request, "groupNameStr" );
		
		try {
			String[] groupNames = null;
			if (groupNameStr != null) {
				groupNames = groupNameStr.split(",");
			}
			staffFlowApplyBiz.txSaveStaffFlowAssign(userAccountId, groupNames);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "保存失败");
			return;
		}
	}
	
	private void queryMyStaffFlowApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
			.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
	
		String userDeptName = HttpRequestHelper.getParameter( request, "userDeptName" );
		String queryString = HttpRequestHelper.getParameter( request, "userAccountId" );
		
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		
		try {
			ListPage<StaffFlowApply> page = null;
			StaffFlowQueryParameters rqp = new StaffFlowQueryParameters();
			rqp.setPageNo(pageNo);
			rqp.setPageSize(pageSize);
			rqp.setApplyType(StaffFlowApply.TYPE_ENTRY);
			
			rqp.setGroupName(userDeptName);
			rqp.setUserKeyword(queryString);
			rqp.addOrder("userAccountID", true);
			
			page = staffFlowApplyBiz.queryMyStaffFlowApplys(rqp, user.getAccountID());

			XMLResponse.outputXML(response, new StaffFlowApplyQueryPage(page).toDocument());
		} catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
}
