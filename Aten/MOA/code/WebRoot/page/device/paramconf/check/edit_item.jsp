<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>验收检查项</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/device/paramconf/check/edit_item.js"></script>
<style>
html {
	overflow:hidden
}
body {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<input id="deviceClassID" type="hidden" value="${deviceClass.id }" />
<input type="hidden" id="deviceTypeCode" value="${deviceClass.deviceType }" />
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_check_item'/>" />
<div class="oppConScroll" style="height:475px">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="2"><img src="themes/comm/images/frameNav2.gif"/>验收检查项</th>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td width="350"><div style="width:131px">
            <div id="deviceTypeSel" name="deviceTypeSel">
            </div>
          </div></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备类别</td>
        <td width="350"><div style="width:131px">
            <div id="deviceClassSel" name="deviceClassSel">
            </div>
          </div></td>
      </tr>
      <tr>
        <td class="tit">备注</td>
        <td><textarea id="remark" class="area01" style="width:288px">${deviceClass.deviceCheckItemRemark }</textarea></td>
      </tr>
      <tr>
        <td colspan="2" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2" class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current default" style="cursor:default">
              <div class="lastNone">检查项配置</div>
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
                        <!--设备检查项-->
                        <div  class="dzbd" style="padding:0 0 5px 0; width:360px; margin:0 auto">
                            <table border="0" cellspacing="0" cellpadding="0" >
                                <tr>
                                    <td valign="top" width="360">
                                      <div style="padding: 6px 0pt 0pt 10px; position:relative" class="mkdzTip">
                                          检查项名称：<input  type="text" class="ipt01"  style="width:100px" id="itemName" /> <input type="button" id="addItemBtn" class="allBtn" value="添加"/>
                                      </div>
                                      <div class="mkdz" style="padding:0 0 0 0;">
                                        <select class="dzbdSel01" multiple="multiple" size="12" style="width:262px;height:210px" id="childModules">
                                        	<c:forEach var="deviceCheckItem" items="${deviceClass.deviceCheckItems}" >
												<option value="${deviceCheckItem.id }">${deviceCheckItem.itemName }</option>
											</c:forEach>			
                                        </select>				
                                      </div>
                                    </td>
                                    <td>
                                       <input id="setTop" type="button" value="移到顶部" class="allBtn pxBtn"/><br />
									   <input id="setUp" type="button" value="上移" class="allBtn pxBtn" /><br />
									   <input id="setDown" type="button" value="下移" class="allBtn pxBtn" /><br />
									   <input id="setBottom" type="button" value="移到底部" class="allBtn pxBtn"/>
                                       <input id="deleteItem" type="button" value="删除" class="allBtn pxBtn" />
                                    </td>

                                </tr>
                            </table>
                        </div>
                        <!--设备检查项 end-->
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
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" />
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" />
  </div>
</div>
</body>
</html>