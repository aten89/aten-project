<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<!--我参加过的会议-->
<div class="soso" style="height:auto">
	<div class="t01" >
	<table>
		<tr>
			<td>所属地区：</td>
			<td><div style="width:106px"><div id="areaCode" name="areaCode"></div></td>
			<td>&nbsp;会议室：</td>
			<td><div style="width:106px"><div id="meetingRoomDiv" name="meetingRoom"></div></td>
			<td>&nbsp;会议主题：<input id="theme" type="text" class="ipt01" maxlength="10" style="width:95px;height:20px"/></td>
			<td>&nbsp;会议时间：从</td>
			<td><input id="inputBeginDate" readonly type="text" class="invokeBoth"  style="width:70px;height:20px"/></td>
			<td>到</td>
			<td><input id="inputEndDate" readonly type="text" class="invokeBoth"  style="width:70px;height:20px"/></td>
			<td><input id="searchMeeting" class="allBtn" type="button" value="搜索"/></td>
			<td><input id="refresh" type="button" class="flash_btn"/></td>
		</tr>
	</table>
	</div>
</div>

<div class="allList">
  <table width="100%" style="margin:-1px 0 0" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr>
			<th width="25%" style="height:24px">会议时间</th>
			<th width="8%">所属地区</th>
			<th width="15%">会议室</th>
			<th>会议主题</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="meetingList">
	</tbody>
  </table>
  <div class="pageNext"></div>
</div>
<input id="hidNumPage" type="hidden" value="1"/>
<input id="hidPageCount" type="hidden" value="15"/>
<script type="text/javascript" src="page/meeting/query/query_iatte.js"></script>
 