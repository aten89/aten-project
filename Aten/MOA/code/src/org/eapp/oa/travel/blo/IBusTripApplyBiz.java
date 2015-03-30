package org.eapp.oa.travel.blo;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.util.hibernate.ListPage;

public interface IBusTripApplyBiz {
	
	public BusTripApply getBusTripApplyById(String id);
	
	public List<BusTripApply> getBusTripApplys(String userAccountId, int formStatus);
	
	public BusTripApply txDelBusTripApply(String id);
	
	public BusTripApply txStartFlow(BusTripApply draf, String userAccountId) throws OaException;
	
	public void txModifyTravel(String tripId, boolean arch);
	
	public BusTripApply txAddOrModifyApplyDraft(BusTripApply draf, String userAccountId) throws OaException;
	
    // 待办列表
    public List<BusTripApply> getDealingTripApply(String userAccount);

    // 跟踪列表
    public ListPage<BusTripApply> getTrackTripApply(BusTripQueryParameters rqp, String userAccount);

    // 归档列表
    public ListPage<BusTripApply> getArchTripApply(BusTripQueryParameters rqp, String userAccount);

    public List<Task> getEndedTasks(String id);

    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, BusTripApply reDraft) throws OaException;

    // 管理员跟踪
    public ListPage<BusTripApply> queryArchTripApply(BusTripQueryParameters rqp, Timestamp startDate, Timestamp endDate);
}
