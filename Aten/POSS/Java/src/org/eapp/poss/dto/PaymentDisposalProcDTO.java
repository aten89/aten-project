package org.eapp.poss.dto;

import java.util.Date;


/**
 * 工单处理过程 显示
 * @author 
 *
 */
public class PaymentDisposalProcDTO implements java.io.Serializable, Comparable<PaymentDisposalProcDTO> {
    
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 502887540493264029L;
	/**
	 * 流程任务
	 */
	public static final int OBJECT_TASK = 0;
	/**
	 * 表单备注
	 */
	public static final int OBJECT_FORM_REMARK = 1;

	private Date createTime;
	private int type;
	private Object object;
	private String curActor;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	public String getCurActor() {
		return curActor;
	}
	public void setCurActor(String curActor) {
		this.curActor = curActor;
	}
	@Override
	public int compareTo(PaymentDisposalProcDTO o) {
		long thisTime = this.createTime.getTime();
		long anotherTime = o.getCreateTime().getTime();
		return (thisTime < anotherTime ? 1 : (thisTime == anotherTime ? 0 : -1));
	}
	
	
}