package org.eapp.oa.device.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDevValidateFormDAO;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * Description: 设备验收单 数据访问对象层
 * @author sds
 * @version Aug 5, 2009
 */
@SuppressWarnings("unchecked")
public class DevValidateFormDAO extends BaseHibernateDAO implements
		IDevValidateFormDAO {

	private static final Log log = LogFactory.getLog(DevValidateFormDAO.class);

	public static final String VALITYPE = "valiType";
	public static final String INDATE = "inDate";
	public static final String ACCOUNTID = "accountID";
	public static final String GROUPNAME = "groupName";
	public static final String VALIDATE = "valiDate";
	public static final String ISMONEY = "isMoney";
	public static final String DEDUCTMONEY = "deductMoney";
	public static final String REMARK = "remark";
	public static final String DEVICEID = "deviceID";

	public DevValidateForm findById(java.lang.String id) {
		log.debug("getting DevValidateForm instance with id: " + id);
		try {
			DevValidateForm instance = (DevValidateForm) getSession().get(
					DevValidateForm.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DevValidateForm> findByExample(DevValidateForm instance) {
		log.debug("finding devValidateForm instance by example");
		try {
			List<DevValidateForm> results = getSession().createCriteria(
					"DevValidateForm").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public List<DevValidateForm> findAll() {
		log.debug("finding all DevValidateForm instances");
		try {
			String queryString = "from DevValidateForm";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public List<DevValidateForm> findByDeviceIdAndValiType(Integer valiType, String deviceID) {
		try {
			String queryString = "from DevValidateForm as devValidateForm where devValidateForm.device.id =:deviceID";
			if (valiType != null) {
				queryString += " and devValidateForm.valiType =:valiType";
			}
			queryString += " order by devValidateForm.valiDate";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("deviceID", deviceID);
			if (valiType != null) {
				queryObject.setInteger("valiType", valiType);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public int findCurrentYearValidFormNO(int validType, int year) {
		try {
			String queryString = "select max(devValidateForm.validFormNO) from DevValidateForm as devValidateForm where devValidateForm.valiType=:valiType and devValidateForm.sequenceYear=:sequenceYear";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setInteger("sequenceYear", year);
			queryObject.setInteger("valiType", validType);
			Integer value = (Integer)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find current year max validForm no failed", re);
			throw re;
		}
	}

	@Override
	public DevValidateForm findByPurchaseDevId(String purchaseDevID) {
		log.debug("finding DevValidateForm by PurchaseDevId");
		try {
			String queryString = "from DevValidateForm as devValidateForm where devValidateForm.purchaseDevice.id =:purchaseDevID";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("purchaseDevID", purchaseDevID);
			queryObject.setMaxResults(1);
			return (DevValidateForm)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find DevValidateForm by PurchaseDevId failed", re);
			throw re;
		}
	}
}
