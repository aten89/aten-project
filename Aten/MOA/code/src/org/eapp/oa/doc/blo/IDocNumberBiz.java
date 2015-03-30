package org.eapp.oa.doc.blo;

import java.util.List;

import org.eapp.oa.doc.hbean.DocNumber;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocNumberBiz {

	/**
	 * 新增公文编号
	 * 
	 * @param docWord
	 * @param yearPrefix
	 * @param yearPostfix
	 * @param orderPrefix
	 * @param orderPostfix
	 * @return
	 * @throws OaException
	 */
	public DocNumber addDocNumber(String docWord, String yearPrefix,
			String yearPostfix, String orderPrefix, String orderPostfix)
			throws OaException;

	/**
	 * 删除公文编号
	 * 
	 * @param id
	 * @return
	 * @throws OaException
	 */
	public DocNumber deleteDocNumber(String id) throws OaException;

	/**
	 * 修改公文编号
	 * 
	 * @param id
	 * @param docWord
	 * @param yearPrefix
	 * @param yearPostfix
	 * @param orderPrefix
	 * @param orderPostfix
	 * @return
	 * @throws OaException
	 */
	public DocNumber modifyDocNumber(String id, String docWord,
			String yearPrefix, String yearPostfix, String orderPrefix,
			String orderPostfix) throws OaException;

	public DocNumber txUpdateOrder(String id, int order, int year);
	
	/**
	 * 查询所有公文编号
	 * 
	 * @return
	 */
	public List<DocNumber> getAllDocNumber();

	/**
	 * 根据id查询公文编号
	 * 
	 * @param id
	 * @return
	 */
	public DocNumber getDocNumber(String id);

	/**
	 * 保存红头模板
	 * 
	 * @param id
	 * @param am
	 */
	public void addHeadTemplate(String id, Attachment am);
}
