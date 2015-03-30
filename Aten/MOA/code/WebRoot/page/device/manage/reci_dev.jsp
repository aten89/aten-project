<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>  
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备领用单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/manage/reci_dev.js"></script>
</head>
<body class="oppBd">
<input type="hidden" id="deviceTypeCode" value="${applyForm.deviceType }" />
<input type="hidden" id="areaCode" value="${param.areaCode }" />
<input type="hidden" id="deviceClassCode" value="${applyForm.deviceClass }" />
<input type="hidden" id="deviceID" value="${param.deviceID }" />
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />

<div class="oppConScroll"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备领用单</th>
      </tr>
      <tr>
      	<td class="tit">资产类别</td>
        <td class="field">${applyForm.deviceTypeName }</td>
      	<td class="tit">所属地区</td>
        <td class="field" colspan="3">${applyForm.areaName }</td>
      </tr>
      <tr>
      	<td class="tit">登记人</td>
        <td class="field">${applyForm.regAccountDisplayName }</td>
        <td class="tit">登记日期</td>
        <td class="field" colspan="3"><fmt:formatDate value="${applyForm.regTime }" pattern="yyyy-MM-dd"/></td>
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
        <td class="tit"><span class="cRed">*</span>预计使用日期</td>
        <td class="field"><input type="text" readonly="readonly" class="invokeBoth" id="planUseDate" /></td>
      </tr>
      <tr>
        <td class="tit">领用说明</td>
        <td width="600" colspan="5"><textarea id="remark" class="area01"></textarea></td>
      </tr>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6" class="tipShow"><span class="crmSubTabsBk"><a class="gmcpAdd linkOver" id="btnSelectDevice" style="left:800px;width:68px"><img src="themes/comm/images/customBtn.gif" />选择设备</a>
          <ul class="crmSubTabs">
            <li class="current default">
              <div class="lastNone">设备清单</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <div id="tab00">
                        <div class="ywjgList">
                          <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          	<thead>
	                          	<tr>
	                              <th width="10%">设备编号</th>
	                              <th width="20%">设备名称</th>
	                              <th width="10%">设备型号</th>
	                              <th width="20%">设备类别</th>
	                              <th width="10%">购买日期</th>
	                              <th width="20%">工作所在地</th>
	                              <th width="10%">操作</th>
	                            </tr>
                          	</thead>
                          	<tbody id="deviceChooseResultBody">
                          		<c:forEach items="${applyForm.devPurchaseLists  }" var="devPurchaseList">
                          		<tr id="${devPurchaseList.device.id }">
                          			<td><span name="listDeviceNO">${devPurchaseList.device.deviceNO }</span>
                          				<input type="hidden" name="listAreaName" value="${devPurchaseList.device.areaName }"/>
                          				<input type="hidden" name="listAreaCode" value="${devPurchaseList.device.areaCode }"/></td>
                          			<td>${devPurchaseList.device.deviceName }</td>
                          			<td>${devPurchaseList.device.deviceModel }</td>
                          			<td><span name="listDeviceClassName">${devPurchaseList.device.deviceClass.name }</span><input type="hidden" name="listDeviceClassID" value="${devPurchaseList.device.deviceClass.id }"/></td>
                          			<td><fmt:formatDate value="${devPurchaseList.device.buyTime }" pattern="yyyy-MM-dd"/></td>
                          			<td>
                          				<select name='areaSel' style='width:60px;height:20px'><script language="javaScript"> document.write (createAreaOption("m/data_dict?act=ereasel", "", "", "${devPurchaseList.areaCode }")) </script></select>
                          			</td>
                          			<td><a class="linkOver" name="delLink" onclick="delItem(this)">删除</a><input id="buyTime_${devPurchaseList.device.id }" type="hidden" value="<fmt:formatDate value="${devPurchaseList.device.buyTime }" pattern="yyyy-MM-dd"/>"/></td>
                          		</tr>
                          		</c:forEach>
                          	</tbody>
                          </table>
                        </div>
                      </div>
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
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" onclick="saveInfo()"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" onclick="doclose()"/>
  </div>
</div>
</body>
</html>