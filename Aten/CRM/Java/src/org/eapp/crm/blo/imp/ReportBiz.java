package org.eapp.crm.blo.imp;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.crm.blo.IReportBiz;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.dao.IReportNormConfDAO;
import org.eapp.crm.dao.IReportQueryAssignDAO;
import org.eapp.crm.dto.ReportAssignInfo;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.ReportNormConf;
import org.eapp.crm.hbean.ReportQueryAssign;
import org.eapp.crm.util.ReportUtils;
import org.eapp.crm.util.ReportUtils.TempParam;
import org.eapp.crm.util.script.ReportExpressionHelper;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;


public class ReportBiz implements IReportBiz {
	/**
     * log
     */
    public static final Log log = LogFactory.getLog(ReportBiz.class);
	/**
	 * 报表子模板文件路径
	 */
	private static final String SUB_TEMP_PREFIX = "$SUB_TEMP_NAME:";
	private DataSource dataSource;
	private IReportQueryAssignDAO reportQueryAssignDAO;
	private IReportNormConfDAO reportNormConfDAO;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setReportQueryAssignDAO(IReportQueryAssignDAO reportQueryAssignDAO) {
		this.reportQueryAssignDAO = reportQueryAssignDAO;
	}

	public void setReportNormConfDAO(IReportNormConfDAO reportNormConfDAO) {
		this.reportNormConfDAO = reportNormConfDAO;
	}

	@Override
	public String csExportRpt(String tempName, Map<String, Object> parameters, String exportType) throws Exception {
		// 获取报表，及报表对应的模板文件
		String absDestFileName = UUID.randomUUID().toString() + "." + exportType;
		String destFileName = FileDispatcher.getTempDir() + absDestFileName;
		String tempDir = FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(SysConstants.REPORT_TEMP_DIR));
		String tempFile = tempDir + tempName;
//		File tempFileName = FileDispatcher.findFile(rptTmp);
//		if (tempFileName == null) {
//			throw new ICISException("找不到报表模板");
//		}
		// 读取模板参数，并根据前台传送的值设置模板参数
		Map<String, Object> params = new HashMap<String, Object>();
		if (parameters != null) {
			List<TempParam> tempParamList = ReportUtils.readTempParams(tempFile);
			for (TempParam param : tempParamList) {
				String[] values = (String[])parameters.get(param.getParamName());
				if(values == null || values.length == 0) {
					continue;
				}
				String value = values[0];
				// 判断参数是否是子报表模板路径
				// 如果是子报表模板路径，先编译子报表为.jasper文件，再保存到参数表中。
				if (value.startsWith(SUB_TEMP_PREFIX)) {
					// 获取模板文件的路径
					String subTempPath = tempDir + value.substring(SUB_TEMP_PREFIX.length());
					// 生成报表的路径
					String subJasperPath = FileDispatcher.getTempDir() + UUID.randomUUID().toString() + ".jasper";
					ReportUtils.compileReport(subTempPath, subJasperPath);
					params.put(param.getParamName(), subJasperPath);
				} else {
					Object converObject = ReportExpressionHelper.convertVal(value, Class.forName(param.getClassName()));
					params.put(param.getParamName(), converObject);
				}
			}
		}
		// 生成报表
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (ReportUtils.EXPORT_HTML.equals(exportType)) {
				ReportUtils.exportToHtml(destFileName, tempFile, connection, params);
			} else if (ReportUtils.EXPORT_PDF.equals(exportType)) {
				ReportUtils.exportToPdf(destFileName, tempFile, connection, params);
			} else if (ReportUtils.EXPORT_XLS.equals(exportType)) {
				ReportUtils.exportToXls(destFileName, tempFile, connection, params);
			} else {
				throw new CrmException("不支持导出类型：" + exportType);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		// 返回生成报表文件的路径
		return FileDispatcher.getTempAbsDir() + absDestFileName;
	}
	
	@Override
	public ListPage<ReportAssignInfo> getUserAccountIdsByDeptAndAccount(String rptID, String userDeptName, String userAccountQueryString, 
			int pageNo, int pageSize) {
		ListPage<ReportAssignInfo> page = new ListPage<ReportAssignInfo>();
		try {
			
		 	UserAccountService uas = new UserAccountService();
		 	if (StringUtils.isBlank(userDeptName)) {
		 		userDeptName = null;
		 	}
		 	if (StringUtils.isBlank(userAccountQueryString)) {
		 		userAccountQueryString = null;
		 	}
		 	List<UserAccountInfo> users =  uas.queryUserAccounts(userDeptName, userAccountQueryString);
		 	if (users == null) {
	            return page;
	        }
	        int totalCount = users.size();// 总记录数
	        int firstResultIndex = (pageNo - 1) * pageSize;

	        if (firstResultIndex > totalCount || totalCount == 0) {// 开始记录大于总记录数
	            return page;
	        }
	        int endResultIndex = firstResultIndex + pageSize;
	        if (endResultIndex > totalCount) {
	        	endResultIndex = totalCount;
	        }
	        //翻页
		 	List<ReportAssignInfo> dataList = new ArrayList<ReportAssignInfo>();
		 	for (int i = firstResultIndex; i < endResultIndex; i++) {
		 		ReportAssignInfo si = new ReportAssignInfo();
		 		UserAccountInfo u = users.get(i);
		 		List<ReportQueryAssign> assigns = reportQueryAssignDAO.findQueryAssigns(rptID, u.getAccountID());
		 		StringBuffer assignStr = new StringBuffer();
				if (assigns != null && !assigns.isEmpty()) {
					for (ReportQueryAssign as : assigns) {
						assignStr.append(as.getGroupName()).append(",");
					}
				}
		 		si.setUser(u);
		 		si.setAssignValue(assignStr.toString());
		 		dataList.add(si);
		 	}
		 	
		 	
		 	page.setCurrentPageNo(pageNo);
		 	page.setCurrentPageSize(pageSize);
		 	page.setTotalCount(totalCount);
		 	page.setDataList(dataList);
		 	return page;
		 } catch (MalformedURLException e) {
		     log.error("getUserAccountByGroup faild", e);
		 } catch (RpcAuthorizationException e) {
		     log.error("getUserAccountByGroup faild", e);
		 }
		 return page;
	}
	
	@Override
	public void txSaveReportQueryAssign(String rptID, String userAccountId, String[] groupNames) {
		reportQueryAssignDAO.deleteQueryAssign(rptID, userAccountId);
		if (groupNames == null) {
			return;
		}
		//排除重复
		Set<String> groupNameSet = new HashSet<String>();
		groupNameSet.addAll(Arrays.asList(groupNames));
		for (String groupName : groupNameSet) {
			if (StringUtils.isBlank(groupName)) {
				continue;
			}
			ReportQueryAssign assign = new ReportQueryAssign();
			assign.setGroupName(groupName);
			assign.setUserAccountID(userAccountId);
			assign.setRptID(rptID);
			reportQueryAssignDAO.save(assign);
		}
		
	}

	@Override
	public List<ReportQueryAssign> getQueryAssigns(String rptID, String userAccountId) {
		return reportQueryAssignDAO.findQueryAssigns(rptID, userAccountId);
	}
	
	@Override
	public List<ReportNormConf> getNormConfs(String rptID) {
		return reportNormConfDAO.findByRptID(rptID);
	}
	
	@Override
	public void modifyNormConfs(String rptID, String normStr) {
		if (StringUtils.isBlank(normStr)) {
			return;
		}
		String[] norms = normStr.split(";");
		if (norms == null || norms.length ==0) {
			return;
		}
		List<ReportNormConf> normConfs = reportNormConfDAO.findByRptID(rptID);
		if (normConfs == null || normConfs.isEmpty()) {
			return;
		}
		Map<String, ReportNormConf> normMap = new HashMap<String, ReportNormConf>();
		for (ReportNormConf rnc : normConfs) {
			normMap.put(rnc.getNormCode(), rnc);
		}
		for (String norm : norms) {
			String[] normItem = norm.split("=");
			if (normItem.length != 2) {
				continue;
			}
			ReportNormConf conf = normMap.get(normItem[0]);
			if (conf == null) {
				continue;
			}
			conf.setNormValue(normItem[1]);
			reportNormConfDAO.save(conf);
		}
	}
}
