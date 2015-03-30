<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/ProdInfoManage/prodFaq/editProdFaq.js"></script>

<title>编辑产品问题</title>
</head>
<body class="tabBd">
	<input type="hidden" id="id" value="${prodFaq.id }"/>
	<input type="hidden" id="prodInfoId" value="${prodFaq.prodInfo.id }"/>
	<div class="tabScroll" style="overflow-x: hidden">
		<div class="addCon" align="center">
			<table border="0" width="100%" cellpadding="0" cellspacing="1">
				<tbody id="prodFaqInfo">
					<tr>
						<td class="tit">提问人：</td>
 						<td >${prodFaq.creatorName }
 							<input type="hidden" class="ipt05" id="creator" value="${prodFaq.creator }"/>
 						</td>
 						<td class="tit">发布时间：</td>
 						<td ><fmt:formatDate value="${prodFaq.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<td class="tit"><span class="cRed">*</span>产品：</td>
 						<td colspan="3"><div id="prodInfoSel" name="prodInfoSel"></div></td>
					</tr>
					<tr>
						<td class="tit"><span class="cRed">*</span>问题概述：</td>
 						<td colspan="3"><input type="text" class="ipt01" id="title" value="${prodFaq.title }"/></td>
					</tr>
					<tr>
						<td class="tit">详细描述：</td>
 						<td colspan="3"><textarea id="content" style="height: 150px" class="area01">${prodFaq.content }</textarea></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="oppBtnBg tabBottom">
		<input class="allBtn" id="save" type="button" value="保存"/>
		<input class="allBtn" id="cancel" type="button" value="取消"/>
	</div>
</body>
</html>

