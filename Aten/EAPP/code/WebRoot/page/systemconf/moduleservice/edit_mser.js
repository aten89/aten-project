var isChange = false;
 var curModuleId;
$(initModuleActionList);

function initModuleActionList(){
    //$.getMainFrame().getCurrentTab().setTitle("设置-模块动作", "设置模块动作状态");
//    $.handleRights(
//        {
//            "saveModuleActions" : $.SysConstants.MODIFY
//        }
//    );
//    
    
    //document.getElementById("mkdzh").style.height=(document.documentElement.clientHeight - 170)+"px";
	$("#subSystemList").ySelect({
        width : 120, json:true,
        url: "m/subsystem/allsystem",
        onChange : subSystemList_change
    });
	
    //禁用按钮
    $("#saveModuleActions,#resetModuleActions").attr("disabled","true").addClass("icoNone");
    
    $("#resetModuleActions").click(
        function(e){
            $("#moduleActions > tbody").html($("#hidActionOptionsBak").val());
            setValidCheck();
            setRpcCheck();
            setHttpCheck();
        }        
    );
    
    $("#saveModuleActions").click(function(e){
            saveModuleActions();
	});
}

function saveModuleActions() {
	$.ajax({
        type : "POST",
        cache: false,
        async : true,
    	url  : "m/module_action/modify",
    	dataType : "json",
    	data: $("#mainForm").serialize(),
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$("#"+$("#hidModuleId").val()+"> span","#systemModulesTree").click();         		
                $.alert("保存成功");
        	}
            $("#saveModuleActions,#resetModuleActions").attr("disabled","true").addClass("icoNone");
            isChange = false;
        }
    });
}

function subSystemList_change(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/module/subsystemtree",
		dataType : "json",
		data : {subSystemID : value},
		success : function(data){
	        $("#systemModulesTree > ul").html(data.htmlValue);
	         $('#systemModulesTree').simpleTree({animate: true,json: true,afterClick : systemModulesTree_click});
	        //清空动作设置的内容
	        $("#moduleActions > tbody").empty();
		}
	});
};


function systemModulesTree_click(o){
    var moduleId = $(">span",o).attr("moduleId");
    if (curModuleId == moduleId) {
    	//重复点击
    	return;
    }
    curModuleId = moduleId;
    
    if (isChange) {
		$.confirm("是否先保存当前修改？", function(r){
    		if (r) {
    			saveModuleActions();
    		} 
    		loadModuleActions(moduleId);
    	});
		isChange=false;
	} else {
		loadModuleActions(moduleId);
	}
}


function loadModuleActions(moduleId) {    
    //清空动作设置的内容
    $("#moduleActions > tbody").empty();
    //设置ID
    $("#hidModuleId").val(moduleId);
    //禁用按钮
    $("#saveModuleActions,#resetModuleActions").attr("disabled","true").addClass("icoNone");
    //加载模块所绑定的动作
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
    	url  : "m/module_action/findmas",
    	dataType : "json",
    	data: {
            moduleID : moduleId
        },
        success :  function(data){
			if ($.checkErrorMsg(data) ) {
				var trHTML = "";
                if(data.moduleActionPage && data.moduleActionPage.dataList){
					var dataList = data.moduleActionPage.dataList;
					$(dataList).each(function(i) {
                        trHTML += "<TR>"
                                + "<TD>" + dataList[i].action.name + "</TD>" 
                                + "<TD><INPUT onclick=\"checkSingleValid();\" name=\"valid\" " + (dataList[i].isValid?"checked":"") + " value=\"" + dataList[i].moduleActionID + "\" type=\"checkbox\"></TD>"
                                + "<TD><INPUT onclick=\"checkSingleHttp();\" name=\"http\" " + (dataList[i].isHTTP?"checked":"") + " value=\"" + dataList[i].moduleActionID + "\" type=\"checkbox\"></TD>"
                                + "<TD><INPUT onclick=\"checkSingleRpc();\" name=\"rpc\" " + (dataList[i].isRPC?"checked":"") + " value=\"" + dataList[i].moduleActionID + "\" type=\"checkbox\"></TD>"
                                + "</TR>";
                    });
                }
                $("#moduleActions > tbody").html(trHTML);
			    //创建备份
			    $("#hidActionOptionsBak").val($("#moduleActions > tbody").html());               
                setValidCheck();
                setRpcCheck();
                setHttpCheck();
			}
        }
    });      
};

//全选Valid
function checkAllValid(){
    $("#moduleActions").find("input[name=valid]","tbody").attr("checked",$("#validcheckbox")[0].checked);
    $("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");  
    isChange = true;
}

//全选rpc
function checkAllRpc(){
    $("#moduleActions").find("input[name=rpc]","tbody").attr("checked",$("#rpccheckbox")[0].checked);
    $("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");
    isChange = true;
}

//全选http
function checkAllHttp(){
    $("#moduleActions").find("input[name=http]","tbody").attr("checked",$("#httpcheckbox")[0].checked);
    $("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");
    isChange = true;
}

//单选Valid
function checkSingleValid(){
	$("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");
    setValidCheck();	
    isChange = true;
}
//单选Rpc
function checkSingleRpc(){
    $("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");
    setRpcCheck();
    isChange = true;
}

//单选HTTP
function checkSingleHttp(){
    $("#saveModuleActions,#resetModuleActions").removeAttr("disabled").removeClass("icoNone");
    setHttpCheck();
    isChange = true;
}

//设置全选Valid状态
function setValidCheck(){
	//是否全没有选Valid
	if ($("#moduleActions").find("input[name=valid]:checked","tbody").length==0){
	    $("#validcheckbox")[0].checked=false;
	}//是否选满
	else if($("#moduleActions").find("input[name=valid]:not(:checked)","tbody").length==0){
	    $("#validcheckbox")[0].checked=true;
	}
}
//设置全选Rpc状态
function setRpcCheck(){
	//是否全没有选Rpc
	if ($("#moduleActions").find("input[name=rpc]:checked","tbody").length==0){
	    $("#rpccheckbox")[0].checked=false;
	}//是否选满Rpc
	else if ($("#moduleActions").find("input[name=rpc]:not(:checked)","tbody").length == 0){
	    $("#rpccheckbox")[0].checked=true;    
	}                                  
}

//设置全选HTTP状态
function setHttpCheck(){
	//是否全没有选Rpc
	if ($("#moduleActions").find("input[name=http]:checked","tbody").length==0){
	    $("#httpcheckbox")[0].checked=false;
	}//是否选满Rpc
	else if ($("#moduleActions").find("input[name=http]:not(:checked)","tbody").length == 0){
	    $("#httpcheckbox")[0].checked=true;    
	}                                  
}