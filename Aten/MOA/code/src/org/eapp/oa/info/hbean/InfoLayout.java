package org.eapp.oa.info.hbean;

import java.util.HashSet;
import java.util.Set;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * InfoLayout entity. Description:信息板块
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-22	李海根	注释修改
 * </pre>
 */
public class InfoLayout implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4278045914998788570L;

    /**
     * ID_,VARCHAR2(36),不能为空 --主键ID
     */
    private String id;
    /**
     * Name_,VARCHAR2(128) --信息名称
     */
    private String name;
    /**
     * FlowKey_,VARCHAR2(36) --流程标识
     */
    private String flowClass;
    /**
     * DisplayOrder_,INTEGER --排序
     */
    private Integer displayOrder;
    /**
     * Description_,VARCHAR2(1024) --描述
     */
    private String description;
    /**
     * 是否发邮件
     */
//    private Boolean isEmail;
    /**
     * 收件人账号
     */
//    private String emailAddr;
    /**
     * 发布权限
     */
    private Set<InfoLayoutAssign> infoPublishAssigns = new HashSet<InfoLayoutAssign>(0);
    /**
     * 管理权限
     */
    private Set<InfoLayoutAssign> infoManAssigns = new HashSet<InfoLayoutAssign>(0);

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the flowKey
     */
    public String getFlowClass() {
        return flowClass;
    }

    /**
     * @param flowKey the flowKey to set
     */
    public void setFlowClass(String flowClass) {
        this.flowClass = flowClass;
    }

    /**
     * @return the displayOrder
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InfoLayoutAssign> getInfoPublishAssigns() {
        return infoPublishAssigns;
    }

    public void setInfoPublishAssigns(Set<InfoLayoutAssign> infoPublishAssigns) {
        this.infoPublishAssigns = infoPublishAssigns;
    }

    public Set<InfoLayoutAssign> getInfoManAssigns() {
        return infoManAssigns;
    }

    public void setInfoManAssigns(Set<InfoLayoutAssign> infoManAssigns) {
        this.infoManAssigns = infoManAssigns;
    }

    public String getFlowClassName() {
    	if (flowClass == null) { 
    		return null;
    	}
        SysCodeDictLoader sys = SysCodeDictLoader.getInstance();
        DataDictInfo d = sys.getFlowClassByKey(flowClass);
        if (d == null) {
            return null;
        }
        return d.getDictName();
    }

//    /**
//     * 获取isEmail
//     * 
//     * @return the isEmail
//     */
//    public Boolean getIsEmail() {
//        return isEmail;
//    }
//
//    /**
//     * 设置isEmail为isEmail
//     * 
//     * @param isEmail the isEmail to set
//     */
//    public void setIsEmail(Boolean isEmail) {
//        this.isEmail = isEmail;
//    }
//
//    /**
//     * 获取emailAddr
//     * 
//     * @return the emailAddr
//     */
//    public String getEmailAddr() {
//        return emailAddr;
//    }
//
//    /**
//     * 设置emailAddr为emailAddr
//     * 
//     * @param emailAddr the emailAddr to set
//     */
//    public void setEmailAddr(String emailAddr) {
//        this.emailAddr = emailAddr;
//    }

    public InfoLayout() {
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InfoLayout other = (InfoLayout) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}