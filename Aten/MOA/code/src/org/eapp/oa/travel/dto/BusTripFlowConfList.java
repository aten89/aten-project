/**
 * 
 */
package org.eapp.oa.travel.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.travel.hbean.BusTripFlowConf;
import org.eapp.util.web.DataFormatUtil;

/**
 * 输出XML
 * 
 * @author zsy
 * @version Nov 25, 2008
 */
public class BusTripFlowConfList {

    /**
     * 信息公告参数
     */
    private List<BusTripFlowConf> busTripFlowConfs;

    /**
     * 构造函数
     * 
     * @param infoLayouts
     */
    public BusTripFlowConfList(List<BusTripFlowConf> busTripFlowConfs) {
        this.busTripFlowConfs = busTripFlowConfs;
    }

    /**
     * 输出信息公告参数设置的XML <?xml version="1.0" encoding="utf-8" ?> <root> <message code="1" /> <content> <info-layout id="2">
     * <flow-class>2</flow-class> <name>22</name> </info-layout> </content> </root>
     * 
     */
    public Document toDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
        Element root = doc.addElement("root");
        root.addElement("message").addAttribute("code", "1");
        if (busTripFlowConfs == null || busTripFlowConfs.size() < 1) {
            return doc;
        }
        Element contentEle = root.addElement("content");

        Element proEle = null;
        for (BusTripFlowConf f : busTripFlowConfs) {
            proEle = contentEle.addElement("bus-trip-flow");
            proEle.addAttribute("id", f.getId());
            proEle.addElement("group-name").setText(DataFormatUtil.noNullValue(f.getGroupName()));
//            proEle.addElement("flow-class", DataFormatUtil.noNullValue(f.getFlowClass()));
//            proEle.addElement("flow-class-name").setText(DataFormatUtil.noNullValue(f.getFlowClassName()));
            proEle.addElement("flow-key", DataFormatUtil.noNullValue(f.getFlowKey()));
            proEle.addElement("flow-name").setText(DataFormatUtil.noNullValue(f.getFlowName()));
           
            proEle.addElement("description").setText(DataFormatUtil.noNullValue(f.getDescription()));
        }
        return doc;
    }
}
