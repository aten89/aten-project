
package org.eapp.oa.hr.dao.impl;

// default package
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.hr.dao.IHolidayTypeDAO;
import org.eapp.oa.hr.hbean.HolidayType;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;


public class HolidayTypeDAO extends BaseHibernateDAO implements IHolidayTypeDAO {

	private static final Log log = LogFactory.getLog(HolidayTypeDAO.class);
	
	public HolidayType findById(java.lang.String id){
		log.debug("getting HolidayInfo instance with id: " + id);
		try {
			HolidayType instance = (HolidayType) getSession().get(HolidayType.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HolidayType> findByExample(HolidayType instance){
		log.debug("finding HolidayInfo instance by example");
		try {
			List<HolidayType> results = getSession().createCriteria("HolidayType")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HolidayType> findByProperty(String propertyName,
			Object value) {
		log.debug("finding HolidayType instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from HolidayType as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HolidayType> findAll(){
		try {
			String queryString = "from HolidayType";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public HolidayType findByHolidayName(String holidayName){
		List<HolidayType> holidayType = findByProperty("holidayName",holidayName);
		if(holidayType == null || holidayType.isEmpty()){
			return null;
		}
		return holidayType.get(0);
	}

	@Override
	public boolean checkHolidayName(String holidayName) {
		String queryString = "select count(rfc) from HolidayType as rfc where rfc.holidayName=:holidayName";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setString("holidayName", holidayName);
        Long count = (Long)queryObject.uniqueResult();
        if (count == null || count.longValue() ==0) {
        	return true;
        }
        return false;
	}
}

