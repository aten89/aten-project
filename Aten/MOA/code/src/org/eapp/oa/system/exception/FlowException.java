package org.eapp.oa.system.exception;

import java.io.Serializable;

/**
 * @author zsy
 * @version 1.0 
 */
public class FlowException extends OaException implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public FlowException() {
		super();
	}

	public FlowException(String message) {
		super(message);
	}
	
	public FlowException(Exception e) {
		super(e);
	}
	
}
