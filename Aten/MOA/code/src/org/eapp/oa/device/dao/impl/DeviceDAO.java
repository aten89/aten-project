package org.eapp.oa.device.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
/**
 * 设备DAO
 * 
 * @author: ZhengChao Email: zhengchao730@163.com
 */

public class DeviceDAO extends BaseHibernateDAO implements IDeviceDAO {

	private static final Log log = LogFactory.getLog(DeviceDAO.class);

	public static final String DEVICENO = "deviceNO";
	public static final String DEVICECLASS = "deviceClass";
	public static final String DEVICETYPE = "deviceType";
	public static final String ISACCESSORY = "isAccessory";
	public static final String DEVICENAME = "deviceName";
	public static final String DEVICEMODEL = "deviceModel";
	public static final String NUMBER = "number";
	public static final String USENUMBER = "useNumber";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
	public static final String ISUSING = "isUsing";
	public static final String REGTIME = "regTime";
	public static final String BUYTIME = "buyTime";
	public static final String BUYTYPE = "buyType";

	public Device findById(java.lang.String id) {
		log.debug("getting Device instance with id: " + id);
		try {
			Device instance = (Device) getSession().get(Device.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Device> findByExample(Device instance) {
		log.debug("finding Device instance by example");
		try {
			List<Device> results = getSession().createCriteria("Device").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Device> findByProperty(String propertyName, Object value) {
		log.debug("finding Device instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Device as m where 1=1 and m."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Device> findAll() {
		log.debug("finding all Device instances");
		try {
			String queryString = "from Device";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List<Device> findByDeviceNO(Object DeviceNO) {
		return findByProperty(DEVICENO, DeviceNO);
	}

	public List<Device> findByDeviceClass(Object DeviceClass) {
		return findByProperty(DEVICECLASS, DeviceClass);
	}

	public List<Device> findByDeviceType(Object DeviceType) {
		return findByProperty(DEVICETYPE, DeviceType);
	}

	public List<Device> findByIsAccessory(Object isAccessory) {
		return findByProperty(ISACCESSORY, isAccessory);
	}

	public List<Device> findByDeviceName(Object DeviceName) {
		return findByProperty(DEVICENAME, DeviceName);
	}

	public List<Device> findByDeviceModel(Object DeviceModel) {
		return findByProperty(DEVICEMODEL, DeviceModel);
	}

	public List<Device> findByNumber(Object number) {
		return findByProperty(NUMBER, number);
	}

	public List<Device> findByUseNumber(Object useNumber) {
		return findByProperty(USENUMBER, useNumber);
	}

	public List<Device> findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List<Device> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<Device> findByIsUsing(Object isUsing) {
		return findByProperty(ISUSING, isUsing);
	}

	public List<Device> findByRegTime(Object regTime) {
		return findByProperty(REGTIME, regTime);
	}

	public List<Device> findByBuyTime(Object buyTime) {
		return findByProperty(BUYTIME, buyTime);
	}

	public List<Device> findByBuyType(Object buyType) {
		return findByProperty(BUYTYPE, buyType);
	}

	public ListPage<Device> queryDeviceListPage(DeviceQueryParameters qp,List<String> deviceClasses) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		if(deviceClasses==null || deviceClasses.isEmpty()){
			return null;
		}
		try {
			StringBuffer hql = new StringBuffer("select m from Device as m left join m.deviceCurStatusInfo as i where 1=1 and i.deviceCurStatus!="+DeviceCurStatusInfo.STATUS_DELETE +" and m.deviceClass.id in(:deviceClasses)");
			// 拼接查询条件
			if (qp.getDeviceNO() != null) {
				//设备编号支持大小写查询
				hql.append(" and lower(m.deviceNO) like lower(:deviceNO)");
				qp.toArountParameter("deviceNO");
			}
			if (qp.getDeviceClass() != null) {
				hql.append(" and m.deviceClass.id = :deviceClass ");
			}
			if (qp.getAreaCode() != null) {
				hql.append(" and m.areaCode =:areaCode");
			}
			if (qp.getDeviceType() != null) {
				hql.append(" and m.deviceType =:deviceType");
			}
			if (qp.getDeviceName() != null) {
				hql.append(" and m.deviceName like:deviceName");
				qp.toArountParameter("deviceName");
			}
			if(qp.getIsUsered() !=null){
				if (qp.getIsUsered() == DeviceCurStatusInfo.ISUSING_NO) {
					hql.append(" and i.owner is null and i.deviceCurStatus ="+DeviceCurStatusInfo.STATUS_NORMAL);
				} else if (qp.getIsUsered() == DeviceCurStatusInfo.ISUSING_YES){
					hql.append(" and i.owner is not null and (i.deviceCurStatus ="+DeviceCurStatusInfo.STATUS_NORMAL +" or i.deviceCurStatus="+DeviceCurStatusInfo.STATUS_LEAVE_TAKE+")");
				} else if (qp.getIsUsered() == DeviceCurStatusInfo.ISUSING_UNDISPOSE){
					hql.append(" and i.deviceCurStatus in ("+DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE+","+DeviceCurStatusInfo.STATUS_BORROW+")");
				} else if (qp.getIsUsered() == DeviceCurStatusInfo.ISUSING_DISPOSEED){
					hql.append(" and i.deviceCurStatus in ("+DeviceCurStatusInfo.STATUS_LEAVEBUY+","
							+DeviceCurStatusInfo.STATUS_TAKE+","+DeviceCurStatusInfo.STATUS_LOST+","+DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED+","
							+DeviceCurStatusInfo.STATUS_LEAVEDONATE+")");
				}
				qp.removeParameter("isUsered");
			}
			if (qp.getBeginBuyTime() != null) {
				hql.append(" and m.buyTime >= :beginBuyTime");
			}
			if (qp.getEndBuyTime() != null) {
				hql.append(" and m.buyTime < :endBuyTime");
			}
			if (qp.getStatus() != null) {
				hql.append(" and i.deviceCurStatus = :status");
			}
			if (qp.getGroupName() != null) {
				hql.append(" and i.groupName like:groupName");
				qp.toArountParameter("groupName");
			}
			if (qp.getApplicantID() != null) {
				hql.append(" and i.owner = :applicantID");
			}
			if (qp.getDeviceModel() != null && qp.getDeviceModel().length()>0){
				hql.append(" and m.deviceModel like:deviceModel");
				qp.toArountParameter("deviceModel");
			}
			qp.addParameter("deviceClasses", deviceClasses);
			ListPage<Device> devices = new CommQuery<Device>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
			
			return devices;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Device>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Device> queryDeviceBorrowInfo(String id, String deviceClass, Timestamp beginTime, Timestamp endTime){
		StringBuffer hql = new StringBuffer();
		if (id != null) {
			hql.append("select d,(select sum(bd.deviceNum) from BorrowDevice as bd where bd.device.id=d.id and bd.beginTime < :endTime and bd.endTime > :beginTime and bd.meetingInfo.id <>:id)");
		} else {
			hql.append("select d,(select sum(bd.deviceNum) from BorrowDevice as bd where bd.device.id=d.id and bd.beginTime < :endTime and bd.endTime > :beginTime)");
		}
		hql.append(" from Device as d where d.status=:status and d.deviceClass =:deviceClass");
		Query queryObject = getSession().createQuery(hql.toString());
		queryObject.setInteger("status", DeviceCurStatusInfo.STATUS_NORMAL);
		queryObject.setString("deviceClass", deviceClass);
		queryObject.setTimestamp("beginTime", beginTime);
		queryObject.setTimestamp("endTime", endTime);
		if (id != null) {
			queryObject.setString("id", id);
		}
		List<Object[]> list = queryObject.list();
		List<Device> devList = new ArrayList<Device>();
		for(Object[] objs : list){
			Device device = (Device)objs[0];
			//Integer deviceNum = 0;
//			if(objs[1] != null) {
//				//deviceNum = ((Long)objs[1]).intValue();		
//			}
//			device.setOrderNum(deviceNum);
			devList.add(device);
		}
		return devList;
	}

	public int getDeviceNumByNo(String deviceNO,String areaCode,String classId){
		try {
			String queryString = "select count(*) from Device as d where d.deviceNO = :deviceNO and d.deviceClass.id=:classId and d.areaCode=:areaCode" +
					" and d.deviceCurStatusInfo.deviceCurStatus !=" + DeviceCurStatusInfo.STATUS_DELETE;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("deviceNO", deviceNO);
			queryObject.setString("areaCode", areaCode);
			queryObject.setString("classId", classId);
			Long value = (Long)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
			
		} catch (RuntimeException re) {
			log.error("getDeviceNumByNo failed", re);
			throw re;
		}
	}
	public int getDeviceNumByName(String areaCode,String classId,String deviceName){
		try {
			String queryString = "select count(*) from Device as d where d.deviceName = :deviceName and d.deviceClass.id=:classId and d.areaCode=:areaCode";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("deviceName", deviceName);
			queryObject.setString("areaCode", areaCode);
			queryObject.setString("classId", classId);
			Long value = (Long)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
			
		} catch (RuntimeException re) {
			log.error("getDeviceNumByNo failed", re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Device> findUnbindITDevice(String deviceType,String deviceClass,String status,boolean isAccessory){
		String hql="select d from Device as d where d.deviceClass =:deviceClass and d.userDevice is null and d.deviceType=:deviceType and d.status=:status " +
				" and d.isAccessory =:isAccessory";
		Query queryObject = getSession().createQuery(hql);
		queryObject.setString("deviceClass", deviceClass);
		queryObject.setString("deviceType", deviceType);
		queryObject.setInteger("status", Integer.parseInt(status));
		queryObject.setBoolean("isAccessory", isAccessory);
		List<Device> list = queryObject.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Device> findDevicesByApplicantID(String applicantID){
		String hql="select d from Device as d left join d.deviceCurStatusInfo as i where i.device.id=d.id and i.owner=:applicantID and i.deviceCurStatus!=0";
		Query queryObject = getSession().createQuery(hql);
		queryObject.setString("applicantID", applicantID);
		List<Device> list = queryObject.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Device> findUseDevicesByApplicantID(String applicantID){
		String hql="select d from Device as d left join d.deviceCurStatusInfo as i where i.device.id=d.id and i.owner=:applicantID and i.deviceCurStatus=" + DeviceCurStatusInfo.STATUS_NORMAL;
		Query queryObject = getSession().createQuery(hql);
		queryObject.setString("applicantID", applicantID);
		List<Device> list = queryObject.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ListPage<Device> findDevicePageByApplicantID(DeviceQueryParameters qp, String applicantID){
		try {
			StringBuffer hql = new StringBuffer("select d from Device as d left join d.deviceCurStatusInfo as i");
			hql.append(" where i.device.id=d.id and i.owner='" + applicantID + "' and i.deviceCurStatus!=0");
			hql.append(" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_DELETE + //排除已删除
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_LEAVEBUY + //排除离职回购
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_TAKE + //排除回购
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_LEAVE_TAKE + //排除离职拿走
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_LEAVEDONATE + //排除离职赠送
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_LOST + //排除丢失
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE + //排除报废未处理
					" and i.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED);//排除报废已处理
			ListPage<Device> devicePage = new CommQuery<Device>().queryListPage(qp, qp.appendOrders(hql, "d"), getSession());
			return devicePage;
		} catch (RuntimeException e) {
			log.error("find my device list page data failed", e);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Device> findDevicesByApplicantIDAndClassID(String applicantID, String deviceClassID){
		String hql="select d from Device as d left join d.deviceCurStatusInfo as i where i.device.id=d.id" +
				" and d.deviceClass.id=:deviceClassID and i.owner=:applicantID and i.deviceCurStatus=" + DeviceCurStatusInfo.STATUS_NORMAL ;
		Query queryObject = getSession().createQuery(hql);
		queryObject.setString("applicantID", applicantID);
		queryObject.setString("deviceClassID", deviceClassID);
		List<Device> list = queryObject.list();
		return list;
	}
	
	public int getDeviceSepNum(String classID,String areaCode){
		try {
			String queryString = "select max(d.sequence) from Device d where d.deviceClass.id=:classID and d.areaCode=:areaCode" + 
					" and d.deviceCurStatusInfo.deviceCurStatus !=" + DeviceCurStatusInfo.STATUS_DELETE;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("classID", classID);
			queryObject.setParameter("areaCode", areaCode);
			Integer value = (Integer)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public ListPage<Device> findDevices(DeviceQueryParameters qp, String ownerAccountID, boolean assignDevClassFlag, List<String> assignDeviceClassIDs, 
			boolean mergeFlag, Boolean approvingFlag, Boolean excludeScrapFlag, List<String> deletedIDsAtReject) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			if(assignDevClassFlag && (assignDeviceClassIDs == null || assignDeviceClassIDs.size() == 0) ){
				return null;
			}
			StringBuffer hql = new StringBuffer("select m from Device as m left join m.deviceCurStatusInfo as dcsi where 1=1");
			// 拼接查询条件
			if (qp.getDeviceNO() != null) {
				//设备编号支持大小写查询
				hql.append(" and lower(m.deviceNO) like lower(:deviceNO)");
				qp.toArountParameter("deviceNO");
			}
			if (qp.getDeviceClass() != null) {
				hql.append(" and m.deviceClass.id = :deviceClass");
			}
			if (qp.getDeviceType() != null) {
				hql.append(" and m.deviceType =:deviceType");
			}
			if (qp.getDeviceName() != null) {
				hql.append(" and m.deviceName like:deviceName");
				qp.toArountParameter("deviceName");
			}
			
			if (qp.getBeginBuyTime() != null) {
				hql.append(" and m.buyTime >= :beginBuyTime");
			}
			if (qp.getEndBuyTime() != null) {
				hql.append(" and m.buyTime <= :endBuyTime");
			}
			if (qp.getAreaCode() != null) {
				hql.append(" and m.areaCode =:areaCode");
			}
			if (qp.getStatus() != null) {
				hql.append(" and dcsi.deviceCurStatus = :status");
			}
			if (qp.getIDs() != null) {
				hql.append(" and m.id not in (:ids)");//排除的ID
			}
			if (qp.getBuyTypes() != null) {
				hql.append(" and m.buyType not in (:buyTypes)");
			}
			if (mergeFlag) {
				hql.append(" and (dcsi.owner='" + ownerAccountID + "' or dcsi.owner is null or dcsi.owner='')" );
				qp.removeParameter("isUsing");
			} else {
				if (ownerAccountID != null) {
					hql.append(" and dcsi.owner='" + ownerAccountID + "'");
				}
				if(qp.getIsUsing() !=null){
					if (qp.getIsUsing() == DeviceCurStatusInfo.ISUSING_NO) {
						hql.append(" and (dcsi.owner is null)");
					} else {
						hql.append(" and dcsi.owner is not null");
					}
					qp.removeParameter("isUsing");
				}
			}
			
			if (assignDevClassFlag && assignDeviceClassIDs != null && assignDeviceClassIDs.size() > 0) {
				hql.append(" and m.deviceClass.id in (:assignDeviceClassIDs)");
				qp.addParameter("assignDeviceClassIDs", assignDeviceClassIDs);
			}
			if (approvingFlag != null) {
				if (approvingFlag) {
					hql.append(" and dcsi.approveType is not null");//设备处于审批中的
				} else {
					hql.append(" and (dcsi.approveType is null");//设备不在审批中的
					if (deletedIDsAtReject != null && deletedIDsAtReject.size() > 0) {
						//如果已删除的设备ID中有，也要显示
						hql.append(" or m.id in (:deletedIDsAtReject)");
						qp.addParameter("deletedIDsAtReject", deletedIDsAtReject);
					}
					hql.append(")");
				}
			}
			if (excludeScrapFlag != null && excludeScrapFlag) {
				//排除已报废的
				hql.append(" and dcsi.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_DELETE + //排除已删除
						" and dcsi.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_LOST + //排除丢失
						" and dcsi.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE + //排除报废未处理
						" and dcsi.deviceCurStatus!=" + DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED);//排除报废已处理
			}
			if (qp.getExcludeSelfBuyFlag() != null) {
				if (qp.getExcludeSelfBuyFlag()) {
					hql.append(" and m.buyType!='" + Device.BUY_TYPE_SELF + "'");//排除个人全额购买的
				}
				qp.removeParameter("excludeSelfBuyFlag");
			}
			if (qp.getExcludeSubBuyFlag() != null) {
				if (qp.getExcludeSubBuyFlag()) {
					hql.append(" and m.buyType!='" + Device.BUY_TYPE_SUB + "'");//排除比例购买的
				}
				qp.removeParameter("excludeSubBuyFlag");
			}
			ListPage<Device> devices = new CommQuery<Device>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
			return devices;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Device>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Device> findDevicesByIDs(List<String> deviceIDs) {
		try {
			StringBuffer hql = new StringBuffer("select m from Device as m where m.id in (:deviceIDs)");
			Query query = getSession().createQuery(hql.toString());
			query.setParameterList("deviceIDs", deviceIDs);
			List<Device> list = (List<Device>)query.list();
			return list;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> findDeviceInfo(String deviceClass) {
		String hql="select d from Device as d where d.deviceClass.id =:deviceClass";
		Query queryObject = getSession().createQuery(hql);
		queryObject.setString("deviceClass", deviceClass);
		List<Device> list = queryObject.list();
		return list;
	}

	@Override
	public ListPage<Device> getStatisticsDeviceListPage(DeviceQueryParameters qp,List<String> deviceClass,List<String> deviceAreaCodes,List<String> deviceTypes) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		if(deviceClass==null || deviceClass.isEmpty() || deviceTypes==null || deviceTypes.isEmpty() || deviceAreaCodes==null || deviceAreaCodes.isEmpty() ){
			return new ListPage<Device>();
		}
		try {
			StringBuffer hql = new StringBuffer("select m from Device as m left join m.deviceCurStatusInfo as i where 1=1 and i.deviceCurStatus!="+DeviceCurStatusInfo.STATUS_DELETE +
					" and m.deviceClass.id in(:deviceClasses)");
			hql.append(" and m.deviceType in(:deviceTypes) ");
			hql.append(" and m.areaCode in(:deviceAreaCodes) ");
			// 拼接查询条件
			if (qp.getDeviceClassIDs() != null) {
				hql.append(" and m.deviceClass.id in ( :deviceClassIDs)");
			}
			if (qp.getDeviceTypeIDs() != null) {
				hql.append(" and m.deviceType in ( :deviceTypes)");
			}
			if (qp.getStatuses() != null) {
				hql.append(" and i.deviceCurStatus in ( :statuses)");
			}
			if (qp.getAreaCodes() != null) {
				hql.append(" and m.areaCode in( :areaCodes)");
			}
			if (qp.getBeginBuyTime() != null) {
				hql.append(" and m.buyTime >= :beginBuyTime");
			}
			if (qp.getEndBuyTime() != null) {
				hql.append(" and m.buyTime < :endBuyTime");
			}
			
//			if (qp.get() != null) {
//				hql.append(" and i.groupName like:groupName");
//				qp.toArountParameter("groupName");
//			}
			if (qp.getBeginReTime() != null) {
				hql.append(" and m.regTime >= :beginReTime");
			}
			if (qp.getEndReTime() != null) {
				hql.append(" and m.regTime <= :endReTime");
			}
			qp.addParameter("deviceClasses", deviceClass);
			qp.addParameter("deviceAreaCodes", deviceAreaCodes);
			qp.addParameter("deviceTypes", deviceTypes);
			ListPage<Device> devices = new CommQuery<Device>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
			
			return devices;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Device>();
		}
	}

	@Override
	public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}
		try {
			StringBuffer hql = new StringBuffer("select m from Device as m left join m.deviceCurStatusInfo as i where 1=1 and i.deviceCurStatus="+DeviceCurStatusInfo.STATUS_NORMAL +
					" and i.owner is null and i.approveType is null");
			// 拼接查询条件
			if (qp.getDeviceClass() != null) {
				hql.append(" and m.deviceClass.id = :deviceClass");
			}
			if (qp.getDeviceType() != null) {
				hql.append(" and m.deviceType =:deviceType");
			}
			if (qp.getAreaCode() != null) {
				hql.append(" and m.areaCode =:areaCode");
			}
			if (qp.getDeviceName() != null) {
				hql.append(" and m.deviceName like:deviceName");
				qp.toArountParameter("deviceName");
			}
			ListPage<Device> devices = new CommQuery<Device>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
			return devices;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Device>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDeviceListByPuerpose(String deviceType,String areaCode, String deviceClass, String puerpose,String userId) {
		try {
			StringBuffer hql = new StringBuffer("select m from Device as m where m.deviceCurStatusInfo.purpose=:puerpose " +
					"and m.deviceType=:deviceType and m.deviceClass.id=:deviceClass and m.deviceCurStatusInfo.owner=:userId and m.deviceCurStatusInfo.deviceCurStatus="+DeviceCurStatusInfo.STATUS_NORMAL);
			if(areaCode!=null && areaCode.length()>0){
				hql.append("and m.areaCode =:areaCode");
			}
			
			Query query = getSession().createQuery(hql.toString());
			query.setParameter("deviceType", deviceType);
			if(areaCode!=null && areaCode.length()>0){
				query.setParameter("areaCode", areaCode);
			}
			
			query.setParameter("deviceClass", deviceClass);
			query.setParameter("puerpose", puerpose);
			query.setParameter("userId", userId);
			List<Device> list = (List<Device>)query.list();
			return list;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
	}
}
