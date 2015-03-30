<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/knowledge/log/query_log.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
</head>
<body class="bdNone">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><!--搜索-->
      <div class="soso">
        <div class="t01">
          <table border="0" cellpadding="0" cellspacing="0" >
            <tr>
              <td>操作者：<input id="draftsman" type="hidden"/><input id="draftsmanName" readonly type="text" class="ipt01" style="width:65px"/>
            	<input type="button" id="openUserSelect" class="selBtn" />
				知识点标题：<input id="knowledgetitle" type="text" class="ipt01"  style="width:130px"/>
              	操作时间：
			  	从 <input id="beginOperatetime" type="text" maxlength="100" readonly class="invokeBoth" value=""  style="width:65px" /> 到 <input id="endOperatetime" type="text" maxlength="100" readonly class="invokeBoth" style="width:65px" />  
			    &nbsp;<input id="lowSearch" name="lowSearch" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;<input id="flashBtn" type="button" class="flash_btn" />
			  </td>
            </tr>
          </table>
        </div>
      </div>
      <!--搜索 end--></td>
  </tr>
  <tr>
    <td><!--列表-->
      <div class="allList">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <thead>
            <tr>
              <th width="13%">姓名</th>
              <th width="10%">操作类型</th>
              <th width="17%">操作时间</th>
              <th width="60%">知识点标题</th>
            </tr>
          </thead>
          <tbody id="logList">
          </tbody>
        </table>
      </div>
      <!--翻页-->
      <div class="pageNext"></div>
      <input id="hidNumPage" type="hidden" value="1"/>
	  <input id="hidPageCount" type="hidden" value="15"/>
      <!--翻页 end--></td>
  </tr>
</table>
</body>
</html>
