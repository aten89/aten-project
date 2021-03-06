﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/travel/approval/draft_travel.js"></script>
<script type="text/javascript" src="page/travel/approval/comm_travel.js"></script>
<style>
.origin td.tit{color:#888; background:#fff}
.origin td{color:#888}
.origin_list th{color:#888; font-weight:normal}
</style>
</head>
<body class="bdNone">
<div class="tabMid">
   <div class="blank"></div>
  <!--出差单-->
  <div class="addCon">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      <thead>
        <tr>
          <th colspan="6">出差审批单</th>
        </tr>
      </thead>
      <tr>
        <td width="100" class="tit">出差单号</td>
        <td width="200"><input type="hidden" name="id" id="tripid" value="${busTripApply.id}"/></td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160">${busTripApply.applicantName}</td>
        <td width="100" class="tit"><span class="cRed">*</span>所属部门</td>
        <td width="230">
		  	<select id="groupname" name="groupname" class="sel02" style="width:150px">
				<c:forEach var="group" items="${depts}">
			        <option value="${group.name}" ${group.name eq busTripApply.applyDept ? 'selected':'' }> ${group.name}</option>
				</c:forEach>
		    </select>
		</td>
      </tr>
      <tr>
        <td class="tit">人员隶属</td>
        <td width="320">
          <select id="regional" name="regional" class="sel02" style="width:110px">
			<c:forEach var="d" items="${areas}" >
		        <option value="${d.dictCode }" ${d.dictCode eq busTripApply.regional ? 'selected':'' }>${d.dictName }</option>
			</c:forEach>
   		  </select>
        </td>
        <td class="tit">差旅借款</td>
        <td><input id="borrowSum" maxlength="8" size="10" type="text" style="width:100px" class="ipt01" value="${busTripApply.borrowSum}"/>元</td>
        <td class="tit">差旅性质</td>
        <td>
	       	<input class="cBox" name="termType" type="radio" checked value="短期" id="shortTerm" /><label for="shortTerm">短期</label>
	       	<input class="cBox" name="termType" type="radio" <c:if test="${busTripApply.termType=='长期'}">checked</c:if> value="长期" id="longTerm" /><label for="longTerm">长期</label>
        </td>
      </tr>
      <tr>
        <td class="tit">指定审批人</td>
        <td colspan="5">
        	<div style="float:left">
        	<input id="appointTo" type="hidden" value="${busTripApply.appointTo}"/>
			<input id="appointToName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${busTripApply.appointToName}"/>
			<input type="button" id="openAppointToSelect" class="selBtn"/>
			</div>
			<div style="color:#E3E0D5;float:left;margin:0 0 0 18px;width:160px;height:14px">必要时可指特殊人员先审批。 </div>
        </td>
      </tr>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow" style="padding:0 6px 20px 6px">
          <div class="costsListHead">
		  	<div style="width:200px;padding:0 0px 20px 0px">日程明细<span id="totalInfo"></span></div>
		  	<span><input id="tripAdd" class="allBtn" type="button" value="新增"/></span>
		  </div>
		  
          <div class="boxShadow" style="left:5px">
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <!--预算信息-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th width="26%">起止地点</th>
                            <th width="26%">出差日程</th>
                            <th width="8%">天数</th>
                            <th width="29%">出差事由</th>
                            <th width="11%">操作</th>
                          </tr>
                          <tbody id="tripList">
                          </tbody>
                        </table>
                      </div>
                      <!--预算信息 end-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
    </table>
  </div>
  <!--出差单 end-->
</div>
<div class="addTool2">
 <div>
  <input id="startFlow" type="button" value="提交" class="allBtn"/>
  <input id="saveAsDraft" type="button" value="保存草稿" class="allBtn"/>
 </div>
</div>

<script language="javascript">
	var jsonStr = '${tripJson}'; 
	var jsonObj = eval('('+jsonStr+')');
	if (jsonObj) {
		tripInfos = jsonObj.busTripApplyDetail;
		draw();
	}
</script>
</body>
</html>
