package org.eapp.action;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IPostBiz;
import org.eapp.comobj.SessionAccount;
import org.eapp.dao.param.PostQueryParameters;
import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.dto.PostTree;
import org.eapp.exception.EappException;
import org.eapp.hbean.Post;
import org.eapp.hbean.UserAccount;
import org.eapp.sys.config.SysConstants;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理职位管理的请求
 * @author zsy
 * @version
 */
public class PostAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(PostAction.class);
	private IPostBiz postBiz;

	private String postID;
	private String postName;
	private String description;
	private String parentPostID;
	private String groupID;
	private String[] postIDs;
	private String orderIDs;
	private String[] userIDs;
	private String binded;
	
	private String htmlValue;//输出HTML内容
	private List<Post> posts;
	private Post post;
	private Set<UserAccount> userAccounts;
	
	public String getHtmlValue() {
		return htmlValue;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public Post getPost() {
		return post;
	}

	public Set<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public void setPostBiz(IPostBiz postBiz) {
		this.postBiz = postBiz;
	}

	public void setPostID(String postID) {
		this.postID = postID;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParentPostID(String parentPostID) {
		this.parentPostID = parentPostID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setPostIDs(String[] postIDs) {
		this.postIDs = postIDs;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public void setUserIDs(String[] userIDs) {
		this.userIDs = userIDs;
	}

	public void setBinded(String binded) {
		this.binded = binded;
	}

	public String initQuery() {
		return success();
	}
	
	public String queryPost() {
		PostQueryParameters postQP = new PostQueryParameters();
		postQP.setPageSize(ServiceQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(postName)) {
			postQP.setPostName(postName);
		}
		postQP.addOrder("postName", true);
		try {
			ListPage<Post> page = postBiz.queryPost(postQP);
			if (page != null) {
				posts = page.getDataList();
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
	
	public String addPost() {
		if (StringUtils.isBlank(postName)) {
			return error("参数不能为空");
		}
		
		try {
			Post post = postBiz.addPost(parentPostID, groupID, postName, description);
			//写入日志
			ActionLogger.log(getRequest(), post.getPostID(), post.toString());
			return success(post.getPostID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(postID)) {
			return error("参数不能为空");
		}
		post = postBiz.getPostByID(postID);
		return success();
	}
	
	public String modifyPost(){
		if (StringUtils.isBlank(postID) || StringUtils.isBlank(postName)) {
			return error("参数不能为空");
		}
		
		try {
			Post post = postBiz.modifyPost(postID, parentPostID, groupID, postName, description);
			//写入日志
			if (post != null) {
				ActionLogger.log(getRequest(), post.getPostID(), post.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewPost() {
		if (StringUtils.isBlank(postID)) {
			return error("参数不能为空");
		}
		post = postBiz.getPostByID(postID);
		return success();
	}
	
	public String deletePosts() {
		if (postIDs == null || postIDs.length < 0) {
			return error("参数不能为空");
		}
		try {
			List<Post> posts = postBiz.deletePosts(postIDs);
			//写入日志
			if (posts != null) {
				for (Post p : posts) {
					ActionLogger.log(getRequest(), p.getPostID(), p.toString());
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
	
	
 
	public String savePostOrder() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		try {
			String[] postIDs = orderIDs.split(",");
			if (postIDs != null && postIDs.length > 0) {
				postBiz.modifyOrder(postIDs, postID);
				//写入日志
				StringBuffer sbf = new StringBuffer();
				sbf.append("\n排序对象：");
				Post g = null;
				for (String gid : postIDs) {
					if (StringUtils.isBlank(gid)) {
						continue;
					}
					g = postBiz.getPostByID(gid);
					sbf.append("\n").append(g.toString());
				}
				ActionLogger.log(getRequest(), postID, sbf.toString());
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
		if (StringUtils.isBlank(postID)) {
			return error("参数不能为空");
		}
		try {
			userAccounts = postBiz.getBindedUsers(postID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindUser() {
		if (StringUtils.isBlank(postID)) {
			return error("参数不能为空");
		}
		try {
			Post post = postBiz.txBindUser(postID, userIDs);
			//写入日志
			if (post != null) {
				StringBuffer sbf = new StringBuffer(post.toString());
				if (post.getUserAccounts() != null) {
					sbf.append("\n绑定对象：");
					for (UserAccount s : post.getUserAccounts()) {
						sbf.append("\n").append(s.toString());
					}
				}
				ActionLogger.log(getRequest(), post.getPostID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadPostTree() {
//		String path = HttpRequestHelper.getParameter(request, "path");

		try {
			List<Post> posts = postBiz.getSubPosts(postID, 1);
			htmlValue = new PostTree(posts).toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadSubPosts() {
		try {
			posts = postBiz.getSubPosts(postID, 1);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadPosts() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			return error("请先登录");
		}
		PostQueryParameters postQP = new PostQueryParameters();
		postQP.setPageSize(PostQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(postName)) {
			postQP.setPostName(postName);
		}
		postQP.setPostName(postName);

		if (StringUtils.isNotBlank(binded)) {
			postQP.setBinded(new Boolean("Y".equals(binded)));
		}
		postQP.addOrder("postName", true);
		try {
			ListPage<Post> page = postBiz.queryPost(postQP);
			if (page != null) {
				posts = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
