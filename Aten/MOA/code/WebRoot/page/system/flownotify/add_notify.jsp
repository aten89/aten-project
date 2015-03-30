<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>添加知会人</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/system/flownotify/add_notify.js"></script>
</head>
<body class="bdDia">
<div class="addCon">
  <input type="hidden" id="holidayDays" value="0"/>
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
	  <tr>
	    <td class="tit">知会标题：</td>
	    <td>
          <input id="subject" maxlength="100" type="text" class="ipt01"/>
        </td>
      </tr>
      <tr>
	    <td class="tit">知会环节：</td>
	    <td>
	    	<input class="cBox" name="notifyType" type="radio" value="1" checked />立即知会&nbsp;
	    	<input class="cBox" name="notifyType" type="radio" value="3" />流程结束后知会
        </td>
      </tr>
      <tr>
	    <td class="tit">发起知会人：</td>
	    <td><label id="creator">${flowNotify.creator}</label> &nbsp;&nbsp;<label id="groupFullName">${flowNotify.groupFullName}</label></td>
      </tr>
  </table>
</div>

<div  class="dzbd">
	<table border="0" cellspacing="0" cellpadding="0" >
	  <tr>
		<td valign="top" width="305">
		 <div class="mkdzTip" style="padding:6px 0 0 10px">知会的人员</div>
		 <div class="mkdz">
		    <div id="bindUsers" class="bindUser" style="width:302px">
			   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
			   <thead>
				   <tr>
				     <th nowrap width="30">操作</th>
				     <th>账号</th>
					 <th>姓名</th>
				  </tr>
			   </thead>
			   <tbody></tbody>
			   </table>
			</div>
		 </div>
		</td>
		<td align="center" width="15">&nbsp;</td>
		<td valign="top" width="322">
			<div class="mkdzTip">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
					<tr>
						<td>机构：</td>
						<td><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
						<td>	
							帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:70px" /> <input id="searchGroupUsers" type="button" class="allBtn subsoW" value="检索"/>
						</td>
					</tr>
				</table>
			</div>
			<div class="mkdz">
			    <div id="unBindUser" class="bindUser" style="height:267px; width:320px">
			      	<table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
						<thead>
						<tr>
						 <th nowrap width="30">操作</th>
						 <th nowrap>帐号</th>
						 <th nowrap>名称</th>
						 <th nowrap>所属机构</th>
						</tr>
						</thead>
					 	<tbody></tbody>
					</table>
				</div>
			</div>
		</td>
	  </tr>
	</table>
</div>
	
<div class="addTool2">
	<input id="btnSave" type="button" value="确定" class="allBtn"/>
	<input id="btnCancel" type="button" value="取消" class="allBtn"/>
</div>
</body>
