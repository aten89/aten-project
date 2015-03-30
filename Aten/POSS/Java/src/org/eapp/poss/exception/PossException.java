package org.eapp.poss.exception;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: 项目级业务异常</p>
 * @version 1.0 
 */
public class PossException extends Exception implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public PossException() {
		super();
	}

	public PossException(String message) {
		super(message);
	}
	
	public PossException(Exception e) {
		super(e);
	}
	
}
