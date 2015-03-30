<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/DataManage/ImportAllocate/AutoAllotCustomer.js"></script>

<title>自动分配客户</title>
</head>
<body class="tabBd">
	<input type="hidden" id="id" value=""/>
	<!--选择销售员工start-->
	<div class="tabScroll" style="overflow-x: hidden">
		<div class="addCon" align="center">
			<table border="0" width="100%" cellpadding="0" cellspacing="1">
				<thead>
					<th width="40%">销售人员</th>
					<th width="60%">分配客户数</th>
				</thead>
				<tbody id="saleStaffTBody">
					<c:if test="${!empty saleStaffs}">
						<c:forEach var="saleStaff" items="${saleStaffs }">
							<tr>
								<td class="tit">${saleStaff.displayName }
									<input type="hidden" id="${saleStaff.accountID }" value="${saleStaff.accountID }"/>
								</td>
		 						<td >
		 							<input type="text" class="ipt05" id="" value="0"/>
		 						</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
			<br/>
			<div class="addCon" align="center" style="color:blue;">待分配客户数：
			<span id="totalToAllotNumSpan"></span>
				<input type="hidden" value="" id="totalToAllotNum"/>
			</div>
		</div>
	</div>
	<!--选择销售员工end-->
	<div class="oppBtnBg tabBottom">
		<input class="allBtn" id="save" type="button" value="确定"/>
		<input class="allBtn" id="cancel" type="button" value="取消"/>
	</div>
</body>
</html>

