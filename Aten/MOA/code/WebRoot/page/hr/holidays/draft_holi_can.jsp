<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/holidays/draft_holi_can.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<input type="hidden" id="holiId" value="${holidayApply.id}"/>
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="6">请假单</th>
		</tr>
      </thead>
	  <tr> 	
	 	<td width="100" class="tit">请假单号</td>
        <td width="200">${holidayApply.id}</td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${holidayApply.applicantName}</td>
        <td width="100" class="tit">所在部门</td>
        <td width="230">${holidayApply.applyDept}</td>
	  </tr>
	  <tr>
        <td class="tit">人员隶属</td>
	    <td>${holidayApply.regionalName}</td>
	    <td class="tit">指定审批人</td>
	    <td colspan="3">${holidayApply.appointToName}</td>
      </tr>
<c:if test="${holidayApply.isSpecial}">   
     <tr>
        <td class="tit"  colspan="6" style="text-align:left;width:auto">
          <span style="color:red;font-size:13px;font-weight:bold">该员工申请了特批，以下是申请的理由：</span></td>
      </tr>
      <tr id="specialReasonPanel" style='display:${holidayApply.isSpecial==true ? "": "none"}'>
        <td class="tit">特批理由</td>
        <td colspan="5">${holidayApply.specialReason}</td>
      </tr>
</c:if>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
       <tr>
        <td colspan="6"  class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current default">
              <div class="lastNone" style="cursor:default">请假单明细</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <!--预算信息-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th width="13%">假期类型</th>
                            <th width="18%">请假时间</th>
                            <th width="10%">天数</th>
                            <th width="25%">附加说明</th>
                            <th width="10%">销假天数</th>
                            <th width="25%">销假说明</th>
                          </tr>
                      	<c:forEach var="holidayApplyDetail" items="${holidayApply.holidayDetail}" >
							<tr>
                            <td>${holidayApplyDetail.holidayName }</td>
                            <td>从 <fmt:formatDate value="${holidayApplyDetail.startDate }" pattern="yyyy-MM-dd"/>${holidayApplyDetail.startTimeStr } 到 <fmt:formatDate value="${holidayApplyDetail.endDate }" pattern="yyyy-MM-dd"/>${holidayApplyDetail.endTimeStr } </td>
                            <td>${holidayApplyDetail.days }天</td>
                            <td>${holidayApplyDetail.remark}</td>
                            <td class="cancelInfo" id="${holidayApplyDetail.id}">
                            	<input name="cancelDays" maxlength="10" type="text" style="width:60px" class="ipt01" value="${holidayApplyDetail.cancelDays}"/></td>
                            <td><input name="cancelRemark" maxlength="10" type="text" style="width:120px" class="ipt01" value="${holidayApplyDetail.cancelRemark}"/></td>
                          </tr>
						</c:forEach>
                        </table>
                      </div>
                      <!--预算信息 end-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
         </td>
      </tr>
  </table>
  
  <div class="blank"></div>
  
</div>
<!--请假单 end-->
 
<div class="addTool2">
	<span id="notifyUserNames" style="display:none;color:blue;"></span>
	<div class="blank" style="height:5px;"></div>
	<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'提交的销假申请', 'XJLC', '/m/holi_arch?act=toview&id=');"/>&nbsp;
	<input id="startFlow" type="button" value="提交" class="allBtn"/>
	<input id="cancel" type="button" value="取消" class="allBtn"/>
</div>
</body>
