package org.eapp.crm.exception;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: 项目级业务异常</p>
 * @version 1.0 
 */
public class CrmException extends Exception implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public CrmException() {
		super();
	}

	public CrmException(String message) {
		super(message);
	}
	
	public CrmException(Exception e) {
		super(e);
	}
	
}
