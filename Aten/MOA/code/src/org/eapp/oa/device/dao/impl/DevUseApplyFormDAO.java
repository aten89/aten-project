package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevUseApplyFormDAO;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.hibernate.Query;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备使用申请单数据访问层
 * 
 * @author sds
 * @version Aug 5, 2009
 */
@SuppressWarnings("unchecked")
public class DevUseApplyFormDAO extends BaseHibernateDAO implements IDevUseApplyFormDAO {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DevUseApplyFormDAO.class);

    // property constants
    /**
     * 用户账号
     */
    public static final String ACCOUNT_ID = "accountId";

    /**
     * groupName
     */
    public static final String GROUP_NAME = "groupName";

    /**
     * 申请日期
     */
    public static final String APPLY_DATE = "applyDate";

    /**
     * 使用用户账号
     */
    public static final String USE_ACCOUNT_ID = "useAccountId";

    /**
     * 使用日期
     */
    public static final String USE_DATE = "useDate";

    /**
     * 目地
     */
    public static final String PURPOSE = "purpose";

    /**
     * 描述
     */
    public static final String DESCRIPTION = "description";

    /**
     * 流程实例id
     */
    public static final String FLOW_INSTANCE_ID = "flowInstanceId";

    /**
     * 通过的
     */
    public static final String PASSED = "passed";

    /**
     * 归档时间
     */
    public static final String ARCHIVE_DATE = "archiveDate";

    public DevPurchaseForm findById(java.lang.String id) {
        log.debug("getting DevUseApplyForm instance with id: " + id);
        try {
            DevPurchaseForm instance = (DevPurchaseForm) getSession().get(DevPurchaseForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public int findCurrentYearFormNO(int year) {
        try {
			String queryString = "select max(form.applyFormNO) from DevPurchaseForm as form where form.sequenceYear=:sequenceYear";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("sequenceYear", year);
            Object value = queryObject.uniqueResult();
            if (value == null) {
                return 0;
            }
            return ((Integer) value).intValue();
        } catch (RuntimeException re) {
            log.error("find current year form no failed", re);
            throw re;
        }
    }

    public List<DevPurchaseList> queryArchDevUseListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        StringBuffer hql = new StringBuffer("select list from DevPurchaseList as list" +
				" where list.devPurchaseForm.formStatus=" + DevPurchaseForm.FORMSTATUS_PUBLISH + " and list.devPurchaseForm.applyType!=" + DevPurchaseForm.APPLY_TYPE_PURCHASE);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id='" + deviceID + "'");
        }
        if (archiveDateOrder != null) {
            hql.append(" order by list.devPurchaseForm.archiveDate");
            if (!archiveDateOrder) {
                hql.append(" desc");
            }
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevPurchaseList>) query.list();
        } catch (RuntimeException re) {
            log.error("queryArchDevUseListByDeviceID", re);
            throw re;
        }
    }

    public PurchaseDevice queryArchPurchaseDevByDeviceID(String deviceID, Boolean archiveDateOrder) {
        StringBuffer hql = new StringBuffer("select list from PurchaseDevice as list" +
				" where list.devPurchaseForm.formStatus=" + DevPurchaseForm.FORMSTATUS_PUBLISH + " and list.devPurchaseForm.applyType=" + DevPurchaseForm.APPLY_TYPE_PURCHASE);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.deviceID='" + deviceID + "'");
        }
        if (archiveDateOrder != null) {
            hql.append(" order by list.devPurchaseForm.archiveDate");
            if (!archiveDateOrder) {
                hql.append(" desc");
            }
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setMaxResults(1);
            return (PurchaseDevice) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("queryArchPurchaseDevByDeviceID", re);
            throw re;
        }
    }

    @Override
    public PurchaseDevice findByPurchaseDeviceId(String id) {
        log.debug("getting PurchaseDevice instance with id: " + id);
        try {
            PurchaseDevice instance = (PurchaseDevice) getSession().get(PurchaseDevice.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<DevPurchaseForm> queryDealDevPurchaseFormByDeviceID(String deviceID, Integer formType) {
		StringBuffer hql = new StringBuffer("select distinct(devpur) from DevPurchaseForm as devpur left join devpur.devPurchaseLists as list " +
				" where 1=1 and devpur.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        if (deviceID != null && !"".equals(deviceID)) {
            hql.append(" and list.device.id=:deviceID");
        }
        if (formType != null) {
            hql.append(" and devpur.applyType=:formType");
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            if (deviceID != null && !"".equals(deviceID)) {
                query.setParameter("deviceID", deviceID);
            }
            if (formType != null) {
                query.setParameter("formType", formType);
            }
            return (List<DevPurchaseForm>) query.list();
        } catch (RuntimeException re) {
            log.error("queryDealDevPurchaseFormByDeviceID failed", re);
            throw re;
        }
    }

    public List<DevPurchaseForm> findDealingFormByApplicantID(String accountID, Integer formType) {
		StringBuffer hql = new StringBuffer("from DevPurchaseForm as f where f.formStatus=" + DevPurchaseForm.FORMSTATUS_APPROVAL);
        if (accountID != null) {
            hql.append(" and f.applicant='" + accountID + "'");
        }
        if (formType != null) {
            hql.append(" and f.applyType=" + formType.intValue());
        }
        try {
            Query query = getSession().createQuery(hql.toString());
            return (List<DevPurchaseForm>) query.list();
        } catch (RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    @Override
    public List<DevPurchaseList> getDeviceDealUseByPurpose(String deviceType, String areaCode, String deviceClass,
            String userId) {
        StringBuffer hql = new StringBuffer("from DevPurchaseList as list  " +
				" where list.devPurchaseForm.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        hql.append(" and list.device.deviceType=:deviceType");
        hql.append(" and list.device.areaCode=:areaCode");
        hql.append(" and list.device.deviceClass.id=:deviceClass");
//        hql.append(" and list.purpose=:purpose");
        hql.append(" and list.devPurchaseForm.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("deviceType", deviceType);
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<DevPurchaseList>) query.list();
        } catch (RuntimeException re) {
            log.error("getDeviceDealUseByPurpose failed", re);
            throw re;
        }
    }

    @Override
    public List<PurchaseDevice> getDeviceDealPurchaseByPurpose(String deviceType, String areaCode, String deviceClass,
            String userId) {
        // StringBuffer hql = new
        // StringBuffer("select distinct(list) from PurchaseDevice as list  left join list.devPurchaseForm.purchaseDevPurposes as devpurpose"
        // +
        // " where list.devPurchaseForm.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL );
        // hql.append(" and list.belongtoAreaCode=:areaCode");
        // hql.append(" and list.deviceClass.id=:deviceClass");
        // hql.append(" and devpurpose.purpose=:purpose");
        // hql.append(" and list.devPurchaseForm.applicant=:userId");
        StringBuffer hql = new StringBuffer("select distinct(list) from PurchaseDevice as list "
                + " where list.devPurchaseForm.formStatus = " + DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        hql.append(" and list.belongtoAreaCode=:areaCode");
        hql.append(" and list.deviceClass.id=:deviceClass");
//        hql.append(" and list.purpose=:purpose");
        hql.append(" and list.devPurchaseForm.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<PurchaseDevice>) query.list();
        } catch (RuntimeException re) {
            log.error("getDeviceDealPurchaseByPurpose failed", re);
            throw re;
        }
    }

    @Override
    public List<DevPurchaseForm> getDevPurchaseFormDealPurchaseByPurpose(String deviceType, String areaCode,
            String deviceClass, String userId) {
		StringBuffer hql = new StringBuffer("select distinct(list) from DevPurchaseForm as list  left join list.purchaseDevices as devpurpose" +
				" where list.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL );
        hql.append(" and list.areaCode=:areaCode");
        hql.append(" and devpurpose.deviceClass.id=:deviceClass");
//        hql.append(" and devpurpose.purpose=:purpose");
        hql.append(" and list.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<DevPurchaseForm>) query.list();
        } catch (RuntimeException re) {
            log.error("getDevPurchaseFormDealPurchaseByPurpose failed", re);
            throw re;
        }
    }

    @Override
    public List<DevPurchaseForm> getDevPurchaseFormDealUseByPurpose(String deviceType, String areaCode,
            String deviceClass,  String userId) {
		StringBuffer hql = new StringBuffer("select distinct(list) from DevPurchaseForm as list  left join list.devPurchaseLists as devpurpose " +
				" where list.formStatus = "+DevFlowApplyProcess.FORMSTATUS_APPROVAL);
        hql.append(" and devpurpose.device.deviceType=:deviceType");
        hql.append(" and devpurpose.device.areaCode=:areaCode");
        hql.append(" and devpurpose.device.deviceClass.id=:deviceClass");
//        hql.append(" and devpurpose.purpose=:purpose");
        hql.append(" and list.applicant=:userId");
        try {
            Query query = getSession().createQuery(hql.toString());
            query.setParameter("deviceType", deviceType);
            query.setParameter("areaCode", areaCode);
            query.setParameter("deviceClass", deviceClass);
//            query.setParameter("purpose", purpose);
            query.setParameter("userId", userId);
            return (List<DevPurchaseForm>) query.list();
        } catch (RuntimeException re) {
            log.error("getDevPurchaseFormDealUseByPurpose failed", re);
            throw re;
        }
    }

}