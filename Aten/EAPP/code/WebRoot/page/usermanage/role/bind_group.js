var isChange = false;
$(initBindGroup);
function initBindGroup(){
    //$.getMainFrame().getCurrentTab().setTitle("机构-角色管理", "角色绑定机构");
    
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#bindRBACRoleGroup").attr("disabled","true").addClass("icoNone");
    
    var dptSelect = $("#deptType").ySelect({
        width: 69,json:true,
        url:"m/datadict/dictsel?dictType=GROUP_TYPE",
        onChange : initGroupTree,
        afterLoad:addOption
    });
    function addOption() {
		dptSelect.addOption("", "全部", 0);
	}
	
	//保存按扭不可用
    $("#saveGroupRoles").attr("disabled","true").addClass("icoNone");
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
    $("#saveGroupRoles").click(
        function(e){
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/rbac_role/bindgroup",
        		dataType : "json",
                data : $("#contetnMain").serialize(),
                success : function(data){
                   if ($.checkErrorMsg(data) ) {
                		$.alert("绑定成功！");
                	}
                    $("#saveGroupRoles").attr("disabled","true").addClass("icoNone");
                   	isChange = false;
                }
            });
        }
    );
    //初始化树
    initGroupTree("");
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
     			$("#saveGroupRoles").click();
			}
			loadBindedGroup(o, name);
		});  
		isChange=false;
	} else {
		loadBindedGroup(o, name);
	}
}

function loadBindedGroup(o, name) {
 //   $("#hidRoleId").val(o);
 //   $("#hidRoleName").val(name);
 //   $("#modifyRBACRole").removeAttr("disabled").removeClass("icoNone");
	$.ajax({
		type : "POST",
		async : true,
		cache: false,
	   	url  : "m/rbac_role/bindedgroups",
	   	dataType : "json",
		data : {roleID : o},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
	    		var dataList = data.groups;
				if (dataList) {
					var rolesActionHTML = "";
					$(dataList).each(function(i) {
						var namePath = dataList[i].namePath;
			         	namePath = namePath.substr(0,namePath.length - 1);
			                        
			         	rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
			                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"groupIDs\" value=\"" + dataList[i].groupID + "\"></td>"
			                               + "<td>" + namePath.replace(/,/gi," &lt;&lt; ") + "</td>"
			                               + "</tr>";
					});
					$("#bindGroupList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
		        	$("#roleid").val(o);
		        	 //点击解绑定事件
			        $("#bindGroupList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveGroupRoles").removeAttr("disabled").removeClass("icoNone");
			                isChange = true;
			            }
			        );
				} else {
					$("#bindRightList > tbody").empty();
				}
	    	}
		}
	});	
}

//载入群组树
function initGroupTree(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/rbac_group/subgroups",
		dataType : "json",
		data : {type : value},
		success : function(data){
			 $("#groupsList > li > ul").html(data.htmlValue);
			  $("#groupsList").simpleTree({
                animate: true,
                json: true,
                afterClick: function(o){
                    var groupId = $(o).attr("groupid");
                    if($(".jsdw input:checked").length == 0){
                         $.alert("请选择要绑定的角色！");
                         return;
                     };
                    //判断是否已被添加
                    if($("#bindGroupList input[value='" + groupId + "']").length != 0){
                    	 $.alert("不能重复绑定");
                        return;
                    };
                    
                    var groupPath = "";
                    var c_ele = o;
                    while(!$(c_ele).hasClass("root") && c_ele){
                        groupPath += " &lt;&lt; " + $("> span",c_ele).text();
                        c_ele = $(c_ele).parents("li[groupid]:first")[0];
                    };
                    var groupHTML = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
                              + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"groupIDs\" value=\"" + groupId + "\"></td>"
                              + "<td>" + groupPath.replace(" &lt;&lt; ","") + "</td>"
                              + "</tr>";
                    $("#bindGroupList > tbody").append(groupHTML).find("a:last").click(
                        function(e){
                            $(this).parent().parent().remove();
                            $("#saveGroupRoles").removeAttr("disabled").removeClass("icoNone");
                        	isChange = true;
                        }
                    );
                    $("#saveGroupRoles").removeAttr("disabled").removeClass("icoNone");
                    isChange = true;
                }
            });
		}
	});
}