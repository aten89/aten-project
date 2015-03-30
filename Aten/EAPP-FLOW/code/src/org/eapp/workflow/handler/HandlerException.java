package org.eapp.workflow.handler;

import org.eapp.workflow.WfmException;

/**
 * @author 卓诗垚
 * @version 2.0
 * Nov 3, 2009
 */
public class HandlerException extends WfmException {

	private static final long serialVersionUID = 1L;

	public HandlerException() {
		super();
	}

	public HandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public HandlerException(String message) {
		super(message);
	}

	public HandlerException(Throwable cause) {
		super(cause);
	}
}
