package org.eapp.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IDataDictionaryDAO;
import org.eapp.dao.param.DataDictQueryParameters;
import org.eapp.hbean.DataDictionary;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;


/**
 * <p>Title:数据字典数据访问实现 </p>
 * @version 1.0
 */
public class DataDictionaryDAO extends BaseHibernateDAO implements IDataDictionaryDAO {
	private static final Log log = LogFactory.getLog(DataDictionaryDAO.class);

	public DataDictionary findById(java.lang.String id) {
		log.debug("getting DataDictionary instance with id: " + id);
		DataDictionary instance = (DataDictionary) getSession().get("org.eapp.hbean.DataDictionary", id);
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataDictionary> findByParentDictID(String parentDictID) {
		Query queryObject = getSession().createQuery("from DataDictionary as model where model.parentDataDictionary.dataDictID= :dataDictID order by model.displaySort");
		queryObject.setString("dataDictID", parentDictID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DataDictionary> findTypesBySubSystem(String subSystemID) {
		log.debug("finding DataDictionary instance with subSystemID: " + subSystemID);

		String queryString = "from DataDictionary as model where model.subSystem.subSystemID = :subSystemID and model.parentDataDictionary is null order by model.displaySort";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("subSystemID", subSystemID);
		return queryObject.list();
	}

	@Override
	public int getChildDataDictsCountByDataDictID(String dataDictID) {
		log.debug("getChildDataDictsCountByDataDictID");

		String queryString = "select count(*) from DataDictionary where parentDataDictionary.dataDictID = :dataDictID order by displaySort";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("dataDictID", dataDictID);
		Long count = (Long) queryObject.uniqueResult();
		return count.intValue();
	}

	@Override
	public int getChildDataDictsCountBySubSystemID(String subSystemID) {
		log.debug("getChildDataDictsCountBySubSystemID");

		String queryString = "select count(*) from DataDictionary where subSystem.subSystemID = :subSystemID and parentDataDictionary.dataDictID is null order by displaySort";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("subSystemID", subSystemID);
		Long count = (Long) queryObject.uniqueResult();
		return count.intValue();
	}

	@Override
	public void resortChildDataDictByDataDictID(String systemId, String dataDictID, int startIndex) {
		log.debug("resortChildDataDictByDataDictID");
		
		String queryString = null;
		Query queryObject = null;
		if (StringUtils.isBlank(dataDictID)) {
			queryString = "update DataDictionary set displaySort = displaySort-1 where parentDataDictionary.dataDictID is null and subSystem.subSystemID = :subSystemID and displaySort >:displaySort";
			queryObject = getSession().createQuery(queryString);
			queryObject.setString("subSystemID", systemId);
			queryObject.setInteger("displaySort", startIndex);
		} else {
			queryString = "update DataDictionary set displaySort = displaySort-1 where parentDataDictionary.dataDictID = :parentDataDictID and subSystem.subSystemID = :subSystemID and displaySort >:displaySort";
			queryObject = getSession().createQuery(queryString);
			queryObject.setString("parentDataDictID", dataDictID);
			queryObject.setString("subSystemID", systemId);
			queryObject.setInteger("displaySort", startIndex);
		}
		queryObject.executeUpdate();
		
	}

	@Override
	public ListPage<DataDictionary> queryDataDict(DataDictQueryParameters queryParameters) {
		if (queryParameters == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select dataDict from DataDictionary as dataDict where 1=1");
			if(queryParameters.getDictName() != null){
				hql.append(" and dataDict.dictName like :dictName");
				queryParameters.toArountParameter("dictName");
			}
			if(queryParameters.getSubSystemID() != null){
				hql.append(" and dataDict.subSystem.subSystemID = :subSystemID");
			}
			
			return new CommQuery<DataDictionary>().queryListPage(queryParameters, 
					queryParameters.appendOrders(hql, "dataDict" ), getSession());
			
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new  ListPage<DataDictionary>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataDictionary> findDataDictByParentCode(String systemID, String dictCode) {
		
		String queryString = "from DataDictionary as model where model.subSystem.subSystemID = :systemID and model.parentDataDictionary.dictCode = :dictCode order by model.displaySort";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("systemID", systemID);
		queryObject.setString("dictCode", dictCode);
		return queryObject.list();
	}

	@Override
	public void deleteDataDictionaryBySubSystemId(String id) {
		log.debug("deleting dataDict SubSystemId");
		String queryString = "delete from DataDictionary where subSystem.subSystemID =:subSystemID";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("subSystemID", id);
		queryObject.executeUpdate();
		log.debug("delete dataDicts successful");
	}
	
	@Override
	public boolean isKeyRepeat(String subSystemId, String parentDataDictID, String dictCode){
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(dictCode)) {
			return true;
		}
		try {
			String hql = "select count(m) from DataDictionary as m where m.subSystem.subSystemID=:systemID and m.dictCode=:dictCode";
			if (StringUtils.isNotBlank(parentDataDictID)) {
				hql += " and m.parentDataDictionary.dataDictID=:parentDataDictID";
			} else {
				hql += " and m.parentDataDictionary is null";
			}
			Query query = getSession().createQuery(hql);
			query.setString("systemID", subSystemId);
			if (StringUtils.isNotBlank(parentDataDictID)) {
				query.setString("parentDataDictID", parentDataDictID);
			}
			query.setString("dictCode", dictCode);
			Long count = (Long)query.uniqueResult();
			if (count != null && count.longValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error("select module fail", e);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataDictionary> findAllDataDictByType(
			String subSystemId, String dictType, Integer treeLevel) {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(dictType)) {
			return null;
		}
		
		StringBuffer hql = new StringBuffer("from DataDictionary as m where m.subSystem.subSystemID=:systemID and m.dictType=:dictType");
		if (treeLevel != null) {
			hql.append(" and m.treeLevel=:treeLevel");
		}
		hql.append(" order by m.displaySort");
		Query query = getSession().createQuery(hql.toString());
		query.setString("systemID", subSystemId);
		query.setString("dictType", dictType);
		if (treeLevel != null) {
			query.setInteger("treeLevel", treeLevel.intValue());
		}
		return query.list();
	}
	
	@Override
	public DataDictionary findDataDictByCode(String subSystemId, String dictCode) {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(dictCode)) {
			return null;
		}
		Query query = getSession().createQuery(
				"from DataDictionary as m where m.subSystem.subSystemID=:systemID and m.dictCode=:dictCode");
		query.setString("systemID", subSystemId);
		query.setString("dictCode", dictCode);
		query.setMaxResults(1);
		return (DataDictionary)query.uniqueResult();
//		List<DataDictionary> l = query.list();
//		if (l != null && l.size() > 0) {
//			return l.get(0);
//		}
	}
	
	@Override
	public int getNextDisplayOrder(String subSystemID, String dataDictID) {
		StringBuffer hql = new StringBuffer("select max(g.displaySort) from DataDictionary as g where g.subSystem.subSystemID=:subSystemID");
		if (StringUtils.isNotBlank(dataDictID)) {
			hql.append(" and g.parentDataDictionary.dataDictID=:dataDictID");
		} else {
			hql.append(" and g.parentDataDictionary is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setString("subSystemID", subSystemID);
		if (StringUtils.isNotBlank(dataDictID)) {
			query.setString("dataDictID", dataDictID);
		}
		Integer order = (Integer)query.uniqueResult();
		if(order == null) {
			return 1;
		}
		return order.intValue() + 1;
	}
	
	@Override
	public int getNextTreeLevel(String subSystemID, String dataDictID) {
		if (StringUtils.isBlank(dataDictID)) {
			return 1;
		}
		Query query = getSession().createQuery("select g.treeLevel from DataDictionary as g " +
				"where g.subSystem.subSystemID=:subSystemID and g.dataDictID=:dataDictID");
		query.setString("subSystemID", subSystemID);
		query.setString("dataDictID", dataDictID);
		Integer level = (Integer)query.uniqueResult();
		if (level == null) {
			return 1;
		}
		return level.intValue() + 1;
	}
	
	@Override
	public void updateOrder(String parentDataDictID, Integer theOrder) {
		StringBuffer hql = new StringBuffer("update DataDictionary as g set g.displaySort=g.displaySort-1 " +
				"where g.displaySort>:displaySort");
		if (StringUtils.isNotBlank(parentDataDictID)) {
			hql.append(" and g.parentDataDictionary.dataDictID=:dataDictID");
		} else {
			hql.append(" and g.parentDataDictionary is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setInteger("displaySort", theOrder == null ? 0 : theOrder.intValue());
		if (StringUtils.isNotBlank(parentDataDictID)) {
			query.setString("dataDictID", parentDataDictID);
		}
		query.executeUpdate();
	}

}