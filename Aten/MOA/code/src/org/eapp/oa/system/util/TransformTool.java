/**
 * 
 */
package org.eapp.oa.system.util;


import java.util.List;

import org.eapp.client.hessian.UserAccountService;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.PostInfo;
/**
 * @author zsy
 * @version Dec 13, 2008
 */
public class TransformTool {
	private static final String GROUP_TYPE_DEPT = "D";//部门
	
	/**
	 * 通过用户ID 查找相应的用户组
	 * @param accountID
	 * @return
	 */
	public static String getDisplayGroupName(String accountID) {
		if (accountID == null) {
			return "";
		}
		StringBuffer g = new StringBuffer();
		UserAccountService uas = new UserAccountService();
		try {
			List<GroupInfo> ua = uas.getBindedGroups(accountID);
			if( ua != null ){
				for(GroupInfo rbacBean : ua){
					if(rbacBean.getType().equalsIgnoreCase(GROUP_TYPE_DEPT)){
						g.append(rbacBean.getGroupName()).append(",");
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (g.length() == 0) {
			return null;
		} else {
			g.deleteCharAt(g.length()-1);
			return g.toString();
		}

	}
	
//	/**
//	 * 取得邮箱地址
//	 * @param accountID
//	 * @return
//	 */
//	public static String getEmail(String accountID) {
//		if (accountID == null || SysConstants.USER_ACCOUNT_ADMIN.equals(accountID)) {
//			return "";
//		}
//		return accountID + SysRuntimeParams.loadSysRuntimeParams().getEmailAddress();
//	}
	
	/**
	 * 根据帐号id获取该帐户所属的职位中文名称
	 * 
	 * @param userAccountId　帐户id
	 * @return　没有所属的职位时返回""
	 */
	public static String getDisplayPostName( String userAccountId ){
		if( userAccountId == null) {
			throw new IllegalArgumentException( "帐户id不能为空" );
		}
		StringBuffer sbPostName = new StringBuffer();
		try{
			UserAccountService uas = new UserAccountService();
			
			List<PostInfo> postDTO = uas.getBindedPosts( userAccountId );
			if( postDTO != null && !postDTO.isEmpty() ){
				//有职位
				for(PostInfo postBean : postDTO){
					sbPostName.append(postBean.getPostName()).append(",");
				}
				if( sbPostName.length() > 0 ){
					sbPostName.deleteCharAt( sbPostName.length() - 1 );
				}
			}	
		} catch( Exception e ){
			e.printStackTrace();
		}
		return sbPostName.toString();
	}	
}
