package org.eapp.poss.blo.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.hessian.GroupService;
import org.eapp.poss.blo.IConfirmExtendBiz;
import org.eapp.poss.dao.IConfirmExtendDAO;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.param.ConfirmExtendQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ConfirmExtend;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.util.hibernate.ListPage;

public class ConfirmExtendBiz implements IConfirmExtendBiz {
	private IConfirmExtendDAO confirmExtendDAO;
	private IProdInfoDAO prodInfoDAO;
	
	public void setConfirmExtendDAO(IConfirmExtendDAO confirmExtendDAO) {
		this.confirmExtendDAO = confirmExtendDAO;
	}

	public void setProdInfoDAO(IProdInfoDAO prodInfoDAO) {
		this.prodInfoDAO = prodInfoDAO;
	}
	
	@Override
	public ConfirmExtend getConfirmExtend(String contractId) {
		return confirmExtendDAO.findById(contractId);
	}
	
	@Override
	public void addConfirmExtend(String prodId, String orgName, Integer custNums, Integer confirmNums, 
			String expressName, String expressNo, String remark,
			String regUser, String regDept) throws PossException {
		ConfirmExtend req = new ConfirmExtend();
		ProdInfo prodInfo = prodInfoDAO.findById(prodId);
		req.setProdInfo(prodInfo);
		req.setOrgName(orgName);
		req.setCustNums(custNums);
		req.setConfirmNums(confirmNums);
		req.setExpressName(expressName);
		req.setExpressNo(expressNo);
		req.setRemark(remark);
		req.setRegUser(regUser);
		req.setRegDept(regDept);
		req.setRegDate(new Date());
		confirmExtendDAO.save(req);
	}
	
	@Override
	public void modifyConfirmExtend(String id, Integer custNums, Integer confirmNums, 
			String expressName, String expressNo, String remark) throws PossException {
		ConfirmExtend req = confirmExtendDAO.findById(id);
		if (req == null) {
			throw new IllegalArgumentException("对象不存在");
		}
		req.setCustNums(custNums);
		req.setConfirmNums(confirmNums);
		req.setExpressName(expressName);
		req.setExpressNo(expressNo);
		req.setRemark(remark);
		confirmExtendDAO.update(req);
	}
	
	@Override
	public void deleteConfirmExtend(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("非法参数：ID为空");
		}
		ConfirmExtend supp = confirmExtendDAO.findById(id);
		if (supp == null) {
			return;
		}
		confirmExtendDAO.delete(supp);
	}
	
	@Override
	public ListPage<ConfirmExtend> queryConfirmExtendPage(ConfirmExtendQueryParameters qp) {
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        return confirmExtendDAO.findPage(qp);
	}

	@Override
	public List<String> getOrgNames() {
		GroupService gs = new GroupService();
		try {
			List<GroupInfo> gis = gs.getChildGroups(null, null);
			if (gis != null) {
				List<String> orgNames = new ArrayList<String>();
				for (GroupInfo gi : gis) {
					orgNames.add(gi.getGroupName());
				}
				return orgNames;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
