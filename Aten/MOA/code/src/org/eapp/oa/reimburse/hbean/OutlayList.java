package org.eapp.oa.reimburse.hbean;

// default package

/**
 * OutlayList entity.
 * Description:费用明细
 * @author MyEclipse Persistence Tools
 */

public class OutlayList implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5220865752240853001L;
	//ID_,VARCHAR2(36),不能为空                               --主键ID
	private String id;
	//ReimbursementID_,VARCHAR2(36)                      --报销单
	private Reimbursement reimbursement;
	//OutlayCategory_,VARCHAR2(128)                      --费用类别
	private String outlayCategory;
	//OutlayName_,VARCHAR2(128)                          --费用名称
	private String outlayName;
	//DocumetNum_,INTEGER                                --单据数量
	private Long documetNum;
	//OutlaySum_,NUMBER(10,2)                            --费用金额
	private Double outlaySum;
	//Description_,VARCHAR2(512)                         --附加说明
	private String description;
	//DISPLAYORDER                                        ----排序
	private Integer displayOrder;
	//ReimItem                                            ----条目
	private ReimItem reimItem;                             
	
	// Constructors
	
	

	/** default constructor */
	public OutlayList() {
	}

	/** full constructor */
	public OutlayList(Reimbursement reimbursement, String outlayCategory,
			String outlayName, Long documetNum,
			Double outlaySum, String description,Integer displayOrder,ReimItem reimItem) {
		this.reimbursement = reimbursement;
		this.outlayCategory = outlayCategory;
		this.outlayName = outlayName;
		this.documetNum = documetNum;
		this.outlaySum = outlaySum;
		this.description = description;
		this.displayOrder = displayOrder;
		this.reimItem = reimItem;
		
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Reimbursement getReimbursement() {
		return this.reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getOutlayCategory() {
		return this.outlayCategory;
	}

	public void setOutlayCategory(String outlayCategory) {
		this.outlayCategory = outlayCategory;
	}

	public String getOutlayName() {
		return this.outlayName;
	}

	public void setOutlayName(String outlayName) {
		this.outlayName = outlayName;
	}

	public Long getDocumetNum() {
		return this.documetNum;
	}

	public void setDocumetNum(Long documetNum) {
		this.documetNum = documetNum;
	}

	public Double getOutlaySum() {
		return this.outlaySum;
	}

	public void setOutlaySum(Double outlaySum) {
		this.outlaySum = outlaySum;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final OutlayList other = (OutlayList) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}
//
//	public ReimItem getReimItem() {
//		return reimItem;
//	}

	public void setReimItem(ReimItem reimItem) {
		this.reimItem = reimItem;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public ReimItem getReimItem() {
		return reimItem;
	}


}