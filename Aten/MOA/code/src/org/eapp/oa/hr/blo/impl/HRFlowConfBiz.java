package org.eapp.oa.hr.blo.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.flow.dao.IFlowConfigDAO;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.oa.hr.blo.IHRFlowConfBiz;
import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.exception.OaException;


public class HRFlowConfBiz implements IHRFlowConfBiz {
	
	private IHRFlowConfDAO  hrFlowConfDAO;
	public IFlowConfigDAO flowConfigDAO;

	public void setHrFlowConfDAO(IHRFlowConfDAO hrFlowConfDAO) {
		this.hrFlowConfDAO = hrFlowConfDAO;
	}
	
	public void setFlowConfigDAO(IFlowConfigDAO flowConfigDAO) {
		this.flowConfigDAO = flowConfigDAO;
	}
	

	public List<HRFlowConf> getAllHolidayFlowConfs() {
		List<HRFlowConf> rfcs = hrFlowConfDAO.findAll();
		if (rfcs == null) {
			return null;
		}
		FlowConfig fc = null;
		for (HRFlowConf rfc : rfcs) {
			if (StringUtils.isNotBlank(rfc.getHolidayFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getHolidayFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setHolidayFlowName(fc.getFlowName());
				}
			}
			if (StringUtils.isNotBlank(rfc.getCanHolidayFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getCanHolidayFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setCanHolidayFlowName(fc.getFlowName());
				}
			}
			if (StringUtils.isNotBlank(rfc.getResignFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getResignFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setResignFlowName(fc.getFlowName());
				}
			}
			if (StringUtils.isNotBlank(rfc.getEntryFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getEntryFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setEntryFlowName(fc.getFlowName());
				}
			}
			if (StringUtils.isNotBlank(rfc.getTransferFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getTransferFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setTransferFlowName(fc.getFlowName());
				}
			}
			if (StringUtils.isNotBlank(rfc.getPositiveFlowKey())) {
				fc = flowConfigDAO.findFlowByFlowKey(rfc.getPositiveFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
				if (fc != null) {
					rfc.setPositiveFlowName(fc.getFlowName());
				}
			}
		}
		return rfcs;
	}
	
	public HRFlowConf getHolidayFlowConf(String id) {
		return hrFlowConfDAO.findById(id);
	}

	@Override
	public HRFlowConf addHolidayFlowConf(String groupName, String holidayFlowKey, String canHolidayFlowKey, 
			String entryFlowKey, String resignFlowKey, String transferFlowKey, String positiveFlowKey, String desc) throws OaException {
        if (groupName == null) {
            throw new IllegalArgumentException();
        }
        // 判断分类名称是否重复
        if (!hrFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }

        HRFlowConf conf = new HRFlowConf();
        conf.setGroupName(groupName);
//        conf.setFlowClass(flowClass);
        conf.setHolidayFlowKey(holidayFlowKey);
        conf.setCanHolidayFlowKey(canHolidayFlowKey);
        conf.setEntryFlowKey(entryFlowKey);
        conf.setResignFlowKey(resignFlowKey);
        conf.setTransferFlowKey(transferFlowKey);
        conf.setPositiveFlowKey(positiveFlowKey);
        conf.setDescription(desc);
        hrFlowConfDAO.save(conf);
        return conf;
	}

	@Override
	public HRFlowConf deleteHolidayFlowConf(String id) {
		HRFlowConf il = hrFlowConfDAO.findById(id);
        if (il != null) {
            //删除
        	hrFlowConfDAO.delete(il);
        }
        return il;
    }

	    /*
	     * (non-Javadoc)
	     */
	@Override
	public HRFlowConf modifyHolidayFlowConf(String id, String groupName, String holidayFlowKey, String canHolidayFlowKey, 
			String entryFlowKey, String resignFlowKey, String transferFlowKey, String positiveFlowKey, String desc) throws OaException {
        if (id == null || groupName == null) {
            throw new IllegalArgumentException();
        }
        //根据ID查找信息参数设置
        HRFlowConf rfc = hrFlowConfDAO.findById(id);
        if (rfc == null) {
            throw new IllegalArgumentException("配置不存在");
        }
        // 判断分类名称是否重复
        if (!rfc.getGroupName().equals(groupName) && !hrFlowConfDAO.checkGroupName(groupName)) {
        	throw new OaException("部门已配置");
        }
        rfc.setGroupName(groupName);
//        rfc.setFlowClass(flowClass);
        rfc.setHolidayFlowKey(holidayFlowKey);
        rfc.setCanHolidayFlowKey(canHolidayFlowKey);
        rfc.setEntryFlowKey(entryFlowKey);
        rfc.setResignFlowKey(resignFlowKey);
        rfc.setTransferFlowKey(transferFlowKey);
        rfc.setPositiveFlowKey(positiveFlowKey);
        rfc.setDescription(desc);
        hrFlowConfDAO.saveOrUpdate(rfc);
        return rfc;
    }
}
