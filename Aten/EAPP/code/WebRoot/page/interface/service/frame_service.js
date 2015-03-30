$(initBulletinFrame);

function initBulletinFrame(){
//    $.handleRights(
//        {
//            "addFwpz" : $.SysConstants.ADD,
//            "modFwpz" : $.SysConstants.MODIFY,
//            "bindJrzh" : $.SysConstants.BIND_ACTOR,
//            "bindMkdz" : $.SysConstants.BIND_RIGHT
//        }
//    );
	
	/*新增服务*/
    $("#addFwpz").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidBulletinId").val("");
            getLoadTypeAndModule();
        }
    );
    
    /*修改服务*/
    $("#modFwpz").click(
        function(e){
            $("#hidAction").val("modify");
            getLoadTypeAndModule();
        }
    );
    
    /*绑定模块动作*/
    $("#bindMkdz").click(
        function(e){
            $("#hidAction").val("bindright");
            getLoadTypeAndModule();
        }
    );
    
    /*绑定接口用户*/
    $("#bindJrzh").click(
        function(e){
            $("#hidAction").val("bindactor");
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
            var serviceId = $("#hidServiceId").val();
            if($.trim(serviceId) == ""){
                return;
            };
            loadModule("initmodify",serviceId);
            break;
        case "view" : 
            var serviceId = $("#hidServiceId").val();
            if($.trim(serviceId) == ""){
                return;
            };
            loadModule("view",serviceId);
            break;
        case "bindactor" : 
            var serviceId = $("#hidServiceId").val();
            loadModule("initbindactor",serviceId);
            break;
        case "bindright" : 
            var serviceId = $("#hidServiceId").val();
            loadModule("initbindright",serviceId);
            break;         
    };
}

//载入模块
function loadModule(servletAction,serviceId){
	var url = "m/service/" + servletAction;
    var data = {serviceID : serviceId?serviceId : ""};
	$.ajaxLoadPage("mainForm", url, data);
}
