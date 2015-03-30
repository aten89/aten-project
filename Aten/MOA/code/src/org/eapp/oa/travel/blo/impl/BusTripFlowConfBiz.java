package org.eapp.oa.travel.blo.impl;

import java.util.List;

import org.eapp.oa.flow.dao.IFlowConfigDAO;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.travel.blo.IBusTripFlowConfBiz;
import org.eapp.oa.travel.dao.IBusTripFlowConfDAO;
import org.eapp.oa.travel.hbean.BusTripFlowConf;


public class BusTripFlowConfBiz implements IBusTripFlowConfBiz {
	
	private IBusTripFlowConfDAO  busTripFlowConfDAO;
	public IFlowConfigDAO flowConfigDAO;

	public void setBusTripFlowConfDAO(IBusTripFlowConfDAO busTripFlowConfDAO) {
		this.busTripFlowConfDAO = busTripFlowConfDAO;
	}

	public void setFlowConfigDAO(IFlowConfigDAO flowConfigDAO) {
		this.flowConfigDAO = flowConfigDAO;
	}
	

	public List<BusTripFlowConf> getAllBusTripFlowConfs() {
		List<BusTripFlowConf> rfcs = busTripFlowConfDAO.findAll();
		if (rfcs == null) {
			return null;
		}
		for (BusTripFlowConf rfc : rfcs) {
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
	
	public BusTripFlowConf getBusTripFlowConf(String id) {
		return busTripFlowConfDAO.findById(id);
	}

	@Override
	public BusTripFlowConf addBusTripFlowConf(String groupName, String flowKey, String desc) throws OaException {
        if (groupName == null) {
            throw new IllegalArgumentException();
        }
        // 判断分类名称是否重复
        if (!busTripFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }

        BusTripFlowConf conf = new BusTripFlowConf();
        conf.setGroupName(groupName);
//        conf.setFlowClass(flowClass);
        conf.setFlowKey(flowKey);
        conf.setDescription(desc);
        busTripFlowConfDAO.save(conf);
        return conf;
	}

	@Override
	public BusTripFlowConf deleteBusTripFlowConf(String id) {
		BusTripFlowConf il = busTripFlowConfDAO.findById(id);
        if (il != null) {
            //删除
        	busTripFlowConfDAO.delete(il);
        }
        return il;
    }

	    /*
	     * (non-Javadoc)
	     */
	@Override
	public BusTripFlowConf modifyBusTripFlowConf(String id, String groupName, String flowKey, String desc)
	     	throws OaException {
        if (id == null || groupName == null) {
            throw new IllegalArgumentException();
        }
        //根据ID查找信息参数设置
        BusTripFlowConf rfc = busTripFlowConfDAO.findById(id);
        if (rfc == null) {
            throw new IllegalArgumentException("配置不存在");
        }
        // 判断分类名称是否重复
        if (!rfc.getGroupName().equals(groupName) && !busTripFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }
        rfc.setGroupName(groupName);
//        rfc.setFlowClass(flowClass);
        rfc.setFlowKey(flowKey);
        rfc.setDescription(desc);
        busTripFlowConfDAO.saveOrUpdate(rfc);
        return rfc;
    }
}
