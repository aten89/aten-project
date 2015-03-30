<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/knowledge/paramconf/query_label.js"></script>
<input id="hidModuleRights" type="hidden" value="<oa:right key='kb_label'/>"/>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input type="button" class="add_btn" id="add"/>
		<input type="button" class="flash_btn" id="reflash"/>
	</div>
</div>

 <div class="soso" style="height:auto;margin-top:1px">
		<div style="padding:2px 0 3px 33px">
			关键字：<input id="labelName"  type="text" class="ipt01" maxlength="100" style="width:65px"/>&nbsp;
			点击次数：从 <input  id="startCount"  type="text" class="ipt01"  style="width:65px"/> 到 
			<input  id="endCount" type="text" class="ipt01"  style="width:65px"/>
			<input id="searchLog" class="allBtn" type="button" value="搜索"/>&nbsp;
			<input id="del" class="allBtn" type="button" value="删除"/>&nbsp;
		</div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="flowSet">
  <thead>
	<tr>
		<th width="37%">关键字</th>
		<th width="18%">关键字属性</th>
		<th width="15%">关键字点击数</th>
		<th width="18%">创建时间</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
  <tbody>
  </tbody>
</table>
</div>
<!--列表 end-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/>
<div class="pageNext"></div> 
