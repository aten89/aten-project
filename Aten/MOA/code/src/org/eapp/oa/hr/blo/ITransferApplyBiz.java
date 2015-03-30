package org.eapp.oa.hr.blo;

import java.util.List;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.dto.TransferQueryParameters;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;


public interface ITransferApplyBiz {

	public TransferApply getTransferApplyById(String id);
	
	public List<TransferApply> getTransferApplys(String userAccountId, int formStatus);

	public TransferApply txAddOrModifyTransferApplyDraft(TransferApply draf, String userAccountId) throws OaException;
	
	public TransferApply txDelTransferApply(String id);
	
	public TransferApply txStartFlow(TransferApply draf, String userAccountId) throws OaException;
	
	/**
	 * 归案或作废修改
	 * @param formId
	 * @param arch
	 */
	public void txModifyTransferApply(String formId, boolean arch);
	
	public List<Task> getEndedTasks(String id);

    public List<TransferApply> getDealingTransferApply(String userAccount, List<String> userRoles);

    public ListPage<TransferApply> getTrackTransferApply(TransferQueryParameters rpq, String userAccount);
    
    public ListPage<TransferApply> getArchTransferApply(TransferQueryParameters rpq, String userAccount);
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, TransferApply holi)
    		throws OaException;

}