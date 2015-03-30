<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
	<jsp:include page="base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Language" content="en" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>${htmlTitle}</title>
	<style type="text/css">
		html{_padding:141px 0 0;height:100%;width:100%;overflow:hidden;}
		body{height:100%;	padding:0;	margin:0;overflow:hidden;}
		.qKey img{_behavior:url("themes/comm/pngbehavior.htc");width:16px;height:16px}
		.logoImg{_behavior:url("themes/comm/pngbehavior.htc");}
		.favImg{_behavior:url("themes/comm/pngbehavior.htc");}

	</style>
	<script language="javascript" src="jqueryui/contextmenu/jquery.contextmenu.r2.js"></script>
	<script type="text/javascript" src="page/index.js"></script>
  </head>
  <body onContextMenu="return false;">
  	<input type="hidden" id="forceChangePassword" value="${forceChangePassword}"/>
  	<!--显示隐藏菜单栏-->
  	<div class="menuBarBtn hideMenuBarBtn" onclick="hideMenuBar()" title="隐藏标题栏"></div>
  	<div class="menuBarBtn showMenuBarBtn" onclick="showMenuBar()" title="显示标题栏"></div>
  	<div class="tabBarBtn hideTabBarBtn" onclick="hideTabBar()" title="隐藏TAB栏"></div>
  	<div class="tabBarBtn showTabBarBtn" onclick="showTabBar()" title="显示TAB栏"></div>
  	<!--显示隐藏菜单栏 end-->
  
	<!--头部-->
	<div class="topBk" id="_topMenuBarDIV">
	  <div class="logo" id="_logoImageDIV"><img src="themes/comm/images/defaultSysLogo.png" onerror="this.src='themes/comm/images/defaultSysLogo.png'" class="logoImg" /></div>
	  <div class="head" id="_systemHeadInfoDIV">
	  	<!--收藏夹-->
	    <div class="favFile undis" id="_favShortCutMenuDIV">
            <div class="favulBk">
             <h2><div class="myfav"></div><div  class="myfavMin" onclick="hideFavShortCutMenu()" /></div></h2>
        	 <div class="favAddButton"><a href="javascript:addToFavList();" onclick=""><img src="themes/comm/images/favAdd.png" class="favImg" style="width:18px;height:16px"/>添加到收藏夹</a></div>
        	 <ul style="overflow-x:hidden" id="_favShortCutMenuUL"></ul>
            </div>
	    </div>
	    <!--收藏夹-->
	    <div class="logo02" id="sub_system_name"></div>
	    <div class="logoShadow" id="sub_system_name_shadow"></div>
	    <div class="syIco" id="_favShortCutLink"></div>
	    <div class="tip">${sessionUser.displayName } 您好 | 部门：${not empty sessionUser.groupNames ? sessionUser.groupNames : '未分配'} <span style="display:none" id="_showSystemMenu">【<label>切换系统↓</label>】</span><span onclick="logoutSystem();">【<label>注销</label>】</span></div>
	    <div class="qKey" id="_shortCutMenuDIV"></div>
	  	
	  	<!--LOGO子系统切换-->
		<div class="subSys undis" id="_logoSysMenuDIV">
		  <table cellspacing="0" cellpadding="0" border="0">
		    <tbody>
		      <tr>
		        <td class="t01"><span></span><div></div></td>
		      </tr>
		      <tr>
		        <td class="t02">
		          <ul id="_logoSysMenuUL">
		<c:forEach var="sys" items="${subSystems}" >
			<c:if test="${sys.isValid}">
		            <li onmouseout="this.className=''" onmouseover="this.className='over'" onclick="loadModuleMenu('${sys.subSystemID}')" id="${sys.subSystemID}" logourl="${not empty sys.logoURL ? sys.logoURLPath : 'themes/comm/images/defaultSysLogo.png'}">
		              <div>
		                <img src="${not empty sys.logoURL ? sys.logoURLPath : 'themes/comm/images/defaultSysImg.png'}" onerror="this.src='themes/comm/images/defaultSysImg.png'" class="favImg" />
		                ${sys.name}
		              </div>
		            </li>
			</c:if>
		</c:forEach>
		          </ul>
		        </td>
		      </tr>
		      <tr>
		        <td class="t03"><span></span>
		          <div></div></td>
		      </tr>
		    </tbody>
		  </table>
		</div>
		<!--LOGO子系统切换 end-->
	
	  	<!--切换系统-->
	    <div class="selSys undis"  id="_toggleSysMenuDIV">
	      <ul>
	<c:forEach var="sys" items="${subSystems}" >
		<c:if test="${sys.isValid}">
	        <li onmouseout="this.className=''" onmouseover="this.className='over'" onclick="loadModuleMenu('${sys.subSystemID}')">
	          <img src="${not empty sys.logoURL ? sys.logoURLPath : 'themes/comm/images/defaultSysImg.png'}" onerror="this.src='themes/comm/images/defaultSysImg.png'" class="logoSmall favImg" />${sys.name}
	        </li>
	    </c:if>
	</c:forEach>
	      </ul>
	    </div>
	    <!--切换系统 end-->
	    
	  </div>
	  
	  <!--菜单-->
	  <div class="nav">
	    <div class="nTip">
	      <div class="level01" id="_moduleMenu01thDIV" style="overflow:visible">
	        <ul class="navOption" id="_fixedModuleMenuUL" style="float:right"></ul>
	        <ul class="navOption" id="_subSysModuleMenuUL"></ul>
	      </div>
	      <div class="level02" id="_moduleMenu02thDIV">
	        <ul id="_moduleMenu02thUL"></ul>
	      </div>
	    </div>
	  </div>
	  <!--菜单 end-->
	  
	  <!--更多二级菜单-->
	  <div class="moreMenu2Div undis"  id="_toggleMoreMenu2thDIV">
	    <ul></ul>
	  </div>
	  <!--更多二级菜单 end-->	
	</div>
	<!--头部 end-->
	
	<div class="conBk" id="_bottomConcentDIV">
	  <div class="conH100">
	    <div class="content">
		  <div class="conRight">
			<div class="conRtopLine">
			  <a id="btnTurnPrev" style="display:none" href="javascript:void(0);" onclick="$.getMainFrame().turnPrevPage();" class="tabPageOver" style="margin-left:1px"><button class="tabUp"></button></a>
			  <a id="btnTurnNext" style="display:none" href="javascript:void(0);" onclick="$.getMainFrame().turnNextPage();" class="tabPageOver tabDownPosi"><button class="tabDown"></button></a>
			</div>
			
			<!--TAB右键菜单-->
			<div id="tabContextMenu" class="rightKey" style="position:absolute;top:80px;left:-12px; z-index:18;display:none;">
			  <ul>
				<li id="liFav" style="display:none"><button class="rKeyFav"></button>添加收藏</li>
				<li id="liRefresh" style="display:none"><button class="rkeyFlash"></button>刷新</li>
				<li id="liClose" style="display:none"><button class="rkeyClose"></button>关闭</li>
				<li id="liCloseOther" style="display:none"><button class="rkeyOther"></button>关闭其他</li>
				<li id="liCloseAll" style="display:none"><button class="rkeyAll"></button>关闭所有</li>
			  </ul>
			</div>
			<!--TAB右键菜单 end-->	
			
			<div class="sideBar">
			  <ul></ul>
			</div>
		  </div>
		  <div id="contentPanel" class="conLeft"></div>
		</div>
	  </div>
	</div>
  </body>
</html>