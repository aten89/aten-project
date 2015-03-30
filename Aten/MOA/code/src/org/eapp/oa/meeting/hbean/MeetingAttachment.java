package org.eapp.oa.meeting.hbean;

/**
 * MeetingAttachment entity. Description:会议信息
 * 
 * @author sds
 */
@SuppressWarnings("unchecked")
public class MeetingAttachment implements java.io.Serializable {

	private static final long serialVersionUID = -8567581936155444441L;
	
	public static final int TYPE_ATTACH = 0; //会议资料
	public static final int TYPE_MINUTE = 1;  //会议纪要
	public static final int TYPE_CONTENTIMG = 2; //内容图片

	private Integer type;
	private MeetingAttachmentId id;
	
	public MeetingAttachment(MeetingAttachmentId id, Integer type) {
		this.id = id;
		this.type = type;
	}
	
	public MeetingAttachment() {
		
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public MeetingAttachmentId getId() {
		return id;
	}
	public void setId(MeetingAttachmentId id) {
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
		final MeetingAttachment other = (MeetingAttachment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}