<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="page/DataManage/ImportAllocate/CustomerImport.js"></script>	
</head>

<body class="bdNone">
	<input type="hidden" id="code" value="${msg.code}"/>
	<input type="hidden" id="msg" value="${msg.text}"/>
	<input type="hidden" id="customerManage" value="${customerManage}"/>
	<div style="margin: 0pt auto; width: 680px;" class="addCon">
		<table width="100%" cellspacing="1" cellpadding="0" border="0" align="center">
			<tbody>
			<tr>
				<th>客户资料导入</th>
			</tr>
			<tr>
				<td style="padding: 13px 10px 0pt 16px; height: 240px; vertical-align: top;">
					<input type="hidden" value=",upload," id="hidModuleRights"/>
    				    <div class="costsImport" style="padding:5px 5px 10px 5px;">
    				      <b>•</b> 只支持Excel文件导入，必须按特定的数据格式，请使用提供的模板。<br/>
    				      <b>•</b> 导入前请确认设置正确无误。
    				      </div>
					    <img src="themes/comm/images/downloadn02.gif"/><a style="text-decoration: underline;" target="_blank" href="page/template/ImportCustomer.xls"><b>导入模板下载</b></a><br/><br/>
					    <form method="post" enctype="multipart/form-data" action="m/importCustomer/uploadCustomer?allocateFlag=${param.allocateFlag}" onsubmit="return submitFrm();">
					    	
					    	客户资料导入：<input type="file" style="width: 260px; height: 20px;" class="ipt01" id="mportFile" name="importFile"/>
					    	<input type="submit" value="提交" class="allBtn"/>
					    </form>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<div style="display:none"> 
	<div id="downloadDiv">
	    <div class="downloadDiv" >
		<div class='dialogTit'><a herf='#' onclick='$(this).parent().parent().parent().parent().remove();'>[关闭]</a></div>
	    <div style="white-space:nowrap;padding:15px 0 0">重复资料已经生成EXCEL文件，请下载</div>
	    <div class="txtDown" ><a id="down_a" target="_blank" ><img src="themes/comm/images/downloadn01.gif" /></a></div>
		</div>
	</div> 
</div> 
  </body>
</html>