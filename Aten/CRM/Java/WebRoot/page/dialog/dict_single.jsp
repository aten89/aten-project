<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>数据字典选择框（单选）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="${ermpPath}jqueryui/tree/jquery.simple.tree.js"></script>
    <script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
	<script language="javascript">
		var args = parent.window.dialogArguments1;
		var selectedDict = null;
		
		
		function init(){
			if (args.subSystemId != null && args.subSystemId != ""){
				$("#subSystemPanel").hide();
				subSystemList_OnChange(args.subSystemId);
			}else{
				initSubSystemList();
			}
		}

		//初始化子系统列表
		function initSubSystemList(){
			var url = args.ermpPath + "m/subsystem/allsystem";
			if (args.ermpPath != args.basePath){
				//跨域
				url +="?jsoncallback=?";
			}
			$.getJSON(url,function(data){
				$("#subSystemList").html(data.htmlValue);
				$("#subSystemList").ySelect({onChange: subSystemList_OnChange});
			});
		}
		
		//系统列表发生选择事情时的回调函数
		function subSystemList_OnChange(value,text){
			var url = args.ermpPath + "m/datadict/expandsubsystem?subSystemID=" + value;
			if (args.ermpPath != args.basePath){
				//跨域
				url +="&jsoncallback=?";
			}
			$.getJSON(url,function(data){
				$("#dictList").html(data.htmlValue);
				$("#dictList").simpleTree({
		        	animate: true,
	                basePath : args.ermpPath,
	                json:true,
		        	afterClick : function(o){
		        		var id = $(o).attr("id");
	                    var name = $(o).find("span:first").text();
	                    selectedDict = {id:id,name:name};
		        	}
		        });
			});
		}
		
		function selectDict(){
			if (selectedDict == null){
				$.alert("请选择一个数据字典！");
				return false;
			}
			args.selected.push(selectedDict);
			parent.window.returnValue1 = true;
			args.closeDialog();
		}
		
		function cancel(){
			parent.window.returnValue1 = false;
			args.closeDialog();
		}
		
		function clearDict(){
			args.selected.push({id:"", name:""});
			parent.window.returnValue1 = true;
			args.closeDialog();
		}
	</script>
</head>
<body class="bdDia" onload="init();">
<div class="dialogBk" style=" width:341px;">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top">
			  <div class="mkdzTip"  id="subSystemPanel">
			  	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
			  	  <tr>
					<td>子系统：</td>
					<td>
					<div id="subSystemList" type="single" width="180" overflow="true" name="subSystem"></div>
					</td>
			 	  </tr>
			 	</table>
			  </div>
			  <div class="mkdz">
				<ul id="dictList" class="simpleTree" style="height:273px; width:335px">
					<li class="root"><span>请选择子系统</span>
						<ul></ul>
					</li>
				</ul>
			  </div>
			</td>
		</tr>
	</table>
</div>
<div class="addTool2">
        <input type="button" value="确 定" onclick="selectDict();" class="allBtn"/>
        <input type="button" value="清 除" onclick="clearDict();" class="allBtn"/>
        <input type="button" value="取 消" onclick="cancel();" class="allBtn"/>
</div>
</body>
</html>
