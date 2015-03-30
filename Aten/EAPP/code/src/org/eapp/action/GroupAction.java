package org.eapp.action;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IGroupBiz;
import org.eapp.dao.param.GroupQueryParameters;
import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.dto.GroupSelect;
import org.eapp.dto.GroupTree;
import org.eapp.dto.PostSelect;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.Group;
import org.eapp.hbean.Role;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理群组管理的请求
 * @author zsy
 * @version
 */
public class GroupAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(GroupAction.class);
	
	private IGroupBiz groupBiz;

	private String groupID;
	private String groupName;
	private String type;
	private String description;
	private String parentGroupID;
	private String postID;
	private String[] groupIDs;
	private String[] roleIDs;
	private String orderIDs;
	private String[] userIDs;
	private String[] postIDs;
	
	private List<Group> groups;
	private Group group;
	private Set<Role> roles;
	private List<Post> posts;
	private Set<UserAccount> userAccounts;
	private String htmlValue;//输出HTML内容
	
	public List<Group> getGroups() {
		return groups;
	}

	public Group getGroup() {
		return group;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public Set<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	public void setGroupBiz(IGroupBiz groupBiz) {
		this.groupBiz = groupBiz;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParentGroupID(String parentGroupID) {
		this.parentGroupID = parentGroupID;
	}

	public void setPostID(String postID) {
		this.postID = postID;
	}

	public void setGroupIDs(String[] groupIDs) {
		this.groupIDs = groupIDs;
	}

	public void setRoleIDs(String[] roleIDs) {
		this.roleIDs = roleIDs;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public void setUserIDs(String[] userIDs) {
		this.userIDs = userIDs;
	}

	public void setPostIDs(String[] postIDs) {
		this.postIDs = postIDs;
	}

	public String initQuery() {
		return success();
	}
	
	public String loadGroupTree() {
		try {
			List<Group> groups = groupBiz.getSubGroups(groupID, type);
			htmlValue = new GroupTree(groups, type).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String queryGroup() {
		GroupQueryParameters groupQP = new GroupQueryParameters();
		groupQP.setPageSize(ServiceQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(groupName)) {
			groupQP.setGroupName(groupName);
		}
		
		groupQP.addOrder("groupName", true);
		try {
			ListPage<Group> page = groupBiz.queryGroup(groupQP);
			if (page != null) {
				groups = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initAdd() {
		return success();
	}
	
	public String addGroup() {
		if (StringUtils.isBlank(groupName) || StringUtils.isBlank(type)) {
			return error("参数不能为空");
		}

		try {
			Group group = groupBiz.addGroup(parentGroupID, postID, groupName, type, description);
			//写入日志
			ActionLogger.log(getRequest(), group.getGroupID(), group.toString());
			return success(group.getGroupID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		group = groupBiz.getGroupByID(groupID);
		return success();
	}
	
	public String modifyGroup() {
		if (StringUtils.isBlank(groupID) || StringUtils.isBlank(groupName) || StringUtils.isBlank(type)) {
			return error("参数不能为空");
		}
		
		try {
			Group group = groupBiz.modifyGroup(groupID, parentGroupID, postID, groupName, type, description);
			//写入日志
			if (group != null) {
				ActionLogger.log(getRequest(), group.getGroupID(), group.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewGroup() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		group = groupBiz.getGroupByID(groupID);
		return success();
	}
	
	public String deleteGroups() {
		if (groupIDs == null || groupIDs.length < 0) {
			return error("参数不能为空");
		}
		try {
			List<Group> groups = groupBiz.deleteGroups(groupIDs);
			//写入日志
			if (groups != null) {
				for (Group g : groups) {
					ActionLogger.log(getRequest(), g.getGroupID(), g.toString());
				}
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initOrder() {
		return success();
	}
	
	public String loadSubGroups() {
		try {
			groups = groupBiz.getSubGroups(groupID, null);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
 
	public String saveGroupOrder() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		try {
			
			String[] groupIDs = orderIDs.split(",");
			if (groupIDs != null && groupIDs.length > 0) {
				groupBiz.modifyOrder(groupIDs, groupID);
				//写入日志
				StringBuffer sbf = new StringBuffer();
				sbf.append("\n排序对象：");
				Group g = null;
				for (String gid : groupIDs) {
					if (StringUtils.isBlank(gid)) {
						continue;
					}
					g = groupBiz.getGroupByID(gid);
					sbf.append("\n").append(g.toString());
				}
				ActionLogger.log(getRequest(), groupID, sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindRole() {
		return success();
	}
	
	public String loadBindedRoles() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			roles = groupBiz.getBindedRoles(groupID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindRole() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			Group group = groupBiz.txBindRole(groupID, roleIDs);
			//写入日志
			if (group != null) {
				StringBuffer sbf = new StringBuffer(group.toString());
				if (group.getRoles() != null) {
					sbf.append("\n绑定对象：");
					for (Role s : group.getRoles()) {
						sbf.append("\n").append(s.toString());
					}
				}
				ActionLogger.log(getRequest(), group.getGroupID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindUser() {
		return success();
	}
	
	public String loadBindedUsers() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			userAccounts = groupBiz.getBindedUsers(groupID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindUser() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			Group group = groupBiz.txBindUser(groupID, userIDs);
			//写入日志
			if (group != null) {
				StringBuffer sbf = new StringBuffer(group.toString());
				if (group.getUserAccounts() != null) {
					sbf.append("\n绑定对象：");
					for (UserAccount s : group.getUserAccounts()) {
						sbf.append("\n").append(s.toString());
					}
				}
				ActionLogger.log(getRequest(), group.getGroupID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initBindPost() {
		return success();
	}
	
	public String loadBindedPosts() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			posts = groupBiz.getBindedPosts(groupID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindPost() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			Group group = groupBiz.txBindPost(groupID, postIDs);
			//写入日志
			if (group != null) {
				StringBuffer sbf = new StringBuffer(group.toString());
				if (group.getPosts() != null) {
					sbf.append("\n绑定对象：");
					for (Post s : group.getPosts()) {
						sbf.append("\n").append(s.toString());
					}
				}
				ActionLogger.log(getRequest(), group.getGroupID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadGroupPosts() {
		if (StringUtils.isBlank(groupID)) {
			return error("参数不能为空");
		}
		try {
			Set<Post> posts = groupBiz.getPostsByGroup(groupID);
			htmlValue = new PostSelect(posts).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadGroupSelect() {

		try {
			List<Group> groups = groupBiz.getGroupsByType(type);
			htmlValue = new GroupSelect(groups).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
