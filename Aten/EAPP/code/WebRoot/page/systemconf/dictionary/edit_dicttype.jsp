<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form id="mainForm" onSubmit="return false;">
<input type="hidden" id="hidSubSystemId" value="${dataDict.subSystem.subSystemID }"/>
<input type="hidden" id="hidDataDictId" value="${dataDict.dataDictID }"/>
<!--数据字典 类型-->
<div class="addCon">
	<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1" class="tabNone">
	<tr>
	<td class="tit">所属系统</td>
	<td id="subSystemName" width="168">${dataDict.subSystem.name }</td>
	<td class="tit">类&nbsp;&nbsp;&nbsp;&nbsp;型</td>
	<td><input id="dictType" readonly type="text" value="${dataDict.dictType }" class="ipt01"/></td>
	</tr>
	<tr>
	  <td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
	  <td colspan="3"><input id="dictKey" readonly type="text" value="${dataDict.dictName }" class="ipt01" maxlength="50"/></td>
	  </tr>
	<tr>
	<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
	<td colspan="3"><textarea id="description" readonly class="area01">${dataDict.description }</textarea></td>
	</tr>
	</table>
</div>
<div class="addTool2">
  <input type="button" id="saveTypeDict" value="保存" class="allBtn"/>
  <input type="button" id="saveAddTypeDict" value="保存并新增" class="allBtn"/>
  <input type="reset" id="resetTypeDict" value="重置" class="allBtn"/>
</div>
</form>
<!--数据字典 类型end-->
<script type="text/javascript" src="page/systemconf/dictionary/edit_dicttype.js"></script>