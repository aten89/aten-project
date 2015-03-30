$(initPortletFrame);

function initPortletFrame(){
//    $.handleRights(
//        {
//            "addPortlet" : $.SysConstants.ADD,
//            "modifyPortlet" : $.SysConstants.MODIFY,
//			"bindRole" : $.SysConstants.BIND_ROLE
//        }
//    );
    
    
    $("#addPortlet").click(
        function(e){
            $("#hidAction").val("initadd");
            $("#hidPortletId").val("");
            getLoadTypeAndModule();
        }
    );
    
    $("#modifyPortlet").click(
        function(e){
            $("#hidAction").val("initmodify");
            getLoadTypeAndModule();
        }
    );
    
    $("#bindRole").click(
        function(e){
            $("#hidAction").val("initbindrole");
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
    
    loadModule(action,$("#hidPortletId").val());
};

//载入模块
function loadModule(servletAction,portletId){
	var url = "m/portlet/"  + servletAction;
    var data = {portletID : portletId?portletId:""};
	$.ajaxLoadPage("contetnMain", url, data);
};