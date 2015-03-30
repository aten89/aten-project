<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>新增设备</title>
</head>
<body class="bdNone">
<input type="hidden" id="deviceID" value="${device.id }" />
<div class="tabMid">
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>基本信息</th>
      </tr>
      <tr>
        <td class="tit">所属地区 </td>
        <td class="field">${device.areaName }</td>
        <td class="tit">登记人</td>
        <td class="field">${device.regAccountName }</td>
        <td class="tit">登记日期</td>
        <td width="230"><fmt:formatDate value="${device.regTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">设备编号</td>
        <td class="field">${device.deviceNO }</td>
        <td class="tit">设备名称</td>
        <td class="field">${device.deviceName }</td>
        <td class="tit">设备型号</td>
        <td class="field">${device.deviceModel }</td>
      </tr>
      <tr>
        <td class="tit">设备类别</td>
        <td class="field">${device.deviceClass.name }</td>
        <td class="tit">设备状态</td>
        <td class="field" colspan="3"><span class="jjC05">${device.deviceCurStatusInfo.deviceCurStatusStr }</span></td>
      </tr>
      <tr>
        <td class="tit">购买方式</td>
        <td class="field">${device.buyTypeName}</td>
        <td class="tit">购买日期</td>
        <td class="field"><fmt:formatDate value="${device.buyTime }" pattern="yyyy-MM-dd"/></td>
        <td class="tit">购买金额</td>
        <td class="field"><fmt:formatNumber value="${device.price }" pattern="0.00"/>元</td>
      </tr>
      <c:if test="${device.buyType=='BUY-TYPE-SUB'}">
      <tr>
        <td class="tit">是否扣款</td>
        <td class="field">${device.deductFlag?'是':'否' }</td>
        <td class="tit">扣款金额</td>
        <td class="field"><fmt:formatNumber value="${device.deductMoney }" pattern="0.00"/>元</td>
        <td class="tit">到账日期</td>
        <td class="field"><fmt:formatDate value="${device.inDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      </c:if>
      <c:if test="${!empty device.devicePropertyDetails}">
      <tr>
        <td class="tit">配置信息</td>
        <td colspan="5">
          <!--配置信息-->
          <div class="ywjgList">
            <table width="398" border="0" cellspacing="1" cellpadding="0">
              <tr>
                <th width="110">配置项</th>
                <th width="">信息</th>
              </tr>
              <c:forEach var="checkItem" items="${device.devicePropertyDetails}"> 
              	<tr>
                <td>${checkItem.propertyName}</td>
                <td>${checkItem.propertyValue}</td>
              	</tr>
              </c:forEach>
            </table>
          </div>
          <!--配置信息--> 
        </td>
      </tr>
      </c:if>
      <tr>
        <td class="tit">设备描述</td>
        <td width="600" colspan="5">${device.description}</td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
</body>
