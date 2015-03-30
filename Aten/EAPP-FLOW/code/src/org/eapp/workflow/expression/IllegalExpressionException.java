package org.eapp.workflow.expression;

import org.eapp.workflow.WfmException;

/**
 * @author 卓诗垚
 * @version 2.0
 * Nov 3, 2009
 */
public class IllegalExpressionException extends WfmException {

	private static final long serialVersionUID = 1L;

	public IllegalExpressionException() {
		super();
	}

	public IllegalExpressionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalExpressionException(String message) {
		super(message);
	}

	public IllegalExpressionException(Throwable cause) {
		super(cause);
	}
}
