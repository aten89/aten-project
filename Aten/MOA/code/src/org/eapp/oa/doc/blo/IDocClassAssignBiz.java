package org.eapp.oa.doc.blo;

import java.util.List;

import org.eapp.oa.doc.hbean.DocClassAssign;
import org.eapp.oa.system.exception.OaException;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocClassAssignBiz {
	/**
	 * 取得绑定的用户
	 * 
	 * @param id
	 * @return
	 */
	public List<DocClassAssign> getBindingUsers(String id);

	/**
	 * 取得绑定的机构
	 * 
	 * @param id
	 * @return
	 */
	public List<DocClassAssign> getBindingGroups(String id);

	/**
	 * 取得绑定的职位
	 * 
	 * @param id
	 * @return
	 */
	public List<DocClassAssign> getBindingPosts(String id);

	/**
	 * 绑定用户
	 * 
	 * @param id
	 * @param userIDs
	 */
	public void txBindingUsers(String id, String[] userIDs)
			throws OaException;

	/**
	 * 绑定机构
	 * 
	 * @param id
	 * @param userIDs
	 */
	public void txBindingGroups(String id, String[] groupIDs)
			throws OaException;

	/**
	 * 绑定职位
	 * 
	 * @param id
	 * @param userIDs
	 */
	public void txBindingPosts(String id, String[] postIDs)
			throws OaException;

}
