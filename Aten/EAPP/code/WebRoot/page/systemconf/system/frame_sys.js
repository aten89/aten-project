$(initSubSystemFrame);

function initSubSystemFrame(){
//    $.handleRights(
//        {
//            "addSubSystem" : $.SysConstants.ADD,
//            "modifySubSystem" : $.SysConstants.MODIFY
//        }
//    );
    
    
    $("#addSubSystem").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidSubSystemId").val("");
            getLoadTypeAndModule();
        }
    );
    
    $("#modifySubSystem").click(
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
            var subSystemId = $("#hidSubSystemId").val();
            if($.trim(subSystemId) == ""){
                return;
            };
            loadModule("initmodify",subSystemId);
            break;
        case "view" : 
            var subSystemId = $("#hidSubSystemId").val();
            if($.trim(subSystemId) == ""){
                return;
            };
            loadModule("view",subSystemId);
            break;
    };
};

//载入模块
function loadModule(servletAction,subSystemId){
	var url = "m/subsystem/" + servletAction;
    var data = {subSystemID : subSystemId ? subSystemId : ""};
	$.ajaxLoadPage("mainForm", url, data);
}
