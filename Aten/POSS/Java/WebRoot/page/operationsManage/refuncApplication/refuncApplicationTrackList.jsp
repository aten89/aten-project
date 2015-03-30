<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/operationsManage/refuncApplication/refuncApplicationTrackList.js"></script>
<input id="status" type="hidden" value="${param.status }" />
<input id="hidModuleRights" type="hidden" value="<eapp:right key='REFUND_TRACK'/>" />
<!-- 工具栏 -->
<div class="soso soRow2" >
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>申请时间：</td>
				<td>从<input id="applyTimeBegin" type="text" maxlength="40" class="ipt05 invokeBoth" />
				到<input id="applyTimeEnd" type="text" maxlength="40" class="ipt05 invokeBoth" />&nbsp;&nbsp;
				</td>
				<td>客户名称：</td>
				<td>
					<input type="text" id="custName"  class="ipt05"/>
					<input type="hidden" id="custId"/>
					<input type="hidden" id="oriSelected"/>
				</td>
			</tr>
			<tr style="height:26px">
				<td>产品项目：</td>
				<td>
					<div id="prodInfoSel" name="prodInfoSel"></div>
				</td>
				<td>客户经理：</td>
				<td>
					<input id="saleManDis" type="text" maxlength="20" value="" class="ipt05" readonly="readonly" />
	      			<input id="saleManId" type="hidden" value=""/>
	      			<input id="openDispacher" class="selBtn" type="button"/>&nbsp;&nbsp;
				</td>
				<td>
					<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
				</td>
			  </tr>
		</table>
	</div>
</div>

<!--列表-->
<div class="allList">
<table id="waitVisitCustomerTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="10%">客户经理</th>
		<th width="12%">申请时间</th>
		<th width="10%">客户名称</th>
		<th width="20%">产品项目</th>
		<th width="20%">是否全额退款</th>
		<th width="10%">退款金额(万)</th>
		<th width="10%">所属步骤</th>
		<th width="10%">处理人</th>
		<th width="8%">操作</th>
	</tr>
  </thead>
 <tbody id="refuncApplicationToDoList">
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