package org.eapp.util.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;


/**
 * CommQuery
 * Hibernate 翻页查询工具类
 * @param <D> 查询对象的类型
 * @author 卓诗垚
 * 
 */
public class CommQuery<D> {
    
//    /**
//     * 旧版本的列表分页查询
//     * 建议使用ListPage<D> queryPage(QueryParameters qp, String queryHql)
//     * @param qp QueryParameters查询参数对象
//     * @param queryHql 查询HQL语句
//     * @param countHql 计算结果总条数的HQL语句，对应于查询HQL语句的Count
//     * @param session Hibernate Session
//     * @return 被封装成页面（分页后）的结果集对象
//     */
//	public static ListPage<Object> queryPage(QueryParameters qp, String queryHql, 
//			String countHql, Session session) {
//    	CommQuery<Object> comQuery = new CommQuery<Object>();
//    	return comQuery.queryListPage(qp, queryHql, countHql, session);
//    }
    
    /**
     * 分页查询
     * @param qp QueryParameters查询参数对象
     * @param query 查询QUERY
     * @param countQuery 统计QUERY
     * @return 被封装成页面（分页后）的结果集对象
     */
    @SuppressWarnings("unchecked")
	public ListPage<D> queryListPage(QueryParameters qp, Query query, Query countQuery) {
    	ListPage<D> listPage = new ListPage<D>();
    	if (qp == null || query == null || countQuery == null) {
    		throw new IllegalArgumentException();
    	}
        int pageNo = qp.getPageNo();
        int pageSize = qp.getPageSize();
        int firstResultIndex = (pageNo - 1) * pageSize;
        countQuery.setProperties(qp.getParameters());
//        launchParamValues(countQuery, qp.getParameters());
        Number totalNumber = (Number) countQuery.uniqueResult();
        long totalCount = totalNumber != null ? totalNumber.longValue() : 0;
      //如果当前页查到数据为空且页码大于1,则查找上一页
        if (totalCount > 0 && firstResultIndex == totalCount) {
        	qp.setPageNo(pageNo - 1);
        	return queryListPage(qp, query, countQuery);
        }
        if (totalCount > 0 && firstResultIndex < totalCount) {
        	query.setProperties(qp.getParameters());
//          	launchParamValues(query, qp.getParameters());
          	query.setFirstResult(firstResultIndex);
          	if (pageSize > 0) {
          		query.setMaxResults(pageSize);
          	}
          	
          	List<D> result = query.list();
          	
//          	listPage = new ListPage<D>();
          	listPage.setTotalCount(totalCount);
          	listPage.setDataList(result);
        }
        listPage.setCurrentPageNo(pageNo);
      	listPage.setCurrentPageSize(pageSize);
        return listPage;
    }
    
    /**
     * 分页查询
     * 支持泛型
     * @param qp QueryParameters查询参数对象
     * @param queryHql 查询HQL语句
     * @param countHql 计算结果总条数的HQL语句，对应于查询HQL语句的Count
     * @param session Hibernate Session
     * @return 被封装成页面（分页后）的结果集对象
     */
	public ListPage<D> queryListPage(QueryParameters qp, String queryHql, String countHql, Session session) {
    	if (qp == null || queryHql == null || session == null) {
    		throw new IllegalArgumentException();
    	}
    	Query query = session.createQuery(queryHql);
    	Query countQuery = session.createQuery(countHql);
    	return queryListPage(qp, query, countQuery);
    }

    /**
     * 分页查询
     * 
     * @param qp QueryParameters查询参数对象
     * @param queryHql 查询HQL语句
     * @param session Hibernate Session
     * @return 被封装成页面（分页后）的结果集对象
     */
    public ListPage<D> queryListPage(QueryParameters qp, String queryHql, Session session) {
    	return queryListPage(qp, queryHql, getCountHql(queryHql), session);
    }
    
    /**
     * 通过查询语取组装成查询总数语句
     * @param queryHql 查询HQL语句
     * @return 计算结果总条数的HQL语句，对应于查询HQL语句的Count
     */
    private static String getCountHql(String queryHql) {
    	String temHql = queryHql.toLowerCase();
    	int i = temHql.indexOf("from");
    	if (i == -1) {
    		throw new IllegalArgumentException("HQL 语法不正确");
    	}
    	return "select count(*) " + queryHql.substring(i);
    }
    
    /**
     * 为Query设置条件
     * @param query Hibernate Query
     * @param conditions 查询条件Map
     */
//    @SuppressWarnings("unchecked")
//	public static void launchParamValues(Query query, Map<String, Object> conditions) {
//    	if(conditions != null && query != null){
//			for (String name : conditions.keySet()) {
//				Object value = conditions.get(name);
//				if (value == null) {
//					continue;
//				}
//				if (value instanceof String) {
//					query.setString(name , (String)value);
//				} else if (value instanceof Integer) {
//					query.setInteger(name , ( (Integer)value ).intValue());	
//				} else if (value instanceof Long) {
//					query.setLong(name, ( (Long)value ).longValue());
//				} else if (value instanceof Float) {
//					query.setFloat(name, ( (Float)value ).floatValue());
//				} else if (value instanceof Double) {
//					query.setDouble(name, ( (Double)value ).doubleValue());
//				} else if (value instanceof Boolean) {
//					query.setBoolean(name, ( (Boolean)value ).booleanValue());
//				} else if (value instanceof Timestamp) {
//					query.setTimestamp(name, (Timestamp)value);
//				} else if (value instanceof Date) {
//					query.setDate(name, (Date)value);
//				} else if (value instanceof Collection) {
//					query.setParameterList(name, (Collection<Object>)value);
//				} else if (value instanceof Object[]) {
//					query.setParameterList(name, (Object[])value);
//				} else {
//					query.setParameter(name, value);
//				}
//			}
//    	}
//	}
}
