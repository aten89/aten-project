//调用初始化函数
$(initSubModuleManage);

//页面初始化
function initSubModuleManage(){
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
    
    switch(action){
        case "add" :             
            $("#addSubModule,#modifyModule,#delModule,#sortSubModule,#bindModuleAction").attr("disabled","true").addClass("icoNone");
            
            $("#saveModule,#resetModule,#saveAddModule").removeAttr("disabled").removeClass("icoNone");
            
            $("#moduleName,#moduleKey,#moduleUrl,#moduleRemark").removeAttr("readonly");
           	$("#showParentModule").attr("disabled","true").addClass("icoNone");

	    	//从外层框架取默认值
	    	$("#subSystemName").text($("#curSubSystemName").val());
	    	$("#hidSubSystemId").val($("#curSubSystemId").val());
	    	$("#hidParentModuleId").val($("#curModuleId").val());
	    	$("#parentModuleName").val($("#curModuleName").val());
	    	//重写重置按扭
    		$("#resetModule").click(function(){
    			document.moduleFrm.reset();
    			$("#subSystemName").text($("#curSubSystemName").val());
	    		$("#hidSubSystemId").val($("#curSubSystemId").val());
    			$("#hidParentModuleId").val($("#curModuleId").val());
	    		$("#parentModuleName").val($("#curModuleName").val());
    			return false;
    		});
            bindEvent();
            break;
        case "modify" :
            $("#addSubModule,#delModule,#sortSubModule,#bindModuleAction").removeAttr("disabled").removeClass("icoNone");
            $("#modifyModule").attr("disabled","true").addClass("icoNone");
            
            $("#saveModule,#resetModule").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddModule").attr("disabled","true").addClass("icoNone");
            
            $("#moduleName,#moduleKey,#moduleUrl,#moduleRemark").removeAttr("readonly");
           	$("#showParentModule").removeAttr("disabled").removeClass("icoNone");
            
            bindEvent();     
            break;
        case "view" :
            $("#addSubModule,#modifyModule,#delModule,#sortSubModule,#bindModuleAction").removeAttr("disabled").removeClass("icoNone");
            
            $("#saveModule,#resetModule,#saveAddModule").remove();
            
     //       $("#showParentModule").attr("disabled","true").addClass("icoNone");
            
            //点击都是时入查看页，查看状态时把相关字段保存到外导框架中
		    $('#titlName').html("—" + $("#moduleName").val());
		    $('#curModuleId').val($('#hidModuleId').val());
		    $('#curModuleName').val($('#moduleName').val());
		    
		    $("#parentModuleName").parent().text($("#parentModuleName").val());
 			$("#moduleName").parent().text($("#moduleName").val());
 			$("#moduleKey").parent().text($("#moduleKey").val());
 			$("#moduleUrl").parent().text($("#moduleUrl").val());
 			$("#moduleRemark").parent().text($("#moduleRemark").val());
 			$("#quoteModuleName").parent().text($("#quoteModuleName").val());
 			
            break;
    }
    
    //显示工具栏
    $('#manageToolBar').show();
}    

function bindEvent() {
	$("#saveModule").click(function(){
        	saveSubModule(false);
        }
    );
    $("#saveAddModule").click(function(){
        	saveSubModule(true);
        }
    );

    //上级模块绑定
    $('#showParentModule').click(function(){
    	var selector = new ModuleDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal) {
				$("#hidParentModuleId").val(retVal.id);
				$("#parentModuleName").val(retVal.name);
			}
		});
		selector.setSubSystemId($("#hidSubSystemId").val());
		selector.openDialog("single");
    });
    //引用模块绑定
    $('#showQuoteModule').click(function(){
    	var selector = new ModuleDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal) {
				$("#hidQuoteModuleId").val(retVal.id);
				$("#quoteModuleName").val(retVal.name);
			}
		});
		selector.openDialog("single");
    });
}

//保存子模块信息
function saveSubModule(saveAndAdd){
    if($.validInput("moduleName", "模块名称", true)){
		return false;
	}
	if($.validInput("moduleKey", "模块代码", true)){
		return false;
	}
	if($.validInput("moduleRemark", "备注", false, null, 500)){
		return false;
	}
	
    //模块名
	var moduleName = $.trim($("#moduleName").val());
    //模块地址
	var moduleUrl = $.trim($("#moduleUrl").val());
    //模块代码
	var moduleKey = $.trim($("#moduleKey").val());
    //模块备注
	var moduleRemark = $.trim($("#moduleRemark").val());
    //模块所属子系统的ID
	var subSystemId = $.trim($("#hidSubSystemId").val());
    //模块所属父模块的ID
	var parentModuleId = $.trim($("#hidParentModuleId").val());
    //模块的ID
    var moduleId = $.trim($("#hidModuleId").val());
    
    var quoteModuleId = $.trim($("#hidQuoteModuleId").val());
    //提交的动作类型
	var action = $("#opType").val();
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/module/" + action,
		dataType : "json",
		data : {
			subSystemID : subSystemId,
			parentModuleID : parentModuleId,
            moduleID : moduleId,
			name : moduleName,
			url : moduleUrl,
			moduleKey : moduleKey,
			description : moduleRemark,
			quoteModuleID : quoteModuleId
		},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var newGroupID= data.msg.text;
				$.alert("保存子模块成功！");
                if (action == "add") {
//                    var parentNode = $("#" + (parentModuleId == ""?subSystemId : parentModuleId), "#systemModules");
//                    childModulesTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" moduleKey=\"" + moduleKey + "\" moduleId=\"" + data.msg.text + "\">" 
//                    		+ "<span moduleId=" + data.msg.text + " moduleKey=\"" + moduleKey + "\" class='text'>" + moduleName + "</span></li>");
                  	childModulesTree.addNode(parentModuleId, newGroupID, moduleName, {moduleId :  newGroupID, moduleKey:moduleKey});
                	//判断是否进行下一步的新增操作
	                if(saveAndAdd){
	                    $("#addSubModule").removeAttr("disabled").click();
	                }else {
	                    //模拟树节点的点击事件
//	                    $("#" + data.msg.text + " > span").click();
	                    childModulesTree.clickNode(newGroupID);
	                }
                } else {
                	if ($("#hidParentModuleIdOld").val() != parentModuleId){
                		//刷新整个树
                		$("#subSystemList div > input[value='" + $("#curSubSystemId").val() + "']").parent().click();
                	} else {
                		//修改当前结点属性与名称，并点击
//                		$("#" + moduleId).attr({moduleId : moduleId,moduleKey : moduleKey})
//                    		.find(">span").attr({moduleId : moduleId,moduleKey : moduleKey}).text(moduleName).click();
                    	childModulesTree.modifyNode(moduleId, moduleName,{moduleId : moduleId,moduleKey : moduleKey});
                    	childModulesTree.clickNode(moduleId);
                	}
                    
                }
			}
		}
	});
}
