<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/allocation/tview_modify.js"></script>
<title>设备调拨单</title>
</head>
<body class="bdNone">
<input id="id" type="hidden" value="${form.id }" />
<input id="deviceTypeCode" type="hidden" value="${form.deviceType }" />
<input id="moveType" type="hidden" value="${form.moveType }" />
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />
<input type="hidden" id="deleteDeviceIDs" value="" />
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备调拨单</th>
      </tr>
      <tr>
        <td class="tit">申请人</td>
        <td class="field">${form.applicantDisplayName }</td>
        <td class="tit">申请部门</td>
        <td class="field">${form.applyGroupName }</td>
        <td class="tit">申请日期</td>
        <td class="field"><fmt:formatDate value="${form.applyDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed">*</span>资产类别</td>
        <td class="field">${form.deviceTypeName }</td>
        <td class="tit"><span class="cRed">*</span>调拨类型</td>
        <td class="field"><div style="width:139px">
            <div id="allotTypeSel" name="allotTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>调拨日期</td>
        <td><input type="text" id="moveDate" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${form.moveDate }" pattern="yyyy-MM-dd"/>" /></td>
      </tr>
      <tr id="trInInfo">
        <td class="tit"><span class="cRed">*</span>调入经办人</td>
        <td>
			<input id="inAccountName" type="text" maxlength="50" class="ipt05" value="${form.inApplicantDisplayName }" style="width:99px" readonly />
   			<input id="inAccountID" type="hidden" value="${form.inAccountID }" />
   			<input id="openInAccountSelect" class="selBtn" type="button"/>
		</td>
        <td class="tit"><span class="cRed">*</span>调入部门</td>
        <td class="field" colspan="3"><span id="inGroupName">${form.inGroupName }</span></td>
      </tr>
      <tr id="sgsm">
        <td class="tit"><span class="cRed">*</span>调拨原因</td>
        <td width="600" colspan="5"><textarea id="reason" class="area01 awIT">${form.reason }</textarea></td>
      </tr>
    </table>
  </div>
  <!--设备清单-->
  <div id="devListBody">
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div style="width:100%"><label  style="float:left">设备清单</label><a id="gmcpAdd" style=" float:right;font-weight:normal; cursor:pointer"><img src="themes/comm/images/customBtn.gif" />选择设备</a></div>
  </div>
  <c:forEach items="${form.devAllocateLists  }" var="devAllocateList">
  <div class="feesSp fsMar">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
      <tr>
        <td class="spNum"><span name="orderNum">${devPurchaseList.displayOrder }</span></td>
        <td colspan="6" class="spOpW opw2">
            <div>
                <a href="javascript:void(0)" onclick="delItem(this)"><img src="themes/comm/images/spDel.gif" />删除</a>
            </div>
        </td>
      </tr>
      <tr>
      	<td height="27" class="spTit" style="width:auto">设备编号：</td>
        <td width="128">${devAllocateList.device.deviceNO }</td>
        <td height="27" class="spTit" style="width:auto">设备名称：</td>
        <td width="150"><span name="deviceName">${devAllocateList.device.deviceName }</span>
        	<input type="hidden" name="deviceID" value="${devAllocateList.device.id }" />
        	<input type="hidden" name="listDevBuyType" value="${devAllocateList.device.buyType }"/>
        	<input type="hidden" name="listDevBuyTypeName" value="${devAllocateList.device.buyTypeName }"/>
        	<input type="hidden" name="listAreaName" value="${devAllocateList.device.areaName }"/>
       		<input type="hidden" name="listAreaCode" value="${devAllocateList.device.areaCode }"/>
        	</td>
        <td width="70" class="spTit">设备型号：</td>
        <td width="180">${devAllocateList.device.deviceModel }</td>
        <td rowspan="4"  class="spOpW">&nbsp;</td>
      </tr>
      <c:if test="${!empty devAllocateList.device.devicePropertyDetails}">
      <tr>
        <td class="spTit" style="width:auto">配置信息：</td>
        <td colspan="5" width="360"><span name="devCfgDesc">${devAllocateList.device.configList }</span></td>
      </tr>
      </c:if>
      <tr>
      	<td class="spTit">设备类别：</td>
        <td>${devAllocateList.device.deviceClass.name }</td>
        <td  class="spTit" style="width:120px">调拨前工作所在地：</td>
        <td colspan="3">${devAllocateList.device.deviceCurStatusInfo.areaName }</td>
      </tr>
    </table>
    <div class="blank" style="height:5px"></div>
  </div>
  </c:forEach>
  </div>
  <!--设备清单 end-->
  <div class="blank" style="height:3px"></div>
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current" style="cursor:default;">
              <div class="lastNone">审批意见</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon"> 
                      <!--查看审批意见 begin-->
                      <div class="clyj htspyj"  style="padding-top:0">
                        <div class="tit01"  style="cursor:pointer" onclick="apprCommSwitch('htcpOpen','ckspyj')"><img src="themes/comm/images/htcpOpen.gif" id="htcpOpen"/> 查看审批意见</div>
                      </div>
                      <div id="ckspyj">
                        <c:forEach var="task" items="${tasks}" >
                          <div class="processBk">
                            <div class="clgcTit">
                              <ul class="clearfix">
                                <li><b>处理人：</b>${task.transactorDisplayName}</li>
                                <li><b>到达时间：</b><fmt:formatDate value="${task.createTime }" pattern="yyyy-MM-dd HH:mm"/></li>
                                <li><b>完成时间：</b><fmt:formatDate value="${task.endTime }" pattern="yyyy-MM-dd HH:mm"/></li>
                                <li><b>步骤：</b>${task.nodeName}</li>
                              </ul>
                            </div>
                            <div class="taskCon nonebb">
                              <div class="handleCon ico_lab">${task.comment }</div>
                            </div>
                          </div>
						</c:forEach>
                      </div>
                      <!--查看审批意见 end--> 
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
    </table>
  </div>
</div>
<div class="addTool2">
  <input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
	<c:forEach var="transition" items="${transitions}" >
		<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
</div>
<div class="blank"></div>
</body>
