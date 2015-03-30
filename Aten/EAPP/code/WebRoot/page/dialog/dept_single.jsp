<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>部门选择框（单选）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
	<script language="javascript">
		var args = parent.window.dialogArguments1;
		var selectedDept = null;
		
		function init(){
			initDeptList();
		}

		//初始化组织机构树
		function initDeptList(){
			var url = args.ermpPath + "m/rbac_group/subgroups?type=";
			if (args.ermpPath != args.basePath){
				//跨域
				url += "&jsoncallback=?";
			}
			$.getJSON(url,function(data){
				$("#groupsList > li > ul").html(data.htmlValue);
				$("#groupsList").simpleTree({
	                animate: true,
	                basePath : args.ermpPath,
	                json:true,
	                afterClick: function(o){
	                    var groupId = $(o).attr("id");
	                    var groupName = $(o).find("span:first").text();
	                    selectedDept = {id:groupId,name:groupName};
	               	}
	            });
			});
		}
		
		function selectDept(){
			if (selectedDept == null){
				$.alert("请选择一个组织机构！");
				return false;
			}
		//	args.selected = [selectedDept];
			args.selected.push(selectedDept);
			parent.window.returnValue1 = true;
			args.closeDialog();
		}
		
		function cancel(){
			parent.window.returnValue1 = false;
			args.closeDialog();
		}
		
		function clearDept(){
			//args.selected = [{id:"", name:""}];
			args.selected.push({id:"", name:""});
			parent.window.returnValue1 = true;
			args.closeDialog();
		}
	</script>
</head>
<body class="bdDia" onload="init();">
<div class="dialogBk">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top">
			  <div class="mkdz">
				<!--系统模块树-->
				<ul id="groupsList" class="simpleTree" style="height:301px; width:335px">
					<li class="root"><span>请选择组织机构</span>
						<ul></ul>
					</li>
				</ul>
				<!--系统模块树 end-->
			  </div>
			</td>
		</tr>
	</table>
    
</div>
<div class="addTool2">
        <input type="button" value="确 定" onclick="selectDept();" class="allBtn"/>
        <input type="button" value="清 除" onclick="clearDept();" class="allBtn"/>
        <input type="button" value="取 消" onclick="cancel();" class="allBtn"/>
</div>
</body>
</html>
