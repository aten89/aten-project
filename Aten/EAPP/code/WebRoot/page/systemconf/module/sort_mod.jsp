<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!--系统模块 排序-->
<div class="addCon">
	<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">
	  <tr>
		<td>
		   <div  class="dzbd">
		   <table width="300"  align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone" style="margin:0 auto">
			  <tr>
				<td width="230" >
				<select id="childModules" size="1" multiple="multiple" class="dzbdSel01" style="height:295px">
<c:forEach var="md" items="${modules}" >
				<option value="${md.moduleID}">${md.name}</option>
</c:forEach>
				</select>
				</td>
				<td width="70">
				   <input id="setTop" type="button" value="移到顶部" class="allBtn pxBtn"/><br />
				   <input id="setUp" type="button" value="上移" class="allBtn pxBtn"  /><br />
				   <input id="setDown" type="button" value="下移" class="allBtn pxBtn" /><br />
				   <input id="setBottom" type="button" value="移到底部" class="allBtn pxBtn"/>
				</td>
			  </tr>
			</table>
		   </div>
		   <div class="blank" style="height:10px"></div>
		</td>
	  </tr>
	</table>
</div>
<div class="addTool2" >
	<input id="saveOrder" type="button" value="保存" class="allBtn"/>
	<input id="reset" type="reset" value="重置" class="allBtn"/>
</div>
<script type="text/javascript" src="page/systemconf/module/sort_mod.js"></script>
<!--系统模块  排序 end-->