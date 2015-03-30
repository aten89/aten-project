<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
	<jsp:include page="../page/base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Language" content="en" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<script type="text/javascript" src="temp/cust_upload.js"></script>	
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
    				      <b>•</b> 只支持Excel文件上传，必须按特定的数据格式，请使用提供的模板。<br/>
    				      <b>•</b> 上传前请确认设置正确无误。
    				    </div>
					    <br/>
					    <form method="post" enctype="multipart/form-data" action="m/importCustomer/uploadCustomerTemp" onsubmit="return submitFrm();">
					    	
					    	客户资料上传：<input type="file" style="width: 260px; height: 20px;" class="ipt01" id="mportFile" name="importFile"/>
					    	<input type="submit" value="提交" class="allBtn"/>
					    </form>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>