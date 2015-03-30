<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组管理-->
<form name="groupFrm" action="" onsubmit="return false">
	<div class="addCon">
		 <input id="hidParentID" name=hidParentID type="hidden" value="${prodTypeEntity.parentProdType.id}"/>
		 <input id="hidOldParentID" name=hidOldParentID type="hidden" value="${prodTypeEntity.parentProdType.id}"/>
		 <input id="hidProdTypeID" name="hidProdTypeID" type="hidden" value="${prodTypeEntity.id}"/>
		 <input id="hidProdType" name="hidProdType" type="hidden" value="${prodTypeEntity.prodType}"/>
		 <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1" class="tabNone">
			  <tr>
				<td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
				<td>
				   <input id="prodType" name="prodType" readonly maxlength="40" type="text" class="ipt01" value="${prodTypeEntity.prodType}"/>
				</td>
			  </tr>
			  <tr>
				<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
				<td colspan="3"><textarea id="remark" name="textarea" readonly class="area01">${prodTypeEntity.remark }</textarea></td>
			  </tr>
		</table>
	</div>
	<div class="addTool2">
	  <input id="saveProdType" type="button" value="保存" class="allBtn"/>
	  <input id="saveAddProdType" type="button" value="保存并新增" class="allBtn"/>
	  <input id="resetProdType" type="reset" value="重置" class="allBtn"/>
	</div>
</form>
<!--群组管理 end-->
<script type="text/javascript" src="page/ParaConf/edit_prod_type.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/dialog/jquery.wbox.js"></script>