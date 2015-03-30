<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="jqueryui/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="jqueryui/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="page/usercenter/shortcut/custom_scut.js"></script>

</head>
<body class="bdNone" onselectstart="return false;" style="-moz-user-select:none;">
<!--快捷定制-->
<div class="ShortCutConfig">
      <input type="hidden" id="shortCutIco" value=""/>
	  <h1 class="headDefault">快捷定制</h1>
	  <div class="blank" style="height:20px"></div>
	  <div class="ShortCutAdd">
	      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr>
			  <td class="tit">已定制快捷菜单</td>
			  <td class="icoList">
			  <form id="cutEnabledForm" onsumbit="return false;">
				  <div id="_customShortCut" class="noSel" style="height:140px">
<c:forEach var="shortCut" items="${enableScms}" >
					  	<div class="icoBk" onmouseover="this.className='icoBk icoOver'" onmouseout="this.className='icoBk'">
					  		<img alt="" src="${shortCut.logoURL}"/><label>${shortCut.menuTitle}</label>
					  		<input type="hidden" id="hid${shortCut.shortCutMenuID}" value="${shortCut.shortCutMenuID}"/>
					  	</div>
</c:forEach>
				  </div>
				  </form>
			  </td>
		    </tr>
		   </table>
	  </div>
	  <div class="blank" style="height:20px"></div>
	  <div class="ShortCutAdd">
	      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr>
			  <td class="tit">未启用的快捷菜单</td>
			  <td class="icoList">
			  <form id="cutDisabledForm" onSumbit="return false;">
				  <div id="_noCustomShortCut" class="noSel" style="height:140px;">
<c:forEach var="shortCut" items="${disableScms}" >
					  	<div class="icoBk" onmouseover="this.className='icoBk icoOver'" onmouseout="this.className='icoBk'">
					  		<img alt="" src="${shortCut.logoURL}"/><label>${shortCut.menuTitle}</label>
					  		<input type="hidden" id="hid${shortCut.shortCutMenuID}" value="${shortCut.shortCutMenuID}"/>
					  	</div>
</c:forEach>
				  </div>
				  </form>
			  </td>
		    </tr>
		   </table>
	  </div>
	  <div class="blank" style="height:20px"></div>
</div>
<div id="console"></div>

<div class="addTool2">
  <input id="saveShortCut" type="button" value="保存定制" class="allBtn" />
  <input id="addShortCut" type="button" value="新增快捷" class="allBtn" />
</div>
<div id="manageToolbar" style="position:absolute;display:none;text-align:center;height:40px;width:110px">&nbsp;
	<a id="modifyShortCut" class="opLink" onclick="modifyCut();">修改</a> | 
	<a id="deleteShortCut" onclick="deleteCut();" class="opLink">删除</a>
</div>
<input id="hidShortCutId" type="hidden" value="" />
</body>
</html>