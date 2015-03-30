package org.eapp.rpc.session;


/**
 * @author linliangyi 2008-06-20
 * @version 1.0
 */
public interface RPCSession {
	
	/**
	 * 返回Session ID
	 * @return 
	 */
	public String getID();
	
	/**
	 * 根据属性名称，从session中获取属性对象
	 * @param attributeName 属性名称
	 * @return
	 */
	public Object getAttribute(String attributeName);
	
	/**
	 * 从Session中移除指定的属性
	 * @param attributeName
	 */
	public void removeAttribute(String attributeName);
	
	/**
	 * 设置属性到session中
	 * @param attributeName 属性名称
	 * @param attribute 属性对象
	 */
	public void setAttribute(String attributeName , Object attribute);
	

	/**
	 * 返回Session构建的时间，
	 * @return 时间的毫秒
	 */
	public long getCreationTime();
	
	/**
	 * 返回session最后一次访问的时间
	 * @return
	 */
	public long getLastAccessedTime();
	
	/**
	 * 返回session的超时时长设置，以分钟为单位
	 * @return
	 */
	public int getMaxInactiveInterval();
	
	/**
	 * 设置session的超时时长，以分钟为单位
	 * @param minute
	 */
	public void setMaxInactiveInterval(int minute);
	
	/**
	 * 返回当前session的上级容器
	 * @return
	 */
	public RPCSessionContainer getSessionContainer();
	
	/**
	 * 销毁当前的Session实例，并移除其中的所有对象 
	 */
	public void invalidate();
	
	/**
	 * 判断这个当前session是否过期，已经失效
	 */
	public boolean isInvalid();
	
	/**
	 * 更新session的最后一次访问时间
	 */
	public void refreshLastAccessedTime();
	
}
