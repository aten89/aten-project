/**
 * 
 */
package org.eapp.oa.meeting.hbean;

import org.eapp.oa.system.hbean.Attachment;

/**
 * @author zsy
 * @version Jul 22, 2009
 */
public class MeetingAttachmentId implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2566953167326265930L;
	private Attachment attachment;
	private MeetingInfo meetingInfo;
	
	public MeetingAttachmentId() {
		
	}
	
	public MeetingAttachmentId(MeetingInfo meetingInfo, Attachment attachment) {
		this.meetingInfo = meetingInfo;
		this.attachment = attachment;
	}
	
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public MeetingInfo getMeetingInfo() {
		return meetingInfo;
	}
	public void setMeetingInfo(MeetingInfo meetingInfo) {
		this.meetingInfo = meetingInfo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachment == null) ? 0 : attachment.hashCode());
		result = prime * result
				+ ((meetingInfo == null) ? 0 : meetingInfo.hashCode());
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
		final MeetingAttachmentId other = (MeetingAttachmentId) obj;
		if (attachment == null) {
			if (other.attachment != null)
				return false;
		} else if (!attachment.equals(other.attachment))
			return false;
		if (meetingInfo == null) {
			if (other.meetingInfo != null)
				return false;
		} else if (!meetingInfo.equals(other.meetingInfo))
			return false;
		return true;
	}
}
