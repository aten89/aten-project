var flowNotify;
$(init);

//初始化页面
function init(){
	parent.window.returnValue = false;
	
	flowNotify = parent.flowNotify;
	$("#subject").val(flowNotify.subject);
	$("input[name='notifyType'][value='" + flowNotify.notifyType + "']").attr("checked","checked");
	
	$("#btnCancel").click(function(){
		parent.closeDialog();
	});
	
	$("#btnSave").click(function(){
		saveFlowNotify();
	});

	//选择机构
	$.getJSON(ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?",function(data){
		$("#groupIdDiv").html(data.htmlValue);
		var groupSelect = $("#groupIdDiv").ySelect({
			width:55,
			onChange: function(value){
				if (value != "") {
					$("#searchGroupUsers").click();
				} else {
					$("#searchGroupUsers > tbody").empty();
				}
			}
		});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
	});
	
	//搜索用户帐号
   $("#searchGroupUsers").click(function(e){
	   	var groupId = $.trim($("#hidGroupId").val());
		var keyword = $.trim($("#userKeyword").val());
		var bindusers="";
		if (groupId == "" && keyword == "") {
			$.alert("请输入查询条件！");
			$("#userKeyword").focus();
			return;
		}
		
		var url = ERMP_PATH + "m/user_account/xmlusers?groupID="+groupId+"&keyword="+encodeURI(keyword) + "&jsoncallback=?";

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
	                         //判断是否已被添加
	                        if($("#bindUserList input[value=" + $("+input",this).val() + "]").length != 0){
	                        	$.alert("不能重复绑定");
	                            return;
	                        };
	                        $("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\"><td><a class=\"opLink\" onclick=\"$(this).parent().parent().remove();\">删除</a><input type=\"hidden\" value=\"" + $(this).parent().find("input").val() + "\"></td><td>" + $(this).parent().parent().find("td").clone().eq(1).html() +"</td><td>" + $(this).parent().parent().find("td").clone().eq(2).html() +"</td></tr>");
	                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
	                    }
	                );
	        	}
            } else {
                $("#actorUserList > tbody").empty();
            }
		});	
	});
	
	//加载已选知会人
	if (flowNotify.notifyUser) {
		var userArr = flowNotify.notifyUser.split(",");
		var userNameArr = flowNotify.notifyUserName.split(",");
		var html = "";
		$(userArr).each(function(i,v){
			if (v) {
				html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\"><td><a class=\"opLink\" onclick=\"$(this).parent().parent().remove();\">删除</a><input type=\"hidden\" value=\"" + v + "\"></td><td>" + v +"</td><td>" +  userNameArr[i] +"</td></tr>";
			}
		});
		$("#bindUserList > tbody").html(html);
		 
	}
}

function saveFlowNotify(){
	var subject = $("#subject").val();
	if (subject==""){
		$.alert("知会标题不能为空");
		return;
	}
	var notifyType = $("input[name='notifyType'][@checked]").val();
	var notifyUser = "";
	var notifyUserName = "";
	$("#bindUserList > tbody tr").each(function(i){
		notifyUser += $(this).find("input").val() + ",";
		notifyUserName += $(this).find("td").eq(2).text() + ",";
	});
//	if (notifyUser==""){
//		$.alert("知会人员不能为空");
//		return;
//	}
	
	flowNotify.subject=subject;
	flowNotify.notifyType = notifyType;
	flowNotify.notifyUser = notifyUser;
	flowNotify.notifyUserName = notifyUserName;
	flowNotify.creator = $("#creator").text();
	flowNotify.groupFullName = $("#groupFullName").text();
	
	parent.window.returnValue = true;
	parent.closeDialog();
}
    