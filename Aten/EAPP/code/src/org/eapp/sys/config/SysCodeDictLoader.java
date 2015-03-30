package org.eapp.sys.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IDataDictionaryBiz;
import org.eapp.hbean.DataDictionary;
import org.eapp.util.spring.SpringHelper;


/**
 * 
 * @author zsy
 * @version
 */
public class SysCodeDictLoader {

	private static final Log log = LogFactory.getLog(SysCodeDictLoader.class);

	private static SysCodeDictLoader singleton;

	private IDataDictionaryBiz dataDictBiz;
//	private IModuleBiz moduleBiz;
	
	// 数据字典MAP
	private HashMap<String, Map<String, DataDictionary>> dataDictsMap;
	// 模块KEY,NAME的对应
//	private HashMap<String, String> moduleMap;

	public static SysCodeDictLoader getInstance() {
		if (singleton == null) {
			singleton = new SysCodeDictLoader();
		}
		return singleton;
	}

	private SysCodeDictLoader() {
		dataDictBiz = (IDataDictionaryBiz)SpringHelper.getBean("dataDictionaryBiz");
	}

	/**
	 * 进行初始化数字字典操作
	 * 
	 * @return
	 */
	public boolean initAllCodeDict() {
		try {
			initDataDict();
//			initModuleMap();
			return true;
		} catch (Exception e) {
			log.error("启动初始化出错。", e);
		}
		return false;
	}

	/**
	 * 初始化数据字典
	 */
	public void initDataDict() {
		HashMap<String, Map<String, DataDictionary>> dataDictsMapBak = new HashMap<String, Map<String, DataDictionary>>();
		List<DataDictionary> typeLists = dataDictBiz.getAllTypesBySubSystem(SysConstants.EAPP_SUBSYSTEM_ID);
		for (DataDictionary typeDataDict : typeLists) {
			String type = typeDataDict.getDictType();
			List<DataDictionary> dataDictLists = dataDictBiz.getAllDataDictByType(SysConstants.EAPP_SUBSYSTEM_ID, type);
			Map<String, DataDictionary> dataMap = new TreeMap<String, DataDictionary>();
			for (DataDictionary dataDict : dataDictLists) {
				if (dataDict.getTreeLevel() > 1) {
					dataMap.put(dataDict.getDictCode(), dataDict);
				}
			}
			dataDictsMapBak.put(type, dataMap);
		}
		dataDictsMap = dataDictsMapBak;
	}
	
	/**
	 * 初始化模块对应图
	 */
//	public void initModuleMap() {
//		List<Module> modules = moduleBiz.getModulesBySubSystem(SysConstants.EAPP_SUBSYSTEM_ID);
//		for(Module module:modules){
//			moduleMap.put(module.getModuleKey(), module.getName());
//		}
//	}


	public Map<String, DataDictionary> getDataDictsByType(String type) {
		return dataDictsMap.get(type);
	}
	
}
