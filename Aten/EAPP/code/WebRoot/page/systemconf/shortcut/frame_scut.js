$(initActionFrame);

function initActionFrame(){
//    $.handleRights(
//        {
//            "opAdd" : $.SysConstants.ADD,
//            "opModify" : $.SysConstants.MODIFY
//        }
//    );
    
    
    $("#opAdd").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidshortCutID").val("");
            getLoadTypeAndModule();
        }
    );
    
    $("#opModify").click(
        function(e){
            $("#hidAction").val("modify");
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
            var shortCutID = $("#hidshortCutID").val();
            if($.trim(shortCutID) == ""){
                return;
            };
            loadModule("initmodify",shortCutID);
            break;
        case "view" : 
            var shortCutID = $("#hidshortCutID").val();
            if($.trim(shortCutID) == ""){
                return;
            };
            loadModule("view",shortCutID);
            break;
    };
}

//载入模块
function loadModule(servletAction,shortCutID){
	var url = "m/shortcutmenu/" + servletAction;
    var data = {shortCutMenuID: shortCutID?shortCutID:""};
	$.ajaxLoadPage("mainForm", url, data);
}
