package org.eapp.oa.device.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceFlowDAO;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.springframework.beans.BeanUtils;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

public class DeviceFlowDAO extends BaseHibernateDAO implements IDeviceFlowDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DeviceFlowDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public ListPage<DeviceFlowView> findDealDeviceFlowPage(DeviceFlowQueryParameters qp, String userID) {
        log.debug("findDealDeviceFlowPage with DeviceFlowQueryParameters: " + qp + " and userId: " + userID);
        if (qp == null) {
            throw new IllegalArgumentException("非法参数:QP为空");
        }
		StringBuffer hql = new StringBuffer(" from DeviceFlowView as f, Task as t left join t.taskAssigns as p where 1=1 ");
        hql.append(" and t.formID=f.id and (t.taskState=:createState or t.taskState=:startState) ");
        // 查询条件
        if (qp.getApplicantID() != null) {
            hql.append(" and f.applicant = :applicantID ");
        }
        if (qp.getApplyCode() != null) {
            hql.append(" and f.applyCode like :applyCode ");
            qp.toArountParameter("applyCode");
        }

        if (qp.getFormType() != null) {
            hql.append(" and f.formType = :formType ");
        }
        if (qp.getBeginApplyTime() != null) {
            hql.append(" and f.applyDate >= :beginApplyTime");
        }
        if (qp.getEndApplyTime() != null) {
            hql.append(" and f.applyDate <= :endApplyTime");
        }
        if (qp.getDeviceType() != null) {
            hql.append(" and f.deviceType= :deviceType");
        }
        hql.append(" and ( (t.transactor=:userId) or "
                + "(p.assignKey=:userId and t.transactor is null and p.type=:type)" + ") ");
        hql.append(" and f.formStatus =:formStatus ");
        StringBuffer orderSb = new StringBuffer();
        orderSb.append(" order by t.createTime ");
        // if(qp.getOrders()!=null && !qp.getOrders().isEmpty()){
        //
        // for(Order order :qp.getOrders()){
        // orderSb.append(order.getFieldName()+" "+order.getType()+",");
        // }
        // orderSb.deleteCharAt(orderSb.length() - 1);
        // }
        try {
            qp.addParameter("userId", userID);
            qp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
            qp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
            qp.addParameter("type", TaskAssign.TYPE_USER);
            qp.addParameter("formStatus", DeviceFlowView.FORMSTATUS_APPROVAL);
            // qp.addParameter("flowClass", InstallForm.FLOW_CLASS);

            ListPage listPage = new CommQuery().queryListPage(qp,
                    "select distinct f,t " + hql.toString() + orderSb.toString(),
                    "select count(distinct f) " + hql.toString(), getSession());
            ListPage<DeviceFlowView> page = new ListPage<DeviceFlowView>();
            BeanUtils.copyProperties(listPage, page, new String[] { "dataList" });
            List<DeviceFlowView> deviceFlowViewList = new ArrayList<DeviceFlowView>();
            if (listPage.getDataList() != null) {
                List<Object[]> list = listPage.getDataList();
                for (Object[] o : list) {
                    DeviceFlowView form = (DeviceFlowView) o[0];
                    Task task = (Task) o[1];
                    form.setTask(task);
                    deviceFlowViewList.add(form);
                }
            }
            page.setDataList(deviceFlowViewList);
            return page;
        } catch (RuntimeException re) {
            log.error("findDealDeviceFlowPage faild", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListPage<DeviceFlowView> findArchDeviceFlowPage(DeviceFlowQueryParameters qp, String userID) {
        log.debug("findArchDeviceFlowPage with DeviceFlowQueryParameters and userID: " + userID);
        if (qp == null) {
            throw new IllegalArgumentException("非法参数:QP为空");
        }
        StringBuffer hql = new StringBuffer(
				"from DeviceFlowView as d, Task as t where t.formID=d.id and t.taskState=:endState " +
				" and (d.formStatus =:archStatus or d.formStatus =:scrapStatus)" );
        if (userID != null) {
            hql.append(" and (t.transactor=:userId or d.applicant=:userId)");
        }
        if (qp.getBeginArchTime() != null) {
            hql.append(" and d.archiveDate >=:beginArchTime");
        }
        if (qp.getEndArchTime() != null) {
            hql.append(" and d.archiveDate <=:endArchTime");
        }
        if (qp.getApplicantID() != null) {
            hql.append(" and d.applicant = :applicantID ");
        }
        if (qp.getApplyCode() != null) {
            hql.append(" and d.applyCode like :applyCode ");
            qp.toArountParameter("applyCode");
        }
        if (qp.getDeviceType() != null) {
            hql.append(" and d.deviceType= :deviceType");
        }
        if (qp.getFormType() != null) {
            hql.append(" and d.formType = :formType ");
        }
        qp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
        qp.addParameter("archStatus", DeviceFlowView.FORMSTATUS_PUBLISH);
        qp.addParameter("scrapStatus", DeviceFlowView.FORMSTATUS_CANCELLATION);
        if (userID != null) {
            qp.addParameter("userId", userID);
        }
        try {
            ListPage listPage = new CommQuery().queryListPage(qp, "select distinct d " + qp.appendOrders(hql, "d"),
                    "select count(distinct d) " + hql.toString(), getSession());
            return listPage;
        } catch (RuntimeException re) {
            log.error("findArchDeviceFlowPage faild", re);
            return new ListPage<DeviceFlowView>();
        }
    }

	@Override
    public ListPage<DeviceFlowView> findDraftDeviceFlowPage(DeviceFlowQueryParameters qp, String userID) {
        log.debug("findDraftDeviceFlowPage with DeviceFlowQueryParameters and userID: " + userID);
        if (qp == null) {
            throw new IllegalArgumentException("非法参数:QP为空");
        }
        StringBuffer hql = new StringBuffer();
        hql.append("select d from DeviceFlowView as d where 1=1 ");
        hql.append(" and d.applicant =:userId");
        hql.append(" and d.formStatus =:status");
        if (qp.getFormType() != null) {
            hql.append(" and d.formType =:formType");
        }
        try {
            qp.addParameter("userId", userID);
            qp.addParameter("status", DeviceFlowView.FORMSTATUS_UNPUBLISH);

            return new CommQuery<DeviceFlowView>().queryListPage(qp, qp.appendOrders(hql, "d"), getSession());
        } catch (RuntimeException re) {
            log.error("findDraftDeviceFlowPage faild", re);
            return new ListPage<DeviceFlowView>();
        }
    }

	@SuppressWarnings("unchecked")
	@Override
    public ListPage<DeviceFlowView> findTrackDeviceFlowPage(DeviceFlowQueryParameters qp, String userID) {
        log.debug("findTrackDeviceFlowPage with DeviceFlowQueryParameters and userID: " + userID);
        if (qp == null) {
            throw new IllegalArgumentException("非法参数:QP为空");
        }
		StringBuffer hql =  new StringBuffer("from DeviceFlowView as d, Task as t where t.formID=d.id and ((t.taskState=:endState " +
				"and t.transactor=:userId)  or d.applicant=:userId) and d.formStatus=:status  and d.archiveDate is null ");
//		hql.append(" and d.id not in(select ct.formID from Task as ct left join ct.taskAssigns as p " )
//		   .append("where  ct.formID = d.id and ((ct.taskState=:createState or ct.taskState=:startState) and ((ct.transactor=:userId) or (p.assignKey=:userId and ct.transactor is null and p.type=:type))))");
        if (qp.getApplicantID() != null) {
            hql.append(" and d.applicant = :applicantID ");
        }
        if (qp.getApplyCode() != null) {
            hql.append(" and d.applyCode like :applyCode ");
            qp.toArountParameter("applyCode");
        }

        if (qp.getFormType() != null) {
            hql.append(" and d.formType = :formType ");
        }
        if (qp.getBeginApplyTime() != null) {
            hql.append(" and d.applyDate >= :beginApplyTime");
        }
        if (qp.getEndApplyTime() != null) {
            hql.append(" and d.applyDate <= :endApplyTime");
        }
        if (qp.getDeviceType() != null) {
            hql.append(" and d.deviceType= :deviceType");
        }
//        qp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
//        qp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        qp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
        qp.addParameter("userId", userID);
        qp.addParameter("status", DeviceFlowView.FORMSTATUS_APPROVAL);
//        qp.addParameter("type", TaskAssign.TYPE_USER);
        try {
            ListPage listPage = new CommQuery().queryListPage(qp, "select distinct d " + qp.appendOrders(hql, "d"),
                    "select count(distinct d) " + hql.toString(), getSession());
            return listPage;
        } catch (RuntimeException re) {
            log.error("findTrackDeviceFlowPage faild", re);
            return new ListPage<DeviceFlowView>();
        }
    }

}
