package org.eapp.dao;

import org.eapp.dao.param.SysMsgQueryParameters;
import org.eapp.hbean.SysMsg;
import org.eapp.util.hibernate.ListPage;


/**
 * 数据访问接口
 * @version
 */
public interface ISysMsgDAO extends IBaseHibernateDAO {
	
	/**
	 * 根据ID加载
	 * @param id
	 * @return
	 */
	public SysMsg findById(java.lang.String id);

	
	/**
	 * 获得最新未读的
	 * @param moduleId 模块
	 * @return 动作列表
	 */
	public SysMsg findLastNoView(String toAccountID);

	/**
	 * 查找
	 * @param aqp
	 * @return
	 */
	public ListPage<SysMsg> querySysMsgs(SysMsgQueryParameters aqp);
}