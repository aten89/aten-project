<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form action="" onSubmit="return false;">
<!--接口配置 新增-->
<div class="addCon tabMid">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
	<td class="tit">板块标题</td>
	<td><input id="portletName" type="text" value="${portlet.portletName }" class="ipt01" maxlength="50"/></td>
	<td class="tit">板块样式</td>
	<td><input id="portletStyle" type="text" value="${portlet.style }" class="ipt01" style="width:56%" maxlength="100"/>[如：width:33.3%;height:220px]</td>
  </tr>
  <tr>
	<td class="tit">所属系统</td>
	<td colspan="3">
		<input id="hidSubSystemId" type="hidden" value="${portlet.subSystem.subSystemID}"/>
		<div id="subSystemList"></div>
	</td>
  </tr>
  <tr>
	<td class="tit">内容地址</td>
	<td colspan="3"><textarea id="portletUrl" name="textarea" class="area01" style="height:16px;">${portlet.url }</textarea></td>
  </tr>
  <tr>
	<td class="tit">更多内容地址</td>
	<td colspan="3"><textarea id="morePortletUrl" name="textarea" class="area01" style="height:16px;">${portlet.moreUrl }</textarea></td>
  </tr>
</table>
<div class="addTool2">
  <input id="savePortlet" type="button" value="保存" class="allBtn"/>
  <input id="savePortletAndAdd" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetPortlet"type="reset" value="重置" class="allBtn"/>
</div>
</div>
</form>
<!--接口配置 新增 end-->
<script type="text/javascript" src="page/systemconf/portlet/edit_portlet.js"></script>