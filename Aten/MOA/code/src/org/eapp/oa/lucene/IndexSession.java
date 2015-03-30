package org.eapp.oa.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

/**
 * 对索引的基本操作
 * @author zsy
 * @version Jun 12, 2009
 */
public class IndexSession {
	private Analyzer analyzer;//分词器
	private Directory dir;//索引目录
	private IndexWriter writer;//索引写入器
	
	
	public IndexSession(String indexDir) throws IndexException {
		try {
			dir = FSDirectory.getDirectory(indexDir);
		} catch (Exception e) {
			throw new IndexException(e);
		}
		analyzer =  ConfigFactory.getAnalyzer();
	}
	
	/**
	 * 初始化索引写入器
	 * @return
	 * @throws IndexException
	 */
	private IndexWriter getWriter() throws IndexException {
		if (writer  == null) {
			try {
				writer = new IndexWriter(dir, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
				writer.setMergeFactor(20);
			} catch (Exception e) {
				throw new IndexException(e);
			}
		}
		return writer;
	}
	
	/**
	 * 创建内存索引目录
	 * @param docs
	 * @return
	 * @throws IndexException
	 */
	public Directory createRAMDirectory(List<Document> docs) throws IndexException {
		if (docs == null) {
			return null;
		}
		try {
			RAMDirectory ramDir = new RAMDirectory();
	    	IndexWriter ramWriter = new IndexWriter(ramDir, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
	    	ramWriter.setMergeFactor(128);
	    	for (Document doc : docs) {
	    		if (doc != null) {
	    			ramWriter.addDocument(doc);
	          	}
	     	}
	    	ramWriter.close();
	    	return ramDir;
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 合并索引到主索引
	 * @param directorys
	 * @throws IndexException
	 */
	public void mergeIndexs(List<Directory> directorys) throws IndexException {
		if (directorys == null || directorys.size() == 0) {
			return;
		}
		try {
			getWriter().addIndexesNoOptimize(directorys.toArray(new Directory[0]));
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 直接添加到主索引
	 * @param docs
	 * @throws IndexException
	 */
	public void addIndex(List<Document> docs) throws IndexException {
		if (docs == null) {
			return;
		}
		try {
	    	for (Document doc : docs) {
	    		if (doc != null) {
	    			getWriter().addDocument(doc);
	          	}
	     	}
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 根据条件删除索引
	 * @param query
	 * @throws IndexException
	 */
	public void deleteIndex(Query query) throws IndexException {
		try {
			getWriter().deleteDocuments(query);
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 删除索引
	 * @param term
	 * @throws IndexException
	 */
	public void deleteIndex(Term term) throws IndexException {
		try {
			getWriter().deleteDocuments(term);
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 删除索引
	 * @param terms
	 * @throws IndexException
	 */
	public void deleteIndex(List<Term> terms) throws IndexException {
		if (terms == null) {
			return;
		}
		try {
			for (Term term : terms) {
				getWriter().deleteDocuments(term);
			}
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 删除所有索引
	 * @throws IndexException
	 */
	public void deleteAllIndex() throws IndexException {
		try {
			String[] files = dir.list();
			for (String file : files) {
				dir.deleteFile(file);
			}
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 优化索引结构
	 * @throws IndexException
	 */
	public void optimize() throws IndexException {
		try {
			getWriter().optimize();
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 提交对主索引的所有操作
	 * @throws IndexException
	 */
	public void commit() throws IndexException {
		try {
			if (writer != null) {
				writer.commit();
			}
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	/**
	 * 回滚对主索引的所有操作
	 */
	public void rollback() {
		try {
			if (writer != null) {
				writer.rollback();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭
	 */
	public void close() {
		try {
    		if (writer != null) {
    			writer.close();
    		}
    		if (dir != null) {
				dir.close();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
		    	if (IndexWriter.isLocked(dir)) {
		    		IndexWriter.unlock(dir);
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
