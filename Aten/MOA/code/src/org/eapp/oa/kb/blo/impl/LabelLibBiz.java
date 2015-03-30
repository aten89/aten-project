package org.eapp.oa.kb.blo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eapp.oa.kb.blo.ILabelLibBiz;
import org.eapp.oa.kb.dao.ILabelLibDAO;
import org.eapp.oa.kb.dto.LabelLibQueryParameters;
import org.eapp.oa.kb.hbean.LabelLib;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.exception.OaException;
/**
 * @author 郑凡超
 * 区域管理业务逻辑
 */
public class LabelLibBiz implements ILabelLibBiz {
	
	private ILabelLibDAO labelLibDAO;

	
	public ILabelLibDAO getLabelLibDAO() {
		return labelLibDAO;
	}

	public void setLabelLibDAO(ILabelLibDAO labelLibDAO) {
		this.labelLibDAO = labelLibDAO;
	}

	@Override
	public ListPage<LabelLib> queryLabelLibs(LabelLibQueryParameters qp) {
		return labelLibDAO.queryLabelLib(qp);
		
	}

	@Override
	public void txAddOrModLabelLib(String id, String name, int property) throws OaException {
		LabelLib label = null;
		if (id != null) {
			label = labelLibDAO.findById(id);
		} else {
			label = new LabelLib();
			label.setCreateDate(new Date());
			label.setCount(0l);
		}
		
		if (!name.equals(label.getName())) {
			//验证重复
			List<LabelLib> list = labelLibDAO.findByName(name);
			if (list != null && list.size() > 0) {
				throw new OaException("标签名称不能重复");
			}
		}
		
		label.setName(name);
		label.setProperty(property);
		labelLibDAO.saveOrUpdate(label);
	}
	

	@Override
	public void txDelLabelLibs(String id ,String name, Long startCount,Long endCount) throws OaException {
		labelLibDAO.deleteLabelLib(id, name, startCount, endCount);
	}
	
	public void txAddOrModLabelLibByNames(String[] names){
		for(String name : names){
			LabelLib label = null;
			if(name !=null && name.length() >0){
				List <LabelLib> list = labelLibDAO.findByName(name);
				if(list!=null && list.size() >0){
					label = list.get(0);
					label.setCount(label.getCount() +1);					
				}else{
					label = new LabelLib();
					label.setCreateDate(new Date());
					label.setCount(1L);
					label.setName(name);
				}
				label.setProperty(0);	
				labelLibDAO.saveOrUpdate(label);
			}				
		}
	}
	
	public List<String> getLabelName() {
		List<LabelLib> list = labelLibDAO.findAll();
		List<String> result = new ArrayList<String>();
		for (LabelLib labelLib : list) {
			result.add(labelLib.getName());
		}
		return result;
	}
}
