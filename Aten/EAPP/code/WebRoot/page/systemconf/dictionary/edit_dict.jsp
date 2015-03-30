<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form name="mainForm" id="mainForm" onSubmit="return false;">
<input type="hidden" id="hidSubSystemId" value="${dataDict.subSystem.subSystemID }"/>
<input type="hidden" id="hidDataDictId" value="${dataDict.dataDictID }"/>
<input type="hidden" id="hidParentDataDictId" value="${dataDict.parentDataDictionary.dataDictID}"/>
<input type="hidden" id="hidParentDataDictIdOld" value="${dataDict.parentDataDictionary.dataDictID}"/>
<!--数据字典 修改-->
<div class="addCon">
  <table width="100%"  border="0"  cellpadding="0"  cellspacing="1" class="tabNone">
  <tr>
	<td class="tit">所属系统</td>
	<td id="subSystemName">${dataDict.subSystem.name }</td>
	<td class="tit">父&nbsp;条&nbsp;目</td>
	<td>
		<input type="text" id="parentDataDict" readonly value="${dataDict.parentDataDictionary.dictName }" class="ipt01" style="width:87%" />
		<button id="showParentDataDict" class="selBtn"></button>
	</td>
  </tr>
  <tr>
	<td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
	<td><input type="text" id="dataDictKey" readonly name="dictkey" value="${dataDict.dictName }" class="ipt01" maxlength="50" /></td>
	<td class="tit">代&nbsp;&nbsp;&nbsp;&nbsp;码</td>
	<td><input type="text" id="dataDictValue" readonly name="dictvalue" value="${dataDict.dictCode }" class="ipt01" maxlength="50" /></td>
  </tr>
  <tr>
	<td class="tit">最&nbsp;大&nbsp;值</td>
	<td><input type="text" id="dataDictMaxValue" readonly name="ceilvalue" value="${dataDict.ceilValue }" class="ipt01" maxlength="50" /></td>
	<td class="tit">最&nbsp;小&nbsp;值</td>
	<td><input type="text" id="dataDictMinValue" readonly name="floorvalue" value="${dataDict.floorValue }" class="ipt01" maxlength="50" /></td>
  </tr>
  <tr>
	<td class="tit">类&nbsp;&nbsp;&nbsp;&nbsp;型</td>
	<td colspan="3"><input type="hidden" name="dictType" id="dataDictType" value="${dataDict.dictType }"/>${dataDict.dictType }</td>
	</tr>
  <tr>
	<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
	<td colspan="3"><textarea name="description" readonly id="description" class="area01">${dataDict.description }</textarea></td>
  </tr>
</table>
</div>
<div class="addTool2">
  <input type="button" id="saveTypeDict" value="保存" class="allBtn"/>
  <input type="button" id="saveAddTypeDict" value="保存并新增" class="allBtn"/>
  <input type="reset" id="resetTypeDict" value="重置" class="allBtn"/>
</div>
<!--数据字典 新增 end-->
</form>
<script type="text/javascript" src="page/systemconf/dictionary/edit_dict.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dict.js"></script>
