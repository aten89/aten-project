<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="page/MyCustomer/UnCommitCustomer/UnCommitCustList.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<c:if test="${param.dataSource == 'manual_entry' }">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='MY_DRAFT_CUST'/>" />
</c:if>
<c:if test="${param.dataSource == 'company_allot' }">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='COMPANY_ALLOT'/>" />
</c:if>
<input type="hidden" id="dataSource" value="${param.dataSource }" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
		<tr>
			<td>
				<div class="addTool sortLine">
					<div class="t01 t_f01">
						<c:if test="${param.dataSource == 'manual_entry' }">
							<input id="toAdd" type="button" class="add_btn"/>&nbsp;&nbsp;
						</c:if>
						<c:if test="${param.dataSource == 'company_allot' }">
							<input id="import" type="button" class="import_btn" />
						</c:if>
					</div>
				</div>
				<div class="blank3"></div>
			</td>
		</tr>
	
	
	<tr>
		<td><!--搜索-->
			<div class="soso">
			<div class="t01" style="height:auto">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="soPaddLeft">客户名称：</td>
						<td class="soPaddRight"><input id="custName" type="text" maxlength="30" class="ipt05"/>&nbsp;&nbsp;</td>
						<td class="soPaddLeft">电话：</td>
						<td class="soPaddRight"><input id="tel" type="text" maxlength="50" class="ipt05"/>&nbsp;&nbsp;</td>
						<td class="soPaddLeft">状态：</td>
						<td class="soPaddRight">
							<div id="statusSel" name="statusSel">
								<div>**所有...</div>
								<div>0**未提交</div>
								<div>2**驳回</div>
							</div>&nbsp;&nbsp;
						</td>
						<td>提交日期：</td>
						<td width="230px">从<input readonly id="bgnSubmitTime" type="text" class="invokeBoth" style="width:65px" />
							到<input readonly id="endSubmitTime" type="text" class="invokeBoth" style="width:65px"/>
						</td>
						<td>
							<input id="lowSearch" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</div>
			</div>
		</td><!--搜索 end-->
	</tr>
	<tr>
		<td><!--列表-->
		<div class="allList">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<thead>
			<tr id="tableTH">
				<th width="15%" sort="custName">客户名称</th>
				<th width="10%">性别</th>
				<th width="15%">电话</th>
				<th width="15%">创建时间</th>
				<th width="15%">提交时间</th>
				<th width="10%">回访次数</th>
				<th width="10%">状态</th>
				<th width="10%">操作</th>
			</tr>
			</thead>
			<tbody id="unCommitCustList">
			</tbody>
		</table>
		</div>
		<div class="pageNext">
		</div>
		<!--翻页 end-->
		</td>
	</tr>
</table>
