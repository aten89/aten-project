package org.eapp.poss.dto;

import java.io.Serializable;
import java.util.List;

import org.eapp.poss.hbean.ProdType;

public class ProdTypeTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2091112116056980315L;

	private List<ProdType> prodTypes;

	public ProdTypeTree(List<ProdType> prodTypes) {
		this.prodTypes = prodTypes;
	}
	
	public List<ProdType> getProdTypes() {
		return prodTypes;
	}

	public void setProdTypes(List<ProdType> prodTypes) {
		this.prodTypes = prodTypes;
	}
	
	public String toString() {
	    StringBuffer out = new StringBuffer();
	    if ((this.prodTypes == null) || (this.prodTypes.size() < 1)) {
	      return out.toString();
	    }
	    String url = "m/prod_type/subProdTypes?prodTypeID=";

	    for (ProdType g : this.prodTypes) {
	      out.append("<li id=\"").append(g.getId()).append("\" prodTypeID=\"").append(g.getId())
	        .append("\">").append("<span class=\"text\" prodTypeID=\"").append(g.getId())
	        .append("\">").append(g.getProdType()).append("</span>");
	      if (g.isHasSubProdType()) {
	        out.append("<ul class=\"ajax\">");
	        out.append("<li id=\"").append(g.getId()).append("\">{url:" + url).append(g.getId())
	          .append("}</li>");
	        out.append("</ul>");
	      }
	      out.append("</li>");
	    }
	    return out.toString();
	}
}