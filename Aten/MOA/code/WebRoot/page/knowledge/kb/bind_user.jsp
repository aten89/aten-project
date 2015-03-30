<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--绑定人员-->
<div class="tabMid">

<div class="subFrame" style="height:400px">
   <div class="infoTip" style="height:18px; padding-top:9px;"><b id="titleName"></b><b>的授权</b></div>
   	<div  class="dzbd">
		<table border="0" cellspacing="0" cellpadding="0" >
		  <tr>
			<td valign="top">
			 <div class="mkdzTip" style="padding:6px 0 0 10px;font-weight:bold">已绑定的人员</div>
			 <div class="mkdz">
			    <div id="bindUsers" class="bindUser">
				   <table   border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
				   <thead>
					   <tr>
					     <th width="40px">操作</th>
						 <th width="110px">帐号</th>
						 <th>
                             <div class="klRight" style="width:250px">
                             <span>知识查询</span>
                             <span>知识管理</span>
                             <span>授权管理</span>
                             <span>分类管理</span>
                             </div>
                         </th>
					  </tr>
				   </thead>
</table>
<div style="overflow-y:auto;overflow-x:hidden;height:242px">
<table  id="bindUserList" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
				   <tbody>
				   		<tr>
				   			<td width="40px">
				   			</td>
					   		<td  width="110px">
					   			所有人<input type="hidden" value="all_user"/>
					   		</td>
				   			<td id="assign_all" class="klBox">
				   				<span><input id="assign_all_0" type="checkbox" value="0" onclick="checkBoxCtrl(this)"/></span>
				   				<span><input id="assign_all_1" type="checkbox" value="1" onclick="assignAllBoxCtrl(this);"/></span>
				   				<span><input id="assign_all_2" type="checkbox" value="2" onclick="assignAllBoxCtrl(this);"/></span>
				   				<span><input id="assign_all_3" type="checkbox" value="3" onclick="assignAllBoxCtrl(this);"/></span>
					   		</td>
				   		</tr>
				   </tbody>
				   </table>
</div>
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
</div>
<!--绑定人员 end-->

<script type="text/javascript" src="page/knowledge/kb/bind_user.js"></script>