package org.eapp.oa.kb.hbean;

import java.util.Date;

import org.eapp.client.util.UsernameCache;


/**
 * BudgetList entity. Description:知识回复
 * 
 * @author MyEclipse Persistence Tools
 */

public class KnowledgeReply implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921836726431893547L;
	
	public static final int WEB_STATUS_USING = 1; //生效
	public static final int WEB_STATUS_DELETED = 0; //删除
	
	/** 主键 */
	private String id;

	/** 所属知识 */
	private Knowledge knowledge;

	/** 内容 */
	private String content;

	/** 发布人机构 */
	private String groupName;

	/** 回复人 */
	private String replyMan;

	/** 回复时间 */
	private Date replyDate;

	// 生成get/set方法
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getReplyMan() {
		return replyMan;
	}
	public String getReplyManName() {
		return UsernameCache.getDisplayName(replyMan);
	}

	public void setReplyMan(String replyMan) {
		this.replyMan = replyMan;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

}
