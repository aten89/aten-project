package org.eapp.crm.blo;

import java.util.List;
import java.util.Map;

import org.eapp.crm.dto.ReportAssignInfo;
import org.eapp.crm.hbean.ReportNormConf;
import org.eapp.crm.hbean.ReportQueryAssign;
import org.eapp.util.hibernate.ListPage;

public interface IReportBiz {

	/**
	 * 导出报表
	 * @param tempName
	 * @param parameters
	 * @param exportType
	 * @return
	 * @throws Exception
	 */
	public String csExportRpt(String tempName, Map<String, Object> parameters, String exportType) throws Exception;
	
	public ListPage<ReportAssignInfo> getUserAccountIdsByDeptAndAccount(String rptID, String userDeptName, String userAccountQueryString, 
			int pageNo, int pageSize);
	
	public void txSaveReportQueryAssign(String rptID, String userAccountId, String[] groupNames);
	
	public List<ReportQueryAssign> getQueryAssigns(String rptID, String userAccountId);
	
	public List<ReportNormConf> getNormConfs(String rptID);
	
	public void modifyNormConfs(String rptID, String normStr);
}
