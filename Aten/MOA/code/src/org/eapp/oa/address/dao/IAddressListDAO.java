/*
 * @(#) IMyDataDAO.java 1.0 09/11/05 
 */
package org.eapp.oa.address.dao;


import java.util.List;

import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 通讯录模块的数据访问接口
 *
 */
public interface IAddressListDAO extends IBaseHibernateDAO{
	
	/**
	 * 根据用户的帐号ID获得关于该用户的资料
	 * 
	 * @param accountId 用户帐号ID
	 * @return 匹配的用户资料。null:不存在与该用户的帐号id对应的个人资料
	 */
	public AddressList findByAccountId(String accountId);
	
	/**
	 * 根据主键获得通讯录
	 * 
	 * @param id 主键
	 * @return 匹配的通讯录
	 */
	public AddressList findById(String id);
	
	/**
	 * 搜索
	 * @param aqp
	 * @return
	 */
	public List<AddressList> queryAddressList(List<String> accountIDs);
	
	/**
	 * 搜索所有通讯录
	 * @return
	 */
	public List<AddressList> findAllAddressList();
	
	/**
	 * 根据帐户id查找Email
	 */
	public String findEmailByAccountId(String accountId);
}