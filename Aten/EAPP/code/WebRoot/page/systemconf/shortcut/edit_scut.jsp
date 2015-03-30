<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
	<input type="hidden" id="shortCutID" value="${shortCutMenu.shortCutMenuID }"/>
	<input type="hidden" id="windowTarget" value="${shortCutMenu.windowTarget }"/>
	<input type="hidden" id="menuStatus" value="${shortCutMenu.isValid ? '1' : '0' }"/>
	<input type="hidden" id="menuIcon" value="${shortCutMenu.logoURL }"/>
	<input type="hidden" id="type" value="${shortCutMenu.type}"/>
	<!--系统新增-->
	<div class="addCon tabMid">
	  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	  <tr>
		<td class="tit">菜单标题</td>
		<td>
			<input id="menuTitle" type="text" readonly class="ipt01" value="${shortCutMenu.menuTitle }" maxlength="30"/>
		</td>
		<td class="tit">菜单链接</td>
		<td>
			<input id="menuLink" type="text" readonly class="ipt01" value="${shortCutMenu.url }" maxlength="200" />
		</td>
	  </tr>
	  <tr>
		<td class="tit">打开方式</td>
		<td>
		  <!--下拉框-->
		  <div id="typeSelect"></div>
		  <!--下拉框 end-->
		  </td>
		<td class="tit">状&nbsp;&nbsp;&nbsp;&nbsp;态</td>
		<td id="statusTD">
			<input type="radio" ${shortCutMenu.isValid ? 'checked' : ''} id="status_true" name="status" value="1" onclick="$('#menuStatus').val(this.value);"><label for="status1">启用</label>&nbsp;&nbsp;
			<input type="radio" ${shortCutMenu.isValid ? '' : 'checked'} name="status" value="0"  onclick="$('#menuStatus').val(this.value);"><label for="status2">停用</label>
		</td>
	  </tr>
	  <tr>
		<td class="tit">菜单图标</td>
		<td colspan="3" style="padding:3px 0 0 10px">
		<div id="iconList" class="icoShow" >
<c:forEach var="icon" items="${icons}" begin="1" varStatus="status">
				<span>
				  <input type="radio" name="menuIcon" value="${icon}" onclick="$('#menuIcon').val(this.value);" /><img border=0 src="${icon}" onclick="$(this).prev('input').click();"/>
				</span>
	<c:if test="${status.index % 16 == 0}"> 
			  <br/>
	</c:if>
</c:forEach>
		</div>
		</td>
	  </tr>
	  
	</table>
	</div>
	<div class="addTool2">
	  <input id="btnSave" type="button" value="保存" class="allBtn"/>
	  <input id="btnSaveAndAdd" type="button" value="保存并新增" class="allBtn"/>
	  <input id="btreset" type="reset" value="重置" class="allBtn"/>
	</div>
	<script type="text/javascript" src="page/systemconf/shortcut/edit_scut.js"></script>