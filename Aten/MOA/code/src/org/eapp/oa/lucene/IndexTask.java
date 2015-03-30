package org.eapp.oa.lucene;

import java.util.Date;

/**
 * 索引的更新任务，
 * 由于对索引的平凡操作效率很低，一般是先记在队列中，定时批量处理
 * 
 * @author zsy
 * @version Jun 12, 2009
 */
public class IndexTask implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6205374805530168637L;
	
	public static final String ACTION_ADD = "add";
	public static final String ACTION_MODIFY = "modify";
	public static final String ACTION_DELETE = "delete";
	
	/** 主键 */
	private String id;

	/** 动作 */
	private String action;

	/** 业务数据的主键 */
	private String documentId;

	/**
	 * 检索文档类型，类型不同数据将会建到不同的索引目录
	 * 值来源于IndexBuilder中的类型，否则会出错，
	 * 也就是说这个类型来决定由哪个IndexBuilder来处理
	 */
	private String documentType;

	/** 创建时间 */
	private Date createDate;

	
	public IndexTask() {
	}

	public IndexTask(String id) {
		this.id = id;
	}

	public IndexTask(String documentid, String documentType, String action) {
		this.action = action;
		this.documentId = documentid;
		this.documentType = documentType;
		this.createDate = new Date();
	}

	// 生成get/set方法
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IndexTask other = (IndexTask) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
