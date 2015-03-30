<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备报废单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/manage/scrap_dev.js"></script>
</head>
<body class="oppBd">
<input type="hidden" id="deviceTypeCode" value="${applyForm.deviceType }" />
<input type="hidden" id="deviceClassCode" value="${applyForm.deviceClass }" />
<input type="hidden" id="deviceID" value="${param.deviceID }" />
<input type="hidden" id="areaCode" value="${param.areaCode }" />
<input type="hidden" id="owner" value="${applyForm.discardDevice.device.deviceCurStatusInfo.owner }" />
<input type="hidden" id="buyType" value="${applyForm.discardDevice.device.buyType }" />
<div class="oppConScroll"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备报废单</th>
      </tr>
      <tr>
      	<td class="tit">资产类别</td>
      	<td class="field">${applyForm.deviceTypeName }
      	
       	</td>
       <td class="tit">登记人</td>
        <td class="field">${applyForm.regAccountDisplayName }</td>
        <td class="tit">登记日期</td>
        <td class="field"><fmt:formatDate value="${applyForm.regTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备名称</td>
        <td>
			<input id="deviceName" type="text" class="ipt05" style="width: 99px" readonly="true" value="${applyForm.discardDevice.device.deviceName }"/> 
        	<input id="deviceID" type="hidden"  class="ipt05" value="${applyForm.discardDevice.device.id }"/>
        	<input id="btnSelectDevice" type="button" class="selBtn" />
		</td>
		<td class="tit">设备编号</td>
        <td class="field"><span id="deviceNO">${applyForm.discardDevice.device.deviceNO }</span></td>
		<td class="tit">设备型号</td>
		<td class="field"><span id="deviceModel">${applyForm.discardDevice.device.deviceModel }</span></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>购买方式</td>
        <td class="field"><span id="buyTypeStr">${applyForm.discardDevice.device.buyTypeName }</span></td>
        <td class="tit"><span class="cRed">*</span>余值</td>
        <td><input type="text" id="remaining" class="ipt05" style="width:95px" maxlength="12" value="<fmt:formatNumber value="${list.remaining }" pattern="0.00"/>"/> 元</td>
        <td class="tit"><span class="cRed">*</span>折旧价格</td>
        <td class="field"><input type="text" id="depreciation" class="ipt05"  style="width:95px" maxlength="12" value="<fmt:formatNumber value="${list.depreciation }" pattern="0.00"/>"/> 元
        </td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>报废类型</td>
        <td class="field"><div style="width:139px">
            <div id="scrapTypeSel" name="scrapTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>处理方式</td>
        <td><div style="width:139px">
            <div id="scrapDisposeTypeSel" name="scrapDisposeTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>报废日期</td>
        <td class="field"><input id="discardDate" type="text" maxlength="100" readonly="readonly" class="invokeBoth"/></td>
      </tr>
      <tr id="trBackBuy" style="display:none;">
        <td class="tit"><span class="cRed">*</span>入司时间</td>
        <td class="field"><input id="enterCompanyDate" style="float:left" type="text" maxlength="100" readonly="readonly" class="invokeBoth" /></td>
        <td class="tit"><span class="cRed">*</span>员工工龄</td>
        <td><input id="workYear" type="text" class="ipt01" value="" style="width:95px" maxlength="6"/> 年</td>
        <td class="tit"><span class="cRed">*</span>回购价格</td>
        <td class="field"><input type="text" id="buyPrice" class="ipt05"  style="width:95px" maxlength="12" value="<fmt:formatNumber value="${list.buyPrice }" pattern="0.00"/>"/> 元</td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>申请人</td>
        <td class="field">
        	<input id="applicantName" type="text" maxlength="50" class="ipt05" value="${applyForm.applicantDisplayName }" style="width:99px" readonly />
   			<input id="applicantID" type="hidden" value="${applyForm.applicant }" ></input>
   			<input id="openUserSelect" class="selBtn" type="button"/>
        </td>
        <td class="tit">所在部门</td>
        <td class="field"><span id="applyGroupName">${applyForm.applyGroupName }</span></td>
        <td class="tit"><span class="cRed">*</span>申请日期</td>
        <td class="field"><input type="text" id="applyDate" maxlength="100" readonly="readonly" class="invokeBoth" value=""/></td>
      </tr>
      <tr>
        <td class="tit">报废原因</td>
        <td width="600" colspan="5"><textarea id="reason" class="area01"></textarea></td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" onclick="saveInfo()"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" onclick="doclose()"/>
  </div>
</div>
</body>
