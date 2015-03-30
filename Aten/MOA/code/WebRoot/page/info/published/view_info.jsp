<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<link href="commui/attachment/style/attachment.css" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="page/info/published/view_info.js"></script>
<title></title>
</head>
<body class="bdNone">
<input type="hidden" value="${info.id}" id="infoFormId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    <!--公告详情-->
    <div class="BulletinDetail" style="width:auto">
      <div class="BulletinPadd">
        <h1 id="bulletinSubject"><font color="${info.subjectColor }" style="font-family:'微软雅黑'">${info.subject }</font></h1>
        <div class="sub">发布人：${info.draftsManName }&nbsp;&nbsp;&nbsp;&nbsp;部门：${info.groupName }&nbsp;&nbsp;&nbsp;&nbsp;发布时间：<fmt:formatDate value="${info.publicDate }" pattern="yyyy-MM-dd HH:mm"/></div>
        <c:if test="${info.displayMode == '0'}" >
        	<div class="mb" style="padding:0 20px">
            <iframe id="ifr" frameborder="0"   scrolling="no" marginwidth="0" marginheight="0" src="${info.contentAccUrl }" onload="Javascript:SetWinHeight(this)"></iframe>
        </div>
        </c:if>
        <c:if test="${info.displayMode == '1'}" >
        <div class="mb" style="padding:0 20px;" >
            <p>${info.content }</p>
        </div>
         </c:if>
      </div>
    </div>
    <!--公告详情 end-->
    </td>
  </tr>
  <tr>
    <td>
    <!--附件控件-->
	<div class="attachment" style="width:auto">
		<div style="padding:10px 0 0 10px;height:22px">
			<b>附件：</b>
		</div>
		<div id="NTKO_AttachmentCtrl" style="height:200px">
		</div>
		<div class="closeInfoWin">[<a href="javascript:void('0')" onclick="closeInfoWin();">关闭窗口</a>]</div>
	</div>
	<!--附件控件 end-->
    </td>
  </tr>
</table>
	<div class="blank" style="height:10px"></div>
</body>
</html>
<script language="javascript" type="text/javascript" defer="defer">
	//注意div中的 white-space: normal; 在firefox下可以执行
enWrap();
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}
</script>