package org.eapp.oa.kb.blo;
import java.util.List;

import org.eapp.oa.kb.dto.LabelLibQueryParameters;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.util.hibernate.ListPage;
import org.eapp.oa.system.exception.OaException;
/**
 * @author 郑凡超
 * 标签管理业务逻辑
 */
public interface ILabelLibBiz {
	
	/**
	 * 新增或删除
	 * @param id
	 * @param name
	 * @param property
	 * @throws OaException
	 */
	public void txAddOrModLabelLib(String id, String name, int property) throws OaException;

	/**
	 * 翻页查找
	 * @param qp
	 * @return
	 */
	public ListPage<LabelLib> queryLabelLibs(LabelLibQueryParameters qp);
	
	/**
	 * 删除
	 * @param id
	 * @param name
	 * @param startCount
	 * @param endCount
	 * @throws OaException
	 */
	public void txDelLabelLibs(String id ,String name,Long startCount, Long endCount)throws OaException;
	
	/**
	 * 
	 * @param names
	 */
	public void txAddOrModLabelLibByNames(String[] names);
	
	/**
	 * 获取标签名称
	 * 
	 */
	public List<String> getLabelName();

}