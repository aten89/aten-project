package org.eapp.oa.reimburse.blo.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.eapp.oa.reimburse.blo.IOutlayListBiz;
import org.eapp.oa.reimburse.dao.IOutlayListDAO;
import org.eapp.oa.reimburse.dto.OutlayListQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.util.hibernate.ListPage;


public class OutlayListBiz implements IOutlayListBiz {
	
	private IOutlayListDAO  outlayListDAO;

	public IOutlayListDAO getOutlayListDAO() {
		return outlayListDAO;
	}

	public void setOutlayListDAO(IOutlayListDAO outlayListDAO) {
		this.outlayListDAO = outlayListDAO;
	}
	

	public StringBuffer findConflict (List<OutlayList> list , List<OutlayList> allList ,String travelBeginDate,String travelEndDate){
		StringBuffer sb = null;
		Map<String,String> map = new HashMap<String, String>();
		
		for (OutlayList o : list) {
			for (OutlayList ol : allList) {
				if(o.getReimItem().getId().equals(ol.getReimItem().getId())){
					continue;
				}else{
					if(o.getOutlayCategory().equals(ol.getOutlayCategory()) && o.getOutlayName().equals(ol.getOutlayName()) && o.getReimItem().getTravelEndDate()!=null && o.getReimItem().getTravelBeginDate()!=null && ol.getReimItem().getTravelEndDate()!=null && ol.getReimItem().getTravelBeginDate()!=null ){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
						try {
							boolean b = ! (sdf.format( o.getReimItem().getTravelEndDate()).compareTo(sdf.format( ol.getReimItem().getTravelBeginDate())) < 0 || sdf.format(o.getReimItem().getTravelBeginDate()).compareTo(sdf.format( ol.getReimItem().getTravelEndDate())) > 0);
							if (b) {
								if(sb == null){
									sb = new StringBuffer("以下费用明细发现冲突：\n");
								}
								if(map.get("“"+ o.getOutlayCategory()+"/"+ o.getOutlayName() +"”") == null){
									map.put("“"+ o.getOutlayCategory()+"/"+ o.getOutlayName() +"”", ol.getReimbursement().getId()+",");
								}else{
									String s = map.get("“"+ o.getOutlayCategory()+"/"+ o.getOutlayName() +"”");
									s += ol.getReimbursement().getId()+",";
									map.put("“"+ o.getOutlayCategory()+"/"+ o.getOutlayName() +"”", s);
								}
							} else { 
								continue;
							} 
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}else{
						continue;
					}
				}
			}
		}
		  Iterator<String> it= map.keySet().iterator();
		  while (it.hasNext())
		  {
			  String key=it.next();
			  String value=map.get(key);
			  sb.append(key + "与"+ value + "号报销单的出差日期冲突！\n");
		  }
		return sb;
	}
	public List<OutlayList> getOutlayListByReimItemID(String id){
		List<OutlayList> list = outlayListDAO.getOutlayListByReimItemID(id);
		return list;
		
	}
	
	public List<OutlayList> checkOutlayList(String accountId){
		List<OutlayList> list = outlayListDAO.checkOutlayList(accountId);
		return list;
	}
	@Override
	public ListPage<OutlayList> queryOutlayList(OutlayListQueryParameters oqp) {
		if(oqp == null){
			throw new IllegalArgumentException("非法参数:OutlayListQueryParameters参数为null");
		}
		ListPage<OutlayList> outlayListPage = outlayListDAO.queryOutlayListDAO(oqp);
		
		return outlayListPage;
	}
	@Override
	public ListPage<OutlayList> queryOutlayList(OutlayListQueryParameters oqp,String financeId) {
		if(oqp == null){
			throw new IllegalArgumentException("非法参数:OutlayListQueryParameters参数为null");
		}
		ListPage<OutlayList> outlayListPage = outlayListDAO.queryOutlayListDAO(oqp,financeId);
//		if(outlayListPage!=null&&outlayListPage.getDataList()!=null&&outlayListPage.getDataList().size()>0&&outlayListPage.getDataList().get(0).getReimbursement()!=null){
//			Hibernate.initialize(outlayListPage.getDataList().get(0).getReimbursement().getBudget());
//		}
		return outlayListPage;
	}

	@Override
	public double getStatOutlay(OutlayListQueryParameters oqp) {
		if(oqp == null){
			throw new IllegalArgumentException("非法参数:OutlayListQueryParameters参数为null");
		}
		return outlayListDAO.findStatOutlay(oqp);
	}
	@Override
	public double getStatOutlay(OutlayListQueryParameters oqp,String financeId) {
		if(oqp == null){
			throw new IllegalArgumentException("非法参数:OutlayListQueryParameters参数为null");
		}
		return outlayListDAO.findStatOutlay(oqp,financeId);
	}
	
	@Override
	public List<OutlayList> getSubOutlayListByOutlayCategory(String outlayCategoryId,String dictKey, String budgetID) {
		if (budgetID == null || outlayCategoryId == null && dictKey == null) {
			return null;
		}
		return outlayListDAO.findOutlayList(outlayCategoryId, dictKey, budgetID);
	}
	
	@Override
	public  Map<String,Double>  getReimbursement(String custName, String projectCode) {
		return outlayListDAO.countReimNumOfProject(custName, projectCode);
	}
}
