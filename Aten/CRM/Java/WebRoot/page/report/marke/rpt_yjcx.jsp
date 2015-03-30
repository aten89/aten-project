<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<script type="text/javascript" src="page/report/marke/rpt_yjcx.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<input id="hidModuleRights" type="hidden" value="<eapp:right key='YJCX'/>" />
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01 t_f01">
	  <input type="button" class="queryAssign_btn" id="queryAssign"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso">
<input id="currentUser" type="hidden" value="${sessionUser.accountID}"/>
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>查询个人业绩：
		  	<input class="cBox" name="personalOpt" type="radio" value="1" checked onclick="clickPersonal(true);"/>是&nbsp;
	      	<input class="cBox" name="personalOpt" type="radio" value="0" onclick="clickPersonal(false);"/>否&nbsp;&nbsp;&nbsp;</td>
		  <td>所属团队：</td>
		  <td>
		   <input id="deptName" disabled readonly type="text" class="ipt01" style="width:110px"/>
	       <button id="showGroup" disabled class="selBtn"></button>&nbsp;&nbsp;
	      </td>
		  <td>投资顾问：</td>
		  <td><div id="userIdDiv" name="userIdSel"></div>&nbsp;&nbsp;</td>
		  <td>客户名称：<input id="custName" type="text" class="ipt05" style="width:80px"/>&nbsp;&nbsp;&nbsp;</td>
          <td>
          	<a href="javascript:void(0)" onclick="showMoreQueryField(this)" style="color:red">更多条件↓</a>&nbsp;
          	<input type="button" value="查询" class="allBtn" id="query">&nbsp;
          	<input id="exportExcel" class="allBtn" type="button" value="导出"/>&nbsp;
          	<div id="fullViewDiv" style="float:right;width:16px;padding:2px 2px 0 0;cursor:pointer;display:none"><input type="image" src="themes/comm/images/fullSscreen.png" id="fullView" title="全屏查看"/></div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<div class="soso moreSoso" style="display:none">
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>项目名称：</td>
          <td><div id="prodInfoDiv" name="prodInfoSel"></div>&nbsp;&nbsp;</td>
          <td>到账日期：<input readonly id="bgnTransferDate" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endTransferDate" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;</td>
          <td>到账金额：<input id="bgnTransferAmount" type="text" class="ipt05" style="width:50px"/>
          到<input id="endTransferAmount" type="text" class="ipt05" style="width:50px"/>&nbsp;&nbsp;</td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<div class="soso moreSoso" style="display:none">
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>产品兑付日期：<input readonly id="bgnProdCashDate" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endProdCashDate" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;</td>
          <td>实际兑付日期：<input readonly id="bgnActualCashDate" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endActualCashDate" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;</td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<!--列表-->
<iframe id="rptFrame" name="rptFrame" frameborder="0" width="100%" ></iframe>
<!--列表 end-->
