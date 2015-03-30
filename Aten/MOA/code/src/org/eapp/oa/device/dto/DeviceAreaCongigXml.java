package org.eapp.oa.device.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceAreaCongigXml {

	private ListPage<AreaDeviceCfg> listPage;
	private List<AreaDeviceCfg> list;

	public DeviceAreaCongigXml(ListPage<AreaDeviceCfg> listPage){
		this.listPage=listPage;
	}
	
	public DeviceAreaCongigXml(List<AreaDeviceCfg> list){
		this.list=list;
	}
	
	
	/**
	 *
	 * 返回格式
	 * <?xml version="1.0" encoding="utf-8" ?> 
	 * <root>
	 * 	<message code="1"/>
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<propertyList id="">
	 * 			<deviceProperty-id>属性 编号</reimbursement-id>
	 *			<property-name>属性名称</property-name>
	 * 			<property-remark>备注</property-remark>
	 * 		</propertyList>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toPageDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (listPage == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", String.valueOf(listPage.getTotalCount()) );
		contentEle.addAttribute("page-size", String.valueOf(listPage.getCurrentPageSize()) );
		contentEle.addAttribute("page-count", String.valueOf(listPage.getTotalPageCount()) );
		contentEle.addAttribute("current-page", String.valueOf(listPage.getCurrentPageNo()));
		contentEle.addAttribute("previous-page", String.valueOf(listPage.hasPreviousPage()));
		contentEle.addAttribute("next-page", String.valueOf(listPage.hasNextPage()) );		
		List<AreaDeviceCfg>  deviceClassList = listPage.getDataList();
		if(deviceClassList != null && deviceClassList.size() > 0){
//			Map<String ,DataDictInfo> devUseTypeMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
			Element proEle = null;
			for (AreaDeviceCfg d : deviceClassList) {
				proEle = contentEle.addElement("area-config");				
				proEle.addAttribute("id", d.getId());
				proEle.addElement("area-name").setText( DataFormatUtil.noNullValue(d.getAreaName()));
				if(d.getDeviceClass()!=null){
					proEle.addElement("class-name").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getName()));
					proEle.addElement("device-typename").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getDeviceTypeName()));
				}
//				Set<DeviceAcptCountCfg> deviceAcptCountCfgs = d.getDeviceAcptCountCfgs();
//				if(deviceAcptCountCfgs!=null && deviceAcptCountCfgs.size()>0){
//					String devPuerposeStr="";
//					for (DeviceAcptCountCfg deviceAcptCountCfg : deviceAcptCountCfgs) {
//						if(devUseTypeMap.containsKey(deviceAcptCountCfg.getDevPurpose())){
//							devPuerposeStr+=devUseTypeMap.get(deviceAcptCountCfg.getDevPurpose()).getDictName();
//							if(deviceAcptCountCfg.getManyTimesFlag()){
//								devPuerposeStr+="（<span class=\"cRed\">可领用多次</span>）";
//							}
//							devPuerposeStr+="<br/>";
//						}
//					}
//					proEle.addElement("config-devPuerpose").setText(DataFormatUtil.noNullValue(devPuerposeStr));
//					
//				}
				proEle.addElement("config-viewcode").setText(DataFormatUtil.noNullValue(d.getViewCode()));
				proEle.addElement("config-useApply").setText(DataFormatUtil.noNullValue(d.getUseApplyFlowName()));
				proEle.addElement("config-allocate").setText(DataFormatUtil.noNullValue(d.getAllocateFlowName()));
				proEle.addElement("config-discard").setText(DataFormatUtil.noNullValue(d.getDiscardFlowName()));
				proEle.addElement("config-dimission").setText(DataFormatUtil.noNullValue(d.getDimissionFlowName()));
				proEle.addElement("config-mainDevFlag").setText(DataFormatUtil.noNullValue(d.getMainDevFlag()));
			}
		}
		return doc;
	}		
	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (list == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		if(list != null && list.size() > 0){
			Element proEle = null;
			for (AreaDeviceCfg d : list) {
				proEle = contentEle.addElement("area-config");				
				proEle.addAttribute("id", d.getId());
				proEle.addElement("area-name").setText( DataFormatUtil.noNullValue(d.getAreaName()));
				if(d.getDeviceClass()!=null){
					proEle.addElement("class-name").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getName()));
					proEle.addElement("class-remark").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getRemark()));
					proEle.addElement("type-name").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getDeviceTypeName()));
					proEle.addElement("class-id").setText(DataFormatUtil.noNullValue(d.getDeviceClass().getId()));
				}
				proEle.addElement("config-viewcode").setText(DataFormatUtil.noNullValue(d.getViewCode()));
				proEle.addElement("config-useApply").setText(DataFormatUtil.noNullValue(d.getUseApplyFlowName()));
				proEle.addElement("config-allocate").setText(DataFormatUtil.noNullValue(d.getAllocateFlowName()));
				proEle.addElement("config-discard").setText(DataFormatUtil.noNullValue(d.getDiscardFlowName()));
				
			}
		}
		return doc;
	}		

	public ListPage<AreaDeviceCfg> getListPage() {
		return listPage;
	}



	public void setListPage(ListPage<AreaDeviceCfg> listPage) {
		this.listPage = listPage;
	}

	public List<AreaDeviceCfg> getList() {
		return list;
	}

	public void setList(List<AreaDeviceCfg> list) {
		this.list = list;
	}
}
