/**
 * 
 */
package org.eapp.oa.kb.dto;

import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.system.util.Tree;

/**
 * 生成有知识库的分类树
 */
public class KnowledgeClassTree {

    /**
     * 类别树
     */
    private Tree tree;
    /**
     * 父类型id
     */
    private String parentClassId;
    /**
     * 权限标志
     */
    private int rightFlag;
    /**
     * 是否带选择框
     */
    private boolean bCheckBox;

    /**
     * 默认构造函数
     */
    public KnowledgeClassTree() {
    }

    /**
     * 构造方法
     * 
     * @param classTree 类别树
     * @param classId 父类别id
     * @param flag 权限标志
     * @param bCheckBoxFlag 是否带选择框
     */
    public KnowledgeClassTree(Tree classTree, String classId, int flag, boolean bCheckBoxFlag) {
        this.tree = classTree;
        this.parentClassId = classId;
        this.rightFlag = flag;
        this.bCheckBox = bCheckBoxFlag;
    }

    /**
     * 生成知识库分类的XML信息
     * 
     * @return 知识库分类的XML信息
     * 
     *         <pre>
     * 修改日期	 修改人	修改原因
     * 2012-8-30	卢凯宁		修改注释
     * </pre>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();

        if (tree.getChildNodeMap() == null || tree.getChildNodeMap().size() < 1 || parentClassId == null) {
            return out.toString();
        }

        Tree finded = tree.findNode(parentClassId);
        if (finded == null || finded.getChildNodeMap() == null) {
            return out.toString();
        }
        String url = "m/knowledge?bCheckBox=" + bCheckBox + "&act=classtree&flag=" + rightFlag + "&pid=";
        for (Tree t : finded.getChildNodeMap().values()) {
            KnowledgeClass m = (KnowledgeClass) t.getNode();
            out.append("<li id='").append(m.getId())
            // .append("' title='").append(DataFormatUtil.noNullValue(m.getDescription()))
                    .append("'>");
            if (bCheckBox) {
                // out.append(addCheckBox("<span>" + latestFunModDTO.getName() + "</span>", latestFunModDTO.getId()));
                out.append(addCheckBox("<span id='" + m.getId() + "Name" + "'>" + m.getName() + "</span>", m.getId()));
            } else {
                out.append("<span id='").append(m.getId()+"Name").append("'>").append(m.getName()).append("</span>");
            }
            if (t.getChildNodeMap().size() > 0) {
                out.append("<ul class='ajax'>");
                out.append("<li>{url:").append(url).append(m.getId()).append("}</li>");
                out.append("</ul>");
            }
            out.append("</li>");
        }
        return out.toString();
    }

    /**
     * 在str前增加checkbox
     * 
     * @param str 要添加的字符串
     * @param id id
     * @return 返回增加了checkbox的串
     */
    private String addCheckBox(String str, String id) {
        return "<input name=\"_doc_chk\" type=\"checkbox\" value=\"cb" + id + "\" class=\"cBox\"　/>" + str;
        // return str + "<input name=\"_doc_chk\" type=\"checkbox\" value=\"cb" + id + "\" class=\"cBox\"/>";
    }
}
