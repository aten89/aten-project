$(initGroupManage);

function initGroupManage(){
    //添加权限约束
	$.handleRights({
        "editAllAcc" : $.SysConstants.MODIFY
    },"hidModuleRights");
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
    
    var typeSelect;
	switch(action){
		case "view" : 
	        $("#editGroup").removeAttr("disabled").removeClass("icoNone");
	        
	        $("#saveGroup").remove();
	        
	        typeSelect = $("#businessType").ySelect({width:117,json:true, url:"m/group_ext/dictsel?dictType=BUSINESS_TYPES",
	        		afterLoad:function(){
	        			selectType();
	        			$("#businessType").parent().text(typeSelect.getDisplayValue());
	        		}
	        });
	        //标题
	        $('#titlName').html("—" + $("#groupname").val());
	        //保存页面值
	        $("#curGroupId").val($("#groupid").val());
	        $("#curGroupName").val($("#groupname").val());
	        $("#curBusinessType").val($("#hidBusinessType").val());
	        
	        $("#groupname").parent().text($("#groupname").val());
	        break;
        case "modify" :
            $("#editGroup").attr("disabled","true").addClass("icoNone");
            
            $("#saveGroup").removeAttr("disabled").removeClass("icoNone");
            
           	typeSelect = $("#businessType").ySelect({width:117,json:true, url:"m/group_ext/dictsel?dictType=BUSINESS_TYPES",afterLoad:selectType});
            
            bindEvent();     
            break;
    }
    
    function selectType() {
    	typeSelect.addOption("", "请选择...", 0);
    	var type = $.trim($("#hidBusinessType").val());
    	if (type != ""){
    		typeSelect.select(type);
    	}
    }

    //显示工具栏
    $('#allTool').show();
}

//绑定事件
function bindEvent(){
    $("#saveGroup").click(function(){
        	saveGroup();
        }
    );
}

//保存
function saveGroup(){
//	if($.validInput("typeHid", "类型", true)){
//		return false;
//	}
    
    var servletAction = $("#opType").val();
    
    var groupID = $('#groupid').val();
    var groupName = $.trim($('#groupname').val());
    var businessType = $('#typeHid').val();
	
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/group_ext/modify",
        dataType : "json",
        data : {
    		groupID : groupID,
    		groupName : groupName,
    		businessType : businessType?businessType:""},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
            	$.alert("机构业务类型保存成功!");
               {
	                $("li[groupid=\""+groupID+"\"] > span","#groupTree").text(groupName);                 
	                $("li[groupid=\""+groupID+"\"] > span","#groupTree").click();
                }                       
        	}
        }
    }); 
}
