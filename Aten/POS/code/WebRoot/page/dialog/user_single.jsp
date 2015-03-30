<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>人员选择框（单选）</title>
	<jsp:include page="../base_path.jsp"></jsp:include>
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
		   	$("#cancel").click(function(e){
				parent.window.returnValue1 = false;
				args.closeDialog();
		   	});
		   	$("#clear").click(function(e){
		   		args.selected.push({id:"", name:"",deptName:""});
				parent.window.returnValue1 = true;
				args.closeDialog();
		   	});
			//初始化机构列表
			initDeptList();
		}

		//初始化机构列表
		function initDeptList(){
			var url = args.ermpPath + "m/rbac_group/groupselect";
			if (args.ermpPath != args.basePath){
				//跨域
				url +="?jsoncallback=?";
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
				$.alert("请输入查询条件！");
				$("#userKeyword").focus();
				return;
			}
			var url = args.ermpPath + "m/user_account/xmlusers?groupID=" + groupId + "&keyword=" + encodeURI(keyword);
			if (args.ermpPath != args.basePath){
				//跨域
				url +="&jsoncallback=?";
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
		                        obj = obj.next();
		                        var displayName = obj.text();
		                        obj = obj.next();
		                        var deptName = obj.text();
		                        args.selected.push({id:accountId,name:displayName,deptName:deptName});
		                        parent.window.returnValue1 = true;
								args.closeDialog();
		                    }
		                );
		        	}
	            } else {
	                $("#actorUserList > tbody").empty();
	            }
			});
		}

	</script>
</head>
<body class="bdDia" onload="init();">
<div class="dialogBk" style="width:350px;">
	<table  border="0" cellspacing="0" cellpadding="0" >
		<tr>
			<td valign="top">
			  <!-- 对话框[选择] -->
		      <div class="mkdzTip" >
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
			    <div id="unBindUser" class="bindUser" style="height:273px;width:340px" >
			      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
		            <thead>
		              <tr>
						 <th nowrap width="30">操作</th>
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
		</tr>
	</table>
</div>
<div class="addTool2" id="but01">
    <input type="button" id="clear" class="allBtn" value="清 除"/>
	<input type="button" id="cancel" class="allBtn" value="取 消"/>
</div>
</body>
</html>
