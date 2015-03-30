package org.eapp.dao;

import java.util.List;
import java.util.Map;

import org.eapp.hbean.SubSystem;

/**
 * 子系统数据操作接口
 * @author surefan
 * @version
 */
public interface ISubSystemDAO extends IBaseHibernateDAO {

	/**
	 * 根据SubSystemId查找实例
	 * @param id 子系统Id
	 * @return SubSystem 子系统信息实例
	 */
	public SubSystem findById(String id);

	/**
	 * 查找所有的子系统
	 * @return List<SubSystem> 子系统列表
	 */
	public List<SubSystem> findAll();
	
	/**
	 * @author zsy
	 * 通过角色ID列表取得有权限进入的子系统
	 * @param roleIDs
	 * @return
	 */
	public List<SubSystem> getSubSystemByRoleIDs(List<String> roleIDs);

	public List<SubSystem> findByName(String name);
	
	public void sortSubSystems(Map<String, Integer> idsort);
	
	public int getMaxOrder();

}