package org.eapp.rpc.session;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Linliangyi 2008-6-20
 * @version 1.0
 */
public final class RPCSessionContainer {
	/*
	 * 容器的单子实例
	 */
	private static RPCSessionContainer single;	
	/*
	 * rpc session 默认超时失效时长
	 */
	private static int maxInactiveInterval = 30 * 60;//失效时间间隔,单位秒 默认30分钟;
	/*
	 * RPC回话容器，这个容器的实现必须是线程同步的
	 */
	private Map<String , RPCSession> container;
	/*
	 * 容器清理，过期session销毁，任务计时器
	 */
	private Timer sessionRecycleTimer;
	
	/**
	 * 获取单子实例
	 * @return
	 */
	public static RPCSessionContainer singleton(){
		if (single == null) {
			single = new RPCSessionContainer();
		}
		return single;
	}
	
	/**
	 * RPCSessionContainer 单子构造函数
	 * @param maxInactiveInterval
	 */
	private RPCSessionContainer(){
		this.container = new Hashtable<String , RPCSession>();
		this.sessionRecycleTimer = new Timer(true);
		this.sessionRecycleTimer.schedule(new SessionRecycleTask(this), maxInactiveInterval * 1000, maxInactiveInterval * 1000);
	}
	
	/**
	 * 移除容器中指定的session
	 * @param sessionID
	 */	
	void removeRPCSession(String sessionID){
		this.container.remove(sessionID);
	}
	
	/**
	 * 在容器中新建一个RPCSession
	 * @return
	 */
	public RPCSession createSession(){
		RPCSession rpcSession = new RPCSessionImp(single);
		container.put(rpcSession.getID(), rpcSession);
		return rpcSession;
	}
	
	/**
	 * 清理容器中过期的session
	 */
	private void cruise(){
		try{
			if(container == null || container.size() == 0) {//确保容器非空
				return;
			}
			/*
			 * 遍历容器，寻找所有失效的session，放入回收站，待销毁
			 */
			for(String key : new HashSet<String>(container.keySet())){
				RPCSession rpcSession = container.get(key);
				/*
				 * 判断取出的session非空，且过期
				 * 有可能在遍历的时候，session被getSession方法销毁
				 */
				if(rpcSession != null && rpcSession.isInvalid()){
					rpcSession.invalidate();
				}
			}
		} catch(Exception anyError) {
			/*
			 * 由于改方法是被守护线程调用的，为保证线程的不因为异常中断，
			 * 这里捕获并销毁所有异常
			 */
			anyError.printStackTrace();
		}
	}
	
	/**
	 * 从容器中获取一个指定的session
	 * 如果sessionID对应的session不存在，返回null
	 * 如果sessionID对应的session过期，返回null
	 * @param sessionID
	 * @return
	 */
	public RPCSession getSession(String sessionID){
		if(sessionID == null){
			return null;
		}else{
			RPCSession rpcSession = single.container.get(sessionID);
			if (rpcSession == null) {//session为空
				return null;				
			} else if(rpcSession.isInvalid()) {//sessoin过期
				//sessoin销毁
				rpcSession.invalidate();
				return null;
			} else {//session 可用
				//更新session访问时间
				rpcSession.refreshLastAccessedTime();
				return rpcSession;
			}
		}
	}

	/**
	 * 取得默认超时失效时长
	 * @return
	 */
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	/**
	 * 设置默认超时失效时长
	 * @param maxInactiveInterval
	 */
	public static void setMaxInactiveInterval(int maxInactiveInterval) {
		RPCSessionContainer.maxInactiveInterval = maxInactiveInterval;
	}
	
	/**
	 * 
	 * @author linliangyi 2008-06-21
	 * @version
	 */
	class SessionRecycleTask extends TimerTask{
		private RPCSessionContainer container;
		/**
		 * 采用私有的构造函数，保证该定时任务类只被RPCSessionContainer使用
		 * @param container
		 */
		private SessionRecycleTask(RPCSessionContainer container){
			this.container = container;
		}
		@Override
		public void run() {
			/*
			 * 调用容器的清理方法，
			 * 给方法不会抛出异常造成线程瘫痪
			 */
			this.container.cruise();
		}
		
	}
}
