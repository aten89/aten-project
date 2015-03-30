/**
 * 
 */
package org.eapp.oa.kb.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;

/**
 * 
 * 知识点翻页解析对象
 */
public class KnowledgeSearchPage {

    /**
     * 知识点分页对象
     */
    private ListPage<Knowledge> knowledgePage;

    /**
     * 构造函数
     * 
     * @param knowledgePages 知识点分类对象
     */
    public KnowledgeSearchPage(ListPage<Knowledge> knowledgePages) {
        this.knowledgePage = knowledgePages;
    }

    /**
     * 生成doc文档对象
     * 
     * @return doc文档对象
     */
    public Document toDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
        Element root = doc.addElement("root");
        if (knowledgePage == null) {
            root.addElement("message").addAttribute("code", "0");
            return doc;
        }
        root.addElement("message").addAttribute("code", "1");
        Element contentEle = root.addElement("content");
        contentEle.addAttribute("total-count", String.valueOf(knowledgePage.getTotalCount()));
        contentEle.addAttribute("page-size", String.valueOf(knowledgePage.getCurrentPageSize()));
        contentEle.addAttribute("page-count", String.valueOf(knowledgePage.getTotalPageCount()));
        contentEle.addAttribute("current-page", String.valueOf(knowledgePage.getCurrentPageNo()));
        contentEle.addAttribute("previous-page", String.valueOf(knowledgePage.hasPreviousPage()));
        contentEle.addAttribute("next-page", String.valueOf(knowledgePage.hasNextPage()));

        List<Knowledge> kbs = knowledgePage.getDataList();
        if (kbs != null && !kbs.isEmpty()) {
            Element ele = null;
            for (Knowledge kb : kbs) {
                ele = contentEle.addElement("knowledge");
                ele.addAttribute("id", kb.getId());
                ele.addElement("subject").setText(DataFormatUtil.noNullValue(kb.getSubject()));
                ele.addElement("match-subject").addCDATA(DataFormatUtil.noNullValue(kb.getMatchSubject()));
                ele.addElement("match-text").addCDATA(DataFormatUtil.noNullValue(kb.getMatchText()));
                ele.addElement("match-class-path-name").addCDATA(kb.getMatchClassPathName());
                ele.addElement("labels").addCDATA(DataFormatUtil.noNullValue(kb.getLabels()));
                ele.addElement("apply-date").setText(
                        DataFormatUtil.noNullValue(kb.getPublishDate(), SysConstants.STANDARD_TIME_PATTERN));
//                ele.addElement("modify-date").setText(
//                        DataFormatUtil.noNullValue(kb.getModifyDate(), SysConstants.STANDARD_DATE_PATTERN));
                ele.addElement("group").setText(DataFormatUtil.noNullValue(kb.getGroupName()));
//                ele.addElement("quoteTime").setText(DataFormatUtil.noNullValue(kb.getQuoteTime()));
                ele.addElement("publisher").setText(DataFormatUtil.noNullValue(kb.getPublisherName()));
//                ele.addElement("lastModifiedBy").setText(DataFormatUtil.noNullValue(kb.getLastModifiedByName()));
                String content = kb.getContent();
                //去掉HTML元素
                if(StringUtils.isNotEmpty(content)){
                    content = content.replaceAll("<[^>]*>", "");
                }
                ele.addElement("content").setText(DataFormatUtil.noNullValue(content));

            }
        }
        return doc;
    }
}
