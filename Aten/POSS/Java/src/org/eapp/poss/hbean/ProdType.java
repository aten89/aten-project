package org.eapp.poss.hbean;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

// default package



/**
 * ProdType entity. @author MyEclipse Persistence Tools
 */

public class ProdType  implements java.io.Serializable {

    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 6676466611331063960L;
	private String id;
    private String prodType;
    private ProdType parentProdType;
    private Integer displayOrder;

	private String remark;

    private boolean hasSubProdType;
    
    private Set<ProdType> subProdTypes = new HashSet<ProdType>(0);

    // Constructors

	/** default constructor */
    public ProdType() {
    }

    
    /** full constructor */
    public ProdType(String prodType, ProdType parentProdType, String remark) {
        this.prodType = prodType;
        this.parentProdType = parentProdType;
        this.remark = remark;
    }
   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getProdType() {
        return this.prodType;
    }
    
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
    
    @JSON(serialize=false)
    public ProdType getParentProdType() {
		return parentProdType;
	}

	public void setParentProdType(ProdType parentProdType) {
		this.parentProdType = parentProdType;
	}
	
	public Integer getDisplayOrder() {
	    return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
	    this.displayOrder = displayOrder;
	}
	
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public boolean isHasSubProdType()
    {
      return this.hasSubProdType;
    }

    public void setHasSubProdType(boolean hasSubProdType)
    {
      this.hasSubProdType = hasSubProdType;
    }
    
    @JSON(serialize=false)
    public Set<ProdType> getSubProdTypes() {
		return subProdTypes;
	}

	public void setSubProdTypes(Set<ProdType> subProdTypes) {
		this.subProdTypes = subProdTypes;
	}
}