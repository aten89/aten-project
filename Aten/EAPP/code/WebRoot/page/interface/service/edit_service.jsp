<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="jkzhMain">
<!--接口配置 新增-->
<div class="tabMid">
<div class="addCon">
  <input type="hidden" id="serviceID" value="${service.serviceID}">
  <input type="hidden" id="hidisValid" value="${service.isValid ? 'Y' : 'N'}">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
	<td class="tit">服务名称</td>
	<td><input id="serviceName" readonly maxlength="40" type="text" class="ipt01" value="${service.serviceName}"/></td>
	<td  class="tit">是否有效</td>
	
    <td><!--下拉框-->
    <div id="isValid" style="display:none">
	  <div>Y**有效</div>
	  <div>N**无效</div>
    </div>
  <!--下拉框 end-->
  </td>
  </tr>
  <tr>
	<td class="tit">服务描述</td>
	<td colspan="3"><textarea id="description" readonly name="textarea" class="area01">${service.description}</textarea></td>
    </tr>
</table>
<div class="addTool2">
  <input id="saveService" type="button" value="保存" class="allBtn"/>
  <input id="saveAddService" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetService" type="reset" value="重置" class="allBtn"/>
</div>
</div>
</div>
<!--接口配置 新增 end-->
</div>
<script type="text/javascript"	src="page/interface/service/edit_service.js"></script>