/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IPostBiz;
import org.eapp.dao.IPostDAO;
import org.eapp.dao.IGroupDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.PostQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;


/**
 * @author zsy
 * @version 
 */
public class PostBiz implements IPostBiz {
	
	private IGroupDAO groupDAO;
	
	private IUserAccountDAO userAccountDAO;
	
	private IPostDAO postDAO;

	public IUserAccountDAO getUserAccountDAO() {
		return userAccountDAO;
	}

	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}
	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(IGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setPostDAO(IPostDAO postDAO) {
		this.postDAO = postDAO;
	}

	public List<Post> getSubPosts(String postID, int depth) {
		List<Post> posts = null;
		if (StringUtils.isBlank(postID)) {
			//取得根级
			posts = postDAO.findRootPosts();
		} else {
			Post rg = postDAO.findById(postID);
			if (rg == null) {
				return posts;
			}
			depth = rg.getTreeLevel().intValue() + depth;
			posts = postDAO.findSubPosts(postID);
		}
		initSubPostLazy(posts, depth);
		return posts;
	}
	
	/**
	 * 
	 * 立即加载多级职位的下级职位
	 * 
	 * @param cols 职位集合
	 */
	private void initSubPostLazy(Collection<Post> cols, int level) {
		if (cols == null || cols.size() == 0) {
			return;
		}
		for (Post m : cols) {
			if (level == 0 || m.getTreeLevel() < level) {
				m.getSubPosts().size();//加载延迟内容
				initSubPostLazy(m.getSubPosts(), level);
			}
		}
	}

	public ListPage<Post> queryPost(PostQueryParameters groupQP) {
		if (groupQP == null) {
			throw new IllegalArgumentException();
		}
		ListPage<Post> page = postDAO.queryPost(groupQP);
//		List<Post> posts = page.getDataList();
//		if (posts != null) {
//			for (Post p : posts) {
//				initParentPostLazy(p, 50);
//			}
//		}
		return page;
	}
	
	/**
	 * 立即加载职位的上级职位
	 * @param post
	 */
//	private void initParentPostLazy(Post post, int breakTime) {
//		if (post == null || breakTime-- < 0) {
//			return;
//		}
//		Hibernate.initialize(post.getParentPost());
//		initParentPostLazy(post.getParentPost(), breakTime);
//	}
	
	public Post getPostByID(String postID) {
		if (StringUtils.isBlank(postID)) {
			throw new IllegalArgumentException();
		}
		Post post = postDAO.findById(postID);
		return post;
	}

	public Post addPost(String parentPostID, String groupID, 
			String postName, String description) throws EappException {
		if (StringUtils.isBlank(postName)) {
			throw new IllegalArgumentException();
		}
		
		postName = postName.trim();
		if (postDAO.checkRepetition(null, postName)) {//职位全局唯一20140924
			throw new EappException("职位名称已存在");
		}
		Post post = new Post();
		if (StringUtils.isNotBlank(groupID)) {
			Group group = groupDAO.findById(groupID);
			if (group == null) {
				throw new EappException("机构不存在");
			} 
			post.setGroup(group);
		}
		if (StringUtils.isNotBlank(parentPostID)) {
			Post pg = postDAO.findById(parentPostID);
			if (pg == null) {
				throw new EappException("上级职位不存在");
			}
			post.setParentPost(pg);
		}
		
		post.setPostName(postName);
		post.setDescription(description);
		//父ID下级中最大的DisplayOrder+1
		post.setDisplayOrder(new Integer(postDAO.getNextDisplayOrder(parentPostID)));
		//父ID的TreeLevel+1
		post.setTreeLevel(new Integer(postDAO.getNextTreeLevel(parentPostID)));
		postDAO.save(post);
		return post;
	}

	public Post modifyPost(String postID, String parentPostID, String groupID, 
			String postName, String description) throws EappException {
		if (StringUtils.isBlank(postID) || StringUtils.isBlank(postName)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(parentPostID)) {
			parentPostID = null;
		}
		Post post = postDAO.findById(postID);
		if (post == null)  {
			throw new IllegalArgumentException("职位对象不存在");
		}
		if (StringUtils.isNotBlank(groupID)) {
			Group group = groupDAO.findById(groupID);
			if (group == null) {
				throw new EappException("机构不存在");
			} 
			post.setGroup(group);	
		} else {
			post.setGroup(null);
		}
		postName = postName.trim();
		if (!postName.equals(post.getPostName())) {
			if (postDAO.checkRepetition(null, postName)) {//职位全局唯一20140924
				throw new EappException("职位名称已存在");
			}
		}
		//父ID有改动时
		String oldParentPostID = post.getParentPost() == null ? null : post.getParentPost().getPostID();
		if ((parentPostID != null && !parentPostID.equals(oldParentPostID)) || 
				(parentPostID == null && oldParentPostID != null)) {
			//父结点不能为本身结点
			if (post.getPostID().equals(parentPostID)) {
				throw new EappException("父结点不能为本身结点");
			}
			
			Post pg = null;
			if (StringUtils.isNotBlank(parentPostID)) {
				pg = postDAO.findById(parentPostID);
				if (pg == null) {
					throw new EappException("上级职位不存在");
				}
			}
			//如果父结点是本身的子结点，刚抛异常
			if (checkParentID(post.getSubPosts(), parentPostID, 50)) {
				throw new EappException("父结点不能为本身的子结点");
			}
			//修改同级群组的DisplayOrder，
			postDAO.updateOrder(oldParentPostID, post.getDisplayOrder());
			post.setDisplayOrder(new Integer(postDAO.getNextDisplayOrder(parentPostID)));
			//修改setTreeLevel
			int level = postDAO.getNextTreeLevel(parentPostID);
			post.setTreeLevel(new Integer(level));
			updateSubPostTreeLevel(post.getSubPosts(), level, 50);
			
			post.setParentPost(pg);
		}
		
		post.setPostName(postName);
		post.setDescription(description);
		postDAO.update(post);
		return post;
	}
	
	/**
	 * 检查要设置的父结点是不是本身的子结点
	 * @param subPost
	 * @param postID
	 * @param breakTime
	 * @return
	 */
	private boolean checkParentID(Set<Post> subPost, String postID, int breakTime) {
		if (StringUtils.isBlank(postID) || subPost == null || subPost.isEmpty() || breakTime-- < 0) {
			return false;
		}
		for (Post p : subPost) {
			if (p.getPostID().equals(postID)) {
				return true;
			}
			boolean result = checkParentID(p.getSubPosts(), postID, breakTime);
			if (result) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 递归更新下级职位的层级
	 * @param posts 职位集合
	 * @param parentLevel 父层级
	 */
	private void updateSubPostTreeLevel(Set<Post> posts, int parentLevel, int breakTime) {
		if (posts == null || posts.isEmpty() || breakTime-- < 0) {
			return;
		}
		int level = parentLevel + 1;
		for (Post p : posts) {
			p.setTreeLevel(new Integer(level));
			updateSubPostTreeLevel(p.getSubPosts(),level, breakTime);
		}
	}
	
	public List<Post> deletePosts(String[] postIDs) throws EappException {
		List<Post> posts = new ArrayList<Post>();
		if (postIDs == null || postIDs.length < 1) {
			return posts;
		}
		Post post = null;
		for (String id : postIDs) {
			if (StringUtils.isBlank(id)) {
				continue;
			}
			post = postDAO.findById(id);
			if (post == null) {
				continue;
			}
			if (postDAO.existSubPost(post)) {
				throw new EappException("职位”" + post.getPostName() + "“存在下级职位");
			}
			postDAO.deleteByID(id);
			posts.add(post);
		}
		return posts;
	}

	public void modifyOrder(String[] orderIDs, String parentPostID) {
		if (orderIDs == null) {
			throw new IllegalArgumentException();
		}
		if (orderIDs.length == 0) {
			return;
		}
		postDAO.saveOrder(orderIDs, parentPostID);
	}

	public Set<UserAccount> getBindedUsers(String postID) {
		if (StringUtils.isBlank(postID)) {
			return null;
		}
		Post post = postDAO.findById(postID);
		if (post == null) {
			return null;
		}
		for (UserAccount user : post.getUserAccounts()) {
			user.getGroups().size();//加载延迟内容
		}
		return post.getUserAccounts();
	}

	public Post txBindUser(String postID, String[] userIDs) {
		if (StringUtils.isBlank(postID)) {
			throw new IllegalArgumentException();
		}
		Post post = postDAO.findById(postID);
		if (post == null)  {
			throw new IllegalArgumentException("职位对象不存在");
		}
		HashSet<UserAccount> set = new HashSet<UserAccount>();
		UserAccount ua = null;
		if (userIDs != null && userIDs.length > 0) {
			for (String gid : userIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				ua = userAccountDAO.findById(gid);
				if (ua != null) {
					set.add(ua);
				}
			}
		}
		post.setUserAccounts(set);
		postDAO.update(post);
		return post;
	}
	
	public Post addBindUser(String postID, String[] accountIDs) {
		if (StringUtils.isBlank(postID)) {
			throw new IllegalArgumentException();
		}
		Post post = postDAO.findById(postID);
		if (post == null)  {
			throw new IllegalArgumentException("职位对象不存在");
		}
		Set<UserAccount> set = post.getUserAccounts();
		if (set == null ) {
			set = new HashSet<UserAccount>();
			post.setUserAccounts(set);
		}
		UserAccount ua = null;
		if (accountIDs != null && accountIDs.length > 0) {
			for (String gid : accountIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				ua = userAccountDAO.findByAccountID(gid);
				if (ua != null) {
					set.add(ua);
				}
			}
		}
		
		postDAO.update(post);
		return post;
	}
	
	public Post txUnBindUser(String postID, String[] accountIDs) {
		if (StringUtils.isBlank(postID)) {
			throw new IllegalArgumentException();
		}
		Post post = postDAO.findById(postID);
		if (post == null)  {
			throw new IllegalArgumentException("职位对象不存在");
		}
		Set<UserAccount> set = post.getUserAccounts();
		if (set == null ) {
			return post;
		}
		UserAccount ua = null;
		if (accountIDs != null && accountIDs.length > 0) {
			for (String gid : accountIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				ua = userAccountDAO.findByAccountID(gid);
				if (ua != null) {
					set.remove(ua);
				}
			}
		}
		postDAO.update(post);
		return post;
	}

	@Override
	public Post getPostByName(String postName) {	
		if (StringUtils.isBlank(postName)) {
			throw new IllegalArgumentException();
		}
		List<Post> posts = postDAO.findByPostName(postName);
		if(posts == null || posts.isEmpty()) {
			return null;
		}
		Post post = posts.get(0);
		post.getUserAccounts().size();//加载延迟内容
		post.getSubPosts().size();//加载延迟内容
		return post;
	}
	
	@Override
	public List<Post> getSubGroupsByName(String postName) {
		List<Post> posts = null;
		if (StringUtils.isBlank(postName)) {
			//取得根级
			posts = postDAO.findRootPosts();
		} else {
			List<Post> results = postDAO.findByPostName(postName);
			if (results == null || results.isEmpty()) {
				return posts;
			}
			//只取得第一个元素(这里邀请应用逻辑中，群组名称必须唯一)
			Post p = results.get(0);
			posts = postDAO.findSubPosts(p.getPostID());
		}
		return posts;
	}

	@Override
	public List<Group> getManagedGroups(String postID) {
		if(StringUtils.isBlank(postID)){
			throw new IllegalArgumentException();
		}
		List<String> managerPostIDs = new ArrayList<String>();
		managerPostIDs.add(postID);
		//迭代查询所有的下属职位ID
		cascadeSubPost(postID , managerPostIDs);		
		//获取职位管理的部门
		if(managerPostIDs.isEmpty()){
			return null;
		}
		return groupDAO.getGroupsByManagerPost(managerPostIDs);			 
	}
	
	
	private void cascadeSubPost(String postID , List<String> postIDs){
		List<Post> subPosts = postDAO.findSubPosts(postID);
		if(subPosts != null && subPosts.size() > 0 ){
			for(Post p : subPosts){
				String subPostID = p.getPostID();
				postIDs.add(subPostID);
				cascadeSubPost(subPostID , postIDs);
			}
		}
	}

}
