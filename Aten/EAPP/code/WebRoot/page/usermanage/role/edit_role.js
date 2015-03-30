var currentTab = null;
var vaildSelect;
$(initRBACRoleManage);

function initRBACRoleManage(){
	currentTab = $.getMainFrame().getCurrentTab();
	
    
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    }
    
    switch(action){
        case "add" : 
            currentTab.setTitle("新增角色", "新增角色");
            vaildSelect = $("#isValid").ySelect({width:95});
            vaildSelect.select(0);
            $("#addRBACRole").attr("disabled","true").addClass("icoNone");
            $("#modifyRBACRole").attr("disabled","true").addClass("icoNone");
            $("#bindRBACRoleGroup").attr("disabled","true").addClass("icoNone");
            $("#bindRBACRoleUser").attr("disabled","true").addClass("icoNone");
            $("#bindRBACRoleModule").attr("disabled","true").addClass("icoNone");
            
            $("#saveRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#resetRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#saveRBACRoleAndAdd").removeAttr("disabled").removeClass("icoNone");
            
            $("#roleName").removeAttr("readonly");
            $("#description").removeAttr("readonly");
            //重写重置按扭
    		$("#resetRBACRole").click(function(){
    			document.contetnMain.reset();
    			vaildSelect.select(0);
    			return false;
    		});
            bindEvent();
            break;
        case "modify" : 
        	currentTab.setTitle("修改角色",$("#roleName").val());
        	vaildSelect = $("#isValid").ySelect({width:95});
            vaildSelect.select($("#hidisValid").val());
        	$("#addRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#modifyRBACRole").attr("disabled","true").addClass("icoNone");
            if($("#hidisValid").val()=='N'){
            	$("#bindRBACRoleGroup").attr("disabled","true").addClass("icoNone");
            	$("#bindRBACRoleUser").attr("disabled","true").addClass("icoNone");
            	$("#bindRBACRoleModule").attr("disabled","true").addClass("icoNone");
            }else{
            	$("#bindRBACRoleGroup").removeAttr("disabled").removeClass("icoNone");
            	$("#bindRBACRoleUser").removeAttr("disabled").removeClass("icoNone");
            	$("#bindRBACRoleModule").removeAttr("disabled").removeClass("icoNone");
            };
            
            $("#saveRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#resetRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#saveRBACRoleAndAdd").attr("disabled","true").addClass("icoNone");
            
            $("#hidRoleName").val($("#roleName").val());
            $("#roleName").removeAttr("readonly");
            $("#description").removeAttr("readonly");
            
            //重写重置按扭
    		$("#resetRBACRole").click(function(){
    			document.contetnMain.reset();
    			vaildSelect.select($("#hidisValid").val());
    			return false;
    		});
            bindEvent();
            break;
        case "view" : 
        	currentTab.setTitle("查看角色",$("#roleName").val());
 //       	$("#isValid").ySelect({width:95,isdisabled:true}).select($("#hidisValid").val());
        	$("#addRBACRole").removeAttr("disabled").removeClass("icoNone");
            $("#modifyRBACRole").removeAttr("disabled").removeClass("icoNone");
            if($("#hidisValid").val()=='N'){
            	$("#bindRBACRoleGroup").attr("disabled","true").addClass("icoNone");
            	$("#bindRBACRoleUser").attr("disabled","true").addClass("icoNone");
            	$("#bindRBACRoleModule").attr("disabled","true").addClass("icoNone");
            }else{
            	$("#bindRBACRoleGroup").removeAttr("disabled").removeClass("icoNone");
            	$("#bindRBACRoleUser").removeAttr("disabled").removeClass("icoNone");
            	$("#bindRBACRoleModule").removeAttr("disabled").removeClass("icoNone");
            };
            
            $("#saveRBACRole,#resetRBACRole,#saveRBACRoleAndAdd").remove();
            
            $("#hidRoleName").val($("#roleName").val());
            
             $("#roleName").parent().text($("#roleName").val());
            $("#description").parent().text($("#description").val());
            $("#isValid").parent().text($("#hidisValid").val()=='Y' ? '有效':'无效');
            break;    
    };
}

//绑定事件
function bindEvent(){
	//保存
    $("#saveRBACRole").click(function(){
        saveRBACRoleInfo(false);
    });
    //保存并新增
    $("#saveRBACRoleAndAdd").click(function(){
        saveRBACRoleInfo(true);
    });
};

//保存信息
function saveRBACRoleInfo(saveAndAdd){
	if($.validInput("roleName", "角色名称", true)){
		return false;
	}
	if($.validInput("description", "角色描述", false, null, 500)){
		return false;
	}
    
     var roleName = $.trim($("#roleName").val());
     var roleVaild = $.trim(vaildSelect.getValue());
     var roleDescription = $.trim($("#description").val());
     var action = $("#hidAction").val();
     var roleId = $("#roleID").val();
	$.ajax({
	   type : "POST",
	   cache: false,
       url  : "m/rbac_role/" + action,
       dataType : "json",
	   data : {
              roleID : roleId,
              roleName : roleName,
              isValid : roleVaild,
              description : roleDescription
          },
      success : function(data){
	      	if ($.checkErrorMsg(data) ) {
	      		$.alert("角色保存成功");
		    	//刷新父列表
               	currentTab.doCallback();
		    	//进行后续处理
		        if(saveAndAdd){
		        	//保存并新增
		            $("#addRBACRole").removeAttr("disabled").click();
		        }else{
		        	//如果是新增，保存之后应转为修改
		        	if (action == "add"){
		        		$("#hidRoleId").val(data.msg.text);
		        	}
		        	$("#hidAction").val("view");
		        	getLoadTypeAndModule();
		        };
	      	}
		}
	});
}