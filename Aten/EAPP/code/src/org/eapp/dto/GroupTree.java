/**
 * 
 */
package org.eapp.dto;

import java.io.Serializable;
import java.util.List;

import org.eapp.hbean.Group;


/**
 * 生成群组树结构片段
 * @author zsy
 * @version
 */
public class GroupTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5731513126107987489L;
	private List<Group> groups;
	private String type;
//	private String path;

	public GroupTree(List<Group> groups, String type) {
		this.groups = groups;
		this.type = type;
//		this.path = path;
	}
	
	public GroupTree() {
		
	}
	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}


	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}

	public String toString() {
		StringBuffer out = new StringBuffer();
		if (groups == null || groups.size() < 1) {
			return out.toString();
		}
		String url = "m/rbac_group/subgroups?groupID=";;
//		if (path != null && !path.trim().equals("")) {
//			url = path + "/m/rbac_group?act=subgroups&path=" + path + "&groupid=";
//		} else {
//			url = "m/rbac_group?act=subgroups&groupid=";
//		}
		for (Group g : groups) {
			out.append("<li id=\"").append(g.getGroupID()).append("\" groupid=\"").append(g.getGroupID())
					.append("\">").append("<span class=\"text\" groupid=\"").append(g.getGroupID())
					.append("\">").append(g.getGroupName()).append("</span>");
			if (g.isHasSubGroup()) {
				out.append("<ul class=\"ajax\">");
				out.append("<li id=\"").append(g.getGroupID()).append("\">{url:" + url).append(g.getGroupID())
						.append("&type=").append(type== null ? "" : type).append("}</li>");
				out.append("</ul>");
			}
			out.append("</li>");
		}
		return out.toString();
	}
}
