package org.eapp.poss.config;

import java.net.MalformedURLException;
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
	// prod status
	private Map<String, DataDictInfo> prodStatusMap;
	// sell rank
	private Map<String, DataDictInfo> sellRankMap;
	
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
			
			// prod status
            prodStatusMap = new TreeMap<String, DataDictInfo>();
            List<DataDictInfo> dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.PROD_STATUS);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                    prodStatusMap.put(dict.getDictCode(), dict);
                }
            }
            
            // sell rank
            sellRankMap = new TreeMap<String, DataDictInfo>();
            dicts = service.getDataDictTree(SystemProperties.SYSTEM_ID, SysConstants.SELL_RANK);
            if (dicts != null) {
                for (DataDictInfo dict : dicts) {
                    sellRankMap.put(dict.getDictCode(), dict);
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
	
    public Map<String, DataDictInfo> getProdStatusMap() {
        if (prodStatusMap == null) {
            initDictionary();
        }
        return prodStatusMap;
    }
    
    public Map<String, DataDictInfo> getSellRankMap() {
        if (sellRankMap == null) {
            initDictionary();
        }
        return sellRankMap;
    }
	
}
