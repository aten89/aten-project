<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>

<script type="text/javascript"
	src="page/customerServiceManage/customerVisit/toDovisitCustomerList.js"></script>
<input id="hidModuleRights" type="hidden" value="<eapp:right key='TO_VISIT_CUST'/>" />
<div class="soso soRow2" >
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>客户名称：</td>
				<td><input id="customerName" type="text" maxlength="40"
					class="ipt05" />&nbsp;&nbsp;</td>
				<td >电话：</td>
				<td><input id="tel" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
				<td>最后回访时间：</td>
				<td>从<input readonly id="bgnVistTime" type="text" class="invokeBoth" style="width:65px" />
					到<input readonly id="endVistTime" type="text" class="invokeBoth" style="width:65px"/>&nbsp;&nbsp;
				</td>
			</tr>
			<tr style="height:26px">
				<td class="tit">投资顾问：</td>
				<td>
					<div style="width: 139px;">
						<div id="customerManageSel" name="customerManageSel"></div>
					</div>
				</td>
				<td>团队：</td>
		 		<td ><div id="saleGroupSel" name="saleGroupSel"></div></td>
				<td>提交日期：</td>
				<td>从<input readonly id="bgnSubmitTime" type="text" class="invokeBoth" style="width:65px" />
					到<input readonly id="endSubmitTime" type="text" class="invokeBoth" style="width:65px"/>
				</td>
				<td>回访状态：</td>
				<td>
					<div id="visitStatusSel">
						<div >1,2,-1**全部</div>
						<div isselected="true">1**待回访</div>
						<div>2**驳回</div>
						<div>-1**不通过</div>
					</div>&nbsp;&nbsp;
				</td>
				<td><input id="searchInfo" type="button" value="搜索" class="allBtn" /></td>
			</tr>
		</table>
	</div>
</div>

<!--列表-->
<div class="allList">
	<table id="waitVisitCustomerTab" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th width="3%">标记</th>
				<th width="8%">客户名称</th>
				<th width="5%">性别</th>
				<th width="11%">电话</th>
				<th width="21%">团队</th>
				<th width="8%">投资顾问</th>
				<th width="10%">提交时间</th>
				<th width="8%">回访次数</th>
				<th width="10%">最后回访时间</th>
				<th width="8%">回访状态</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody id="waitVisitCustomerList">
		</tbody>
	</table>
</div>
<div class="pageNext"></div>
<!--翻页 end-->
<!--列表 end-->