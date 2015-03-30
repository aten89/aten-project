<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="commui/attachmentSingle/ajaxfileupload.js"></script>
<script type="text/javascript" src="commui/attachmentSingle/com.eapp.poss.attachement.single.js"></script>
<script type="text/javascript" src="page/ProdInfoManage/prodInfo/editProdInfo.js"></script>
</head>
<body class="tabBd" scroll="no">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='PROD_MAN'/>" />
<input type="hidden" id="prodInfoId"  value="${prodInfo.id}" />
<input type="hidden" id="supplierId"  value="${prodInfo.supplier.id}" />
<input type="hidden" id="prodType"  value="${prodInfo.prodType}" />
<input type="hidden" id="secondaryClassify"  value="${prodInfo.prodSecondaryClassify}" />
<input type="hidden" id="prodStatus"  value="${prodInfo.prodStatus}" />
<input type="hidden" id="sellRank"  value="${prodInfo.sellRank}" />
<input type="hidden" id="needGradeFlag"  value="${prodInfo.needGradeFlag}" />
<input type="hidden" id="redeemFlag"  value="${prodInfo.redeemFlag}" />
<div class="tabScroll">
  <div class="addCon">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>产品代码：</td>
		<td><input id="prodCode" type="text" maxlength="36" class="ipt05" style="width: 200px;" value="${prodInfo.prodCode}"/> </td>
		<td class="tit"><span class="cRed">*</span>产品名称： </td>
		<td><input id="prodName" type="text" maxlength="128" class="ipt05" style="width: 200px;" value="${prodInfo.prodName}"/></td>
	</tr>
    <tr>
      <td class="tit"><span class="cRed">*</span>产品类型：</td>
	  <td><div id="prodTypeSel" name="prodTypeSel"></div></td>
      <td class="tit"><span class="cRed">*</span>产品二级分类：</td>
      <td><div id="secondClassifySel" name="secondClassifySel"></div></td>
    </tr>
    <tr>
      <td class="tit">融资公司名称：</td>
      <td><input id="financeCompanyName" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.financeCompanyName}"/></td>
      <td class="tit">产品状态：</td>
      <td ><div id="prodStatusSel" name="prodStatusSel"></div></td>
    </tr>
    <tr>
      <td class="tit">供应商：</td>
      <td><div id="supplierSel" name="supplierSel"></div></td>
      <td class="tit"><span class="cRed">*</span>项目总额度(万)：</td>
      <td><input id="pjtTotalAmount" type="text" maxlength="10" class="ipt05" style="width: 200px;" value="${prodInfo.pjtTotalAmount}"/></td>
    </tr>
    <tr>
      <td class="tit">项目总额度备注：</td>
      <td><input id="pjtAmountRemark" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.pjtAmountRemark}"/></td>
      <td class="tit"><span class="cRed">*</span>发行额度(万)：</td>
      <td><input id="sellAmount" type="text" maxlength="10" class="ipt05" style="width: 200px;" value="${prodInfo.sellAmount}"/></td>
    </tr>
    <tr>
      <td class="tit">期限(月)：</td>
      <td><input id="sellTimeLimit" type="text" maxlength="6" class="ipt05" style="width: 200px;" value="${prodInfo.sellTimeLimit}"/></td>
      <td class="tit">期限备注：</td>
      <td><input id="timeLimitRemark" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.timeLimitRemark}"/></td>
    </tr>
    <tr>
      <td class="tit">销售级别：</td>
      <td ><div id="sellRankSel" name="sellRankSel"></div></td>
      <td class="tit">运营预警：</td>
      <td><input id="operationWarning" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.operationWarning}"/></td>
    </tr>
    <tr>
      <td class="tit">产品核算系数：</td>
      <td><input id="accountCoefficient" type="text" maxlength="8" class="ipt05" style="width: 200px;" value="${prodInfo.accountCoefficient}"/></td>
      <td class="tit"><span class="cRed">*</span>是否赎回：</td>
      <td>
      	<input name="redeemFlag"  type="radio" value="true"/>是&nbsp;
      	<input name="redeemFlag"  type="radio" value="false" checked/>否&nbsp;
      </td>
    </tr>
    <tr>
      <td colspan="4">
      	<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      		<tr>
      			<td colspan="4" class="tipList">&nbsp;</td>
      		</tr>
      		<tr>
      			<td colspan="4"  class="tipShow"><span class="crmSubTabsBk">
      				<ul class="crmSubTabs">
      					<li class="current" style="cursor:default;">
      						<div class="lastNone">预期年化收益率</div>
      					</li>
      				</ul></span>
      				<div class="boxShadow" >
      					<div class="shadow01">
      						<div class="shadow02" >
      							<div class="shadow03" >
      								<div class="shadow04" >
      									<div class="shadowCon">
      										<div id="tab00">
      											<div class="ywjgList">
      												<table width="100%" border="0" cellspacing="1" cellpadding="0">
										              	<thead>
											                <tr>
											                	<th width="20%">下限(万)</th>
											                  	<th width="20%">上限(万)</th>
												      			<th width="20%">年化收益率(%)</th>
												      			<th width="20%">受益类别</th>
											                  	<th width="10%">操作&nbsp;&nbsp;<img id="addExpectYearYield" src="themes/comm/images/customBtn.gif" title="添加" /></th>
											                </tr>
										                </thead>
										                <tbody id="expectYearYield">
										                	<c:if test="${prodInfo.expectYearYields != null }">
										                		<c:forEach items="${prodInfo.expectYearYields}" var="expectYearYield">
										                			<tr id="${expectYearYield.id }">
										           						<td>${expectYearYield.lowerLimit }</td>
										           						<td>${expectYearYield.upperLimit }</td>
										           						<td>${expectYearYield.yearYield }</td>
										           						<td>${expectYearYield.benefitType }</td>
										           						<td>
										           							<input type="image"  src="themes/comm/images/crmEdit_ico.gif" title="修改" onclick="editExpectYearYield(this)"/>
										           							<input type="image"  src="themes/comm/images/crmDel_ico.gif" title="删除" onclick="delExpectYearYield(this)"/>
										           						</td>
										                			</tr>
										                		</c:forEach>
										                	</c:if>
										                </tbody>
										             </table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
      </td>
    </tr>
    <tr>
      <td class="tit">发行日期：</td>
      <td ><input readonly id="sellDate" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.sellDate}" pattern="yyyy-MM-dd"/>" /></td>
      <td class="tit">停止划款时间：</td>
      <td><input readonly id="transferDeadline" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.transferDeadline}" pattern="yyyy-MM-dd"/> "/></td>
    </tr>
    <tr>
      <td class="tit">募集结束日期：</td>
      <td ><input readonly id="raiseFundsEndTime" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.raiseFundsEndTime}" pattern="yyyy-MM-dd"/> "/></td>
      <td class="tit">产品成立日期：</td>
      <td><input readonly id="prodSetUpDate" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.prodSetUpDate}" pattern="yyyy-MM-dd"/>"/></td>
    </tr>
    <tr>
      <td class="tit">产品兑付日期：</td>
      <td ><input readonly id="prodCashDate" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.prodCashDate}" pattern="yyyy-MM-dd"/> "/></td>
      <td class="tit">实际兑付日期：</td>
      <td><input readonly id="actualCashDate" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.actualCashDate}" pattern="yyyy-MM-dd"/> "/></td>
    </tr>
    <tr>
      <td class="tit">付息方式：</td>
      <td colspan="3"><input id="payInterestMethod" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.payInterestMethod}"/></td>
    </tr>
    <tr>
      <td colspan="4">
      	<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      		<tr>
      			<td colspan="4" class="tipList">&nbsp;</td>
      		</tr>
      		<tr>
      			<td colspan="4"  class="tipShow"><span class="crmSubTabsBk">
      				<ul class="crmSubTabs">
      					<li class="current" style="cursor:default;">
      						<div class="lastNone">产品付息日期</div>
      					</li>
      				</ul></span>
      				<div class="boxShadow" >
      					<div class="shadow01">
      						<div class="shadow02" >
      							<div class="shadow03" >
      								<div class="shadow04" >
      									<div class="shadowCon">
      										<div id="tab00">
      											<div class="ywjgList">
      												<table width="100%" border="0" cellspacing="1" cellpadding="0">
										              	<thead>
											                <tr>
											                	<th width="20%">预计付息日</th>
												      			<th width="20%">实际付息日</th>
											                  	<th width="10%">操作&nbsp;&nbsp;<img id="addProdPayDates" src="themes/comm/images/customBtn.gif" title="添加" /></th>
											                </tr>
										                </thead>
										                <tbody id="prodPayDate">
										                	<c:if test="${prodInfo.prodPayDates != null }">
										                		<c:forEach items="${prodInfo.prodPayDates}" var="prodPayDate">
										                			<tr id="${prodPayDate.id }">
										           						<td><fmt:formatDate value="${prodPayDate.expectPayDate }" pattern="yyyy-MM-dd"/></td>
										           						<td><fmt:formatDate value="${prodPayDate.actualPayDate }" pattern="yyyy-MM-dd"/></td>
										           						<td>
										           							<input type="image"  src="themes/comm/images/crmEdit_ico.gif" title="修改" onclick="editProdPayDate(this)"/>
										           							<input type="image"  src="themes/comm/images/crmDel_ico.gif" title="删除" onclick="delProdPayDate(this)"/>
										           						</td>
										                			</tr>
										                		</c:forEach>
										                	</c:if>
										                </tbody>
										             </table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
      </td>
    </tr>
    <tr>
      <td class="tit">账户名：</td>
      <td ><input id="trustBank" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.trustBank}"/></td>
      <td class="tit">开户行：</td>
      <td><input id="raiseBank" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.raiseBank}"/></td>
    </tr>
    <tr>
      <td class="tit">募集账号：</td>
      <td colspan="3"><input id="raiseAccount" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.raiseAccount}"/></td>
    </tr>
    <tr>
      <td class="tit">简版附件：</td>
      <td colspan="3">
      	<div id="janeEditionAttchOldShow" style="display:none;">
      		<c:if test="${!empty prodInfo.janeEditionAttch}">
      			<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${prodInfo.janeEditionAttch.filePath }" target="_blank">${prodInfo.janeEditionAttch.displayName }${prodInfo.janeEditionAttch.fileExt }</a>
      		</c:if>
      	</div>
      	<div id="janeEditionAttchDIV"></div>
      </td>
    </tr>
    <tr>
      <td class="tit">非简版附件：</td>
      <td colspan="3">
      	<div id="otherEditionAttchOldShow" style="display:none;">
      		<c:if test="${!empty prodInfo.otherEditionAttch}">
      			<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${prodInfo.otherEditionAttch.filePath }" target="_blank">${prodInfo.otherEditionAttch.displayName }${prodInfo.otherEditionAttch.fileExt }</a>
      		</c:if>
      	</div>
      	<div id="otherEditionAttchDIV"></div>
      </td>
    </tr>
    <tr>
      <td class="tit">产品经理：</td>
      <td><input id="prodManager" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.prodManager}"/></td>
      <td class="tit">产品经理联系电话：</td>
      <td><input id="prodManagerTel" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.prodManagerTel}"/></td>
    </tr>
    <tr>
      <td class="tit">产品经理联系邮箱：</td>
      <td><input id="prodManagerEmail" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.prodManagerEmail}"/></td>
      <td class="tit">产品督导：</td>
      <td><input id="prodSupervisor" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.prodSupervisor}"/></td>
    </tr>
    <tr>
      <td class="tit">产品督导联系方式：</td>
      <td><input id="supervisorContactWay" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.supervisorContactWay}"/></td>
      <td class="tit">备注：</td>
      <td><input id="supervisorRemark" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.supervisorRemark}"/></td>
    </tr>
    <tr>
      <td class="tit">预计发行日期：</td>
      <td><input readonly id="expectSellDate" type="text" class="invokeBoth soTimeW" style="width: 200px;" value="<fmt:formatDate value="${prodInfo.expectSellDate}" pattern="yyyy-MM-dd"/> "/></td>
      <td class="tit">运营周期(天)：</td>
      <td><input id="operationPeriod" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.operationPeriod}"/></td>
    </tr>
    <tr>
      <td class="tit">预约划款合计(万)：</td>
      <td><input id="totalAppointmentAmount" type="text" maxlength="10" class="ipt05" style="width: 200px;" value="${prodInfo.totalAppointmentAmount}"/></td>
      <td class="tit">划款金额(万)：</td>
      <td><input id="transferAmount" maxlength="10" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.transferAmount}"/></td>
    </tr>
    <tr>
      <td class="tit">到账金额(万)：</td>
      <td><input id="toAccountAmount" maxlength="10" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.toAccountAmount}"/></td>
      <td class="tit">到账小额数：</td>
      <td><input id="toAccountSmallAmount" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.toAccountSmallAmount}"/></td>
    </tr>
    <tr>
      <td class="tit">是否需评级：</td>
      <td>
      	<input name="needGradeFlag"  type="radio" value="true"/>是&nbsp;
      	<input name="needGradeFlag"  type="radio" value="false" checked/>否&nbsp;
      </td>
      <td class="tit">可用额度(万)：</td>
      <td><input id="remainAmount" maxlength="10" type="text" class="ipt05" style="width: 200px;" value="${prodInfo.remainAmount}"/></td>
    </tr>
    <tr>
      <td class="tit">视频讲座：</td>
      <td colspan="3"><input id="videoLectures" type="text" class="ipt05" style="width: 820px;" value="${prodInfo.videoLectures}"/></td>
    </tr>
  </table>
  </div>
</div>
<div class="oppBtnBg tabBottom">
	<input id="save" type="button" value="保存" class="allBtn"/>
	<input id="cancel" type="button" value="取消" class="allBtn"/>
</div>
</body>
</html>