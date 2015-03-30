/**
 * 
 */
package org.eapp.oa.kb.blo.impl;

import java.util.Collection;
import java.util.List;

import org.eapp.oa.kb.blo.IIndexTaskBiz;
import org.eapp.oa.kb.dao.IIndexTaskDAO;
import org.eapp.oa.lucene.IndexTask;


/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class IndexTaskBiz implements IIndexTaskBiz {

	private IIndexTaskDAO indexTaskDAO;
	
	public IIndexTaskDAO getIndexTaskDAO() {
		return indexTaskDAO;
	}

	public void setIndexTaskDAO(IIndexTaskDAO indexTaskDAO) {
		this.indexTaskDAO = indexTaskDAO;
	}

	@Override
	public long countIndexTask() {
		return indexTaskDAO.countIndexTask();
	}

	@Override
	public List<IndexTask> getIndexTask(int count) {
		return indexTaskDAO.findIndexTask(count);
	}

	@Override
	public IndexTask getIndexTask(String documentId, String documentType) {
		IndexTask ins = new IndexTask();
		ins.setDocumentId(documentId);
		ins.setDocumentType(documentType);
		List<IndexTask> list = indexTaskDAO.findByExample(ins);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void txDelIndexTasks(Collection<IndexTask> its) {
		if (its == null) {
			return;
		}
		for (IndexTask it : its) {
			indexTaskDAO.delete(it);
		}
	}

	@Override
	public void txAddIndexTask(IndexTask it) {
		if (it == null) {
			throw new IllegalArgumentException();
		}
		indexTaskDAO.save(it);
		
	}

	@Override
	public void txModifyIndexTask(IndexTask it) {
		if (it == null) {
			throw new IllegalArgumentException();
		}
		indexTaskDAO.update(it);
	}

}
