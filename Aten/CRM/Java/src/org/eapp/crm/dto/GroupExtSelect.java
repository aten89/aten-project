/**
 * 
 */
package org.eapp.crm.dto;

import java.util.Collection;

import org.eapp.crm.hbean.GroupExt;

/**
 * 组织机构扩展信息下拉选择框
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	    lhg		新建
 * </pre>
 */
public class GroupExtSelect extends HTMLSelect {

    /**
     * 组织机构扩展信息
     */
    private Collection<GroupExt> groupExts;

    /**
     * 构造函数
     * 
     * @param groupExts 组织机构扩展信息
     */
    public GroupExtSelect(Collection<GroupExt> groupExts) {
        this.groupExts = groupExts;
    }

    /**
     * 有默认值的情况 格式： 
     * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     * <div>zxt00001**datacenter</div>
     * <div>004**matural</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (groupExts == null || groupExts.size() < 1) {
            return out.toString();
        }
        for (GroupExt g : groupExts) {
            out.append(createOption(g.getGroupId(), g.getGroupName()));
        }
        return out.toString();
    }
}
