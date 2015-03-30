/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Set;

import org.eapp.dao.param.PostQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * 职位管理相关的业务方法
 * @author zsy
 * @version
 */
public interface IPostBiz {
	
	/**
	 * 通过职位ID取得给定深度的下级职位列表，
	 * 当职位ID为空时从根级取起
	 * @param postID 职位ID
	 * @param depth 立即加载的深度
	 * @return
	 */
	public List<Post> getSubPosts(String postID, int depth);
	
	/**
	 * 根据条件查询职位
	 * @param groupQP 查询参数
	 * @return
	 */
	public ListPage<Post> queryPost(PostQueryParameters groupQP);
	
	/**
	 * 通过ID取得职位
	 * @param postID 职位ID
	 * @return
	 */
	public Post getPostByID(String postID);
	
	/**
	 * 通过Name取得职位
	 * @param postName 职位ID
	 * @return
	 */
	public Post getPostByName(String postName);
	
	/**
	 * 新增职位
	 * @param parentPostID 上级职位ID
	 * @param groupID 群组ID
	 * @param postName 职位名称
	 * @param description 描述
	 */
	public Post addPost(String parentPostID, String groupID, 
			String postName, String description) throws EappException;
	
	/**
	 * 修改职位
	 * @param postID 职位ID
	 * @param parentPostID 上级职位ID
	 * @param groupID 群组ID
	 * @param postName 职位名称
	 * @param description 描述
	 */
	public Post modifyPost(String postID, String parentPostID, String groupID, 
			String postName, String description) throws EappException;
	
	/**
	 * 删除职位
	 * 根据ID删除职位
	 * 有子机构的不能职位
	 * 有人员绑定的不能职位
	 * @param postIDs 职位ID
	 */
	public List<Post> deletePosts(String[] postIDs) throws EappException;
	
	/**
	 * 对职位下级进行排序，按orderIDs数组顺序
	 * @param orderIDs 排序后的ID列表
	 * @param parentPostID 职位ID
	 */
	public void modifyOrder(String[] orderIDs, String parentPostID);
	
	
	/**
	 * 取得职位已绑定的用户列表
	 * @param postID 职位ID
	 * @return 
	 */
	public Set<UserAccount> getBindedUsers(String postID);
	
	
	/**
	 * 绑定用户
	 * 删除原有后，再绑定新的
	 * @param postID 职位ID
	 * @param userIDs 用户ID列表
	 * @return Post
	 */
	public Post txBindUser(String postID, String[] userIDs);
	
	/**
	 * 绑定用户帐号
	 * 追加绑定
	 * @param postID
	 * @param accountIDs
	 * @return
	 */
	public Post addBindUser(String postID, String[] accountIDs);
	
	/**
	 * 解除绑定用户帐号
	 * @param postID
	 * @param accountIDs
	 * @return
	 */
	public Post txUnBindUser(String postID, String[] accountIDs);
	
	/**
	 * 根据职位名称查找下级职位
	 * @param postName
	 * @return
	 */
	public List<Post> getSubGroupsByName(String postName);
	
	/**
	 * 根据职位ID，查找
	 * @param postID
	 * @return
	 */
	public List<Group> getManagedGroups(String postID);
}
