<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/leavedeal/tview_backbuy.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<title>离职设备处理单</title>
</head>
<body class="bdNone">
<input type="hidden" id="id" value="${form.id }" />
<div class="tabMid"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>离职设备处理单</th>
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
      <tr>
        <td class="tit">资产类别</td>
        <td colspan="5" class="field">${form.deviceTypeName }</td>
      </tr>
      <tr>
        <td class="tit">入司时间</td>
        <td class="field"><fmt:formatDate value="${form.enterCompanyDate }" pattern="yyyy-MM-dd"/></td>
        <td class="tit">员工工龄</td>
        <td class="field" colspan="3">${form.workYear }年</td>
      </tr>
    </table>
  </div>
  <!--设备清单-->
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div>设备清单</div>
  </div>
  <c:forEach var="list" items="${form.discardDevLists}" >
  <div class="feesSp fsMar">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
      <tr>
        <td class="spNum"><span name="orderNum">${devList.displayOrder }</span></td>
        <td colspan="6" class="spOpW opw2">&nbsp;</td>
      </tr>
      <tr>
        <td width="70" height="27" class="spTit">设备名称：</td>
        <td width="128"><span name="deviceName">${list.device.deviceName }</span><span name="deviceID" style="display: none">${list.device.id }</span><span name="listID" style="display: none">${list.id }</span></td>
        <td width="70" class="spTit">设备编号：</td>
        <td width="100"><span name="deviceNO">${list.device.deviceNO }</span></td>
        <td width="70" class="spTit">购买方式：</td>
        <td width="350">${list.device.buyTypeName }</td>
      </tr>
      <tr>
        <td class="spTit">处理方式：</td>
        <td>${list.dealTypeDisplayName }</td>
        <td class="spTit">离职原因：</td>
        <td colspan="3">${list.reason }</td>
      </tr>
      <c:if test="${list.dealType == 'LEAVE_DISPOSE_BACKBUY'}">
      <tr>
        <td colspan="7" style="padding:0 5px"><ul class="allInfotip atBk">
            <li>请选择是否按以下的价格回购该设备</li>
          </ul></td>
      </tr>
      </c:if>
      <tr>
        <td colspan="7"  class="feesList"><div class="boxShadow">
            <div class="shadow01">
              <div class="shadow02">
                <div class="shadow03">
                  <div class="shadow04">
                    <div class="shadowCon shadowPadd">
                      <table width="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                          <th  class="sbRight" width="15%">设备余值</th>
                          <td width="20%"><fmt:formatNumber value="${list.remaining }" pattern="0.00"/>元</td>
                          <th width="15%" class="sbRight">已提折旧</th>
                          <td width="50%"><fmt:formatNumber value="${list.depreciation }" pattern="0.00"/>元</td>
                        </tr>
                        <!-- 回购确认页面，只显示出处理方式是离职回购的设备，拿走和退库在该页面不显示 -->
  						<c:if test="${list.dealType == 'LEAVE_DISPOSE_BACKBUY'}">
                        <tr>
                          <th class="sbRight">回购价格</th>
                          <td><fmt:formatNumber value="${list.buyPrice }" pattern="0.00"/>元</td>
                          <th  class="sbRight">不回购价格</th>
                          <td><fmt:formatNumber value="${list.noBuyPrice }" pattern="0.00"/>元</td>
                        </tr>
                        <tr>
                          <th class="sbRight">预期付款日期</th>
                          <td><fmt:formatDate value="${list.planPayDate }" pattern="yyyy-MM-dd"/></td>
                          <th class="sbRight">是否回购</th>
                          <td><input name="backBuyFlag${list.id }" type="radio" value="1" id="yes${list.id }"  class="cBox"<c:if test="${list.buyFlag }"> checked="checked"</c:if> />
                            <label for="yes${list.id }">是</label>
                            &nbsp;
                            <input name="backBuyFlag${list.id }" type="radio" value="0" id="no${list.id }" class="cBox"<c:if test="${!list.buyFlag }"> checked="checked"</c:if>/>
                            <label for="no${list.id }">否</label></td>
                        </tr>
                        </c:if>
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
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon"> 
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
  <input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
  <input type="hidden" id="transitionName" value=""/>
	<c:forEach var="transition" items="${transitions}" >
		<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
</div>
<div class="blank"></div>
</body>
