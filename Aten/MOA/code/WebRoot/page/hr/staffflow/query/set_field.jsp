<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>定制字段显示</title>
<script type="text/javascript" src="page/hr/staffflow/query/set_field.js"></script>
<style>
html {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<div class="oppConScroll" style="height:318px"> 
  <!--定制字段显示 end-->
<div class="addCon">
  <table border="0" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<th class="tipBg"><img src="themes/comm/images/frameNav2.gif"/>定制字段显示</th>
    </tr>
    <tr>
      <td class="fieldSet">
      	  <span class="costsBm">【选择要显示的字段】 <a href="javascript:void(0);" onclick="selectAll(true);">全选</a> <a href="javascript:void(0);" onclick="selectAll(false);">全不选</a></span>
      	  <ul id="columnList">
          	<!--  <li><input name="" type="checkbox" value="" class="cBox" id="gsjc"/><label for="gsjc">财务隶属</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="qsdm"/><label for="qsdm">设备编号</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="gslx"/><label for="gslx">设备类别</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="sshy"/><label for="sshy">设备名称</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="nbfj"/><label for="nbfj">购买日期</label></li>
           	<li><input name="" type="checkbox" value="" class="cBox" id="pq"/><label for="pq">原值（元）</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="gsdz"/><label for="gsdz">设备状态</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="bgdh"/><label for="bgdh">余值</label></li>
          	<li><input name="" type="checkbox" value="" class="cBox" id="kfjl"/><label for="kfjl">回收余额</label></li>-->
         </ul>
         
         <br /><br />
         <div style="clear:both">
         <div class="costsBm">【每页显示的记录数】</div>
         <input type="text" id="pageNo" class="ipt01" maxlength="6" style="width:30px; margin:0 0 0 12px" value="15" /> 行</div>
      </td>
    </tr>
  </table>
</div>
  <!--故障编辑 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveFlieds" type="button" value="确 定" class="allBtn"/>
    <input id="cancelFlieds" type="button" value="取 消" class="allBtn"/>
  </div>
</div>
</body>
