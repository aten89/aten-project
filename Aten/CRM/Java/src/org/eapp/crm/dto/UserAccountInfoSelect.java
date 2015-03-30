/**
 * 
 */
package org.eapp.crm.dto;

import java.util.Collection;

import org.eapp.rpc.dto.UserAccountInfo;

/**
 * 用户信息下拉选择框
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	    lhg		新建
 * </pre>
 */
public class UserAccountInfoSelect extends HTMLSelect {

    /**
     * 用户信息
     */
    private Collection<UserAccountInfo> userAccountInfos;

    /**
     * 构造函数
     * 
     * @param userAccountInfos 用户信息
     */
    public UserAccountInfoSelect(Collection<UserAccountInfo> userAccountInfos) {
        this.userAccountInfos = userAccountInfos;
    }

    /**
     * 有默认值的情况 格式： <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     * <div>zxt00001**datacenter</div>
     * <div>004**matural</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (userAccountInfos == null || userAccountInfos.size() < 1) {
            return out.toString();
        }
        for (UserAccountInfo u : userAccountInfos) {
            out.append(createOption(u.getAccountID(), u.getDisplayName()));
        }
        return out.toString();
    }
}
