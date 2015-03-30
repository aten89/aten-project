<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程数据库管理</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/system/flowdata/del_histdata.js"></script>
</head>
	<body class="bdNone">
		<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_data'/>" />
		<div class="blank" style="height: 60px"></div>
		<div class="cxbsBg" style="height: 210px">
			流程数据库删除包括以下几个表：<span style="color: red;">删除截止时间之前数据，可能要花很长时间</span><Br />
			1、流程轨迹表<span style="color: red;">(将全部删除)</span><Br />2、流程上下文变量表<Br />
			3、集合操作者表<Br />4、集合操作角色表<Br />
			5、任务实例表<Br />6、执行令牌表<Br />7、流程实例表
		</div>

		<div style="margin:0 auto; width:450px;">
		<table border="0" cellpadding="0"  cellspacing="10">
			<tr >
				<td>截止时间：</td>
				<td>
					<input id="startDate" readonly="readonly" name="traveldate" type="text" class="invokeBoth soTimeW" value=""/>
					<input style="margin-left: 10px;" id="delData" name="delData" type="button" value="删除" class="allBtn" />
				</td>
				<td>
					<div id="importantShow" class="infoTipBg2" style="float:left; width: 150px; display: none" ></div>
				</td>
			</tr>
		</table>
		</div>
		<div id="show" style="display:none; position: absolute; z-index: 9999999; background-image: url(themes/comm/images/loadLine.gif); background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-top-style: solid; border-right-style: solid; border-bottom-style: solid; border-left-style: solid; border-top-color: rgb(238, 238, 238); border-right-color: rgb(238, 238, 238); border-bottom-color: rgb(238, 238, 238); border-left-color: rgb(238, 238, 238); border-image: initial; padding-top: 20px; padding-right: 0px; padding-bottom: 0px; padding-left: 20px; width: 390px; height: 48px; left: 500px; top: 240px; background-position: initial initial; background-repeat: initial initial; "><img src="themes/comm/images/loading.gif" align="absmiddle" />&nbsp;<font style="font-size:20px;color:#f30">正在执行,请稍后……</font></div>
	</body>
</html>