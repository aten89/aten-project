/**
 * 
 */
package org.eapp.blo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.dao.param.DataDictQueryParameters;

import org.eapp.exception.EappException;
import org.eapp.hbean.DataDictionary;
import org.eapp.util.hibernate.ListPage;


/**
 * @version 1.0
 */
public interface IDataDictionaryBiz {

	/**
	 * 列出子系统下的所有类型
	 * 
	 * @param systemId 子系统ID
	 * @return 所有类型
	 */
	public List<DataDictionary> getAllTypesBySubSystem(String systemId);

	/**
	 * 列出系统下某类型的所有数据字典  add 08/06/09 by wsw
	 * @param systemID
	 * @param type
	 * @return
	 */
	public List<DataDictionary> getAllDataDictByType(String systemID,String type);
	/**
	 * 查看数据字典实例
	 * 
	 * @param dataDictID 条目ID
	 * @return 条目实例
	 */
	public DataDictionary getDataDictionary(String dataDictID);

	/**
	 * 删除数据字典实例
	 * 
	 * @param id 条目ID
	 * @return 
	 * @throws EappException 该条目有子条目
	 */
	public DataDictionary deleteDataDictionary(String id) throws EappException;

	/**
	 * 保存数据字典实例
	 * 
	 * @param subSystemID
	 * @param parentDataDictID
	 * @param dictName
	 * @param dictCode
	 * @param ceilValue
	 * @param floorValue
	 * @param dictType
	 * @param description
	 * @return 保存后的实例(生成的ID)
	 * @throws EappException KEY在数据库中有重复
	 */
	public DataDictionary addDataDictionary(String subSystemID, String parentDataDictID,
			String dictName, String dictCode, String ceilValue, String floorValue, String dictType,
			String description) throws EappException;

	/**
	 * 修改数据字典实例
	 * 
	 * @param dataDict 数据字典实例
	 * @throws EappException 类型必须位于第一层;普通字典条目不能位于第一层;模块的原有子条目不能作为父条目
	 */
	public DataDictionary modifyDataDictionary(String dataDictID, String subSystemID, String parentDataDictID,
			String dictName, String dictCode, String ceilValue, String floorValue, String dictType,
			String description) throws EappException;

	/**
	 * 更新数据条目顺序
	 * 
	 * @param idSort 条目ID和顺序的MAP
	 * @return 是否更新成功
	 */
	public void modifyDataDictionarySort(Map<String, Integer> idSort);

	/**
	 * 列出父条目下的子条目树
	 * 
	 * @param dataDictID 条目ID
	 * @param level 树的层次
	 * @return 子条目树
	 */
	public Set<DataDictionary> getChildDataDictsByParentID(String dataDictID, int level);

	/**
	 * 2008.6.6 add
	 * 根据KEY模糊查询条目
	 * @param dataDictQP
	 * @return
	 */
	public ListPage<DataDictionary> queryDataDict(DataDictQueryParameters dataDictQP);
	
	/**
	 * 2008.6.6 add
	 * 查询条目的父条目到顶级条目
	 * @param dataDictID
	 * @return
	 */
	public List<DataDictionary> getParentsByDataDict(String dataDictID);

	/**
	 * 取得子条目
	 * 2008.6.17 ADD FRO HESSIAN
	 * @param systemID
	 * @param dictCode
	 * @param level 为0时取整个子条目树,其他则取1层
	 * @return
	 */
	public List<DataDictionary> getChildDataDicts(String systemID, String dictCode);

	/**
	 * 取得某类型的子条目列表
	 * 2008.6.17 ADD FRO HESSIAN
	 * @param systemID
	 * @param type
	 * @return
	 */
	public List<DataDictionary> getDataDictList(String systemID, String type);

	/**
	 * 取得某类型的子条目树
	 * 2008.6.17 ADD FRO HESSIAN
	 * @param systemID
	 * @param type
	 * @return
	 */
	public List<DataDictionary> getDataDictTree(String systemID, String type);

	/**
	 * 根据KEY得到条目
	 * 2008.6.17 ADD FRO HESSIAN
	 * @param systemID
	 * @param key
	 * @return
	 */
	public DataDictionary getDataDictByCode(String systemID, String dictCode);
}
