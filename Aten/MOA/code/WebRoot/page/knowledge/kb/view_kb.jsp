<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="commui/attachment/style/attachment.css" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="page/knowledge/kb/view_kb.js"></script>
<title></title>

</head>
<body class="bdNone">
<input type="hidden" value="${knowledge.id}" id="id"/>
<input type="hidden" value="${userAccount}" id="userAccount"/>

<div class="blank" style="height:8px"></div>
<div class="tabMid" id="txbd">
    <div class="addCon">
     <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
          <tr>
            <th colspan="6">知识</th>
          </tr>
          <tr>
            <td class="tit">发布人</td>
            <td width="20%">${knowledge.publisherName}</td>
            <td class="tit" >发布部门</td>
            <td width="25%">${knowledge.groupName}</td>
            <td class="tit">发布时间</td>
            <td><fmt:formatDate value="${knowledge.publishDate }" pattern="yyyy-MM-dd HH:mm"/></td>
          </tr>
          <tr>
            <td class="tit">一级分类</td>
            <td>${knowledge.firstTypeName}</td>
            <td class="tit">二级分类</td>
            <td>${knowledge.secondTypeName}</td>
            <td class="tit" >最后修改时间</td>
            <td><fmt:formatDate value="${knowledge.modifyDate }" pattern="yyyy-MM-dd HH:mm"/> </td>
          </tr>
          <tr>
          	<td class="tit">知识类别</td>
            <td colspan="5" id="knowClassShow">${knowledge.knowledgeClass.classFullName}</td>
          </tr>
          <tr>
            <td class="tit">标题</td>
            <td  colspan="5">${knowledge.subject}
          </tr>
          <tr>
            <td class="tit">关键字</td>
            <td colspan="5">${knowledge.labels}</td>
          </tr>
          <tr>
            <td class="tit">概述</td>
            <td colspan="5">${knowledge.remark}</td>
          </tr>
          <tr>
          	  <td colspan="6" style="text-align:left" class="tit"><b>知识内容</b></td>
          </tr>
          <tr>
	          <td colspan="6" style="text-align:left">
			     <div>${knowledge.content}</div>
	          </td>
          </tr>
          <!-- 判断是否客服中心并进行页面初始化end -->
     </table>
	 <div id="NTKO_AttachmentCtrl" style="height:160px;padding:8px 0 18px"></div>
	 <!-- 评论 -->
	 <div style="border:1px solid #eee" id="commentCon" >
	  <div class="klComm" id="addReplyDiv" style="border:none">
		<div class="portletLeft" style="width:100%;border:none;border-bottom:1px solid #eee">
		  <h1 class="" style="padding-left:10px">
        	<table border="0" cellpadding="0"  cellspacing="0">
        		<tr>
        			<td colspan="2">评论</td>
        		</tr>
        	</table>
		  </h1>
	  	  <div class="portPadd" style="height:auto; width:auto;padding-bottom:10px;padding-left:10px">
			<textarea name="" id="reply" class="area01" style="width:98%; height:80px"></textarea><br/>
			<input type="button" value="提交评论" class="allBtn" id="addReply"/>
	  	  </div>
		</div>
	  </div>
	  <div style="padding:0 0 5px 10px; background:#fff; font-weight:bold"><img src="themes/comm/images/commentResult_ico.gif" style=" vertical-align:middle"/>&nbsp;目前有<span id="klNum"></span>个评论</div>
	  <div class="klBk" id="replyLst"></div>
	  <div class="pageNext" style="background:#fff;padding-right:8px; text-align:right"></div>
	  <input id="hidNumPage" type="hidden" value="1"/>
	  <input id="hidPageCount" type="hidden" value="5"/>
	</div>  
	<!-- end评论 -->
</div>  
</div>
<!--知识库详情 end-->
</body>
</html>
