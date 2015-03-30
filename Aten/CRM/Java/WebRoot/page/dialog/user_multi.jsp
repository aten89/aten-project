<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<title>人员选择框（多选）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
	
	<style>
		.mkdzTip td{ background:none}
		.bindUser td{ padding-left:9px}
	</style>
	
	<script language="javascript">
		var args = parent.window.dialogArguments1;
		
		function init(){
			//回车搜索
			$.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers"));
			$("#searchGroupUsers").click(function(e){
				searchUser();
		   	});
		   	$("#select").click(function(e){
				var selected = args.selected;
				selected.splice(0,args.selected.length);//先清空
				$("#bindUserList > tbody tr").each(function(i){
					var userId = $(this).find("input[type='hidden'][name='id']").val();
					var userName = $(this).find("input[type='hidden'][name='name']").val();
					selected.push({id:userId,name:userName});
				});
				//args.selected = getSelectedUser();
				
				parent.window.returnValue1 = true;
				args.closeDialog();
		   	});
		   	$("#cancel").click(function(e){
				parent.window.returnValue1 = false;
				args.closeDialog();
		   	});
		   	$("#clear").click(function(e){
		   		$("#bindUserList > tbody tr").each(function(i){
					$(this).remove();
				});
		   	});
			//初始化机构列表
			initDeptList();
			//添加已选人员
			loadSelected();
		}
		
		function delUsers(a){
			$(a).parent().parent().remove();
		}
		
		//初始化机构列表
		function initDeptList(){
			var url = args.ermpPath + "m/rbac_group/groupselect";
			if (args.ermpPath != args.basePath){
				//跨域
				url += "?jsoncallback=?";
			}
			$.getJSON(url,function(data){
				$("#groupIdDiv").html(data.htmlValue);
				var groupSelect = $("#groupIdDiv").ySelect({
					width:55,
					onChange: function(value){
						if (value != "") {
							$("#searchGroupUsers").click();
						} else {
							$("#actorUserList > tbody").empty();
						}
					}
				});
				groupSelect.addOption("", "所有...", 0);
				groupSelect.select(0);
			});
		}
		
		function searchUser(){
			var groupId = $.trim($("#hidGroupId").val());
			var keyword = $.trim($("#userKeyword").val());
			if (keyword != "") groupId = "";	//搜索账号时，忽略机构	
			if (groupId == "" && keyword == "") {
				alert("请输入查询条件！");
				$("#userKeyword").focus();
				return;
			}
			
			var url = args.ermpPath + "m/user_account/xmlusers?groupID=" + groupId + "&keyword=" + encodeURI(keyword);
			if (args.ermpPath != args.basePath){
				//跨域
				url += "&jsoncallback=?";
			}
			$.getJSON(url,function(data){
				if ($.checkErrorMsg(data) ) {
					var dataList = data.userAccounts;
        			if (dataList) {
		                var moduleActionHTML = "";
		                $(dataList).each(function(i) {
		                    	var groupName = dataList[i].groupNames;
		                        moduleActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
									+ "<td><a class=opLink>选择</a><input type=\"hidden\" value=\"" + dataList[i].accountID + "\"></td>"
									+ "<td>" + dataList[i].accountID + "</td>"
									+ "<td>" + dataList[i].displayName + "</td>" 
									+ "<td>" + groupName + "</td>"
									+ "</tr>";
		                });
		                $("#actorUserList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
		                //点击绑定事件
		                $("#actorUserList a").click(function(e){
		                        var obj = $(this).parent().next();
		                        var accountId = obj.text();
		                        var displayName = obj.next().text();
		                        //判断是否已选择
		                        if ($("#bindUserList > tbody").find("input[name='id'][value='" + accountId + "']").length==0){
		                        	$("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		         						+"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input name=\"id\" type=\"hidden\" value=\"" + accountId + "\"/><input name=\"name\" type=\"hidden\" value=\"" + displayName + "\"/></td>"
		         						+"<td style='white-space:normal'><div class='klWrap'>" + displayName + "</div></td>"
		         						+"<td style='white-space:normal'><div class='klWrap'>" + accountId + "</div></td>"
								  		+"</tr>");
								}
		                    }
		                );
		        	}
	            } else {
	                $("#actorUserList > tbody").empty();
	            }
			});
		}
		
		//加载已选择人员
		function loadSelected() {
        	var selectedHtml = "";
        	for (var i=0 ; i<args.selected.length ; i++){
				var userName = args.selected[i].name;
				var userId = args.selected[i].id;
				selectedHtml += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
	         						+"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input name=\"id\" type=\"hidden\" value=\"" + userId + "\"/><input name=\"name\" type=\"hidden\" value=\"" + userName + "\"/></td>"
	         						+"<td style='white-space:normal'><div class='klWrap'>" + userName + "</div></td>"
	         						+"<td style='white-space:normal'><div class='klWrap'>" + userId + "</div></td>"
							  	+"</tr>";
			}
			$("#bindUserList > tbody").html(selectedHtml);
		}
		
		function getSelectedUser(){
			var selected = [];
			$("#bindUserList > tbody tr").each(function(i){
				var userId = $(this).find("input[type='hidden'][name='id']").val();
				var userName = $(this).find("input[type='hidden'][name='name']").val();
				selected.push({id:userId,name:userName});
			});
			return selected;
		}
	</script>
</head>
<body class="bdDia" onload="init();">
<div class="dialogBk" style="width:658px;">
	<table border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top" width="340" style="padding:0">
			  <!-- 对话框[选择] -->
		      <div class="mkdzTip">
			  	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
			  	  <tr>
					<td>机构：</td>
					<td><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
					<td>帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:65px" /> 
						<input id="searchGroupUsers" type="button" class="allBtn" value="检索" style="width:38px"/>
					</td>
			 	  </tr>
			 	</table>
			  </div>
			  <div class="mkdz">
			    <div id="unBindUser" class="bindUser" style="width:338px">
			      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
		            <thead>
		              <tr>
						 <th nowrap>操作</th>
						 <th width="28%" nowrap>账号</th>
						 <th width="20%" nowrap>姓名</th>
						 <th width="37%" nowrap>所属机构</th>
					  </tr>
					 </thead>
					 <tbody></tbody>
		          </table>
			    </div>
			  </div>    	        
			<!-- 对话框[选择] end-->
			</td>
			<td width="15"><img src="themes/comm/images/dialogMove.gif" style="margin:0; padding:0; vertical-align:middle"/></td>
			<td valign="top" width="302">
			  <div style="padding: 6px 0pt 0pt 10px; position:relative" class="mkdzTip">选择结果</div>
			  <div class="mkdz">
			    <div id="bindUsers" class="bindUser" style="width:300px">
				   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
					   <thead>
						   <tr>
	                          <th width="30px">操作</th>
	                          <th>姓名</th>
	                          <th>账号</th>
						  	</tr>
					   </thead>
					   <tbody>
			
					   </tbody>
				   </table>
			    </div>					
			  </div>
			</td>
		</tr>
	</table>
</div>
<div class="addTool2" id="but01">
    <input type="button" id="select" class="allBtn" value="确 定"/>
    <input type="button" id="clear" class="allBtn" value="清 除"/>
	<input type="button" id="cancel" class="allBtn" value="取 消"/>
</div>
</body>
</html>
