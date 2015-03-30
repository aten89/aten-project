package org.eapp.oa.kb.hbean;

/**
 * BudgetList entity. Description:知识分类授权
 * 
 * @author MyEclipse Persistence Tools
 */

public class KnowledgeClassAssign implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2016107182795262613L;

	/**
	 * 用户
	 */
	public static final int TYPE_USER = 0;
	/**
	 * 群组
	 */
	public static final int TYPE_GROUP = 1;
	/**
	 * 职位
	 */
	public static final int TYPE_POST = 2;
	
	/**
	 * 知识查询权限
	 */
	public static final int FLAG_KB_QUERY = 0;
	/**
	 * 知识管理权限
	 */
	public static final int FLAG_KB_MANAGE = 1;
	/**
	 * 分类授权权限
	 */
	public static final int FLAG_CLASS_ASSIGN = 2;
	/**
	 * 分类管理权限
	 */
	public static final int FLAG_CLASS_MANAGE = 3;
	
	/**
	 * 授权给所有用户
	 */
	public static final String ASSIGNKEY_ALL_USER = "all_user";
	
	/** 主键 */
	private String id;

	/** 知识类别 */
	private KnowledgeClass knowledgeClass;

	/**
	 * 授权类型0：用户 1：角色 2：机构
	 */
	private int type;

	/** 授权键值 */
	private String assignKey;

	/** 类别标志 */
	private int flag;

	public KnowledgeClassAssign() {
	}
	public KnowledgeClassAssign(String id, KnowledgeClass knowledgeClass,
			int type, String assignKey, int flag) {
		super();
		this.id = id;
		this.knowledgeClass = knowledgeClass;
		this.type = type;
		this.assignKey = assignKey;
		this.flag = flag;
	}




	// 生成get/set方法
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public KnowledgeClass getKnowledgeClass() {
		return knowledgeClass;
	}

	public void setKnowledgeClass(KnowledgeClass knowledgeClass) {
		this.knowledgeClass = knowledgeClass;
	}

	public String getAssignKey() {
		return assignKey;
	}

	public void setAssignKey(String assignKey) {
		this.assignKey = assignKey;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
