<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/cost/reimburse/comm_reim.js"></script>
<script type="text/javascript" src="page/cost/reimburse/draft_reim.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<title></title>
</head>
<body class="bdNone">
<div class="tabMid2">
<!--费用审批-->
<div class="addCon">
	<form id="reiForm">
	  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
		  <tr>
			<th colspan="6">报销单填写</th>
		  </tr>
		  <tr>
			<td class="tit"><div style="width:78px">报销单号</div></td>
			<td width="180" align="right"><input type="hidden" name="id" id="reimid" value="${reimDraft.id}"/></td>
			<td class="tit">填单时间</td>
			<td width="180"><fmt:formatDate value="${reimDraft.applyDate}" pattern="yyyy-MM-dd HH:mm"/></td>
			<td class="tit">财务隶属</td>
			<td width="180">
			  <select id="finance" name="finance" class="sel02" style="width:150px">
				<c:forEach var="d" items="${areas}" >
			        <option value="${d.dictCode }" ${d.dictCode eq reimDraft.finance ? 'selected':'' }>${d.dictName }</option>
				</c:forEach>
    		  </select>
			</td>
		  </tr>
		  <tr>
			<td class="tit">报销人</td>
			<td>${reimDraft.applicantName}</td>
			<td class="tit">所属机构</td>
			<td>
			  <select id="groupname" name="groupname" class="sel02" style="width:110px">
				<c:forEach var="g" items="${groups}" >
			        <option value="${g.name }" ${g.name eq reimDraft.applyDept ? 'selected':'' }>${g.name }</option>
				</c:forEach>
    		  </select>
			</td>
			<td class="tit"><div style="width:99px; text-align:right">报销总额</div></td>
			<td><b id="outlaySumShow"><fmt:formatNumber value="${reimDraft.reimbursementSum}" type="currency" pattern="0.00"/> </b> 元</td>
		  </tr>
		  <tr>
		  	<td class="tit">预借款额</td>
			<td><input id="loansum" name="loansum" type="text" maxlength="8" style="width:120px" class="ipt01" size="10" value="${reimDraft.loanSum}"/>  元</td>
		  	<td class="tit"><div style="width:99px; text-align:right;">指定审批人</div></td>
			<td><input name=appointTo id="appointTo" type="hidden" value="${reimDraft.appointTo}"/>
			<input name="appointToName" id="appointToName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${reimDraft.appointToName}"/><input type="button" id="openAppointToSelect" class="selBtn"></td>
		  	<td class="tit">受款人</td>
			<td><input name="payee" id="payee" type="hidden" value="${reimDraft.payee}"/>
			<input name="payeeName" id="payeeName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${reimDraft.payeeName}"/><input type="button" id="openUserSelect" class="selBtn"></td>
		  </tr>
		  <tr>
			<td class="tit">报销事由</td>
			<td colspan="5"><textarea id="causa" style="width:727px" name="causa" class="area01" >${reimDraft.causa}</textarea></td>
		  </tr>
	  </table>
	</form>
	</div>
	<div class="blank" style="height:3px"></div>	
<div class="costsListHead">
  <div>费用明细表</div>
  <span><input id="costsAdd" class="allBtn" type="button" value="新增"/></span>
</div>
<div class="blank" style="height:3px"></div>
<div class="feesSp"></div>
</div>
<div class="blank"></div>
	<div class="addTool2">
		<input id="startFlow" class="allBtn" type="button" value="提交" name="saveActorAcc"/>
		<input id="saveAsDraft" class="allBtn" type="button" value="保存为草稿" name="saveActorAcc"/>
	</div>
	<p>&nbsp;</p><p>&nbsp;</p>
	
<!--初始化Javascript对象-->
<script language="javascript">
	var jsonStr = '${reimJson}'; 
	var jsonObj = eval('('+jsonStr+')');
	if (jsonObj) {
		reimItemDrafts = jsonObj.reimItems;
		draw();
	}
	
</script>	
</body>
</html>