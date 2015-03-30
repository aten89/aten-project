package org.eapp.oa.doc.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.doc.dao.IDocClassDAO;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.doc.hbean.DocClassAssign;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassDAO extends BaseHibernateDAO implements IDocClassDAO {
	private static final Log log = LogFactory.getLog(DocClassDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String FLOWCLASS = "flowclass";
	public static final String DISPLAYORDER = "displayorder";
	public static final String DESCRIPTION = "description";

	/* (non-Javadoc)
	 */
	public DocClass findById(java.lang.String id) {
		log.debug("getting DocClass instance with id: " + id);
		try {
			DocClass instance = (DocClass) getSession().get(
					DocClass.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClass> findByExample(DocClass instance) {
		log.debug("finding DocClass instance by example");
		try {
			List<DocClass> results = getSession().createCriteria("DocClass").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClass> findByProperty(String propertyName, Object value) {
		log.debug("finding DocClass instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DocClass as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	public List<DocClass> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	/* (non-Javadoc)
	 */
	public List<DocClass> findByFlowclass(Object flowclass) {
		return findByProperty(FLOWCLASS, flowclass);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClass> findByDisplayorder() {
//		return findByProperty(DISPLAYORDER, displayorder);
		log.debug("finding all DocClass instances");
		try {
			String queryString = "from DocClass l order by l.displayOrder";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	public List<DocClass> findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClass> findAll() {
		log.debug("finding all DocClass instances");
		try {
			String queryString = "from DocClass";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<DocClass> findAll(Integer fileType) {
		log.debug("finding all DocClass instances");
		try {
			String queryString = "from DocClass as dc where dc.fileClass=:fileType";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setInteger("fileType", fileType);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	@Override
	public int getDisplayOrder() {
		try {
			String queryString = "select max(l.displayOrder) from DocClass l";
			Query queryObject = getSession().createQuery(queryString);
			Integer value = (Integer)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 获取有权限的公文
	 */
	public List<DocClass> findAssignDoc(String userAccountId,
			List<String> groupNames, List<String> postNames) {	
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		try {
			StringBuffer queryString = new StringBuffer("select distinct(dca.docClass) from DocClassAssign as dca");
			queryString.append(" where (");
			queryString.append("(dca.type=:utype and dca.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (dca.type=:gtype and dca.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (dca.type=:ptype and dca.assignKey in (:postNames))");
			}
			queryString.append(")");
			
			Query query = getSession().createQuery(queryString.toString());
//			query.setInteger("flag", flag);
			query.setInteger("utype", DocClass.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			
			if (isGroup) {
				query.setInteger("gtype", DocClass.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("ptype", DocClass.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			
			List<DocClass> list= query.list();
			return list;
		} catch (RuntimeException re) {
			log.error("select DocAssign failed!", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocClass> findAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames, String name,int fileClass) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasNmae = name != null;
		
		try {
			StringBuffer queryString = new StringBuffer("select distinct(dca.docClass) from DocClassAssign as dca");
			queryString.append(" where dca.docClass.fileClass=:fileClass and (");
			queryString.append("(dca.type=:utype and dca.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (dca.type=:gtype and dca.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (dca.type=:ptype and dca.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasNmae) {
				queryString.append(" and dca.docClass.name=:name");
			}
			Query query = getSession().createQuery(queryString.toString());
			query.setInteger("fileClass", fileClass);
//			query.setInteger("flag", flag);
			query.setInteger("utype", DocClassAssign.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			
			if (isGroup) {
				query.setInteger("gtype", DocClassAssign.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("ptype", DocClassAssign.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (hasNmae) {
				query.setString("name", name);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("delete binding by flow_key failed", re);
			throw re;
		}
	}
}
