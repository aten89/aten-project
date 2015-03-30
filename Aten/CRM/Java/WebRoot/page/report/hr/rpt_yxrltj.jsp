<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<script type="text/javascript" src="page/report/hr/rpt_yxrltj.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<input id="hidModuleRights" type="hidden" value="<eapp:right key='YXRL'/>" />
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01 t_f01">
	  <input type="button" class="queryAssign_btn" id="queryAssign"/>
	  <input type="button" class="confNorm_btn"  id="saveNorm"/>
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
          <td>查询类型：
		  	<input class="cBox" name="personalOpt" type="radio" value="1" checked onclick="clickPersonal(true);"/>个人&nbsp;
	      	<input class="cBox" name="personalOpt" type="radio" value="0" onclick="clickPersonal(false);"/>机构&nbsp;&nbsp;&nbsp;</td>
		  <td>所属机构：</td>
		  <td><input id="deptName" disabled readonly type="text" class="ipt01" style="width:110px"/>
	       <button id="showGroup" disabled class="selBtn"></button>&nbsp;&nbsp;</td>
		  <td>员工：</td>
		  <td><div id="userIdDiv" name="userIdSel"></div>&nbsp;&nbsp;</td>
		  <td>查询月份：
		  	<select id="yearSel" class="sel02" style="width:65px"></select>
			  <select id="monthSel" class="sel02" style="width:50px">
				<option value="01">01月</option>
				<option value="02">02月</option>
				<option value="03">03月</option>
				<option value="04">04月</option>
				<option value="05">05月</option>
				<option value="06">06月</option>
				<option value="07">07月</option>
				<option value="08">08月</option>
				<option value="09">09月</option>
				<option value="10">10月</option>
				<option value="11">11月</option>
				<option value="12">12月</option>
			  </select>&nbsp;&nbsp;&nbsp;</td>
          <td>
          	<input type="button" value="查询" class="allBtn" id="query">&nbsp;
          	<input id="exportExcel" class="allBtn" type="button" value="导出"/>&nbsp;
          	<div id="fullViewDiv" style="float:right;width:16px;padding:2px 2px 0 0;cursor:pointer;display:none"><input type="image" src="themes/comm/images/fullSscreen.png" id="fullView" title="全屏查看"/></div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<!--列表-->
<iframe id="rptFrame" name="rptFrame" frameborder="0" width="100%" ></iframe>
<!--列表 end-->
