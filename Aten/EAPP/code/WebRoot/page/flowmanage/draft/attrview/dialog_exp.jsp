<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/flowmanage/draft/attrview/dialog_exp.js"></script>
</head>
<body class="bdDia">
	<div class="soso">
	  <div class="t01">
	  <table border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td>变量名称：</td>
			<td><div id="flowVarsDiv"></div></td>
			<td>&nbsp;操作符：</td>
			<td><div id="VarOptDiv" style="display:none">
				<div isselected="true"> == **==</div>
				<div> != **!=</div>
				<div> && **&&</div>
				<div> || **||</div>
				<div> >= **>=</div>
				<div> <= **<=</div>
				<div> > ** > </div>
				<div> < ** < </div>
				<div> + ** + </div>
				<div> - ** - </div>
				<div> * ** * </div>
				<div> / ** / </div>
			</div></td>
			<td>&nbsp; <input type="button" id="addVar" value="添加" class="allBtn"/>
			</td>
		  </tr>
	  </table>
	  </div>
	</div>
	<div class="dialogBk">
		<textarea id="expression" class="area01" style="width:523px;height:162px;"></textarea>
	</div>
	<div class="addTool2" id="but01">
	    <input type="button" id="comitBtn" class="allBtn" value="确 定"/>
		<input type="button" id="cancelBtn" class="allBtn" value="取 消"/>
	</div>
</body>
</html>