<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/manage/view_dev.js"></script>
<title>新增设备</title>
</head>
<body class="bdNone">
<input type="hidden" id="deviceID" value="${device.id }" />
<div class="tabMid">
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th colspan="6"><img src="themes/comm/images/frameNav2.gif"/>基本信息</th>
      </tr>
      <tr>
        <td class="tit">所属地区 </td>
        <td class="field">${device.areaName }
		</td>
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
        <td class="field" colspan="3"><fmt:formatDate value="${device.buyTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">购买金额</td>
        <td class="field"><fmt:formatNumber value="${device.price }" pattern="0.00"/>元</td>
        <td class="tit">财务原值</td>
        <td class="field" colspan="3"><fmt:formatNumber value="${device.financeOriginalVal }" pattern="0.00"/>元</td>
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
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="6"  class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs" id="crmTab" style="width:700px">
            <li class="current" id="liValidForm">
              <div>设备验收单</div>
            </li>
            <li id="liDevFlowLogList">
              <div>设备操作流程记录</div>
            </li>
            <li id="liUptLogList">
              <div class="lastNone">设备信息更新记录</div>
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
                        <!--设备验收单-->
                        <div class="spyjConBk">
                          <div class="spyjCon">
                          	<c:choose>
                          		<c:when test="${not empty device.devValidateForm }">
	                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
	                              <tr>
	                                <td class="spTitn">检验人</td>
	                                <td width="180">${device.devValidateForm.accountName}</td>
	                                <td class="spTitn">检验日期</td>
	                                <td colspan="3" width="360"><fmt:formatDate value="${device.devValidateForm.valiDate }" pattern="yyyy-MM-dd"/></td>
	                              </tr>
	                              <tr>
	                                <td class="spTitn">检查项</td>
	                                <td colspan="5" style="padding-top:6px;padding-bottom:6px;"><div class='meetingSb'>
	                                    <table width="628" border="0"  cellpadding="0"  cellspacing="1">
	                                      <thead>
	                                        <tr>
	                                          <th width="159">名称</th>
	                                          <th width="80">是否合格</th>
	                                          <th>备注</th>
	                                        </tr>
	                                       	
	                                      </thead>
	                                      <tbody id="">
	                                      	<c:forEach var="valieDetail" items="${device.devValidateForm.deviceValiDetails}"> 
								              	<tr>
								                <td>${valieDetail.item}</td>
								                
								                <td>${valieDetail.isEligibility?'是':'否'}<input type="hidden" name="itemRemark" id="is${valieDetail.item}" value="${valieDetail.isEligibility}"/></td>
								                <td>${valieDetail.remark}</td>
								              	</tr>
								           </c:forEach>
	                                      </tbody>
	                                    </table>
	                                  </div></td>
	                              </tr>
	                              <tr>
	                                <td class="spTitn">备注</td>
	                                <td colspan="5" style="">${device.devValidateForm.remark}</td>
	                              </tr>
	                            </table>
                            </c:when>
                            <c:otherwise>
                            	没有验收单!
                            </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                        <!--设备验收单 end--> 
                      </div>
                      <div id="tab01"  style="display:none"> 
                        <!--设备流程记录-->
                        <div class="ywjgList">
                          <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <thead>
                          	<tr>
                              <th width="100">操作人</th>
                              <th width="100">所属部门</th>
                              <th width="70">操作时间</th>
                              <th width="70">操作类型</th>
                              <th width="">备注</th>
                              <th width="90">操作</th>
                            </tr>
                          </thead>
                          <tbody id="tbodyDevFlowLog">
                          </tbody>
                          </table>
                        </div>
                        <!--设备流程记录--> 
                      </div>
                      <div id="tab02" style="display:none"> 
                        <!--设备信息更新记录-->
                        <div class="ywjgList">
                          <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          	<thead>
	                          	<tr>
	                              <th width="100">操作人</th>
	                              <th width="70">操作日期</th>
	                              <th width="70">操作类型</th>
	                              <th width="">操作内容</th>
	                            </tr>
                          	</thead>
                          	<tbody id="tbodyUptLog">
                          	</tbody>
                          </table>
                        </div>
                        <!--设备信息更新记录--> 
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
</body>
