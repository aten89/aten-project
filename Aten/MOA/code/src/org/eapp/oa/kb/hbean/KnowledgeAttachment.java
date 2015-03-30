package org.eapp.oa.kb.hbean;

/**
 * @author Tim
 * @version 2009-08-25
 */
@SuppressWarnings("unchecked")
public class KnowledgeAttachment implements java.io.Serializable {

	private static final long serialVersionUID = 1716210086761340156L;
	
	
	public static final int TYPE_ATTACH = 0; //知识附件
	public static final int TYPE_CONTENT_IMG = 1;  //知识内容图片

	private Integer type;
	private KnowledgeAttachmentId id;
	
	public KnowledgeAttachment(KnowledgeAttachmentId id, Integer type) {
		this.id = id;
		this.type = type;
	}
	
	public KnowledgeAttachment() {
		
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public KnowledgeAttachmentId getId() {
		return id;
	}
	public void setId(KnowledgeAttachmentId id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final KnowledgeAttachment other = (KnowledgeAttachment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}