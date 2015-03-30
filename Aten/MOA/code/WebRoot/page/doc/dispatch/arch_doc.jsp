<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/doc/dispatch/arch_doc.js"></script>
<input id="hidModuleRights" type="hidden" value="<oa:right key='dis_arch'/>" />
<!--发文 归档文件-->
<div class="soso" style="height:auto">
	<div class="t01">
	   <table border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td>标题：<input id="subject" type="text" class="ipt01" maxlength="30" style="width:90px"/>&nbsp;
	    	入库时间：从 <input id="beginArchiveDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    	<input id="endArchiveDate" readonly type="text" class="invokeBoth"  style="width:65px"/>&nbsp;</td>
	    	<td>状态：</td>
	    	<td><div id="passedDiv" name="passed" style="display:none"><div>**所有...</div><div>Y**通过</div><div>N**作废</div></div>&nbsp;</td>
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
			<th width="44%">文件标题</th>
			<th width="14%">类别</th>
			<th width="8%">起草人</th>
			<th width="16%">归档时间</th>
			<th width="6%">状态</th>
			<th width="12%">操作</th>
		</tr>
	</thead>
	<tbody id="docArchList">
  </table>
  <div class="pageNext"></div>
<input id="hidNumPage" type="hidden" value="1"/>
<input id="hidPageCount" type="hidden" value="15"/>
</div>
<!--发文 归档文件 end-->