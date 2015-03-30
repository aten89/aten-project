<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--与操作帐号绑定-->
<div class="tabMid">
<div class="subFrame">
   <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="subLeft02">
		   <!--角色定位-->
		   <div class="mkdzTip">
			  <b>角色：</b><input id="txtSearchRole" maxlength="40" type="text" class="ipt05" style="width:65px"/>
		   	  <input id="search_Role" type="button" class="allBtn subsoW" value="检索" />
		   </div>
		   <div class="jsdw">
		   </div>
		   <!--角色定位 end-->
		</td>
		<td class="subRight">
		   <table align="center" style="margin:0 auto" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>
				   <div  class="dzbd">
				   <table border="0" cellspacing="0" cellpadding="0" >
					  <tr>
						<td valign="top" width="312">
						 <div class="mkdzTip" style="padding:6px 0 0 10px">已绑定的用户帐号</div>
						 <div class="mkdz">
						    <div id="bindUsers" class="bindUser" style="height:270px;width:310px">
							   <input id="roleid" name="roleID" type="hidden"/>
							   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
							   <thead>
								   <tr>
								     <th nowrap width="30">操作</th>
									 <th nowrap>显示名称</th>
									 <th nowrap>用户帐号</th>
									 <th nowrap>所属机构</th>
								  </tr>
							   </thead>
							   <tbody></tbody>
							   </table>
							</div>
						    <div class="addTool2" >
							  <input id="saveUserRoles" type="button" value="保存" class="allBtn"/>
						    </div>
						 </div>
						</td>
						<td align="center" width="15">&nbsp;</td>
						<td valign="top" width="312">
						 <div class="mkdzTip">
						  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
						  <tr>
							<td >机构：</td>
							<td><div id="groupIdDiv" popwidth='110' popheight="240"></div></td>
							<td>
							<td>
							  帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:60px" /> <input id="searchGroupUsers" type="button" class="allBtn subsoW" value="检索" /></td>
						  </tr>
						 </table>
						 </div>
						 <div class="mkdz">
						    <div id="unBindUser" class="bindUser" style="height:305px;width:310px">
						      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
                                <thead>
                                  <tr>
									 <th nowrap width="30">操作</th>
									 <th nowrap>显示名称</th>
									 <th nowrap>用户帐号</th>
									 <th nowrap>所属机构</th>
								  </tr>
								 </thead>
								 <tbody></tbody>
                              </table>
						    </div>
						</div>
						</td>
					  </tr>
					</table>
				   </div>
				</td>
			  </tr>
			 </table>
		</td>
	  </tr>
	</table>
</div>
</div>
<script type="text/javascript" src="page/usermanage/role/bind_user.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dept.js"></script>