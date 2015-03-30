package org.eapp.oa.flow.blo.impl;

import java.util.Calendar;
import java.util.Date;

import org.eapp.oa.flow.blo.IFlowDataBiz;
import org.eapp.oa.flow.dao.IFlowDataDAO;
import org.eapp.oa.system.exception.OaException;


/**
 * 流程数据库操作业务逻辑实现
 */
public class FlowDataBiz implements IFlowDataBiz {

    /**
     * 流程引擎 数据库操作 DAO 接口
     */
    private IFlowDataDAO flowDataDAO;
    
    /**
     * 设置 flowDataDAO
     * 
     * @param flowDataDAO the flowDataDAO to set
     */
    public void setFlowDataDAO(IFlowDataDAO flowDataDAO) {
        this.flowDataDAO = flowDataDAO;
    }
    

    @Override
    public void deleteFlowData(Date date) throws OaException {
        // 日期不为空
        if (date == null) {
            throw new IllegalArgumentException("选取的日期不能为空!");
        }
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.YEAR, -1);
        if (ca.getTime().before(date)) {
            throw new OaException("选取的日期应该在当前日期一年之前.");
        }
        // 删除流程引擎 相关数据
        flowDataDAO.deleteFlowData(date);
    }

    @Override
    public Integer getRunningFlag() {
        return flowDataDAO.getRunningFlag();
    }
}
