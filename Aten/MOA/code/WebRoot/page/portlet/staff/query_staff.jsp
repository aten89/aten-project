<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/portlet/staff/query_staff.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>
<body >
<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="0">
	<tr>
     	<td colspan="4">&nbsp;</td>
   	</tr>
   	<tr>
     	<td colspan="4" class="tipList">&nbsp;</td>
   	</tr>
    <tr>
	  <td colspan="4" class="tipShow">
		<span class="crmSubTabsBk">
		  <ul class="crmSubTabs" id="crmTab">
		  	<li><div>合同提醒</div></li>
		  	<li><div>转正提醒</div></li>
			<li><div class="lastNone">生日提醒</div></li>
		  </ul>
		</span>
		<div>
		  <div class="boxShadow">
			<div class="shadow01">
      		  <div class="shadow02" >
          		<div class="shadow03" >
                  <div class="shadow04" >
                 	<div class="shadow04" >
                   	  <div class="shadowCon" style="width:100%;">
                   	  
						<div id="tab00" style="display:none">
						  <div class="divPanelSub">
							<ul id="dataList0"></ul>
			          	  </div> 
						</div>
						
						<div id="tab01" style="display:none">
						  <div class="divPanelSub">
							<ul id="dataList1"></ul>
			          	  </div> 
						</div>

						<div id="tab02" style="display:none">
						  <div class="divPanelSub">
							<ul id="dataList2"></ul>
			          	  </div> 
						</div>
						
					  </div>
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
</body>
</html>