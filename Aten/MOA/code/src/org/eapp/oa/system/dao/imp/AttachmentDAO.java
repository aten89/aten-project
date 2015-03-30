package org.eapp.oa.system.dao.imp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.system.dao.IAttachmentDAO;
import org.eapp.oa.system.hbean.Attachment;
import org.hibernate.Query;

/**
 * 
 * @author Administrator
 *
 */
public class AttachmentDAO extends BaseHibernateDAO implements IAttachmentDAO  {
    
	
	private static final Log log = LogFactory.getLog(AttachmentDAO.class);
	//property constants
	public static final String FILE_EXT = "fileExt";
	public static final String DISPLAY_NAME = "displayName";

 
    /* (non-Javadoc)
	 */
    public Attachment findById(String id) {
        log.debug("getting Attachment instance with id: " + id);
        try {
            Attachment instance = (Attachment) getSession()
                    .get(Attachment.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
   
    /* (non-Javadoc)
	 */
    @SuppressWarnings("unchecked")
	private List<Attachment> findByProperty(String propertyName, Object value) {
      log.debug("finding Attachment instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Attachment as model where model." 
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
	public List<Attachment> findByFileExt(Object fileExt) {
		return findByProperty(FILE_EXT, fileExt);
	}
	
	/* (non-Javadoc)
	 */
	public List<Attachment> findByDisplayName(Object displayName) {
		return findByProperty(DISPLAY_NAME, displayName);
	}
}