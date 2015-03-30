/**
 * 
 */
package org.eapp.poss.dto;

import java.util.Collection;

import org.eapp.poss.hbean.ProdType;

public class ProdTypeSelect extends HTMLSelect {

    private Collection<ProdType> prodTypes;

    public ProdTypeSelect(Collection<ProdType> prodTypes) {
        this.prodTypes = prodTypes;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        if (prodTypes == null || prodTypes.isEmpty()) {
            return out.toString();
        }
        for (ProdType p : prodTypes) {
            out.append(createOption(p.getId(), p.getProdType()));
        }
        return out.toString();
    }

}
