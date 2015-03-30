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
<script type="text/javascript" src="page/ProdInfoManage/prodInfo/prodInfoList.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='prod_info'/>" />
<!--工具栏-->
<div class="addTool">
  <div class="t01">
	  <input type="button" class="add_btn" id="addProdInfo" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso soRow2" >
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="soPaddLeft">发行日期从：</td>
		<td class="soPaddRight"><input readonly id="sellDateBegin" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">到：</td>
		<td class="soPaddRight"><input readonly id="sellDateEnd" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">供应商：</td>
		<td class="soPaddRight"><div id="supplierSel" name="supplierSel"></div></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	  </tr>
	  <tr>
	  	<td class="soPaddLeft">产品类型：</td>
		<td class="soPaddRight"><div id="prodTypeSel" name="prodTypeSel"></div>&nbsp;&nbsp;</td>
	  	<td class="soPaddLeft">产品二级分类：</td>
		<td class="soPaddRight"><div id="secondClassifySel" name="secondClassifySel"></div>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">产品状态：</td>
		<td class="soPaddRight"><div id="prodStatusSel" name="prodStatusSel"></div>&nbsp;&nbsp;</td>
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
	<tr id="tableTH">
		<th width="7%" sort="prodCode">产品代码</th>
		<th width="24%">产品名称</th>
		<th width="10%">产品类型</th>
		<th width="10%">产品二级分类</th>
		<th width="12%">供应商</th>
		<th width="8%">产品状态</th>
		<th width="8%">项目总额度(万)</th>
		<th width="5%">期限(月)</th>
		<th width="8%">发行日期</th>
		<th width="8%">操作</th>
	</tr>
  </thead>
 <tbody id="prodInfoList">
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