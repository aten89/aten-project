package org.eapp.oa.kb.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.oa.kb.blo.IKnowledgeBiz;
import org.eapp.oa.kb.blo.IKnowledgeClassAssignBiz;
import org.eapp.oa.kb.blo.IKnowledgeClassBiz;
import org.eapp.oa.kb.dto.KnowledgeClassAssignXml;
import org.eapp.oa.kb.dto.KnowledgeClassTree;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.dto.KnowledgeReplyPage;
import org.eapp.oa.kb.dto.KnowledgeReplyQueryParameters;
import org.eapp.oa.kb.dto.KnowledgeSearchPage;
import org.eapp.oa.kb.dto.RichTextImageSelList;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.dto.AttachmentList;
import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.FileUploadHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.SimpleFileUpload;
import org.eapp.oa.system.util.Tree;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

/**
 * 知识点相关servlet
 */
public class KnowledgeCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 894932421380316916L;

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(KnowledgeCtrl.class);

    /**
     * 上传文件最大值
     */
    private int maxUploadSize;
    /**
     * 知识分类业务接口
     */
    private IKnowledgeClassBiz knowledgeClassBiz;
    /**
     * 知识分类授权业务接口
     */
    private IKnowledgeClassAssignBiz knowledgeClassAssignBiz;
    /**
     * 知识点业务接口
     */
    private IKnowledgeBiz knowledgeBiz;


    /**
     * Constructor of the object.
     */
    public KnowledgeCtrl() {
        super();
    }

    /**
     * KnowledgeCtrl 初始化
     */
    public void init(ServletConfig config) throws ServletException {
        try {
            maxUploadSize = Integer.parseInt(config.getInitParameter("maxUploadSize"));
        } catch (Exception e) {
            log.error("init faild", e);
            maxUploadSize = -1;
        }
        knowledgeClassBiz = (IKnowledgeClassBiz) SpringHelper.getSpringContext().getBean("knowledgeClassBiz");
        knowledgeClassAssignBiz = (IKnowledgeClassAssignBiz) SpringHelper.getSpringContext().getBean(
                "knowledgeClassAssignBiz");
        knowledgeBiz = (IKnowledgeBiz) SpringHelper.getSpringContext().getBean("knowledgeBiz");
    }

    /**
     * 销毁方法
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * doGet 方法
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    /**
     * doPost 方法
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = HttpRequestHelper.getParameter(request, "act");
        if ("classtree".equalsIgnoreCase(act)) {
            // 取得有查询权限的分类树
            getKnowledgeClassTree(request, response);
            return;
        } else if ("classright".equalsIgnoreCase(act)) {
            // 取得分类的权限
            getKnowledgeClassRigth(request, response);
            return;
        } else if ("addnode".equalsIgnoreCase(act)) {
            // 添加子节点
            addKnowledgeClassTree(request, response);
            return;
        } else if ("modifyNode".equalsIgnoreCase(act)) {
            // 修改节点
            modifyKnowledgeClass(request, response);
            return;
        } else if ("delNode".equalsIgnoreCase(act)) {
            // 删除节点
            delKnowledgeClass(request, response);
            return;
        } else if ("movenode".equalsIgnoreCase(act)) {
            // 移动节点
            moveKnowledgeClassTreeNode(request, response);
            return;
        } else if ("init_assign".equalsIgnoreCase(act)) {
            // 初始化授权页面
            initAssign(request, response);
            return;
        } else if ("init_assign_detail".equalsIgnoreCase(act)) {
            // 初始化详细授权页面
            initAssignDetail(request, response);
            return;
        } else if ("get_binding_users".equalsIgnoreCase(act)) {
            // 已绑定的用户
            getBindUser(request, response);
            return;
        } else if ("binduser".equals(act)) {
            // 绑定用户
            saveBindUser(request, response);
            return;
        } else if ("get_binding_groups".equalsIgnoreCase(act)) {
            // 已绑定的机构
            getBindGroup(request, response);
            return;
        } else if ("bindgroup".equals(act)) {
            // 绑定机构
            saveBindGroup(request, response);
            return;
        } else if ("get_binding_post".equalsIgnoreCase(act)) {
            // 已绑定的职位
            getBindPost(request, response);
            return;
        } else if ("bindpost".equals(act)) {
            // 绑定职位
            saveBindPost(request, response);
            return;
        } else if ("synassign".equals(act)) {
            // 权限同步
            synchronizedAssign(request, response);
            return;
        } else if ("knowledgelist".equals(act)) {
            // 知识列表
            getKnowledgeList(request, response);
            return;
        } else if ("knowledgeinfo".equals(act)) {
            // 知识详细
            getKnowledgeInfo(request, response);
            return;
        } else if ("initaddknowledge".equals(act)) {
            // 初始化新增页面
            initAddKnowledge(request, response);
            return;
        } else if ("saveknowledge".equals(act)) {
            // 添加或修改
            saveKnowledge(request, response);
            return;
        } else if ("initmodifyknowledge".equals(act)) {
            // 初始化修改页面
            initModifyKnowledge(request, response);
            return;
        } else if ("delknowledge".equals(act)) {
            // 删除
            delKnowledge(request, response);
            return;
        } else if ("saveReply".equals(act)) {
            // 回复
            saveReply(request, response);
            return;
        } else if ("loadreplylist".equals(act)) {
            // 查询回复
            queryKnowledgeReply(request, response);
            return;
        } else if ("knowledgeimagelist".equals(act)) {
            // 知识点图片或视频列表
            contentAttachmentList(request, response);
            return;
        } else if ("uploadcontentatt".equals(act)) {
            // 上传图片或视频
            uploadContentAttachment(request, response);
            return;
        } else if ("uploadattachments".equals(act)) {
            // 上传附件
            uploadAttachments(request, response);
            return;
        } else if ("uploadatta".equals(act)) {
            // 上传附件
            uploadAtta(request, response);
            return;
        } else if ("getattachments".equals(act)) {
            // 取得附件列表
            getAttachments(request, response);
            return;
        } else if ("delcontentatt".equals(act)) {
            // 删除附件
            delContentAttachment(request, response);
            return;
        } else if ("changetofinal".equals(act)) {
            // 转正式库
            changeToFinal(request, response);
            return;
        } else if ("changetype".equals(act)) {
            // 改变类别
            changeClass(request, response);
            return;
//        } else if ("upload".equals(act)) {
//            // 上传压缩文档
//            uploadZip(request, response);
//            return;
//        } else if ("export".equals(act)) {
//            // 导出知识点
//            exportKnowledge(request, response);
//            return;
        } else if ("firstType".equals(act)) {
            // 一级分类
            getFirstType(request, response);
            return;
        } else if ("secondType".equals(act)) {
            // 二级分类
            getSecondType(request, response);
            return;
//        } else if ("solveStatus".equals(act)) {
//            // 解决方案
//            getSolveStatus(request, response);
//            return;
        } else if ("deleteReply".equals(act)) {
            // 删除内网评论
            deleteKnowledgeReply(request, response);
            return;
        } else if ("copyKnowledge".equals(act)) {
            // 拷贝知识点
            copyKnowledge(request, response);
            return;
//        } else if ("scoreKnowledge".equals(act)) {
//            // 知识点评分
//            scoreKnowledge(request, response);
//            return;
        } else if ("updateEKnowledgeAttachment".equals(act)) {
            updateEKnowledgeAttachment(request, response);
            return;
//        } else if ("modify_Knowledge_class".equals(act)) {
//            // 重新选择知识类别
//            modifyKnowledgeclass(request, response);
//            return;
        }
    }

    /**
     * 取得有查询权限的分类树
     */
    private void getKnowledgeClassTree(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String parentClassId = HttpRequestHelper.getParameter(request, "pid");
        // 添加树的checkbox
        boolean bCheckBox = HttpRequestHelper.getBooleanParameter(request, "bCheckBox", false);
        int flag = HttpRequestHelper.getIntParameter(request, "flag", KnowledgeClassAssign.FLAG_KB_QUERY);
        Tree tree = new Tree();
        if (bCheckBox) {
            tree = KnowledgeClassRight.getAllTree(request, flag);
        } else {
            tree = KnowledgeClassRight.getRightTree(request, flag);
        }
        // 改为Json输出来了来了
        // HTMLResponse.outputHTML(response, new KnowledgeClassTree(tree,
        // isFinal ? KnowledgeClass.FINAL_KB_ID :
        // KnowledgeClass.TEMP_KB_ID).toJson());

        // 添加树的checkbox
        HTMLResponse.outputHTML(response, new KnowledgeClassTree(tree, parentClassId, flag, bCheckBox).toString());

    }

    /**
     * 取得分类的权限
     */
    private void getKnowledgeClassRigth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String classId = HttpRequestHelper.getParameter(request, "classid");

        String rights = KnowledgeClassRight.getRigths(request, classId);
        XMLResponse.outputStandardResponse(response, 1, rights);
    }

    /**
     * 添加子节点
     */
    private void addKnowledgeClassTree(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String parentId = HttpRequestHelper.getParameter(request, "parentId");
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, parentId, KnowledgeClassAssign.FLAG_CLASS_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String nodeText = HttpRequestHelper.getParameter(request, "nodeText");
        String nodeDesc = HttpRequestHelper.getParameter(request, "nodeDesc");
        if (nodeText == null) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        KnowledgeClass knowClass;
        try {
            knowClass = knowledgeClassBiz.txAddKnowledgeClass(parentId, nodeText.trim(), nodeDesc);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, knowClass.getId());
            // XMLResponse.outputXML(response, new
            // KnowledgeClassNodeXml(knowClass).toDocument());
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        }
    }

    /**
     * 修改节点
     */
    private void modifyKnowledgeClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String nodeText = HttpRequestHelper.getParameter(request, "nodeText");
        String nodeDesc = HttpRequestHelper.getParameter(request, "nodeDesc");
        if (id == null || nodeText == null) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        try {
            knowledgeClassBiz.txModifyKnowledgeClass(id, nodeText.trim(), nodeDesc);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());

        }
    }

    /**
     * 删除节点
     */
    private void delKnowledgeClass(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (KnowledgeClass.FINAL_KB_ID.equals(id) || KnowledgeClass.TEMP_KB_ID.equals(id)) {
            XMLResponse.outputStandardResponse(response, 0, "不能删除根级类别");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        try {
            knowledgeClassBiz.txDelKnowledgeClass(id);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "删除成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            log.error("delKnowledgeClass failed:", e);
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        }
    }

    /**
     * 移动节点
     */
    private void moveKnowledgeClassTreeNode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String targetId = HttpRequestHelper.getParameter(request, "targetId");
        int moveStatus = HttpRequestHelper.getIntParameter(request, "status", 0);
        try {
            knowledgeClassBiz.txMoveKnowledgeClassNode(id, targetId, moveStatus);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "移动成功");
        } catch (Exception e) {
            log.error("moveKnowledgeClassTreeNode failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "移动失败");
        }
    }

    /**
     * 初始化页面授权
     */
    private void initAssign(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        KnowledgeClass knowClass = knowledgeClassBiz.getKnowledgeClassById(id);
        request.setAttribute("knowClass", knowClass);
        request.getRequestDispatcher("/page/knowledge/kb/frame_assign.jsp").forward(request, response);
    }

    /**
     * 初始化详细授权页面
     */
    private void initAssignDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String page = HttpRequestHelper.getParameter(request, "page");
        String title = HttpRequestHelper.getParameter(request, "title");
        request.setAttribute("title", title);
        request.getRequestDispatcher("/page/knowledge/kb/" + page).forward(request, response);

    }

    /**
     * 获取已绑定的用户
     */
    private void getBindUser(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<KnowledgeClassAssign> list = knowledgeClassAssignBiz.getBindingUsers(id);
            XMLResponse.outputXML(response, new KnowledgeClassAssignXml(list).toDocument());
        } catch (Exception e) {
            log.error("getBindUser failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定用户
     */
    private void saveBindUser(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        if (KnowledgeClass.FINAL_KB_ID.equals(id) || KnowledgeClass.TEMP_KB_ID.equals(id)) {
            XMLResponse.outputStandardResponse(response, 0, "该类别不允许用户授权！");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_ASSIGN)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }

        String[] user_assign = HttpRequestHelper.getParameters(request, "user_assign");
        try {
            knowledgeClassAssignBiz.txBindingUsers(id, user_assign);
            // 清除缓存权限
            KnowledgeClassRight.clearRightCache(request);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
        } catch (Exception e) {
            log.error("saveBindUser failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
        }
    }

    /**
     * 获取已绑定机构
     */
    private void getBindGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<KnowledgeClassAssign> list = knowledgeClassAssignBiz.getBindingGroups(id);
            XMLResponse.outputXML(response, new KnowledgeClassAssignXml(list).toDocument());
        } catch (Exception e) {
            log.error("getBindGroup failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定机构
     */
    private void saveBindGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_ASSIGN)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }

        String[] group_assign = HttpRequestHelper.getParameters(request, "group_assign");
        try {
            knowledgeClassAssignBiz.txBindingGroups(id, group_assign);
            // 清除缓存权限
            KnowledgeClassRight.clearRightCache(request);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "绑定机构成功！");
        } catch (Exception e) {
            log.error("saveBindGroup failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定机构失败！");
        }
    }

    /**
     * 获取已绑定职位
     */
    private void getBindPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<KnowledgeClassAssign> list = knowledgeClassAssignBiz.getBindingPosts(id);
            XMLResponse.outputXML(response, new KnowledgeClassAssignXml(list).toDocument());
        } catch (Exception e) {
            log.error("getBindPost failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定职位
     */
    private void saveBindPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_ASSIGN)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }

        String[] post_assign = HttpRequestHelper.getParameters(request, "post_assign");
        try {
            knowledgeClassAssignBiz.txBindingPosts(id, post_assign);
            // 清除缓存权限
            KnowledgeClassRight.clearRightCache(request);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
        } catch (Exception e) {
            log.error("saveBindPost failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
        }
    }

    /**
     * 同步权限
     */
    private void synchronizedAssign(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            // 判断有没有权限
            if (!KnowledgeClassRight.hasRigth(request, id, KnowledgeClassAssign.FLAG_CLASS_ASSIGN)) {
                XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
                return;
            }
            // 同步权限
            knowledgeClassAssignBiz.txSynAssign(id);
            // 清除缓存权限
            KnowledgeClassRight.clearRightCache(request);
            // 清除树缓存
            KnowledgeClassRight.clearTreeCache(request);
            XMLResponse.outputStandardResponse(response, 1, "同步权限成功！");
        } catch (Exception e) {
            log.error("synchronizedAssign failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "同步权限失败！");
        }
    }

    
    
    /**
     * 获取知识列表
     */
    private void getKnowledgeList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // 取封装好的xml
    	int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 12);
		String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
		boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
		
        String classId = HttpRequestHelper.getParameter(request, "knowClassId");
        String subject = HttpRequestHelper.getParameter(request, "subject");
        String publisher = HttpRequestHelper.getParameter(request, "publisher");
		String beginPublishDate = DataFormatUtil.formatTime( request.getParameter("beginPublishDate"));
		String endPublishDate = DataFormatUtil.formatTime( request.getParameter("endPublishDate"));

        if (classId == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, classId, KnowledgeClassAssign.FLAG_KB_QUERY)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        try {
            KnowledgeQueryParameters kqp = new KnowledgeQueryParameters();
            kqp.setPageNo(pageNo);
            kqp.setPageSize(pageSize);
    		//增加对排序列的处理
    		if (sortCol != null && !sortCol.trim().equals("")){
    			kqp.addOrder(sortCol, ascend);
    		}
    		
    		kqp.setKnowledgeClass(classId);
    		kqp.setSubject(subject);
    		kqp.setPublisher(publisher);
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
            // 默认按id排序
            kqp.addOrder("id", true);
            ListPage<Knowledge> list = knowledgeBiz.queryKnowledge(kqp);
            XMLResponse.outputXML(response, new KnowledgeSearchPage(list).toDocument());
        } catch (Exception e) {
            log.error("getKnowledgeList failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 知识详细
     */
    private void getKnowledgeInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String id = HttpRequestHelper.getParameter(request, "id");
        
        if (id == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        Knowledge know = knowledgeBiz.getKnowledgeById(id);
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, know.getKnowledgeClass().getId(), KnowledgeClassAssign.FLAG_KB_QUERY)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        request.setAttribute("userAccount", user.getAccountID());
        request.setAttribute("knowledge", know);
        request.getRequestDispatcher("/page/knowledge/kb/view_kb.jsp").forward(request, response);
    }

    /**
     * 初始化新增页面
     */
    private void initAddKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String id = HttpRequestHelper.getParameter(request, "id"); // 类型的id
        if (id == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "参数不正确");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        boolean isFinal = HttpRequestHelper.getBooleanParameter(request, "isFinal", false);
        KnowledgeClass knowClass = knowledgeClassBiz.getKnowledgeClassById(id);
        Knowledge knowledge = new Knowledge();
        knowledge.setPublisher(user.getAccountID());
        knowledge.setPublishDate(new Date());
        knowledge.setKnowledgeClass(knowClass);
        if (isFinal) {
            knowledge.setStatus(Knowledge.STATUS_FINAL);
        } else {
            knowledge.setStatus(Knowledge.STATUS_TEMP);
        }
        knowledge.setGroupName(user.getGroupNames());
        request.setAttribute("knowledge", knowledge);
        request.getRequestDispatcher("/page/knowledge/kb/draf_kb.jsp").forward(request, response);
    }

    /**
     * 保存或修改知识点
     */
    @SuppressWarnings("unchecked")
	private void saveKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }

        String id = HttpRequestHelper.getParameter(request, "id");
        String classId = HttpRequestHelper.getParameter(request, "classId");
        String subject = HttpRequestHelper.getParameter(request, "subject");
        String label = HttpRequestHelper.getParameter(request, "label");
        String remark = HttpRequestHelper.getParameter(request, "remark");
        String content = HttpRequestHelper.getParameter(request, "content");
        String firstType = HttpRequestHelper.getParameter(request, "firstType");
        String secondType = HttpRequestHelper.getParameter(request, "secondType");
        int status = HttpRequestHelper.getIntParameter(request, "status", Knowledge.STATUS_TEMP);
		
		if (classId == null || subject == null) {
			XMLResponse.outputStandardResponse(response, 0, "参数为空！");
			return;
		}

        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, classId, KnowledgeClassAssign.FLAG_KB_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有该知识类别的操作权限");
            return;
        }

     // 获取附件
        Map<String, Attachment> imgMap = (Map<String, Attachment>) request.getSession().getAttribute(SysConstants.SESSION_KNOW_IMG);
        Collection<Attachment> imgAtts = null;
        if (imgMap != null) {
            imgAtts = imgMap.values();
        }
        
        try {
            // 保存知识点
            Knowledge kb = null;
            if (id == null) {
            	//新进
            	kb = knowledgeBiz.txAddKnowledge(classId, subject, label, remark, content, firstType, secondType, status, user.getAccountID(), user.getGroupNames(), imgAtts);
            } else {
            	//修改
            	kb = knowledgeBiz.txModifyKnowledge(id, subject, label, remark, content, firstType, secondType, user.getAccountID(), imgAtts);
            }
            // 移除附件session
            request.getSession().removeAttribute(SysConstants.SESSION_KNOW_IMG);
            XMLResponse.outputStandardResponse(response, 1, kb.getId());
        } catch (OaException oe) {
            XMLResponse.outputStandardResponse(response, 0, oe.getMessage());
        } catch (Exception e) {
            log.error("saveKnowledge failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }


    /**
     * 初始化修改
     */
    private void initModifyKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "参数不正确");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        Knowledge knowledge = knowledgeBiz.getKnowledgeById(id);
        if (knowledge == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "该知识点不存在");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }

        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, knowledge.getKnowledgeClass().getId(),
                KnowledgeClassAssign.FLAG_KB_QUERY)) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "没有权限");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("knowledge", knowledge);
        request.getRequestDispatcher("/page/knowledge/kb/draf_kb.jsp").forward(request, response);
    }

    /**
     * 删除知识点
     * 
     */
    private void delKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String classId = HttpRequestHelper.getParameter(request, "classid");
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, classId, KnowledgeClassAssign.FLAG_KB_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            knowledgeBiz.txDelKnowledge(id, user.getAccountID());
            XMLResponse.outputStandardResponse(response, 1, "删除成功！");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            log.error("delKnowledge failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 添加知识点回复
     */
    private void saveReply(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        String content = HttpRequestHelper.getParameter(request, "replyContent");
        try {
            knowledgeBiz.txSaveKnowledgeReply(id, content, user.getAccountID(), user.getGroupNames());
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            log.error("saveReply failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 查询回复
     */
    private void queryKnowledgeReply(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 0);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 0);
        try {
            KnowledgeReplyQueryParameters krqp = new KnowledgeReplyQueryParameters();
            krqp.setKnowledgeClass(id);
            krqp.setPageNo(pageNo);
            krqp.setPageSize(pageSize);
            krqp.addOrder("replyDate", false);
            ListPage<KnowledgeReply> list = knowledgeBiz.queryKnowledgeReply(krqp);
            XMLResponse.outputXML(response, new KnowledgeReplyPage(list).toDocument());
        } catch (Exception e) {
            log.error("queryKnowledgeReply failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 上传附件
     */
    private void uploadAttachments(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload, "referid");
            if (referId == null) {
                XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
                return;
            }

            String[] deletedFiles = FileUploadHelper.getParameters(fileUpload, "delFilenames");
            if (deletedFiles != null) {
                for (int fileIndex = 0; fileIndex < deletedFiles.length; fileIndex++) {
                    if (deletedFiles[fileIndex] != null && !"".equals(deletedFiles[fileIndex])) {
                        deletedFiles[fileIndex] = new String(deletedFiles[fileIndex].getBytes("iso-8859-1"), "utf-8");
                    }
                }
            }

            List<Attachment> files = new ArrayList<Attachment>();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if (fileList != null && !fileList.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
                String dir = SysConstants.KNOWLEDGE_ATTACHMENT_DIR + sdf.format(new Date());
                for (FileItem file : fileList) {
                    String fileName = fileUpload.getFileName(file);
                    if (fileName == null || fileName.trim().equals("")) {
                        continue;
                    }
                    Attachment am = new Attachment(fileName, file.getSize());
                    String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
                    am.setFilePath(path);

                    // 保存附件
                    FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());

                    files.add(am);
                }
            }

            knowledgeBiz.txUpdateAttachment(referId, deletedFiles, files);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
            XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024 + "K!");
        } catch (Exception e) {
            log.error("uploadAttachments failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }

    }

    /**
     * 上传附件
     */
    private void uploadAtta(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload, "referid");
            if (referId == null) {
                XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
                return;
            }

            String[] deletedFiles = FileUploadHelper.getParameters(fileUpload, "delFilenames");
            if (deletedFiles != null) {
                for (int fileIndex = 0; fileIndex < deletedFiles.length; fileIndex++) {
                    if (deletedFiles[fileIndex] != null && !"".equals(deletedFiles[fileIndex])) {
                        deletedFiles[fileIndex] = new String(deletedFiles[fileIndex].getBytes("iso-8859-1"), "utf-8");
                    }
                }
            }

            List<Attachment> files = new ArrayList<Attachment>();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if (fileList != null && !fileList.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
                String dir = SysConstants.KNOWLEDGE_ATTACHMENT_DIR + sdf.format(new Date());
                for (FileItem file : fileList) {
                    String fileName = fileUpload.getFileName(file);
                    if (fileName == null || fileName.trim().equals("")) {
                        continue;
                    }
                    Attachment am = new Attachment(fileName, file.getSize());
                    String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
                    am.setFilePath(path);
                    // 保存附件
                    FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());
                    files.add(am);
                }
            }

            knowledgeBiz.txUpdateAtta(referId, deletedFiles, files);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
            XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024 + "K!");
        } catch (Exception e) {
            log.error("uploadAttachments failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }

    }

    /**
     * 取得附件列表
     */
    private void getAttachments(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String referId = HttpRequestHelper.getParameter(request, "referid");
        if (referId == null) {
            XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
            return;
        }
        try {

            List<Attachment> list = knowledgeBiz.getAttachments(referId);
            XMLResponse.outputXML(response, new AttachmentList(list).toDocument());
        } catch (Exception e) {
            log.error("getAttachments failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "查询失败");
        }

    }

    /**
     * 上传图片
     */
    @SuppressWarnings("unchecked")
    private void uploadContentAttachment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
//        String attType = HttpRequestHelper.getParameter(request, "attType");

        try {
            fileUpload.doParse();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if (fileList != null && !fileList.isEmpty()) {
                FileItem file = fileList.get(0);
                String fileName = fileUpload.getFileName(file);
                if (fileName == null || fileName.trim().equals("")) {
                    XMLResponse.outputStandardResponse(response, 0, "文件名不能为空");
                }
                Attachment am = new Attachment(fileName, file.getSize());
                if (!FileDispatcher.getConfig().isImgExt(am.getFileExt())) {
                    XMLResponse.outputStandardResponse(response, 0, "只能上传图片格式");
                    return;
                }

                String newFileName = SerialNoCreater.createUUID();
                String tempPath = FileDispatcher.getTempAbsDir() + newFileName + am.getFileExt();
                am.setId(newFileName);
                
                am.setFilePath(tempPath);
                // 保存附件
                FileUtil.saveFile(FileDispatcher.getSaveDir(tempPath), file.getInputStream());
                
	            Map<String, Attachment> imgMap = (Map<String, Attachment>) request.getSession().getAttribute(SysConstants.SESSION_KNOW_IMG);
                if (imgMap == null) {
                    imgMap = new HashMap<String, Attachment>();
                }
                imgMap.put(am.getId(), am);
                request.getSession().setAttribute(SysConstants.SESSION_KNOW_IMG, imgMap);
            }
            XMLResponse.outputStandardResponse(response, 1, "上传成功");
        } catch (SizeLimitExceededException e) {
            XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024 + "K!");
        } catch (Exception e) {
            log.error("uploadContentAttachment failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
    }

    /**
     * 知识点图片列表
     */
    @SuppressWarnings("unchecked")
    private void contentAttachmentList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referId = HttpRequestHelper.getParameter(request, "referId");
        try {
            List<Attachment> list = new ArrayList<Attachment>();
            Map<String, Attachment> imgMap = new HashMap<String, Attachment>();
            imgMap = (Map<String, Attachment>) request.getSession().getAttribute(SysConstants.SESSION_KNOW_IMG);
            if (imgMap != null) {
                list.addAll(imgMap.values());
            }
          
            if (referId != null) {
                Collection<Attachment> attas = knowledgeBiz.getContentAttachments(referId);
                if (attas != null) {
                    list.addAll(attas);
                }
            }
            XMLResponse.outputXML(response, new RichTextImageSelList(list).toDocument());
        } catch (Exception e) {
            log.error("contentAttachmentList failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "查询失败");
        }
    }

    /**
     * 删除附件
     */
    @SuppressWarnings("unchecked")
    private void delContentAttachment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attId = HttpRequestHelper.getParameter(request, "attId");
        String referId = HttpRequestHelper.getParameter(request, "referid");
        Map<String, Attachment> imgMap = (Map<String, Attachment>) request.getSession().getAttribute(SysConstants.SESSION_KNOW_IMG);
        if (imgMap != null) {
            imgMap.remove(attId);
        }
        try {
            if (referId != null) {
                knowledgeBiz.txDelContentAttachment(attId, referId);
            }
            XMLResponse.outputStandardResponse(response, 1, "删除成功");
        } catch (Exception e) {
            log.error("delContentAttachment failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "删除失败");
        }
    }

    /**
     * 转正式库
     */
    private void changeToFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String ids = HttpRequestHelper.getParameter(request, "ids");
        String parentId = HttpRequestHelper.getParameter(request, "parentId");
        if (ids == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }
        if (parentId == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }

        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, parentId, KnowledgeClassAssign.FLAG_KB_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String[] idArray = ids.split(",");
        try {
            knowledgeBiz.txChangeToFinal(idArray, parentId);
            XMLResponse.outputStandardResponse(response, 1, "转换成功");
        } catch (Exception e) {
            log.error("changeToFinal failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "转换失败");
        }
    }

    /**
     * 改变类别
     */
    private void changeClass(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String ids = HttpRequestHelper.getParameter(request, "ids");
        String parentId = HttpRequestHelper.getParameter(request, "parentId");
        if (ids == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }
        if (parentId == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, parentId, KnowledgeClassAssign.FLAG_KB_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String[] idArray = ids.split(",");
        try {
            knowledgeBiz.txChangeClass(idArray, parentId);
            XMLResponse.outputStandardResponse(response, 1, "转换类别成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            log.error("changeClass failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "转换类别失败");
        }
    }


    /**
     * 一级分类
     */
    private void getFirstType(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
        	Map<String, DataDictInfo> map = SysCodeDictLoader.getInstance().getFirstTypeMap();
            if (map == null) {
                return;
            }
            HTMLResponse.outputHTML(response, new DataDictionarySelect(map.values()).toString());
        } catch (Exception e) {
            log.error("getFirstType failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    /**
     * 二级分类
     */
    private void getSecondType(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
        	Map<String, DataDictInfo> map = SysCodeDictLoader.getInstance().getSecondTypeMap();
            if (map == null) {
                return;
            }
            HTMLResponse.outputHTML(response, new DataDictionarySelect(map.values()).toString());
        } catch (Exception e) {
            log.error("getSecondType failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }


    /**
     * 提取知识点附件添加
     */
    private void updateEKnowledgeAttachment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SimpleFileUpload fileUpload = new SimpleFileUpload(request, maxUploadSize);
        try {
            fileUpload.doParse();
            String referId = FileUploadHelper.getParameter(fileUpload, "referid");
            if (referId == null) {
                XMLResponse.outputStandardResponse(response, 0, "referid不能为空");
                return;
            }
            // 删除的文件
            String[] deletedFiles = FileUploadHelper.getParameters(fileUpload, "delFilenames");
            if (deletedFiles != null) {
                for (int fileIndex = 0; fileIndex < deletedFiles.length; fileIndex++) {
                    if (deletedFiles[fileIndex] != null && !"".equals(deletedFiles[fileIndex])) {
                        deletedFiles[fileIndex] = new String(deletedFiles[fileIndex].getBytes("iso-8859-1"), "utf-8");
                    }
                }
            }
            // 要保存的附件
            List<Attachment> files = new ArrayList<Attachment>();
            List<FileItem> fileList = fileUpload.getFileDataList();
            if (fileList != null && !fileList.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
                String dir = SysConstants.KNOWLEDGE_ATTACHMENT_DIR + sdf.format(new Date());
                for (FileItem file : fileList) {
                    String fileName = fileUpload.getFileName(file);
                    if (fileName == null || fileName.trim().equals("")) {
                        continue;
                    }
                    Attachment am = new Attachment(fileName, file.getSize());
                    String path = dir + SerialNoCreater.createUUID() + am.getFileExt();
                    am.setFilePath(path);

                    // 保存附件
                    FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), file.getInputStream());

                    files.add(am);
                }
            }

            knowledgeBiz.txUpdateAttachment(referId, deletedFiles, files);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (SizeLimitExceededException e) {
            XMLResponse.outputStandardResponse(response, 0, "上载文件大于" + maxUploadSize / 1024 + "K!");
        } catch (Exception e) {
            log.error("uploadAttachments failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "上载文件失败");
        }
    }

    /**
     * 拷贝知识点
     */
    private void copyKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String ids = HttpRequestHelper.getParameter(request, "ids");
        String parentId = HttpRequestHelper.getParameter(request, "parentId");
        if (ids == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }
        if (parentId == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }
        // 判断有没有权限
        if (!KnowledgeClassRight.hasRigth(request, parentId, KnowledgeClassAssign.FLAG_KB_MANAGE)) {
            XMLResponse.outputStandardResponse(response, 0, "没有操作权限");
            return;
        }
        String[] idArray = ids.split(",");
        try {
            knowledgeBiz.txCopyKnowledge(idArray, parentId);
            XMLResponse.outputStandardResponse(response, 1, "知识点拷贝成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            log.error("copyKnowledge failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "知识点拷贝失败");
        }
    }

    /**
     * 删除评论
     */
    private void deleteKnowledgeReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            knowledgeBiz.txDeleteKnowledgeReply(id);
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (Exception e) {
            log.error("deleteKnowledgeReply failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

}
