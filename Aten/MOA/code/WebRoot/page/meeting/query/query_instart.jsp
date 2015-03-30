<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<!--我未开始的会议-->

<div class="soso" style="height:auto">
	<div class="t01" >
	<table>
		<tr>
			<td>&nbsp;所属地区：</td>
			<td><div style="width:106px"><div id="areaCode" name="areaCode"></div></td>
			<td>会议室：</td>
			<td><div style="width:106px"><div id="meetingRoomDiv" name="meetingRoom"></div></td>
			<td>&nbsp;会议主题：<input id="theme" type="text" class="ipt01" maxlength="10" style="width:95px;height:20px"/></td>
			<td>&nbsp;<input id="searchMeeting" class="allBtn" type="button" value="搜索"/></td>
			<td>&nbsp;<input id="refresh" type="button" class="flash_btn"/></td>
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
</div>
<div class="blank"></div>
<b>您当前总共有&nbsp;<font color="red" id="totalCount">0</font>&nbsp;个未开始会议。</b>
<script type="text/javascript" src="page/meeting/query/query_instart.js"></script>
 