<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="commui/autocoomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="commui/autocoomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="commui/autocoomplete/jquery.bgiframe.min.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/operationsManage/refuncApplication/refuncApplicationFrame.js"></script>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body class="bdNone">
<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='REFUNC_REGISTER'/><eapp:menu key='REFUNC_APPLICATION'/>" />
<div class="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td width="131" valign="top"><div class="crmSubNav crmSubLevel" id="crmSubNav">
              <h1>退款申请</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="refuncNotice" url="page/operationsManage/refuncApplication/refuncNoticeList.jsp" class="subFirst"><input type="button" class="draft_icon"/>退款须知</li>
                  <li id="refuncApply1" url="page/operationsManage/refuncApplication/refuncApplicationToDoList.jsp" class="subFirst">
                    <input type="button" class="draft_icon"/>退款申请
                  </li>
               <!--   <li id="refuncApply2" url="page/operationsManage/refuncApplication/refuncApplicationDraftList.jsp" class="submb">
                    <div><img src="themes/comm/images/draft_icon.gif"/>起草</div>
                  </li> --> 
                  <li id="refuncApply3" url="page/operationsManage/refuncApplication/refuncApplicationToDoList.jsp" class="submb">
                    <div><input type="button" class="dbgd_icon"/>待办</div>
                  </li>
                  <li id="refuncApply4" url="page/operationsManage/refuncApplication/refuncApplicationTrackList.jsp" class="submb">
                    <div><input type="button" class="track_icon"/>跟踪</div>
                  </li>
                  <li id="refuncApply5" url="page/operationsManage/refuncApplication/refuncApplicationArchList.jsp" class="subLast">
                    <div><input type="button" class="archived_icon"/>归档</div>
                  </li>
                </ul> 
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td class="crmFrameBg" width="100%" valign="top"><div class="crmFrameHead flatHide"><input type="button" class="frameNav2" /><span id="crmFrameHead"></span></div>
            <div class="blank2 flatHide"></div>
            <div id="costsCon" class="costsCon"> </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
