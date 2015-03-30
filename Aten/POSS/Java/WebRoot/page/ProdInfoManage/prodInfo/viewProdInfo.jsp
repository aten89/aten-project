<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<script type="text/javascript" src="page/ProdInfoManage/prodInfo/viewProdInfo.js"></script>
<input id="prodDetailHidModuleRights" type="hidden" value="<eapp:right key='prod_info'/>" />
<input id="prodFQAHidModuleRights" type="hidden" value="<eapp:right key='PROD_INFO_FAQ'/>" />
<input id="prodIssuePlanHidModuleRights" type="hidden" value="<eapp:right key='prod_plan'/>" />
<!--工具栏-->
<div class="addTool" id="divAddTool">
  <div class="t01 t_f01">
  	<input type="button" class="edit_btn" id="modify" />
  	<input type="button" class="addProdFaq_btn" id="addProdFaq" />
  	<input type="button" class="editIssuePlan_btn"  id="editIssuePlan" />
  	<input type="button" class="viewIssuePlan_btn" id="viewIssuePlan" />
  </div>
</div>
<div class="addCon">
  <input type="hidden" id="prodInfoId"  value="${prodInfo.id}" />
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>产品代码：</td>
		<td>${prodInfo.prodCode}</td>
		<td class="tit"><span class="cRed">*</span>产品名称： </td>
		<td>${prodInfo.prodName}</td>
	</tr>
    <tr>
      <td class="tit"><span class="cRed">*</span>产品类型：</td>
	  <td>${prodInfo.prodTypeName}</td>
      <td class="tit"><span class="cRed">*</span>产品二级分类：</td>
      <td>${prodInfo.secondClassifyName}</td>
    </tr>
    <tr>
      <td class="tit">融资公司名称：</td>
      <td>${prodInfo.financeCompanyName}</td>
      <td class="tit">产品状态：</td>
      <td >${prodInfo.prodStatusName}</td>
    </tr>
    <tr>
      <td class="tit">供应商：</td>
      <td>${prodInfo.supplier=="" ? "" : prodInfo.supplier.supplier}</td>
      <td class="tit"><span class="cRed">*</span>项目总额度(万)：</td>
      <td>${prodInfo.pjtTotalAmount}</td>
    </tr>
    <tr>
      <td class="tit">项目总额度备注：</td>
      <td>${prodInfo.pjtAmountRemark}</td>
      <td class="tit"><span class="cRed">*</span>发行额度(万)：</td>
      <td>${prodInfo.sellAmount}</td>
    </tr>
    <tr>
      <td class="tit">期限(月)：</td>
      <td>${prodInfo.sellTimeLimit}</td>
      <td class="tit">期限备注：</td>
      <td>${prodInfo.timeLimitRemark}</td>
    </tr>
    <tr>
      <td class="tit">销售级别：</td>
      <td >${prodInfo.sellRankName}</td>
      <td class="tit">运营预警：</td>
      <td>${prodInfo.operationWarning}</td>
    </tr>
    <tr>
      <td class="tit">产品核算系数：</td>
      <td>${prodInfo.accountCoefficient }</td>
      <td class="tit"><span class="cRed">*</span>是否赎回：</td>
      <td>${prodInfo.redeemFlag ? "是" : "否"}</td>
    </tr>
    <tr>
      <td class="tit">预期年化收益率：</td>
      <td colspan="3">
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
											                	<th width="25%">下限(万)</th>
											                  	<th width="25%">上限(万)</th>
												      			<th width="25%">年化收益率(%)</th>
												      			<th width="25%">受益类别</th>
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
      <td ><fmt:formatDate value="${prodInfo.sellDate}" pattern="yyyy-MM-dd"/></td>
      <td class="tit">停止划款时间：</td>
      <td><fmt:formatDate value="${prodInfo.transferDeadline}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
      <td class="tit">募集结束日期：</td>
      <td ><fmt:formatDate value="${prodInfo.raiseFundsEndTime}" pattern="yyyy-MM-dd"/></td>
      <td class="tit">产品成立日期：</td>
      <td><fmt:formatDate value="${prodInfo.prodSetUpDate}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
      <td class="tit">产品兑付日期：</td>
      <td ><fmt:formatDate value="${prodInfo.prodCashDate}" pattern="yyyy-MM-dd"/></td>
      <td class="tit">实际兑付日期：</td>
      <td><fmt:formatDate value="${prodInfo.actualCashDate}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
      <td class="tit">付息方式：</td>
      <td colspan="3">${prodInfo.payInterestMethod}</td>
    </tr>
    <tr>
      <td class="tit">产品付息日期：</td>
      <td colspan="3">
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
											                	<th width="50%">预计付息日</th>
												      			<th width="50%">实际付息日</th>
											                </tr>
										                </thead>
										                <tbody id="prodPayDate">
										                	<c:if test="${prodInfo.prodPayDates != null }">
										                		<c:forEach items="${prodInfo.prodPayDates}" var="prodPayDate">
										                			<tr id="${prodPayDate.id }">
										           						<td><fmt:formatDate value="${prodPayDate.expectPayDate }" pattern="yyyy-MM-dd"/></td>
										           						<td><fmt:formatDate value="${prodPayDate.actualPayDate }" pattern="yyyy-MM-dd"/></td>
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
      <td >${prodInfo.trustBank}</td>
      <td class="tit">开户行：</td>
      <td>${prodInfo.raiseBank}</td>
    </tr>
    <tr>
      <td class="tit">募集账号：</td>
      <td colspan="3">${prodInfo.raiseAccount}</td>
    </tr>
    <tr>
      <td class="tit">简版附件：</td>
      <td colspan="3">
      	<c:if test="${!empty prodInfo.janeEditionAttch}">
      		<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${prodInfo.janeEditionAttch.filePath }" target="_blank">${prodInfo.janeEditionAttch.displayName }${prodInfo.janeEditionAttch.fileExt }</a>
      	</c:if>
      </td>
    </tr>
    <tr>
      <td class="tit">非简版附件：</td>
      <td colspan="3">
      	<c:if test="${!empty prodInfo.otherEditionAttch}">
  			<img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${prodInfo.otherEditionAttch.filePath }" target="_blank">${prodInfo.otherEditionAttch.displayName }${prodInfo.otherEditionAttch.fileExt }</a>
  		</c:if>
      </td>
    </tr>
    <tr>
      <td class="tit">产品经理：</td>
      <td>${prodInfo.prodManager}</td>
      <td class="tit">产品经理联系电话：</td>
      <td>${prodInfo.prodManagerTel}</td>
    </tr>
    <tr>
      <td class="tit">产品经理联系邮箱：</td>
      <td>${prodInfo.prodManagerEmail}</td>
      <td class="tit">产品督导：</td>
      <td>${prodInfo.prodSupervisor}</td>
    </tr>
    <tr>
      <td class="tit">产品督导联系方式：</td>
      <td>${prodInfo.supervisorContactWay}</td>
      <td class="tit">备注：</td>
      <td>${prodInfo.supervisorRemark}</td>
    </tr>
    <tr>
      <td class="tit">预计发行日期：</td>
      <td><fmt:formatDate value="${prodInfo.expectSellDate}" pattern="yyyy-MM-dd"/></td>
      <td class="tit">运营周期(天)：</td>
      <td>${prodInfo.operationPeriod}</td>
    </tr>
    <tr>
      <td class="tit">预约划款合计(万)：</td>
      <td>${prodInfo.totalAppointmentAmount}</td>
      <td class="tit">划款金额(万)：</td>
      <td>${prodInfo.transferAmount}</td>
    </tr>
    <tr>
      <td class="tit">到账金额(万)：</td>
      <td>${prodInfo.toAccountAmount}</td>
      <td class="tit">到账小额数：</td>
      <td>${prodInfo.toAccountSmallAmount}</td>
    </tr>
    <tr>
      <td class="tit">是否需评级：</td>
      <td>${prodInfo.needGradeFlag ? "是" : "否"}</td>
      <td class="tit">可用额度(万)：</td>
      <td>${prodInfo.remainAmount}</td>
    </tr>
    <tr>
      <td class="tit">视频讲座：</td>
      <td colspan="3">${prodInfo.videoLectures}</td>
    </tr>
    <tr>
      <td class="tit">创建人：</td>
      <td colspan="3">${prodInfo.creator}</td>
    </tr>
  </table>
  <div class="btTip"></div>
</div>
