var deptTree = null;
var groupSelect;
$(initBindUser);

//初始化页面
function initBindUser(){
    $("#bindPerson,#saveBind").attr("disabled","true").addClass("icoNone");
    $("#addSubGroup,#sortSub,#editGroup,#delGroup,#bindRole,#bindPost").removeAttr("disabled","true").removeClass("icoNone");
    
    var groupId = $('#curGroupId').val();

    //保存绑定
    $("#saveBind").click(
    	function(){
    		$.ajax({
                type : "POST",
                cache: false,
                async : true,
                dataType : "json",
        		url  : "m/rbac_group/binduser" + (groupId?"?groupID=" + groupId : ""),
                data : $("#contetnMain").serialize(),
                success : function(data){
                   if ($.checkErrorMsg(data) ) {
                		 $.alert("绑定成功！");
                	}
                   $("#saveBind").attr("disabled","true").addClass("icoNone");
                }
            });
    	}
    );
    
    groupSelect = $("#groupIdDiv").ySelect({
    	width: 55,
    	json:true, 
    	url : "m/rbac_group/groupselect",
    	afterLoad:function(){
    		groupSelect.addOption("", "所有...", 0);
			groupSelect.select(0);
    	},
    	onChange: function(value){
    		if (value != "") {
				$("#searchGroupUsers").click();
			} else {
				$("#actorUserList > tbody").empty();
			}
    	}
    });
    
    //打开机构选择对话框
//    $("#deptSelBut").click(function(){
//    	var selector = new DeptDialog(BASE_PATH);
//		var selected = selector.openDialog("single");
//		if (selected) {
//			$("#hidGroupId").val(selected.id);
//			$("#deptName").val(selected.name);
//		}
//    });
    
    //搜索用户帐号
   	$("#searchGroupUsers").click(
	   	function(e){
//		 	var groupId = $.trim($("#hidGroupId").val());
	   		var groupId = groupSelect.getValue();
    		var keyword = $.trim($("#userKeyword").val());
    		if (groupId == "" && keyword == "") {
    			$.alert("请输入查询条件！");
    			$("#userKeyword").focus();
    			return;
    		}
		    $.ajax({
		        type : "POST",
		        cache: false,
		        async : true,
				url  : "m/user_account/xmlusers",
				dataType : "json",
		        data : "groupID="+groupId+"&keyword="+keyword,
		        success : function(data){
		        	if ($.checkErrorMsg(data) ) {
        				var dataList = data.userAccounts;
        				if (dataList) {
        					var moduleActionHTML = "";
        					$(dataList).each(function(i) {
        						var groupName = dataList[i].groupNames;
		                        moduleActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
                                         + "<td><a class=opLink>绑定</a><input type=\"hidden\" value=\"" + dataList[i].userID + "\"></td>"
                                         + "<td>" + dataList[i].displayName + "</td>" 
                                      	 + "<td>" + dataList[i].accountID + " </td>"
                                       	 + "<td>" + groupName + "</td>"
                                         + "</tr>";
        					});
        					$("#actorUserList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
		                
			                //点击绑定事件
			                $("#actorUserList a").click(
			                    function(e){
			                        //判断是否已被添加
			                        if($("#bindUserList input[value='" + $("+input",this).val() + "']").length != 0){
			                        	$.alert("不能重复绑定");
			                            return;
			                        };
			                        
			                        $("#bindUserList > tbody").append(
			                            $(this).parent().parent().clone().removeClass("over")
			                            
			                        )
			                        .find("a:last").html("删除").click(
			                            function(e){
			                                $(this).parent().parent().remove();
			                                $("#saveBind").removeAttr("disabled").removeClass("icoNone");
			                            }
			                        ).next().attr("name","userIDs");
			                        
			                        $("#saveBind").removeAttr("disabled").removeClass("icoNone");
			                    }
			                );
        				} else {
        					$("#actorUserList > tbody").empty();	
        				}
		        	}
		        }
		    });
	   	}
   	);
   
   	//回车搜索
   	$.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers")); 
    
	//取得已绑定的用户
    getBindedUsers(groupId);  
}

//取得绑定用户
function getBindedUsers(groupId){
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/bindedusers",
        dataType : "json",
        data : {groupID: groupId?groupId : ""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
	    		var dataList = data.userAccounts;
        		if (dataList) {
        			var rolesActionHTML = "";
        			$(dataList).each(function(i) {
        				 rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"userIDs\" value=\"" + dataList[i].userID + "\"></td>" 
                               + "<td>" + dataList[i].displayName + "</td>" 
                               + "<td>" + dataList[i].accountID + " </td>"
                               + "<td>" + dataList[i].groupNames + "</td>"
                               + "</tr>";
        			});
        			$("#bindUserList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
        
			        //点击解绑定事件
			        $("#bindUserList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveBind").removeAttr("disabled").removeClass("icoNone");
			            }
			        );
        		} else {
        			 $("#bindUserList > tbody").empty();
        		}
        	}
        }
    }); 
}
