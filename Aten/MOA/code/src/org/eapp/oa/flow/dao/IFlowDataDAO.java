package org.eapp.oa.flow.dao;

import java.util.Date;


/**
 * 流程数据库 操作DAO接口,不使用 OA 系统的 Hibernate ,使用 流程引擎 中 的 WfmConfiguration 获取 session 手动关闭.
 */
public interface IFlowDataDAO {

    /**
     * 获取 流程引擎数据库删除完成 标识 返回 0:正在执行 1:执行成功 -1:执行失败
     * 
     * @return Integer
     */
    Integer getRunningFlag();

    /**
     * 删除 指定日期 之前 的流程相关数据
     * 
     * @param date 指定日期

     */
    void deleteFlowData(Date date);
}
