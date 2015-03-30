package org.eapp.oa.kb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.kb.dao.ILabelLibDAO;
import org.eapp.oa.kb.dto.LabelLibQueryParameters;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;


public class LabelLibDAO extends BaseHibernateDAO implements ILabelLibDAO {
	
	private static final Log log = LogFactory.getLog(LabelLibDAO.class);
	// property constants
	
	public LabelLib findById(java.lang.String id) {
		log.debug("getting LabelLib instance with id: " + id);
		try {
			LabelLib instance = (LabelLib) getSession().get(LabelLib.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<LabelLib> findByName(String name) {
		try {
			String queryString = "from LabelLib as model where model.name=:name";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setString("name", name);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<LabelLib> findAll() {
		log.debug("finding all LabelLib instances");
		try {
			String queryString = "from LabelLib";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ListPage<LabelLib> queryLabelLib(LabelLibQueryParameters qp){
		if (qp == null)  {
			throw new IllegalArgumentException("非法参数:查询参数为空");
		}
		try {
			StringBuffer hql = new StringBuffer("select m from LabelLib as m where 1=1 ");
			//拼接查询条件
			if(qp.getName()!=null){
				hql.append(" and m.name like :name");
				qp.toArountParameter("name");
			}
			if(qp.getStartCount()!=null){
				hql.append(" and m.count >= :stratCount");
			}
			if(qp.getEndCount()!=null){
				hql.append(" and m.count <= :endCount");
			}
			return new CommQuery<LabelLib>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<LabelLib>();
		}
	}

	@Override
	public void deleteLabelLib(String id, String name, Long startCount, Long endCount) {

		try {
			Map<String, Object> param = new HashMap<String, Object>();
			StringBuffer hql = new StringBuffer("delete LabelLib as m  where 1=1 ");
			if(id != null){
				hql.append(" and m.id =:id");
				param.put("id", id);
			}
			if(name != null){
				hql.append(" and m.name like :name");
				param.put("name", "%" + name + "%");
			}
			if(startCount != null){
				hql.append(" and m.count >=:startCount");	
				param.put("startCount", startCount);
			}
			if(endCount != null){
				hql.append(" and m.count <=:endCount");
				param.put("endCount", endCount);
			}
			
			Query query = getSession().createQuery(hql.toString());
			query.setProperties(param);
//			CommQuery.launchParamValues(query, param);
			query.executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}