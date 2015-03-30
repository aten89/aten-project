<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/usercenter/shortcut/edit_scut.js"></script>
</head>
<body class="bdDia">
  <input type="hidden" id="shortCutID" value="${shortCutMenu.shortCutMenuID }"/>
  <input type="hidden" id="windowTarget" value="${shortCutMenu.windowTarget }"/>
  <input type="hidden" id="shortCutIco" value="${shortCutMenu.logoURL }"/>
  <input type="hidden" id="type" value="${shortCutMenu.type}"/>
  <input type="hidden" id="isValid" value="${shortCutMenu.isValid}"/>
  <div class="dialogBk">
 	<div id="glzTree" class="addCon">
	  <table width="99%"  border="0" align="center" cellpadding="0"  cellspacing="1" style="margin:0 0">
	  	<tr>
	  	  <th colspan="2">快捷菜单</th>
	  	</tr>
	  	<tr>
		  <td class="tit" style="width:100px">名称</td>
		  <td><input id="shortCutName" type="text" value="${shortCutMenu.menuTitle }" class="ipt01" maxlength="50"/></td>
		</tr>
		<tr>
		  <td class="tit" style="width:100px">链接</td>
		  <td><input id="shortCutUrl" type="text" value="${shortCutMenu.url }" class="ipt01" maxlength="200"/></td>
		</tr>
		<tr>
		  <td class="tit" style="width:100px">打开方式</td>
		  <td><!--下拉框-->
		  	<div id="openTra"></div>
		  	<!--下拉框 end-->
		  </td>
		</tr>
		<tr>
		  <td class="tit" style="width:100px">链接图标</td>
		  <td style="padding:0px;">        
			<div id="iconList" class="icoShow" style="width:100%;height:158px;overflow:auto;padding:5px 0 0 10px;">
<c:forEach var="icon" items="${scmIcons}" begin="1" varStatus="status">
	          <span>
	          	<input type="radio" name="shortCutIco" value="${icon}" onclick="$('#shortCutIco').val(this.value);" class="linkIco"/><img border=0 src="${icon}" onclick="$(this).prev('input').click();"/>                   
			  </span>
	<c:if test="${status.index % 10 == 0}"> 
			  <br/>
	</c:if>
</c:forEach>
	      	</div>
          </td>
		</tr>
	  </table>
	</div>
  </div>
  <div id="console"></div>
  <div class="addTool2">
   <input id="saveShortCut" type="button" value="保存" class="allBtn" />
   <input id="closeShortCut" type="button" value="关闭" class="allBtn" />
</div>
</body>
</html>