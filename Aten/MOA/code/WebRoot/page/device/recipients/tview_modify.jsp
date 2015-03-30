<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/recipients/tview_modify.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<title>新增设备</title>
</head>
<body class="bdNone">
<input type="hidden" id="id" value="${form.id}" />
<input type="hidden" id="applyType" value="${form.applyType}" />
<input type="hidden" id="buyType" value="${form.buyType}" />
<c:choose>
	<c:when test="${form.applyType=='0' }">
	<input type="hidden" id="deviceClassCode" value="${form.deviceClass }"/>
	</c:when>
	<c:otherwise>
	<input type="hidden" id="deviceClassCode" value="${form.purchaseDeviceClass.id }"/>
	</c:otherwise>
</c:choose>
<input type="hidden" id="deviceTypeCode" value="${form.deviceType}" />
<input type="hidden" id="deleteDeviceIDs" value="" />
<input type="hidden" id="areaCode" value="${form.areaCode}" />
<input type="hidden" id="purposes" value="${form.purposes }"/>
<input type="hidden" id="areaCodePurpose" value="${form.workAreaCode}"/>
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th colspan="6" id="formTitleTH"><img src="themes/comm/images/frameNav2.gif"/>设备${form.applyType=='0'?'领用':'申购' }单</th>
      </tr>
      <tr>
        <td class="tit">申请单编号</td>
        <td class="field">${form.fullFormNO}</td>
        <td class="tit">申请类型</td>
        <td class="field" colspan="3"><c:choose>
        	<c:when test="${form.applyType == 0}">领用</c:when><c:otherwise>申购</c:otherwise>
        </c:choose></td>
      </tr>
      <tr>
        <td class="tit">申请人</td>
        <td class="field">${form.applicantDisplayName }<input type="hidden" id="applicant" value="${form.applicant }"/></td>
        <td class="tit">申请部门</td>
        <td class="field"><span id="applyGroupName">${form.applyGroupName }</span></td>
        <td class="tit">申请日期</td>
        <td class="field"><fmt:formatDate value="${form.applyDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed">*</span>所属区域</td>
        <td class="field">${form.areaName }</td>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td class="field">${form.deviceTypeName }</td>
        <td class="tit"><span class="cRed">*</span>预计使用日期</td>
        <td><input type="text" id="planUseDate" maxlength="100" readonly="readonly" class="invokeBoth" style="width:65px" 
        	value="<fmt:formatDate value="${form.planUseDate }" pattern="yyyy-MM-dd"/>"/></td>
      	
      </tr>
      <c:if test="${form.applyType == '1'}">
      <tr id="areaDeviceTR">
        <td class="tit"><span class="cRed">*</span>设备类别</td>
        <td class="field">${form.deviceClassDisplayName }</td>
        <td class="tit"><span class="cRed">*</span>购买方式</td>
        <td class="field"><div style="float:left; width:139px;">
                <div id="buyTypeSel" name="buyTypeSel"></div>
              </div></td>
       <td class="tit"><span class="cRed">*</span>预算金额</td>
        <td class="field"><input type="text" class="ipt05" style="width:100px" id="budgetMoney" maxlength="12" value="<fmt:formatNumber value="${form.budgetMoney }" pattern="0.00"/>"/>元</td>
      </tr>
      <tr id="devCfgDescTR">
        <td class="tit"><span class="cRed">*</span>配置要求</td>
        <td width="600" colspan="5"><textarea id="devCfgDesc" class="area01  awIT">${form.devCfgDesc }</textarea></td>
      </tr>
       <tr>
        <td class="tit"><span class="cRed" id="red">*</span><span id="descriptTitle">申购说明</span></td>
        <td colspan="5"><textarea class="area01  awIT" id="remark" style="float:left">${form.remark }</textarea>
        <ul id="msg" class="allInfotip aiLeft;" style="width:350px;clear:none;">
            <li>请您详细填写要申购设备的信息（如品牌、型号、显卡等）</li>
        </ul>
        </td>
      </tr>
      </c:if>
     <c:if test="${form.applyType == '0'}">
      <tr>
        <td class="tit"><span id="descriptTitle">领用说明</span></td>
        <td colspan="5"><textarea class="area01  awIT" id="remark">${form.remark }</textarea></td>
      </tr>
      </c:if>
      </table>
  </div>
  <!--设备清单-->
  <c:if test="${form.applyType == 0 }">
  <div id="devListBody">
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div style="width:100%"><label  style="float:left">设备清单</label><a id="gmcpAdd2" onclick="equipmentChoose()"   style=" float:right;font-weight:normal; cursor:pointer"><img src="themes/comm/images/customBtn.gif" />选择设备</a></div>
  </div>
	  	
		 <c:forEach items="${form.devPurchaseLists  }" var="devPurchaseList">
		 <c:if test="${not empty devPurchaseList.device}">
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
			         <td width="70" height="27" class="spTit">设备编号：</td>
			        <td width="28"><span name="deviceNO">${devPurchaseList.device.deviceNO }</span></td>
			        <td width="70"  class="spTit">设备名称：</td>
			        <td width="100">${devPurchaseList.device.deviceName }<span name="deviceID" style="display: none">${devPurchaseList.device.id }</span>
			        	<input type="hidden" name="listAreaName" value="${devPurchaseList.device.areaName }"/>
                   	 	<input type="hidden" name="listAreaCode" value="${devPurchaseList.device.areaCode }"/>
			        </td>
			       <td width="70" class="spTit">设备型号：</td>
			        <td width="290">${devPurchaseList.device.deviceModel }</td>
			        <td rowspan="4"  class="spOpW">&nbsp;</td>
			      </tr>
			      <c:if test="${!empty devPurchaseList.device.devicePropertyDetails}">
			      <tr>
			        <td width="70" class="spTit">配置信息：</td>
			        <td colspan="5" width="500"><span name="devCfgDesc">${devPurchaseList.device.configList }</span></td>
			      </tr>
			      </c:if>
			      <tr>
			        <td width="70" height="27" class="spTit">设备类别：</td>
			        <td width="128"><span name="listDeviceClassName">${devPurchaseList.device.deviceClass.name }</span><input type="hidden" name="listDeviceClassID" value="${devPurchaseList.device.deviceClass.id }"/></td>
			        <td width="70" class="spTit">工作所在地：</td>
			        <td width="290" colspan="3"><select name='areaSel' style='width:60px;height:20px'><script language="javaScript"> document.write (createAreaOption("m/data_dict?act=ereasel", "", "", "${devPurchaseList.areaCode }")) </script></select></td>
			      </tr>
			    </table>
			    <div class="blank" style="height:5px"></div>
			  </div>
	  	</c:if>
	  	</c:forEach>
	  
  </div>
  </c:if>
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
  <!--故障编辑 end--> 
</div>
<div class="addTool2">
  <input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
	<c:forEach var="transition" items="${transitions}" >
		<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
</div>
<div class="blank"></div>
</body>