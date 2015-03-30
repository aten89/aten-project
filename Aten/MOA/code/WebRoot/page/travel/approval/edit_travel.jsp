<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>添加出差信息</title>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/travel/approval/edit_travel.js"></script>
</head>
<body class="bdDia">
 <input type="hidden" id="tripDays" value="0" />
<div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
    <thead>
      <tr>
        <th colspan="2">出差信息</th>
      </tr>
    </thead>
    <tr>
      <td class="tit"><div style="width:66px;float:right"><span class="cRed">*</span>出发地点</div></td>
      <td><input id="fromPlace"  maxlength="40" type="text" style="width:345px" class="ipt01" /></td>
    </tr>
    <tr>
      <td class="tit"><div  style="width:66px;float:right"><span class="cRed">*</span>到达地点</div></td>
      <td><input maxlength="40" id="toPlace" type="text" style="width:345px" class="ipt01" /></td>
    </tr>    
    <tr>
      <td class="tit"><span class="cRed">*</span>出差日程</td>
      <td> 从&nbsp;<input id="startDate" readonly type="text" class="invokeBoth" style="width:66px" onchange="getDays();"/>
         到&nbsp;<input id="endDate" readonly type="text" class="invokeBoth" style="width:66px" onchange="getDays();"/>
        <span id="totalDays" class="dataCount" style="float:none"></span>
      </td>
    </tr>
    <tr>
      <td class="tit">出差事由</td>
      <td><textarea class="area01" id="causa" style="width:345px"></textarea></td>
    </tr>
  </table>
</div>
<div class="addTool2">
 <div>
  <input id="saveBtn" type="button" value="确定" class="allBtn" />
  <input id="closeBtn" type="button" value="取消" class="allBtn"/>
 </div>
</div>
</body>
