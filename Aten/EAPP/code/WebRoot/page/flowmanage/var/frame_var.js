$(initActionFrame);

function initActionFrame(){
//    $.handleRights(
//        {
//            "addAction" : $.SysConstants.ADD,
//            "modifyAction" : $.SysConstants.MODIFY
//        }
//    );
    
    
    $("#addAction").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidActionId").val("");
            getLoadTypeAndModule();
        }
    );
    
    $("#modifyAction").click(
        function(e){
            $("#hidAction").val("modify");
            getLoadTypeAndModule();
        }
    );
    
    getLoadTypeAndModule();
};

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
            var actionid = $("#hidActionId").val();
            if($.trim(actionid) == ""){
                return;
            };
            loadModule("initmodify",actionid);
            break;
        case "view" : 
            var actionid = $("#hidActionId").val();
            if($.trim(actionid) == ""){
                return;
            };
            loadModule("view",actionid);
            break;
    };
};

//载入模块
function loadModule(servletAction,actionId){
	var url = "m/flow_var/" + servletAction;
    var data = {varId : actionId ? actionId : ""};
	$.ajaxLoadPage("mainForm", url, data);
};