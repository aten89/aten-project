<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/manage/frame_dev.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="deviceTypeCode" type="hidden" value="${param.deviceTypeCode }"/>
<input id="hidModuleRights" type="hidden" value="<oa:right key='${param.moduleKey }'/>"/>
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
	  <tbody>
	    <tr>
	      <td class="tdValign1">
	      	 <div class="costsNav" id="costsNav" >
				 <h1>设备所属地区</h1>
				 <div class="mb">
					<ul id="menuList">
	<c:forEach items="${companyAreas }" var="area">
						<li id="fuzhou" url="page/device/manage/query_dev.jsp?areaCode=${area.dictCode}&deviceTypeCode=${param.deviceTypeCode }"><img src="themes/comm/images/city_xm.gif" />${area.dictName }</li>
	</c:forEach>
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