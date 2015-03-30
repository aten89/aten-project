/**
 * 
 */
package org.eapp.client.hessian;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.SystemProperties;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.rpc.IDataDictionaryService;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * @author jasonwong
 *
 */
public class DataDictionaryService extends BaseEAPPService {
	private static Log log = LogFactory.getLog(DataDictionaryService.class);
	
	private static IDataDictionaryService service;
	
	private IDataDictionaryService getService() throws MalformedURLException{
		if (service == null) {
			synchronized (DataDictionaryService.class) {
				service = (IDataDictionaryService) factory.create(IDataDictionaryService.class, getServiceUrl(SystemProperties.SERVICE_DATA_DICTIONARY));
			}
		}
		return service;
	}
	
	/**
	 * 根据KEY取条目实例
	 * @param systemID 子系统KEY
	 * @param key 条目KEY
	 * @return 条目实例
	 * @throws IllegalArgumentException:子系统KEY或者条目KEY为空
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 * @throws RPCLoginException
	 */
	public DataDictInfo getDataDict(String systemID, String key) throws
			RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getDataDict(getDefaultSessionID(false), systemID, key);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getDataDict(getDefaultSessionID(true), systemID, key);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 根据字典分类取条目集合
	 * @param systemID 子系统KEY 
	 * @param type 字典分类
	 * @return 条目集合,列表型
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 * @throws RPCLoginException
	 */
	public List<DataDictInfo> getDataDicts(String systemID, String type)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getDataDicts(getDefaultSessionID(false), systemID, type);
		} catch(RpcAuthorizationException e) {
			log.warn("帐号登陆超时,重新登录");
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				return getService().getDataDicts(getDefaultSessionID(true), systemID, type);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 根据字典分类取条目集合
	 * @param systemID 子系统KEY 
	 * @param type 字典分类
	 * @return 条目集合,树型
	 * @throws RpcAuthorizationException
	 * @throws MalformedURLException
	 * @throws RPCLoginException
	 */
	public List<DataDictInfo> getDataDictTree(String systemID, String type)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getDataDictTree(getDefaultSessionID(false), systemID, type);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getDataDictTree(getDefaultSessionID(true), systemID, type);
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 根据父节点取子条目集合
	 * @param systemID 子系统KEY
	 * @param dataDictKey 条目KEY
	 * @param level 级别,如果为0则返回全部,1则返回一层
	 * @return 子条目集合
	 * @throws RPCAuthorizationException
	 * @throws MalformedURLException
	 * @throws RPCLoginException
	 */
	public List<DataDictInfo> getChildDataDicts(String systemID, String dataDictKey)
			throws RpcAuthorizationException, MalformedURLException {
		try {
			return getService().getChildDataDicts(getDefaultSessionID(false), systemID, dataDictKey);
		} catch(RpcAuthorizationException e) {
			if (RpcAuthorizationException.CODE_OVERTIME == e.getCode()) {
				log.warn("帐号登陆超时,重新登录");
				return getService().getChildDataDicts(getDefaultSessionID(true), systemID, dataDictKey);
			} else {
				throw e;
			}
		}
	}
}
