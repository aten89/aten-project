package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDeviceAreaConfigDAO extends IBaseHibernateDAO {

	public AreaDeviceCfg findById(java.lang.String id);


	public List<AreaDeviceCfg> findByProperty(String propertyName,
			Object value);

	public  List<AreaDeviceCfg> findByDeviceType(Object docword);


	public  List<AreaDeviceCfg> findAll();
	
	/**
	 * 搜索是主设备的区域类别配置
	 * @return
	 */
	public List<AreaDeviceCfg> findMainDevAreaDeviceCfgs();
	
	public void updateByDeviceType(String deviceType,String orderPostfix);
	
	public  List<AreaDeviceCfg> findByAreaCodeAndClassId(String areaCode,String classId);
	
	public ListPage<AreaDeviceCfg> queryAllAreaDeviceCfgPage(QueryParameters qp);
	
	/**
	 * 获得指定地区的所有设备类别区域信息，如果地区类别不指定，则查询所有设备类别区域配置
	 * @param areaCode 指定区域
	 * @param deviceClassID 指定设备类别
	 * @return
	 */
	public List<AreaDeviceCfg> findAreaDeviceByAreaCode(String areaCode,String deviceClassID);
	
//	public List<DeviceAcptCountCfg> getDeviceAcptCountCfgByClassId(
//			String areaCode, String classID, String purpose);
	
}
