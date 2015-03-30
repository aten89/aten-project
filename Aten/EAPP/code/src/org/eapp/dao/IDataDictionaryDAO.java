package org.eapp.dao;

import java.util.List;

import org.eapp.dao.param.DataDictQueryParameters;
import org.eapp.hbean.DataDictionary;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @version
 */
public interface IDataDictionaryDAO extends IBaseHibernateDAO {
	
	/**
	 * 删除数据字典实例 
	 * @param dataDictID 条目ID
	 */
//	public void deleteByID(String dataDictID);


	/**
	 * 加载数据字典实例
	 * @param id 条目ID
	 * @return 条目信息实例
	 */
	public DataDictionary findById(java.lang.String id);

	public List<DataDictionary> findByParentDictID(String parentDictID);

	/**
	 * 获取子系统下的类型数目
	 * @param subSystemID 子系统ID
	 * @return 类型数目
	 */
	public int getChildDataDictsCountBySubSystemID(String subSystemID);
	
	/**
	 * 获取条目的子条目数
	 * @param dataDictID 数据字典子条目ID
	 * @return 子条目数
	 */
	public int getChildDataDictsCountByDataDictID(String dataDictID);
	
	/**
	 * 将条目下顺序大于startIndex的条目顺序减1(即向前移一位)
	 * @param dataDictID 条目ID
	 * @param startIndex 起始点
	 */
	public void resortChildDataDictByDataDictID(String systemId, String dataDictID, int startIndex);

	public ListPage<DataDictionary> queryDataDict(DataDictQueryParameters dataDictQP);
	
	public List<DataDictionary> findTypesBySubSystem(String subSystemID);

	public List<DataDictionary> findDataDictByParentCode(String systemID, String dictCode);
	
	public void deleteDataDictionaryBySubSystemId(String id);
	
	/**
	 * 判断字典key是否重复
	 * @param subSystemId
	 * @param dictName
	 * @return
	 */
	public boolean isKeyRepeat(String subSystemId, String parentDataDictID, String dictName);
	
	/**
	 * 通过字典类型取得子系统下所有字典
	 * @param subSystemId
	 * @param dictType
	 * @param displaySort 若为空，不受约束
	 * @return
	 */
	public List<DataDictionary> findAllDataDictByType(
			String subSystemId, String dictType, Integer treeLevel);
	
	/**
	 * 通过字典key取得子系统下所有字典
	 * @param subSystemId
	 * @param dictCode
	 * @return
	 */
	public DataDictionary findDataDictByCode(String subSystemId, String dictCode);
	
	public int getNextDisplayOrder(String subSystemID, String dataDictID);
	
	public int getNextTreeLevel(String subSystemID, String dataDictID);
	
	public void updateOrder(String parentDataDictID, Integer theOrder);
}