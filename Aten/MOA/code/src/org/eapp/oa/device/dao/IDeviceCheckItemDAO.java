package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDeviceCheckItemDAO extends IBaseHibernateDAO {

	public DeviceCheckItem findById(String id);

	public ListPage<DeviceCheckItem> queryDeviceCheckItemPage(QueryParameters qp);

	public List<DeviceCheckItem> findByDeviceClassID(String deviceClassID);
}
