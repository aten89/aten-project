package org.eapp.hbean;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

/**
 */

public class SysMsg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7019214635457886010L;
	private String msgID;
	private String fromSystemID;
	private String msgSender;
	private String toAccountID;
	private String msgContent;
	private Timestamp sendTime;
	private Boolean viewFlag;
	
	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getFromSystemID() {
		return fromSystemID;
	}

	public void setFromSystemID(String fromSystemID) {
		this.fromSystemID = fromSystemID;
	}

	public String getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(String msgSender) {
		this.msgSender = msgSender;
	}

	public String getToAccountID() {
		return toAccountID;
	}

	public void setToAccountID(String toAccountID) {
		this.toAccountID = toAccountID;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Boolean getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(Boolean viewFlag) {
		this.viewFlag = viewFlag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((msgID == null) ? 0 : msgID.hashCode());
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
		final SysMsg other = (SysMsg) obj;
		if (msgID == null) {
			if (other.msgID != null)
				return false;
		} else if (!msgID.equals(other.msgID))
			return false;
		return true;
	}

}