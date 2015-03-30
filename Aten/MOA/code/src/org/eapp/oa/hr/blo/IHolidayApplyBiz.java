package org.eapp.oa.hr.blo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.exception.OaException;
import org.eapp.util.hibernate.ListPage;


public interface IHolidayApplyBiz {

	public HolidayApply getHolidayApplyById(String id);
	
	public List<HolidayApply> getHolidayApplys(String userAccountId, int formStatus);

	public HolidayApply txDelHolidayApply(String id);
	
	public HolidayApply txStartFlow(HolidayApply draf, String userAccountId) throws OaException;
	
	public HolidayApply txStartCancelFlow(String holiId, List<HolidayDetail> details, String user) throws OaException;
	
	public HolidayApply txAddOrModifyHolidayApplyDraft(HolidayApply draf, String userAccountId) throws OaException;

	/**
	 * 归案或作废修改
	 * @param formId
	 * @param arch
	 */
	public void txModifyHolidayApply(String formId, boolean arch);
	
	/**
	 * 销假归案或作废修改
	 * @param formId
	 * @param arch
	 */
	public void txModifyCancelHolidayApply(String formId, boolean arch);
	
	public List<Task> getEndedTasks(String id);

    public List<HolidayApply> getDealingHolidayApply(String userAccount, List<String> userRoles);

    public ListPage<HolidayApply> getTrackHolidayApply(HolidayQueryParameters rpq, String userAccount);
    
    public ListPage<HolidayApply> getArchHolidayApply(HolidayQueryParameters rpq, String userAccount);
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, HolidayApply holi)
    		throws OaException;
    //统计历史请假信息
    public Map<String, Double> getUserHolidayStat(String userAccountId);

    public ListPage<HolidayDetail> getHolidayDetail(HolidayQueryParameters rpq);
    
    public void csExportHolidayDetail(HolidayQueryParameters rpq, String filePath) throws IOException, OaException;
}