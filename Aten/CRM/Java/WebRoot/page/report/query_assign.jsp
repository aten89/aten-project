<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>	
		<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.dept.js"></script>
		<script type="text/javascript" src="page/report/query_assign.js"></script>	
	</head>
	<body class="bdNone">
		<input id="rptId" type="hidden" value="${param.rptId }"/>
		
		  <!--搜索-->
		  
		<div class="soso">
		  <div class="t01">
			  <table border="0" cellpadding="0" cellspacing="0" style="border:none">
				  <tr>
				  	<td>机构：</td>
					<td width="160"><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
					<td width="600">
					  姓名：
					  	<input id="userAccountId" type="text" class="ipt05" />
					  	&nbsp;&nbsp; 
			    		<input id="searchAddrListList" class="allBtn" type="button" value="搜索"/>
			    	</td>
				  </tr>
			  </table>
		  </div>
		</div>
		<!-- 搜索结束 -->
		<!-- 视图列表 -->
		<div class="allList">
		  <table width="100%" cellspacing="0" cellpadding="0" border="0">
			<thead>
				<tr>	
					<th width="15%">帐户名</th>
					<th width="15%">姓名</th>
					<th width="65%">授权部门</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody id="data_list"></tbody>
		  </table>
		</div>
		<!-- 翻页 -->
		<div class="pageNext"></div>
		<!-- 翻页 结束-->
	<!-- 视图列表 结束-->
	</body>
</html>