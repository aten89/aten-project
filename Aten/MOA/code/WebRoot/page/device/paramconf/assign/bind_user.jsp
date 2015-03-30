<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--绑定人员-->
<input id="flagArgs" type="hidden" value="${param.flag}"/>
<input id=titleArgs type="hidden" value="${title}"/>

<div class="subFrame" style="height:418px">
 <div class="infoTip" style="height:18px; padding-top:9px"><b id="titleName"></b></div>
	<div  class="dzbd">
		<table border="0" cellspacing="0" cellpadding="0" >
		  <tr>
			<td valign="top" width="305">
			 <div class="mkdzTip" style="padding:6px 0 0 10px">已绑定的人员</div>
			 <div class="mkdz">
			    <div id="bindUsers" class="bindUser" style="width:302px">
				   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
				   <thead>
					   <tr>
					     <th nowrap width="30">操作</th>
						 <th nowrap>帐号</th>
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
			<td valign="top" width="322">
				<div class="mkdzTip">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
						<tr>
							<td>机构：</td>
							<td><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
							<td>	
								帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:70px" /> <input id="searchGroupUsers" type="button" class="allBtn" value="检索" style="width:38px"/>
							</td>
						</tr>
					</table>
				</div>
				<div class="mkdz">
				    <div id="unBindUser" class="bindUser" style="height:301px; width:320px">
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
</div>

<!--绑定人员 end-->
<!-- 对话框[机构选择] -->
<div style="display:none">
     <div id="deptDialog" style="padding:0; margin:0">
		<input id="selectparentid" type="hidden" value=""/>        	
		<input id="selectparent" type="hidden" value=""/>
		<div style="padding:0; margin:0">
			<ul class="simpleTree" id="parentTreeUl" style="height:248px; width:248px">
				<LI class=root>
					<SPAN>机构</SPAN>
					<ul></ul>
				</LI>
			</ul>
		</div>
     </div>       	       
</div>	   
<!-- 对话框[机构选择] end-->
<script type="text/javascript" src="page/device/paramconf/assign/bind_user.js"></script>