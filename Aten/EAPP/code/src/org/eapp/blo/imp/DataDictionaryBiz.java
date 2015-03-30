/**
 * 
 */
package org.eapp.blo.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IDataDictionaryBiz;
import org.eapp.dao.IDataDictionaryDAO;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.dao.param.DataDictQueryParameters;

import org.eapp.exception.EappException;
import org.eapp.hbean.DataDictionary;
import org.eapp.hbean.SubSystem;
import org.eapp.util.hibernate.ListPage;


/**
 * @version 1.0
 */
public class DataDictionaryBiz implements IDataDictionaryBiz {

	/**
	 */
	
	private IDataDictionaryDAO dataDictionaryDAO;
	private ISubSystemDAO subSystemDAO;

	public ISubSystemDAO getSubSystemDAO() {
		return subSystemDAO;
	}

	public void setSubSystemDAO(ISubSystemDAO subSystemDAO) {
		this.subSystemDAO = subSystemDAO;
	}

	/**
	 * @param dataDictionaryDAO the dataDictionaryDAO to set
	 */
	public void setDataDictionaryDAO(IDataDictionaryDAO dataDictionaryDAO) {
		this.dataDictionaryDAO = dataDictionaryDAO;
	}

	private boolean isCodeRepeat(String subSystemID, String parentDataDictID, String dictCode) throws EappException {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(dictCode)) {
			return true;
		}
		return dataDictionaryDAO.isKeyRepeat(subSystemID, parentDataDictID, dictCode);
	}

	public List<DataDictionary> getAllTypesBySubSystem(String systemID) {
		List<DataDictionary> dataDicts = dataDictionaryDAO.findTypesBySubSystem(systemID);
		initLazy(dataDicts, 2);
		return dataDicts;
	}

	public List<DataDictionary> getAllDataDictByType(String systemID, String dictType) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(dictType)) {
			throw new IllegalArgumentException();
		}

		return dataDictionaryDAO.findAllDataDictByType(systemID, dictType, null);
	}

	public Set<DataDictionary> getChildDataDictsByParentID(String dataDictID, int level) {
		if (StringUtils.isBlank(dataDictID)) {
			throw new IllegalArgumentException();
		}
		DataDictionary dataDict = dataDictionaryDAO.findById(dataDictID);
		if(dataDict == null){
			throw new IllegalArgumentException("参数异常:数据库中不存在该条目ID");
		}
		int treeLevel = 0;
		if (level < 0) { // 层次如果为0,则显示所有的子条目
			throw new IllegalArgumentException();
		} else if (level > 0) {
			treeLevel = dataDict.getTreeLevel() + level;
		} else {
			treeLevel = 0;
		}
		Set<DataDictionary> dataDicts = (Set<DataDictionary>) dataDict.getChildDataDictionaries();
		initLazy(dataDicts, treeLevel);
		return dataDicts;
	}

	public DataDictionary getDataDictionary(String dataDictID) {
		DataDictionary dataDict = dataDictionaryDAO.findById(dataDictID);
		if(dataDict == null){
			throw new IllegalArgumentException();
		}
		return dataDict;
	}

	public DataDictionary addDataDictionary(String subSystemID, String parentDataDictID,
			String dictName, String dictCode, String ceilValue, String floorValue, String dictType,
			String description) throws EappException {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(dictName)) {
			throw new IllegalArgumentException();
		}
		DataDictionary dataDict = new DataDictionary();
		
		SubSystem subSystem = subSystemDAO.findById(subSystemID);
		if(subSystem == null){
			throw new IllegalArgumentException("数据库中不存在该子系统ID"+subSystemID);
		}
		dataDict.setSubSystem(subSystem);
		
		if (StringUtils.isNotBlank(parentDataDictID)) {
			DataDictionary parentDataDict = dataDictionaryDAO.findById(parentDataDictID);
			if(parentDataDict == null){
				throw new IllegalArgumentException("数据库中不存在该父模块ID"+parentDataDictID);
			}
			dataDict.setParentDataDictionary(parentDataDict);
			dictType = parentDataDict.getDictType();
		} else {
			dictCode = dictType;
		}
		if (StringUtils.isBlank(dictCode) || StringUtils.isBlank(dictType)) {
			throw new IllegalArgumentException();
		}
		if (isCodeRepeat(subSystemID, parentDataDictID, dictCode)) {
			throw new EappException("同级下代码不能重复");
		}

		dataDict.setDictName(dictName);
		dataDict.setDictCode(dictCode);
		dataDict.setCeilValue(ceilValue);
		dataDict.setFloorValue(floorValue);
		dataDict.setDictType(dictType);
		dataDict.setDescription(description);
		dataDict.setDisplaySort(new Integer(dataDictionaryDAO.getNextDisplayOrder(subSystemID, parentDataDictID)));
		dataDict.setTreeLevel(new Integer(dataDictionaryDAO.getNextTreeLevel(subSystemID, parentDataDictID)));
		dataDictionaryDAO.save(dataDict);
		return dataDict;
	}

	public DataDictionary deleteDataDictionary(String id) throws EappException {
		DataDictionary dataDict = dataDictionaryDAO.findById(id);
		if(dataDict == null){
			throw new IllegalArgumentException("参数异常:数据库中不存在该条目ID");
		}
		int childCount = dataDictionaryDAO.getChildDataDictsCountByDataDictID(id);
		if (childCount > 0) {
			throw new EappException("条目下有子条目");
		}

		String parentDataDictID = null;
		if(dataDict.getParentDataDictionary() != null){
			parentDataDictID = dataDict.getParentDataDictionary().getDataDictID();
		}
		String systemid = dataDict.getSubSystem().getSubSystemID();
		Integer startIndex = dataDict.getDisplaySort();
		dataDictionaryDAO.delete(dataDict);
		dataDictionaryDAO.resortChildDataDictByDataDictID(systemid, parentDataDictID, startIndex);
		return dataDict;
	}

	public DataDictionary modifyDataDictionary(String dataDictID, String subSystemID, String parentID,
			String dictName, String dictCode, String ceilValue, String floorValue, String dictType,
			String description) throws EappException {
		if (StringUtils.isBlank(dataDictID) ||StringUtils.isBlank(subSystemID) || StringUtils.isBlank(dictName)) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isBlank(parentID)) {
			parentID = null;
		}
		DataDictionary dataDictSrc = dataDictionaryDAO.findById(dataDictID);
		if (dataDictSrc == null){
			throw new IllegalArgumentException("参数异常:数据库中不存在该条目ID");
		}
		
		SubSystem subSystem = subSystemDAO.findById(subSystemID);
		if(subSystem == null){
			throw new IllegalArgumentException("子系统ID:"+subSystemID+"不存在");
		}
		dataDictSrc.setSubSystem(subSystem);
		
		//父ID有改动时
		String oldParentModuleID = dataDictSrc.getParentDataDictionary() == null ? null : dataDictSrc.getParentDataDictionary().getDataDictID();
		if ((parentID != null && !parentID.equals(oldParentModuleID)) || 
				(parentID == null && oldParentModuleID != null)) {
			if (dataDictSrc.getParentDataDictionary() == null || StringUtils.isBlank(parentID)) {
				// 父条目为空则为类型条目,类型条目只能在第一层
				throw new EappException("类型不能移动");
			}
			//父结点不能为本身结点
			if (dataDictSrc.getDataDictID().equals(parentID)) {
				throw new EappException("父节点不能为本身节点");
			}
			
			DataDictionary pd = null;
			if (StringUtils.isNotBlank(parentID)) {
				pd = dataDictionaryDAO.findById(parentID);
				if (pd == null) {
					throw new EappException("模块不存在");
				}
				dictType = pd.getDictType();
			}
			dataDictSrc.setParentDataDictionary(pd);
			if (!dictType.equals(dataDictSrc.getDictType())) {
				//类型变掉，要修改所有下级节点的类型
				updateSubDictType(dataDictSrc, dictType);
			}
			dataDictSrc.setDictType(dictType);
			if (!dataDictSrc.getDictCode().equals(dictCode) && isCodeRepeat(subSystemID, parentID, dictCode)) {
				throw new EappException("同级下代码不能重复");
			}
			//如果父结点是本身的子结点，刚抛异常
			checkIsChild(dataDictSrc.getChildDataDictionaries(), parentID);
			//修改原父级下的的DisplayOrder，
			dataDictionaryDAO.updateOrder(oldParentModuleID, dataDictSrc.getDisplaySort());
			dataDictSrc.setDisplaySort(new Integer(dataDictionaryDAO.getNextDisplayOrder(subSystemID, parentID)));
			//修改setTreeLevel
			int level = dataDictionaryDAO.getNextTreeLevel(subSystemID, parentID);
			dataDictSrc.setTreeLevel(new Integer(level));
			updateSubDictTreeLevel(dataDictSrc.getChildDataDictionaries(), level, 50);
		}

		if (StringUtils.isBlank(parentID)) {
			//修改的是类型
			dictCode = dictType;
			if (!dictType.equals(dataDictSrc.getDictType())) {
				//类型变掉，要修改所有下级节点的类型
				updateSubDictType(dataDictSrc, dictType);
			}
		}
		dataDictSrc.setCeilValue(ceilValue);
		dataDictSrc.setDescription(description);
		dataDictSrc.setDictName(dictName);
		dataDictSrc.setDictCode(dictCode);
		dataDictSrc.setFloorValue(floorValue);
		
		dataDictionaryDAO.saveOrUpdate(dataDictSrc);
		return dataDictSrc;
	}
	
	private void updateSubDictType(DataDictionary dict, String dictType) {
		dict.setDictType(dictType);
		dataDictionaryDAO.saveOrUpdate(dict);
		Set<DataDictionary> dicts = dict.getChildDataDictionaries();
		if (dicts != null && !dicts.isEmpty()) {
			for (DataDictionary m : dicts) {
				updateSubDictType(m, dictType);
			}
		}
		
	}
	
	private void checkIsChild(Set<DataDictionary> dicts, String parentID) throws EappException {
		if (dicts == null || dicts.isEmpty()) {
			return;
		}
		for (DataDictionary m : dicts) {
			if (m.getDataDictID().equals(parentID)) {
				throw new EappException("父节点不能为该节点的子节点");
			}
			checkIsChild(m.getChildDataDictionaries(), parentID);
		}
	}

	private void updateSubDictTreeLevel(Set<DataDictionary> dicts, int parentLevel, int breakTime) {
		if (dicts == null || dicts.isEmpty() || breakTime-- < 0) {
			return;
		}
		int level = parentLevel + 1;
		for (DataDictionary g : dicts) {
			g.setTreeLevel(new Integer(level));
			updateSubDictTreeLevel(g.getChildDataDictionaries(),level, breakTime);
		}
	}
	
	public void modifyDataDictionarySort(Map<String, Integer> idSort) {
		if(idSort == null){
			throw new IllegalArgumentException();
		}
		Set<String> ids = idSort.keySet();
		for (String dataDictID : ids) {
			DataDictionary dataDict = dataDictionaryDAO.findById(dataDictID);
			Integer order = idSort.get(dataDictID);
			dataDict.setDisplaySort(order);
			dataDictionaryDAO.saveOrUpdate(dataDict);
		}
	}

	/**
	 * 
	 * 立即加载多级模块的子模块
	 * 
	 * @param cols 模块集合
	 */
	private void initLazy(Collection<DataDictionary> cols, int level) {
		if (cols == null || cols.isEmpty()) {
			return;
		}
		for (DataDictionary d : cols) {
			if (level == 0 || d.getTreeLevel() < level) {
				d.getChildDataDictionaries().size();//加载延迟内容
				initLazy(d.getChildDataDictionaries(), level);
			}
		}
	}

	public ListPage<DataDictionary> queryDataDict(DataDictQueryParameters dataDictQP) {
		return dataDictionaryDAO.queryDataDict(dataDictQP);
	}

	public List<DataDictionary> getParentsByDataDict(String dataDictID) {
		if (StringUtils.isBlank(dataDictID)) {
			throw new IllegalArgumentException();
		}
		List<DataDictionary> parentDataDictList = new ArrayList<DataDictionary>();
		DataDictionary dataDict = dataDictionaryDAO.findById(dataDictID);
		if (dataDict == null){
			throw new IllegalArgumentException("参数异常:数据库不存在该条目ID,"+dataDictID);
		}
		DataDictionary parentDataDict = dataDict.getParentDataDictionary();
		while (parentDataDict != null){
			parentDataDictList.add(parentDataDict);
			parentDataDict =parentDataDict.getParentDataDictionary();
		}
		return parentDataDictList;
	}

	public List<DataDictionary> getChildDataDicts(String systemID, String dictCode) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(dictCode)) {
			throw new IllegalArgumentException();
		}
		return dataDictionaryDAO.findDataDictByParentCode(systemID,dictCode);
	}

	public List<DataDictionary> getDataDictList(String systemID, String type) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(type)) {
			throw new IllegalArgumentException();
		}
		return dataDictionaryDAO.findAllDataDictByType(systemID, type, null);
	}

	public List<DataDictionary> getDataDictTree(String systemID, String type) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(type)) {
			throw new IllegalArgumentException();
		}
		return dataDictionaryDAO.findAllDataDictByType(systemID, type, new Integer(2)); 
	}
	

	public DataDictionary getDataDictByCode(String systemID, String dictCode) {
		if (StringUtils.isBlank(systemID) || StringUtils.isBlank(dictCode)) {
			throw new IllegalArgumentException();
		}
		DataDictionary dataDict = dataDictionaryDAO.findDataDictByCode(systemID, dictCode);
		if(dataDict == null){
			throw new IllegalArgumentException("无法找到相应的条目");
		}
		return dataDict;
	}
}
