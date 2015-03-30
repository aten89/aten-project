<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业资源管理系统</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/portlet/notify/more_notify.js"></script>
</head>
<body class="bdNone" >

<div class="soso">
  <div class="t01">
	<table style="float:left;" border="0" cellpadding="0" cellspacing="0">
	  <tr>
		<td>
			标题：<input id="subject" type="text" class="ipt05" value="" maxlength="60" style="width:100px" />&nbsp;
		</td>
		<td>
			发起人：<input id="creator" type="hidden"/>
	    		<input id="creatorName" readonly type="text" class="ipt01" style="width:65px"/>
	    		<input type="button" id="openUserSelect" class="selBtn"/>&nbsp;
	    </td>
		<td>
			流程类别：
		</td>
		<td>
			<div id="flowClassDIV" name="flowClass" style="display:none"></div>&nbsp; 
		</td>
		<td>
			流程状态：
		</td>
		<td>
			<div id="flowStatusDIV" name="flowStatus" style="display:none">
				<div>**所有...</div>
				<div>1**通过</div>
				<div>0**作废</div>
				<div>2**审批中</div>
			</div>&nbsp; 
		</td>
		<td>
			知会时间：<input id="begintime" type="text" class="invokeBoth tw2" /> - 
			<input id="endtime"  type="text" class="invokeBoth tw2" />&nbsp;
			<input type="button" value="搜索" id="queryNotify" class="allBtn"/>
			<input id="refresh" type="button" class="flash_btn"/>
		</td>
			</tr>
	</table>
	</div>
</div>
			
			
   <!--列表-->
<div class="allList">
  
  <table id="zxt" width="100%" style="margin:-1px 0 0" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
	  	<th width="5%">序号</th>
	    <th width="36%">标题</th>
	    <th width="9%">发起人</th>
		<th width="18%">发起人部门</th>
		<th width="9%">流程类别</th>
		<th width="7%">流程状态</th>
		<th width="12%">知会时间</th>
		<th width="5%">操作</th>
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