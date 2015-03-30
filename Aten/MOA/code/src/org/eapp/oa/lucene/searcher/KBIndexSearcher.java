/**
 * 
 */
package org.eapp.oa.lucene.searcher;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.eapp.oa.kb.blo.IKnowledgeBiz;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.lucene.ConfigFactory;
import org.eapp.oa.lucene.HtmlParser;
import org.eapp.oa.lucene.IndexBuilder;
import org.eapp.oa.lucene.IndexTaskRunner;
import org.eapp.oa.lucene.builder.FinalKBIndexBuilder;
import org.eapp.oa.lucene.builder.KBIndexBuilder;
import org.eapp.oa.lucene.builder.TempKBIndexBuilder;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;


/**
 * 
 * 知识点全文检索
 */
public class KBIndexSearcher {
    /**
     * //分词器
     */
    private Analyzer analyzer;
    /**
     * //查询最大结束集
     */
    private int numHits;
    /**
     * 知识点 业务逻辑层
     */
    private IKnowledgeBiz knowledgeBiz;
    /**
     * // 用这个进行高亮显示
     */
    private SimpleHTMLFormatter colorHTMLFormatter;
    /**
     * // 用这个进行高亮显示
     */
    private SimpleHTMLFormatter boldHTMLFormatter;

    /**
     * 构造方法
     */
    public KBIndexSearcher() {
        analyzer = ConfigFactory.getAnalyzer();
        numHits = 1000;// max:8322002
        knowledgeBiz = (IKnowledgeBiz) SpringHelper.getSpringContext().getBean("knowledgeBiz");
//        prodComKnowledgeClassBiz = (IProdComKnowledgeClassBiz) SpringHelper.getSpringContext().getBean("prodComKnowledgeClassBiz");
        // 用这个进行高亮显示，默认是<b>..</b>
        // 用这个指定<read>..</read>
        colorHTMLFormatter = new SimpleHTMLFormatter("<font class='klColor'>", "</font>");
        boldHTMLFormatter = new SimpleHTMLFormatter("<font class='klColor'>", "</font>");
    }

    /**
     * 查询所有，从正式库与临时库时找
     * 
     * @param queryString 查询字符串
     * @param kbClasses 知识类型
     * @param pageNo 分页第几页
     * @param pageSize 分页每页大小
     * @param fragmentSize fragmentSize
     * @return ListPage<Knowledge> 集合
     */
    public ListPage<Knowledge> searchAll(String queryString, List<String> kbClasses, int pageNo, int pageSize,
            int fragmentSize) {
        Searcher searcher = null;
        try {
            IndexBuilder tempkb = IndexTaskRunner.getIndexBuilder(TempKBIndexBuilder.DOCUMENTTYPE);
            IndexBuilder finalkb = IndexTaskRunner.getIndexBuilder(FinalKBIndexBuilder.DOCUMENTTYPE);
            List<IndexSearcher> searchers = new ArrayList<IndexSearcher>(2);
            if (tempkb.indexExists()) {
                searchers.add(new IndexSearcher(tempkb.getIndexDir()));
            }
            if (finalkb.indexExists()) {
                searchers.add(new IndexSearcher(finalkb.getIndexDir()));
            }
            if (searchers.size() == 0) {
                return null;
            }
            searcher = new MultiSearcher(searchers.toArray(new IndexSearcher[0]));
            return search(searcher, queryString, kbClasses, pageNo, pageSize, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从正式库里查询
     * 
     * @param queryString 查询字符串
     * @param kbClasses 知识类型
     * @param pageNo 分页第几页
     * @param pageSize 分页每页大小
     * @param fragmentSize fragmentSize
     * @return ListPage<Knowledge> 集合
     */
    public ListPage<Knowledge> searchFinal(String queryString, List<String> kbClasses, int pageNo, int pageSize,
            int fragmentSize) {
        Searcher searcher = null;
        try {
            IndexBuilder finalkb = IndexTaskRunner.getIndexBuilder(FinalKBIndexBuilder.DOCUMENTTYPE);
            if (!finalkb.indexExists()) {
                return null;
            }
            searcher = new IndexSearcher(finalkb.getIndexDir());
            return search(searcher, queryString, kbClasses, pageNo, pageSize, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

   
    /**
     * 从临时库里查询
     * 
     * @param queryString 查询字符串
     * @param kbClasses 知识类型
     * @param pageNo 分页第几页
     * @param pageSize 分页每页大小
     * @param fragmentSize fragmentSize
     * @return ListPage<Knowledge> 集合
     */
    public ListPage<Knowledge> searchTemp(String queryString, List<String> kbClasses, int pageNo, int pageSize,
            int fragmentSize) {
        Searcher searcher = null;
        try {
            IndexBuilder tempkb = IndexTaskRunner.getIndexBuilder(TempKBIndexBuilder.DOCUMENTTYPE);
            if (!tempkb.indexExists()) {
                return null;
            }
            searcher = new IndexSearcher(tempkb.getIndexDir());
            return search(searcher, queryString, kbClasses, pageNo, pageSize, fragmentSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 搜索方法
     * 
     * @param searcher 搜索器
     * @param queryString 查询字符串
     * @param kbClasses 知识类型
     * @param pageNo 分页第几页
     * @param pageSize 分页每页大小
     * @param fragmentSize fragmentSize
     * @return ListPage<Knowledge> 集合
     * @throws IOException io异常
     */
    private ListPage<Knowledge> search(Searcher searcher, String queryString, List<String> kbClasses, int pageNo,
            int pageSize, int fragmentSize) throws IOException {
        queryString = queryString.toLowerCase();// 不区分大小写
        ListPage<Knowledge> listPage = new ListPage<Knowledge>();
        TopDocCollector collector = new TopDocCollector(numHits);// 设置结果集的最大缓冲区
        // Lucene查询器
        BooleanQuery likeQuery = new BooleanQuery();
        String[] keys = queryString.split(" ");
        for (String key : keys) {
            if (key == null || key.length() == 0) {
                continue;
            }
            String[] fiels = new String[] {// 要查询的字段
            KBIndexBuilder.FIELDNAME_KNOWLEDGECLASSNAME, KBIndexBuilder.FIELDNAME_SUBJECT,
                    KBIndexBuilder.FIELDNAME_LABELS, KBIndexBuilder.FIELDNAME_CONTENT, KBIndexBuilder.FIELDNAME_REMARK,
                    KBIndexBuilder.FIELDNAME_PUBLISHER, KBIndexBuilder.FIELDNAME_PUBLISHERNAME, KBIndexBuilder.FIELDNAME_GROUPNAME,
                    KBIndexBuilder.FIELDNAME_FIRSTTYPE, KBIndexBuilder.FIELDNAME_SECONDTYPE };
            IKSegmentation se = new IKSegmentation(new StringReader(key), true);
            Lexeme le = null;
            BooleanQuery bq = new BooleanQuery();
            while ((le = se.next()) != null) {
                String tKeyWord = le.getLexemeText();
                for (int i = 0; i < fiels.length; i++) {
                    String tFeild = fiels[i];
                    TermQuery tq = new TermQuery(new Term(tFeild, tKeyWord));
                    bq.add(tq, BooleanClause.Occur.SHOULD); // 关键字之间是 "或" 的关系
                    // LIKE匹配
                    BooleanQuery bQuery = new BooleanQuery();
                    bQuery.add(new WildcardQuery(new Term(tFeild, "*" + tKeyWord + "*")), BooleanClause.Occur.SHOULD);
                    bq.add(bQuery, BooleanClause.Occur.SHOULD);
                }
            }
            likeQuery.add(bq, BooleanClause.Occur.MUST);

            // 分词匹配
            // Query query = IKQueryParser.parseMultiField(new String[] {//要查询的字段
            // KBIndexBuilder.FIELDNAME_KNOWLEDGECLASSNAME,
            // KBIndexBuilder.FIELDNAME_SUBJECT,
            // KBIndexBuilder.FIELDNAME_LABELS,
            // KBIndexBuilder.FIELDNAME_CONTENT,
            // KBIndexBuilder.FIELDNAME_REMARK,
            // KBIndexBuilder.FIELDNAME_PUBLISHER,
            // KBIndexBuilder.FIELDNAME_PUBLISHERNAME,
            // KBIndexBuilder.FIELDNAME_FIRSTTYPE,
            // KBIndexBuilder.FIELDNAME_SECONDTYPE,
            // KBIndexBuilder.FIELDNAME_PRODNAME,
            // KBIndexBuilder.FIELDNAME_PRODVER,
            // KBIndexBuilder.FIELDNAME_REQCODE,
            // KBIndexBuilder.FIELDNAME_WOCODE,
            // }, key);
            // likeQuery.add(query, BooleanClause.Occur.MUST);
            // BooleanQuery bQuery = new BooleanQuery();
            // //LIKE匹配
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_KNOWLEDGECLASSNAME , "*" + queryString +
            // "*")) , BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_SUBJECT , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_LABELS , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_CONTENT , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_REMARK , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_PUBLISHER , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_PUBLISHERNAME , "*" + queryString + "*"))
            // , BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_FIRSTTYPE , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_SECONDTYPE , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_PRODNAME , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_PRODVER , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_REQCODE , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // bQuery.add(new WildcardQuery(new Term(KBIndexBuilder.FIELDNAME_WOCODE , "*" + queryString + "*")) ,
            // BooleanClause.Occur.SHOULD);
            // likeQuery.add(bQuery, BooleanClause.Occur.MUST);
        }

        // 开始查询
        if (kbClasses == null || kbClasses.size() == 0) {// 无分类条件
            searcher.search(likeQuery, collector);
        } else {// 有分类条件
            TermsFilter termsFilter = new TermsFilter();
            for (String kbClass : kbClasses) {
                termsFilter.addTerm(new Term(KBIndexBuilder.FIELDNAME_KNOWLEDGECLASS, kbClass));
            }
            searcher.search(likeQuery, termsFilter, collector);
        }

        int totalCount = collector.getTotalHits();// 查询到的总记录数
        int firstResultIndex = (pageNo - 1) * pageSize;// 开始查询位置
        if (totalCount > 0 && firstResultIndex < totalCount) {
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 从索引中读出查询到的ID
            List<String> ids = new ArrayList<String>();
            for (int i = firstResultIndex; i < hits.length && i < firstResultIndex + pageSize; i++) {
                Document doc = searcher.doc(hits[i].doc);
                ids.add(doc.get(IndexBuilder.FIELDNAME_DOCUMENT_ID));
            }
            List<Knowledge> knowledges = knowledgeBiz.getKnowledgeByIds(ids);

            if (fragmentSize > 0) {
                // 指定高亮的格式
                // 指定查询评分
                Highlighter highlighter = new Highlighter(colorHTMLFormatter, new QueryScorer(likeQuery));
                Highlighter boldHighlighter = new Highlighter(boldHTMLFormatter, new QueryScorer(likeQuery));
                // 等于你要返回的数据长度
                highlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));
                boldHighlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));

                for (Knowledge kb : knowledges) {
                    String matchText = null;
                    // 内容
                    String content = HtmlParser.extractPlainText(kb.getContent());
                    if (content != null) {
                        matchText = highlighter.getBestFragment(analyzer, KBIndexBuilder.FIELDNAME_CONTENT,
                                content.toLowerCase());
                    }
                    if (matchText == null && kb.getRemark() != null) {
                        matchText = highlighter.getBestFragment(analyzer, KBIndexBuilder.FIELDNAME_REMARK, kb
                                .getRemark().toLowerCase());
                    }
                    if (matchText == null) {
                        if (content != null) {
                            matchText = DataFormatUtil.cutString(content, fragmentSize);
                        } else if (kb.getRemark() != null) {
                            matchText = DataFormatUtil.cutString(kb.getRemark(), fragmentSize);
                        }
                    }
                    kb.setMatchText(matchText);
                    // 标题
                    if (kb.getSubject() != null) {
                        matchText = highlighter.getBestFragment(analyzer, KBIndexBuilder.FIELDNAME_SUBJECT, kb
                                .getSubject().toLowerCase());
                        if (matchText == null) {
                            matchText = DataFormatUtil.cutString(kb.getSubject(), fragmentSize);
                        }
                        kb.setMatchSubject(matchText);
                    }
                    // 类别

                    if (kb.getClassPathName() != null) {
                        matchText = boldHighlighter.getBestFragment(analyzer,
                                KBIndexBuilder.FIELDNAME_KNOWLEDGECLASSNAME, kb.getClassPathName().toLowerCase());
                        if (matchText == null) {
                            matchText = DataFormatUtil.cutString(kb.getClassPathName(), fragmentSize);
                        }
                        kb.setMatchClassPathName(matchText);
                    }
                }
            }

            listPage.setTotalCount(totalCount);
            listPage.setDataList(knowledges);
        }
        listPage.setCurrentPageNo(pageNo);
        listPage.setCurrentPageSize(pageSize);

        return listPage;
    }
}
