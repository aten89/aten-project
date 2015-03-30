<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>部门选择框（多选）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script type="text/javascript" src="${ermpPath}jqueryui/tree/jquery.simple.tree.js"></script>
	<script language="javascript">
		var args = parent.window.dialogArguments1;
		
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
	                createCheckBox : true,
	                afterAjax : function(node){
	                	//由于树是分步加载的
	                	var selected = args.selected;
						for (var i=0 ; i<selected.length ; i++){
							var id = selected[i].id;
							var name = selected[i].name;
							if (id != null && id != ""){
								$(node).find("input[id='chk_" + id + "']").attr("checked",true);
							}else{
								$(node).find("input[name='chk_" + name + "']").attr("checked",true);
							}
						}
	                }
	            });
	            
	            //默认选中外部传入的部门
	            //首选以ID为关键字查找，如果没有ID，则以名称为关键字
	            var selected = args.selected;
				for (var i=0 ; i<selected.length ; i++){
					var id = selected[i].id;
					var name = selected[i].name;
					if (id != null && id != ""){
						$("#groupsList").find("input[id='chk_" + id + "']").attr("checked",true);
					}else{
						$("#groupsList").find("input[name='chk_" + name + "']").attr("checked",true);
					}
				}
			});
		}
		
		function selectDept(){
			var selected = args.selected;
			for (var i=selected.length-1 ; i>=0 ; i--){
				var checkbox = null;
				if (selected[i].id != null && selected[i].id != ""){
					checkbox = $("#chk_" + selected[i].id)[0];
				}else{
					checkbox = $("input[name='chk_" + selected[i].name + "]")[0];
				}
				if (checkbox != null){
					if (checkbox.checked==false) selected.splice(i,1);
					checkbox.checked = false;
				}
			}
			$("#groupsList").find("input[type='checkbox']").each(
				function(i){
					if (this.checked){
						var array = this.value.split(":");
						selected.push({id:array[0], name:array[1]});
					}
				}
			);
			parent.window.returnValue1 = true;
			args.closeDialog();
		}
		
		function cancel(){
			parent.window.returnValue1 = false;
			args.closeDialog();
		}
		
		function clearDept(){
		//	args.selected = [];
			args.selected.splice(0,args.selected.length);
			$("#groupsList").find("input[type='checkbox']").each(
				function(i){
					this.checked = false;
				}
			);
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
