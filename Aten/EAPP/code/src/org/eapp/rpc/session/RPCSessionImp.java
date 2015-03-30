/**
 * 
 */
package org.eapp.rpc.session;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * @author linliangyi 2008-06-21
 * @version 1.0
 */
public class RPCSessionImp implements RPCSession {
	/*
	 * session id
	 */
	private String id;
	/*
	 * session上级容器
	 */
	private RPCSessionContainer containter;
	/*
	 * session 生成的时间
	 */
	private long creationTime;
	/*
	 * 最后一期访问session的时间
	 */
	private long lastAccessedTime;
	/*
	 * session超时时长,单位分钟
	 */
	private int maxInactiveInterval;
	/*
	 * session 属性的存储Map
	 */
	private Map<String , Object> sessionMap;

	
	RPCSessionImp(RPCSessionContainer containter) {
		this.id = UUID.randomUUID().toString();
		this.containter = containter;
		this.maxInactiveInterval = containter.getMaxInactiveInterval();
		this.creationTime = System.currentTimeMillis();
		this.lastAccessedTime = creationTime;
		this.sessionMap = new Hashtable<String , Object>();
	}
	
	@Override
	public String getID() {
		return this.id;
	}
	
	@Override
	public synchronized Object getAttribute(String attributeName) {
		if(isInvalid()) {
			return null;
		}
		return this.sessionMap.get(attributeName);
	}
	
	@Override
	public synchronized void removeAttribute(String attributeName) {
		if(isInvalid()) {
			return;
		}
		this.sessionMap.remove(attributeName);
	}
	
	@Override
	public synchronized void setAttribute(String attributeName, Object attribute) {
		if(isInvalid()){
			throw new IllegalStateException("Session was outdate!");
		}
		this.sessionMap.put(attributeName, attribute);

	}
	
	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public long getLastAccessedTime() {		
		return this.lastAccessedTime;
	}
	
	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}
	
	@Override
	public void setMaxInactiveInterval(int minute) {
		if(isInvalid()){
			throw new IllegalStateException("Session was outdate!");
		}
		this.maxInactiveInterval = minute;
	}

	@Override
	public RPCSessionContainer getSessionContainer() {
		return this.containter;
	}
	
	/* 
	 * 改实现必须是同步的
	 */
	@Override
	public synchronized void invalidate() {
		if(this.containter != null){
			this.containter.removeRPCSession(this.id);
		}
		if(this.sessionMap != null){
			this.sessionMap.clear();
		}
		/*
		 * Debug
		 */
	}	
	
	@Override
	public boolean isInvalid() {
		return (this.lastAccessedTime + this.maxInactiveInterval * 1000) < System.currentTimeMillis();
	}
	
	@Override
	public synchronized void refreshLastAccessedTime(){
		if(isInvalid()){
			throw new IllegalStateException("Session was outdate!");
		}
		this.lastAccessedTime = System.currentTimeMillis();
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(other instanceof RPCSession){
			RPCSession otherSession = (RPCSession)other;
			return id.equals(otherSession.getID());
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
