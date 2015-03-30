<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/transferRecordDealList.js"></script>
<input id="hidModuleRights" type="hidden" value="<eapp:right key='import_and_allocat'/>" />
<!--划款列表-->
<div class="allList">
<table id="transferRecordTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="50%">产品名称</th>
		<th width="12%">划款金额（万）</th>
		<th width="8%">期限（月）</th>
		<th width="10%">成交日期</th>
		<th width="10%">产品成立日期</th>
		<th width="10%">产品兑付日期</th>
	</tr>
  </thead>
 <tbody id="transferRecordList">
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