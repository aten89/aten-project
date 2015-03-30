<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Language" content="en" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>重新部署12312312312</title>
		<script type="text/javascript" src="page/system/cachedata/reset_data.js"></script>
	</head>
	<body class="bdNone">
		<input id="hidModuleRights" type="hidden" value="<oa:right key='datadictreload'/>" />
		<div class="blank" style="height: 60px"></div>
		<div class="cxbsBg" style="height:100px">
			热加载企业资源管理系统数据字典及业务数据缓存,主要包括：<br/>
			(1)流程配置模块的流程分类<br/>
			(2)费用管理模块的费用分类<br/>
		</div>
		<div class="cxbsBk">
			<input id="reloaddatadict" name="reloaddatadict" type="button"
				value="重新加载" class="allBtn" />
		</div>
		
	</body>
</html>