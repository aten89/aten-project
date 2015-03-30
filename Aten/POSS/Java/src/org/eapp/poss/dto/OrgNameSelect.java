/**
 * 
 */
package org.eapp.poss.dto;

import java.util.List;

/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class OrgNameSelect extends HTMLSelect {

    private List<String> orgNames;

    public OrgNameSelect(List<String> orgNames) {
        this.orgNames = orgNames;
    }

    /**
     * 有默认值的情况 格式：
     *  <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     *  <div>zxt00001**datacenter</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (orgNames == null || orgNames.isEmpty()) {
            return out.toString();
        }
        for (String s : orgNames) {
            out.append(createOption(s, s));
        }
        return out.toString();
    }

}
