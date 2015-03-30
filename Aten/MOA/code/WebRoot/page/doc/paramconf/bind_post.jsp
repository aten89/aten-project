<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--绑定职位-->
<input id="titleArgs" type="hidden" value="${title}"/>
<div class="tabMid">
<div class="subFrame" style="height:418px">
 <div class="infoTip" style="height:18px; padding-top:9px"><b id="titleName"></b></div>
	<div  class="dzbd">
		<table border="0" cellspacing="0" cellpadding="0" >
		  <tr>
			<td valign="top" width="335">
			 <div class="mkdzTip" style="padding:6px 0 0 10px">已绑定的职位</div>
			 <div class="mkdz">
			    <div class="bindUser" style="width:332px">
				   <table  id="bindPostList" width="100%" border="0" cellspacing="0" cellpadding="0">
				   <thead>
					   <tr>
					     <th nowrap width="30">操作</th>
						 <th nowrap>职位</th>
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
				<div class="mkdzTip" style="padding:6px 0 0 10px">请选择职位</div>
				<div class="mkdz">
				<!--群组管理树-->
				<ul id="postTree" class="simpleTree" style="height:297px; width:261px">
					<li class="root" postid=""><span postid="">职位</span>
						<ul></ul>
					</li>
				</ul>
				<!--群组管理树 end-->
				</div>
			</td>
		  </tr>
		</table>
	</div>
</div>
</div>
<!--绑定人员 end-->
<script type="text/javascript" src="page/doc/paramconf/bind_post.js"></script>