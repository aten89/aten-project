/**
 * 
 */
package org.eapp.oa.lucene;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 索引队列，就是对索引任务（IndexTask）的管理
 * @author zsy
 * @version Jun 12, 2009
 */
public abstract class IndexTaskQueue {
	private static final Log log = LogFactory.getLog(IndexTaskQueue.class);
    /**
     * 返回索引指令队列
     * @return 
     */
    public abstract Set<IndexTask> getQueue();
    
    /**
     * 清空索引指令队列
     * @param indexTask 也处理过的索引任务
     */
    public abstract void clearQueue(Set<IndexTask> indexTask);
    
    /**
     * 判断索引任务队列是否已满
     * @return
     */
    public abstract boolean isFull();
    
    /**
     * 新增索引任务
     * @param it
     */
    protected abstract void addIndexTask(IndexTask it);
    
    /**
     * 修改索引任务
     * @param it
     */
    protected abstract void modifyIndexTask(IndexTask it);
    
    /**
     * 删除索引任务
     * @param it
     */
    protected abstract void deleteIndexTask(IndexTask it);
    
    /**
     * 查找索引任务
     * @param documentId
     * @param documentType
     * @return
     */
    protected abstract IndexTask getIndexTask(String documentId, String documentType);
    

    /**
     * 根据状态机矩阵安排索引队列
     * @param documentId 文档ID
     * @param documentType 文档类型
     * @param action 指令
     */
    public final void arrangeQueue(String documentId, String documentType, String action){
    	if (documentId == null || documentType == null || action == null) {
    		throw new IllegalArgumentException();
    	}
    	IndexTask it = getIndexTask(documentId, documentType);
    	if (it == null) {
    		it = new IndexTask(documentId, documentType, action);
    		addIndexTask(it);
    		return;
    	}
    	
    	boolean bHasAdd = false;
    	boolean bHasModify = false;
    	boolean bHasDelete = false;
    	if(IndexTask.ACTION_ADD.equals(it.getAction())) {
    		bHasAdd = true;
    	} else if(IndexTask.ACTION_MODIFY.equals(it.getAction())) {
        	bHasModify = true;
     	} else if(IndexTask.ACTION_DELETE.equals(it.getAction())) {
        	bHasDelete = true;
      	}
        //状态机矩阵请参见下列文字
        /*
         			输入的状态
               				ADD		MOD		DEL
                	————————————————————————
               		ADD	|	ADD		ADD		清空
         队列中的状态	    MOD	|	非法		MOD		DEL
               		DEL	|	MOD		MOD		DEL
               	
               	原DEL -> MOD 的状态应该是 “MOD” 而不是 “非法”
        */
    	if (IndexTask.ACTION_ADD.equals(action)) {
    		if (bHasAdd) {
    			
    		} else if (bHasModify) {
    			log.warn("IndexTaskQueue: 非法状态：修改后新增同一文档: " + documentId);
	    	} else if(bHasDelete) {
                it.setAction(IndexTask.ACTION_MODIFY);
                modifyIndexTask(it);
	    	}
	    } else if (IndexTask.ACTION_MODIFY.equals(action)) {
	    	if (bHasAdd) {
	    		//不变
	    	} else if (bHasModify) {
	    		//不变
	    	} else if (bHasDelete) {
                it.setAction(IndexTask.ACTION_MODIFY);
                modifyIndexTask(it);
	    	}
	    } else if (IndexTask.ACTION_DELETE.equals(action)) {
	    	if (bHasAdd) {
	    		deleteIndexTask(it);
            } else if (bHasModify) {
                it.setAction(IndexTask.ACTION_DELETE);
                modifyIndexTask(it);
            } else if (bHasDelete) {
            	//不变
            }
	    } else {
			log.warn("IndexTaskQueue: 非法操作：" + action);
	    }
    }
}
