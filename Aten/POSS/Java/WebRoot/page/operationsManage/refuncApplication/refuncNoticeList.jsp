<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/operationsManage/refuncApplication/refuncNoticeList.js"></script>
<input id="status" type="hidden" value="${param.status }" />
<input id="hidModuleRights" type="hidden" value="<eapp:right key='REFUND_NOTICE'/>" />

<!-- 工具栏 -->
<div class="addTool">
	<div class="t01 t_f01">
		<input type="button" class="add_btn" id="addRefuncNotice" />
	</div>
</div>
<div class="blank3"></div>
<div class="soso">
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>登记时间从：</td>
				<td><input id="createTimeBegin" type="text" maxlength="40" class="invokeBoth soTimeW ipt05"/>&nbsp;&nbsp;</td>
				<td>到：</td>
				<td><input id="createTimeEnd" type="text" maxlength="40" class="invokeBoth soTimeW ipt05" />&nbsp;&nbsp;</td>
				<td>供应商：</td>
				<td><input id="trustCompany" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
				<td>
					<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
</div>

<!--列表-->
<div class="allList">
<table id="refuncNoticeListTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="10%">公司名称</th>
		<th width="48%">详细须知</th>
		<th width="10%">提交资料模板</th>
		<th width="10%">联系人</th>
		<!-- <th width="20%">登记时间</th> -->
		<th width="12%">操作</th>
	</tr>
  </thead>
 <tbody id="paymentToDoList">
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