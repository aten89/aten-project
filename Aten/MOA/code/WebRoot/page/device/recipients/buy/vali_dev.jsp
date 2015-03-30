<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>设备验收单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/recipients/buy/vali_dev.js"></script>
<style>
html {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<input id="applicant" type="hidden" value="${userId }"/>
<div class="oppConScroll" style="height:358px">
  <div class="addCon">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
      <tr>
        <th class="tipBg" colspan="4"><img src="themes/comm/images/frameNav2.gif"/>设备验收单</th>
      </tr>
      <tr>
        <td class="tit">检验人</td>
        <td width="388">${userName }</td>
        <td class="tit">检验日期</td>
        <td><span id="valiDate"><fmt:formatDate value="${validate}" pattern="yyyy-MM-dd"/></span></td>
      </tr>
      <tr>
        <td class="tit">检查项</td>
        <td colspan="3" style="padding-top:6px;padding-bottom:6px;"><div class='meetingSb'>
            <table width="628" border="0"  cellpadding="0"  cellspacing="1">
              <thead>
                <tr>
                  <th width="119">名称</th>
                  <th width="80">是否合格</th>
                  <th>备注</th>
                </tr>
              </thead>
              <tbody id="valiTab">
              <c:forEach var="list" items="${deviceCheckItems}">
                <tr class="valilist">
                  <td>${list.itemName}</td>
                  <td><input type="checkbox" class="cBox"/></td>
                  <td><input type='text'  class='ipt05' style="width:386px"/></td>
                </tr>
                </c:forEach>
              </tbody>
            </table>
          </div></td>
      </tr>
      <tr>
        <td class="tit">备注</td>
        <td colspan="3" style=""><textarea id="remark"  name="remark" class="area01 awIT" id=""></textarea></td>
      </tr>
    </table>
  </div>
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" onclick="saveInfo()"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" onclick="doclose()"/>
  </div>
</div>
</body>
