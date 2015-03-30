/**
 * 
 */
package org.eapp.crm.dto;

import java.util.Collection;

import org.eapp.crm.hbean.ReportQueryAssign;

/**
 * 
 */
public class ReportAssignSelect extends HTMLSelect {

    /**
     * 组织机构扩展信息
     */
    private Collection<ReportQueryAssign> reportQueryAssigns;

    /**
     * 构造函数
     * 
     * @param groupExts 组织机构扩展信息
     */
    public ReportAssignSelect(Collection<ReportQueryAssign> reportQueryAssigns) {
        this.reportQueryAssigns = reportQueryAssigns;
    }

    /**
     * 有默认值的情况 格式： 
     * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     * <div>zxt00001**datacenter</div>
     * <div>004**matural</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (reportQueryAssigns == null || reportQueryAssigns.size() < 1) {
            return out.toString();
        }
        for (ReportQueryAssign g : reportQueryAssigns) {
            out.append(createOption(g.getGroupName(), g.getGroupName()));
        }
        return out.toString();
    }
}
