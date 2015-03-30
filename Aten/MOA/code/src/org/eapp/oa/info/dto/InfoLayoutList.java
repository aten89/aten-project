/**
 * 
 */
package org.eapp.oa.info.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;

/**
 * 信息公告参数设置输出XML
 * 
 * @author zsy
 * @version Nov 25, 2008
 * 
 *          <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-25	李海根	注释修改
 * </pre>
 */
public class InfoLayoutList {

    /**
     * 信息公告参数
     */
    private List<InfoLayout> infoLayouts;

    /**
     * 构造函数
     * 
     * @param infoLayouts
     */
    public InfoLayoutList(List<InfoLayout> infoLayouts) {
        this.infoLayouts = infoLayouts;
    }

    /**
     * 输出信息公告参数设置的XML <?xml version="1.0" encoding="utf-8" ?> <root> <message code="1" /> <content> <info-layout id="2">
     * <flow-class>2</flow-class> <name>22</name> </info-layout> </content> </root>
     * 
     * @return
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-25	李海根	注释修改
     * </pre>
     */
    public Document toDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
        Element root = doc.addElement("root");
        root.addElement("message").addAttribute("code", "1");
        if (infoLayouts == null || infoLayouts.size() < 1) {
            return doc;
        }
        Element contentEle = root.addElement("content");

        Element proEle = null;
        for (InfoLayout f : infoLayouts) {
            proEle = contentEle.addElement("info-layout");
            proEle.addAttribute("id", f.getId());
            proEle.addElement("flow-class", DataFormatUtil.noNullValue(f.getFlowClass()));
            proEle.addElement("flow-class-name").setText(DataFormatUtil.noNullValue(f.getFlowClassName()));
            proEle.addElement("name").setText(DataFormatUtil.noNullValue(f.getName()));
            // 增加是否邮件通知字段
//            proEle.addElement("isEmail").setText(DataFormatUtil.noNullValue(f.getIsEmail()));
            proEle.addElement("description").setText(DataFormatUtil.noNullValue(f.getDescription()));
        }
        return doc;
    }
}
