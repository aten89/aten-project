package org.eapp.oa.doc.dao;

import java.util.List;

import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocClassDAO extends IBaseHibernateDAO {

	public DocClass findById(java.lang.String id);

	public List<DocClass> findByExample(DocClass instance);

	public List<DocClass> findByProperty(String propertyName, Object value);

	public List<DocClass> findByName(Object name);

	public List<DocClass> findByFlowclass(Object flowclass);

	public List<DocClass> findByDisplayorder();

	public List<DocClass> findByDescription(Object description);

	public List<DocClass> findAll();
	
	public List<DocClass> findAll(Integer fileType);

	//自定义抽象方法
	public int getDisplayOrder();
	
	public List<DocClass> findAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames, String name,int fileClass);
	
	public List<DocClass> findAssignDoc(String userAccountId,
			List<String> groupNames, List<String> postNames) ;
}
