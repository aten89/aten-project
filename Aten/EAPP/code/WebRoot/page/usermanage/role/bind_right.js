var isChange = false;
$(initBindRight);

function initBindRight(){
    //$.getMainFrame().getCurrentTab().setTitle("动作-角色管理", "角色绑定模块动作");
    
    
    $("#subSystemList").ySelect({width: 110,json:true,url : "m/subsystem/=allsystem",onChange : subSystemList_OnChange});
    
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#bindRBACRoleModule").attr("disabled","true").addClass("icoNone");
    //保存按扭不可用
    $("#saveRightRoles").attr("disabled","true").addClass("icoNone");
    var selectRole = $("#hidRoleId").val();
    /*如果ID为空则不能进行修改操作,不为空则可修改且列出唯一项*/ 
    if(selectRole=='null' || selectRole== ""){
    	$("#modifyRBACRole").attr("disabled","true").addClass("icoNone");
    	$.ajax({
			type : "POST",
			async : true,
			cache: false,
			dataType : "json",
		   	url  : "m/rbac_role/xmlroles",
		    success : searchRolesSuccess
		});
    }else{
    	$("#txtSearchRole").attr("disabled","true").addClass("icoNone");
    	$("#search_Role").attr("disabled","true").addClass("icoNone");
    	var roleName = $("#hidRoleName").val();
    	var StrHtml ="";
    	StrHtml += "<div><input name=\"roleid_1\" type=\"radio\" value=\"" + selectRole + "\" id=\"" + selectRole + "\" onclick=\"javascript:selectRole('"+selectRole+"','"+roleName+"');\"/>" 
                                   + "<label>" + roleName + "</label></div>";
		$(".jsdw").html(StrHtml); 
    	$("#"+selectRole).click();
    };
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
    
    //绑定用户动作
    $("#saveRightRoles").click(
        function(e){
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/rbac_role/bindright",
        		dataType : "json",
                data : $("#contetnMain").serialize(),
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		 $.alert("绑定成功！");
                	}
                    $("#saveRightRoles").attr("disabled","true").addClass("icoNone");
                   	isChange = false;
                }
            });
        }
    );
}

//取得服务有效的权限列表
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
};
       
//左边的单选单击
function selectRole(o, name){
	if (isChange) {
		$.confirm("是否先保存当前修改？", function(r){
			if (r) {
     			$("#saveRightRoles").click();
			}
			loadBindedRight(o, name);
		});
		isChange=false;
	} else {
		loadBindedRight(o, name);
	}
}

function loadBindedRight(o, name) {
 //   $("#hidRoleId").val(o);
 //   $("#hidRoleName").val(name);
 //   $("#modifyRBACRole").removeAttr("disabled").removeClass("icoNone");
	$.ajax({
		type : "POST",
		async : true,
		cache: false,
	   	url  : "m/rbac_role/bindedrights",
	   	dataType : "json",
		data : {roleID: o},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
	    		var dataList = data.moduleActions;
        		if (dataList) {
        			var rolesActionHTML = "";
        			$(dataList).each(function(i) {
        				rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"moduleActionIDs\" value=\"" + dataList[i].moduleActionID + "\"></td>"
		                               + "<td>"+dataList[i].module.subSystem.name+"</td>" 
		                               + "<td>"+dataList[i].module.name+"</td>"
		                               + "<td>"+dataList[i].action.name+"</td>"
		                               + "</tr>";
        			});
        			$("#bindRightList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
		        	$("#roleid").val(o);
		        	//点击解绑定事件
			        $("#bindRightList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
			                isChange = true;
			                $("#" +$(this).next().val(), "#moduleActionList > tbody").removeClass("over");//移除样式
			            }
			        );
			        
			         $("tr", "#moduleActionList > tbody").each(function() {
			         	if ($("#bindRightList input[value='" + $(this).attr("id") + "']").length != 0) {//是否在已绑定列表
			         		$(this).addClass("over");
			         	} else {
			         		$(this).removeClass("over");
			         	}
			         });
        		} else {
        			$("#bindRightList > tbody").empty();
        		}
	    	}
		}
	});	
};

function subSystemList_OnChange(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/module/subsystemtree",
		dataType : "json",
		data : {subSystemID : value},
		success : function(data){
	        $("#jkpzTree").html(data.htmlValue);
	        $("#jkpzTree").simpleTree({animate: true,json:true,afterClick : systemModulesClick});
            //清空权限列表
            $("#moduleActionList > tbody").empty();
		}
	});
};

function systemModulesClick(o){
    var moduleId = $(o).attr("moduleId");
    
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
		url  : "m/module_action/findmas",
		dataType : "json",
		data : "moduleID=" + moduleId + "&isHTTP=Y",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		if(data.moduleActionPage && data.moduleActionPage.dataList){
					var dataList = data.moduleActionPage.dataList;
					var moduleActionHTML = "";
					$(dataList).each(function(i) {
						var maid = dataList[i].moduleActionID;
                    	var inList = $("#bindRightList input[value='" + maid + "']").length != 0;//是否在已绑定列表
                        moduleActionHTML += "<tr id='" + maid + "' " + (inList ? "class='over'" : "") + ">" 
                                         + "<td><a class='opLink bindopt'>绑定</a> <a class='opLink unbindopt'>解除</a><input type=\"hidden\" value=\"" + maid + "\"></td>" 
                                         + "<td>" + dataList[i].action.actionKey + "</td>" 
                                         + "<td>" + dataList[i].action.name + " </td>"
                                         + "</tr>";
					});
					$("#moduleActionList > tbody").html(moduleActionHTML);
					//点击绑定事件
	                $("#moduleActionList .bindopt").click(function(e){
                        if($(".jsdw input:checked").length == 0){
                            $.alert("请选择要绑定的角色！");
                            return;
                        };
						
						var c_tr = $(this).parent().parent();
						var actionID = c_tr.find("input").val();
						
						 //判断是否已被添加
                        if($("#bindRightList input[value='" + actionID + "']").length != 0){
                            $.alert("不能重复绑定");
                            return;
                        };
                        
                        //	var actionKey = c_tr.find("td:eq(1)").html();
						var actionName = c_tr.find("td:eq(2)").html();
                        var systemName = $(".root > span").text();
						var moduleName = $("> span",o).text();
						
						$("#bindRightList > tbody").append(
                        		"<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
                        		+ "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"moduleActionIDs\" value=\"" + actionID + "\"></td><td>" 
                        		+ systemName + "</td><td>" + moduleName + "</td><td>" + actionName + "</td></tr>")
                        .find("a:last").click(function(e){
                                $(this).parent().parent().remove();
                                $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
                        		isChange = true;
                        		
                        		$("#" +$(this).next().val(), "#moduleActionList > tbody").removeClass("over");//移除样式
                        });
                        $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
                        isChange = true;
                        
                        c_tr.addClass('over');//添加已绑定样式
					});
					
					//点击解除绑定事件
	                $("#moduleActionList .unbindopt").click(function(e){
                        if($(".jsdw input:checked").length == 0){
                            $.alert("请选择要绑定的角色！");
                            return;
                        };
						
						var c_tr = $(this).parent().parent();
						var actionID = c_tr.find("input").val();
						
						$("#bindRightList input[value='" + actionID + "']").parent().parent().remove();
						$("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
						isChange = true;
						$("#" +actionID, "#moduleActionList > tbody").removeClass("over");//移除样式
					});
        		} else {
        			$("#moduleActionList > tbody").empty();
        		}
        	}
        }
    });
}