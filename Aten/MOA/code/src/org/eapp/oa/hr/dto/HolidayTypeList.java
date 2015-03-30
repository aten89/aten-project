/**
 * 
 */
package org.eapp.oa.hr.dto;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;


/**
 * @author ZFC
 */
public class HolidayTypeList {
	
	private List<HolidayType> holidayType;
	

	public List<HolidayType> getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(List<HolidayType> holidayType) {
		this.holidayType = holidayType;
	}
	
	public HolidayTypeList(List<HolidayType> holidayType) {
		super();
		this.holidayType = holidayType;
	}

	
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		if (holidayType == null) {
			root.addElement("message").addAttribute("code", "0");
			return doc;
		}
		root.addElement("message").addAttribute("code", "1");
		Element contentEle = root.addElement("content");
		Element proEle = null;
		for (HolidayType r : holidayType) {
			proEle = contentEle.addElement("holiday-type");				
			proEle.addAttribute("id", r.getId());
			proEle.addElement("holiday-name").setText(DataFormatUtil.noNullValue(r.getHolidayName()));
			proEle.addElement("max-days").setText(DataFormatUtil.noNullValue(r.getMaxDays()));
			proEle.addElement("expression").setText(DataFormatUtil.noNullValue(r.getExpression()));
			proEle.addElement("desc").setText(DataFormatUtil.noNullValue(r.getDescription()));
		}
		return doc;
	}
}





