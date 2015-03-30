/**
 * 
 */
package org.eapp.crm.dto;

import java.util.ArrayList;
import java.util.List;

import org.eapp.crm.hbean.UserAccountExt;

/**
 * 多个用户账号扩展
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-27	lhg		新建
 * </pre>
 */
public class MultipleUserAccountExtDTO {
    
    /**
     * userAccountExts
     */
    List<UserAccountExt> userAccountExts = new ArrayList<UserAccountExt>(0);

    /**
     * get the userAccountExts
     * @return the userAccountExts
     */
    public List<UserAccountExt> getUserAccountExts() {
        return userAccountExts;
    }

    /**
     * set the userAccountExts to set
     * @param userAccountExts the userAccountExts to set
     */
    public void setUserAccountExts(List<UserAccountExt> userAccountExts) {
        this.userAccountExts = userAccountExts;
    }

}
