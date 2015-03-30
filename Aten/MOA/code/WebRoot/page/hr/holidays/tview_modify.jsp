<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/hr/holidays/tview_modify.js"></script>
<script type="text/javascript" src="page/hr/holidays/comm_holi.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
  <!--请假单-->
  <div class="addCon">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      <thead>
        <tr>
          <th colspan="6">请假单</th>
        </tr>
      </thead>
      <tr>
      	<td width="100" class="tit">请假单号</td>
        <td width="200" id="holiid">${holidayApply.id}</td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${holidayApply.applicantName}</td>
        <td width="100" class="tit">所在部门</td>
         <td width="230" id="applyDept">${holidayApply.applyDept}</td>
      </tr>
      <tr>
      	<td class="tit">人员隶属</td>
        <td>
          <select id="regional" name="regional" class="sel02" style="width:110px">
			<c:forEach var="d" items="${areas}" >
		        <option value="${d.dictCode }" ${d.dictCode eq holidayApply.regional ? 'selected':'' }>${d.dictName }</option>
			</c:forEach>
   		  </select>
        </td>
        <td class="tit">指定审批人</td>
        <td colspan="3">
        	<div style="float:left">
        	<input id="appointTo" type="hidden" value="${holidayApply.appointTo}"/>
			<input id="appointToName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${holidayApply.appointToName}"/>
			<input type="button" id="openAppointToSelect" class="selBtn">
			</div>
			<div style="color:#E3E0D5;float:left;margin:0 0 0 18px;width:160px;height:14px">必要时可指特殊人员先审批。 </div>
        </td>
      </tr>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow" style="padding:0 6px 20px 6px">
          <div class="costsListHead">
		  	<div style="width:200px;padding:0 0px 20px 0px">请假单明细<span id="totalInfo"></span></div>
		  	<span><input id="holiAdd" class="allBtn" type="button" value="新增"/></span>
		  </div>
          <div class="boxShadow" style="left:5px" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <!--预算信息-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th width="21%">假期类型</th>
                            <th width="38%">请假期限</th>
                            <th width="12%">天数</th>
                            <th>附加说明</th>
                            <th width="5%"><div style="width:80px">操作</div></th>
                          </tr>
                          <tbody id="holidayList">
                          </tbody>
                        </table>
                      </div>
                      <!--预算信息 end-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
         </td>
      </tr>
      <tr>
        <td class="tit"  colspan="6" style="text-align:left;width:auto">
          <input id="isSpecial" type="checkbox" <c:if test="${holidayApply.isSpecial==true}">checked</c:if> value="1" class="cBox" onclick="displayReason();" /><label for="isSpecial" onclick="displayReason();">申请特批</label>
          <span style="color:#555;font-style:italic;">（不符合规定的请假单，须经过特批！请慎重选择！）</span></td>
      </tr>
      <tr id="specialReasonPanel" style='display:${holidayApply.isSpecial==true ? "": "none"}'>
        <td class="tit">特批理由</td>
        <td colspan="5"><textarea id="specialReason" class="area01" >${holidayApply.specialReason}</textarea></td>
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
                              		<img src="themes/comm/images/stateIcoNone.gif"/>[未查看]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_start'}">
                              		<img src="themes/comm/images/stateIco02.gif"/>[已查看,未处理]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_end'}">
                              		<img src="themes/comm/images/stateIco01.gif"/>[已处理]
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
	</div>
<div class="blank"></div>

  <!--请假单 end-->
  <c:forEach var="holiday" items="${holidayApply.holidayDetail}" >
	<textarea style="display:none" id="tmp_${holiday.id}">${holiday.remark}</textarea>
  </c:forEach>
    <div class="addTool2">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	<tr>
		         <td>
		          	<input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
		         	<input type="hidden" id="transitionName"/>
		        <c:if test="${not empty param.notify}"> 
					<span id="notifyUserNames" style="display:none;color:blue;"></span>
					<div class="blank" style="height:5px;"></div>
					<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'修改的请假申请', 'QJLC', '/m/holi_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;&nbsp;&nbsp;
			    </c:if>
			    
		            <span id="commitBut">
				<c:forEach var="transition" items="${transitions}" >
						<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
				</c:forEach>
					</span>
				 </td>
     </tr>
     </table>
	</div>
</div>
<script language="javascript">
	var jsonStr = '${holiJson}'; 
	var jsonObj = eval('('+jsonStr+')');
	if (jsonObj) {
		holiInfos = jsonObj.holidayDetail;
		draw();
	}
</script>
</body>
