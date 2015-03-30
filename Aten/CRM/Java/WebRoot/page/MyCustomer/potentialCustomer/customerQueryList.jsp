<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/MyCustomer/potentialCustomer/customerQueryList.js"></script>
<c:if test="${param.status == '3' }">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='S_POTENTIAL_CUST'/>" />
</c:if>
<c:if test="${param.status == '1' }">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='APPROVE_CUST'/>" />
</c:if>
<input id="status" type="hidden" value="${param.status }" />
<div class="soso">
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>客户名称：</td>
				<td><input id="custName" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
				<td>电话：</td>
				<td><input id="tel" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
				<td>提交日期：</td>
				<td width="230px">从<input readonly id="bgnSubmitTime" type="text" class="invokeBoth" style="width:65px" />
					到<input readonly id="endSubmitTime" type="text" class="invokeBoth" style="width:65px"/>
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
	<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th width="16%">客户名称</th>
				<th width="10%">性别</th>
				<th width="18%">电话</th>
				<th width="18%">创建时间</th>
				<th width="18%">提交时间</th>
				<th width="10%">回访次数</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody id="custList">
		</tbody>
	</table>
</div>
<div class="pageNext"></div>
<!--翻页 end-->
<!--列表 end-->
