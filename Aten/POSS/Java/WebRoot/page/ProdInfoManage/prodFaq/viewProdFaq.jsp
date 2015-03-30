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
<script type="text/javascript" src="page/ProdInfoManage/prodFaq/viewProdFaq.js"></script>
</head>

<body class="bdNone">
<input type="hidden" id="id"  value="${prodFaq.id}" />
<div class="tabMid">
  <div class="addCon">
  	<table width="100%" align="center" border="0" cellpadding="0"  cellspacing="1">
      <tr>
        <th colspan="4" class="tit"><div style="height:15px">产品问题</div></th>
      </tr>
      <tr>
      	<td class="tit">提问人：</td>
      	<td >${prodFaq.creatorName }</td>
      	<td class="tit">发布时间：</td>
      	<td ><fmt:formatDate value="${prodFaq.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
      </tr>
      <tr>
        <td class="tit" style="padding-top:10px"  valign="top"><div>产品：</div></td>
        <td colspan="3" style="width:600px">${prodFaq.prodInfo.prodName }</td>
      </tr>
      <tr>
        <td class="tit" style="padding-top:10px"  valign="top"><div>问题概述：</div></td>
        <td colspan="3" style="width:600px">${prodFaq.title }</td>
      </tr>
      <tr>
        <td class="tit" style="padding-top:10px"  valign="top"><div>详细描述：</div></td>
        <td colspan="3" style="width:600px">${prodFaq.content }</td>
      </tr>
      <tr>
        <td colspan="4" class="tit"><div style="height:15px"><span style="float:left;">问题解答</span></div></td>
      </tr>
      <tr>
      	<td class="tit">问题解答：</td>
      	<td colspan="3"><textarea id="content" rows="" class="area01" value=""></textarea></td>
      </tr>
      <tr>
      	<td colspan="4">
      		<div class="addTool2">
				<input id="save" type="button" value="提交" class="allBtn"/>
				<input id="cancel" type="button" value="取消" class="allBtn"/>
			</div>
      	</td>
      </tr>
      <tr>
        <td colspan="4" class="tit"><div style="height:15px"><span style="float:left;">历史答复</span></div></td>
      </tr>
      <tr>
      	<td colspan="4">
      		<div >
      			<c:if test="${!empty prodFaq.childProdFaqs }">
      				<c:forEach var="childProdFaq" items="${prodFaq.childProdFaqs }">
      					<div class="processBk">
      						<div class="clgcTit">
      							<ul class="clearfix">
      								<li><b>解答人：</b>${childProdFaq.creatorName }</li>
      								<li><b>解答时间：</b><fmt:formatDate value="${childProdFaq.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></li>
      							</ul>
      						</div>
      						<div class="taskCon nonebb">
      							<div class="taskTit ico_lab"></div>
      							<div class="handleCon">${childProdFaq.content }</div>
      						</div>
      					</div>
      				</c:forEach>
      			</c:if>
      		</div>
      	</td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>