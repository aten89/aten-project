/**
 * 
 */
package org.eapp.exception;

import java.io.Serializable;

/**项目级业务异常
 * @version 
 */
public class EappException extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 675500356223078674L;
	public EappException() {
		super();
	}

	public EappException(String message) {
		super(message);
	}
	
	public EappException(Exception e) {
		super(e);
	}
	
}
