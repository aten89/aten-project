/**
 * 
 */
package org.eapp.poss.dto;

import java.util.Collection;

import org.eapp.poss.hbean.Supplier;

/**
 * @author zsy
 * @version Nov 26, 2008
 */
public class SupplierSelect extends HTMLSelect {

    private Collection<Supplier> suppliers;

    public SupplierSelect(Collection<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * 有默认值的情况 格式：
     *  <div isselected="true">8a283d001a52213f011a5255d2f60002**dsfdf</div> 
     *  <div>zxt00001**datacenter</div>
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        if (suppliers == null || suppliers.isEmpty()) {
            return out.toString();
        }
        for (Supplier s : suppliers) {
            out.append(createOption(s.getId(), s.getSupplier()));
        }
        return out.toString();
    }

}
