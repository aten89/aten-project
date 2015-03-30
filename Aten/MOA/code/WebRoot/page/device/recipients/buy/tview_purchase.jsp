<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/recipients/buy/tview_purchase.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<title>设备领用申购单</title>
</head>
<body class="bdNone">
<input id="deviceType" type="hidden" value="${form.deviceType}"/>
<c:choose>
	<c:when test="${form.applyType=='0' }">
	<input type="hidden" id="deviceClass" value="${form.deviceClass }"/>
	</c:when>
	<c:otherwise>
	<input type="hidden" id="deviceClass" value="${form.purchaseDeviceClass.id }"/>
	</c:otherwise>
</c:choose>
<input id="totPrice" type="hidden" value="${form.budgetMoney }"/>
<input id="budgetMoney" type="hidden" value="${form.budgetMoney }"/>
<input id="areaCode" type="hidden" value="${form.areaCode }"/>
<input id="id" type="hidden" value="${form.id}"/>
<input id="useCount" type="hidden" value="${form.useCount}"/>
<input type="hidden" id="purposes" value="${form.purposes }"/>
<input type="hidden" id="applicant" value="${form.applicant }"/>
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备${form.applyType=='0'?'领用':'申购' }单</th>
      </tr>
      <tr>
        <td class="tit">申请单编号</td>
        <td class="field">${form.fullFormNO}</td>
        <td class="tit">申请类型</td>
        <td colspan="3">${form.applyTypeName }</td>
        
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
     	<td class="tit">所属地区</td>
        <td class="field"><span>${form.areaName}</span></td>
        <td class="tit">资产类别</td>
        <td class="field"><span id="deviceTypeName">${form.deviceTypeName}</span></td>
        <td class="tit">预计使用日期</td>
        <td class="field"><fmt:formatDate value="${form.planUseDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">设备类别</td>
        <td class="field"><span id="deviceClassName">${form.purchaseDeviceClass.name }</span></td>
        <td class="tit">购买方式</td>
        <td class="field">${form.buyTypeName }</td>
        <td class="tit">预算金额</td>
        <td class="field"><fmt:formatNumber value="${form.budgetMoney }" pattern="0.00"/>元</td>
      </tr>
      <tr>
        <td class="tit">配置要求</td>
        <td colspan="5">${form.devCfgDesc }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="cf60" onclick="deviceStock()">【查看库存设备】</a></td>
      </tr>
      <tr>
        <td class="tit">申购说明</td>
        <td colspan="5">${form.remark }</td>
      </tr>
      <c:if test="${!empty form.purchaseDevPurposes}">
      <tr>
         <td colspan="6">
         <c:forEach var="list"  items="${form.purchaseDevPurposes}">
          <div class="newRemark" style="width:96%"><b>该员工名下已有<b class="jjC01">${list.useCount}</b>台${form.deviceClassDisplayName}作为“${list.purposeName}”<c:if test="${list.useCount>0}">,其中：<c:if test="${list.useCount>0}"><c:if test="${list.useFuzhouCount>0}">福州${list.useFuzhouCount}台；</c:if><c:if test="${list.useShangHaiCount>0}">上海${list.useShangHaiCount}台；</c:if><c:if test="${list.useXiamenCount>0}">厦门${list.useXiamenCount}台</c:if></c:if></c:if></b><br/>
          </div>
          </c:forEach>
         </td>
      </tr>
       </c:if>
    </table>
  </div>
  <!--设备清单-->
  <div id="devListBody">
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div style="width:100%"><label  style="float:left">设备清单</label><a id="gmcpAdd2" onclick="equipmentAdd()"  style=" float:right;font-weight:normal; cursor:pointer"><img src="themes/comm/images/customBtn.gif" />添加设备信息</a></div>
  </div>
  <c:forEach var="devList" items="${form.purchaseDevices }">
  <c:if test="${ !empty devList.deviceName}">
  <div class="feesSp fsMar">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
       <tr>
        <td class="spNum"><span name="orderNum">${devPurchaseList.displayOrder }</span></td>
        <td colspan="6" class="spOpW opw2">
            <div>
                <a href="javascript:void(0)" onclick="equipmentEdit(this)"><img src="themes/comm/images/spEdit.gif" />修改</a><a href="javascript:void(0)" onclick="delItem(this,'${devList.price}')"><img src="themes/comm/images/spDel.gif" />删除</a>
            </div>
        </td>
      </tr>
      <tr>
        <td width="100" height="27" class="spTit">设备名称：</td>
        <td width="128"><span name="areaCodePurpose" style="display: none">${devList.areaCode}</span><span name="main_flag" style="display: none">${devList.deviceClassMainFlag}</span><span name="deviceName">${devList.deviceName}</span><span name="purpose" style="display: none">${devList.purpose}</span><span name="purchaseId" style="display: none">${devList.id}</span></td>
        <td width="100" class="spTit">设备类别：</td>
        <td width="100"><span name="deviceClassName">${devList.deviceClass.name}</span><span name="deviceClass" style="display: none">${devList.deviceClass.id}</span></td>
        <td width="70" class="spTit">设备型号：</td>
        <td width="290"><span name="deviceModel">${devList.deviceModel}</span></td>
        <td rowspan="4"  class="spOpW">&nbsp;</td>
      </tr>
      <c:if test="${!empty devList.devicePropertyDetails}">
      <tr>
        <td width="70" class="spTit">配置信息：</td>
        <td colspan="5" width="500"><span name="optionItem">${devList.configList}</span><span name="optionLists" style="display: none">${devList.configListStr}</span></td>
      </tr>
      </c:if>
      <tr>
        <td width="70" height="27" class="spTit">购买金额：</td>
        <td width="128"><span name="price"><fmt:formatNumber value="${devList.price}" pattern="0.00"/></span>元</td>
        <td width="70" class="spTit">购买日期：</td>
        <td width=""><span name="buyTime"><fmt:formatDate value="${devList.buyTime }" pattern="yyyy-MM-dd"/></span></td>
      <td width="70" class="spTit">验收状态：</td>
        <td width="290"><span name="flag"><c:if test="${devList.devValidateForm.accountID==null }"><div class="jjC03">未验收</div></c:if><c:if test="${devList.devValidateForm.accountID !=null }"><span class="jjC04">待验收</span>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="cf60" onclick="viewValiForm('${devList.devValidateForm.id}')">【查看验收单】</a><span name="valiFormId" style="display: none">${devList.devValidateForm.id}</span></c:if></span></td>
      </tr>
      <tr>
        <td width="70" class="spTit">设备用途：</td>
        <td><span name="purposeName">${devList.purposeDisplayName}</span></td>
        <td width="70" class="spTit">工作所在地：</td>
        <td colspan="3"><span name="areaCodePurposeName">${devList.areaName}</span></td>
      </tr>
      <tr>
        <td width="70" class="spTit">设备描述：</td>
        <td colspan="5" width="500"><span name="description">${devList.description}</span></td>
      </tr>
    </table>
    <div class="blank" style="height:5px"></div>
  </div>
  </c:if>
  </c:forEach>
  <!--设备清单 end-->
  </div>
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
                       <!--审批意见-->
                      <div class="clyjBk clearfix bd0">
                        <div class="clyj  padd0">
                          <div class="mb">
                            <div class="con01">
                              <div class="yjSubBk">
                                <textarea id="comment" class="area01 spArea"></textarea>
                              </div>
                              <div class="clear"></div>
                            </div>
                          </div>
                          <div class="sqBk">
                            <ul>
                              <li>【设备${form.applyType=='0'?'领用':'申购' }单】审批意见请在左侧填写提交。</li>
                            </ul>
                          </div>
                        </div>
                      </div>
                      <!--审批意见 end--> 
                      <!--查看审批意见 begin-->
                      <div class="clyj htspyj"  style="padding-top:0">
                        <div class="tit01"  style="cursor:pointer" onclick="apprCommSwitch('htcpOpen','ckspyj')"><img src="themes/comm/images/htcpOpen.gif" id="htcpOpen"/> 查看审批意见</div>
                      </div>
                      <div id="ckspyj">
                      	<c:forEach var="task" items="${tasks}" >
						<div class="processBk">
                          <div class="clgcTit">
                            <ul class="clearfix">
                              <li><b>处理人：</b>${task.transactorDisplayName }</li>
                              <li><b>到达时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>完成时间：</b><fmt:formatDate value="${task.endTime }" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>步骤：</b>${task.nodeName }</li>
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
<div class="addTool2">
  <input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
	<c:forEach var="transition" items="${transitions}" >
		<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
</div>
<div class="blank"></div>
</body>
