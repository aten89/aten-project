package org.eapp.oa.hr.blo;

import java.util.List;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.dto.PositiveQueryParameters;
import org.eapp.oa.hr.hbean.PositiveApply;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;


public interface IPositiveApplyBiz {

	public PositiveApply getPositiveApplyById(String id);
	
	public List<PositiveApply> getPositiveApplys(String userAccountId, int formStatus);

	public PositiveApply txAddOrModifyPositiveApplyDraft(PositiveApply draf, String userAccountId) throws OaException;
	
	public PositiveApply txDelPositiveApply(String id);
	
	public PositiveApply txStartFlow(PositiveApply draf, String userAccountId) throws OaException;
	
	/**
	 * 归案或作废修改
	 * @param formId
	 * @param arch
	 */
	public void txModifyPositiveApply(String formId, boolean arch);
	
	public List<Task> getEndedTasks(String id);

    public List<PositiveApply> getDealingPositiveApply(String userAccount, List<String> userRoles);

    public ListPage<PositiveApply> getTrackPositiveApply(PositiveQueryParameters rpq, String userAccount);
    
    public ListPage<PositiveApply> getArchPositiveApply(PositiveQueryParameters rpq, String userAccount);
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, PositiveApply holi)
    		throws OaException;

}