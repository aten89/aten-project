<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
</head>
<body class="bdNone">
<!--查看日志-->
<div class="tabMid">
<div class="addCon">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
    <th colspan="6">接口服务日志</th>
  </tr>
  <tr>
	<td class="tit">系统名称</td>
	<td>${actionLog.systemName}</td>
	<td class="tit">模块名称</td>
	<td>${actionLog.moduleName} [${actionLog.moduleKey}]</td>
	<td class="tit">动作名称</td>
	<td>${actionLog.actionName} [${actionLog.actionKey}]</td>
  </tr>
  <tr>
	<td class="tit">接口帐号</td>
	<td>${actionLog.accountID}</td>
	<td class="tit">接口名称</td>
	<td>${actionLog.accountName}</td>
	<td class="tit">IP&nbsp;&nbsp;地址</td>
	<td>${actionLog.ipAddress}</td>
  </tr>
  <tr>
	<td class="tit">操作时间</td>
	<td><fmt:formatDate value="${actionLog.operateTime }" pattern="yyyy-MM-dd HH:mm"/></td>
    <td class="tit">操作结果</td>
    <td>${actionLog.resultStatus=='Y'?'成功':'失败'}</td>
    <td  class="tit">对象&nbsp;&nbsp;ID</td>
    <td>${actionLog.objectID}</td>
    </tr>
  <tr>
    <td  class="tit">对&nbsp;&nbsp;&nbsp;&nbsp;象</td>
    <td colspan="5">${actionLog.object}</td>
    </tr>
</table>
<!--查看日志 end-->
</div>
</div>
</body>
</html>
