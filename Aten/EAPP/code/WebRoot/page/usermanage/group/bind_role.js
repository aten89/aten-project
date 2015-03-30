$(initBindRole);

//初始化页面
function initBindRole(){
    $("#bindRole,#saveBind").attr("disabled","true").addClass("icoNone");
    $("#addSubGroup,#sortSub,#editGroup,#delGroup,#bindPerson,#bindPost").removeAttr("disabled","true").removeClass("icoNone");
    
    var groupId = $('#curGroupId').val();
    
    //查询
    $("#searchRole").click(function(){
        getRole($.trim($("#rolename").val()));
    });
    //添加
    $("#addBind").click(function(){
        addBind();
    }); 
    //删除
    $("#delBind").click(function(){
        delBind();
    }); 
    //保存
    $("#saveBind").click(function(){
        saveBind(groupId);
    });     
    getExRole(groupId);
    //回车搜索
    $.EnterKeyPress($("#rolename"),$("#searchRole"));      
}

//取得绑定角色
function getExRole(groupId){
    $("#bindedRole").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/bindedroles",
        dataType : "json",
        data : {groupID : groupId?groupId:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.roles;
        		if (dataList) {
        			var StrHtml = "";
        			$(dataList).each(function(i) {
        				StrHtml+="<option value=\"" + dataList[i].roleID + "\">" + dataList[i].roleName + "</option>";
        			});
        			$("#bindedRole").html(StrHtml);     
        		}
        	}
        }
    }); 
}

//取得角色
function getRole(rolename){
    $("#role").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_role/xmlroles",
        dataType : "json",
        data : {roleName : rolename?rolename:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.roles;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
						 //已选的不列出
			            if ($("option[value=\""+dataList[i].roleID+"\"]","#bindedRole").length == 0){
			                StrHtml+="<option value=\"" + dataList[i].roleID + "\">" + dataList[i].roleName + "</option>";
			            }	
					});
					$("#role").html(StrHtml);
				}
        	}
        }
    }); 
}

//添加绑定列表
function addBind(){
    $("option:selected","#role").each(function(){
        if ($("option[value='"+$(this).val()+"']","#bindedRole").length == 0){ 
            $(this).appendTo($("#bindedRole"));
        }else{
            $.alert("该角色已绑定!");
        }
    });
    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//删除绑定列表
function delBind(){
    $("option:selected","#bindedRole").each(function(){
        if ($("option[value='"+$(this).val()+"']","#role").length == 0){ 
            $(this).appendTo($("#role"));
        }else{
            $(this).remove();
        }
    });
    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//保存绑定
function saveBind(groupId){
    var bindList="";
    $("option","#bindedRole").each(function(){
        bindList+="&roleIDs="+$(this).val();
    });
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/bindrole",
        dataType : "json",
        data : (groupId?"&groupID=" + groupId : "") + bindList,
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("角色绑定成功!");      
        		$("#saveBind").attr("disabled","true").addClass("icoNone");
        	}
		}
    }); 
}