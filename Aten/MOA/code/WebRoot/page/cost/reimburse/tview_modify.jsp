<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/cost/reimburse/comm_reim.js"></script>
<script type="text/javascript" src="page/cost/reimburse/tview_modify.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<title></title>
<style>
.allList td{ padding:3px}
.allList th{ padding:2px 3px 3px 3px}</style>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='rei_deal'/>" />
<!--报销单审批-->
<form id="reiForm">
<div class="tabMid2" id="txbd">
	<div class="addCon">
	
	  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
		  <tr>
			<th colspan="6">报销单修改</th>
		  </tr>
		  <tr>
			<td class="tit"><div style="width:78px">报销单号</div></td>
			<td width="180" id="reimid">${reimbusement.id}</td>
			<td class="tit">填单时间</td>
			<td width="180"><fmt:formatDate value="${reimbusement.applyDate}" pattern="yyyy-MM-dd HH:mm"/></td>
			<td class="tit">财务隶属</td>
			<td width="180">
				 <select id="finance" name="finance" class="sel02" style="width:110px">
				<c:forEach var="d" items="${areas}" >
			        <option value="${d.dictCode }" ${d.dictCode eq reimbusement.finance ? 'selected':'' }>${d.dictName }</option>
				</c:forEach>
    		  </select>
			</td>
		  </tr>
		  <tr>
			<td class="tit">报销人</td>
			<td>${reimbusement.applicantName}</td>
			<td class="tit">所属机构</td>
			<td id="applyDept">${reimbusement.applyDept}</td>
			<td class="tit">受款人</td>
			<td><input name="payee" id="payee" type="hidden" value="${reimbusement.payee}"/>
			<input name="payeeName" id="payeeName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${reimbusement.payeeName}"/>&nbsp;
			<input type="button" id="openUserSelect" class="selBtn"></td>
		  </tr>
		  <tr>
		  	<td class="tit">预借款额</td>
			<td><input id="loansum" name="loansum" type="text" maxlength="8" class="ipt01" size="10" value="${reimbusement.loanSum}"/></td>
		  	<td class="tit">指定审批人</td>
			<td><input name=appointTo id="appointTo" type="hidden" value="${reimbusement.appointTo}"/>
			<input name="appointToName" id="appointToName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${reimbusement.appointToName}"/>&nbsp;
			<input type="button" id="openAppointToSelect" class="selBtn"></td>
		  	<td class="tit">报销总额</td>
			<td><b id="outlaySumShow"><fmt:formatNumber value="${reimbusement.reimbursementSum}" type="currency" pattern="0.00"/></b> 元</td>
		  </tr>
		  <tr>
			<td class="tit">报销事由</td>
			<td colspan="5"><textarea id="causa" style="width:727px" name="causa" class="area01" >${reimbusement.causa}</textarea></td>
		  </tr>
	  </table>
	
	</div>
	<div class="blank"></div>
	<div class="costsListHead">
	
<div>费用明细表</div>
  <span><input id="costsAdd" class="allBtn" type="button" value="新增"/></span>
</div>
<div class="blank" style="height:3px"></div>
<div class="feesSp"></div>
	<div class="blank"></div>
	<div class="addCon">
		<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
		      <tr>
			    <th colspan="2">审批</th>
			  </tr>
			  <tr>
		        <td  class="tit">修改说明</td>
		        <td><textarea id="comment" class="area01" style="width: 632px;" name="comment"></textarea>
		        	<input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
		        	<input type="hidden" id="cancel" name="cancel"/>
		        </td>
		      </tr>
		      <tr>
		        <td  class="tit">操&nbsp;&nbsp;&nbsp;&nbsp;作</td>
		         <td>
		         	<input type="hidden" id="transitionName" name="transition"/>
		            <span id="commitBut">
<c:forEach var="transition" items="${transitions}" >
						<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
</c:forEach>
			        </span>
			        <input class="allBtn" type="button" value="关闭" onclick="$.getMainFrame().getCurrentTab().close();" style="margin-left:50px"/>
				 </td>
		     </tr>
		</table>
	</div>	  
	<div class="blank"></div>
	<div class="costsLog" id="costsLog">
		<h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
		<div class="mb">
 					<c:forEach var="task" items="${tasks}" >
                      	<div class="processBk">
                          <div class="clgcTit">
                            <ul class="clearfix">
                              <li><b>处理人：</b>${task.transactorDisplayName }</li>
                              <li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>处理状态：</b>
                            	 <c:if test="${task.taskState=='ps_create'}">
                              		<img src="themes/comm/images/stateIcoNone.gif">[未查看]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_start'}">
                              		<img src="themes/comm/images/stateIco02.gif">[已查看,未处理]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_end'}">
                              		<img src="themes/comm/images/stateIco01.gif">[已处理]
                              	</c:if>
                              </li>
                            </ul>
                          </div>
                          <div class="taskCon nonebb">
                            <div class="handleCon"> ${task.comment } </div>
                          </div>
                        </div>
                      </c:forEach>
	</div>
	<div class="blank"></div>
</div>
</div>
</form>
<!--报销单审批 end-->
<script language="javascript">
	var jsonStr = '${reimJson}'; 
	var jsonObj = eval('('+jsonStr+')');
	reimItemDrafts = jsonObj.reimItems;
	draw();
</script>
</body>
</html>
