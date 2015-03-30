<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>流程选择框</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script language="javascript">
		var args = window.dialogArguments;
		window.returnValue = false;
		var selectedFlow = null;
		
		function init(){
			//加载流程列表
			initFlowList(args.flowClass);
		}

		//初始化流程选择列表
		function initFlowList(flowClass){
			var url = "m/flow_man?act=get_flow_select&flowClass=" + flowClass;
			if (args.oaPath != args.basePath){
				//跨域
				url = args.oaPath + url + "&format=json&jsoncallback=?";
			}else{
				//非跨域
				url = args.oaPath + url + "&format=json";
			}
			$.getJSON(url,function(data){
				
				//解析XML中的返回代码
				var xml = $.parseXMLDocument(data.xmlValue);
	            var message = $("message",xml);
	            if(message.attr("code") == "1"){
	                var tBodyHTML = "";
	                $("flow-config",xml).each(
	                    function(index){
	                        var flowConfig = $(this);
	                        tBodyHTML += "<tr  onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
								      + "<td><a class=\"opLink\" onclick=\"selectFlow('" + $(flowConfig).attr("flow-key") + "','" + $("flow-name",flowConfig).text() + "');\">选择</a></td>"//操作
								      + "<td>" + $("flow-class",flowConfig).text() + "</td>"//流程分类
							          + "<td>" + $("flow-name",flowConfig).text() + "</td>"//流程名称
									  + "</tr>";
	                    }
	                );
	                $("#flowTab > tbody").html(tBodyHTML);
	            }
	            else{
	               alert($("message",xml).text());
	            };
			});
			
		}
		
		function selectFlow(flowKey, flowName){
			selectedFlow = {id:flowKey,name:flowName};
			args.selected = [selectedFlow];
			window.returnValue = true;
			window.close();
		}
		
		function cancel(){
			window.returnValue = false;
			window.close();
		}
		
		function clearFlow(){
			args.selected = [{id:"", name:""}];
			window.returnValue = true;
			window.close();
		}
	</script>
</head>
<body class="bdDia" onload="init();">
<div class="dialogBk">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top">
				<!-- 对话框[流程选择] -->
					<div id="flowDialog" style="padding:10px; margin:0">
						<div class="allList" style="overflow:auto;height:268px">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" id="flowTab">
							<thead>
						 	<tr>
							 	<th nowrap width="30">操作</th>
							 	<th>流程类别</th>
							 	<th>流程名称</th>
						  	</tr>
						  	</thead>
						  	<tbody></tbody>
					    </table>
					</div>       	       
				<!-- 对话框[流程选择] end-->
			</td>
		</tr>
	</table>
    
</div>
<div class="addTool2">
        <input type="button" value="清 除" onclick="clearFlow();" class="allBtn">&nbsp;&nbsp;&nbsp;
        <input type="button" value="取 消" onclick="cancel();" class="allBtn">
</div>
</body>
</html>
