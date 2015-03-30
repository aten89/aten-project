$(initModuleBind);

function initModuleBind(){
    $("#bindModuleAction,#saveBindActions").attr("disabled","true").addClass("icoNone");
    $("#addSubModule,#modifyModule,#delModule,#sortSubModule").removeAttr("disabled","true").removeClass("icoNone");
    
    //查询
    $("#searchAction").click(function(){
        getAction($.trim($("#txtFilterAction").val()));
    });
    //添加
    $("#btnAddAction").click(function(){
        addBind();
    }); 
    //删除
    $("#btnDelAction").click(function(){
        delBind();
    }); 
    //保存
    $("#saveBindActions").click(function(){
        saveBind();
    });
    
    getAction();
    //回车搜索
    $.EnterKeyPress($("#txtFilterAction"),$("#searchAction"));
}

//取动作
function getAction(actionName){
    $("#excludeActions").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/action/excacts",
        dataType : "json",
        data : {moduleID : $("#curModuleId").val(), name : actionName ? actionName : ""},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.actInfos;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
			            //已选的不列出
			            if ($("option[value=\""+dataList[i].actionID+"\"]","#includeActions").length == 0){
			                StrHtml+="<option value=\"" + dataList[i].actionID + "\">" + dataList[i].name + "</option>";
			            }
			        });
			        $("#excludeActions").html(StrHtml);   
        		}
        	}
        }
    });     
}

//添加绑定列表
function addBind(){
    $("option:selected","#excludeActions").each(function(){
        if ($("option[value='"+$(this).val()+"']","#includeActions").length == 0){ 
            $(this).appendTo($("#includeActions"));
        }else{
            $.alert("该动作已绑定!");
        }
    });
    $("#saveBindActions").removeAttr("disabled").removeClass("icoNone");
}

//删除绑定列表
function delBind(){
    $("option:selected","#includeActions").each(function(){
        if ($("option[value='"+$(this).val()+"']","#excludeActions").length == 0){ 
            $(this).appendTo($("#excludeActions"));
        }else{
            $(this).remove();
        }
    });
    $("#saveBindActions").removeAttr("disabled").removeClass("icoNone");
}

//保存绑定
function saveBind(){
    var opActions = "";
    $("#includeActions > option").each(function(){
		opActions += $(this).val() + ",";
	});          
	//去掉最后的,号
	opActions = opActions.length == 0?"" : opActions.substr(0,opActions.length - 1);
	
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/module/bindaction",
        dataType : "json",
        data : {
            moduleID : $("#curModuleId").val(),
            actionStr: opActions
        },
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
				$.alert("动作绑定成功!");  
		        $("#saveBindActions").attr("disabled","true").addClass("icoNone");
        	}
		}
    }); 
}