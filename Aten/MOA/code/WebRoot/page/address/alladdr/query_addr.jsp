<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>
		<script type="text/javascript" src="${ermpPath}page/dialog/dialog.dept.js"></script>
		<script type="text/javascript" src="page/address/alladdr/query_addr.js"></script>	
	</head>
	<body class="bdNone">
		<input id="hidModuleRights" type="hidden" value="<oa:right key='all_addr'/>" />
		<input id="userDeptId" type="hidden" class="ipt01"/>
		
		  <!--搜索-->
		  
		<div class="soso">
		  <div class="t01">
			  <table border="0" cellpadding="0" cellspacing="0" style="border:none">
				  <tr>
				  	<td>机构：</td>
					<td>
						<input id="deptName" readonly type="text" class="ipt01" style="width:160px"/>
	       				<button id="openDeptSelect" class="selBtn"></button>&nbsp;&nbsp;</td>
					<td>
					  姓名：<input id="userAccountId" type="text" class="ipt05" />&nbsp;&nbsp; 
			    		<input id="searchAddrListList" class="allBtn" type="button" value="搜索"/>
			    		<input id="exportAsCSV" class="allBtn" type="button" value="导出成CSV" title="导出成CSV，用于导入foxmail"/>
			    		<input id="refreshAddrList" type="button" class="flash_btn"/>
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
					<th width="1%" class="listCBoxW"><input name="" type="checkbox" value="" class="cBox" style="height:13px" id="AllSelectState"/></th>
					<th width="13%">帐户名</th>
					<th width="11%">姓名</th>
					<th width="12%">移动电话</th>
					<th width="13%">办公电话</th>
					<th width="34%">E-mail</th>
					<th width="6%">座位号</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody id="addrListList">
			</tbody>
		  </table>
		</div>
		<!-- 翻页 -->
		<div class="pageNext"></div>		
		<input id="hidNumPage" type="hidden" value="1"/>
		<input id="hidPageCount" type="hidden" value="10"/>
		<!-- 翻页 结束-->
	<!-- 视图列表 结束-->
	</body>
</html>