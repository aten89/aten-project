package org.eapp.pos.exception;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: 项目级业务异常</p>
 * @version 1.0 
 */
public class PosException extends Exception implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public PosException() {
		super();
	}

	public PosException(String message) {
		super(message);
	}
	
	public PosException(Exception e) {
		super(e);
	}
	
}
