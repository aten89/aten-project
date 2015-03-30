<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/ProdInfoManage/prodPlan/editProdPlan.js"></script>

<title>编辑发行方案</title>
</head>
<body class="tabBd" scroll="no">
	<input type="hidden" id="id" value="${prodIssuePlan.id }"/>
	<input type="hidden" id="prodId" value="${prodIssuePlan.prodInfo.id }"/>
	<div id="scrollDIV" class="tabScroll" style="overflow-x: hidden">
		<div class="addCon" align="center">
			<table border="0" width="98%" cellpadding="0" cellspacing="1">
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag1','planInfo1');"><b id="planShowFlag1">-</b>基本信息</th>
			    </tr>
			    <tbody id="planInfo1">
					<tr>
						<td class="tit">项目名称：</td>
						<td>${prodIssuePlan.prodInfo.prodName }</td>
						<td class="tit">是否允许进行标准预约：</td>
						<td>
							<input name="stdOrderFlag" type="radio" value="1" ${prodIssuePlan.stdOrderFlag == '1' ? 'checked':''}/> 是&nbsp;
	    					<input name="stdOrderFlag" type="radio" value="0" ${prodIssuePlan.stdOrderFlag == '1' ? '':'checked'}/> 否&nbsp;
						</td>
						<td class="tit">产品经理：</td>
						<td>${prodIssuePlan.prodInfo.prodManager }</td>
					</tr>
					<tr>
						<td class="tit">产品经理电话：</td>
							<td>${prodIssuePlan.prodInfo.prodManagerTel }</td>
						<td class="tit">销售模式：</td>
						<td>
							<input type="text" class="ipt01" id="salesModel" value="${prodIssuePlan.salesModel }"/>
						</td>
						<td class="tit">销售级别：</td>
						<td>${prodIssuePlan.prodInfo.sellRankName}</td>
					</tr>
					<tr>
						<td class="tit">发行额度(万)：</td>
							<td>${prodIssuePlan.prodInfo.sellAmount}</td>
						<td class="tit">最低成立标准(万)：</td>
						<td>
							<input type="text" class="ipt01" id="lowestStd" value="${prodIssuePlan.lowestStd }"/>
						</td>
						<td class="tit">剩余金额预警上限(%)：</td>
						<td>
							<input type="text" class="ipt01" id="remainAmountWarn" value="${prodIssuePlan.remainAmountWarn }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">剩余金额叫停上限(%)：</td>
						<td>
							<input type="text" class="ipt01" id="remainAmountStop" value="${prodIssuePlan.remainAmountStop }"/>
						</td>
						<td class="tit">产品核算系数：</td>
						<td>${prodIssuePlan.prodInfo.accountCoefficient}</td>
						<td class="tit">小额上限：</td>
						<td>
							<input type="text" class="ipt01" id="smallCaps" value="${prodIssuePlan.smallCaps }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">控制小额数目上限：</td>
						<td>
							<input type="text" class="ipt01" id="smallNumCaps" value="${prodIssuePlan.smallNumCaps }"/>
						</td>
						<td colspan="4">
						</td>
					</tr>
					<tr>
						<td class="tit">说明：</td>
						<td colspan="5">
							<textarea id="description" style="height:100px" class="area01">${prodIssuePlan.description }</textarea>
						</td>
					</tr>
					<tr>
						<td class="tit">职业经理人提成比例：</td>
						<td colspan="5">
							<input type="text" class="ipt01" id="managerTakeRatio" value="${prodIssuePlan.managerTakeRatio }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">合伙人提成比例：</td>
						<td colspan="5">
							<input type="text" class="ipt01" id="partnerTakeRatio" value="${prodIssuePlan.partnerTakeRatio }"/>
						</td>
					</tr>
				</tbody>
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag2','planInfo2');"><b id="planShowFlag2">-</b>小额分配标准</th>
			    </tr>
			    <tbody id="planInfo2">
				    <tr>
						<td class="tit">大额起点(万)：</td>
						<td>
							<input type="text" class="ipt01" id="bigAmountPoint" value="${prodIssuePlan.bigAmountPoint }"/>
						</td>
						<td class="tit">最低打款额度(万)：</td>
						<td>
							<input type="text" class="ipt01" id="lowestPayAmount" value="${prodIssuePlan.lowestPayAmount }"/>
						</td>
						<td class="tit">大小额配比：</td>
						<td>
							<input type="text" class="ipt01" id="sizeVolumeRatio" value="${prodIssuePlan.sizeVolumeRatio }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">有无大额换小额：</td>
						<td>
							<input name="bigToSmallFlag" type="radio" value="1" ${prodIssuePlan.bigToSmallFlag == '1' ? 'checked':''}/> 有&nbsp;
	    					<input name="bigToSmallFlag" type="radio" value="0" ${prodIssuePlan.bigToSmallFlag == '1' ? '':'checked'}/> 无&nbsp;
						</td>
						<td class="tit">有无小额指定：</td>
						<td>
							<input name="smallAppointFlag" type="radio" value="1" ${prodIssuePlan.smallAppointFlag == '1' ? 'checked':''}/> 有&nbsp;
	    					<input name="smallAppointFlag" type="radio" value="0" ${prodIssuePlan.smallAppointFlag == '1' ? '':'checked'}/> 无&nbsp;
						</td>
						<td colspan="2">
						</td>
					</tr>
				</tbody>
				
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag3','planInfo3');"><b id="planShowFlag3">-</b>时间信息</th>
			    </tr>
			    <tbody id="planInfo3">
				    <tr>
						<td class="tit">预热开始时间：</td>
						<td>
							<input id="preheatStartTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.preheatStartTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td class="tit">预热结束时间：</td>
						<td>
							<input id="preheatEndTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.preheatEndTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td colspan="2">
						</td>
					</tr>
					<tr>
						<td class="tit">预计发行时间：</td>
						<td>
							<fmt:formatDate value="${prodIssuePlan.prodInfo.expectSellDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td class="tit">预计结束时间：</td>
						<td>
							<input id="estimateEndTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.estimateEndTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td colspan="2">
						</td>
					</tr>
					<tr>
						<td class="tit">合同寄送时间：</td>
						<td>
							<input id="contractSendTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.contractSendTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td class="tit">募集账号和有账号<br/>资料发送时间：</td>
						<td>
							<input id="dataSendTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.dataSendTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td class="tit">产品视频培训时间：</td>
						<td>
							<input id="prodTrainTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.prodTrainTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
					</tr>
				</tbody>
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag4','planInfo4');"><b id="planShowFlag4">-</b>非标预约控制项</th>
			    </tr>
			    <tbody id="planInfo4">
				    <tr>
						<td class="tit">非标开始时间：</td>
						<td>
							<input id="nonStdStartTime" readonly type="text" class="invokeBoth" style="width:70%" value="<fmt:formatDate value="${prodIssuePlan.nonStdStartTime}" pattern="yyyy-MM-dd"/>"/>
						</td>
						<td class="tit">预计非标额度：</td>
						<td>
							<input type="text" class="ipt01" id="nonStdAmount" value="${prodIssuePlan.nonStdAmount }"/>
						</td>
						<td class="tit">非标额度限制比例：</td>
						<td>
							<input type="text" class="ipt01" id="nonStdLimitRatio" value="${prodIssuePlan.nonStdLimitRatio }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">排序时间限制(分钟)：</td>
						<td>
							<input type="text" class="ipt01" id="orderTimeLimit" value="${prodIssuePlan.orderTimeLimit }"/>
						</td>
						<td class="tit">小额数量限制：</td>
						<td>
							<input type="text" class="ipt01" id="smallNumLimit" value="${prodIssuePlan.smallNumLimit }"/>
						</td>
						<td class="tit">非标确认时间(分钟)：</td>
						<td>
							<input type="text" class="ipt01" id="nonStdCfmMin" value="${prodIssuePlan.nonStdCfmMin }"/>
						</td>
					</tr>
					<tr>
						<td class="tit">排序规则：</td>
						<td colspan="5">
							<input type="hidden" id="orderRule" value="${prodIssuePlan.orderRule }"/>
							<input name="orderRuleCB" type="checkbox" value="预计打款时间" /> 预计打款时间&nbsp;
							<input name="orderRuleCB" type="checkbox" value="客户级别优先" /> 客户级别优先&nbsp;
							<input name="orderRuleCB" type="checkbox" value="小额中的大额优先（相同额度以预约号优先）" /> 小额中的大额优先（相同额度以预约号优先）&nbsp;
							<input name="orderRuleCB" type="checkbox" value="大额以预约号优先" /> 大额以预约号优先&nbsp;
							<br/>
							<input name="orderRuleCB" type="checkbox" value="会员专区普通客户预约优先" /> 会员专区普通客户预约优先&nbsp;
							<input name="orderRuleCB" type="checkbox" value="会员专区离职客户预约优先" /> 会员专区离职客户预约优先&nbsp;
							<input name="orderRuleCB" type="checkbox" value="小额中的小额优先" /> 小额中的小额优先&nbsp;
							<input name="orderRuleCB" type="checkbox" value="大额中的小额优先" /> 大额中的小额优先&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="blank" style="height:60px;"></div>
	</div>
	
	<div class="oppBtnBg tabBottom">
		<input class="allBtn" id="save" type="button" value="保存"/>
		<input class="allBtn" id="cancel" type="button" value="取消"/>
	</div>
</body>
</html>

