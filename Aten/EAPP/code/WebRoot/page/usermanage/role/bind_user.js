var deptTree = null;
var groupSelect;
var isChange = false;
$(initBindUser);

function initBindUser(){
    //$.getMainFrame().getCurrentTab().setTitle("用户-角色管理", "角色绑定用户帐号");
    
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#bindRBACRoleUser").attr("disabled","true").addClass("icoNone");
    //保存按扭不可用
    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
    var selectRoleID = $("#hidRoleId").val();
    /*如果ID为空则不能进行修改操作,不为空则可修改且列出唯一项*/
    if(selectRoleID=='null' || selectRoleID== ""){
    	$("#modifyRBACRole").attr("disabled","true").addClass("icoNone");
    	$.ajax({
			type : "POST",
			cache: false,
			async : true,
			dataType : "json",
		   	url  : "m/rbac_role/xmlroles",
		    success : searchRolesSuccess
		});
    }else{
    	$("#txtSearchRole").attr("disabled","true").addClass("icoNone");
    	$("#search_Role").attr("disabled","true").addClass("icoNone");
    	var selectRoleName = $("#hidRoleName").val();
    	var StrHtml ="";
    	StrHtml += "<div><input name=\"roleid_1\" type=\"radio\" value=\"" + selectRoleID + "\" id=\"" + selectRoleID + "\" onclick=\"javascript:selectRole('"+selectRoleID+"','"+selectRoleName+"');\"/>" 
                                   + "<label>" + selectRoleName + "</label></div>";
		$(".jsdw").html(StrHtml); 
    	$("#"+selectRoleID).click();
    }
    //角色列表筛选
    $("#search_Role").click(
        function(e){
        	$(".jsdw > div:hidden").show();
        	var tvalue = $.trim($("#txtSearchRole").val());
            if(tvalue == ""){
                return;
            };
            $(".jsdw label:not(:contains('" + tvalue + "'))").parent().hide();
            //选中的不隐藏
            $(".jsdw input:checked").parent().show();
        }
    );
	$.EnterKeyPress($("#txtSearchRole"),$("#search_Role")); 
	
	 //绑定用户帐号
    $("#saveUserRoles").click(
        function(e){
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/rbac_role/binduser",
        		dataType : "json",
                data : $("#contetnMain").serialize(),
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		 $.alert("绑定成功！");
                	}
                	$("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                   isChange = false;
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
//		   	var groupId = $.trim($("#hidGroupId").val());
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
			                        if($(".jsdw input:checked").length == 0){
			                            $.alert("请选择要绑定的角色！");
			                            return;
			                        };
			                        
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
			                                $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
			                                isChange = true;
			                            }
			                        ).next().attr("name","userIDs");
			                        
			                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
			                        isChange = true;
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
 //  $("#searchGroupUsers").click();
    
    //回车搜索
    $.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers"));  

}

//搜索所有有效角色
function searchRolesSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.roles;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				var sid = dataList[i].roleID;
	    		var sName = dataList[i].roleName;
				StrHtml += "<div><input name=\"roleid_1\" type=\"radio\" value=\"" + sid + "\"  onclick=\"javascript:selectRole('"+sid+"','"+sName+"');\"/>" 
	            	+ "<label>" + sName + "</label></div>";
			});
			$(".jsdw").html(StrHtml); 
		}
	}
}

//左边的单选单击
function selectRole(o, name){
	if (isChange) {
		$.confirm("是否先保存当前修改？", function(r){
			if (r) {
     			$("#saveUserRoles").click();
			}
			loadBindedUsers(o, name);
		});
		isChange=false;
	} else {
		loadBindedUsers(o, name);
	}
}

function loadBindedUsers(o, name) {	
 //   $("#hidRoleId").val(o);
  //  $("#hidRoleName").val(name);
  //  $("#modifyRBACRole").removeAttr("disabled").removeClass("icoNone");
	$.ajax({
		type : "POST",
		async : true,
		cache: false,
	   	url  : "m/rbac_role/bindedusers",
	   	dataType : "json",
	   	data : {roleID: o},
	    success : function(data){
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
			        $("#roleid").val(o);
			        //点击解绑定事件
			        $("#bindUserList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
			                isChange = true;
			            }
			        );
        		} else {
        			$("#bindUserList > tbody").empty();
        		}
	    	}
		}
	});	
}