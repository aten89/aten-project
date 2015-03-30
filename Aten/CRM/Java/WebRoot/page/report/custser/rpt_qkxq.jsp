<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<script type="text/javascript" src="page/report/custser/rpt_qkxq.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<input id="hidModuleRights" type="hidden" value="<eapp:right key='QKXQ'/>" />
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
		  <td>所属团队：</td>
		  <td><input id="deptName" readonly type="text" class="ipt01" style="width:110px"/>
	       <button id="showGroup" class="selBtn"></button>&nbsp;&nbsp;</td>
		  <td>投资顾问：</td>
		  <td><div id="userIdDiv" name="userIdSel"></div>&nbsp;&nbsp;</td>
          <td>客户名称：</td>
		  <td><input id="custName" type="text" class="ipt01" style="width:80px"/>&nbsp;&nbsp;</td>
          <td>回访状态：</td>
          <td>
		  	<div id="statusDiv" name="statusSel">
		  		<div>**所有...</div>
		  		<div isselected="true">3**通过</div>
		  		<div>-1**不通过</div>
		  		<div>2**驳回</div>
		  		<div>1**待回访</div>
		  	</div>&nbsp;&nbsp;&nbsp;
	      </td>
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
          <td>潜客通过时间：</td>
          <td>
          	<input readonly id="bgnPassTime" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endPassTime" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;
		  </td>
		  <td>潜客提交时间：</td>
          <td>
          	<input readonly id="bgnSubmitTime" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endSubmitTime" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;
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
		  <td>最后回访时间：</td>
          <td>
          	<input readonly id="bgnVisitTime" type="text" class="invokeBoth" style="width:70px"/>
			到<input readonly  id="endVisitTime" type="text" class="invokeBoth" style="width:70px"/>&nbsp;&nbsp;
		  </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<!--列表-->
<iframe id="rptFrame" name="rptFrame" frameborder="0" width="100%" ></iframe>
<!--列表 end-->
