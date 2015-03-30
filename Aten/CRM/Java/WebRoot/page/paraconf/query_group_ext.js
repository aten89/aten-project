var listGroupTree= null;

$(initListGroup);

//初始化页面
function initListGroup(){
	//添加权限约束
	$.handleRights({
        "editGroup" : $.SysConstants.MODIFY
    },"hidModuleRights");
    
    //初始化树
    initGroupTree("");
    
    //绑定修改    
    $('#editGroup').click(function(){
        $('#opType').val('modify');
        var groupID = $("#curGroupId").val();
        loadModule("initmodify",groupID);
    });
}

//载入群组树
function initGroupTree(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : ERMP_PATH+"m/rbac_group/subgroups",
		dataType : "json",
		data : {type : value},
		success : function(data){
			var htmlValue = data.htmlValue;
	        $("#groupTree > li > ul").html(htmlValue);
	        listGroupTree = $("#groupTree").simpleTree({
	        	animate: true,
	        	basePath: ERMP_PATH,
	        	autoclose:false,
	        	selectRoot : true,
	        	json: true,
	        	afterClick: clickTree
	        })[0];
		}
	});
}

//选中树节点
function clickTree(o){
    var groupId = $(o).attr("groupid");   
    
    if ($.trim(groupId) == ""){
    	//清空
        $('#titlName').html("");
        $('#curGroupId').val("");
        $('#curGroupName').val("");
        $('#curBusinessType').val("");
        //显示工具栏
        $('#allTool').show();
        $("#editGroup").attr("disabled","true").addClass("icoNone");
        $("#groupModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
    }else{
    	var p = $("#groupTree")[0];
        if($(o).offset().top < $(p).offset().top){
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        }else if($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()){
            p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
        }
        $('#opType').val('view');
        loadModule("view", groupId);
    }
}

//载入模块
function loadModule(servletAction,groupId){
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/group_ext/" + servletAction,
        data : {groupID : groupId?groupId:""},
        success : function(html){
        	if (html.charAt(0) =='{') {//返回JSON数据，说明已超时
		        	top.location.reload();
			} 
            $("#groupModuleMain")[0].innerHTML = html;
            $("#groupModuleMain script").each(
                function(){
                    $(this).appendTo("#groupModuleMain");
                }
            );
        }
    });
}