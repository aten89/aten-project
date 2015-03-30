<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<link rel="stylesheet" type="text/css" href="commui/autocoomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="commui/autocoomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="commui/autocoomplete/jquery.bgiframe.min.js"></script>

<script type="text/javascript" src="commui/attachmentSingle/ajaxfileupload.js"></script>
<script type="text/javascript" src="commui/attachmentSingle/com.eapp.poss.attachement.single.js"></script>

<script type="text/javascript" src="page/operationsManage/paymentRegister/againModify.js"></script>
<title></title>
</head>
<body class="tabBd">

<input type="hidden" id="custPaymentId" value="${custPayment.id }" />
<input type="hidden" id="saleManId" value="${custPayment.saleManId }" />
<input type="hidden" id="cusManageName" value="${custPayment.cusManageName }" />
<input type="hidden" id="customerId" value="${custPayment.custId }" />
<input type="hidden" id="prodId" value="${custPayment.prodId }" />
<input type="hidden" id="custProperty" value="${custPayment.custProperty }" />
<input type="hidden" id="payType" value="${custPayment.payType }" />
<input type="hidden" id="payDepositFlag" value="${custPayment.payDepositFlag }" />
<input type="hidden" id="standardFlag" value="${custPayment.standardFlag }" />

<div class="tabScroll" style="overflow-x: hidden">
<div class="addCon openWinW03" style="margin-left: auto;margin-right: auto;">
  <table border="0" width="100%" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>投资顾问：</td>
		<td>
			<input id="custManageId" type="hidden" />
			<input id="custManage" type="text" maxlength="40" class="ipt05" disabled="disabled" />
		</td>
		 <c:if test="${!custPayment.standardFlag }">
        	<td class="tit"><span class="cRed">*</span>划款时间： </td>
       	 </c:if>
       	 <c:if test="${custPayment.standardFlag }">
        	<td class="tit"><span class="cRed">*</span>预计划款时间： </td>
       	 </c:if>
		<td>
			<input style="width:110px" readonly="readonly" name="transferDate" id="transferDate" type="text" class="invokeBoth" 
			  	value="<fmt:formatDate value="${custPayment.transferDate }" pattern="yyyy-MM-dd"/>"   />
		</td>
	</tr>
    <tr>
      	<td class="tit"><span class="cRed">*</span>客户名称：</td>
		<c:if test="${custPayment.payType == '1'}">
			<td>
			<div style="width: 139px;">
				<div id="customerInfoSel" name="customerInfoSel"></div>
			</div>
		</td>
		</c:if>
		<c:if test="${custPayment.payType == '0'}">
			<td >
							<input type="text" id="custName" value="${custPayment.custName}" class="ipt05 iptSuggest"/>
							<input type="hidden" id="custId" value="${custPayment.custId}"  />
							<input type="hidden" id="oriSelected" value="${custPayment.custName}" />
						</td>
		</c:if>
		<td class="tit"><span class="cRed">*</span>客户性质：</td>
		<td>
			<div id="customerNatureSel" name="customerNatureSel"></div>
		</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>产品项目：</td>
		<td colspan="3">
			<div id="prodInfoSel" name="prodInfoSel"></div>
		</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>有无定金：</td>
		<td colspan="3">
			<div id="earnestSel">
						        <div isselected="true">**请选择...</div>
						        <div>1**有</div>
						        <div>0**无</div>
				            </div>
		</td>
    </tr>
    <tr>
    	<td class="tit">剩余额度(万)：</td>
		<td><input id="remainingAmount" type="text" maxlength="40" class="ipt05" disabled="disabled" /></td>
		<td class="tit">已划款总额度(万)：</td>
		<td><input id="transferAmount" type="text" maxlength="40" class="ipt05" disabled="disabled" /></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>预约金额(万)：</td>
		<td><input id="appointmentAmount" type="text" maxlength="40" class="ipt05" value="${custPayment.appointmentAmount }" /></td>
		<td class="tit"><span class="cRed">*</span>预约金额大写：</td>
		<td>
			<div id="appointmentAmountCapital">${custPayment.appointmentAmountCapital }</div>
		</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>划款金额(万)：</td>
		<td><input id="payAmount" type="text" class="ipt05" value="${custPayment.transferAmount }" disabled="disabled" /></td>
		<td class="tit"><span class="cRed">*</span>划款金额大写：</td>
		<td><div id="payAmountCapital">${custPayment.transferAmountCapital }</div></td>
    </tr>
    <tr>
      <td class="tit">备注：</td>
      <td colspan="3"><textarea id="remark" name="remark" class="area01">${custPayment.remark }</textarea></td>
    </tr>
    
   <tr>
      <td class="tit"><span class="cRed">*</span>到款凭条：</td>
      <td>
      	<div id="janeEditionAttchOldShow" style="display:none;">
      		<c:if test="${!empty custPayment.paymentReceipt}">
      			<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${custPayment.paymentReceipt.filePath }" target="_blank">${custPayment.paymentReceipt.displayName }${custPayment.paymentReceipt.fileExt }</a>
      		</c:if>
      	</div>
      	<div id="janeEditionAttchDIV"></div>
      </td>
      
      <td class="tit"><span class="cRed">*</span>身份证号码：</td>
		<td><input id="identityNum" type="text" maxlength="36" class="ipt05" value="${custPayment.identityNum}" /></td>
    </tr>
    
   <!--  <c:if test="${!custPayment.standardFlag }">
    	<tr>
	      <td class="tit"><span class="cRed">*</span>审批人：</td>
	      <td colspan="3">
	      	<input id="dispacherDis" type="text" maxlength="20" class="iptSo iptSoSleW" value="${custPayment.approverName }" readonly="readonly" />
	      	<input id="dispacher" type="hidden" value="${custPayment.approver }" />
	      	<input id="openDispacher" class="selBtn" type="button"/>
      	</td>
   	   </tr>
    </c:if>
    --> 
    <tr>
		<td colspan="4" class="tipList">&nbsp;</td>
	  </tr>
	  <tr>
		<td colspan="4" class="tipShow">
			<span class="crmSubTabsBk">
				<ul class="crmSubTabs">
					<li class="current" style="cursor: default;">
						<div class="lastNone">处理意见</div>
					</li>
					<li><span style="color:red">${custPayment.prodStatus == 'STATUS_FOUND' ? '提示：该产品状态为“已成立”。' : ''}</span></li>
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
		<td colspan="4">
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
										
										  <li><b>到达时间：</b><fmt:formatDate value="${disposalProcDTO.object.createTime }" pattern="yyyy-MM-dd HH:mm"/></li>
										  <li><b>响应时间：</b><fmt:formatDate value="${disposalProcDTO.object.startTime }" pattern="yyyy-MM-dd HH:mm"/></li>
										  <li><b>结束时间：</b><fmt:formatDate value="${disposalProcDTO.object.endTime }" pattern="yyyy-MM-dd HH:mm"/></li>
										
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
</div>
<!--重新修改 end-->

	<div class="oppBtnBg tabBottom" id="commitBut">
		<input type="hidden" id="transitionName" /> 
		<input type="hidden" id="taskInstanceId" name="tiid" value="${ tiid}"/>
		<c:forEach var="transition" items="${transitions}">
			<input class="allBtn" type="button" value="${transition}" />
		</c:forEach>
	</div>
	
</body>
</html>