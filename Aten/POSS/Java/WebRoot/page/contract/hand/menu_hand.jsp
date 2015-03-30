<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/contract/hand/menu_hand.js"></script>
<title></title>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='cont_hand'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td class="tdValign1"><div class="costsNav" id="costsNav" >
              <h1>本机构合同上交</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="hand_reg" url="m/cont_hand/initquery"><span class="costsQdsqd flatIcon_m0"></span>上交登记</li>
                  <li id="hand_check" url="m/cont_hand/initquerycheck"><span class="costsDbsqd flatIcon_m0"></span>上交审核</li>
                </ul>
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td  class="tdValign2"><div class="costsCon" id="costsCon"> 加载页面中... </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>