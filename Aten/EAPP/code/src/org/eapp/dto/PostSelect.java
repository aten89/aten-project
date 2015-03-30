/**
 * 
 */
package org.eapp.dto;

import java.util.Collection;

import org.eapp.hbean.Post;


/**
 * 生成动作列表
 * @author zsy
 * @version 
 */
public class PostSelect extends HTMLSelect {
	
	private Collection<Post> posts;
	
	public PostSelect(Collection<Post> posts) {
		this.posts = posts;
	}

	/**
	 * @return the actions
	 */
	public Collection<Post> getPosts() {
		return posts;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setPosts(Collection<Post> posts) {
		this.posts = posts;
	}

	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**matural</div>
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		if (posts == null || posts.size() < 1) {
			return out.toString();
		}
		for (Post g : posts) {
			out.append(createOption(g.getPostID(), g.getPostName()));
		}
		return out.toString();
	}
}
