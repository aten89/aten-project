<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>编辑费用明细</title>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<link rel="stylesheet" type="text/css" href="commui/autocomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="page/cost/reimburse/edit_reim.js"></script>
<script type="text/javascript" src="page/cost/reimburse/Common.js"></script>

</head>
<body class="bdNone" style="padding:0px">
<div class="tabMid2">
  <!--费用审批 新增-->
  <div class="feesSp" style="min-height:340px;_height:340px">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabLine">
      <tr>
        <th colspan="6" class="addHeader">费用明细</th>
      </tr>
      <tr>
       	<td class="spTit"><div>出差日期：</div></td>
        <td width="150">
        	<input readonly id="travelBeginDate" name="traveldate" type="text" class="invokeBoth"  value=""/>
        </td>
        <td class="spTit"><div>结束日期：</div></td>
        <td>
        	<input readonly id="travelEndDate" name="traveldate" type="text" class="invokeBoth"  value=""/>
        </td>
        <td class="spTit"><div>出差地点：</div></td>
        <td><input id="travelPlace" type="text"  style='width:222px' class="ipt01"/></td>
      </tr>
      <tr>
        <td class="spTit"><div>所属区域：</div></td>
        <td><input id="regionName" type="text" class="ipt01" style="width:140px"/></td>
        <td class="spTit"><div>客户名称：</div></td>
        <!-- <td width="133"><select id='customName'  onchange='addTitleAttr(this)'  style='width:121px;height:20px'><script language="javaScript">document.write(createOptionOnlyName("m/par_khmc?act=select",null,"无"))</script></select>
        </td> -->
        <td><input id="customName" type="text" class="ipt01" style="width:140px"/></td>
        <td class="spTit"><div>同行名单：</div></td>
        <td>
        	<input name="coterielList" id='coterielList' type="text" style='width:200px' class="ipt01"/><input type="button" id="openSelect" class="selBtn" style="margin-left:6px"/>
        </td>
      </tr>
      <tr>
        <td colspan="6" class="feesList"><div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon shadowPadd">
                      <!--费用类别-->
                      <table width="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                          <th width="20%">费用类别</th>
                          <th width="20%">费用名称</th>
                          <th width="12%">单据(补贴)数</th>
                          <th width="12%">费用金额</th>
                          <th width="30%">附加说明</th>
                          <th width="6%"><img id="costsAdd" src="themes/comm/images/addFees.gif"/></th>
                        </tr>
                        <tbody id="outlayListDrafts">
	                 		
                        </tbody>
                      </table>
                      <!--费用类别 end-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
    </table>
  </div>
  <!--费用审批 end-->
  <div class="feesBlank"></div>
  <div class="addTool2">
    <input type="button" value="保存" class="allBtn" id="openSaveInfo"/>
    <input id="close" type="button" value="取消" class="allBtn"/>
  </div>
</div>
</body>
</html>