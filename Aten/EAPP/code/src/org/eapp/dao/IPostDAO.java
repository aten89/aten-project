package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.PostQueryParameters;
import org.eapp.hbean.Post;
import org.eapp.util.hibernate.ListPage;

/**
 * 职位管理DAO
 * @author zsy
 * @version
 */
public interface IPostDAO extends IBaseHibernateDAO {

	Post findById(String id);

	List<Post> findByPostName(String postName);
	
	List<Post> findByGroupID(String groupID);
	
	/**
	 * 查找第一级职位，即parentPostID为空的记录
	 * @return
	 */
	List<Post> findRootPosts();
	
	/**
	 * 查找下级职位
	 * @param postID 职位ID
	 * @return
	 */
	List<Post> findSubPosts(String postID);

	/**
	 * 根据条件查询职位
	 * @param postQP 查询参数
	 * @return
	 */
	ListPage<Post> queryPost(PostQueryParameters postQP);
	
	/**
	 * 取得职位的下级职位的下一个序号
	 * @param postID
	 * @return
	 */
	int getNextDisplayOrder(String postID);
	
	/**
	 * 取得职位的下级职位的树深度
	 * @param postID
	 * @return
	 */
	int getNextTreeLevel(String postID);
	
	/**
	 * 是否存在下级部门
	 * @param postID
	 * @return
	 */
	boolean existSubPost(Post post);
	/**
	 * 根据ID删除职位
	 * 有下级职位的不能删除
	 * 有人员绑定的不能删除
	 * 
	 * @param postID
	 */
	public void deleteByID(String postID);
	
	/**
	 * 修改职位的排序号
	 * @param postID 职位ID
	 * @param parentPostID 父职位ID
	 */
	public void saveOrder(String[] postIDs, String parentPostID);
	
	/**
	 * 修改指定父ID下的职位的排序号，当排序号大于给定值时减1
	 * @param parentPostID 父ID
	 * @param theOrder 排序号
	 */
	public void updateOrder(String parentPostID, Integer theOrder);
	
	
	/**
	 * 检查名称是否重复
	 * @param name 名称
	 * @return
	 */
	public boolean checkRepetition(String parentPostID, String name);
}