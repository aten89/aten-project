package org.eapp.oa.kb.dto;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;


/**
 * 
 * @author Tim
 * @version Jun 26, 2009
 */
public class KnowledgeReplyPage {

    /**
     * 回复翻页对象
     */
    private ListPage<KnowledgeReply> replyPage;

    /**
     * 构造方法
     * 
     * @param replyPages 回复翻页对象
     */
    public KnowledgeReplyPage(ListPage<KnowledgeReply> replyPages) {
        this.replyPage = replyPages;
    }

    /**
     * 生成xml文档
     * 
     * @return xml文档
     * 
     *         <pre>
     * 修改日期	 修改人	修改原因
     * 2012-8-30	卢凯宁		修改注释
     * </pre>
     */
    public Document toDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
        Element root = doc.addElement("root");
        if (replyPage == null) {
            root.addElement("message").addAttribute("code", "0");
            return doc;
        }
        root.addElement("message").addAttribute("code", "1");
        Element contentEle = root.addElement("content");
        contentEle.addAttribute("total-count", String.valueOf(replyPage.getTotalCount()));
        contentEle.addAttribute("page-size", String.valueOf(replyPage.getCurrentPageSize()));
        contentEle.addAttribute("page-count", String.valueOf(replyPage.getTotalPageCount()));
        contentEle.addAttribute("current-page", String.valueOf(replyPage.getCurrentPageNo()));
        contentEle.addAttribute("previous-page", String.valueOf(replyPage.hasPreviousPage()));
        contentEle.addAttribute("next-page", String.valueOf(replyPage.hasNextPage()));

        List<KnowledgeReply> krbs = replyPage.getDataList();
        if (krbs != null && krbs.size() > 0) {
            Element ele = null;
            for (KnowledgeReply kb : krbs) {
                ele = contentEle.addElement("knowledge_reply");
                ele.addAttribute("id", kb.getId());
                ele.addElement("reply_man").setText(DataFormatUtil.noNullValue(kb.getReplyManName()));
                ele.addElement("accountID").setText(DataFormatUtil.noNullValue(kb.getReplyMan()));
                ele.addElement("reply_content").setText(DataFormatUtil.noNullValue(kb.getContent()));
                ele.addElement("group").setText(DataFormatUtil.noNullValue(kb.getGroupName()));
                ele.addElement("reply_date").setText(
                        DataFormatUtil.noNullValue(kb.getReplyDate(), SysConstants.STANDARD_TIME_PATTERN));
            }
        }
        return doc;
    }
}
