<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/device/scrap/tview_finance.js"></script>
<title>设备调拨单</title>

</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_deal'/>"/>
<input type="hidden" id="formId" value="${form.id }"/>
<input type="hidden" id="transitionName" value=""/>
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备报废单</th>
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
        <td class="detailField"><fmt:formatDate value="${form.applyDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
       <c:choose>
      <c:when test="${param.flag == 'backbuy' || param.flag == 'backbuyappr' || !empty form.archiveDate  }">
      <tr>
      	 <td class="tit">资产类别</td>
        <td class="field">${form.deviceTypeName }</td>
        <td class="tit">入司时间</td>
        <td class="field"><fmt:formatDate value="${form.enterCompanyDate }" pattern="yyyy-MM-dd"/></td>
        <td class="tit">员工工龄</td>
        <td class="field">${form.workYear }年</td>
      </tr>
      </c:when>
      <c:otherwise>
      	<tr>
      	 <td class="tit">资产类别</td>
        <td class="field" colspan="5">${form.deviceTypeName }</td>
      </tr>
      </c:otherwise>
      </c:choose>
    </table>
  </div>
  <!--设备清单-->
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div>设备清单</div>
  </div>
  <c:forEach var="devList" items="${form.discardDevLists }">
  <div class="feesSp">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
      <tr>
        <td class="spNum"><span name="orderNum">${devList.displayOrder }</span></td>
        <td colspan="6" class="spOpW opw2">&nbsp;</td>
      </tr>
      <tr>
        <td width="70" class="spTit">设备编号：</td>
        <td width="128">${devList.device.deviceNO }</td>
        <td width="70" height="27" class="spTit">设备名称：</td>
        <td width="100"><span id="deviceName">${devList.device.deviceName }</span><span name="deviceID" style="display: none">${devList.device.id }</span><span name="listID" style="display: none">${devList.id }</span></td>
        <td width="70" class="spTit">购买方式：</td>
        <td width="390">${devList.device.buyTypeName }</td>
        <td rowspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td class="spTit">处理方式：</td>
        <td>${devList.dealTypeDisplayName }</td>
        <td class="spTit">报废原因：</td>
        <td colspan="3" width="500">${devList.reason }</td>
      </tr>
      <tr>
        <td colspan="7"  class="feesList"><div class="boxShadow" style="z-index:99">
            <div class="shadow01">
              <div class="shadow02">
                <div class="shadow03">
                  <div class="shadow04">
                    <div class="shadowCon shadowPadd">
                      <table width="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                          <th width="60" class="sbRight"><span class="cRed">*</span>报废类型</th>
                          <td width=""><select name='discardTypeSel' style='width:100px;height:20px'><script language="javaScript"> document.write (createOption("m/data_dict?act=selectscraptypexml","", "", "${list.discardType}")) </script></select></td>
                          <th width="60"  class="sbRight"><span class="cRed">*</span>余值</th>
                          <td width="120"><input type="text" name="remaining" class="ipt05" style="width:60px" value="<fmt:formatNumber value="${list.remaining }" pattern="0.00"/>"/>
                            元</td>
                          <th width="60"  class="sbRight"><span class="cRed">*</span>已提折旧</th>
                          <td width="120"><input type="text" name="depreciation" class="ipt05"  style="width:60px" value="<fmt:formatNumber value="${list.depreciation }" pattern="0.00"/>"/>
                            元</td>
                          <th width="60" class="sbRight"><span class="cRed">*</span>报废日期</th>
                          <td width="120"><input type="text" name="discardDate" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" value="<fmt:formatDate value="${list.discardDate }" pattern="yyyy-MM-dd"/>"/></td>
                        </tr>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
    </table>
    <div class="feesBlank"></div>
    <div class="blank" style="height:5px"></div>
  </div>
 </c:forEach>
  
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
          <div class="boxShadow"  style="z-index:97">
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
                              <li>【设备报废单】审批意见请在左侧填写提交。</li>
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
  	<span id="commitBut">
	<c:forEach var="transition" items="${transitions}" >
		<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
	</span>
</div>
</body>
