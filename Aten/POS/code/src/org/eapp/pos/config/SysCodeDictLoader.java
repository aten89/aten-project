package org.eapp.pos.config;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.DataDictionaryService;
import org.eapp.client.hessian.SubSystemService;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.rpc.dto.SubSystemInfo;



/**
 * @author zsy
 * @version
 */
public class SysCodeDictLoader {

	private static final Log log = LogFactory.getLog(SysCodeDictLoader.class);

	private static SysCodeDictLoader singleton;
	
	private SubSystemInfo currentSysConfig;//当前信息信息
//	private Map<String, DataDictInfo> flowClass;//流程分类
	private Map<String, DataDictInfo> productMap;//产品
	private Map<String, DataDictInfo> sexMap;//性别
	private Map<String, DataDictInfo> customerNatureMap;//性别
	private Map<String, DataDictInfo> commTypeMap;//沟通类型
	private Map<String, DataDictInfo> appointmentTypeMap;//提醒方式
	
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
			currentSysConfig = sysCer.getSubSystemInfo(SystemProperties.SYSTEM_ID);
			//加载流程分类
//			flowClass = new HashMap<String, DataDictInfo>();
//			List<DataDictInfo> dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.FLOW_CLASS_KEY);
//			//流程分类只有一级，取得第一级封装成Map
//			if (dicts != null) {
//				for (DataDictInfo d : dicts) {
//					flowClass.put(d.getDictCode(), d);
//				}
//			}
			
			// 产品
			productMap = new LinkedHashMap<String, DataDictInfo>();
			List<DataDictInfo> dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.PRODUCT);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                	productMap.put(dict.getDictCode(), dict);
                }
            }
            
            // 性别
        	sexMap = new TreeMap<String, DataDictInfo>();
			dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.SEX);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                	sexMap.put(dict.getDictCode(), dict);
                }
            }
            
            // 客户性质
            customerNatureMap = new TreeMap<String, DataDictInfo>();
			dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.CUSTOMER_NATURE);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                	customerNatureMap.put(dict.getDictCode(), dict);
                }
            }
            
            commTypeMap = new TreeMap<String, DataDictInfo>();
            dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.COMM_TYPE);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                	commTypeMap.put(dict.getDictCode(), dict);
                }
            }
            //提醒方式
            appointmentTypeMap = new TreeMap<String, DataDictInfo>();
            dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.APPOINTMENT_TYPE);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                	appointmentTypeMap.put(dict.getDictCode(), dict);
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
//	public Map<String, DataDictInfo> getFlowClass() {
//		if (flowClass == null) {
//			initDictionary();
//		}
//		return flowClass;
//	}
	
	/**
	 * 获取产品
	 * @return
	 */
	public Map<String, DataDictInfo> getProductMap() {
		if (productMap == null) {
			initDictionary();
		}
        return productMap;
    }
	
	/**
	 * 获取性别
	 * @return
	 */
	public Map<String, DataDictInfo> getSexMap() {
		if (sexMap == null) {
			initDictionary();
		}
        return sexMap;
    }
	
	/**
	 * 获取客户性质
	 * @return
	 */
	public Map<String, DataDictInfo> getCustomerNatureMap() {
		if (customerNatureMap == null) {
			initDictionary();
		}
        return customerNatureMap;
    }
	
	/**
	 * 获取沟通类型
	 * @return
	 */
	public Map<String, DataDictInfo> getCommTypeMap() {
		if (commTypeMap == null) {
			initDictionary();
		}
        return commTypeMap;
    }
	
	/**
	 * 获取提醒类型
	 * @return
	 */
	public Map<String, DataDictInfo> getAppointmentTypeMap() {
		if (appointmentTypeMap == null) {
			initDictionary();
		}
		return appointmentTypeMap;
	}
	
	
}
