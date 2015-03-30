/**
 * 
 */
package org.eapp.crm.dto;

import java.util.List;

import org.eapp.crm.hbean.UserAccountExt;

/**
 * 扩展信息下拉选择框
 * 
 */
public class UserAccountExtSelect extends HTMLSelect {

    /**
     * 扩展信息
     */
    private List<UserAccountExt> userExts;

    /**
     * 构造函数
     * 
     * @param userExts 扩展信息
     */
    public UserAccountExtSelect(List<UserAccountExt> userExts) {
        this.userExts = userExts;
    }

    /**
     * 有默认值的情况 格式： 
     * <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     * <div>zxt00001**datacenter</div>
     * <div>004**matural</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (userExts == null || userExts.size() < 1) {
            return out.toString();
        }
        for (UserAccountExt u : userExts) {
            out.append(createOption(u.getAccountId(), u.getDisplayName()));
        }
        return out.toString();
    }
}
