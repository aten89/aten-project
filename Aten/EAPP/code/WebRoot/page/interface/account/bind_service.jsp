<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--接口帐号 绑定-->
<div class="addCon tabMid">
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">
  <tr>
	<td valign="top" style="padding:2px 10px 0px 6px">
	   <!--信息提示-->
	   <div class="infoTip"></div>
	   <!--信息提示 end-->
	   <div  class="dzbd" style="padding:0">
		 <table  align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone">
		  <tr>
			<td>
			   <div  class="dzbd">
			   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabNone">
				  <tr>
					<td valign="top" width="280" style="height:305px; padding:0px 0 0">
					 <div class="mkdzTip">已绑定服务</div>
					 <div class="mkdz" style=" border:none">
						<select id="bindedSer" name="bindedSer" size="1" multiple="multiple" class="dzbdSel01" style="height:260px; width:280px">
						</select>
					 </div>
					</td>
					<td>
					   &nbsp;&nbsp;&nbsp;<input id="addBind" name="addBind" type="button" value="添加&lt;&lt;" class="allBtn"/><br />
					   &nbsp;&nbsp;&nbsp;<input id="delBind" name="delBind" type="button" value="删除&gt;&gt;" class="allBtn" style="margin:6px 0px"/>
					</td>
					<td valign="top" width="280"  style="height:305px; padding:0px 0 0">
					<div class="mkdzTip">未绑定服务：<input id="sername" name="sername" maxlength="40" type="text" class="ipt01" style="width:118px" value=""/>&nbsp;<input id="searchSer" type="button" value="搜索" class="allBtn subsoW" /></div>
					<div class="mkdz" style=" border:none">
					<select id="service" name="service" size="1" multiple="multiple" class="dzbdSel01" style="height:260px; width:280px">
					</select>
					</div>
					</td>
				  </tr>
				</table>
			   </div>
			</td>
		  </tr>
		 </table>
	   </div>
	</td>
  </tr>
 </table>
</div>
<div class="addTool2">
<input id="saveBind" name="saveBind" type="button" value="保存" class="allBtn"/>
</div>
<!--接口帐号 绑定 end-->
<script type="text/javascript" src="page/interface/account/bind_service.js"></script>