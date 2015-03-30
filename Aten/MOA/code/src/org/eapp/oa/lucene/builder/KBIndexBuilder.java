/**
 * 
 */
package org.eapp.oa.lucene.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.eapp.oa.kb.blo.IKnowledgeBiz;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.lucene.HtmlParser;
import org.eapp.oa.lucene.IndexBuilder;
import org.eapp.oa.lucene.IndexException;
import org.eapp.oa.lucene.IndexSession;
import org.eapp.oa.lucene.IndexTask;
import org.eapp.oa.lucene.IndexTaskRunner;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;


/**
 * 知识库索引类
 */
public abstract class KBIndexBuilder extends IndexBuilder {

    private Log log = LogFactory.getLog(KBIndexBuilder.class);
    /**
     * 知识分类
     */
    public static final String FIELDNAME_KNOWLEDGECLASS = "knowledgeClass";
    /**
     * 知识分类名称
     */
    public static final String FIELDNAME_KNOWLEDGECLASSNAME = "knowledgeClassName";
    /**
     * 标题
     */
    public static final String FIELDNAME_SUBJECT = "subject";
    /**
     * labels
     */
    public static final String FIELDNAME_LABELS = "labels";
    /**
     * remark
     */
    public static final String FIELDNAME_REMARK = "remark";
    /**
     * 内容
     */
    public static final String FIELDNAME_CONTENT = "content";
    /**
     * groupName
     */
    public static final String FIELDNAME_GROUPNAME = "groupName";
    /**
     * 发布者
     */
    public static final String FIELDNAME_PUBLISHER = "publisher";
    /**
     * 发布者姓名
     */
    public static final String FIELDNAME_PUBLISHERNAME = "publisherName";
    /**
     * 第一分类
     */
    public static final String FIELDNAME_FIRSTTYPE = "firstType";
    /**
     * 第二分类
     */
    public static final String FIELDNAME_SECONDTYPE = "secondType";
    

    @Override
    protected void doBuildIndexs(Collection<IndexTask> indexTasks) throws IndexException {
        IKnowledgeBiz knowledgeBiz = (IKnowledgeBiz) SpringHelper.getSpringContext().getBean("knowledgeBiz");
        List<String> ids = new ArrayList<String>();
        for (IndexTask it : indexTasks) {
            ids.add(it.getDocumentId());
        }
        List<Knowledge> knowledges = knowledgeBiz.getKnowledgeByIds(ids);

        IndexSession session = null;
        try {
            session = new IndexSession(indexDir);
            session.addIndex(makeDocuments(knowledges));
        } catch (IndexException e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    protected void doDeleteIndexs(Collection<IndexTask> indexTasks) throws IndexException {
        IndexSession session = null;
        try {
            session = new IndexSession(indexDir);
            for (IndexTask it : indexTasks) {
                session.deleteIndex(new Term(FIELDNAME_DOCUMENT_ID, it.getDocumentId()));
            }
        } catch (IndexException e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    /**
     * 获取知识点类型 正式 临时
     * 
     * @return int 类型
     */
    protected Integer getKnowledgeStatus() {
        return null;
    }

    /**
     * 重建索引
     */
    public void reBuildIndex() {
        IKnowledgeBiz knowledgeBiz = (IKnowledgeBiz) SpringHelper.getSpringContext().getBean("knowledgeBiz");
        KnowledgeQueryParameters kqp = new KnowledgeQueryParameters();
        kqp.setPageNo(1);
        // 一次查找3000条
        kqp.setPageSize(3000);
        kqp.setStatus(getKnowledgeStatus());
        // 添加查询条件的排序
        kqp.addOrder("id", false);
        ListPage<Knowledge> page = knowledgeBiz.queryKnowledge(kqp);
        if (page.getTotalCount() == 0) {
            return;
        }

        IndexSession session = null;
        try {
            // 暂停更新线程
            IndexTaskRunner.setPause(true); 
            session = new IndexSession(indexDir);
            // 删除当前索引目录
            session.deleteAllIndex();
            // 添加第一页索引
            List<Directory> dirs = new ArrayList<Directory>();
            Directory dir = session.createRAMDirectory(makeDocuments(page.getDataList()));
            dirs.add(dir);

            long totalPageCount = page.getTotalPageCount();
            // 如果只有一页返回
            if (totalPageCount > 1) { 
                // 循环创建第二页以后的索引
                for (int i = 2; i < totalPageCount; i++) {
                    kqp.setPageNo(i);
                    page = knowledgeBiz.queryKnowledge(kqp);

                    dir = session.createRAMDirectory(makeDocuments(page.getDataList()));
                    dirs.add(dir);
                    if (dirs.size() > 10) {
                        // 合并索引
                        session.mergeIndexs(dirs);
                        dirs.clear();
                    }
                }
            }

            if (dirs.size() > 0) {
                // 合并索引
                session.mergeIndexs(dirs);
            }
            // 优化索引
            session.optimize();
        } catch (IndexException e) {
            session.rollback();
            log.error("reBuildIndex faild", e);
        } finally {
            IndexTaskRunner.setPause(false); // 暂停更新线程
            session.close();
        }
    }

    /**
     * makeDocuments
     * 
     * @param knowledges 知识点
     * @return List<Document>集合
     */
    private List<Document> makeDocuments(List<Knowledge> knowledges) {
        List<Document> docs = new ArrayList<Document>();
        for (Knowledge knowledge : knowledges) {
            docs.add(makeDocument(knowledge));
        }
        return docs;
    }

    /**
     * 将知识库内容转成lucene Document
     * 
     * @param knowledge 知识点
     * @return Document 文档
     */
    private Document makeDocument(Knowledge knowledge) {
        if (knowledge == null) {
            return null;
        }
        Document doc = new Document();
        Field field = new Field(FIELDNAME_DOCUMENT_ID, knowledge.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        doc.add(field);
        field = new Field(FIELDNAME_DOCUMENT_TYPE, documentType, Field.Store.YES, Field.Index.NOT_ANALYZED);
        doc.add(field);
        if (knowledge.getKnowledgeClass() != null) {
            field = new Field(FIELDNAME_KNOWLEDGECLASS, knowledge.getKnowledgeClass().getId(), Field.Store.YES,
                    Field.Index.NOT_ANALYZED);
            field.setBoost(1f);
            doc.add(field);
            field = new Field(FIELDNAME_KNOWLEDGECLASSNAME, knowledge.getClassPathName().toLowerCase(), Field.Store.NO,
                    Field.Index.ANALYZED);
            doc.add(field);
        }
        if (knowledge.getSubject() != null) {
            field = new Field(FIELDNAME_SUBJECT, knowledge.getSubject().toLowerCase(), Field.Store.NO,
                    Field.Index.ANALYZED);
            field.setBoost(2F); // 关键字设置权重为2
            doc.add(field);
        }
        if (knowledge.getLabels() != null) {
            field = new Field(FIELDNAME_LABELS, knowledge.getLabels().toLowerCase(), Field.Store.NO,
                    Field.Index.ANALYZED);
            field.setBoost(5F); // 关键字设置权重为5
            doc.add(field);
        }
        if (knowledge.getRemark() != null) {
            field = new Field(FIELDNAME_REMARK, knowledge.getRemark().toLowerCase(), Field.Store.NO,
                    Field.Index.ANALYZED);
            field.setBoost(2F);
            doc.add(field);
        }
        if (knowledge.getContent() != null) {
            String content = HtmlParser.extractText(knowledge.getContent());
            if (content != null) {
                field = new Field(FIELDNAME_CONTENT, content.toLowerCase(), Field.Store.NO, Field.Index.ANALYZED);
            }
            doc.add(field);
        }
        if (knowledge.getGroupName() != null) {
            field = new Field(FIELDNAME_GROUPNAME, knowledge.getGroupName().toLowerCase(), Field.Store.NO,
                    Field.Index.ANALYZED);
            doc.add(field);
        }
        if (knowledge.getPublisher() != null) {
            field = new Field(FIELDNAME_PUBLISHER, knowledge.getPublisher().toLowerCase(), Field.Store.NO,
                    Field.Index.NOT_ANALYZED);
            doc.add(field);
            field = new Field(FIELDNAME_PUBLISHERNAME, knowledge.getPublisherName(), Field.Store.NO,
                    Field.Index.NOT_ANALYZED);
            doc.add(field);
        }
        if (knowledge.getFirstType() != null) {
            field = new Field(FIELDNAME_FIRSTTYPE, knowledge.getFirstType(), Field.Store.NO, Field.Index.ANALYZED);
            doc.add(field);
        }
        if (knowledge.getSecondType() != null) {
            field = new Field(FIELDNAME_SECONDTYPE, knowledge.getSecondType(), Field.Store.NO, Field.Index.ANALYZED);
            doc.add(field);
        }
        return doc;
    }

}
