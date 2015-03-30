/**
 * 
 */
package org.eapp.rpc.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eapp.blo.IDataDictionaryBiz;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.hbean.DataDictionary;
import org.eapp.rpc.IDataDictionaryService;
import org.eapp.rpc.session.RPCAuthorizationFilter;
import org.eapp.sys.config.SysConstants;

import com.caucho.hessian.server.HessianServlet;

/**
 * @version
 */
public class DataDictionaryService extends HessianServlet implements IDataDictionaryService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1312864028619931972L;
	private static final String MODULE_KEY = "dataDict";
	private IDataDictionaryBiz dataDictionaryBiz;
	
	public void setDataDictionaryBiz(IDataDictionaryBiz dataDictionaryBiz) {
		this.dataDictionaryBiz = dataDictionaryBiz;
	}

	@Override
	public List<DataDictInfo> getChildDataDicts(String sessionID, String systemID, String dictCode)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getChildDataDicts 参数:sessionID="+sessionID+",systemID="+systemID);
		List<DataDictionary> childDataDicts = dataDictionaryBiz.getChildDataDicts(systemID, dictCode);
		
		List<DataDictInfo> dataDictDTOs = new ArrayList<DataDictInfo>();
		for(DataDictionary dataDict:childDataDicts){
			dataDictDTOs.add(copy(dataDict));
		}
		return dataDictDTOs;
	}

	@Override
	public List<DataDictInfo> getDataDicts(String sessionID, String systemID, String type)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getDataDictListByType 参数:sessionID="+sessionID+",systemID="+systemID+",type="+type);
		List<DataDictionary> dataDicts = dataDictionaryBiz.getDataDictList(systemID, type);
		
		List<DataDictInfo> dataDictDTOs = new ArrayList<DataDictInfo>();
		for(DataDictionary dataDict:dataDicts){
			dataDictDTOs.add(copy(dataDict));
		}
		return dataDictDTOs;
	}

	@Override
	public List<DataDictInfo> getDataDictTree(String sessionID, String systemID, String type)
			throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.QUERY);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getDataDictTreeByType 参数:sessionID="+sessionID+",systemID="+systemID+",type="+type);
		List<DataDictionary> dataDicts = dataDictionaryBiz.getDataDictTree(systemID, type);
		List<DataDictInfo> dataDictDTOs = copy(dataDicts);
		return dataDictDTOs;
	}

	@Override
	public DataDictInfo getDataDict(String sessionID, String systemID, String dictCode) throws RpcAuthorizationException {
		RPCAuthorizationFilter.authorize(sessionID,SysConstants.EAPP_SUBSYSTEM_ID,MODULE_KEY, SysConstants.VIEW);
//		InterfaceLogger.log(sessionID, MODULE_KEY, SysConstants.QUERY, null,"方法:getValueByKey 参数:sessionID="+sessionID+",systemID="+systemID+",key="+key);
		DataDictionary dataDict = dataDictionaryBiz.getDataDictByCode(systemID, dictCode);
		return copy(dataDict);
	}
	
	private DataDictInfo copy(DataDictionary from){
		DataDictInfo to = new DataDictInfo();
		to.setDataDictID(from.getDataDictID());
		if (from.getParentDataDictionary() != null) {
			to.setParentDataDictID(from.getParentDataDictionary().getDataDictID());
		}
		if (from.getSubSystem() != null) {
			to.setSubSystemID(from.getSubSystem().getSubSystemID());
		}
		to.setDictName(from.getDictName());
		to.setDictCode(from.getDictCode());
		to.setCeilValue(from.getCeilValue());
		to.setFloorValue(from.getFloorValue());
		to.setDisplaySort(from.getDisplaySort());
		to.setDictType(from.getDictType());
		to.setTreeLevel(from.getTreeLevel());
		to.setDescription(from.getDescription());
		return to;
	}
	
	private List<DataDictInfo> copy(Collection<DataDictionary> dataDicts) {
		if (dataDicts == null || dataDicts.size() < 1) {
			return null;
		}
		List<DataDictInfo> dataDictDTOs = new ArrayList<DataDictInfo>();
		for(DataDictionary dataDict : dataDicts) {
			DataDictInfo dto = copy(dataDict);
			dataDictDTOs.add(dto);
			dto.setChildDataDicts(copy(dataDict.getChildDataDictionaries()));
		}
		return dataDictDTOs;
	}
}
