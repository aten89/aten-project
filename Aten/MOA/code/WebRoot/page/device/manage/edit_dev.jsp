<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>新增设备</title>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/manage/edit_dev.js"></script>

</head>
<body class="oppBd">
  <input id="deviceClassOld" type="hidden" value="${device.deviceClass.id}"/>
  <input id="deviceClassId" type="hidden" value="${device.deviceClass.id}"/>
  <input id="deviceId" type="hidden" value="${device.id}"/>
  <input id="deviceTypeCode" type="hidden" value="${device.deviceType }"/>
  <input id="status" type="hidden" value="${device.deviceCurStatusInfo.deviceCurStatus }"/>
  <input id="buyTypeValue"  type="hidden" value="${device.buyType }"/>
  <input id="deviceNO"  type="hidden" value="${device.deviceNO }"/>
  <input id="orderLength"  type="hidden" value="${orderLength }"/>
  <input id="areaCode"  type="hidden" value="${device.areaCode }"/>
  <input id="valiFormFlag"  type="hidden" value="${valiFormFlag }"/>
  <input id="deductFlag"  type="hidden" value="${device.deductFlag }"/>
  <c:if test="${!empty device.devicePropertyDetails}">
    <input id="propertyDetails"  type="hidden" value="1"/>
  </c:if>
  <c:if test="${empty device.devicePropertyDetails}">
    <input id="propertyDetails"  type="hidden" value="0"/>
  </c:if>
  
  
<div class="oppConScroll"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>基本信息</th>
      </tr>
      <tr>
        <td class="tit">所属地区</td>
        <td class="field">${device.areaName }</td>
        <td class="tit">登记人</td>
        <td class="field">${device.regAccountName }</td>
        <td class="tit">登记日期</td>
        <td class="field"><fmt:formatDate value="${device.regTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
       <td class="tit">资产类别</td>
        <td class="field">${device.deviceTypeName }</td>
        <td class="tit"><span class="cRed">*</span>设备类别</td>
        <td class="field"><div style="width:139px">
            <div id="deviceClassDiv" name="deviceClass">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>设备状态</td>
        <td class="field"><div style="width:139px">
            <div id="sbzt" name="satus">
              <div isselected="true">1**正常</div>
            </div>
          </div></td>
      </tr>
      <tr>
        <td class="tit">设备编号</td>
        <td class="field"><span id="deviceNOSpan">${deviceNO }</span>&nbsp;&nbsp;&nbsp;<span class="cRed">*</span>流水号<input id="sepNum" style="width:45px" maxlength="10" class="ipt05" type="text" value="${sepNum }"/></td>
        <td class="tit"><span class="cRed">*</span>设备名称</td>
        <td class="field"><input id="deviceName" type="text" maxlength="50" class="ipt05" value="${device.deviceName }"/></td>
        <td class="tit"><span class="cRed">*</span>设备型号</td>
        <td class="field"><input id="deviceModel" type="text" maxlength="50" class="ipt05" value="${device.deviceModel }" /></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>购买方式</td>
        <td class="field"><div style="width:139px">
            <div id="buyTypeSel" name="buyType"></div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>购买日期</td>
        <td class="field" colspan="3"><input id="buyTime" type="text" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${device.buyTime }" pattern="yyyy-MM-dd"/>"/>
        	</td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>购买金额</td>
        <td class="field"><input id="price" type="text" maxlength="50" class="ipt05" style="width:99px" value="<fmt:formatNumber value="${device.price }" pattern="0.00"/>"/>
          元</td>
        <td class="tit">财务原值</td>
        <td class="field" colspan="3"><input id="financeOriginalVal" type="text" maxlength="12" class="ipt05 iptSo" 
        		value="<fmt:formatNumber value="${device.financeOriginalVal }" pattern="0.00"/>"/>元</td>
      </tr>
      
      <tr id="sfkk" style="display:none">
        <td class="tit">是否扣款</td>
        <td width="558"  colspan="5"><input name="sfjk" type="radio" value="1" id="yk" onclick="displayOrhide('yk')" class="cBox"/>
          <label for="yk">是</label>
          &nbsp;
          <input name="sfjk" type="radio" value="0" id="wk" onclick="displayOrhide('wk')" class="cBox" />
          <label for="wk">否</label>
          <span id="je">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="cRed">*</span>扣款金额：
          <input id="deductMoney" type="text" maxlength="50" class="ipt05 iptSo" value="<fmt:formatNumber value="${device.deductMoney }" pattern="0.00"/>"/>
          元
          &nbsp;&nbsp;&nbsp;到账日期：
          <input type="text" id="inDate" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${device.inDate }" pattern="yyyy-MM-dd"/>"/>
          </span></td>
      </tr>
      <tr id="configInfo" style="display:none">
        <td class="tit"><span class="cRed">*</span>配置信息</td>
        <td colspan="5"><!--配置信息-->
          
          <div class="ywjgList">
            <table width="433" border="0" cellspacing="1" cellpadding="0" >
              <tr>
                <th width="110">配置项</th>
                <th width="269">信息</th>
              </tr>
              <tbody id="optionTab">
              <c:forEach var="checkItem" items="${device.devicePropertyDetails}"> 
              	<tr>
                <td>${checkItem.propertyName}</td>
                <td><input id="sbmc5" type="text" maxlength="50" class="ipt05" style="width:215px" value="${checkItem.propertyValue}"></input></td>
              	</tr>
              </c:forEach>
              </tbody>
              
              
            </table>
          </div>
          
          <!--配置信息--></td>
      </tr>
      <tr>
        <td class="tit">设备描述</td>
        <td width="600" colspan="5"><textarea id="description" class="area01">${device.description }</textarea></td>
      </tr>
      <tbody id="ysdtd">
        <tr>
          <td colspan="6"  class="tit" style="padding-left:9px;padding-top:2px;text-align:left;width:auto;height:26px"><a class="linkOver" id="addValidate"  style="float:right;width:79px;padding-right:20px"><img src="themes/comm/images/customBtn.gif" />添加验收单</a></td>
        </tr>
      </tbody>
     
      <tbody id="ysd" style="display:none">
        <tr>
          <td colspan="6" class="tipList">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="6"  class="tipShow"><span class="crmSubTabsBk"><a class=" gmcpAdd linkOver" id="deleteValidate" style="left:688px;width:88px"><img src="themes/comm/images/minus.gif" /> 删除验收单</a>
            <ul class="crmSubTabs" id="crmTab">
              <li  class="current"  style="cursor:default;">
                <div class="lastNone">设备验收单</div>  
              </li>
            </ul>
            </span>
            <div class="boxShadow"  >
              <div class="shadow01">
                <div class="shadow02" >
                  <div class="shadow03" >
                    <div class="shadow04" >
                      <div class="shadowCon"> 
                        <!--项目列表-->
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
                                <td class="spTitn"><span class="cRed">*</span>检验人</td>
                                <td><input id="applicantName" type="text" maxlength="50" class="ipt05"  style="width:99px" readonly=true value="${device.devValidateForm.accountName}"/><input id="applicant" type="hidden" value="${device.devValidateForm.accountID}"/>
                                  <input type="button" id="openUserSelect" class="selBtn"></td>
                                <td class="spTitn"><span class="cRed">*</span>检验日期</td>
                                <td><input id="valiDate" type="text" maxlength="100" readonly="readonly" class="invokeBoth"  value="<fmt:formatDate value="${device.devValidateForm.valiDate }" pattern="yyyy-MM-dd"/>"/></td>
                              </tr>
                              <tr>
                                <td class="spTitn">检查项</td>
                                <td colspan="3" style="padding-top:6px;padding-bottom:6px;"><div class='meetingSb'>
                                    <table width="628" border="0"  cellpadding="0"  cellspacing="1">
                                      <thead>
                                        <tr>
                                          <th width="159">名称</th>
                                          <th width="110">是否合格</th>
                                          <th width="258">备注</th>
                                          <th width="80" style="padding:0;font-weight:normal; cursor:pointer; text-align:left"><div style="width:80px"><a class="linkOver" id="addValidateBnt"  style=" float:right;width:79px"><img src="themes/comm/images/customBtn.gif" />添加</a></div></th>
                                        </tr>
                                       </thead>
                                       <tbody id="valiTab">
                                        <c:forEach var="valieDetail" items="${device.devValidateForm.deviceValiDetails}"> 
							              	<tr>
							                <td>${valieDetail.item}</td>
							                
							                <td>${valieDetail.isEligibility?'是':'否'}<input type="hidden" name="itemRemark" id="is${valieDetail.item}" value="${valieDetail.isEligibility}"/></td>
							                <td>${valieDetail.remark}</td>
							                <td><a href="#" class="opLink" onclick="deleteValOption(this);return false;">删除</a></td>
							              	</tr>
							           </c:forEach>
                                      </tbody>
                                      
                                    </table>
                                  </div></td>
                              </tr>
                              <tr>
                                <td class="spTitn">备注</td>
                                <td colspan="3" style=""><textarea  name="remark" class="area01 awIT" id="remark">${device.devValidateForm.remark}</textarea></td>
                              </tr>
                            </table>
                          </div>
                        </div>
                        <!--项目列表 end--> 
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div></td>
        </tr>
      </tbody>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" />
    <input id="closeBtn" type="button" value="关 闭" class="allBtn"/>
  </div>
</div>
</body>
