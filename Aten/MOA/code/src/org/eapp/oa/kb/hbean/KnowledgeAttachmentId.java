/**
 * 
 */
package org.eapp.oa.kb.hbean;

import org.eapp.oa.system.hbean.Attachment;

/**
 * @author Tim
 * @version 2009-08-25
 */
public class KnowledgeAttachmentId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 753637248221666077L;
	private Attachment attachment;
	private Knowledge knowledge;
	
	public KnowledgeAttachmentId() {
		
	}
	
	public KnowledgeAttachmentId(Knowledge knowledge, Attachment attachment) {
		this.knowledge = knowledge;
		this.attachment = attachment;
	}
	
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	
	
	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachment == null) ? 0 : attachment.hashCode());
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
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
		final KnowledgeAttachmentId other = (KnowledgeAttachmentId) obj;
		if (attachment == null) {
			if (other.attachment != null)
				return false;
		} else if (!attachment.equals(other.attachment))
			return false;
		if (knowledge == null) {
			if (other.knowledge != null)
				return false;
		} else if (!knowledge.equals(other.knowledge))
			return false;
		return true;
	}
}
