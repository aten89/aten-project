package org.eapp.workflow.def.parse;

import org.eapp.workflow.WfmException;

/**
 * XML解析错误
 * @author 卓诗垚
 * @version 1.0
 */
public class ParserException extends WfmException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -666002356661486275L;
	public ParserException() {
	    super();
	  }
	  public ParserException(String message, Throwable cause) {
	    super(message, cause);
	  }
	  public ParserException(String message) {
	    super(message);
	  }
	  public ParserException(Throwable cause) {
	    super(cause);
	  }
}
