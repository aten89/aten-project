<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/hr/staffflow/menu_staff.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='staff_flow'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td class="tdValign1"><div class="costsNav" id="costsNav" >
              <h1>审批流程</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="staff_start" url="page/hr/staffflow/start_staff.jsp"><span class="costsQdsqd"></span>启动</li>
                  <li id="staff_Deal" url="page/hr/staffflow/deal_staff.jsp"><span class="costsDbsqd"></span>待办</li>
                  <li id="staff_Track" url="page/hr/staffflow/track_staff.jsp"><span class="costsGzsqd"></span>跟踪</li>
                  <li id="staff_Arch" url="page/hr/staffflow/arch_staff.jsp"><span class="costsGdsqd"></span>归档</li>
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