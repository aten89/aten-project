package org.eapp.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IDataDictionaryBiz;
import org.eapp.blo.ISubSystemBiz;
import org.eapp.dao.param.DataDictQueryParameters;
import org.eapp.dto.DataDictTree;
import org.eapp.dto.DataDictSelect;
import org.eapp.exception.EappException;
import org.eapp.hbean.DataDictionary;
import org.eapp.hbean.SubSystem;
import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.util.hibernate.ListPage;


/**
 * 公开访问的方法
 * @author zsy
 * @version Dec 3, 2008
 */
public class DataDictionaryAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(DataDictionaryAction.class);
	
	
	//服务类
	private IDataDictionaryBiz dataDictionaryBiz;
	private ISubSystemBiz subSystemBiz;
	
	//输入
	private int pageNo;
	private int pageSize;
	private String dataDictID;
	private String parentDataDictID;
	private String dictName;
	private String dictCode;
	private String ceilValue;
	private String floorValue;
	private String dictType;//字典类型
	private String description;
	private String subSystemID;
	private String orderIDs;
	
	//输出
	private DataDictionary dataDict;
	private List<DataDictionary> dataDicts;
	private ListPage<DataDictionary> dataDictPage;
	private String htmlValue;//输出HTML内容
	
	public DataDictionary getDataDict() {
		return dataDict;
	}

	public List<DataDictionary> getDataDicts() {
		return dataDicts;
	}

	public ListPage<DataDictionary> getDataDictPage() {
		return dataDictPage;
	}

	public void setDataDictionaryBiz(IDataDictionaryBiz dataDictionaryBiz) {
		this.dataDictionaryBiz = dataDictionaryBiz;
	}

	public void setSubSystemBiz(ISubSystemBiz subSystemBiz) {
		this.subSystemBiz = subSystemBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setDataDictID(String dataDictID) {
		this.dataDictID = dataDictID;
	}

	public void setParentDataDictID(String parentDataDictID) {
		this.parentDataDictID = parentDataDictID;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public void setCeilValue(String ceilValue) {
		this.ceilValue = ceilValue;
	}

	public void setFloorValue(String floorValue) {
		this.floorValue = floorValue;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSubSystemID(String subSystemID) {
		this.subSystemID = subSystemID;
	}

	public void setOrderIDs(String orderIDs) {
		this.orderIDs = orderIDs;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	/**
	 * 打开方式列表
	 */
	public String loadDictSelect() {
		Map<String, DataDictionary> typeMap = SysCodeDictLoader.getInstance().getDataDictsByType(dictType);
		htmlValue = new DataDictSelect(typeMap.values()).toString();
		return success();
	}
	
	/**
	 * 初始化列表页面,传出子系统列表供选择
	 */
	public String initQueryPage() {
		return success();
	}
	
	/**
	 * 根据KEY模糊查询条目
	 */
	public String queryDataDict() {
		DataDictQueryParameters dataDictQP = new DataDictQueryParameters();
		dataDictQP.setPageSize(pageSize);
		dataDictQP.setPageNo(pageNo);
		if (StringUtils.isNotBlank(dictName)) {
			dataDictQP.setDictName(dictName);
		}
		if (StringUtils.isNotBlank(subSystemID)) {
			dataDictQP.setSubSystemID(subSystemID);
		}
		
		try {
			dataDictPage = dataDictionaryBiz.queryDataDict(dataDictQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化新增页面
	 */
	public String initAdd() {
		if (StringUtils.isBlank(dataDictID)) {
			return error();//转到新增类型页面
		}
		return success();
	}

	/**
	 * 新增数据字典条目
	 */
	public String addDataDict() {
		if (StringUtils.isBlank(subSystemID) || StringUtils.isBlank(dictName)) {
			return error("参数不能为空");
		}
		if (StringUtils.isNotBlank(parentDataDictID) && StringUtils.isBlank(dictCode)) {
			return error("参数不能为空");
		}

		try {
			DataDictionary dataDict = dataDictionaryBiz.addDataDictionary(subSystemID, parentDataDictID, dictName,
					dictCode, ceilValue, floorValue, dictType, description);
			ActionLogger.log(getRequest(), dataDict.getDataDictID(), dataDict.toString());
			return success(dataDict.getDataDictID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化修改页面
	 */
	public String initModify() {
		if (StringUtils.isBlank(dataDictID)) {
			return error("参数不能为空");
		}

		dataDict = dataDictionaryBiz.getDataDictionary(dataDictID);
		int treeLevel = dataDict.getTreeLevel();
		if (treeLevel == 1) {
			return error();//转到新增类型页面
		}
		return success();
	}

	
	/**
	 * 修改数据字典条目
	 */
	public String modifyDataDict() {
		if (StringUtils.isBlank(dataDictID) || StringUtils.isBlank(subSystemID) || StringUtils.isBlank(dictName)) {
			return error("参数不能为空");
		}
		if (StringUtils.isNotBlank(parentDataDictID) && StringUtils.isBlank(dictCode)) {
			return error("参数不能为空");
		}
		
		try {
			DataDictionary dataDict = dataDictionaryBiz.modifyDataDictionary(dataDictID, subSystemID, parentDataDictID, dictName, dictCode, ceilValue, floorValue, dictType, description);
			ActionLogger.log(getRequest(), dataDictID, dataDict.toString());
			return success(dataDict.getDataDictID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 查看页面
	 */
	public String viewDataDict() {
		if (StringUtils.isBlank(dataDictID)) {
			return error("参数不能为空");
		}
		dataDict = dataDictionaryBiz.getDataDictionary(dataDictID);
		int treeLevel = dataDict.getTreeLevel();
		if (treeLevel == 1) {// 如果条目属于第一层则是类型条目,如果类型管理页
			return error();//转到新增类型页面
		}
		return success();
	}
	
	/**
	 * 初始化排序页面
	 */
	public String initOrder() {
		if (StringUtils.isBlank(dataDictID) && StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		
		if (StringUtils.isNotBlank(dataDictID)) {
			dataDicts = new ArrayList<DataDictionary>();
			dataDicts.addAll(dataDictionaryBiz.getChildDataDictsByParentID(dataDictID, 1));
		} else if (StringUtils.isNotBlank(subSystemID)) {// 为子条目排序
			dataDicts = dataDictionaryBiz.getAllTypesBySubSystem(subSystemID);
		}

		return success();
	}
	
	/**
	 * 排序数据字典条目
	 */
	public String orderDataDict() {
		if (StringUtils.isBlank(orderIDs)) {
			return error("参数不能为空");
		}
		String[] dataDictIDs = orderIDs.split(",");
		Map<String, Integer> idSort = new HashMap<String, Integer>();
		for (int i = 0; i < dataDictIDs.length; i++) {
			idSort.put(dataDictIDs[i], i + 1);
		}
		try {
			dataDictionaryBiz.modifyDataDictionarySort(idSort);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}

	}
	
	/**
	 * 删除数据字典条目
	 */
	public String deleteDataDict() {
		if (StringUtils.isBlank(dataDictID)) {
			return error("参数不能为空");
		}
		try {
			DataDictionary dataDict = dataDictionaryBiz.deleteDataDictionary(dataDictID);
			ActionLogger.log(getRequest(), dataDict.getDataDictID(), dataDict.toString());
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 查找条目的父节点路径
	 */
	public String loadParentPath() {
		if (StringUtils.isBlank(dataDictID)) {
			return error("参数不能为空");
		}
		try {
			List<DataDictionary> dataDicts = dataDictionaryBiz.getParentsByDataDict(dataDictID);
			Collections.reverse(dataDicts);
			StringBuffer parents = new StringBuffer();
			for (DataDictionary dataDict : dataDicts) {
				parents.append(dataDict.getDataDictID());
				parents.append("/");
			}
			parents.append(dataDictID);
			return success(parents.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 子系统展开条目
	 */
	public String loadSubSystemTree() {
		if (StringUtils.isBlank(subSystemID)) {
			return error("参数不能为空");
		}
		try {
			SubSystem subSystem = subSystemBiz.getSubSystem(subSystemID);
			List<DataDictionary> dataDicts = dataDictionaryBiz.getAllTypesBySubSystem(subSystemID);
			DataDictTree typeTree = new DataDictTree();
			typeTree.setSubSystem(subSystem);
			typeTree.setDataDicts(dataDicts);
			htmlValue = typeTree.toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 条目展开子条目
	 */
	public String loadDataDictTree() {
		if (StringUtils.isBlank(dataDictID)) {
			return error("参数不能为空");
		}
		try {
			Set<DataDictionary> dataDicts = dataDictionaryBiz.getChildDataDictsByParentID(dataDictID, 2);
			DataDictTree dataDictTree = new DataDictTree();
			dataDictTree.setDataDicts(dataDicts);
			htmlValue = dataDictTree.toString();
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
