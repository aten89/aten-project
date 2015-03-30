<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/portlet/query_product.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>
<body >
<input type="hidden" id="typeSize"  value="${fn:length(prodTypeEntitys)}  " />
<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="0">
	<tr>
     	<td colspan="4">&nbsp;</td>
   	</tr>
   	<tr>
     	<td colspan="4" class="tipList">&nbsp;</td>
   	</tr>
    <tr>
	  <td colspan="4" class="tipShow">
		<span class="crmSubTabsBk">
		  <ul class="crmSubTabs" id="crmTab" style="width:800px">
	<c:if test="${!empty prodTypeEntitys}">
		<c:forEach var="prodtype" items="${prodTypeEntitys }" varStatus="status"> 
			<li><div>${prodtype.prodType }</div></li>
		</c:forEach>
	</c:if>
			<li id="foundProdLI"><div class="lastNone">已成立产品</div></li>
		  </ul>
		</span>
		<div>
		  <div class="boxShadow">
			<div class="shadow01">
      		  <div class="shadow02" >
          		<div class="shadow03" >
                  <div class="shadow04" >
                 	<div class="shadow04" >
                   	  <div class="shadowCon" style="width:100%;">
		<c:if test="${!empty prodTypeEntitys}">
			<c:forEach var="prodtype" items="${prodTypeEntitys }" varStatus="status"> 
						<div id="tab0${status.index}" style="display:none">
					 	  <input type="hidden" id="prodType${status.index }" value="${prodtype.id }"/>
			        	  <div class="gnmkBk"  style="height:140px;width:800px;overflow:auto;">
			           		<div class="allList">
							  <table id="informationTab" width="1500px" border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<th width="28%">产品名称</th>
										<th width="8%">产品二级分类</th>
										<th width="7%">供应商</th>
										<th width="6%">发行额度(万)</th>
										<th width="6%">预计发行日期</th>
										<th width="6%">运营周期(天)</th>
										<th width="4%">期限(月)</th>
										<th width="6%">销售级别</th>
										<th width="7%">预约划款合计(万)</th>
										<th width="6%">划款金额(万)</th>
										<th width="6%">到账金额(万)</th>
										<th width="7%">项目剩余额度(万)</th>
										<th width="3%">操作</th>
									</tr>
								</thead>
								<tbody id="prodList${status.index}">
								</tbody>
							  </table>
							</div>
			          	  </div> 
						</div>
			</c:forEach>
		</c:if>
						<!-- 已成立产品 start -->
						<div id="tab0_last" style="display:none">
			        	  <div class="gnmkBk"  style="height:140px;width:800px;overflow:auto;">
			           		<div class="allList">
							  <table id="informationTab" width="1500px" border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<th width="28%">产品名称</th>
										<th width="8%">产品二级分类</th>
										<th width="7%">供应商</th>
										<th width="6%">发行额度(万)</th>
										<th width="6%">预计发行日期</th>
										<th width="6%">运营周期(天)</th>
										<th width="4%">期限(月)</th>
										<th width="6%">销售级别</th>
										<th width="7%">预约划款合计(万)</th>
										<th width="6%">划款金额(万)</th>
										<th width="6%">到账金额(万)</th>
										<th width="7%">项目剩余额度(万)</th>
										<th width="3%">操作</th>
									</tr>
								</thead>
								<tbody id="prodList_last">
								</tbody>
							  </table>
							</div>
			          	  </div> 
						</div>
						<!-- 已成立产品 end -->
						
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
</body>
</html>