/**
 * 
 */
package org.eapp.pos.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;


/**
 * Aciont基类
 * @author zsy
 *
 */
public abstract class BaseAction implements java.io.Serializable {
	
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6381993740290237964L;
	/**
	 * 成功
	 */
	protected static final String SUCCESS = "success";
	/**
	 * 失败
	 */
	protected static final String ERROR = "error";
	/**
	 * 返回的信息
	 */
	protected Message msg;
	
	/**
	 * 返回信息
	 * @return 信息
	 */
	public Message getMsg() {
		return msg;
	}

	/**
	 * 获取HttpServletResponse
	 * @return HttpServletResponse
	 */
	@JSON(serialize = false)
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	/**
	 * 获取HttpServletRequest
	 * @return HttpServletRequest
	 */
	@JSON(serialize = false)
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	/**
	 * 获取HttpSession
	 * @return HttpSession
	 */
	@JSON(serialize = false)
	public HttpSession getSession() {
		return getRequest().getSession();
	}
	
	/**
	 * 成功信息
	 * @param text 消息内容
	 * @return 成功
	 */
	protected String success(String text) {
		msg = new Message(text, 1);
		return SUCCESS;
	}
	
	/**
	 * 操作成功
	 * @return 成功
	 */
	protected String success() {
		return success("操作成功");
	}
	
	/**
	 * 失败信息
	 * @param text 消息内容
	 * @return 失败
	 */
	protected String error(String text) {
		msg = new Message(text, 0);
		return ERROR;
	}
	
	/**
	 * 操作失败
	 * @return 失败
	 */
	protected String error() {
		return error("操作失败");
	}
	
	/**
	 * 要申明为public才可以
	 * 输出的信息
	 * @author zsy
	 */
	public static class Message {
		/**
		 * 消息内容
		 */
		private String text;
		/**
		 * 消息码
		 */
		private int code;
		/**
		 * 构造
		 * @param text 消息内容
		 * @param code 消息码
		 */
		public Message(String text, int code) {
			this.text = text;
			this.code = code;
		}
		/**
		 * 构造
		 * @param text 消息内容
		 */
		public Message(String text) {
			this.text = text;
			this.code = 0;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
		
	}
}
