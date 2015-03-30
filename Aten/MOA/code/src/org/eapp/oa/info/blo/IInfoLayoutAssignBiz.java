package org.eapp.oa.info.blo;

import java.util.List;

import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.exception.OaException;

public interface IInfoLayoutAssignBiz {
	/**
	 * 取得绑定的用户
	 * @param id
	 * @return
	 */
	public List<InfoLayoutAssign> getBindingUsers(String id, int flag);
	/**
	 * 取得绑定的机构
	 * @param id
	 * @return
	 */
	public List<InfoLayoutAssign> getBindingGroups(String id, int flag);
	/**
	 * 取得绑定的职位
	 * @param id
	 * @return
	 */
	public List<InfoLayoutAssign> getBindingPosts(String id, int flag);
	/**
	 * 绑定用户
	 * @param id
	 * @param userIDs
	 */
	public void txBindingUsers(String id,String[] userIDs, int flag)throws OaException;
	/**
	 * 绑定机构
	 * @param id
	 * @param userIDs
	 */
	public void txBindingGroups(String id,String[] groupIDs, int flag)throws OaException;
	/**
	 * 绑定职位
	 * @param id
	 * @param userIDs
	 */
	public void txBindingPosts(String id,String[] postIDs, int flag)throws OaException;
	
}
