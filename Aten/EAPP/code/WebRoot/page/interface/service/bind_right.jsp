<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--接口配置 绑定权限-->
<div class="tabMid">
<div class="subFrame">
   <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	<td valign="top" class="subLeft02">
		   <!--角色定位-->
		   <div class="mkdzTip">
			  <b>服务：</b><input id="txtSearchService" type="text" maxlength="40" class="ipt05" style="width:65px"/>
		   	  <input id="search_Service" type="button" class="allBtn subsoW" value="检索" />
		   </div>
		   <div class="jsdw" style="height:391px">
		   </div>
		   <!--角色定位 end-->
		</td>
		<td class="subRight">
		   <table align="center" style="margin:0 auto" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>
				   <div  class="dzbd">
				   <table border="0" cellspacing="0" cellpadding="0" class="tabNone">
					  <tr>
						<td valign="top" width="380">
						 <div class="mkdzTip" style="padding:6px 0 0px 10px">已绑定的权限</div>
							<div class="mkdz">
						    <div class="bindUser" style="width:378px; height:366px">
						       <input id="serviceid" name="serviceID" type="hidden"/>
							   <table id="bindRightList" border="0" cellspacing="0" cellpadding="0">
							   <thead>
								  <tr>
								  	<th nowrap width="30">操作</th>
									<th nowrap width="134">子系统</th>
									<th nowrap width="90">模块名称</th>
									<th nowrap width="60">动作名称</th>
								  </tr>
							     </thead>
								  <tbody></tbody>
							  </table>
							</div>
							<div class="addTool2" >
							<input id="saveRightRoles" type="button" value="保存" class="allBtn"/>
							</div>
						 </div>
						</td>
						<td align="center" width="8">&nbsp;</td>
						<td valign="top">
						 <div class="mkdzTip">
						  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
						  <tr>
							<td width="27%">子系统：</td>
							<td width="73%">
								<div id="subSystemList">
								</div>
							</td>
						  </tr>
						 </table>
						 </div>
						  <div class="mkdz">
						   <!--系统模块树-->
						 	<ul class="simpleTree" id="jkpzTree" style="height:193px;width:243px">
							</ul>
						   <!--系统模块树 end-->
						 </div>
						 <div class="blank"></div>
						 <div class="mkdzTip" style="padding:6px 0 0px 10px">模块操作</div>
						 <div class="mkdz"> 
						   <div class="bindUser" style="width:248px; height:165px">
						   <table id="moduleActionList" border="0" cellspacing="0" cellpadding="0">
								  <thead>
								  <tr>
								  	<th nowrap width="60">操作</th>
									<th nowrap width="70">动作代码</th>
									<th nowrap width="70">动作名称</th>
								  </tr>
								  </thead>
								  <tbody>
								  </tbody>
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
<!--接口配置 绑定权限 end-->
<script type="text/javascript" src="page/interface/service/bind_right.js"></script>
<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
