package org.eapp.oa.hr.dao;

import java.util.List;

import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.StaffFlowQueryAssign;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

public interface IStaffFlowApplyDAO extends IBaseHibernateDAO {

	public StaffFlowApply findById(String id);
	
	public StaffFlowApply findByUserAccountId(String userAccountId);

	public List<StaffFlowApply> findByExample(StaffFlowApply instance);

	public List<StaffFlowApply> findStaffFlowApplys(String userAccountId, int formStatus);
	
	public List<StaffFlowApply> getDealingStaffFlowApply(String userAccountId);
	
	public ListPage<StaffFlowApply> getTrackOrArchStaffFlowApply(StaffFlowQueryParameters rqp,
			String userAccountId,Boolean isArch);
	
	public String getMaxID();
	
	/**
	 * 查询最大工号
	 * @return
	 */
	public String queryMaxEmployeeNumber();
	
	/**
	 * 检查工号是否重复
	 * @param employeeNumber
	 * @return
	 */
	public boolean checkMaxEmployeeNumber(String employeeNumber, String id);
	
	public List<StaffFlowApply> queryStaffFlowApply(List<String> accountIDs);
	
	public ListPage<StaffFlowApply> queryStaffFlowApply(StaffFlowQueryParameters rqp);
	
	/**
	 * 根据用户删除查询权限
	 * @param userAccountId
	 */
	public void deleteQueryAssign(String userAccountId);
	
	/**
	 * 根据用户查找查询权限
	 * @param userAccountId
	 * @return
	 */
	public List<StaffFlowQueryAssign> findStaffFlowQueryAssigns(String userAccountId);
}