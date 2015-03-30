package org.eapp.oa.reimburse.dao;

import java.util.Date;
import java.util.List;

import org.eapp.util.hibernate.ListPage;


import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IReimbursementDAO extends IBaseHibernateDAO {
	public Reimbursement findById(java.lang.String id);

	public List<Reimbursement> findReimbursements(String userAccountId, int formStatus);
	
	public List<Reimbursement> findByExample(Reimbursement instance);

	public List<Reimbursement> findByProperty(String propertyName,
			Object value);

	public List<Reimbursement> findByFinance(Object finance);

	public List<Reimbursement> findByApplicant(Object applicant);

	public List<Reimbursement> findByApplyDept(Object applyDept);

	public List<Reimbursement> findByPayee(Object payee);

	public List<Reimbursement> findByCausa(Object causa);

	public List<Reimbursement> findByTravelPlace(Object travelPlace);

	public List<Reimbursement> findByTravelDays(Object travelDays);

	public List<Reimbursement> findByCoterielList(Object coterielList);

	public List<Reimbursement> findByLoanSum(Object loanSum);

	public List<Reimbursement> findByBudgetItem(Object budgetItem);

	public List<Reimbursement> findByRegionCode(Object regionCode);

	public List<Reimbursement> findByReimbursementSum(
			Object reimbursementSum);

	public List<Reimbursement> findByArchived(Object archived);

	public List<Reimbursement> findAll();

	/**
	 * 根据条件查询报销单
	 * @param rqp
	 * @return
	 */
	public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp);
	public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp,List<String> budgetIds);
	
	/**
	 * 取得待办的报销申请单
	 * @param userAccountId
	 * @return
	 */
	public List<Reimbursement> queryDealingReimbursement(String userAccountId);
	public ListPage<Reimbursement> queryDealingReimbursement(ReimbursementQueryParameters rqp, String userAccountId);
	/**
	 * 根据条件查找用户已归档或未归档的报销单
	 * @param rqp
	 * @param userAccountId
	 * @param isArch
	 * @return
	 */
	public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp, 
			String userAccountId, boolean isArch);
	public ListPage<Reimbursement> queryReimbursement(ReimbursementQueryParameters rqp, 
			String financeId);
	
	/**
	 * 查找所有处理中即未归档的报销单
	 * @return
	 */
	public List<Reimbursement> queryDealingReimbursement();

	String getMaxReimbursementID();
	
	/**
	 * 根据条件查询报销单明细
	 * @param budgetItem
	 * @param month
	 * @return
	 */
	Double getOutlayListByArgs(String budgetItem, String outlayCategory, Date startDate, Date endDate);
	Double getOutlayListByArgsId(String budgetId, String outlayCategory, Date startDate, Date endDate);
	Double getOutlayListByArgsId(String budgetId, String outlayCategory, Date startDate, Date endDate,String financeId);

	/**
	 * 根据预算项目和费用名称，查询在途的报销单条目
	 * @param budgetItem
	 * @param category
	 * @param reimID	报销单号，用于排除当前报销单，否则会重复计算
	 * @return
	 */
	public List<OutlayList> queryNoPassedOutlayList(String budgetItem, String category, String reimID);
	
    /**
     * 根据预算项目和费用名称及时间段，取得在途的报销单条目的费用类别的总额
     * 
     * @param budgetID 预算项目
     * @param category 费用类别名称
     * @param reimId 当前报销单ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 总额
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-4-11	李海根	新建
     * </pre>
     */
    public Double getSumNoPassCategoryInQuarter(String budgetID, String category, String reimId,
            Date startDate, Date endDate);
    
    /**
     * 根据预算项目和费用名称及时间段，取得已归档的报销单条目的费用类别的总额
     * @param budgetID 预算项目
     * @param category 费用类别名称
     * @param reimId 当前报销单ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 总额
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2013-4-15	李海根	新建
     * </pre>
     */
    public Double getSumPassedCategoryInQuarter(String budgetID, String category, String reimId,
            Date startDate, Date endDate);
    
}