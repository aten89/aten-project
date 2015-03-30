/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IGroupBiz;
import org.eapp.dao.IPostDAO;
import org.eapp.dao.IGroupDAO;
import org.eapp.dao.IRoleDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.GroupQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;

/**
 * @author zsy
 * @version 
 */
public class GroupBiz implements IGroupBiz {
	
	private IGroupDAO groupDAO;
	
	private IUserAccountDAO userAccountDAO;
	
	private IRoleDAO roleDAO;
	
	private IPostDAO postDAO;


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

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	@Override
	public Group getGroupByName(String groupName){
		List<Group> results = groupDAO.findByGroupName(groupName);
		if (results == null || results.isEmpty()) {
			return null;
		}
		//只取得第一个元素(这里邀请应用逻辑中，群组名称必须唯一)
		Group rg = results.get(0);
		//增加加载部门的上一个部门
     	return rg;
	}

	@Override
	public List<Group> getSubGroupsByName(String groupName, String type) {
		List<Group> groups = null;
		if (StringUtils.isBlank(groupName)) {
			//取得根级
			groups = groupDAO.findRootGroups(type);
		} else {
			List<Group> results = groupDAO.findByGroupName(groupName);
			if (results == null || results.isEmpty()) {
				return groups;
			}
			//只取得第一个元素(这里邀请应用逻辑中，群组名称必须唯一)
			Group rg = results.get(0);
			groups = groupDAO.findSubGroups(rg.getGroupID(), type);
		}
		return groups;		
	}
	
	public List<Group> getSubGroups(String groupID, String type) {
		List<Group> groups = null;
		if (StringUtils.isBlank(groupID)) {
			//取得根级
			groups = groupDAO.findRootGroups(type);
		} else {
			//取得子级
			groups = groupDAO.findSubGroups(groupID, type);
		}
		return groups;
	}
	
	@Override
	public List<Group> getGroupsByManagerPost(List<String> managerPostIDs) {
		if (managerPostIDs == null || managerPostIDs.isEmpty()) {
			return null;
		}
		return groupDAO.getGroupsByManagerPost(managerPostIDs);
	}

	@Override
	public ListPage<Group> queryGroup(GroupQueryParameters groupQP) {
		if (groupQP == null) {
			throw new IllegalArgumentException();
		}
		ListPage<Group> page = groupDAO.queryGroup(groupQP);
//		List<Group> groups = page.getDataList();
//		if (groups != null) {
//			for (Group g : groups) {
//				initParentGroupLazy(g, 50);
//			}
//		}
		return page;
	}
	
	/**
	 * 立即加载群组的父群组
	 * @param group
	 */
//	private void initParentGroupLazy(Group group, int breakTime) {
//		if (group == null || breakTime-- < 0) {
//			return;
//		}
//		Hibernate.initialize(group.getParentGroup());
//		initParentGroupLazy(group.getParentGroup(), breakTime);
//	}
	
	@Override
	public Group getGroupByID(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		return group;
	}

	@Override
	public Group addGroup(String parentGroupID, String postID,
			String groupName, String type, String description) throws EappException {
		if (StringUtils.isBlank(type) || StringUtils.isBlank(groupName)) {
			throw new IllegalArgumentException();
		}

		if (groupDAO.checkRepetition(null, groupName)) {//全局唯一20140924
			throw new EappException("机构名称已存在");
		}
		Group group = new Group();
		if (StringUtils.isNotBlank(postID)) {
			Post post = postDAO.findById(postID);
			if (post == null) {
				throw new IllegalArgumentException("职位不存在");
			} 
//			else if (user.getIsLogicDelete()) {
//				throw new MatrixException("用户已被删除");
//			} else if (user.getIsLock()) {
//				throw new MatrixException("用户已被锁定");
//			} else if (user.getInvalidDate() != null && user.getInvalidDate().getTime() < System.currentTimeMillis()) {
//				throw new MatrixException("用户已被失效");
//			}
			group.setManagerPost(post);
		}
		if (StringUtils.isNotBlank(parentGroupID)) {
			Group pg = groupDAO.findById(parentGroupID);
			if (pg == null) {
				throw new IllegalArgumentException("父机构不存在");
			}
			group.setParentGroup(pg);
		}
		
		group.setGroupName(groupName);
		group.setType(type);
		group.setDescription(description);
		//父ID下级中最大的DisplayOrder+1
		group.setDisplayOrder(new Integer(groupDAO.getNextDisplayOrder(parentGroupID)));
		//父ID的TreeLevel+1
		group.setTreeLevel(new Integer(groupDAO.getNextTreeLevel(parentGroupID)));
		groupDAO.save(group);
		return group;
	}

	@Override
	public Group modifyGroup(String groupID, String parentGroupID,
			String postID, String groupName, String type, String description) throws EappException {
		if (StringUtils.isBlank(groupID) || StringUtils.isBlank(type) || 
				StringUtils.isBlank(groupName)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(parentGroupID)) {
			parentGroupID = null;
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("groupID的对象不存在");
		}
		if (StringUtils.isNotBlank(postID)) {
			Post post = postDAO.findById(postID);
			if (post == null) {
				throw new EappException("职位不存在");
			} 
//			else if (user.getIsLogicDelete()) {
//				throw new MatrixException("用户已被删除");
//			} else if (user.getIsLock()) {
//				throw new MatrixException("用户已被锁定");
//			} else if (user.getInvalidDate() != null && user.getInvalidDate().getTime() < System.currentTimeMillis()) {
//				throw new MatrixException("用户已被失效");
//			}
			group.setManagerPost(post);
		} else {
			group.setManagerPost(null);
		}
		
		if (!groupName.equals(group.getGroupName())) {
			if (groupDAO.checkRepetition(null, groupName)) {//全局唯一20140924
				throw new EappException("机构名称已存在");
			}
		}
		//父ID有改动时
		String oldParentGroupID = group.getParentGroup() == null ? null : group.getParentGroup().getGroupID();
		if ((parentGroupID != null && !parentGroupID.equals(oldParentGroupID)) || 
				(parentGroupID == null && oldParentGroupID != null)) {
			//父结点不能为本身结点
			if (group.getGroupID().equals(parentGroupID)) {
				throw new EappException("父机构不能为本身机构");
			}
			
			Group pg = null;
			if (StringUtils.isNotBlank(parentGroupID)) {
				pg = groupDAO.findById(parentGroupID);
				if (pg == null) {
					throw new EappException("父机构不存在");
				}
			}
			//如果父结点是本身的子结点，刚抛异常
			if (checkParentID(group.getSubGroups(), parentGroupID, 50)) {
				throw new EappException("所选机构不能为该机构的子机构");
			}
			//修改同级群组的DisplayOrder，
			groupDAO.updateOrder(oldParentGroupID, group.getDisplayOrder());
			group.setDisplayOrder(new Integer(groupDAO.getNextDisplayOrder(parentGroupID)));
			//修改setTreeLevel
			int level = groupDAO.getNextTreeLevel(parentGroupID);
			group.setTreeLevel(new Integer(level));
			updateSubGroupTreeLevel(group.getSubGroups(), level, 50);
			
			group.setParentGroup(pg);
		}
		
		group.setGroupName(groupName);
		group.setType(type);
		group.setDescription(description);
		groupDAO.update(group);
		return group;
	}
	
	/**
	 * 检查要设置的父结点是不是本身的子结点
	 * @param subGroup
	 * @param groupID
	 * @param breakTime
	 * @return
	 */
	private boolean checkParentID(Set<Group> subGroup, String groupID, int breakTime) {
		if (StringUtils.isBlank(groupID) || subGroup == null || subGroup.size() < 1 || breakTime-- < 0) {
			return false;
		}
		for (Group g : subGroup) {
			if (g.getGroupID().equals(groupID)) {
				return true;
			}
			boolean result = checkParentID(g.getSubGroups(), groupID, breakTime);
			if (result) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 递归更新子群组的层级
	 * @param groups 群组集合
	 * @param parentLevel 父层级
	 */
	private void updateSubGroupTreeLevel(Set<Group> groups, int parentLevel, int breakTime) {
		if (groups == null || groups.isEmpty() || breakTime-- < 0) {
			return;
		}
		int level = parentLevel + 1;
		for (Group g : groups) {
			g.setTreeLevel(new Integer(level));
			updateSubGroupTreeLevel(g.getSubGroups(),level, breakTime);
		}
	}
	
	@Override
	public List<Group> deleteGroups(String[] groupIDs) throws EappException {
		List<Group> groups = new ArrayList<Group>();
		if (groupIDs == null || groupIDs.length < 1) {
			return groups;
		}
		Group group = null;
		for (String id : groupIDs) {
			if (StringUtils.isBlank(id)) {
				continue;
			}
			group = groupDAO.findById(id);
			if (group == null) {
				continue;
			}
			if (groupDAO.existSubGroup(group)) {
				throw new EappException("机构”" + group.getGroupName() + "“存在子机构");
			}
			if (groupDAO.existUserAccount(group)) {
				throw new EappException("机构”" + group.getGroupName() + "“已经被用户关联");
			}
			groupDAO.deleteByID(id);
			groups.add(group);
		}
		return groups;
	}

	@Override
	public void modifyOrder(String[] orderIDs, String parentGroupID) {
		if (orderIDs == null) {
			throw new IllegalArgumentException();
		}
		if (orderIDs.length == 0) {
			return;
		}
		groupDAO.saveOrder(orderIDs, parentGroupID);
	}

	@Override
	public Set<Role> getBindedRoles(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			return null;
		}
		Group group = groupDAO.findById(groupID);
		if (group == null) {
			return null;
		}
		group.getRoles().size();//加载延迟内容
		return group.getRoles();
	}
	
	@Override
	public List<Post> getBindedPosts(String groupID) {
//		if (StringUtils.isBlank(groupID)) {
//			return null;
//		}
//		Group group = groupDAO.findById(groupID);
//		if (group == null) {
//			return null;
//		}
//		Hibernate.initialize(group.getPosts());
//		return group.getPosts();
		return postDAO.findByGroupID(groupID);
	}

	@Override
	public Set<UserAccount> getBindedUsers(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			return null;
		}
		Group group = groupDAO.findById(groupID);
		if (group == null) {
			return null;
		}
		for (UserAccount user : group.getUserAccounts()) {
			user.getGroups().size();//加载延迟内容
		}
		return group.getUserAccounts();
	}

	@Override
	public Group txBindRole(String groupID, String[] roleIDs) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("机构对象不存在");
		}
		HashSet<Role> set = new HashSet<Role>();
		Role rr = null;
		if (roleIDs != null && roleIDs.length > 0) {
			for (String gid : roleIDs) {
				if (StringUtils.isBlank(gid)) {
					continue;
				}
				rr = roleDAO.findById(gid);
				if (rr != null) {
					set.add(rr);
				}
			}
		}
		group.setRoles(set);
		groupDAO.update(group);
		return group;
	}

	@Override
	public Group txBindUser(String groupID, String[] userIDs) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("机构对象不存在");
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
		group.setUserAccounts(set);
		groupDAO.update(group);
		return group;
	}
	
	@Override
	public Group txBindPost(String groupID, String[] postIDs) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("机构对象不存在");
		}
		HashSet<Post> set = new HashSet<Post>();
		Post post = null;
		if (postIDs != null && postIDs.length > 0) {
			for (String pid : postIDs) {
				if (StringUtils.isBlank(pid)) {
					continue;
				}
				post = postDAO.findById(pid);
				if (post != null) {
					post.setGroup(group);
					set.add(post);
				}
			}
		}
		group.setPosts(set);
		if (group.getManagerPost() != null) {
			//如果管理者职位不在绑定列表中，则清空
			boolean findedManPost = false;
			for (Post p : set) {
				if (p.getPostID().equals(group.getManagerPost().getPostID())) {
					findedManPost = true;
					break;
				}
			}
			if (!findedManPost) {
				group.setManagerPost(null);
			}
		}
		groupDAO.update(group);
		return group;
	}
	
	@Override
	public Group addBindUser(String groupID, String[] accountIDs) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("机构对象不存在");
		}
		Set<UserAccount> set = group.getUserAccounts();
		if (set == null ) {
			set = new HashSet<UserAccount>();
			group.setUserAccounts(set);
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
		
		groupDAO.update(group);
		return group;
	}
	
	@Override
	public Group txUnBindUser(String groupID, String[] accountIDs) {
		if (StringUtils.isBlank(groupID)) {
			throw new IllegalArgumentException();
		}
		Group group = groupDAO.findById(groupID);
		if (group == null)  {
			throw new IllegalArgumentException("机构对象不存在");
		}
		Set<UserAccount> set = group.getUserAccounts();
		if (set == null ) {
			return group;
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
		groupDAO.update(group);
		return group;
	}

	@Override
	public Set<Post> getPostsByGroup(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			return null;
		}
		Group group = groupDAO.findById(groupID);
		group.getPosts().size();//加载延迟内容
		return group.getPosts();
	}

	@Override
	public List<Group> getGroupsByType(String type) {
		return groupDAO.findGroupsByType(type);
	}

}
