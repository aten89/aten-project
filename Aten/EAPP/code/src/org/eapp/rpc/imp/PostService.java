/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eapp.blo.IPostBiz;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.Post;
import org.eapp.rpc.IPostService;
import org.eapp.rpc.dto.PostInfo;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @author 林良益
 * @version 1.0
 */
public class PostService extends HessianServlet implements IPostService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7821940054206725971L;
	
	private static final String MODULE_KEY = "post";
	private IPostBiz postBiz;
	

	public void setPostBiz(IPostBiz postBiz) {
		this.postBiz = postBiz;
	}
	
	@Override
	public PostInfo getPost(String sessionID, String postName)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.VIEW);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.VIEW, null,"方法:getPostByName 参数:sessionID="+sessionID
//				+",postName="+postName);
		if (postName == null) {
			return null;
		}
		//根据名称获取职位
		Post post = postBiz.getPostByName(postName);
		//转化为DTO
		return copy(post);
	}
	
	@Override
	public PostInfo getPostByID(String sessionID, String postID)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.VIEW);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.VIEW, null,"方法:getPostByID 参数:sessionID="+sessionID
//				+",postID="+postID);
		if (postID == null) {
			return null;
		}
		Post post = postBiz.getPostByID(postID);
		//复制属性
		return copy(post);
	}
	
	@Override
	public List<PostInfo> getChildPosts(String sessionID, String postName)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getSubPostByName 参数:sessionID="+sessionID
//				+",postName="+postName);
		List<Post> pList = postBiz.getSubGroupsByName(postName);
		if (pList == null) {
			return null;
		}
		List<PostInfo> subPostDTOs = new ArrayList<PostInfo>();
		for(Post subPost : pList){
			subPostDTOs.add(copy(subPost));
		}
		return subPostDTOs;
	}
	
	@Override
	public String addPost(String sessionID, String parentPostID, String bandingGroupID, String postName,
			String description) throws EappException, RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.ADD);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.ADD, null,"方法:PostService.addPost 参数:sessionID="+sessionID
				+",parentPostID="+parentPostID+",postName="+postName+",bandingGroupID="+bandingGroupID+",description="+description);
		Post post = postBiz.addPost(parentPostID, bandingGroupID, postName, description);
		if (post == null) {
			return null;
		}
		RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.ADD, post.getPostID(), post.toString());
		return post.getPostID();
	}
	
	@Override
	public void modifyPost(String sessionID, String postID,
			String parentPostID, String bandingGroupID, String postName,
			String description) throws RpcAuthorizationException, EappException {

		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.MODIFY);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.MODIFY, null,"方法:PostService.modifyPost 参数:sessionID="+sessionID
				+",postID="+postID+",parentPostID="+parentPostID+",postName="+postName+",bandingGroupID="+bandingGroupID+",description="+description);
		Post post = postBiz.modifyPost(postID, parentPostID, bandingGroupID, postName, description);
		if (post == null) {
			return;
		}
		RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.MODIFY, post.getPostID(), post.toString());
	}
	
	@Override
	public void deletePosts(String sessionID, String[] postIDs) throws EappException, RpcAuthorizationException {

		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.DELETE);
		RPCLogger.interfaceLog(sessionID, MODULE_KEY, SysConstants.DELETE, null,"方法:PostService.deletePosts 参数:sessionID="
				+ sessionID + ",postIDs=" + Arrays.toString(postIDs));
		if (postIDs == null || postIDs.length == 0) {
			return;
		}
		List<Post> postList = postBiz.deletePosts(postIDs);
		if (postList != null) {
			for(Post post : postList){
				RPCLogger.actionLog(sessionID, MODULE_KEY, SysConstants.DELETE, post.getPostID(), post.toString());
			}
		}
	}

	
	/**
	 * 复制Post属性到远程传输对象
	 * @param src
	 * @param dest
	 */
	static PostInfo copy(Post src) {
		PostInfo dest = new PostInfo();
		if(src.getGroup() != null){
			dest.setBandingGroupID(src.getGroup().getGroupID());
		}
		dest.setDescription(src.getDescription());
		dest.setDisplayOrder(src.getDisplayOrder());
		if(src.getParentPost() != null){
			dest.setParentPostID(src.getParentPost().getPostID());
		}
		dest.setPostID(src.getPostID());
		dest.setPostName(src.getPostName());
		dest.setTreeLevel(src.getTreeLevel());
		return dest;
	}
}
