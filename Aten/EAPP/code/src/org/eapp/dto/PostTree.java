/**
 * 
 */
package org.eapp.dto;

import java.io.Serializable;
import java.util.List;

import org.eapp.hbean.Post;


/**
 * 生成职位树结构片段
 * @author zsy
 * @version
 */
public class PostTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5772363506052236470L;
	private List<Post> posts;
//	private String path;
	
	public PostTree(List<Post> posts) {
		this.posts = posts;
//		this.path = path;
	}
	
	public PostTree() {
		
	}
	/**
	 * @return the groups
	 */
	public List<Post> getPosts() {
		return posts;
	}


//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}

	/**
	 * @param groups the groups to set
	 */
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public String toString() {
		StringBuffer out = new StringBuffer();
		if (posts == null || posts.size() < 1) {
			return out.toString();
		}
		String url = "m/post/subposts?postID=";
//		if (path != null && !path.trim().equals("")) {
//			url = path + "/m/post?act=subposts&path=" + path + "&postid=";
//		} else {
//			url = "m/post?act=subposts&postid=";
//		}
		for (Post g : posts) {
			out.append("<li id=\"").append(g.getPostID()).append("\" postid=\"").append(g.getPostID())
					.append("\">").append("<span class=\"text\" postid=\"").append(g.getPostID())
					.append("\">").append(g.getPostName()).append("</span>");
			if (g.isHasSubPost()) {
				out.append("<ul class=\"ajax\">");
				out.append("<li id=\"").append(g.getPostID()).append("\">{url:" + url)
						.append(g.getPostID()).append("}</li>");
				out.append("</ul>");
			}
			out.append("</li>");
		}
		return out.toString();
	}
}
