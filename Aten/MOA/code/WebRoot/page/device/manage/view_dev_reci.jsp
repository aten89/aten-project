<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/EquipmentManagement/EquipmentInformation/EquipmentInfoUseView.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<title>新增设备</title>
</head>
<body class="bdNone">
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备领用申购单</th>
      </tr>
      <tr>
        <td class="tit">申请单编号</td>
        <td colspan="5" class="field">${form.fullFormNO }</td>
      </tr>
      <tr>
        <td class="tit">申请人</td>
        <td class="field">${form.applicantDisplayName }</td>
        <td class="tit">申请部门</td>
        <td class="field">${form.applyGroupName }</td>
        <td class="tit">申请日期</td>
        <td class="field"><fmt:formatDate value="${form.applyDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <c:if test="${form.applyType == 1 }">
      	<tr>
	        <td class="tit">购买方式</td>
	        <td class="field">${form.buyTypeDisplayName }</td>
	        <td class="tit">预算金额</td>
	        <td class="field" colspan="3">${form.budgetMoney }</td>
	    </tr>
      </c:if>
      <tr>
        <td class="tit">预计使用日期</td>
        <td colspan="5"><fmt:formatDate value="${form.planUseDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <c:if test="${form.applyType == 1 }">
      	<tr>
	        <td class="tit">配置要求</td>
	        <td colspan="5">${form.devCfgDesc }</td>
      	</tr>
	      <tr>
	        <td class="tit">申购说明</td>
	        <td colspan="5">${form.remark }</td>
	      </tr>
      </c:if>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs" id="crmTab">
            <li  class="current"  style="cursor:default;">
              <div class="lastNone">设备清单</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow"  >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon"> 
                      <!--使用历史记录-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th width="60">设备编号</th>
                            <th width="60">设备类别</th>
                            <th width="100">设备名称</th>
                            <th width="60">设备型号</th>
                            <th width="">配置信息</th>
                            <th width="68">购买日期</th>
                            <th width="68">归还日期</th>
                          </tr>
                          <c:forEach items="${form.devPurchaseLists }" var="list">
                          <tr>
                            <td>${list.device.deviceNO }</td>
                            <td>${list.device.deviceClass.name }</td>
                            <td>${list.device.deviceName }</td>
                            <td>${list.device.deviceModel }</td>
                            <td>${list.device.configList }</td>
                            <td><fmt:formatDate value="${list.device.buyTime }" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${list.returnBackDate }" pattern="yyyy-MM-dd"/></td>
                          </tr>
                          </c:forEach>
                        </table>
                      </div>
                      <!--使用历史记录--> 
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="blank"></div>
</body>
