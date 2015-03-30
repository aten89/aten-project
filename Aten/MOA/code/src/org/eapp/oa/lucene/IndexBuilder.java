/**
 * 
 */
package org.eapp.oa.lucene;

import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * 将业务数据建到索引文件，提供对索引的基本操作，如添加、删除、优化。
 * 每个子类维护一个单独的索引目录
 * 每个子类必须明确索引目录与文档类型的值
 * @author zsy
 * @version Jun 12, 2009
 */
public abstract class IndexBuilder {
	
	public static final String FIELDNAME_DOCUMENT_ID = "docId";//索引的主键，一般是对应数据库主键、根据这个来删除索引
	public static final String FIELDNAME_DOCUMENT_TYPE = "docType";//索引的类型，当多个索引合并查询时可以区别开
	
	private static int needOptimizeSize = 300;//需要优化的队列长度
	private static int currentSize = 0;//已经处理的索引队列长度，用于和needOptimizeSize对比，确定是否启动优化索引进程
	    
	protected String indexDir;//索引目录
	protected String documentType;//文档类型(跟IndexTask中的documentType相对应)
	
	public IndexBuilder(int needOptimizeSize) {
		IndexBuilder.needOptimizeSize = needOptimizeSize;
	}
	
	public IndexBuilder() {
		
	}

	public static int getNeedOptimizeSize() {
		return needOptimizeSize;
	}

	public static void setNeedOptimizeSize(int needOptimizeSize) {
		IndexBuilder.needOptimizeSize = needOptimizeSize;
	}

	public static int getCurrentSize() {
		return currentSize;
	}

	public static void setCurrentSize(int currentSize) {
		IndexBuilder.currentSize = currentSize;
	}

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
	
	public String getIndexDir() {
		return indexDir;
	}
	
	public String getDocumentType() {
		return documentType;
	}
	
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	/**
	 * 索引目录是否存在
	 * @return
	 */
	public boolean indexExists() {
		try {
			return IndexReader.indexExists(FSDirectory.getDirectory(indexDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 优化索引
	 */
	public void optimizeIndex() {
		IndexSession session = null;
		try {
			session = new IndexSession(indexDir);
			session.optimize();
			session.commit();
		} catch (IndexException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		currentSize = 0;
	}
	
	/**
	 * 先检测是否需要优化索引，需要才执行优化
	 */
	public void testOptimizeIndex() {
		if (currentSize >= needOptimizeSize) {
			optimizeIndex();
		}
	}
	
	/**
	 * 添加索引
	 * @param indexTasks
	 */
	public void buildIndexs(Collection<IndexTask> indexTasks) throws IndexException {
		if (indexTasks != null)  {
			currentSize += indexTasks.size();
			doBuildIndexs(indexTasks);
		}
	}
	
	/**
	 * 删除索引
	 * @param indexTasks
	 */
	public void deleteIndexs(Collection<IndexTask> indexTasks) throws IndexException {
		if (indexTasks != null)  {
			currentSize += indexTasks.size();
			doDeleteIndexs(indexTasks);
		}
	}
	
	/**
	 * 删除索引的实现
	 * @param indexTasks
	 */
	protected abstract void doBuildIndexs(Collection<IndexTask> indexTasks) throws IndexException;

	/**
	 * 添加索引的实现
	 * @param indexTasks
	 */
	protected abstract void doDeleteIndexs(Collection<IndexTask> indexTasks) throws IndexException;
	
}
