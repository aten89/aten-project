<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>

<script type="text/javascript" src="page/contract/hand/query_check.js"></script>

<input id="r_userActionKeys1" type="hidden" value="<eapp:right key='send_check'/>" />
<div class="blank3"></div>
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>产品项目：</td>
		<td><input id="prodName" type="text" maxlength="40" class="ipt01" style="width:200px" />&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn" r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="19%">产品项目</th>
		<th width="8%">所属机构</th>
		<th width="6%">登记人</th>
		<th width="8%">登记时间</th>
		<th width="5%">上交数</th>
		<th width="5%">签署数</th>
		<th width="5%">空白数</th>
		<th width="5%">签废数</th>
		<th width="8%">快递公司名称</th>
		<th width="8%">上交快递单号</th>
		<th width="8%">上交时间</th>
		<th width="10%">上交备注</th>
		<th width="5%">操作</th>
	</tr>
  </thead>
 <tbody id="data_list">
 </tbody>
</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="10"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
