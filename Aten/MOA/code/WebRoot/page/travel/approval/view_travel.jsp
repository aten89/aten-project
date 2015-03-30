<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/travel/approval/view_travel.js"></script>
<style>
.clgcTit li{width:168px}
.origin td.tit{color:#888; background:#fff}
.origin td{color:#888}
.origin_list th{color:#888; font-weight:normal}
</style>
</head>
<body class="bdNone">
<input type="hidden" id="tripAppId" value="${oldTripApply.id}"/>
<div class="tabMid">
  <!--出差单-->
  <div class="addCon">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      <thead>
        <tr>
          <th colspan="6">出差审批单</th>
        </tr>
      </thead>
      <tr>
        <td width="100" class="tit"><div style="width:90px; float:right">出差单号</div></td>
        <td width="160">${busTripApply.id}
        </td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160">${busTripApply.applicantName}</td>
        <td class="tit">所属部门</td>
        <td>${busTripApply.applyDept}</td>
      </tr>
      <tr>
        <td class="tit">人员隶属</td>
        <td width="180">${busTripApply.regionalName}</td>
        <td class="tit">差旅借款</td>
        <td><fmt:formatNumber value="${busTripApply.borrowSum }" pattern="0.00"/>元</td>
        <td class="tit">差旅性质</td>
        <td>${busTripApply.termType}</td>
      </tr>
      <tr>
        <td class="tit">指定审批人</td>
        <td colspan="5">${busTripApply.appointToName}</td>
      </tr>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow">
          <span class="crmSubTabsBk">
	        <ul class="crmSubTabs">
	            <li class="current default">
	              <div class="lastNone" style="cursor:default">日程明细</div>
	            </li>
	        </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <!--信息-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th width="28%">起止地点</th>
                            <th width="26%">出差日程</th>
                            <th width="10%">天数</th>
                            <th width="36%">出差事由</th>
                          </tr>
                      	<c:forEach var="applyDetail" items="${busTripApply.busTripApplyDetail}" >
							<tr>
                            <td>${applyDetail.fromPlace }——${applyDetail.toPlace }</td>
                            <td>从 <fmt:formatDate value="${applyDetail.startDate }" pattern="yyyy-MM-dd"/> 到 <fmt:formatDate value="${applyDetail.endDate }" pattern="yyyy-MM-dd"/> </td>
                            <td style="color:red;font-weight:bold">${applyDetail.days }天</td>
                            <td>${applyDetail.causa}</td>
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
  </div>
  <!--申请单 end-->
  <div class="blank"></div>
  <div class="costsLog" id="costsLog">
    <h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
    <div class="mb">
 		<c:forEach var="task" items="${tasks}" >
            <div class="processBk">
               <div class="clgcTit">
                  <ul class="clearfix">
                    <li style="width:120px"><b>处理人：</b>${task.transactorDisplayName }</li>
                    <li style="width:150px"><b>审批步骤：</b>${task.nodeName }</li>
                    <li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                    <li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                    <li style="width:180px"><b>处理状态：</b>
                    <c:if test="${task.taskState=='ps_create'}">
                        <img src="themes/comm/images/stateIcoNone.gif">[未查看]
                    </c:if>
                    <c:if test="${task.taskState=='ps_start'}">
                        <img src="themes/comm/images/stateIco02.gif">[已查看,未处理]
                    </c:if>
                    <c:if test="${task.taskState=='ps_end'}">
                        <img src="themes/comm/images/stateIco01.gif">[已处理]
                    </c:if>
                    </li>
                   </ul>
                 </div>
               <div class="taskCon nonebb">
             <div class="handleCon"> ${task.comment } </div>
           </div>
         </div>
       </c:forEach>    
    </div>
  </div>
  <div class="blank"></div>  
</div>
<div class="addTool2">
 <input class="allBtn" type="button" value="关闭" onclick="$.getMainFrame().getCurrentTab().close();" style="margin-left:50px"/>
</div>
</body>
</html>