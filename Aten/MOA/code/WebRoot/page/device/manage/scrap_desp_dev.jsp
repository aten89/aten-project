<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备报废处理单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/manage/scrap_desp_dev.js"></script>

</head>
<body class="oppBd">
<input type="hidden" id="id" value="${form.id }" /> 
<input type="hidden" id="deviceID" value="${param.deviceID }" /> 
<input type="hidden" id="deviceTypeCode" value="${form.deviceType }" />
<input type="hidden" id="areaCode" value="${param.areaCode }" />

<div class="oppConScroll"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备报废处理单</th>
      </tr>
      <tr>
        <td class="tit">资产类型</td>
        <td width="180">${form.deviceTypeDisplayName }</td>
        <td class="tit">登记人</td>
        <td class="field">${form.regAccountDisplayName }<input type="hidden" id="regAccountID" value="${form.regAccountID }" /></td>
        <td class="tit">登记日期</td>
        <td class="field"><span id="regTime"><fmt:formatDate value="${form.regTime }" pattern="yyyy-MM-dd"/></span></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>变卖价格</td>
        <td class="field"><input id="salePrice" type="text" maxlength="12" class="ipt05" style="width:99px"/>
        元</td>
        <td class="tit"><span class="cRed">*</span>变卖日期</td>
        <td colspan="3" class="field"><input type="text" id="saleDate" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${form.saleDate }" pattern="yyyy-MM-dd"/>" /></td>
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
	                              <th width="15%">设备编号</th>
	                              <th width="25%">设备名称</th>
	                              <th width="20%">设备型号</th>
	                              <th width="15%">设备类别</th>
	                              <th width="10%">购买日期</th>
	                              <th width="5%">操作</th>
	                            </tr>
                          </thead>
                            <tbody id="deviceChooseResultBody">
                            	<c:forEach items="${form.discardDealDevLists  }" var="discardDealDevList">
	                          		<tr id="${discardDealDevList.device.id }">
	                          			<td>${discardDealDevList.device.deviceNO }</td>
	                          			<td>${discardDealDevList.device.deviceName }</td>
	                          			<td>${discardDealDevList.device.deviceModel }</td>
	                          			<td>${discardDealDevList.device.deviceClass.name }</td>
	                          			<td><fmt:formatDate value="${discardDealDevList.device.buyTime }" pattern="yyyy-MM-dd"/></td>
	                          			<td><a class="linkOver" name="delLink" onclick="delItem(this)">删除</a></td>
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
    <input id="saveBtn" type="button" value="保 存" class="allBtn" />
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" />
  </div>
</div>
</body>
