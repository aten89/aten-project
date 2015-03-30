/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.EappException;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IPostService;
import org.eapp.rpc.dto.PostInfo;


/**
 * @author 林良益
 * 
 */
public class PostService extends BaseEAPPService {
	private static Log log = LogFactory.getLog(PostService.class);
	private static IPostService service;
	
	private IPostService getService() throws MalformedURLException{
		if (service == null) {
			synchronized (PostService.class) {
			service = (IPostService) factory.create(IPostService.class, getServiceUrl(SystemProperties.SERVICE_POST));
			}
		}
		return service;
	}
	
	/**
	 * 根据名称·获取职位实例
	 * @param sessionID
	 * @param postName
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException 
	 */
	public PostInfo getPost(String postName)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getPost(getDefaultSessionID(false), postName);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getPost(getDefaultSessionID(true), postName);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 根据ID获取职位实例
	 * @param sessionID
	 * @param postID
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException 
	 */
	public PostInfo getPostByID(String postID)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getPostByID(getDefaultSessionID(false), postID);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getPostByID(getDefaultSessionID(true), postID);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 获取一个职位直接管理的下属职位
	 * @param sessionID
	 * @param postName
	 * @return
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException 
	 */
	public List<PostInfo> getChildPosts(String postName)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getChildPosts(getDefaultSessionID(false), postName);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getChildPosts(getDefaultSessionID(true), postName);
			} else {
				throw e;
			}
		}			
	}
	
	
	/**
	 * 新增职位
	 * @param sessionID
	 * @param parentPostID
	 * @param postName
	 * @param bandingGroupID
	 * @param description
	 * @return
	 * @throws EappException
	 * @throws MalformedURLException 
	 */
	public String addPost(String parentPostID, String bandingGroupID, String postName, String description)
			throws EappException, MalformedURLException {
		
		try {
			return getService().addPost(getDefaultSessionID(false), parentPostID, bandingGroupID, postName, description);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().addPost(getDefaultSessionID(true), parentPostID, bandingGroupID, postName, description);
			} else {
				throw e;
			}
		}

	}
	
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
	 * @throws MalformedURLException 
	 */
	public void modifyPost(String postID , String parentPostID, String bandingGroupID, String postName, String description)
			throws EappException, MalformedURLException {
		try {
			getService().modifyPost(getDefaultSessionID(false), postID, parentPostID, bandingGroupID, postName, description);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				getService().modifyPost(getDefaultSessionID(true), postID, parentPostID, bandingGroupID, postName, description);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 删除职位
	 * @param sessionID
	 * @param postIDs
	 * @throws MalformedURLException 
	 * @throws EappException 
	 */
	public void deletePosts(String[] postIDs)
			throws MalformedURLException, EappException {
		try {
			getService().deletePosts(getDefaultSessionID(false), postIDs);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				getService().deletePosts(getDefaultSessionID(true), postIDs);
			}else{
				throw e;
			}
		}
	}
}
