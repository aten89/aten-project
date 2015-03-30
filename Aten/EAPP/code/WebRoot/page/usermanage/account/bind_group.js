$(intiBindGroup());
function intiBindGroup(){
    //$.getMainFrame().getCurrentTab().setTitle("群组-用户帐号", "用户帐号绑定群组");
    
	$("#modUserAcc").removeAttr("disabled").removeClass("icoNone");
    $("#bindOrg").attr("disabled","true").addClass("icoNone");
    $("#bindRole").removeAttr("disabled").removeClass("icoNone");
	
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
	$("#saveBind").attr("disabled","true").addClass("icoNone");
	$(".infoTip").html("当前编辑的用户帐号："+$("#hidAccountId").val()+"&nbsp;&nbsp;&nbsp;显示名称：" 
			+ $("#hidDisplayName").val()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;锁定状态：" 
			+ ($("#hidIsLock").val()=="Y" ? "锁定":"未锁定"));
	var accountId=$("#hidAccountId").val();
	//保存
    $("#saveBind").click(
        function(e){
        	$("#accountid_").val(accountId?accountId : "");
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/user_account/bindgroup",
        		dataType : "json",
                data : $("#mainForm").serialize(),
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		$.alert("绑定成功！");
                	}
                    $("#saveBind").attr("disabled","true").addClass("icoNone");
                }
            });
        }
    );
    //初始化树
	initGroupTree("");
	
    //删除
    $("#delBind").click(function(){
        delBind();
    });
         
    getBindedGroup(accountId);
}

//初始化已绑定的组
function getBindedGroup(accountId){
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/user_account/bindedgroups",
        dataType : "json",
        data : {accountID:accountId}, 
        success : getBindedGroupSuccess
    });	
}

//初始化已绑定列表返回成功
function getBindedGroupSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.groups;
		if (dataList) {
			var rolesActionHTML = "";
	        $(dataList).each(function(i) {
	            	var namePath = dataList[i].namePath;
	                namePath = namePath.substr(0,namePath.length - 1);
	                        
	                rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
	                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"groupIDs\" value=\"" + dataList[i].groupID  + "\"></td>"
	                               + "<td>" + namePath.replace(/,/gi," &lt;&lt; ") + "</td>"
	                               + "</tr>";
	        });
	        $("#bindGroupList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
	        //点击解绑定事件
	        $("#bindGroupList > tbody a").click(
	            function(e){
	                $(this).parent().parent().remove();
	                $("#saveBind").removeAttr("disabled").removeClass("icoNone");
	            }
	        );
		}
	} else {
		 $("#bindRightList > tbody").empty();
	}
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
	        	afterClick : function(o){
                    var groupId = $(o).attr("groupid");
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
                            $("#saveBind").removeAttr("disabled").removeClass("icoNone");

                        }
                    );
                    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
	        	}
			});
		}
	});
}