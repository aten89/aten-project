<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备离职处理单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/leavedeal/edit_leave.js"></script>
</head>
<body class="oppBd">
<input type="hidden" id="id" value="${applyForm.id }"/>
<input type="hidden" id="deviceTypeCode" value="${applyForm.deviceType }"/>
<input type="hidden" id="formType" value="${applyForm.formType }"/>
<div class="oppConScroll" style="min-height:400px"> 
  <!--设备单 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>离职处理单</th>
      </tr>
      <tr>
        <td class="tit">申请人</td>
        <td class="field">${applyForm.applicantDisplayName }<input id="inAccountID" type="hidden" value="${applyForm.applicant }" /></td>
        <td class="tit">申请部门</td>
        <td class="field">${applyForm.applyGroupName }</td>
        <td class="tit">申请日期</td>
        <td class="field"><fmt:formatDate value="${applyForm.applyDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td class="field" colspan="5"><select id='deviceTypeSel' style='width:100px;height:20px' onchange="processDeviceList(this);"><script language="javaScript">  
       			document.write (createDeviceTypeOption("m/data_dict?act=selectdevtype","", "", "${applyForm.deviceType}", ""))
       		</script>
       	</select></td>
      </tr>
    </table>
  </div>
  <!--设备清单 end-->
  <div id="devListBody">
  <div class="blank" style="height:3px"></div>
  <div class="costsListHead">
    <div>设备清单</div>
    <span><a class="linkOver" id="btnSelectDevice"><img src="themes/comm/images/customBtn.gif" />选择设备</a></span> </div>
    <c:forEach items="${applyForm.discardDevLists}" var="list">
    	<!--设备清单-->
		  <div class="feesSp fsMar">
		    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
		      <tr>
		        <td class="spNum"><span name="orderNum">${devPurchaseList.displayOrder }</span></td>
		        <td colspan="6" class="spOpW opw2">
		            <div>
		                <a class="linkOver" name="delLink" onclick="delItem(this)" /><img src="themes/comm/images/spDel.gif" />删除</a>
		            </div>
		        </td>
		      </tr>
		      <tr>
		        <td width="70" height="27" class="spTit">设备名称：</td>
		        <td width="128">${list.device.deviceName }<span name="deviceID" style="display:none;">${list.device.id }</span></td>
		        <td width="70" class="spTit">设备编号：</td>
		        <td width="100"><span name="deviceNO">${list.device.deviceNO }</span></td>
		        <td width="70" class="spTit">购买方式：</td>
		        <td width="310">${list.device.buyTypeName }</td>
		        <td rowspan="2" class="spOpW">&nbsp;</td>
		      </tr>
		      <tr>
		        <td class="spTit"><span class="cRed">*</span>处理方式：</td>
		        <td>
		        	<select name='dealTypeSel' style='width:100px;height:20px'><script language="javaScript">  
		        	<c:choose>
		        		<c:when test="${applyForm.formType=='0'}">
		        			document.write (createDealTypeOption("m/data_dict?act=selectscrapdisposetypexml","", "", "${list.dealType}", ${list.device.buyType == 'BUY-TYPE-COMP'}))
		        		</c:when>
		        		<c:when test="${applyForm.formType=='1'}">
		        			document.write (createLeaveDealTypeOption("m/data_dict?act=leavedealtypesel","", "", "${list.dealType}", "${list.device.buyType}"))
		        		</c:when>
		        	</c:choose>
		        		</script>
		        	</select>
		        </td>
		        <td class="spTit">离职原因：</td>
		        <td colspan="3" width="500"><input type="text" name="reason" class="ipt01" style="width:360px" value="${list.reason }"/></td>
		      </tr>
		      <tr>
		        <td colspan="7" style="height:6px">&nbsp;</td>
		      </tr>
		    </table>
		    <div class="blank" style="height:5px"></div>
		  </div>
		  <!--设备清单 end--> 
    </c:forEach>
  </div>
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="提 交" class="allBtn"/>
    <input id="draftBtn" type="button" value="保存为草稿" class="allBtn"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn"/>
  </div>
</div>
</body>
