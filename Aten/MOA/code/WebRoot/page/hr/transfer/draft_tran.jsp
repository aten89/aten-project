<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/hr/transfer/draft_tran.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
  <!--请假单-->
  <div class="addCon">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      <thead>
        <tr>
          <th colspan="6">基本信息</th>
        </tr>
      </thead>
      <tr>
      	<td width="100" class="tit">申请单号</td>
        <td width="200"><input type="hidden" name="id" id="tranid" value="${transferApply.id}"/></td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${transferApply.applicantName}</td>
        <td width="100" class="tit">申请时间</td>
        <td width="230"><fmt:formatDate value="${transferApply.applyDate }" pattern="yyyy-MM-dd HH:mm"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferUserName">*</span>异动员工</td>
        <td>
          <div style="float:left">
			<input id="transferUserName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${transferApply.transferUserName}"/>
			<input type="button" id="openUserSelect" class="selBtn"/>
		  </div>
        </td>
        <td class="tit">员工工号</td>
        <td id="transferUser">${transferApply.transferUser}</td>
        <td class="tit">合同主体</td>
        <td>
          <input id="contractBody" maxlength="40" type="text" class="ipt01" value="${transferApply.contractBody}"/>
        </td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
        <td>
          <input id="contractStartDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${transferApply.contractStartDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同结束时间</td>
        <td>
          <input id="contractEndDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${transferApply.contractEndDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
    	<td class="tit"><span class="cRed" valid="transferDate">*</span>调动日期</td>
        <td>
          <input id="transferDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${transferApply.transferDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferOutDept">*</span>调出部门</td>
        <td>
          <div style="float:left">
			<input id="transferOutDept" readonly maxlength="40" type="text" style="width:70%" class="ipt01" value="${transferApply.transferOutDept}"/>
			<input type="button" id="openOutDeptSelect" class="selBtn"/>
		  </div>
    	</td>
    	<td class="tit"><span class="cRed" valid="transferOutPost">*</span>调出前岗位</td>
        <td>
          <input id="transferOutPost" maxlength="40" type="text" class="ipt01" value="${transferApply.transferOutPost}"/>
        </td>
        <td class="tit"><span class="cRed" valid="entryDate">*</span>入司日期</td>
        <td>
          <input id="entryDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${transferApply.entryDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferInDept">*</span>调入部门</td>
        <td>
          <div style="float:left">
			<input id="transferInDept" readonly maxlength="40" type="text" style="width:70%" class="ipt01" value="${transferApply.transferInDept}"/>
			<input type="button" id="openInDeptSelect" class="selBtn"/>
		  </div>
    	</td>
    	<td class="tit"><span class="cRed" valid="transferInPost">*</span>调入后岗位</td>
        <td>
          <input id="transferInPost" maxlength="40" type="text" class="ipt01" value="${transferApply.transferInPost}"/>
        </td>
        <td class="tit"><span class="cRed" valid="reportDate">*</span>报到时间</td>
        <td>
          <input id="reportDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${transferApply.reportDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferReason">*</span>调动原因</td>
        <td colspan="5"><textarea id="transferReason" class="area01">${transferApply.transferReason}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">工作交接内容</td>
        <td colspan="5"><textarea id="transitionContent" class="area01">${transferApply.transitionContent}</textarea></td>
      </tr>
      <thead>
        <tr>
          <th colspan="6">调动支持信息</th>
        </tr>
      </thead>
      <tr>
      	<td class="tit">财务支持人员</td>
        <td>
          <div style="float:left">
			<input id="finaceSupportUserName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${transferApply.finaceSupportUserName}"/>
			<input id="finaceSupportUser" type="hidden" value="${transferApply.finaceSupportUser}"/>
			<input type="button" id="openFIUserSelect" class="selBtn"/>
		  </div>
        </td>
        <td class="tit">财务支持电话</td>
        <td>
          <input id="finaceSupportTel" maxlength="40" type="text" class="ipt01" value="${transferApply.finaceSupportTel}"/>
        </td>
        <td class="tit">财务支持邮箱</td>
        <td>
          <input id="finaceSupportEmail" maxlength="40" type="text" class="ipt01" value="${transferApply.finaceSupportEmail}"/>
        </td>
      </tr>
      <tr>
		<td class="tit">IT支持人员</td>
        <td>
          <div style="float:left">
			<input id="itSupportUserName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${transferApply.itSupportUserName}"/>
			<input id="itSupportUser" type="hidden" value="${transferApply.itSupportUser}"/>
			<input type="button" id="openITUserSelect" class="selBtn"/>
		  </div>
        </td>
        <td class="tit">IT支持电话</td>
        <td>
          <input id="itSupportTel" maxlength="40" type="text" class="ipt01" value="${transferApply.itSupportTel}"/>
        </td>
        <td class="tit">IT支持邮箱</td>
        <td>
          <input id="itSupportEmail" maxlength="40" type="text" class="ipt01" value="${transferApply.itSupportEmail}"/>
        </td>
      </tr>
      <tr>
        <td class="tit">人力支持人员</td>
        <td>
          <div style="float:left">
			<input id="hrSupportUserName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${transferApply.hrSupportUserName}"/>
			<input id="hrSupportUser" type="hidden" value="${transferApply.hrSupportUser}"/>
			<input type="button" id="openHRUserSelect" class="selBtn"/>
		  </div>
        </td>
        <td class="tit">人力支持电话</td>
        <td>
          <input id="hrSupportTel" maxlength="40" type="text" class="ipt01" value="${transferApply.hrSupportTel}"/>
        </td>
        <td class="tit">人力支持邮箱</td>
        <td>
          <input id="hrSupportEmail" maxlength="40" type="text" class="ipt01" value="${transferApply.hrSupportEmail}"/>
        </td>
      </tr>
      <tr>
        <td class="tit">变更后办公地</td>
        <td>
          <input id="changeOffice" maxlength="40" type="text" class="ipt01" value="${transferApply.changeOffice}"/>
        </td>
        <td class="tit">周汇报线</td>
        <td>
          <input id="weeklyReportTo" maxlength="40" type="text" class="ipt01" value="${transferApply.weeklyReportTo}"/>
        </td>
        <td class="tit">月汇报线</td>
        <td>
          <input id="monthlyReportTo" maxlength="40" type="text" class="ipt01" value="${transferApply.monthlyReportTo}"/>
        </td>
      </tr>
    </table>
  </div>
  <!--请假单 end-->
</div>
<br/>
<div class="addTool2">
	<span id="notifyUserNames" style="display:none;color:blue;"></span>
	<div class="blank" style="height:5px;"></div>
	<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'提交的异动申请', 'YDLC', '/m/tran_arch?act=toview&id=');"/>&nbsp;
	<input id="startFlow" type="button" value="提交" class="allBtn"/>
	<input id="saveAsDraft" type="button" value="保存草稿" class="allBtn"/>
</div>
</body>
</html>