package org.eapp.crm.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.IReportBiz;
import org.eapp.crm.dto.ReportAssignInfo;
import org.eapp.crm.dto.ReportAssignSelect;
import org.eapp.crm.dto.UserAccountInfoSelect;
import org.eapp.crm.hbean.ReportNormConf;
import org.eapp.crm.hbean.ReportQueryAssign;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

import com.opensymphony.xwork2.ActionContext;
public class ReportAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7355961603989992352L;
	private static final Log log = LogFactory.getLog(ReportAction.class);
	
	private IReportBiz reportBiz;
	
	private String tempName;
	private String exportType;
	private int pageNo;
    private int pageSize;
    private String userDeptName;
    private String userAccountId;
    private String groupNameStr;
    private String rptID;
    private String normStr;

	private String reportPath;
	private ListPage<ReportAssignInfo> assignInfoPage;
	private List<ReportQueryAssign> queryAssigns;
	private List<ReportNormConf> normConfs;
	private String htmlValue;
	
	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public void setGroupNameStr(String groupNameStr) {
		this.groupNameStr = groupNameStr;
	}

	public void setRptID(String rptID) {
		this.rptID = rptID;
	}
	
	public void setNormStr(String normStr) {
		this.normStr = normStr;
	}

	public String getReportPath() {
		return reportPath;
	}

	public ListPage<ReportAssignInfo> getAssignInfoPage() {
		return assignInfoPage;
	}

	public List<ReportQueryAssign> getQueryAssigns() {
		return queryAssigns;
	}

	public List<ReportNormConf> getNormConfs() {
		return normConfs;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	public String exportRpt() {
		if (tempName == null || exportType == null) {
			return error("参数不能为空");
		}
		try {
			ActionContext context = ActionContext.getContext();   
	        Map<String, Object>  parameters = context.getParameters();
//	        for (String k : parameters.keySet()) {
//	        	System.out.println(k + " = " + ((String[])parameters.get(k))[0]);
//	        }
			reportPath = reportBiz.csExportRpt(tempName, parameters, exportType);
			return success();
		} catch(Exception e) {
			log.error("", e);
			return error("系统出错");
		}
	}
	
	
	public String queryReportAssign() {
		if (StringUtils.isBlank(rptID)) {
			return error("参数不能为空");
		}
		try {
			assignInfoPage = reportBiz.getUserAccountIdsByDeptAndAccount(rptID, userDeptName, userAccountId, pageNo, pageSize);
			return success();
		} catch(Exception e) {
			log.error("", e);
			return error("系统出错");
		}
	}
	
	public String saveReportAssign() {
		if (StringUtils.isBlank(rptID) || StringUtils.isBlank(userAccountId)) {
			return error("参数不能为空");
		}
		
		
		try {
			String[] groupNames = null;
			if (StringUtils.isNotBlank(groupNameStr)) {
				groupNames = groupNameStr.split(",");
			}
			reportBiz.txSaveReportQueryAssign(rptID, userAccountId, groupNames);
			return success();
		} catch(Exception e) {
			log.error("", e);
			return error("系统出错");
		}
	}
	
	public String loadAssignDeptSelect() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		List<ReportQueryAssign> depts = reportBiz.getQueryAssigns(rptID, user.getAccountID());
		htmlValue = new ReportAssignSelect(depts).toString();
		return success();
	}
	
	public String loadAssignDepts() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SystemProperties.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登入");
		}
		queryAssigns = reportBiz.getQueryAssigns(rptID, user.getAccountID());
		return success();
	}
	
	public String loadDeptUserSelect() {
		UserAccountService uas = new UserAccountService();
		try {
			List<UserAccountInfo> userList = new ArrayList<UserAccountInfo>();
			if (StringUtils.isNotBlank(userDeptName)) {
				String[] nameArr = userDeptName.split(",");
				for (String name : nameArr) {
					if (StringUtils.isBlank(name)) {
						continue;
					}
					userList.addAll(uas.queryUserAccounts(name, null));
				}
			}
//			List<UserAccountInfo> users = uas.queryUserAccounts(userDeptName, null);
			htmlValue = new UserAccountInfoSelect(userList).toString();
			return success();
		} catch(Exception e) {
			log.error("", e);
			return error("系统出错");
		}
	}
	
	public String initNormConf() {
		normConfs = reportBiz.getNormConfs(rptID);
		return success();
	}
	
	public String saveReportNorm() {
		if (StringUtils.isBlank(rptID)) {
			return error("参数不能为空");
		}
		try {
			reportBiz.modifyNormConfs(rptID, normStr);
			return success();
		} catch(Exception e) {
			log.error("", e);
			return error("系统出错");
		}
	}
}
