<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Language" content="en" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="page/hr/salarybill/import_salary.js"></script>
  </head>
  
<body class="bdDia">
	<input type="hidden" id="code" value="${code}"/>
	<input type="hidden" id="msg" value="${msg}"/>
	<div class="addCon">
		<table cellspacing="1" cellpadding="0" border="0" align="center" width="100%">
			<tbody>
			<tr>
				<td style="height:160px;vertical-align:top;padding:5px 5px 0 16px">
   				    <div class="costsImport">
   				    	<b>•</b> 只支持Excel文件导入，必须按特定的数据格式，请使用提供的模板。<br/>
   				    	<b>•</b> 若要修改已导入的数据，更正后重新导入即可。<br/>
   				    </div>
				    <img src="themes/comm/images/downloadn02.gif" /><a href="page/template/salaryImportTemplate.xls" target="_blank" style="text-decoration:underline"><b>导入模板下载</b></a><br /><br />
					
					月份：
					<select id="yearSel" class="sel02" style="width:65px"></select>
					<select id="monthSel" class="sel02" style="width:50px">
						<option value="01">01月</option>
						<option value="02">02月</option>
						<option value="03">03月</option>
						<option value="04">04月</option>
						<option value="05">05月</option>
						<option value="06">06月</option>
						<option value="07">07月</option>
						<option value="08">08月</option>
						<option value="09">09月</option>
						<option value="10">10月</option>
						<option value="11">11月</option>
						<option value="12">12月</option>
					</select>
				  	<div class="blank" style="height:5px;"></div>
				    <form id="impForm" method="post" enctype="multipart/form-data" action="m/salary_bill?act=import">
				    	<input type="hidden" name="month" id="month"/>
				    	文件：<input type="file" name="importFile" id="mportFile"  class="ipt01" style="width:260px;height:20px"/>
				    	<input id="submitBtn" type="button" value="提交" class="allBtn"/>
				    </form>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>
