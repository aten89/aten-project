/**
 * 
 */
package org.eapp.rpc;

import java.util.List;

import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.PostInfo;


/**
 * @author zsy
 * @version 1.0
 */
public interface IPostService {
	
	/**
	 * 根据名称·获取职位实例
	 * @param sessionID
	 * @param postName
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public PostInfo getPost(String sessionID, String postName) throws RpcAuthorizationException;	
	
	/**
	 * 根据ID获取职位实例
	 * @param sessionID
	 * @param postID
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public PostInfo getPostByID(String sessionID, String postID) throws RpcAuthorizationException;
	
	/**
	 * 获取一个职位直接管理的下属职位
	 * @param sessionID
	 * @param postName
	 * @return
	 * @throws RpcAuthorizationException
	 */
	public List<PostInfo> getChildPosts(String sessionID , String postName) throws RpcAuthorizationException;
	
	
	/**
	 * 新增职位
	 * @param sessionID
	 * @param parentPostID
	 * @param postName
	 * @param bandingGroupID
	 * @param description
	 * @return
	 * @throws EappException
	 * @throws RpcAuthorizationException
	 */
	public String addPost(String sessionID, String parentPostID, String bandingGroupID, 
			String postName, String description) throws EappException , RpcAuthorizationException;
	
	/**
	 * 修改职位
	 * @param sessionID
	 * @param postID
	 * @param parentPostID
	 * @param postName
	 * @param bandingGroupID
	 * @param description
	 * @return
	 * @throws EappException
	 * @throws RpcAuthorizationException
	 */
	public void modifyPost(String sessionID, String postID, String parentPostID, String bandingGroupID, 
			String postName, String description) throws EappException, RpcAuthorizationException;
	
	/**
	 * 删除职位
	 * @param sessionID
	 * @param postIDs
	 * @throws RpcAuthorizationException
	 * @throws RelatedException
	 */
	public void deletePosts(String sessionID, String[] postIDs) throws EappException, RpcAuthorizationException;

}
