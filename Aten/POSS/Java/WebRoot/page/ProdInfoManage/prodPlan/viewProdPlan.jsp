<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/ProdInfoManage/prodPlan/viewProdPlan.js"></script>

<title>查看发行方案</title>
</head>
<body class="tabBd" scroll="no">
	<input id="prodIssuePlanHidModuleRights" type="hidden" value="<eapp:right key='prod_plan'/>" />
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
						<td>${prodIssuePlan.stdOrderFlag == '1' ? '是':'否'}</td>
						<td class="tit">产品经理：</td>
						<td>${prodIssuePlan.prodInfo.prodManager }</td>
					</tr>
					<tr>
						<td class="tit">产品经理电话：</td>
							<td>${prodIssuePlan.prodInfo.prodManagerTel }</td>
						<td class="tit">销售模式：</td>
						<td>${prodIssuePlan.salesModel }</td>
						<td class="tit">销售级别：</td>
						<td>${prodIssuePlan.prodInfo.sellRankName}</td>
					</tr>
					<tr>
						<td class="tit">发行额度(万)：</td>
							<td>${prodIssuePlan.prodInfo.sellAmount}</td>
						<td class="tit">最低成立标准(万)：</td>
						<td>${prodIssuePlan.lowestStd }</td>
						<td class="tit">剩余金额预警上限(%)：</td>
						<td>${prodIssuePlan.remainAmountWarn }</td>
					</tr>
					<tr>
						<td class="tit">剩余金额叫停上限(%)：</td>
						<td>${prodIssuePlan.remainAmountStop }</td>
						<td class="tit">产品核算系数：</td>
						<td>${prodIssuePlan.prodInfo.accountCoefficient}</td>
						<td class="tit">小额上限：</td>
						<td>${prodIssuePlan.smallCaps }</td>
					</tr>
					<tr>
						<td class="tit">控制小额数目上限：</td>
						<td>${prodIssuePlan.smallNumCaps }</td>
						<td colspan="4">
						</td>
					</tr>
					<tr>
						<td class="tit">说明：</td>
						<td colspan="5">
							<textarea id="description" style="height:100px" readonly class="area01">${prodIssuePlan.description }</textarea>
						</td>
					</tr>
					<tr id="managerTakeRatioTR">
						<td class="tit">职业经理人提成比例：</td>
						<td colspan="5" style="color:red">${prodIssuePlan.managerTakeRatio }</td>
					</tr>
					<tr id="partnerTakeRatioTR">
						<td class="tit">合伙人提成比例：</td>
						<td colspan="5" style="color:red">${prodIssuePlan.partnerTakeRatio }</td>
					</tr>
				</tbody>
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag2','planInfo2');"><b id="planShowFlag2">-</b>小额分配标准</th>
			    </tr>
			    <tbody id="planInfo2">
				    <tr>
						<td class="tit">大额起点(万)：</td>
						<td>${prodIssuePlan.bigAmountPoint }</td>
						<td class="tit">最低打款额度(万)：</td>
						<td>${prodIssuePlan.lowestPayAmount }</td>
						<td class="tit">大小额配比：</td>
						<td>${prodIssuePlan.sizeVolumeRatio }</td>
					</tr>
					<tr>
						<td class="tit">有无大额换小额：</td>
						<td>${prodIssuePlan.bigToSmallFlag == '1' ? '有':'无'}</td>
						<td class="tit">有无小额指定：</td>
						<td>${prodIssuePlan.smallAppointFlag == '1' ? '有':'无'}</td>
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
						<td><fmt:formatDate value="${prodIssuePlan.preheatStartTime}" pattern="yyyy-MM-dd"/></td>
						<td class="tit">预热结束时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.preheatEndTime}" pattern="yyyy-MM-dd"/></td>
						<td colspan="2">
						</td>
					</tr>
					<tr>
						<td class="tit">预计发行时间：</td>
						<td>
							<fmt:formatDate value="${prodIssuePlan.prodInfo.expectSellDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td class="tit">预计结束时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.estimateEndTime}" pattern="yyyy-MM-dd"/></td>
						<td colspan="2">
						</td>
					</tr>
					<tr>
						<td class="tit">合同寄送时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.contractSendTime}" pattern="yyyy-MM-dd"/></td>
						<td class="tit">募集账号和有账号<br/>资料发送时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.dataSendTime}" pattern="yyyy-MM-dd"/></td>
						<td class="tit">产品视频培训时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.prodTrainTime}" pattern="yyyy-MM-dd"/></td>
					</tr>
				</tbody>
				
				<tr>
			    	<th colspan="6" class="tipBg" onclick="hideTbody('planShowFlag4','planInfo4');"><b id="planShowFlag4">-</b>非标预约控制项</th>
			    </tr>
			    <tbody id="planInfo4">
				    <tr>
						<td class="tit">非标开始时间：</td>
						<td><fmt:formatDate value="${prodIssuePlan.nonStdStartTime}" pattern="yyyy-MM-dd"/></td>
						<td class="tit">预计非标额度：</td>
						<td>${prodIssuePlan.nonStdAmount }</td>
						<td class="tit">非标额度限制比例：</td>
						<td>${prodIssuePlan.nonStdLimitRatio }</td>
					</tr>
					<tr>
						<td class="tit">排序时间限制(分钟)：</td>
						<td>${prodIssuePlan.orderTimeLimit }</td>
						<td class="tit">小额数量限制：</td>
						<td>${prodIssuePlan.smallNumLimit }</td>
						<td class="tit">非标确认时间(分钟)：</td>
						<td>${prodIssuePlan.nonStdCfmMin }</td>
					</tr>
					<tr>
						<td class="tit">排序规则：</td>
						<td colspan="5">
							<input type="hidden" id="orderRule" value="${prodIssuePlan.orderRule }"/>
							<input name="orderRuleCB" type="checkbox" disabled value="预计打款时间" /> 预计打款时间&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="客户级别优先" /> 客户级别优先&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="小额中的大额优先（相同额度以预约号优先）" /> 小额中的大额优先（相同额度以预约号优先）&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="大额以预约号优先" /> 大额以预约号优先&nbsp;
							<br/>
							<input name="orderRuleCB" type="checkbox" disabled value="会员专区普通客户预约优先" /> 会员专区普通客户预约优先&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="会员专区离职客户预约优先" /> 会员专区离职客户预约优先&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="小额中的小额优先" /> 小额中的小额优先&nbsp;
							<input name="orderRuleCB" type="checkbox" disabled value="大额中的小额优先" /> 大额中的小额优先&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="blank" style="height:30px;"></div>
	</div>
	
	<div class="oppBtnBg tabBottom">
		<input class="allBtn" id="cancel" type="button" value="关闭"/>
	</div>
</body>
</html>

