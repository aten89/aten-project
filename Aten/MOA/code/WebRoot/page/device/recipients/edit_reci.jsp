<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备领用申购单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/recipients/edit_reci.js"></script>

</head>
<body class="oppBd">
<input type="hidden" id="id" value="${applyForm.id }"/> 
<input type="hidden" id="applyType" value="${applyForm.applyType }"/>
<input type="hidden" id="buyType" value="${applyForm.buyType }"/>
<input type="hidden" id="areaCode" value="${applyForm.areaCode }"/>
<input type="hidden" id="deviceTypeCode" value="${applyForm.deviceType }"/>
<c:choose>
	<c:when test="${applyForm.applyType=='0' }">
	<input type="hidden" id="deviceClass" value="${applyForm.deviceClass }"/>
	</c:when>
	<c:otherwise>
	<input type="hidden" id="deviceClass" value="${applyForm.purchaseDeviceClass.id }"/>
	</c:otherwise>
</c:choose>
<input type="hidden" id="purposes" value="${applyForm.purposes }"/>
<input type="hidden" id="areaCodePurpose" value="${applyForm.areaCodePurpose}"/>
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />
<div class="oppConScroll" style="min-height:400px"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备领用申购单</th>
      </tr>
      <tr>
        <td class="tit">申请人</td>
        <td class="field">${applyForm.applicantDisplayName }<input type="hidden" id="applicant" value="${applyForm.applicant }"/></td>
        <td class="tit">申请部门</td>
        <td class="field"><span id="applyGroupName">${applyForm.applyGroupName }</span></td>
        <td class="tit">申请日期</td>
        <td class="field"><span id="nowDate"><fmt:formatDate value="${applyForm.applyDate }" pattern="yyyy-MM-dd"/></span></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>申请类型</td>
        <td colspan="5"><div style="float:left;width:130px;">
            <input name="formType" type="radio" value="0" id="radDevUse" onclick="displayOrhide('radDevUse')" class="cBox" checked="checked" />
            <label for="radDevUse">领用</label>
            &nbsp;
            <input name="formType" type="radio" value="1" id="radDevPurchase" onclick="displayOrhide('radDevPurchase')" class="cBox"/>
            <label for="radDevPurchase">申购</label>
          </div>
          </td>
      </tr>
      <tr>
      <td class="tit"><span class="cRed">*</span>所属区域</td>
        <td class="field"><div style="width:139px">
            <div id="belongtoAreaSel" name="belongtoAreaSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td class="field"><div style="width:139px">
            <div id="deviceTypeSel" name="deviceTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>预计使用日期</td>
        <td><input type="text" id="planUseDate" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" value="<fmt:formatDate value="${applyForm.planUseDate }" pattern="yyyy-MM-dd"/>"/></td>
      	
      </tr>
      <tr id="areaDeviceTR" style="display:none">
        <td class="tit"><span class="cRed">*</span>设备类别</td>
        <td class="field"><div style="width:139px">
            <div id="deviceClassSel" name="deviceClassSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>购买方式</td>
        <td class="field"><div style="float:left; width:139px;">
                <div id="buyTypeSel" name="buyTypeSel"></div>
              </div></td>
       <td class="tit"><span class="cRed">*</span>预算金额</td>
        <td class="field"><input type="text" class="ipt05" style="width:100px" id="budgetMoney" maxlength="12" value="<fmt:formatNumber value="${applyForm.budgetMoney }" pattern="0.00"/>"/>元</td>
      </tr>
      <tr id="devCfgDescTR" style="display:none">
	     
        <td class="tit"><span class="cRed">*</span>配置要求</td>
        <td colspan="5"><textarea id="devCfgDesc" class="area01">${applyForm.devCfgDesc }</textarea></td>
      <tr>
        <td class="tit"><span class="cRed" id="red" style="display: none">*</span><span id="descriptTitle">领用说明</span></td>
        <td colspan="5"> 
        <textarea class="area01" id="remark">${applyForm.remark }</textarea>
        <ul id="msg" class="allInfotip aiLeft;" style="width:350px;clear:none;display: none">
            <li>请您详细填写要申购设备的信息（如品牌、型号、显卡等）</li>
          </ul>
        </td>
      </tr>
  </table>
  </div>
  <!--设备清单-->
  <div id="devListBody">
      <div class="blank" style="height:3px"></div>
      <table border="0" cellpadding="0"  cellspacing="0" width="100%">
      	<tr>
      	  <td>
      	  	<div class="costsListHead">
	          <div style="width:100%"><label style="float:left">设备清单</label><a id="gmcpAdd" onclick="equipmentChoose()"   style=" float:right;font-weight:normal; cursor:pointer"><img src="themes/comm/images/customBtn.gif" />选择设备</a></div>
	        </div>
      	  </td>
      	</tr>
      </table>
      
      <c:if test="${applyForm.applyType == 0 }">
   		<c:forEach items="${applyForm.devPurchaseLists  }" var="devPurchaseList">
    		<c:if test="${not empty devPurchaseList.device}">
	      <div class="feesSp fsMar">
	        <table cellspacing="0" cellpadding="0" border="0" width="100%" >
	          <tr>
		        <td class="spNum"><span name="orderNum">${devPurchaseList.displayOrder }</span></td>
		        <td colspan="6" class="spOpW opw2">
		            <div>
		                <a class="linkOver" name="delLink" onclick="delItem(this)" /><img src="themes/comm/images/spDel.gif" />删除</a>
		            </div>
		        </td>
		      </tr>
	          <tr>
	            <td width="70" class="spTit">设备编号：</td>
	            <td width="128"><span name="deviceNO">${devPurchaseList.device.deviceNO }</span></td>
	            <td width="70" height="27" class="spTit">设备名称：</td>
	            <td width="100">${devPurchaseList.device.deviceName }<span name="deviceID" style="display: none">${devPurchaseList.device.id}</span>
	            	<input type="hidden" name="listAreaName" value="${devPurchaseList.device.areaName }"/>
                    <input type="hidden" name="listAreaCode" value="${devPurchaseList.device.areaCode }"/>
	            </td>
	           <td width="70" class="spTit">设备型号：</td>
	            <td width="290">${devPurchaseList.device.deviceModel }</td>
	            <td rowspan="4">&nbsp;</td>
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
	            <td width="290" colspan="3"><select name='areaSel' style='width:60px;height:20px'>
	            	<script language="javaScript"> document.write (createAreaOption("m/data_dict?act=ereasel", "", "", "${devPurchaseList.areaCode }")) </script></select>
	            </td>
	          </tr>
	          <tr>
		        <td colspan="7" style="padding:0; height:10px">&nbsp;</td>
		      </tr>
	        </table>
	        <div class="blank" style="height:5px"></div>
	      </div>
	      
	</c:if>
		</c:forEach>
	</c:if>
	  </div>
  <!--设备清单 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="提 交" class="allBtn"/>
    <input id="draftBtn" type="button" value="保存为草稿" class="allBtn"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn"/>
  </div>
</div>
</body>

