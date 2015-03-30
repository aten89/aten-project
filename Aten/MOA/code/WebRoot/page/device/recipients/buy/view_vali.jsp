<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/recipients/buy/view_vali.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<title>设备领用申购单</title>
</head>
<body class="bdNone">
<div class="tabMid">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th colspan="4"><img src="themes/comm/images/frameNav2.gif"/>设备申购验收单</th>
      </tr>
      <tr>
        <td class="tit">验收单编号</td>
        <td colspan="3">${form.fullFormNO}</td>
      </tr>
      <tr>
         <td class="tit">检验人</td>
         <td class="field">${form.accountName}</td>
         <td class="tit">检验日期</td>
         <td width="420"><fmt:formatDate value="${form.valiDate }" pattern="yyyy-MM-dd"/></td>
       </tr>
       <tr>
         <td class="tit">检查项</td>
         <td colspan="3" style="padding-top:6px;padding-bottom:6px;"><div class='meetingSb'>
             <table width="628" border="0"  cellpadding="0"  cellspacing="1">
               <thead>
                 <tr>
                   <th width="159">名称</th>
                   <th width="110">是否合格</th>
                   <th width="258">备注</th>
                 </tr>
                </thead>
                 <c:forEach var="valieDetail" items="${form.deviceValiDetails}"> 
					<tr>
					 <td>${valieDetail.item}</td>
					<td>${valieDetail.isEligibility?'是':'否'}</td>
					<td>${valieDetail.remark}</td>
					</tr>
				</c:forEach>
               
             </table>
           </div></td>
       </tr>
       <tr>
         <td class="tit">备注</td>
         <td colspan="3" style="">${form.remark}</td>
       </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="blank"></div>
</body>
