<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript"	src="page/doc/dispatch/dialog_header.js"></script>
<title>套红头</title>
</head>
<body class="bdDia">
	<div class="addCon">
	  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	      <tr>
		    <th colspan="6">编号</th>
		  </tr>
		  <tr>
			  <td class="tit" width="20%">
			  	文件字
			  </td>
			  <td width="30%">
			    <select id="docNum" name="docNum" class="sel02" style="width:110px">
					<option value="">请选择...</option>
					<c:forEach var="docNum" items="${docNums}" >
				        <option value="${docNum.id }" >${docNum.docWord }</option>
					</c:forEach>
	    		</select>
			  </td>
			  <td  class="tit"  width="10%">
			  	年份
			  </td>	
			  <td width="15%">
			  	<input type="text" id="year" class="ipt01" maxlength="4" disabled style="width:35px"/>
			  </td>
			  <td  class="tit" width="10%">
			  	流水号
			  </td>
			   <td width="15%">
			   <input type="text" id="num" maxlength="8" class="ipt01" disabled style="width:35px"/>
			  </td>
		  
		  </tr>
	  </table>
	</div>
	<div class="addCon">
		<table  width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
			<tr>
				<th colspan="6">红头预览</th>
			</tr>
			<tr style="height:30px">
				<td colspan="6" id="docNumPre"></td>
			</tr>
		</table>
	</div>
	<div class="addTool2">
		<input type="button" value="确定" class="allBtn" id="submit"/>
		<input type="button" value="取消" class="allBtn" id="cancel"/>
	</div> 
</body>