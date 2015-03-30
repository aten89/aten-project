<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--绑定机构-->
<input id="flagArgs" type=hidden value="${param.flag}"/>
<input id=titleArgs type="hidden" value="${title}"/>
<div class="tabMid">
<div class="subFrame" style="height:418px">
 <div class="infoTip" style="height:18px; padding-top:9px"><b id="titleName"></b></div>
	<div  class="dzbd">
		<table border="0" cellspacing="0" cellpadding="0" >
		  <tr>
			<td valign="top" width="335">
			 <div class="mkdzTip" style="padding:6px 0 0 10px">已绑定的机构</div>
			 <div class="mkdz">
			    <div class="bindUser" style="width:332px">
				   <table  id="bindGroupList" width="100%" border="0" cellspacing="0" cellpadding="0">
				   <thead>
					   <tr>
					     <th nowrap width="30">操作</th>
						 <th nowrap>机构</th>
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
			<td valign="top" width="268">
				<div class="mkdzTip">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
				  <tr>
					<td width="30%">机构类型：</td>
					<td width="70%">
						<div id="deptType" type="single"  overflow="true" name="type">
						</div>
					</td>
				  </tr>
			    </table>
				</div>
				<div class="mkdz">
				<!--系统模块树-->
				<ul id="groupsList" class="simpleTree" style="height:296px; width:260px">
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
</div>
</div>
<!--绑定人员 end-->
<script type="text/javascript" src="page/device/paramconf/assign/bind_group.js"></script>