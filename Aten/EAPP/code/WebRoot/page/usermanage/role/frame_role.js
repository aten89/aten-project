$(initRBACRoleFrame);

function initRBACRoleFrame(){
//    $.handleRights({
//        "addRBACRole" : $.SysConstants.ADD,
//        "modifyRBACRole" : $.SysConstants.MODIFY,
//        "bindRBACRoleGroup" : $.SysConstants.BIND_GROUP,
//        "bindRBACRoleUser" : $.SysConstants.BIND_USER,
//        "bindRBACRoleModule" : $.SysConstants.BIND_RIGHT
//    });
    
    
    //添加角色
    $("#addRBACRole").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidRoleId").val("");
            getLoadTypeAndModule();
        }
    );
    
    //编辑角色
    $("#modifyRBACRole").click(
        function(e){
            $("#hidAction").val("modify");
            getLoadTypeAndModule();
        }
    );
    
    //绑定群组
    $("#bindRBACRoleGroup").click(
        function(e){
            $("#hidAction").val("bindgroup");
            getLoadTypeAndModule();
        }
    );
    
    //绑定用户
    $("#bindRBACRoleUser").click(
        function(e){
            $("#hidAction").val("binduser");
            getLoadTypeAndModule();
        }
    );
    
    //绑定模块动作
    $("#bindRBACRoleModule").click(
        function(e){
            $("#hidAction").val("bindright");
            getLoadTypeAndModule();
        }
    );
    getLoadTypeAndModule();
}

//取得载入的标志并载入相应的模块
function getLoadTypeAndModule(){
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    
    switch(action){
        case "add" : 
            loadModule("initadd");
            break;
        case "modify" : 
            var roleId = $("#hidRoleId").val();
            if($.trim(roleId) == ""){
                return;
            };
            loadModule("initmodify",roleId);
            break;
        case "view" : 
             var roleId = $("#hidRoleId").val();
            if($.trim(roleId) == ""){
                return;
            };
            loadModule("view",roleId);
            break;
        case "bindgroup" : 
            var roleId = $("#hidRoleId").val();
            loadModule("initbindgroup",roleId);
            break;
        case "binduser" : 
            var roleId = $("#hidRoleId").val();
            loadModule("initbinduser",roleId);
            break;
        case "bindright" : 
            var roleId = $("#hidRoleId").val();
            loadModule("initbindright",roleId);
            break;         
    };
};

//载入模块
function loadModule(servletAction,roleId){
	var url = "m/rbac_role/" + servletAction;
    var data = {roleID : roleId?roleId : ""};
	$.ajaxLoadPage("contetnMain", url, data);
}
