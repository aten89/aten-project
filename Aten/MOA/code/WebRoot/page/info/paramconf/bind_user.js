var deptTree = null;
var isChange = false;
var flagArgs;
$(initImpowerUser);
function initImpowerUser(){
    flagArgs= $("#flagArgs").val();
    $("#titleName").text($("#titleArgs").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerUser").attr("disabled","true").addClass("icoNone");
    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
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
    
    //当flagArgs=2时，是绑定收件人账号
	if (flagArgs == 2) {
		 $("#ImpowerPost").remove();
		 $("#ImpowerGroup").remove();
	}
    
    /*
	//初始化机构列表
	$('#deptDialog').dialog({
		title: '机构列表',
		minWidth: 260,
		minHeight: 320,
		width: 260,
		height: 320,
		autoOpen: false,
		buttons: {
			'确认': function() {
	             $("#hidGroupId").val(deptTree.attr("groupid"));
	             $("#deptName").val(deptTree.attr("groupname"));
	             $(this).dialog('close');
	             $("#userKeyword").focus();
			},
			'取消' : function(){
	             $("#hidGroupId").val("");
	             $("#deptName").val("");
	             $(this).dialog('close');
			}
		}
	});
	*/
    /*
	//加载树列表
	$("#deptDialog .simpleTree > li > ul").load(ERMP_PATH + "/m/rbac_group?act=subgroups" + "&path=" + ERMP_PATH,
	    function(){
	        deptTree = $("#deptDialog .simpleTree").simpleTree({
	            animate: true,
	            afterClick: function(o){
	                //设置临时存储,便于在确定时取值出来
	                deptTree.attr({groupid : $(o).attr("groupid"),groupname : $(">span",o).text()});
	            }
	        });
	    }
	);
	*/
	
	//保存绑定用户帐号
    $("#saveUserRoles").click(
        function(e){
        	if (flagArgs == 2) {
        		saveEmailUser();
        	} else {
        		saveBindedUsers();
        	}
        }
    );
    
	//搜索用户帐号
   $("#searchGroupUsers").click(
	   	function(e){
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
		                        $("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\"><td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).parent().find("input").val() + "\"></td><td>" + $(this).parent().parent().find("td").clone().eq(1).html() +"</td></tr>");
		                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
		                    }
		                );
		        	}
	            } else {
	                $("#actorUserList > tbody").empty();
	            }
			});	
		    
	   	}
   );
   if (flagArgs == 2) {
	   loadEmailUsers();
   } else {
	   loadBindedUsers();
   }
   $.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers"));//回车搜索
}

/**
 * 加载绑定的授权用户账号
 */
function loadBindedUsers(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/info_param",
		data : {act:"get_binding_users",id:$("#id").val(),flag:flagArgs},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
		           $("assign",xml).each(function(index){
		               var contents=$(this);
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input type=\"hidden\" value=\"" + contents.text() + "\"></td>"
		               			 +"<td>" + contents.text() + "</td></tr>";
		           });
		          $("#bindUserList > tbody").html(tbodyHtml).find("td:empty").html("&nbsp;");
		          //$.alert("绑定成功！");
		    }else if(message.text() != ""){
		        $.alert("读取已绑定用户失败，原因：" + message.text());
		     };
		},
		error : $.ermpAjaxError
	});
}

/**
 * 保存绑定的授权用户账号
 */
function saveBindedUsers() {
	var users = "";
	$("#bindUserList > tbody tr").each(function(i){
		users += "&user_ids=" + $(this).find("input").val();
	});
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
        url  : "m/info_param?act=binduser&id="+$("#id").val()+"&flag="+flagArgs,
		data : users,
        success : function(xml){
            var message = $("message",xml);
            if (message.attr("code") == "1") {
                $.alert("绑定成功！");
            }else if(message.text() != ""){
                $.alert("服务器消息：" + message.text());
            };
           $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
        },
        error : $.ermpAjaxError
    });
}

/**
 * 删除绑定的账号
 * @param a
 */
function delUsers(a){
	$(a).parent().parent().remove();
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}

/**
 * 加载绑定的收件人账号
 */
function loadEmailUsers(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/info_param",
		data : {act:"get_email_users",id:$("#id").val(),flag:flagArgs},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
		           $("emailUsers",xml).each(function(index){
		               var contents=$(this);
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input type=\"hidden\" value=\"" + contents.text() + "\"></td>"
		               			 +"<td>" + contents.text() + "</td></tr>";
		           });
		          $("#bindUserList > tbody").html(tbodyHtml).find("td:empty").html("&nbsp;");
		          //$.alert("绑定成功！");
		    }else if(message.text() != ""){
		        $.alert("读取已配置收件人失败，原因：" + message.text());
		     };
		},
		error : $.ermpAjaxError
	});
}

/**
 * 保存绑定的收件人
 */
function saveEmailUser() {
	var users = "";
	$("#bindUserList > tbody tr").each(function(i){
//		users += "&user_ids=" + $(this).find("input").val();
		users += $(this).find("input").val() + ",";
	});
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
        url  : "m/info_param?act=bindEmailUser&id="+$("#id").val()+"&emailUsers=" + users,
		data : users,
        success : function(xml){
            var message = $("message",xml);
            if (message.attr("code") == "1") {
                $.alert("绑定成功！");
            }else if(message.text() != ""){
                $.alert("服务器消息：" + message.text());
            };
           $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
        },
        error : $.ermpAjaxError
    });
}