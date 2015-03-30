<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="dev" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/device/paramconf/menu_param.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<dev:menu key='dev_paramset'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
	  <tbody>
	    <tr>
	      <td class="tdValign1">
	      	 <div class="costsNav" id="costsNav" >
				 <h1>参数设置</h1>
				 <div class="mb">
					<ul id="menuList">
					  <li id="deviceProperty" url="page/device/paramconf/property/query_prop.jsp"><img src="themes/comm/images/deviceProperty.gif"/>设备属性</li>
					  <li id="deviceClass" url="page/device/paramconf/class/query_class.jsp"><img src="themes/comm/images/devicePropertyConfig.gif" />设备类别</li>
					  <li id="areaCodeDev" url="page/device/paramconf/area/query_area.jsp"><img src="themes/comm/images/deviceClassConfig.gif"/>区域设备</li>
					  <li id="checkOption" url="page/device/paramconf/check/query_item.jsp"><img src="themes/comm/images/deviceCheck.gif" />验收检查项</li>
					  <li id="assginMamager" url="page/device/paramconf/assign/query_assign.jsp"><img src="themes/comm/images/deviceInventoryConfig.gif" />授权管理</li>
					</ul>
				 </div>
			  </div>
	      </td>
	      <td><div class="vBlank"></div></td>
	      <td  class="tdValign2">
		      <div class="costsCon" id="costsCon">
				  加载页面中...
			  </div>
	      </td>
	    </tr>
	  </tbody>
	</table>
  </div>
</div>
</body>
</html>