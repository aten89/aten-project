/**
 * 
 */
package org.eapp.oa.lucene;

/**
 * 对索引的操作异常
 * @author zsy
 * @version Jun 12, 2009
 */
public class IndexException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -375017740649538119L;

	public IndexException() {
		super();
	}

	public IndexException(String message) {
		super(message);
	}
	
	public IndexException(Exception e) {
		super(e);
	}
}
