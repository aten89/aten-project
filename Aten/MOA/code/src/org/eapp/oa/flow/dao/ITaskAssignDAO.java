package org.eapp.oa.flow.dao;

import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.oa.system.dao.IBaseHibernateDAO;


public interface ITaskAssignDAO extends IBaseHibernateDAO {

	public  TaskAssign findById(String id);

}