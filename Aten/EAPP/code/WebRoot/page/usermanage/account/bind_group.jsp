<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--角色 绑定群组-->
<div class="tabMid">
<div class="subFrame">
	<input id="accountid_" name="accountID" type="hidden">
   <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  	<td valign="top" style="padding:2px 10px 0px 6px">
	  	   <!--信息提示-->
		   <div class="infoTip" style="padding:7px 0px 6px 38px"> </div>
	  	</td>
	  </tr>
	  <tr>
		<td class="subRight">
		   <table align="center" style="margin:0 auto" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>
				   <div  class="dzbd">
				   <table border="0" cellspacing="0" cellpadding="0" class="tabNone">
					  <tr>
						<td valign="top">
						 <div class="mkdzTip" style="padding:7px 0 0 10px">已绑定的机构</div>
						 <div class="mkdz">
						    <!--组织机构-->
							<div class="bindUser" style="height:257px;width:330px">
							   <table id="bindGroupList" width="100%" border="0" cellspacing="0" cellpadding="0">
							   		<thead>
									  <tr>
										<th width="14%">操作</th>
										<th width="86%">机构</th>
									  </tr>
								     </thead>
							   		<tbody></tbody>
							  </table>
							</div>
						    <!--组织机构 end-->
							<div class="addTool2" >
							  <input type="button" id="saveBind" disabled value="保存" class="allBtn"/>
							</div>
						 </div>
						</td>
						<td align="center" width="30">&nbsp;</td>
						<td valign="top">
						  <div class="mkdzTip">
						    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
							  <tr>
								<td width="30%">机构类型：</td>
								<td width="70%">
									<div id="deptType">
									</div>
								</td>
							  </tr>
						    </table>
						  </div>
						  <div class="mkdz">
							<!--系统模块树-->
							<ul id="groupsList" class="simpleTree" style="height:287px; width:260px">
								<li class="root"><span>机构</span>
									<ul></ul>
								</li>
							</ul>
							<!--系统模块树 end-->
						  </div>
						</td>
					  </tr>
					</table>
				   </div>
				</td>
			  </tr>
			 </table>
		   <br />
		</td>
	  </tr>
	</table>
</div>
</div>
<!--角色 绑定群组 end-->
<script type="text/javascript" src="page/usermanage/account/bind_group.js"></script>