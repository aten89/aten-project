package org.eapp.oa.hr.hbean;

// default package

/**
 */

public class HRFlowConf implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5220865752240853001L;
	private String id;
	private String groupName;
//	private String flowClass;
	private String holidayFlowKey;
	private String canHolidayFlowKey;
	private String entryFlowKey;
	private String resignFlowKey;
	private String description;
	private String transferFlowKey;
	private String positiveFlowKey;
	
	private transient String holidayFlowName;
	private transient String canHolidayFlowName;
	private transient String entryFlowName;
	private transient String resignFlowName;
	private transient String transferFlowName;
	private transient String positiveFlowName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getHolidayFlowKey() {
		return holidayFlowKey;
	}
	public void setHolidayFlowKey(String holidayFlowKey) {
		this.holidayFlowKey = holidayFlowKey;
	}
	public String getCanHolidayFlowKey() {
		return canHolidayFlowKey;
	}
	public void setCanHolidayFlowKey(String canHolidayFlowKey) {
		this.canHolidayFlowKey = canHolidayFlowKey;
	}
	public String getEntryFlowKey() {
		return entryFlowKey;
	}
	public void setEntryFlowKey(String entryFlowKey) {
		this.entryFlowKey = entryFlowKey;
	}
	public String getResignFlowKey() {
		return resignFlowKey;
	}
	public void setResignFlowKey(String resignFlowKey) {
		this.resignFlowKey = resignFlowKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTransferFlowKey() {
		return transferFlowKey;
	}
	public void setTransferFlowKey(String transferFlowKey) {
		this.transferFlowKey = transferFlowKey;
	}
	public String getPositiveFlowKey() {
		return positiveFlowKey;
	}
	public void setPositiveFlowKey(String positiveFlowKey) {
		this.positiveFlowKey = positiveFlowKey;
	}
	public String getHolidayFlowName() {
		return holidayFlowName;
	}
	public void setHolidayFlowName(String holidayFlowName) {
		this.holidayFlowName = holidayFlowName;
	}
	public String getCanHolidayFlowName() {
		return canHolidayFlowName;
	}
	public void setCanHolidayFlowName(String canHolidayFlowName) {
		this.canHolidayFlowName = canHolidayFlowName;
	}
	public String getEntryFlowName() {
		return entryFlowName;
	}
	public void setEntryFlowName(String entryFlowName) {
		this.entryFlowName = entryFlowName;
	}
	public String getResignFlowName() {
		return resignFlowName;
	}
	public void setResignFlowName(String resignFlowName) {
		this.resignFlowName = resignFlowName;
	}
	public String getTransferFlowName() {
		return transferFlowName;
	}
	public void setTransferFlowName(String transferFlowName) {
		this.transferFlowName = transferFlowName;
	}
	public String getPositiveFlowName() {
		return positiveFlowName;
	}
	public void setPositiveFlowName(String positiveFlowName) {
		this.positiveFlowName = positiveFlowName;
	}
	
}