/**
 * 
 */
package org.eapp.oa.kb.blo;

import java.util.Collection;
import java.util.List;

import org.eapp.oa.lucene.IndexTask;


/**
 * @author zsy
 * @version Jun 12, 2009
 */
public interface IIndexTaskBiz {
	/**
	 * 查询给定数量的索引任务
	 * @param count
	 * @return
	 */
	List<IndexTask> getIndexTask(int count);
	
	/**
	 * 统计索引任务数量
	 * @return
	 */
	long countIndexTask();
	
	/**
	 * 取得索引任务
	 * @param documentId
	 * @param documentType
	 * @return
	 */
	IndexTask getIndexTask(String documentId, String documentType);
	
	/**
	 * 删除索引任务
	 * @param its
	 */
	void txDelIndexTasks(Collection<IndexTask> its);
	
	/**
	 * 新增索引任务
	 * @param it
	 */
	void txAddIndexTask(IndexTask it);
	
	/**
	 * 修改索引任务
	 * @param it
	 */
	void txModifyIndexTask(IndexTask it);
}
