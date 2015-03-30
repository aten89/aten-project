<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/ProdInfoManage/prodInfo/prodInfoFrame.js"></script>
</head>
<body class="bdNone">
<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='PROD_MAN'/>" />
<div class="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td width="131" valign="top"><div class="crmSubNav crmSubLevel" id="crmSubNav">
              <h1>产品资料</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="prod_info_detail" url="m/prod_info/view_prodInfo?id=${param.prodInfoId}">产品信息</li>
                  <li id="prod_faq" url="m/prod_faq/view_prodFaqByProdInfoId?prodInfoId=${param.prodInfoId}">产品问题</li>
                </ul> 
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td class="crmFrameBg" width="100%" valign="top"><div class="crmFrameHead flatHide"><input type="button" class="frameNav2" /><span id="crmFrameHead">产品资料</span></div>
            <div class="blank2 flatHide"></div>
            <div id="costsCon" class="costsCon"> </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
