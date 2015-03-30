package org.eapp.oa.kb.dao;
import java.util.List;

import org.eapp.oa.kb.dto.LabelLibQueryParameters;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface ILabelLibDAO extends IBaseHibernateDAO {

	/**
	 * 通过ID查找
	 * @param id
	 * @return
	 */
	public LabelLib findById(String id);
	
	/**
	 * 通过名称查找
	 * @param name
	 * @return
	 */
	public List<LabelLib> findByName(String name);

	/**
	 * 查找所有
	 * @return
	 */
	public List<LabelLib> findAll();
	
	/**
	 * 翻页查找
	 * @param qp
	 * @return
	 */
	public ListPage<LabelLib> queryLabelLib(LabelLibQueryParameters qp);
	
	/**
	 * 删除
	 * @param id
	 * @param name
	 * @param startCount
	 * @param endCount
	 */
	public void deleteLabelLib(String id, String name, Long startCount, Long endCount);
}