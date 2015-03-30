<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/operationsManage/paymentRegister/paymentApproveLeader.js"></script>
<title>划款审批</title>
</head>
<body class="tabBd">
<input type="hidden" id="custPaymentId" value="${custPayment.id }" />

<div class="tabScroll" style="overflow-x: hidden">
<div class="addCon openWinW03" style="margin-left: auto;margin-right: auto;">
  <table border="0" width="100%" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
   
      <tr>
        <td class="tit"><div>投资顾问</div></td>
        <td class="field">${custPayment.cusManageName }</td>
        <c:if test="${!custPayment.standardFlag }">
        	 <td class="tit"><div>划款时间</div></td>
       	 </c:if>
       	 <c:if test="${custPayment.standardFlag }">
        	 <td class="tit"><div>预计划款时间</div></td>
       	 </c:if>
        <td class="field"><fmt:formatDate value="${custPayment.transferDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><div>客户名称</div></td>
        <td class="field">${custPayment.custName }</td>
        <td class="tit"><div>客户性质</div></td>
        <td class="field">
       	 <c:if test="${custPayment.custProperty == '0'}">个人</c:if>
       	 <c:if test="${custPayment.custProperty == '1'}">机构</c:if>
        </td>
      </tr>
      <tr>
        <td class="tit"><div>划款类型</div></td>
        <td class="field">${custPayment.payTypeName }</td>
        <td class="tit"><div>有无定金</div></td>
        <td class="field">
        	<c:if test="${!custPayment.payDepositFlag }">无</c:if>
       		<c:if test="${custPayment.payDepositFlag }">有</c:if>
       	</td>
      </tr>
      <tr>
        <td class="tit"><div>产品项目</div></td>
        <td class="field" colspan="3">${custPayment.proName }</td>
      </tr>
      <tr>
        <td class="tit"><div>剩余额度(万)</div></td>
        <td class="field">${custPayment.syed }</td>
        <td class="tit"><div>已划款总额度(万)</div></td>
        <td class="field">${custPayment.yhked }</td>
      </tr>
      <tr>
        <td class="tit"><div>预约金额(万)</div></td>
        <td class="field">${custPayment.appointmentAmount }</td>
        <td class="tit"><div>预约金额大写</div></td>
        <td class="field">${custPayment.appointmentAmountCapital }</td>
      </tr>
      <tr>
        <td class="tit"><div>划款金额(万)</div></td>
        <td class="field">${custPayment.transferAmount }</td>
        <td class="tit"><div>划款金额大写</div></td>
        <td class="field">${custPayment.transferAmountCapital }</td>
      </tr>
      <tr>
        <td class="tit"><div>备注</div></td>
        <td class="field" colspan="3">${custPayment.remark }</td>
      </tr>
      
      <tr>
      <td class="tit">到款凭条：</td>
      <td >
      	<c:if test="${!empty custPayment.paymentReceipt}">
      			<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${custPayment.paymentReceipt.filePath }" target="_blank">${custPayment.paymentReceipt.displayName }${custPayment.paymentReceipt.fileExt }</a>
      	</c:if>
      </td>
       <td class="tit"><div>身份证号:</div></td>
        <td class="field">${custPayment.identityNum }</td>
    </tr>
    
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
  </div>
</div>

<!--新增划款 end-->

		<div class="oppBtnBg tabBottom">
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