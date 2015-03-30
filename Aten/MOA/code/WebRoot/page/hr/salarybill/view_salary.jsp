<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/salarybill/view_salary.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
	<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="6">工资条</th>
		</tr>
      </thead>
	  <tr> 	
	 	<td width="10%" class="tit">月份</td>
        <td width="23%">${salaryBill.month}</td>
        <td width="10%"  class="tit">导入时间</td>
	    <td width="23%"><fmt:formatDate value="${salaryBill.importDate}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td width="10%"  class="tit">系统帐号</td>
        <td width="24%">${salaryBill.userAccountID}</td>
        
	  </tr>
	  <tr>
        <td class="tit">部门</td>
	    <td>${salaryBill.dept}</td>
	    <td class="tit">职务</td>
	    <td>${salaryBill.post}</td>
        <td class="tit">工号</td>
	    <td>${salaryBill.employeeNumber}</td>
      </tr>
      <tr>
      	<td width="100" class="tit">姓名</td>
        <td width="230">${salaryBill.userName}</td>
      	<td class="tit">入职日期</td>
	    <td><fmt:formatDate value="${salaryBill.entryDate}" pattern="yyyy-MM-dd"/></td>
	    <td class="tit">人数</td>
	    <td>${salaryBill.peopleNums}</td>
      </tr>
      <tr>
        <td class="tit">状态</td>
	    <td>${salaryBill.status}</td>
	    <td class="tit">工资总额</td>
	    <td>${salaryBill.wageTotal}</td>
	    <td class="tit">基本工资</td>
	    <td>${salaryBill.wageBasic}</td>
      </tr>
      <tr>
        <td class="tit">绩效工资</td>
	    <td>${salaryBill.wagePerformance}</td>
	    <td class="tit">绩效得分</td>
	    <td>${salaryBill.scorePerformance}</td>
	    <td class="tit">实际绩效工资</td>
	    <td>${salaryBill.wagePerformanceReal}</td>
      </tr>
      <tr>
        <td class="tit">补助</td>
	    <td>${salaryBill.allowance}</td>
	    <td class="tit">提成</td>
	    <td>${salaryBill.commission}</td>
	    <td class="tit">不足月天数</td>
	    <td>${salaryBill.lessMonthDays}</td>
      </tr>
      <tr>
        <td class="tit">不足月扣款</td>
	    <td>${salaryBill.deductLessMonth}</td>
	    <td class="tit">事假天数</td>
	    <td>${salaryBill.leaveCompassionate}</td>
	    <td class="tit">事假扣款</td>
	    <td>${salaryBill.deductCompassionate}</td>
      </tr>
      <tr>
        <td class="tit">病假天数</td>
	    <td>${salaryBill.leaveSick}</td>
	    <td class="tit">病假扣款</td>
	    <td>${salaryBill.deductSick}</td>
	    <td class="tit">迟到扣款</td>
	    <td>${salaryBill.deductLate}</td>
      </tr>
      <tr>
        <td class="tit">其它补/扣款</td>
	    <td>${salaryBill.deductElse}</td>
	    <td class="tit">应发工资总额</td>
	    <td>${salaryBill.wagePayable}</td>
	    <td class="tit">养老个人</td>
	    <td>${salaryBill.pension}</td>
      </tr>
      <tr>
        <td class="tit">失业个人</td>
	    <td>${salaryBill.lostJob}</td>
	    <td class="tit">医疗个人</td>
	    <td>${salaryBill.medical}</td>
	    <td class="tit">社保、公积金补缴</td>
	    <td>${salaryBill.insurancePayment}</td>
      </tr>
      <tr>
        <td class="tit">本月社保扣款</td>
	    <td>${salaryBill.costSocialSecurity}</td>
	    <td class="tit">本月公积金扣款</td>
	    <td>${salaryBill.costAccumulationFund}</td>
	    <td class="tit">五险一金扣款</td>
	    <td>${salaryBill.costFiveInsurance}</td>
      </tr>
      <tr>
        <td class="tit">应税工资</td>
	    <td>${salaryBill.taxWage}</td>
	    <td class="tit">税前工资</td>
	    <td>${salaryBill.wagePreTax}</td>
	    <td class="tit">个税</td>
	    <td>${salaryBill.taxPersonal}</td>
      </tr>
      <tr>
        <td class="tit">工资实发</td>
	    <td>${salaryBill.wageReal}</td>
	    <td class="tit">补发工资</td>
	    <td>${salaryBill.wageAllowance}</td>
	    <td class="tit"></td>
	    <td></td>
      </tr>
	</table>
  </div>
</body>
