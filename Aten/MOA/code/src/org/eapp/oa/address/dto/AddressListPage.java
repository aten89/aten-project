package org.eapp.oa.address.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


public class AddressListPage{
	private List<AddressList> addressList;//分页后数据据
	private int pageNo;
	private int pageSize;
	private int totalCount;
	
	
	public AddressListPage(List<AddressList> addressList, int totalCount, int pageNo, int pageSize) {
		this.addressList = addressList;
		this.totalCount = totalCount;
		this.pageNo = pageNo < 1 ? 1 : pageNo;
		this.pageSize = pageSize;
	}
	

	/**
	 * 返回xml格式：
	 * <root>
	 * 	<message code="1" />
	 * 	<content total-count="1" page-size="0" page-count="1" current-page="1" previous-page="false" next-page="false">
	 * 		<record id="">
	 * 			<userAccountId></userAccountId>
	 * 			<userName></userName>
	 * 			<userDeptId></userDeptId>
	 *			<userDeptName></userDeptName>
	 * 			<userPostId></userPostId>
	 * 			<userPostName></userPostName>
	 * 			<userMobile></userMobile>
	 * 			<userContactTel></userContactTel>
	 * 			<userOfficeTel></userOfficeTel>
	 * 			<userEmail></userEmail>
	 * 		</record>
	 * </content>
	 * </root>
	 * @param addressList
	 * @return
	 */
	public Document toDocument(){
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		
		if (addressList == null || addressList.isEmpty()) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		
		Element contentEle = root.addElement("content");
		contentEle.addAttribute("total-count", Long.toString(totalCount));
		contentEle.addAttribute("page-size", Long.toString(pageSize));
		int totalPageCount = (totalCount - 1) / pageSize + 1;
		contentEle.addAttribute("page-count", Long.toString(totalPageCount));
		contentEle.addAttribute("current-page", Long.toString(pageNo));
		contentEle.addAttribute("previous-page", Boolean.toString(pageNo > 1));
		contentEle.addAttribute("next-page", Boolean.toString(pageNo < totalPageCount));
		
		Element addressEle = null;
		for (AddressList addrList : addressList) {
			addressEle = contentEle.addElement("address-list");
			addressEle.addAttribute("id", addrList.getId());//主键
			addressEle.addElement("user-account-id").setText(addrList.getUserAccountId());
			addressEle.addElement("user-name").setText(addrList.getUserName());//姓名
			addressEle.addElement("mobile").setText(DataFormatUtil.noNullValue(addrList.getUserMobile()));//手机
			addressEle.addElement("officeTel").setText(DataFormatUtil.noNullValue(addrList.getUserOfficeTel()));//公司电话
			addressEle.addElement("email").setText(DataFormatUtil.noNullValue(addrList.getUserEmail()));//email
			addressEle.addElement("seatNumber").setText(DataFormatUtil.noNullValue(addrList.getSeatNumber()));
		}
		return doc;
	}
}