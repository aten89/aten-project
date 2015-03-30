package org.eapp.workflow;

/**
 * @author 卓诗垚
 * @version 1.0
 * Nov 6, 2008
 */
public class WfmException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WfmException() {
		super();
	}

	public WfmException(String message, Throwable cause) {
		super(message, cause);
	}

	public WfmException(String message) {
		super(message);
	}

	public WfmException(Throwable cause) {
		super(cause);
	}
}
