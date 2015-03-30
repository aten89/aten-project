/**
 * 
 */
package org.eapp.oa.lucene.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eapp.oa.kb.blo.IIndexTaskBiz;
import org.eapp.oa.lucene.IndexTask;
import org.eapp.oa.lucene.IndexTaskQueue;
import org.eapp.util.spring.SpringHelper;


/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class DBIndexTaskQueue extends IndexTaskQueue {

	private IIndexTaskBiz indexTaskBiz;
	
	private int maxSize;
	
	public DBIndexTaskQueue(int maxSize) {
		indexTaskBiz = (IIndexTaskBiz)SpringHelper.getSpringContext().getBean(
				"indexTaskBiz");
		this.maxSize = maxSize;
	}
	public DBIndexTaskQueue() {
		this(512);
	}
	
	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public Set<IndexTask> getQueue() {
		List<IndexTask> its = indexTaskBiz.getIndexTask(maxSize);
		if (its == null) {
			return null;
		}
		return new HashSet<IndexTask>(its);
	}
	
	@Override
	public void clearQueue(Set<IndexTask> indexTasks) {
		indexTaskBiz.txDelIndexTasks(indexTasks);

	}
	
	@Override
	public boolean isFull() {
		/**
		 * 当队列满时就算时间没到也会马上执行处理，
		 * 每次往队列添加索引任务时都去判断一次，
		 * 永远返回false是即取消队列满时自动执行处理，只有等定时器时间到时才处理
		 * 
		 */
		return false;
//		long size = indexTaskBiz.countIndexTask();
//		return size >= maxSize;
	}
	
	@Override
	protected void addIndexTask(IndexTask it) {
		indexTaskBiz.txAddIndexTask(it);
	}
	
	@Override
	protected void modifyIndexTask(IndexTask it) {
		indexTaskBiz.txModifyIndexTask(it);
	}

	@Override
	protected void deleteIndexTask(IndexTask it) {
		List<IndexTask> list = new ArrayList<IndexTask>();
		list.add(it);
		indexTaskBiz.txDelIndexTasks(list);

	}

	@Override
	protected IndexTask getIndexTask(String documentId, String documentType) {
		if (documentId == null || documentType == null) {
			throw new IllegalArgumentException();
		}
		return indexTaskBiz.getIndexTask(documentId, documentType);
	}
}
