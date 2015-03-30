package org.eapp.oa.doc.blo;

import java.util.List;

import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public interface IDocClassBiz {

	/**
	 * 新增公文类别
	 * 
	 * @param name
	 * @param flowClass
	 * @param description
	 * @param bodyTemplate
	 * @return
	 * @throws OaException
	 */
	public DocClass addDocClass(String name, String flowClass, Integer fileClass,
			String description) throws OaException;

	/**
	 * 删除公文类别
	 * 
	 * @param id
	 * @return
	 * @throws OaException
	 */
	public DocClass deleteDocClass(String id) throws OaException;

	/**
	 * 修改公文类别
	 * 
	 * @param id
	 * @param name
	 * @param flowClass
	 * @param description
	 * @param bodyTemplate
	 * @return
	 * @throws OaException
	 */
	public DocClass modifyDocClass(String id, String name,String flowClass,Integer filewClass,
			String description) throws OaException;

	/**
	 * 取得用户有权限的公文类别
	 * 
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @return
	 */
	public List<DocClass> getAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames,int fileClass);

	/**
	 * 获得所有的公文类别信息
	 * 
	 * @return
	 */
	public List<DocClass> getAllDocClass();

	/**
	 * 排序
	 * 
	 * @param DocClassIds
	 */
	public void txSaveOrder(String[] DocClassIds);

	
	/**
	 * 通过名称查找有权限的类别
	 * @param userAccountId
	 * @param groupNames
	 * @param postNames
	 * @param name
	 * @param fileClass
	 * @return
	 */
	public DocClass getAssignDocClassByName(String userAccountId,
			List<String> groupNames, List<String> postNames, String name, int fileClass);
	
	/**
	 * 通过名称查找
	 * @param name
	 * @return
	 */
	public DocClass getDocClassByName(String name);
	
	/**
	 * 通过id查找
	 * 
	 * @param id
	 * @return
	 */
	public DocClass getDocClassById(String id);

	/**
	 * 新增正文模板
	 * 
	 * @param docClassId
	 * @param am
	 */
	public void addBodyTemplate(String docClassId, Attachment am);
	
	public List<DocClass> getByFileClass(Integer fileType);

}
