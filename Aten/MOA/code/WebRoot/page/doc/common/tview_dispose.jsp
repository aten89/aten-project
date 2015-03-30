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
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="commui/office/oa.office.js"></script>
<script type="text/javascript" src="page/doc/common/tview_dispose.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='nor_deal'/>"/>
<input type="hidden" value="${docForm.id}" id="docFormId"/>
<input type="hidden" value="${docForm.copyDocFormId}" id="copyDocFormId"/>
<input type="hidden" value="${docForm.bodyDraftDocUrl}" id="draftDocUrl"/>
<input type="hidden" value="${sessionUser.displayName}" id="userId"/>
<!-- input type="hidden" value="${args}" id="args"/>
<input type="hidden" value="${param.type}" id="disposeType"/-->
<input type="hidden" value="${message}" id="message"/>
<!--起草文件-->
<div class="tabMid2" id="txbd" >
<div class="addCon">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
      <tr>
	    <th colspan="6">${ docForm.docClassName}</th>
	  </tr>
      <tr>
        <td class="tit">起草人</td>
        <td width="150">${docForm.draftsmanName }</td>
        <td class="tit" >起草部门</td>
        <td width="150" id="dept">${docForm.groupName}</td>
        <td class="tit">起草时间</td>
        <td><fmt:formatDate value="${docForm.draftDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">文件标题</td>
        <td colspan="5">${docForm.subject }</td>
      </tr>
      <tr>
      	<td class="tit">内&nbsp;&nbsp;&nbsp;&nbsp;容</td>
		<td colspan="5">
		<a href="javascript:void(0)" id="fullScrean" style="font-weight:bold;text-decoration:underline">全屏查看</a>
		</td>
	  </tr>
      <tr>
	    <td colspan="6" id="NTKO_OfficeCtrl" style="height:800px"></td>
	  </tr>
	  <tr>
	    <td colspan="6" id="NTKO_AttachmentCtrl" style="height:200px"></td>
	  </tr>
 </table>
</div>
<div class="blank"></div>
<div class="addCon">
	<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	      <tr>
		    <th colspan="2">处理  <c:if test="${not empty pt.executionTime}"> -- 该文件被要求于<fmt:formatDate value="${pt.executionTime }" pattern="yyyy-MM-dd"/>之前完成操作！</c:if> </th>
		  </tr>
	      <tr>
	        <td class="tit"><div style="width:120px">处理意见</div></td>
	        <td width="700"><textarea id="comment" class="area01" style="width:632px;" name="comment"></textarea>
	        	<input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
	        </td>
	      </tr>
	      <tr>
	        <td  class="tit">操&nbsp;&nbsp;&nbsp;&nbsp;作</td>
	        <td>
	         	<input type="hidden" id="transitionName"/>
	            <span id="commitBut">
					<c:forEach var="transition" items="${transitions}" >
						<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
					</c:forEach>
				</span>
				<!-- <input id="tempSave" class="allBtn" type="button" value="临时保存"/>&nbsp;&nbsp; -->
			 </td>
	     </tr>
	     <tr>
        	<td colspan="2" class="tipList">&nbsp;</td>
     	 </tr>
	     <tr>
			<td colspan="2" class="tipShow">
				<span class="crmSubTabsBk">
	         		 <ul class="crmSubTabs" id="crmTab">
	           			 <li class="current">
	             		 <div class="lastNone">各步骤处理结果</div>
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
                      <c:forEach var="task" items="${tasks}" >
                      	<div class="processBk">
                          <div class="clgcTit">
                            <ul class="clearfix">
                              <li><b>处理人：</b>${task.transactorDisplayName }</li>
                              <li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                              <li><b>处理状态：</b>
                            	 <c:if test="${task.taskState=='ps_create'}">
                              		<img src="themes/comm/images/stateIcoNone.gif">[未查看]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_start'}">
                              		<img src="themes/comm/images/stateIco02.gif">[已查看,未处理]
                              	 </c:if>
                              	 <c:if test="${task.taskState=='ps_end'}">
                              		<img src="themes/comm/images/stateIco01.gif">[已处理]
                              	</c:if>
                              </li>
                            </ul>
                          </div>
                          <div class="taskCon nonebb">
                            <div class="handleCon"> ${task.comment } </div>
                          </div>
                        </div>
                      </c:forEach>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </td>
	</tr>
	</table>
</div>	  

<div class="blank"></div>
<!-- 各级审批意见 -->
</body>
</html>
