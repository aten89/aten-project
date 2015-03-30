<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_man'/><oa:right key='flow_man'/>" />

<!-- 搜索 -->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>流程类别：</td>
		<td>
		    <!--下拉框-->
			<div id="flowDraftSel" type="single" overflow="true" name="flowClass">
			</div>
			<!--下拉框 end-->
		</td>
		<td>
			&nbsp;流程名称：<input id="flowName" name="flowName" type="text" maxlength="40" class="ipt05"  style="width:100px"/>
		</td>
		<td>&nbsp;<input id="btnSearch" type="button" value="搜索" class="allBtn"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/></td>
		<td>
	  </tr>
  </table>
  </div>
</div>
<!-- 搜索 end -->

<!--已发布流程-->
<div class="allList">
	  <table id="publishedFlowList" width="100%"  border="0" align="center" cellpadding="0"  cellspacing="0">
		<thead>
			<tr>
				<th width="15%">流程类别</th>
				<th width="20%">流程名称</th>
				<th width="15%">版本号</th>
				<th width="32%">流程描述</th>
				<th width="18%">操作</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	  </table>
	</div>
<!--已发布流程 end-->

<!-- 翻页 -->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>

<script type="text/javascript" src="page/system/flowconf/query_pub.js"></script>