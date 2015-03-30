<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组 添加人员-->
   <div  class="dzbd" style="padding-top:0">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
		   <table border="0" cellspacing="0" cellpadding="0" style="border:none">
		  <tr>
			<td valign="top" width="312" style="height:349px; padding:18px 0 0">
			 <div class="mkdzTip" style="padding:6px 0 0 10px">已绑定的用户帐号</div>
			 <div class="mkdz">
			 	<form name="contetnMain" id="contetnMain" onsubmit="return false;">
				    <div id="bindUsers" class="bindUser" style="height:265px;width:310px">
					   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none">
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
					  <input id="saveBind" type="button" value="保存" class="allBtn"/>
				    </div>
			    </form>
			 </div>
			</td>
			<td align="center" width="15">&nbsp;</td>
			<td valign="top" width="312"  style="height:349px; padding:18px 0 0">
			 <div class="mkdzTip">
			  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none">
			  <tr>
					<td >机构：</td>
					<td><div id="groupIdDiv" popwidth='110' popheight="240"></div></td>
					<td>
					  帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:60px" /> <input id="searchPostUsers" type="button" class="allBtn subsoW" value="检索" /></td>
				  </tr>
			  <tr>
			 </table>
			 </div>
			 <div class="mkdz">
			    <div id="unBindUser" class="bindUser" style="height:300px;width:310px">
			      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none">
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
		</td>
	  </tr>
	</table>
   </div>
<!--群组 添加人员 end-->
<script type="text/javascript" src="page/usermanage/post/bind_user.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dept.js"></script>