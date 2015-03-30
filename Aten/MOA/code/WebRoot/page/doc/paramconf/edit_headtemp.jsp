<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="commui/office/oa.office.js"></script>
<script type="text/javascript"	src="page/doc/paramconf/edit_headtemp.js"></script>
<title></title>
</head>
<body class="bdNone">
<div class="tabMid">
<input type="hidden" value="${docNumber.id}" id="docNumberId"/>
<input type="hidden" value="${docNumber.headTemplateUrl }" id="headTemplateUrl"/>
<!--编辑红头模板-->
<div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  <thead>
	<tr>
		<th colspan="2">信息详情</th>
	</tr>
  </thead>
  <tr>
    <td class="tit">公文编号</td>
    <td>${docNumber.description }</td>
  </tr>
  <tr >
    <td class="tit">可用标签</td>
    <td>[发文字号] <a href="javascript:void(0);" onclick="insertBookmark('[发文字号]','docNumber');">添加</a></td>
  </tr>
  <tr>
    <td colspan="2" id="NTKO_OfficeCtrl" style="height:800px"></td>
  </tr>
</table>
</div>
<div class="addTool2">
  	<input type="button" value="保存" class="allBtn" id="saveTemplate"/>
  	<input type="button" value="取消" class="allBtn" id="cancelTemplate"/>
</div>
</div>
<!--编辑红头模板 end-->
</body>
</html>