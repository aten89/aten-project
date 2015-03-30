/**
 * 
 */
package org.eapp.rpc;

import java.util.List;

import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.exception.RpcAuthorizationException;



/**
 * @version 
 */
public interface IDataDictionaryService {
	/**
	 * 根据KEY取条目实例
	 * @param systemID 子系统KEY
	 * @param key 条目KEY
	 * @return 条目实例
	 * @throws RpcAuthorizationException
	 */
	public DataDictInfo getDataDict(String sessionID, String systemID, String dictCode) throws RpcAuthorizationException;
	
	/**
	 * 根据字典分类取条目集合
	 * @param systemID 子系统KEY 
	 * @param type 字典分类
	 * @return 条目集合,列表型
	 */
	public List<DataDictInfo> getDataDicts(String sessionID, String systemID, String type) throws RpcAuthorizationException;
	
	/**
	 * 根据字典分类取条目集合
	 * @param systemID 子系统KEY 
	 * @param type 字典分类
	 * @return 条目集合,树型
	 */
	public List<DataDictInfo> getDataDictTree(String sessionID, String systemID, String type) throws RpcAuthorizationException;
	
	/**
	 * 根据父节点取子条目集合
	 * @param systemID 子系统KEY
	 * @param dictCode 条目KEY
	 * @param level 级别,如果为0则返回全部,1则返回一层
	 * @return 子条目集合
	 */
	public List<DataDictInfo> getChildDataDicts(String sessionID, String systemID, String dictCode) throws RpcAuthorizationException;
}
