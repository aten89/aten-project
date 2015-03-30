<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>
		<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
		<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
		<script type="text/javascript" src="page/hr/holidays/query/query_holi.js"></script>
	</head>
	<body class="bdNone">
		<input id="hidModuleRights" type="hidden" value="<oa:right key='holi_query'/>" />
		
		<div class="soso">
		  <div class="t01">
		    <table cellspacing="0" cellpadding="0" border="0" >
		      <tbody>
		        <tr>
		          <td>请假人：</td>
		          <td><input id="applicant" type="hidden"/>
			    		<input id="applicantName" readonly type="text" class="ipt01" maxlength="30" style="width:65px"/>
			    		<input type="button" id="openUserSelect" class="selBtn"/>&nbsp;&nbsp;
			      </td>
			      <td>请假时间：</td>
			      <td><input readonly id="bgnQueryDate" type="text" class="invokeBoth" style="width:70px"/>
					到<input readonly  id="endQueryDate" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;</td>
		          <td><input type="button" value="搜索" class="allBtn" id="query"/>&nbsp;
		          	<input type="button" value="导出Excel" class="allBtn" id="export"/>&nbsp;
		            <input id="refresh" type="button" class="flash_btn"/> 
		          </td>
		        </tr>
		      </tbody>
		    </table>
		  </div>
		</div>
		
		<!--列表-->
		<div class="allList">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
		  <thead>
		    <tr>
				<th width="8%">单号</th>
				<th width="8%">请假人</th>
				<th width="15%">所属部门</th>
				<th width="8%">人员隶属</th>
				<th width="8%">假期类型</th>
				<th width="23%">请假时间</th>
				<th width="5%">请假天数</th>
				<th width="5%">销假天数</th>
				<th width="15%">附加说明</th>
				<th width="5%">操作</th>
			</tr>
		  </thead>
		  <tbody id="list">
		  </tbody>
		</table>
		</div>
		<!--列表 end-->
		<!--翻页-->
		<input type="hidden" id="hidNumPage" value="1"/>
		<input type="hidden" id="hidPageCount" value="15"/> 
		<div class="pageNext">
		</div>
		<!--翻页 end-->
	</body>
</html>