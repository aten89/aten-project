package org.eapp.dao;

import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;

/**
 * 服务管理DAO
 * @author ibm
 * @version
 */
public interface IServiceDAO extends IBaseHibernateDAO {

	public Service findById(java.lang.String id);

	/**
	 * 根据查询条件查询服务
	 * @param qp 查询条件
	 * @return ListPage -> dataList<Service> 对象
	 */
	public ListPage<Service> queryService(ServiceQueryParameters qp);
	
	/**
	 * 
	 * @param ser
	 * @return
	 */
	public boolean existUserAccount(Service ser);
	
	/**
	 * 检查名称是否重复，若为空认为是重复
	 * @param name 服务名称
	 * @return
	 */
	public boolean checkRepetition(String name);
}