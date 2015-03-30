package org.eapp.oa.info.dto;
import org.eapp.util.hibernate.QueryParameters;

public class InformationQueryParameters extends QueryParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1444218923132335386L;
	//标题
	public void setSubject(String title){
		this.addParameter("subject", title);
	}
	public String getSubject(){
		return (String)this.getParameter("subject");
	}
	//板块id
	public void setInfoLayout(String id){
		this.addParameter("infoLayout", id);
	}
	public String getInfoLayout(){
		return (String)this.getParameter("infoLayout");
	}
	//类型
	public void setInfoClass(String infoClass){
		this.addParameter("infoClass", infoClass);
	}
	public String getInfoClass(){
		return (String)this.getParameter("infoClass");
	}
	//属性
	public void setInfoPropertys(Integer[] propertys){
		this.addParameter("infoProperty", propertys);
	}
	public Integer[] getInfoPropertys(){
		return (Integer[])this.getParameter("infoProperty");
	}
	//状态
	public void setInfoStatus(int property){
		this.addParameter("infoStatus", property);
	}
	public Integer getInfoStatus(){
		return (Integer)this.getParameter("infoStatus");
	}
}
