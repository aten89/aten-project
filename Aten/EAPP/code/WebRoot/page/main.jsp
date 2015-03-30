<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业应用公共平台</title>
<style>
html{height:100%}
</style>
<script type="text/javascript" src="page/main.js"></script>
</head>
<body class="bdNone">
	<input id="showFlag" type="hidden" value="${param.showFlag }"/>
	<!--固定首页-->
	<div id="portletPanelDiv" style="width:100%">
		<!--  div class="divLayout" style="width:34%;height:238px">
			<div class="blankH">
				<div class="divPanel">
	    			 <h1 class="divTitle01"><div><span>业务排行榜</span><button class="biClose">&nbsp;</button></div></h1>
					 <div class="mb">
						<iframe src="page/tempBox.jsp" marginheight="0" marginwidth="0"  frameborder="0" width="100%" height="100%" scrolling="auto" allowTransparency="true"></iframe>
					 </div>
				</div>
			</div>
		</div-->
	</div>
	<!--固定首页 end-->

	<div style="clear:both;height:30px"></div>
</body>
</html>