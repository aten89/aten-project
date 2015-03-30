package org.eapp.oa.flow.blo;

import java.util.Date;

import org.eapp.oa.system.exception.OaException;


/**
 * 流程数据库操作业务逻辑接口
 * </pre>
 */
public interface IFlowDataBiz {

    /**
     * 通过日期,删除该日期之前的流程相关数据
     * 
     * @param date 日期
     * @throws OaException oa异常
     */
    void deleteFlowData(Date date) throws OaException;

    /**
     * 获取 流程引擎数据库删除完成 标识
     */
    Integer getRunningFlag();
}
