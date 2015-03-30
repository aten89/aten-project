package org.eapp.oa.doc.dao;

import java.util.List;

import org.eapp.oa.doc.hbean.DocClassAssign;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocClassAssignDAO extends IBaseHibernateDAO {

	public DocClassAssign findById(java.lang.String id);

	public List<DocClassAssign> findByExample(DocClassAssign instance);

	public List<DocClassAssign> findByProperty(String propertyName, Object value);

	public List<DocClassAssign> findByType(Object type);

	public List<DocClassAssign> findByAssignkey(Object assignkey);

	public List<DocClassAssign> findAll();
	
	//自定义抽象方法
	public List<DocClassAssign> findBindById(String id , int type);
	
	public void delBindById(String id , int type);
}
