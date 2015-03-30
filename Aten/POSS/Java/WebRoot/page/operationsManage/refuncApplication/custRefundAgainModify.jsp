<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/ui.datepicker.special.js"></script>

<script type="text/javascript" src="${ermpPath}oa/commui/attachment/oa.attachment.json.js"></script>
<link href="${ermpPath}oa/commui/attachment/style/attachment.css" rel="stylesheet"	type="text/css"></link>
<script type="text/javascript" src="${ermpPath}jqueryui/eapp.common.js"></script>

<script type="text/javascript" src="page/operationsManage/refuncApplication/custRefundAgainModify.js"></script>
<title></title>
</head>
<body class="crmBd">
<input type="hidden" id="idHid" value="${custRefund.id }"/>
<input type="hidden" id="saleManIdHid" value="${custRefund.saleManId }"/>
<input type="hidden" id="custIdHid" value="${custRefund.custId }"/>
<input type="hidden" id="prodInfoIdHid" value="${custRefund.prodInfo.id }"/>
<input type="hidden" id="paymentIdHid" value="${custRefund.paymentId }"/>
<input type="hidden" id="fullRefundFlagHid" value="${custRefund.fullRefundFlag }"/>

<input type="hidden" id="saleManId" value="${userAccountID }"/>

<form name="mainForm">
<div class="addCon openWinW03" style="margin-left: auto;margin-right: auto;">
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>客户经理：</td>
		<td><input id="saleManName" type="text" disabled="disabled" maxlength="40" class="ipt05" value="${userDisplayName }"/></td>
		<td class="tit"><span class="cRed">*</span>申请时间： </td>
		<td><input readonly="readonly" name="applyTime" id="applyTime" type="text" class="invokeBoth" value="${!empty custRefund.applyTimeStr ? custRefund.applyTimeStr : nowDate }"/></td>
	</tr>
    <tr>
      	<td class="tit"><span class="cRed">*</span>流程标题：</td>
		<td colspan="3"><input id="flowTitle" type="text" maxlength="100" class="ipt05" style="width: 100%;" value="${custRefund.flowTitle }"/></td>
    </tr>
    <tr>
      	<td class="tit"><span class="cRed">*</span>客户名称：</td>
		<td colspan="2">
			<div style="width: 100%;">
				<div id="customerInfoSel" name="customerInfoSel"></div>
			</div>
		</td>
		<td><a href="javascript:void();" onclick="viewCustomerInfo();">查看客户详细信息</a></td>
    </tr>
    <tr>
    	<td class="tit">客户性质：</td>
		<td colspan="3"><span id="customerProperty"></span></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>认购产品：</td>
		<td colspan="2">
			<div id="prodInfoSel" name="prodInfoSel"></div>
		</td>
		<td><a href="javascript:void();" onclick="viewProductInfo();">查看产品详细信息</a></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>项目所属供应商：</td>
		<td colspan="3"><span id="supplier"></span></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>我的划款登记：</td>
		<td colspan="3">
			<div id="custPaymentSel" name="custPaymentSel"></div>
		</td>
    </tr>
    <tr>
    	<td class="tit">划款金额(万)：</td>
		<td><span id="transferAmount"></span></td>
		<td class="tit">打款时间：</td>
		<td><span id="transferDate"></span></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>是否全额退款：</td>
    	<td>
    		<div id="fullRefundFlagSel" name="fullRefundFlagSel">
				<div>true**是</div>
				<div isselected="true">false**否</div>
			</div>
    	</td>
    	<td class="tit"><span class="cRed">*</span>退款金额（万）：</td>
    	<td><input id="refundAmount" type="text" maxlength="40" disabled="disabled" class="ipt05" value="${custRefund.refundAmount }"/></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>退款原因：</td>
    	<td colspan="4"><textarea id="refundReason" rows="10" cols="" style="width: 100%">${custRefund.refundReason }</textarea></td>
    </tr>
    <tr>
    	<td class="tit">附件：</td>
    	<td colspan="4"><div id="NTKO_AttachmentCtrl" style="height: 150px; margin: 0 auto;"></div></td>
    </tr>
    <tr>
    	<td class="tit">审批人：</td>
    	<td colspan="4">
    		<input id="approverDis" type="text" maxlength="20" value="${custRefund.approverName }" class="iptSo iptSoSleW" readonly="readonly" />
	      	<input id="approver" type="hidden" value="${custRefund.approver }"/>
	      	<input id="openDispacher" class="selBtn" type="button"/>
    	</td>
    </tr>
    
    
    <tr>
		<td colspan="5" class="tipList">&nbsp;</td>
	  </tr>
	  <tr>
		<td colspan="5" class="tipShow">
			<span class="crmSubTabsBk">
				<ul class="crmSubTabs">
					<li class="current" style="cursor: default;">
						<div class="lastNone">处理意见</div>
					</li>
					<li><span style="color:red">${custRefund.prodInfo.prodStatus == 'STATUS_FOUND' ? '提示：该产品状态为“已成立”。' : ''}</span></li>
				</ul>
			</span>
		<!--处理意见-->
			<div class="yjSubBk">
					<textarea id="comment" class="area01 spArea"></textarea>
			</div>									
		<!--处理意见 end-->
		</td>
	  </tr>
	  
	  <tr>
		<td colspan="5">
	  <div >
        <c:forEach var="disposalProcDTO" items="${disposalProcDTOs}" >
		<!-- 工单处理 -->
		<c:choose>
			<c:when test="${disposalProcDTO.type == 0}">
				<div class="processBk">
				  <div class="clgcTit">
					<ul class="clearfix">
					  <li><b>处理人：</b>${disposalProcDTO.curActor }</li>
					  <li><b>任务状态：</b>${disposalProcDTO.object.taskStateStr}</li>
					  <li><b>任务耗时：</b>${disposalProcDTO.object.dealTime}</li>
					  <li>&nbsp;</li>
					  <li><b>到达时间：</b><fmt:formatDate value="${disposalProcDTO.object.createTime }" pattern="yyyy-MM-dd HH:mm"/></li>
					  <li><b>响应时间：</b><fmt:formatDate value="${disposalProcDTO.object.startTime }" pattern="yyyy-MM-dd HH:mm"/></li>
					  <li><b>结束时间：</b><fmt:formatDate value="${disposalProcDTO.object.endTime }" pattern="yyyy-MM-dd HH:mm"/></li>
					  <li>&nbsp;</li>
					</ul>
				  </div>
				  <c:if test="${not empty disposalProcDTO.object.taskComments }">
					<c:forEach items="${disposalProcDTO.object.taskComments }" var="comm">
					<c:if test="${comm.dataType!=null && comm.dataType==0 }">
						<c:if test="${comm.flag == 1 }">
							<!-- 已提交，显示出来 -->
							<div class="taskCon nonebb">
								<div class="taskTit ico_lab">
								<span><fmt:formatDate value="${comm.addtime }" pattern="yyyy-MM-dd HH:mm"/></span>
								<span>[${comm.task.nodeName }]</span>
								<span class="noneb">[${comm.taskClass }]</span>
								</div>
								<div class="handleCon">${comm.comment }</div>
							 </div>
						</c:if>
						<c:if test="${comm.flag == 0 }">
							<!-- 暂未提交，　设置隐藏域 -->
							<input type="hidden" id="commId" value="${comm.id}" />
							<input type="hidden" id="comment_"value="${comm.comment}" />
						</c:if>
					</c:if>
					</c:forEach>
				  </c:if>
				</div><!-- end of processBk -->
			</c:when>
		</c:choose>
	</c:forEach>
                 </div>
	</td>
</tr>
  </table>
  <div class="btTip"></div>
</div>
<!--新增划款 end-->
</form>

<div class="oppBtnBg">
	<input type="hidden" id="transitionName" /> 
	<input type="hidden" id="taskInstanceID" name="tiid" value="${ tiid }" /> 
	<span id="commitBut">  
		<c:forEach var="transition" items="${transitions}">
			<input class="allBtn" type="button" value="${transition}" />
		</c:forEach>
	</span>
</div>
</body>
</html>