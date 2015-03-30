$(initActorAccountFrame);

function initActorAccountFrame(){
//    $.handleRights(
//        {
//            "addActorAcc" : $.SysConstants.ADD,
//            "modActorAcc" : $.SysConstants.MODIFY,
//            "bindService" : $.SysConstants.BIND_SERVICE
//        }
//    );
    

	//增加
    $("#addActorAcc").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidAccountId").val("");
            getLoadTypeAndModule();
        }
    );
    
    //修改
    $("#modActorAcc").click(
        function(e){
            $("#hidAction").val("modify");
            getLoadTypeAndModule();
        }
    );
    
    //查看
    $("#viewActorAcc").click(
        function(e){
            $("#hidAction").val("view");
            getLoadTypeAndModule();
        }
    );    

	/*绑定服务*/
	$("#bindService").click(
		function(){
			$("#hidAction").val("initbindserver");
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
            var accountId = $("#hidAccountId").val();          
            if($.trim(accountId) == ""){
                return;
            };
            loadModule("initmodify",accountId);
            break;
        case "view" : 
            var accountId = $("#hidAccountId").val();
            if($.trim(accountId) == ""){
                return;
            };
            loadModule("view",accountId);
            break;
        case "initbindserver":
        	loadModule("initbindserver","");
        	break;      	
    };
};

//载入模块
function loadModule(servletAction,accountId){
	var url = "m/actor_account/" + servletAction;
    var data = {accountID : accountId?accountId : ""};
	$.ajaxLoadPage("mainForm", url, data);
}
