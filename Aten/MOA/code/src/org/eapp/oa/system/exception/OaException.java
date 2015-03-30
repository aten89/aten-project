package org.eapp.oa.system.exception;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: 项目级业务异常</p>
 * @version 1.0 
 */
public class OaException extends Exception implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public OaException() {
		super();
	}

	public OaException(String message) {
		super(message);
	}
	
	public OaException(Exception e) {
		super(e);
	}
	
}
