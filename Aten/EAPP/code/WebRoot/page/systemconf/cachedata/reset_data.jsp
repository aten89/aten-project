<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Language" content="en" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>重新部署</title>
		<script type="text/javascript" src="page/systemconf/cachedata/reset_data.js"></script>
	</head>
	<body class="bdNone">
		<div class="blank" style="height: 60px"></div>
		<div class="cxbsBg">
			热加载企业应用公共平台数据字典,主要包括：<br/>
			(1) 快捷方式模块的打开方式<br/>
			(2) 机构管理模块的机构类型<br/>
			(3) 首页地址的配置<br/>
		</div>
		<div class="cxbsBk">
			<input id="reloaddatadict" type="button" value="重新加载" class="allBtn" r-action="redeploy"/>
		</div>
		
	</body>
</html>