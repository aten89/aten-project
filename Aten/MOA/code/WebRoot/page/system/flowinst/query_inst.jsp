<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程实例管理</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/system/flowinst/query_inst.js"></script>
</head>
<body class="bdNone" >
<input  type="hidden" id="ttid" />
<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_inst'/>" />
 <div class="soso" style="height:auto">
		<div style="padding:2px 0 3px 33px">
			表单号：<input id="formid"  type="text" class="ipt01"  style="width:65px"/>&nbsp;
			任务描述：<input id="desc"  type="text" class="ipt01"  style="width:200px"/>&nbsp;
			创建时间：从 <input readonly id="beginCreateDate"  type="text" class="invokeBoth"  style="width:65px"/> 到 
			<input readonly id="endCreateDate" type="text" class="invokeBoth"  style="width:65px"/>
			<input id="applicant" type="hidden"/>
			<input id="searchLog" class="allBtn" type="button" value="搜索"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/>&nbsp;&nbsp;
		</div>
	</div>

<div class="allList">
  <table id="zxt" width="100%" style="margin:-1px 0 0" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
	  	<th width="5%">序号</th>
	    <th width="20%">任务描述</th>
	    <th width="25%">表单ID</th>
	    <th width="12%">任务指派人</th>
		<th width="13%">任务名称</th>
		<th width="15%">任务创建时间</th>
		<th width="10%">操作</th>
	  </tr>
  </thead>
  <tbody id="taskList">
  </tbody>
</table>
</div>
<!--翻页 end-->
<!--列表 end-->
<div class="pageNext">
</div>
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="16"/> 
</body>
</html>