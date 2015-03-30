$(initUserAccountFrame);

function initUserAccountFrame(){
//    $.handleRights({
//        "addUserAcc" : $.SysConstants.ADD,
//        "modUserAcc" : $.SysConstants.DELETE,
//        "bindRole" : $.SysConstants.BIND_ROLE,
//        "bindOrg" : $.SysConstants.BIND_GROUP,
//        "resetPwd" : $.SysConstants.SET_PASSWORD
//    });
    
	//增加
    $("#addUserAcc").click(
        function(e){
            $("#hidAction").val("add");
            $("#hidAccountId").val("");
            getLoadTypeAndModule();
        }
    );
    
    //修改
    $("#modUserAcc").click(
        function(e){
            $("#hidAction").val("modify");
            getLoadTypeAndModule();
        }
    );
    
    //查看
    $("#viewUserAcc").click(
        function(e){
            $("#hidAction").val("view");
            getLoadTypeAndModule();
        }
    );    
    
	/*绑定角色*/
	$("#bindRole").click(
		function(){		
			$("#hidAction").val("initbindrole");
			getLoadTypeAndModule();
		}				
	);    

	/*绑定机构*/
	$("#bindOrg").click(
		function(){
			$("#hidAction").val("initbindgroup");
			getLoadTypeAndModule();
		}	
	);
	
    /*绑定重置密码*/
    $("#resetPwd").click(function(){
    	$.confirm("是否重置该用户密码?", function(r){
			if (r) {
				$("#hidAction").val("setpassword");
            	getLoadTypeAndModule();
			}
		});
	});	
	
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
        case "initbindrole":
        	loadModule("initbindrole","");
        	break;
        case "initbindgroup":
        	loadModule("initbindgroup","");
        	break;
        case "setpassword":
            var accountId = $("#hidAccountId").val();  
            if($.trim(accountId) == ""){
                return;
            };      
            resetPassword(accountId);
            break;         	         	       	
    };
};

//载入模块
function loadModule(servletAction,accountId){
	var url = "m/user_account/" + servletAction;
    var data = {accountID : accountId?accountId : ""};
	$.ajaxLoadPage("mainForm", url, data);
}

//重置密码
function resetPassword(accountId){
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/user_account/setpassword",
        dataType : "json",
        data :{accountID : accountId?accountId : ""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		$.alert("密码重置成功!");        
        	}
        }
    });
};