<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/report/hr/menu_rpt.js"></script>
<script type="text/javascript" src="page/report/dialog_dept.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="commui/escape/strutil.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<eapp:menu key='stat_hr'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td class="tdValign1"><div class="crmSubNav crmSubLevel" id="costsNav">
              <h1>人力类</h1>
              <div class="mb" style="text-align:left;">
                <ul id="menuList">
                  <li id="GRTJ" url="page/report/hr/rpt_grtj.jsp">个人统计</li>
                  <li id="YXRL" url="page/report/hr/rpt_yxrltj.jsp">有效人力统计</li>
                  <li id="TDYXRL" url="page/report/hr/rpt_tdyxrl.jsp">团队有效人力统计</li>
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