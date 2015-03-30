/**
 * 
 */
package org.eapp.oa.lucene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 索引任务队列的定时处理器
 * @author zsy
 * @version Jun 12, 2009
 */
public class IndexTaskRunner {
	private static final Log log = LogFactory.getLog(IndexTaskRunner.class);
	
	private static IndexTaskRunner singleton = new IndexTaskRunner();

	private IndexTaskQueue queue;//任务队列的查询器
	private boolean pause = false;//是否暂停
	private boolean running = false;//是否运行中
	
	
    
    private Map<String, BuilderFactor> indexBuilders;//存放索引目录的处理器
    
    private Timer timer;//定时器
    
	private IndexTaskRunner() {
		indexBuilders = new HashMap<String, BuilderFactor>();
		timer = new Timer();
	}
	
	/**
	 * 启动定时器
	 * @param queue
	 * @param indexBuilders
	 */
	public static void start(IndexTaskQueue queue, IndexBuilder[] indexBuilders) {
		//默认执行周期为半小时
		start(queue, indexBuilders, ConfigFactory.getLuceneConfig().getPeriod());
	}
	
	/**
	 * 启动定时器
	 * 必须先调用start方法后才能正常执行
	 * @param queue
	 * @param indexBuilders
	 * @param period
	 */
	public static void start(IndexTaskQueue queue, IndexBuilder[] indexBuilders, long period) {
		singleton.queue = queue;
		for (IndexBuilder indexBuilder : indexBuilders)  {
			singleton.indexBuilders.put(indexBuilder.getDocumentType(), 
					singleton.new BuilderFactor(indexBuilder));
		}
		//启动定时器
		singleton.timer.schedule(singleton.new MyTimerTask(singleton), period, period);
	}
	
	/**
	 * 添加到新增队列
	 * 必须先调用start方法后才能正常执行
	 * @param documentId
	 * @param documentType 值来源于IndexBuilder中的类型，否则会出错
	 */
	public static void addIndex(String documentId, String documentType) {
		singleton.sendCommand(documentId, documentType, IndexTask.ACTION_ADD);
	}
	
	/**
	 * 添加到修改队列
	 * 必须先调用start方法后才能正常执行
	 * @param documentId
	 * @param documentType 值来源于IndexBuilder中的类型，否则会出错
	 */
	public static void modifyIndex(String documentId, String documentType) {
		singleton.sendCommand(documentId, documentType, IndexTask.ACTION_MODIFY);
	}
	
	/**
	 * 添加到删除队列
	 * 必须先调用start方法后才能正常执行
	 * @param documentId
	 * @param documentType 值来源于IndexBuilder中的类型，否则会出错
	 */
	public static void deleteIndex(String documentId, String documentType) {
		singleton.sendCommand(documentId, documentType, IndexTask.ACTION_DELETE);
	}
	
	/**
	 * 发布操作到队列中，如果队列已满立即处理
	 * @param documentId
	 * @param documentType
	 * @param action
	 */
	private void sendCommand(String documentId, String documentType, String action) {
		if (singleton.queue == null) {
			throw new IllegalStateException("Runner还未启动");
		}
		queue.arrangeQueue(documentId, documentType, action);
		if (queue.isFull()) {//如果队列已满，马上处理
			run();
		}
	}
	
	/**
	 * 通过文档类型取得IndexBuilder
	 * @param documentType
	 * @return
	 */
	public static IndexBuilder getIndexBuilder(String documentType) {
		if (singleton.indexBuilders == null) {
			throw new IllegalStateException("Runner还未启动");
		}
		BuilderFactor bf = singleton.indexBuilders.get(documentType);
		if (bf == null) {
			return null;
		}
		return bf.indexBuilder;
	}
	
	/**
	 * 当前是否为暂停状态
	 * @return
	 */
	public static boolean isPause() {
		return singleton.pause;
	}

	/**
	 * 设置暂停
	 * 暂停后下次定时器到点时不执行处理，并不是马上停止当前处理线程
	 * @param pause
	 */
	public static void setPause(boolean pause) {
		singleton.pause = pause;
	}
	
	/**
	 * 手动触发执行
	 */
	public static void run() {
		new Timer().schedule(singleton.new MyTimerTask(singleton), 0);
	}
	
	/**
	 * 手动触发优化索引
	 */
	public static void optimizeIndex() {
		for (BuilderFactor ibf : singleton.indexBuilders.values()) {
       		ibf.indexBuilder.optimizeIndex();
       	}
	}
	
    /**
     * 处理过程
     */
    public void doRun() {
        synchronized (this) {
            // 是否处于索引任务暂停状态
            if (pause) {
                log.warn("索引更新线程处于暂停状态");
                return;
            }
            if (running) {
                log.warn("索引更新线程已经处于运行状态");
                return;
            }
            running = true;// 设置运行状态true
            log.info(" Index process begin...");
            try {
                // 获取索引任务队列
                Set<IndexTask> indexTasks = queue.getQueue();

                // 将索引任务分解成 "删除" "新增" 两个任务缓存，添加到各自的队表里
                for (IndexTask iTask : indexTasks) {
                    if (iTask == null) {
                        continue;
                    }
                    BuilderFactor bf = indexBuilders.get(iTask.getDocumentType());
                    if (IndexTask.ACTION_ADD.equals(iTask.getAction())) {
                        bf.toAdds.add(iTask);
                    } else if (IndexTask.ACTION_DELETE.equals(iTask.getAction())) {
                        bf.toDeletes.add(iTask);
                    } else if (IndexTask.ACTION_MODIFY.equals(iTask.getAction())) {
                        // 修改操作要先删除再新增
                        bf.toDeletes.add(iTask);
                        bf.toAdds.add(iTask);
                    } else {
                        log.warn("非法操作：" + iTask.getAction());
                    }
                }

                // 批量处理分解完，各自队列里的任务
                for (BuilderFactor ibf : singleton.indexBuilders.values()) {
                    ibf.build();
                }

                // 在索引建立成功后,清空索引任务队列
                queue.clearQueue(indexTasks);
            } catch (Exception e) {
                log.error("索引更新线程，处理失败");
                e.printStackTrace();
            }
            // 设置运行状态为false
            running = false;
            // 输出索引进程结束时间
            log.info("Index process completed.");
        }
    }
	
	/**
	 * 
	 * @author zsy
	 * @version Jun 15, 2009
	 */
	private class BuilderFactor {
		IndexBuilder indexBuilder;
		
		Set<IndexTask> toDeletes;//保存待删除的索引任务
	    Set<IndexTask> toAdds;//保存待新增的索引任务
	    
	    public BuilderFactor(IndexBuilder indexBuilder) {
	    	this.indexBuilder = indexBuilder;
	    	toDeletes = new HashSet<IndexTask>();
			toAdds = new HashSet<IndexTask>();
	    }
	    
	    /**
	     * 处理新增,删除队列
	     * 对任务缓存进行操作一定要先删除索引，再进行增加
	     */
	    public void build() throws IndexException {
	    	indexBuilder.deleteIndexs(toDeletes);
	    	indexBuilder.buildIndexs(toAdds);
	    	
	    	//判断是否进行索引优化
	    	indexBuilder.testOptimizeIndex();
	    	clear();
	    }
	    
	    /**
	     * 清空任务缓存列表
	     */
	    public void clear() {
	       	toAdds.clear();
	       	toDeletes.clear();
	    }
	}
	
	/**
	 * TimerTask
	 * @author zsy
	 * @version Jun 12, 2009
	 */
	private class MyTimerTask extends TimerTask{
		private IndexTaskRunner runner;
		
		private MyTimerTask(IndexTaskRunner runner) {
			this.runner = runner;
		}
		@Override
		public void run() {
			this.runner.doRun();
		}
		
	}
}
