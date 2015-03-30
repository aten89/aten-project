<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/ProdInfoManage/prodFaq/prodFaqList.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='prod_faq'/>" />
<!--工具栏-->
<div class="addTool">
  <div class="t01">
	  <input type="button" class="add_btn" id="addProdFaq" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso soRow2" >
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="soPaddLeft">发布日期从：</td>
		<td class="soPaddRight"><input readonly id="createTimeBegin" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">到：</td>
		<td class="soPaddRight"><input readonly id="createTimeEnd" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">产品：</td>
		<td class="soPaddRight"><div id="prodInfoSel" name="prodInfoSel"></div></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
	  	<td class="soPaddLeft">提问人：</td>
		<td class="soPaddRight"><div id="creatorSel" name="creatorSel"></div>&nbsp;&nbsp;</td>
	  	<td class="soPaddLeft">是否解答：</td>
		<td class="soPaddRight">
			<div id="hasAnswerSel" name="hasAnswerSel">
				<div isselected="true">**请选择...</div>
				<div>true**是</div>
				<div>false**否</div>
			</div>&nbsp;&nbsp;
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr id="#tableTH">
		<th width="10%">提问人</th>
		<th width="15%">提问时间</th>
		<th width="25%">产品项目</th>
		<th width="30%">问题概述</th>
		<th width="8%">是否解答</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
 <tbody id="prodFaqList">
 </tbody>
</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>