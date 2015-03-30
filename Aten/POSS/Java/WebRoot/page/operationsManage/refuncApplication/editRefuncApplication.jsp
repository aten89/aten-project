<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<script type="text/javascript" src="page/operationsManage/refuncApplication/editRefuncApplication.js"></script>
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
				<div isselected="true">true**是</div>
				<div>false**否</div>
			</div>
    	</td>
    	<td class="tit"><span class="cRed">*</span>退款金额（万）：</td>
    	<td><input id="refundAmount" type="text" maxlength="40" disabled="disabled" class="ipt05" value="${custRefund.refundAmount }"/></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>退款原因：</td>
    	<td colspan="4"><textarea id="refundReason" rows="10" cols="" style="width:100%;height:80px">${custRefund.refundReason }</textarea></td>
    </tr>
    <tr style="height: 180px;">
    	<td class="tit">附件：</td>
    	<td colspan="4"><div id="NTKO_AttachmentCtrl" style="height: 150px; margin: 0 auto;"></div></td>
    </tr>
    <tr style="display:none">
    	<td class="tit">审批人：</td>
    	<td colspan="4">
    		<input id="approverDis" type="text" maxlength="20" class="iptSo iptSoSleW" readonly="readonly" />
	      	<input id="approver" type="hidden" />
	      	<input id="openDispacher" class="selBtn" type="button"/>
    	</td>
    </tr>
  </table>
  <div class="btTip"></div>
</div>
<!--新增划款 end-->
<div class="addTool2">
  <input type="button" id="commit"  class="allBtn" value="提交审批"/>&nbsp;&nbsp;
 <!-- <input type="button" id="save"  class="allBtn" value="保存草稿"/>&nbsp;&nbsp;-->
  <input type="button" id="close" class="allBtn" value="取消"/>
</div>
</body>