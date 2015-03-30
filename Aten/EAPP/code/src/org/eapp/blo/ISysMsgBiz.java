/**
 * 
 */
package org.eapp.blo;

import org.eapp.dao.param.SysMsgQueryParameters;
import org.eapp.hbean.SysMsg;
import org.eapp.util.hibernate.ListPage;


/**
 * @version
 */
public interface ISysMsgBiz {

	/**
	 * 查看信息
	 */
	public SysMsg getAction(String id);
	
	/**
	 * 获得最新未读的
	 */
	public SysMsg txGetLastNoView(String toAccountID);
	
	/**
	 * 新增
	 */
	public SysMsg addSysMsg(String fromSystemID, String msgSender, String toAccountID, String msgContent);

	public void deleteSysMsgs(String[] msgIDs);
	
	public void modifyViewFlag(String msgID);
	
	public ListPage<SysMsg> querySysMsgs(SysMsgQueryParameters aqp);
	

}
