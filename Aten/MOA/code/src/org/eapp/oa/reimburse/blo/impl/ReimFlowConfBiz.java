package org.eapp.oa.reimburse.blo.impl;

import java.util.List;

import org.eapp.oa.flow.dao.IFlowConfigDAO;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.reimburse.blo.IReimFlowConfBiz;
import org.eapp.oa.reimburse.dao.IReimFlowConfDAO;
import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.system.exception.OaException;


public class ReimFlowConfBiz implements IReimFlowConfBiz {
	
	private IReimFlowConfDAO  reimFlowConfDAO;
	public IFlowConfigDAO flowConfigDAO;

	public void setReimFlowConfDAO(IReimFlowConfDAO reimFlowConfDAO) {
		this.reimFlowConfDAO = reimFlowConfDAO;
	}
	
	public void setFlowConfigDAO(IFlowConfigDAO flowConfigDAO) {
		this.flowConfigDAO = flowConfigDAO;
	}
	

	public List<ReimFlowConf> getAllReimFlowConfs() {
		List<ReimFlowConf> rfcs = reimFlowConfDAO.findAll();
		if (rfcs == null) {
			return null;
		}
		for (ReimFlowConf rfc : rfcs) {
			if (rfc.getFlowKey() == null) {
				continue;
			}
			FlowConfig fc = flowConfigDAO.findFlowByFlowKey(rfc.getFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
			if (fc != null) {
				rfc.setFlowName(fc.getFlowName());
			}
		}
		return rfcs;
	}
	
	public ReimFlowConf getReimFlowConf(String id) {
		return reimFlowConfDAO.findById(id);
	}

	@Override
	public ReimFlowConf addReimFlowConf(String groupName, String flowKey, String desc) throws OaException {
        if (groupName == null) {
            throw new IllegalArgumentException();
        }
        // 判断分类名称是否重复
        if (!reimFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }

        ReimFlowConf conf = new ReimFlowConf();
        conf.setGroupName(groupName);
//        conf.setFlowClass(flowClass);
        conf.setFlowKey(flowKey);
        conf.setDescription(desc);
        reimFlowConfDAO.save(conf);
        return conf;
	}

	@Override
	public ReimFlowConf deleteReimFlowConf(String id) {
    	ReimFlowConf il = reimFlowConfDAO.findById(id);
        if (il != null) {
            //删除
        	reimFlowConfDAO.delete(il);
        }
        return il;
    }

	    /*
	     * (non-Javadoc)
	     */
	@Override
	public ReimFlowConf modifyReimFlowConf(String id, String groupName, String flowKey, String desc)
	     	throws OaException {
        if (id == null || groupName == null) {
            throw new IllegalArgumentException();
        }
        //根据ID查找信息参数设置
        ReimFlowConf rfc = reimFlowConfDAO.findById(id);
        if (rfc == null) {
            throw new IllegalArgumentException("配置不存在");
        }
        // 判断分类名称是否重复
        if (!rfc.getGroupName().equals(groupName) && !reimFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }
        rfc.setGroupName(groupName);
//        rfc.setFlowClass(flowClass);
        rfc.setFlowKey(flowKey);
        rfc.setDescription(desc);
        reimFlowConfDAO.saveOrUpdate(rfc);
        return rfc;
    }
}
