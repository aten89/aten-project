package org.eapp.oa.hr.blo;

import java.io.IOException;
import java.util.List;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.dto.StaffAssignInfo;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;


public interface IStaffFlowApplyBiz {

	public StaffFlowApply getStaffFlowApplyById(String id);
	
	public StaffFlowApply getStaffFlowApplyByUserAccountID(String userAccountID);
	
	public StaffFlowApply getEntryApplyInfo(String userAccountID);
	
	public List<StaffFlowApply> getStaffFlowApplys(String userAccountId, int formStatus);

	public StaffFlowApply txDelStaffFlowApply(String id);
	
	public StaffFlowApply txStartFlow(StaffFlowApply draf, String userAccountId) throws OaException;
	
	public StaffFlowApply txAddOrModifyStaffFlowApplyDraft(StaffFlowApply draf, String userAccountId) throws OaException;

	/**
	 * 归案或作废修改
	 * @param formId
	 * @param arch
	 */
	public void txModifyStaffFlowApply(String formId, boolean arch);
	
	public List<Task> getEndedTasks(String id);

    public List<StaffFlowApply> getDealingStaffFlowApply(String userAccount);

    public ListPage<StaffFlowApply> getTrackStaffFlowApply(StaffFlowQueryParameters rpq, String userAccount);
    
    public ListPage<StaffFlowApply> getArchStaffFlowApply(StaffFlowQueryParameters rpq, String userAccount);
    
    /**
     * 驳回修改审批处理
     * @param taskInstanceId
     * @param comment
     * @param transitionName
     * @param holi
     * @throws OaException
     */
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, StaffFlowApply holi)
    		throws OaException;
    
    /**
     * 最后确认审批处理（添加EAPP系统账号）
     * @param taskInstanceId
     * @param comment
     * @param transitionName
     * @param staffId
     * @throws OaException
     */
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String staffId, 
    		String userAccountID)throws OaException;
    
    /**
	 * 查询最大工号
	 * @return
	 */
	public String getMaxEmployeeNumber();
	
	public List<StaffFlowApply> getStaffFlowApplyByAccountIds(List<String> uids, int pageNo, int pageSize) throws OaException;

	public ListPage<StaffFlowApply> queryStaffFlowApply(StaffFlowQueryParameters rqp);
	
	public ListPage<StaffFlowApply> queryMyStaffFlowApplys(StaffFlowQueryParameters rqp, String userAccountID);
	
	public ListPage<StaffFlowApply> queryContractPrompt(StaffFlowQueryParameters rqp, int beforeDays);
	
	public ListPage<StaffFlowApply> queryFormalPrompt(StaffFlowQueryParameters rqp, int beforeDays);
	
	public ListPage<StaffFlowApply> queryBirthdayPrompt(StaffFlowQueryParameters rqp, int beforeDays);
	
	public void csExportStaffFlowApply(StaffFlowQueryParameters rqp, String expNameAndValue, String filePath) throws IOException, OaException;
	
	public ListPage<StaffAssignInfo> getUserAccountIdsByDeptAndAccount(String userDeptName, String userAccountQueryString, 
			int pageNo, int pageSize);
	
	public void txSaveStaffFlowAssign(String userAccountId, String[] groupNames);
	
//	public List<StaffFlowQueryAssign> queryStaffFlowQueryAssigns(String userAccountId);
}