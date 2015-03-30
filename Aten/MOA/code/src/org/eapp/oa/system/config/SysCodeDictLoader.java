package org.eapp.oa.system.config;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eapp.client.hessian.DataDictionaryService;
import org.eapp.client.hessian.SubSystemService;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.rpc.dto.SubSystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author zsy
 * @version
 */
public class SysCodeDictLoader {

	private static Logger log = LoggerFactory.getLogger(SysCodeDictLoader.class);

	private static SysCodeDictLoader singleton;
	
	private SubSystemInfo currentSysConfig;//当前信息信息
	private Map<String, DataDictInfo> flowClass;//流程分类
	private Map<String, DataDictInfo> docSecurityClass;//公文密级
	private Map<String, DataDictInfo> docUrgency;//公文缓急
	
	private Map<String, DataDictInfo> companyArea;//所属地区
	private Map<String, DataDictInfo> costClassNameMap;//所属地区
	
	private List<DataDictInfo> deviceTypeList;//设备类型
	private Map<String, DataDictInfo> deviceTypeMap; //设备类型
	private List<DataDictInfo> deviceValiType; //验证类型
	
//	private List<DataDictInfo> devUseTypeList;//设备用途
//	private Map<String, DataDictInfo> devUseTypeMap; //设备用途
//	private Map<String, List<DataDictInfo>> devUseTypesMapbyKey; //设备用途
	
	private List<DataDictInfo> allotTypeList;//调拨类型
	private Map<String, DataDictInfo> allotTypeMap;//调拨类型
	private Map<String, List<DataDictInfo>> allotTypeMapByKey;//调拨类型Map
	
	private Map<String, DataDictInfo> buyType; //购买类型
	private Map<String, List<DataDictInfo>> buyTypeMapByKey; //购买类型
	
	private List<DataDictInfo> scrapTypeList;//报废类型
	private Map<String, DataDictInfo> scrapTypeMap;//报废类型Map
	
	private List<DataDictInfo> scrapDisposeTypeList;//报废类型处理方式
	private Map<String, DataDictInfo> scrapDisposeTypeMap;//报废类型处理方式Map
	private Map<String, List<DataDictInfo>> scrapDisposeTypeMapByKey;//报废类型处理方式Map
	
	private List<DataDictInfo> leaveDealTypeList;
	private Map<String, DataDictInfo> leaveDealTypeMap;
	private Map<String, List<DataDictInfo>> leaveDealTypeMapByKey;
	
	private Map<String, DataDictInfo> firstTypeMap; //知识库一级分类
	private Map<String, DataDictInfo> secondTypeMap; //知识库二级分类
	
	
	public static SysCodeDictLoader getInstance() {
		if (singleton == null) {
			singleton = new SysCodeDictLoader();
		}
		return singleton;
	}

	private SysCodeDictLoader() {
		
	}

	/**
	 * 进行初始化数字字典操作
	 * 
	 * @return
	 */
	public boolean initAllCodeDict() {
		try {
			initDictionary();//从ERMP读取字典
			return true;
		} catch (Exception e) {
			log.error("启动初始化出错。", e);
		}
		return false;
	}
	
	
	
	/**
	 * 从ERMP读取字典
	 */
	public void initDictionary() {
		DataDictionaryService service = new DataDictionaryService();
		try {
			SubSystemService sysCer = new SubSystemService();
			//当前子系统
			currentSysConfig = sysCer.getSubSystemInfo(SysConstants.SYSTEM_ID);
			//加载流程分类
			flowClass = new TreeMap<String, DataDictInfo>();
			List<DataDictInfo> dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.FLOW_CLASS_KEY);
			//流程分类只有一级，取得第一级封装成Map
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					flowClass.put(d.getDictCode(), d);
				}
			}
			
			//加载公文密级
			docSecurityClass = new TreeMap<String, DataDictInfo>();
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.DOC_SECURITY_KEY);
			//密级分类只有一级，取得第一级封装成Map
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					docSecurityClass.put(d.getDictCode(), d);
				}
			}
			
			//加载公文缓急
			docUrgency = new TreeMap<String, DataDictInfo>();
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.DOC_URGENCY_KEY);
			//缓急分类只有一级，取得第一级封装成Map
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					docUrgency.put(d.getDictCode(), d);
				}
			}
			
			//所属地区
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.COMPANY_AREA_KEY);
			companyArea = new TreeMap<String, DataDictInfo>();
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					companyArea.put(d.getDictCode(), d);
				}
			}
			
			//费用类别
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.COST_CLASS_KEY);
			costClassNameMap = new TreeMap<String, DataDictInfo>();
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					costClassNameMap.put(d.getDictName(), d);
				}
			}
			
			//资产类别
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.DEVICE_TYPE_KEY);
			deviceTypeList = dicts;
			//加载设备类型
			deviceTypeMap = new HashMap<String, DataDictInfo>();
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					deviceTypeMap.put(d.getDictCode(), d);
				}
			}
			
			//加载设备验收单类型
			deviceValiType = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.DEVICE_VALIDATE_KEY);
			
			//加载设备用途
//			devUseTypeList = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.DEV_USETYPE_KEY);
//			devUseTypeMap = new HashMap<String, DataDictInfo>();
//			devUseTypesMapbyKey = new HashMap<String, List<DataDictInfo>>();
//			//缓急分类只有一级，取得第一级封装成Map
//			if (devUseTypeList != null) {
//				for (DataDictInfo d : devUseTypeList) {
//					devUseTypeMap.put(d.getDictCode(), d);
//					List<DataDictInfo> devUseTypes = d.getChildDataDicts();
//					if (devUseTypes != null) {
//						List<DataDictInfo> list = new ArrayList<DataDictInfo>();
//						for (DataDictInfo dictionaryDTO : devUseTypes) {
//								if(dictionaryDTO.getParentDataDictID().equals(d.getDataDictID())){
//									list.add(dictionaryDTO);
//									devUseTypeMap.put(dictionaryDTO.getDictCode(), dictionaryDTO);
//								}
//							
//						}
//						devUseTypesMapbyKey.put(d.getDictCode(), list);
//					}
//				}
//			}
			
			
			//加载调拨类型
			allotTypeList = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.ALLOT_TYPE_KEY);
			allotTypeMap = new HashMap<String, DataDictInfo>();
			allotTypeMapByKey = new HashMap<String, List<DataDictInfo>>();
			//缓急分类只有一级，取得第一级封装成Map
			if (allotTypeList != null) {
				for (DataDictInfo d : allotTypeList) {
					allotTypeMap.put(d.getDictCode(), d);
					List<DataDictInfo> devUseTypes = d.getChildDataDicts();
					if (devUseTypes != null) {
						List<DataDictInfo> list = new ArrayList<DataDictInfo>();
						for (DataDictInfo dictionaryDTO : devUseTypes) {
								if(dictionaryDTO.getParentDataDictID().equals(d.getDataDictID())){
									list.add(dictionaryDTO);
									allotTypeMap.put(dictionaryDTO.getDictCode(), dictionaryDTO);
								}
							
						}
						allotTypeMapByKey.put(d.getDictCode(), list);
					}
				}
			}
			
			//加载设备购买类型
			buyType = new HashMap<String, DataDictInfo>();
			buyTypeMapByKey = new HashMap<String, List<DataDictInfo>>();
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.BUY_TYPE_KEY);
			//缓急分类只有一级，取得第一级封装成Map
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					buyType.put(d.getDictCode(), d);
					List<DataDictInfo> devBuyTypes = d.getChildDataDicts();
					if (devBuyTypes != null) {
						List<DataDictInfo> list = new ArrayList<DataDictInfo>();
						for (DataDictInfo dictionaryDTO : devBuyTypes) {
								if(dictionaryDTO.getParentDataDictID().equals(d.getDataDictID())){
									list.add(dictionaryDTO);
									buyType.put(dictionaryDTO.getDictCode(), dictionaryDTO);
								}
							
						}
						buyTypeMapByKey.put(d.getDictCode(), list);
					}
				}
			}
			
			
			scrapTypeList = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.SCRAP_TYPE_KEY);
			scrapTypeMap = new HashMap<String, DataDictInfo>();
			if (scrapTypeList != null) {
				for (DataDictInfo d : scrapTypeList) {
					scrapTypeMap.put(d.getDictCode(), d);
				}
			}
			
			scrapDisposeTypeList = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.SCRAP_DISPOSE_TYPE_KEY);
			scrapDisposeTypeMap = new HashMap<String, DataDictInfo>();
			scrapDisposeTypeMapByKey = new HashMap<String, List<DataDictInfo>>();
			//缓急分类只有一级，取得第一级封装成Map
			if (scrapDisposeTypeList != null) {
				for (DataDictInfo d : scrapDisposeTypeList) {
					scrapDisposeTypeMap.put(d.getDictCode(), d);
					List<DataDictInfo> devUseTypes = d.getChildDataDicts();
					if (devUseTypes != null) {
						List<DataDictInfo> list = new ArrayList<DataDictInfo>();
						for (DataDictInfo dictionaryDTO : devUseTypes) {
								if(dictionaryDTO.getParentDataDictID().equals(d.getDataDictID())){
									list.add(dictionaryDTO);
									scrapDisposeTypeMap.put(dictionaryDTO.getDictCode(), dictionaryDTO);
								}
							
						}
						scrapDisposeTypeMapByKey.put(d.getDictCode(), list);
					}
				}
			}
			
			leaveDealTypeList = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.LEAVE_DEAL_TYPE_KEY);//离职处理方式
			leaveDealTypeMap = new HashMap<String, DataDictInfo>();
			leaveDealTypeMapByKey = new HashMap<String, List<DataDictInfo>>();
			//缓急分类只有一级，取得第一级封装成Map
			if (leaveDealTypeList != null) {
				for (DataDictInfo d : leaveDealTypeList) {
					leaveDealTypeMap.put(d.getDictCode(), d);
					List<DataDictInfo> devUseTypes = d.getChildDataDicts();
					if (devUseTypes != null) {
						List<DataDictInfo> list = new ArrayList<DataDictInfo>();
						for (DataDictInfo dictionaryDTO : devUseTypes) {
								if(dictionaryDTO.getParentDataDictID().equals(d.getDataDictID())){
									list.add(dictionaryDTO);
									leaveDealTypeMap.put(dictionaryDTO.getDictCode(), dictionaryDTO);
								}
							
						}
						leaveDealTypeMapByKey.put(d.getDictCode(), list);
					}
				}
			}
			
			//加载一级分类
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.KB_FIRST_TYPE_KEY);
			firstTypeMap = new HashMap<String, DataDictInfo>();
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					firstTypeMap.put(d.getDictCode(), d);
				}
			}
			//加载二级分类
			dicts = service.getDataDictTree(SysConstants.SYSTEM_ID, SysConstants.KB_SECOND_TYPE_KEY);
			secondTypeMap = new HashMap<String, DataDictInfo>();
			if (dicts != null) {
				for (DataDictInfo d : dicts) {
					secondTypeMap.put(d.getDictCode(), d);
				}
			}
			
			log.info("init dictDate OK");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RpcAuthorizationException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 当前子系统信息
	 * @return
	 */
	public SubSystemInfo getCurrentSysConfig() {
		if (currentSysConfig == null) {
			initDictionary();
		}
		return currentSysConfig;
	}

	/**
	 * 取得流程分类
	 * @return
	 */
	public Map<String, DataDictInfo> getFlowClass() {
		if (flowClass == null) {
			initDictionary();
		}
		return flowClass;
	}
	
	/**
	 * 通过Key取得流程分类
	 * @param key
	 * @return
	 */
	public DataDictInfo getFlowClassByKey(String value) {
		return getFlowClass().get(value);
	}
	/**
	 * 公文密级
	 * @return
	 */
	public Map<String, DataDictInfo> getDocSecurityClass() {
		return docSecurityClass;
	}
	/**
	 * 公文缓急
	 * @return
	 */
	public Map<String, DataDictInfo> getDocUrgency() {
		return docUrgency;
	}
	
	/**
	 * 区域
	 * @return
	 */
	public Map<String, DataDictInfo> getAreaMap() {
		return companyArea;
	}
	
	/**
	 * 费用类别
	 * @return
	 */
	public Map<String, DataDictInfo> getCostClassMap() {
		return costClassNameMap;
	}
	
	/**
	 * 设备类别
	 * @return
	 */
	public List<DataDictInfo> getDeviceTypeList() {
		return deviceTypeList;
	}
	
	/**
	 * 设备类别
	 * @return
	 */
	public Map<String, DataDictInfo> getDeviceType() {
		return deviceTypeMap;
	}
	
	/**
	 * 设备验收单类型
	 * @return List<DataDictionaryDTO>
	 */
	public List<DataDictInfo> getDeviceValiType(){
		return deviceValiType;
	}
	
	/**
	 * 获取设备验收单类型属性
	 * @param dictKey
	 * @return List<DataDictionaryDTO>
	 */
	public List<DataDictInfo> getSubDeviceValiType(String dictKey) {
		if (getDeviceValiType() == null || dictKey == null) {
			return null;
		}
		for (DataDictInfo d : getDeviceValiType()) {
			if (dictKey.equals(d.getDictCode())) {
				return d.getChildDataDicts();
			}
		}
		return null;
	}
	
//	public List<DataDictInfo> getDevUseTypeList() {
//		return devUseTypeList;
//	}
//	
//	public Map<String, DataDictInfo> getDevUseTypeMap() {
//		return devUseTypeMap;
//	}
//	public Map<String, List<DataDictInfo>> getDevUseTypesMapbyKey() {
//		return devUseTypesMapbyKey;
//	}
	
	/**
	 * 获取调拨类型
	 * @return
	 */
	public List<DataDictInfo> getAllotTypeList() {
		return allotTypeList;
	}
	
	public Map<String, DataDictInfo> getAllotTypeMap() {
		return allotTypeMap;
	}
	
	public Map<String, List<DataDictInfo>> getAllotTypeMapByKey() {
		return allotTypeMapByKey;
	}
	
	/**
	 * 取得购买类型
	 * @return List<DataDictionaryDTO>
	 */
	public Map<String, DataDictInfo> getBuyType() {
		return buyType;
	}
	public Map<String, List<DataDictInfo>> getBuyTypeMapByKey() {
		return buyTypeMapByKey;
	}
	
	public List<DataDictInfo> getScrapTypeList() {
		return scrapTypeList;
	}

	public Map<String, DataDictInfo> getScrapTypeMap() {
		return scrapTypeMap;
	}
	
	public List<DataDictInfo> getScrapDisposeTypeList() {
		return scrapDisposeTypeList;
	}
	
	public Map<String, DataDictInfo> getScrapDisposeTypeMap() {
		return scrapDisposeTypeMap;
	}
	public Map<String, List<DataDictInfo>> getScrapDisposeTypeMapByKey() {
		return scrapDisposeTypeMapByKey;
	}
	
	public List<DataDictInfo> getLeaveDealTypeList() {
		return leaveDealTypeList;
	}
	public Map<String, DataDictInfo> getLeaveDealTypeMap() {
		return leaveDealTypeMap;
	}
	public Map<String, List<DataDictInfo>> getLeaveDealTypeMapByKey() {
		return leaveDealTypeMapByKey;
	}
	
	/**
	 * 获取一级分类
	 * @return List<DataDictionaryDTO>
	 */
	public Map<String, DataDictInfo> getFirstTypeMap(){
		return firstTypeMap;
	}
	
	/**
	 * 获取二级分类
	 * @return List<DataDictionaryDTO>
	 */
	public Map<String, DataDictInfo> getSecondTypeMap(){
		return secondTypeMap;
	}
}
