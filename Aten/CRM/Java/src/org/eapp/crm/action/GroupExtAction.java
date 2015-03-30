package org.eapp.crm.action;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.DataDictionaryService;
import org.eapp.client.util.SystemProperties;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.IGroupExtBiz;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.dto.DataDictionarySelect;
import org.eapp.crm.dto.GroupExtInfo;
import org.eapp.crm.dto.GroupExtSelect;
import org.eapp.crm.dto.UserAccountInfoSelect;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.crm.system.util.web.HTMLResponse;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

public class GroupExtAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9039387597844449359L;
	private static final Log log = LogFactory.getLog(GroupExtAction.class);
	
	//para 
	private int pageNo;
	private int pageSize;
	
	private String groupID;
	private String groupName;
	private String businessType;

	private String[] groupIDs;
	private String[] namePaths;
	
	private String dictType;
	private String queryType;
	
	//service
	private IGroupExtBiz groupExtBiz;
	
	//access result
	private ListPage<GroupExt> groupExtPage;
	private GroupExtInfo group;
	
	private List<DataDictInfo> dictBusinessTypes;
	private String htmlValue;
	
	/**
     * 销售人员
     */
    private List<UserAccountInfo> saleStaffs;
	
	public String initQuery() {
	    return success();
	}
	public String viewGroup() {
	    if (StringUtils.isBlank(this.groupID)) {
	      return error("参数不能为空");
	    }
	    
	    try {
	    	group = groupExtBiz.getGroupByID(this.groupID);
		    
		    return success();
	    }catch(Exception ex) {
	    	log.error(ex.getMessage(),ex);
	    }
	    
	    return error();
	}
	
	public String saveGroupExt() {
		if (StringUtils.isBlank(this.groupID)) {
		      return error("部门ID不能为空");
		}
		
		if (StringUtils.isBlank(this.groupName)) {
		      return error("部门名称不能为空");
		}
		
//		if (StringUtils.isBlank(this.businessType)) {
//		      return error("部门业务类型不能为空");
//		}
		try {
			this.groupExtBiz.txSaveGroupExt(this.groupID,this.groupName,this.businessType);
			
			return success();
		}catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}
		
		return error();
	}
	
	public String loadDictSelect() {
		try {
			DataDictionaryService service = new DataDictionaryService();
			List<DataDictInfo> listDict = service.getChildDataDicts(SystemProperties.SYSTEM_ID, this.dictType);
		    this.htmlValue = new DataDictionarySelect(listDict).toString();
		    return success();
		}catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}
		
		return error();
	}
	
	public String loadDictSaleDeport() {
		try {
			ListPage<GroupExt> listGroupExt = this.groupExtBiz.getGroupExtsByBusinessType(GroupExt.BUSINESS_TYPE_SALE);
			
			Set<DataDictInfo> listDict = new LinkedHashSet<DataDictInfo>();
			DataDictInfo empty = new DataDictInfo();
			empty.setDataDictID("");
			empty.setDictCode("");
			empty.setDictName("所有...");
			listDict.add(empty);
			
			if(listGroupExt != null && listGroupExt.getDataList() != null) {
				for(GroupExt item : listGroupExt.getDataList()) {
					DataDictInfo dict = new DataDictInfo();
					dict.setDataDictID(item.getGroupId());
					dict.setDictCode(item.getGroupId());
					dict.setDictName(item.getGroupName());
					
					listDict.add(dict);
				}
			}
			
			this.htmlValue = new DataDictionarySelect(listDict).toString();
			return success();
		}catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}
		
		return error();
	}
	
	public String initModify() {
	    if (StringUtils.isBlank(this.groupID)) {
	      return error("参数不能为空");
	    }
	    
	    try {
		    this.group = this.groupExtBiz.getGroupByID(this.groupID);
		    return success();
	    }catch(Exception ex) {
	    	log.error(ex.getMessage(),ex);
	    }
	    
	    return error();
	}
	
	
	/**
     * 销售部门下拉选择框
     * 
     * 
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-6     lhg     新建
     * </pre>
     */
    public void querySaleGroupSel() {
        // 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        try {
            List<GroupExt> groupExts = groupExtBiz.querySaleGroup(user.getAccountID());
            if (groupExts == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new GroupExtSelect(groupExts).toString());
        } catch (CrmException e) {
            return;
        } catch (Exception e) {
            log.error("querySaleGroup failed: ", e);
            return;
        }
    }
    
    /**
     * 
     * 获取全部销售部门信息
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-16	hyg 	新建
     * </pre>
     */
    public void queryAllSaleGroupSel() {
        try {
            List<GroupExt> groupExts = groupExtBiz.queryAllSaleGroupSel(GroupExt.BUSINESS_TYPE_SALE);
            if (groupExts == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new GroupExtSelect(groupExts).toString());
        } catch (Exception e) {
            log.error("queryAllSaleGroupSel failed: ", e);
            return;
        }
    }

    /**
     * 根据组织机构ID获取该机构的员工
     * 
     * 
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-6     lhg     新建
     * </pre>
     */
    public void queryGroupStaffByGroupName() {
        // 获取当前登录用户
//        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        try {
            List<UserAccountInfo> userAccountInfos = groupExtBiz.queryGroupStaffByGroupName(groupName);
            if (userAccountInfos == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new UserAccountInfoSelect(userAccountInfos).toString());
        } catch (CrmException e) {
            return;
        } catch (Exception e) {
            log.error("querySaleGroup failed: ", e);
            return;
        }
    }

    /**
     * 根据当前登录者获取销售人员
     * 
     * @return 操作结果
     * 
     *         <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-6     lhg     新建
     * </pre>
     */
    public String queryAllSaleStaff() {
        // 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        try {
//            saleStaffs = groupExtBiz.queryAllSaleStaff(user.getAccountID());
//            if (jsoncallback != null) {
        	if ("A".equals(queryType)) {
        		//所有的销售人员
                saleStaffs = groupExtBiz.queryAllSaleStaff();
        	} else {
        		saleStaffs = groupExtBiz.queryManSaleStaffs(user.getAccountID());
        	}
                
            //防止重复数据
            Set<UserAccountInfo> set = new HashSet<UserAccountInfo>(saleStaffs);
            HTMLResponse.outputHTML(getResponse(), new UserAccountInfoSelect(set).toString());
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("queryAllSaleStaff failed: ", e);
            return error();
        }
    }
    
    public String queryManSaleStaffs() {
        // 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        try {
            saleStaffs = groupExtBiz.queryManSaleStaffs(user.getAccountID());
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("queryAllSaleStaff failed: ", e);
            return error();
        }
    }

    
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setGroupExtBiz(IGroupExtBiz groupExtBiz) {
		this.groupExtBiz = groupExtBiz;
	}

	public ListPage<GroupExt> getGroupExtPage() {
		return groupExtPage;
	}
	
	public List<DataDictInfo> getDictBusinessTypes() {
		return dictBusinessTypes;
	}
	
	public String[] getGroupIDs() {
		return groupIDs;
	}

	public void setGroupIDs(String[] groupIDs) {
		this.groupIDs = groupIDs;
	}

	public String[] getNamePaths() {
		return namePaths;
	}

	public void setNamePaths(String[] namePaths) {
		this.namePaths = namePaths;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public GroupExtInfo getGroup() {
		return group;
	}
	
	public String getHtmlValue() {
		return htmlValue;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	
    public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	/**
     * get the saleStaffs
     * @return the saleStaffs
     */
    public List<UserAccountInfo> getSaleStaffs() {
        return saleStaffs;
    }
}
