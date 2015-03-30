package org.eapp.oa.flow.dao.impl;

import org.eapp.oa.flow.dao.ITaskAssignDAO;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * A data access object (DAO) providing persistence and search support for
 * TaskAssign entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .TaskAssign
 * @author MyEclipse Persistence Tools
 */

public class TaskAssignDAO extends BaseHibernateDAO implements ITaskAssignDAO {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(TaskAssignDAO.class);

	@Override
	public TaskAssign findById(java.lang.String id) {
		log.debug("getting TaskAssign instance with id: " + id);
		TaskAssign instance = (TaskAssign) getSession().get(
				TaskAssign.class, id);
		return instance;
	}
}