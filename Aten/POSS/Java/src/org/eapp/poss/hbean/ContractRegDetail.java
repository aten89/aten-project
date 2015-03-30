package org.eapp.poss.hbean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.eapp.client.util.UsernameCache;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class ContractRegDetail implements java.io.Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 2180773728253568486L;
    private String id;
	private BlankContract blankContract;
	private Integer contractNums;
	private String regUser;
	private Date regDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JSON(serialize = false)
	public BlankContract getBlankContract() {
		return blankContract;
	}
	public void setBlankContract(BlankContract blankContract) {
		this.blankContract = blankContract;
	}
	public Integer getContractNums() {
		return contractNums;
	}
	public void setContractNums(Integer contractNums) {
		this.contractNums = contractNums;
	}
	public String getRegUser() {
		return regUser;
	}
	public void setRegUser(String regUser) {
		this.regUser = regUser;
	}
	public String getRegUserName(){
		if (StringUtils.isBlank(regUser)) {
			return "";
		}
		return UsernameCache.getDisplayName(regUser);
	}
	@JSON(format="yyyy-MM-dd")
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
}