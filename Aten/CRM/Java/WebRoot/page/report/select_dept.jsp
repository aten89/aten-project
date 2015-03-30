<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>部门选择</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script type="text/javascript" src="${ermpPath}jqueryui/tree/jquery.simple.tree.js"></script>
	<script type="text/javascript" src="page/report/select_dept.js"></script>	
	<script language="javascript">
		
	</script>
</head>
<body class="bdDia"">
<div class="dialogBk">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top">
			  <div class="mkdz">
				<!--系统模块树-->
				<ul id="groupsList" class="simpleTree" style="height:301px; width:335px">
					<li class="root"><span>请选择组织机构</span>
						<ul></ul>
					</li>
				</ul>
				<!--系统模块树 end-->
			  </div>
			</td>
		</tr>
	</table>
</div>
<div class="addTool2">
        <input type="button" value="确 定" onclick="selectDept();" class="allBtn"/>
        <input type="button" value="清 除" onclick="clearDept();" class="allBtn"/>
        <input type="button" value="取 消" onclick="cancel();" class="allBtn"/>
</div>
</body>
</html>
