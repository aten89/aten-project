package org.eapp.oa.flow.dao;

import java.util.List;

import org.eapp.oa.flow.hbean.FlowMeta;
import org.eapp.oa.system.dao.IBaseHibernateDAO;


public interface IFlowMetaDAO extends IBaseHibernateDAO {

	public FlowMeta findById(String id);

	public List<FlowMeta> findByFlowCategory(Object flowCategory);
}