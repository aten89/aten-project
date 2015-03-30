<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="commui/attachment/style/attachment.css" rel="stylesheet" type="text/css"></link>
<link rel="STYLESHEET" type="text/css"  href="commui/jwysiwyg/jquery.wysiwyg.css"/>
<link rel="STYLESHEET" type="text/css" href="commui/autocomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="commui/autocomplete/jquery.bgiframe.min.js"></script>
<script type="text/javascript" src="commui/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="commui/jwysiwyg/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>

<script type="text/javascript" src="page/knowledge/kb/draf_kb.js"></script>
<title></title>
</head>
<body class="bdNone">
<input type="hidden" value="${knowledge.id}" id="id"/>
<input type="hidden" value="${knowledge.status}" id="status"/>
<input type="hidden" value="${knowledge.knowledgeClass.id }" id="knowClassId"/>
<input type="hidden" value="${knowledge.firstType}" id="firstTypeForShow"/>
<input type="hidden" value="${knowledge.secondType}" id="secondTypeForShow"/>

<div class="tabMid" id="txbd">
    <div class="addCon">
      <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
          <tr>
            <th colspan="6">知识</th>
          </tr>
          <tr>
            <td class="tit">发布人</td>
            <td>${knowledge.publisherName}</td>
            <td class="tit" >发布部门</td>
            <td width="25%">${knowledge.groupName}</td>
            <td class="tit">发布时间</td>
            <td><fmt:formatDate value="${knowledge.publishDate }" pattern="yyyy-MM-dd HH:mm"/></td>
          </tr>
          <tr>
          	<td class="tit">知识类别</td>
            <td colspan="3" id="knowClassShow">${knowledge.knowledgeClass.classFullName}</td>
            <td class="tit" >最后修改时间</td>
            <td><fmt:formatDate value="${knowledge.modifyDate }" pattern="yyyy-MM-dd HH:mm"/> </td>
          </tr>
          <tr>
            <td class="tit"><span class="cRed">*</span>标题</td>
            <td  colspan="5"><input type="text" id="subject" value="${knowledge.subject }" class="ipt01" maxlength="100"/></td>
          </tr>
          <tr>
            <td class="tit">关键字</td>
            <td colspan="5">
            	<input type="text" id="label" value="${knowledge.labels }"  class="ipt01" maxlength="80"/>  
            </td>
          </tr>
          <tr>
            <td class="tit">概述</td>
            <td colspan="5">
                <textarea id="remark" class="area01"  maxlength="4000">${knowledge.remark } </textarea>
            </td>
          </tr>
          <tr id="forCustomerService1">        
            <td class="tit"><span class="cRed"></span>一级分类</td>
            <td>
	           	<div id="firstTypeSel" name="firstType"></div>
            </td>
            <td class="tit"><span class="cRed"></span>二级分类</td>
            <td colspan="3">
            	<div id="secondTypeSel"  name="secondType"></div>
            </td>          
          </tr>

     </table>
    </div>
    <div id="content" style="margin:0 auto; text-align:center;">
        <div id="contentVal" style="display:none;">${knowledge.content}</div>
        <textarea rows="20" id="wysiwyg" style="width:100%"></textarea>
    </div>
    <div id="NTKO_AttachmentCtrl" style="height:200px; width:100%; margin:0 auto"></div>
    <div class="addTool2">
        <input id="addKnow" class="allBtn" type="button" value="提交" name="addKnow"/>
    </div>    
</div>
</body>
</html>
