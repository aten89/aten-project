/**
 * 
 */
package org.eapp.workflow.asyn.exec;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * 异步任务执行器
 * 单子模式
 * @author 林良益
 * 2008-10-27
 * @version 2.0
 */
public class AsynJobRunner {
	
	private static AsynJobRunner single;
	
	//执行器并发的线程数
	int numberOfThreads = 1;
	//异步线程的空闲间隔（无任务时sleep的时间）
	int idleInterval = 60000;	
	//锁监控线程的间隔时间
	int lockMonitorInterval = 60000;	
	//锁时间上限
	int maxLockTime = 600000;
	//锁缓冲时间（考虑多线程执行的lock延时）
	int lockBufferTime = 5000;
	
	//线程池（）
	Map<String , Thread> threads = new HashMap<String , Thread>();

	//受监控的任务ID集合
	Map<String , String> monitoredJobIds = Collections.synchronizedMap(new HashMap<String , String>());
	
	//执行器的开始状态
	boolean isStarted = false;
	
	private AsynJobRunner(){
	}
	
	/**
	 * 单子实例化
	 * @return
	 */
	public static AsynJobRunner getInstance(){
		synchronized(AsynJobRunner.class){
			if(single == null){
				single = new AsynJobRunner();
			}
		}
		return single;
	}
	
	/**
	 * 初始化对象
	 * @return
	 */
	public void init(Properties props){
		if (props == null || props.isEmpty()) {
			return;
		}
		String field = props.getProperty("AsynJobRunner.numberOfThreads");
		if (field != null) {
			single.setNumberOfThreads(Integer.parseInt(field));
		}
		field = props.getProperty("AsynJobRunner.idleInterval");
		if (field != null) {
			single.setIdleInterval(Integer.parseInt(field));
		}
		field = props.getProperty("AsynJobRunner.maxLockTime");
		if (field != null) {
			single.setMaxLockTime(Integer.parseInt(field));
		}
		field = props.getProperty("AsynJobRunner.lockMonitorInterval");
		if (field != null) {
			single.setLockMonitorInterval(Integer.parseInt(field));
		}
		field = props.getProperty("AsynJobRunner.lockBufferTime");
		if (field != null) {
			single.setLockBufferTime(Integer.parseInt(field));
		}
		
		field = props.getProperty("AsynJobRunner.start");
		if ("true".equals(field) || "Y".equals(field)) {//是否启动
			single.start();
		}
	}
	
	/**
	 * 启动异步执行器
	 * 同步方法
	 */
	public synchronized void start() {
		if (! isStarted) {
			for (int i=0; i< numberOfThreads; i++) {
				startThread();
			}
			isStarted = true;
		}
	}	
	
	/**
	 * 构建并开始单个处理线程
	 * 同步方法
	 */
	protected synchronized void startThread() {
		String threadName = getNextThreadName();
		Thread thread = new AsynExecuteThread(threadName, this, idleInterval);
		threads.put(threadName, thread);
		thread.start();
	}	
	
	/**
	 * 停止异步执行器内的所有线程，并等待线程完成
	 * 该方法将等待所有线程真实结束后，返回
	 * @throws InterruptedException
	 */
	public void stopAndJoin() throws InterruptedException {
		Iterator<Thread> iter = stop().iterator();
		while (iter.hasNext()) {
			Thread thread = (Thread) iter.next();
			thread.join();
		}
	}
	
	/**
	 * 停止异步执行器内的所有线程，并返回线程组
	 * 同步方法
	 * 将所有的线程都设置为结束状态，但不意味着这些线程就立即结束
	 * @return
	 */
	public synchronized List<Thread> stop() {
		List<Thread> stoppedThreads = new ArrayList<Thread>(threads.size());
		if (isStarted) {
			for (int i=0; i < numberOfThreads; i++) {
				stoppedThreads.add(stopThread());
			}
			isStarted = false;
		}
		return stoppedThreads;
	}
	
	/**
	 * 结束运行器中的最后一个线程
	 * 从线程池的末端取最后一个线程，并将其状态设置为结束
	 * @return
	 */
	protected synchronized Thread stopThread() {
		String threadName = getLastThreadName();
		AsynExecuteThread thread = (AsynExecuteThread)threads.remove(threadName);
		thread.setActive(false);
		thread.interrupt();
		return thread;
	}
	
	protected String getNextThreadName() {
		return getThreadName(threads.size()+1);
	}
	
	protected String getLastThreadName() {
		return getThreadName(threads.size());
	}
		  
	private String getThreadName(int index) {
		return "Asynchronous-Job-Runner:" + getHostName() + ":" + index;
	}
	
	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "unknown";
		}
	}

	/**
	 * 取得受监管的异步任务ID集合
	 * @return
	 */
	public Set<String> getMonitoredJobIds() {
		return new HashSet<String>(monitoredJobIds.values());
	}
	
	/**
	 * 将异步任务ID加入线程监管
	 * 每个异步任务对应一个监管线程
	 * 受监控的任务通常是还未到期的任务
	 * @param threadName
	 * @param jobId
	 */	  
	public void addMonitoredJobId(String threadName, String jobId) {
		monitoredJobIds.put(threadName, jobId);
	}
	
	/**
	 * 移除一个线程对一个异步任务的监管
	 * @param threadName
	 */
	public void removeMonitoredJobId(String threadName) {
		monitoredJobIds.remove(threadName);
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public int getIdleInterval() {
		return idleInterval;
	}

	public void setIdleInterval(int idleInterval) {
		this.idleInterval = idleInterval;
	}

	public int getLockMonitorInterval() {
		return lockMonitorInterval;
	}

	public void setLockMonitorInterval(int lockMonitorInterval) {
		this.lockMonitorInterval = lockMonitorInterval;
	}

	public int getMaxLockTime() {
		return maxLockTime;
	}

	public void setMaxLockTime(int maxLockTime) {
		this.maxLockTime = maxLockTime;
	}

	public int getLockBufferTime() {
		return lockBufferTime;
	}

	public void setLockBufferTime(int lockBufferTime) {
		this.lockBufferTime = lockBufferTime;
	}

	public boolean isStarted() {
		return isStarted;
	}
	
	
}
