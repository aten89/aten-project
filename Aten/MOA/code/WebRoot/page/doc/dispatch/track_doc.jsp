<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<script type="text/javascript" src="page/doc/dispatch/track_doc.js"></script>
<input id="hidModuleRights" type="hidden" value="<oa:right key='dis_track'/>" />
<!--跟踪文件-->

<div class="soso" style="height:auto">
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td>标题：<input id="subject" type="text" class="ipt01" maxlength="30" style="width:90px"/>&nbsp;
	    	起稿时间：从 <input id="beginDraftDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    	<input id="endDraftDate" readonly type="text" class="invokeBoth"  style="width:65px"/>&nbsp;</td>
	    	<td><input id="searchDoc" class="allBtn" type="button" value="搜索"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/></td>
	  	</tr>
	  </table>
	</div>
</div>
<div class="allList">
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr>
			<th width="28%">文件标题</th>
			<th width="16%">起草时间</th>
			<th width="10%">起草人</th>
			<th width="10%">所在步骤</th>
			<th width="10%">办理人</th>
			<th width="16%">到达时间</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="docTrackList">
  </table>
  <div class="pageNext"></div>
	<input id="hidNumPage" type="hidden" value="1"/>
	<input id="hidPageCount" type="hidden" value="15"/>
</div>
<!--跟踪文件 end-->
