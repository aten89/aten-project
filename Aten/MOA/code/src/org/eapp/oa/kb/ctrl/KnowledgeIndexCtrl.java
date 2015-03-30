package org.eapp.oa.kb.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.kb.blo.IKnowledgeBiz;
import org.eapp.oa.kb.blo.IKnowledgeClassBiz;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.dto.KnowledgeSearchPage;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.lucene.IndexTaskRunner;
import org.eapp.oa.lucene.builder.FinalKBIndexBuilder;
import org.eapp.oa.lucene.builder.KBIndexBuilder;
import org.eapp.oa.lucene.builder.TempKBIndexBuilder;
import org.eapp.oa.lucene.searcher.KBIndexSearcher;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;


/**
 * KnowledgeIndexCtrl 知识点索引
 */
public class KnowledgeIndexCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 894932421380316916L;

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(KnowledgeIndexCtrl.class);

    /**
     * 知识点类别接口
     */
    private IKnowledgeClassBiz knowledgeClassBiz;

    /**
     * 知识点业务接口
     */
    private IKnowledgeBiz knowledgeBiz;

    /**
     * Constructor of the object.
     */
    public KnowledgeIndexCtrl() {
        super();
    }

    /**
     */
    public void init(ServletConfig config) throws ServletException {
        knowledgeClassBiz = (IKnowledgeClassBiz) SpringHelper.getSpringContext().getBean("knowledgeClassBiz");
        knowledgeBiz = (IKnowledgeBiz) SpringHelper.getSpringContext().getBean("knowledgeBiz");
    }

    /**
     * servlet销毁方法
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * doGet方法
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    /**
     * doPost方法
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = HttpRequestHelper.getParameter(request, "act");
        if ((SysConstants.MODIFY + "_rebuild").equalsIgnoreCase(act)) {
            // 重建索引
            reBuildIndex(request, response);
            return;
        } else if ((SysConstants.MODIFY + "_update").equalsIgnoreCase(act)) {
            // 更新索引
            updateIndex(request, response);
            return;
        } else if ("search".equalsIgnoreCase(act)) {
            // 查询
            search(request, response);
            return;
        } else if ("searchByArgs".equalsIgnoreCase(act)) {
            // 高级搜索
            searchByArgs(request, response);
            return;
        }
    }

    /**
     * 重建索引
     * 
     */
    private void reBuildIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            KBIndexBuilder builder = (KBIndexBuilder) IndexTaskRunner.getIndexBuilder(TempKBIndexBuilder.DOCUMENTTYPE);
            builder.reBuildIndex();
            builder = (KBIndexBuilder) IndexTaskRunner.getIndexBuilder(FinalKBIndexBuilder.DOCUMENTTYPE);
            builder.reBuildIndex();
            XMLResponse.outputStandardResponse(response, 1, "重建索引成功");
        } catch (Exception e) {
            log.error("reBuildIndex failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "重建索引失败");
        }
    }

    /**
     * 更新索引
     */
    private void updateIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            IndexTaskRunner.run();
            XMLResponse.outputStandardResponse(response, 1, "更新索引队列成功");
        } catch (Exception e) {
            log.error("updateIndex failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "更新索引队列失败");
        }
    }

    /**
     * 全文检索查找知识点
     */
    private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }
        List<String> classIds = knowledgeClassBiz.getSearchableClassId(user.getAccountID(), groupNames, postNames);

        String keyword = HttpRequestHelper.getParameter(request, "keyword");
        int status = HttpRequestHelper.getIntParameter(request, "status", -1);
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
        int fragmentSize = HttpRequestHelper.getIntParameter(request, "fragmentsize", 100);
        KBIndexSearcher s = new KBIndexSearcher();
        ListPage<Knowledge> page = null;
        // if(keyword !=null && keyword.length()>0){
        if (Knowledge.STATUS_FINAL == status) {
            page = s.searchFinal(keyword, classIds, pageNo, pageSize, fragmentSize);
        } else if (Knowledge.STATUS_TEMP == status) {
            page = s.searchTemp(keyword, classIds, pageNo, pageSize, fragmentSize);
        } else {
            page = s.searchAll(keyword, classIds, pageNo, pageSize, fragmentSize);
        }
        XMLResponse.outputXML(response, new KnowledgeSearchPage(page).toDocument());
    }

    /**
     * 知识搜索-高级搜索
     */
    private void searchByArgs(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		
        String subject = HttpRequestHelper.getParameter(request, "subject");
        String publisher = HttpRequestHelper.getParameter(request, "publisher");
		String beginPublishDate = DataFormatUtil.formatTime( request.getParameter("beginPublishDate"));
		String endPublishDate = DataFormatUtil.formatTime( request.getParameter("endPublishDate"));
		String firstType = HttpRequestHelper.getParameter(request, "firstType");
		String secondType = HttpRequestHelper.getParameter(request, "secondType");
        Integer status = HttpRequestHelper.getIntParameter(request, "status", -1);
        if (status == -1) {
            status = null;
        }

        ListPage<Knowledge> list = null;
        try {
            KnowledgeQueryParameters kqp = new KnowledgeQueryParameters();
            kqp.setPageNo(pageNo);
            kqp.setPageSize(pageSize);
    		kqp.setSubject(subject);
    		kqp.setPublisher(publisher);
    		kqp.setFirstType(firstType);
    		kqp.setSecondType(secondType);
    		if(beginPublishDate != null){
    			kqp.setBeginPublishDate(Timestamp.valueOf(beginPublishDate));
    		}
    		if(endPublishDate != null){//加一天
    			Timestamp t = Timestamp.valueOf(endPublishDate);
    			Calendar ca = Calendar.getInstance();
    			ca.setTimeInMillis(t.getTime());
    			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
    			t.setTime(ca.getTimeInMillis());
    			kqp.setEndPublishDate(t);
    		}
            
            // 有查看权限的知识类别
            List<String> classIds = knowledgeClassBiz.getSearchableClassId(user.getAccountID(), groupNames, postNames);
            kqp.setKnowledgeClassIds(classIds);
            // 设置是否正式库
            kqp.setStatus(status);
            // 默认排序
            kqp.addOrder("publishDate", false);
            kqp.addOrder("id", true);
            list = knowledgeBiz.getPageList(kqp);
            XMLResponse.outputXML(response, new KnowledgeSearchPage(list).toDocument());
        } catch (Exception e) {
            log.error("searchByArgs failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }
}
