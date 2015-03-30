package org.eapp.oa.kb.blo;

import org.eapp.oa.kb.dto.KnowledgeLogQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.util.hibernate.ListPage;

/**
 * 知识点日志业务逻辑接口
 */
public interface IKnowledgeLogBiz {
	
    /**
     * 获取操作日志分页对象
     * @param queryParameters queryParameters分页查询对象
     * @return ListPage<KnowledgeLog>分页对象
     */
	public ListPage<KnowledgeLog> getKnowledgeLogListPage(KnowledgeLogQueryParameters queryParameters);
	
	/**
	 * 新增操作日志
	 * @param userid 用户id
	 * @param type 操作类型
	 * @param knowledgeid 知识点id
	 * @param knowledgetitle 知识点标题
	 * @return 知识点日志
	 */
	public KnowledgeLog addKnowledgeLog(String userid, String type, String knowledgeid, String knowledgetitle);

}
