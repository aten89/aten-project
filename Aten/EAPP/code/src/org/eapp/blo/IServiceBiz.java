/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.ModuleAction;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;


/**
 * 服务管理相关的业务方法
 * @author zsy
 * @version 
 */
public interface IServiceBiz {
	
	/**
	 * 根据查询条件查询服务
	 * @param qp 查询条件
	 * @return ListPage 对象
	 */
	public ListPage<Service> queryService(ServiceQueryParameters qp);
	
	/**
	 * 根据ID删除服务，
	 * 若服务已被用户或群组绑，则禁止删除
	 * @param serviceIDs 服务ID
	 * @throws RelatedException 该服务已被用户或群组绑定时抛出
	 */
	public List<Service> deleteServices(String[] serviceIDs) throws EappException;
	
	/**
	 * 新增服务
	 * @param serviceName 服务名称,不能为空
	 * @param isValid 是否有效
	 * @param description 描述
	 */
	public Service addService(String serviceName, boolean isValid,
			String description) throws EappException;
	
	/**
	 * 修改服务信息，不会修改其关联集合部分
	 * @param serviceID 服务ID
	 * @param serviceName 服务名称,不能为空
	 * @param isValid 是否有效
	 * @param description 描述
	 */
	public Service modifyService(String serviceID, String serviceName, boolean isValid, 
			String description)throws EappException;
	
	/**
	 * 通过服务ID取得实例
	 * @param serviceID 服务ID
	 * @return
	 */
	public Service getServiceByID(String serviceID);
	
	
	/**
	 * 绑定接口帐号到服务
	 * 接口帐号ID对应的记录必须存在否则抛出异常
	 * @param serviceID 服务ID
	 * @param actorIDs 用户ID
	 */
	public Service txBindActor(String serviceID, String[] actorIDs);
	
	/**
	 * 绑定模块动作到服务
	 * @param serviceID 服务ID
	 * @param moduleActionIDs 模块动作ID
	 */
	public Service txBindRight(String serviceID, String[] moduleActionIDs);
	
	/**
	 * 取得已绑定到服务的模块动作
	 * @param serviceID 服务ID
	 * @return
	 */
	public Set<ModuleAction> getBindedRights(String serviceID);
	
	/**
	 * 取得已绑定到服务的接口用户
	 * @param serviceID 服务ID
	 * @return
	 */
	public Set<ActorAccount> getBindedActors(String serviceID);
}
