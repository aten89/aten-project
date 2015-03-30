var currentTab = null;
var vaildSelect;
$(initServiceManage);

function initServiceManage(){
	currentTab = $.getMainFrame().getCurrentTab();
    
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    }
	
    switch(action){
        case "add" : 
            currentTab.setTitle("新增服务配置", "新增服务服务");
            $("#addFwpz").attr("disabled","true").addClass("icoNone");
            $("#modFwpz").attr("disabled","true").addClass("icoNone");
            $("#bindJrzh").attr("disabled","true").addClass("icoNone");
            $("#bindMkdz").attr("disabled","true").addClass("icoNone");
            $("#saveService").removeAttr("disabled").removeClass("icoNone");
            $("#resetService").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddService").removeAttr("disabled").removeClass("icoNone");
            $("#serviceName").removeAttr("readonly");
            vaildSelect = $("#isValid").ySelect({width:95});
            vaildSelect.select(0);
            $("#description").removeAttr("readonly");
            //重写重置按扭
    		$("#resetService").click(function(){
    			document.mainForm.reset();
    			vaildSelect.select(0);
    			return false;
    		});
            bindEvent();
            break;
        case "modify" : 
        	currentTab.setTitle("修改服务配置",$("#serviceName").val());
        	$("#addFwpz").removeAttr("disabled").removeClass("icoNone");
            $("#modFwpz").attr("disabled","true").addClass("icoNone");
            $("#saveService").removeAttr("disabled").removeClass("icoNone");
            $("#resetService").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddService").attr("disabled","true").addClass("icoNone");
            $("#hidServiceName").val($("#serviceName").val());
            $("#serviceName").removeAttr("readonly");
            vaildSelect = $("#isValid").ySelect({width:95});
            vaildSelect.select($("#hidisValid").val());
            $("#description").removeAttr("readonly");
            if($("#hidisValid").val()=='N'){
            	$("#bindJrzh").attr("disabled","true").addClass("icoNone");
            	$("#bindMkdz").attr("disabled","true").addClass("icoNone");
            }else{
            	$("#bindJrzh").removeAttr("disabled").removeClass("icoNone");
            	$("#bindMkdz").removeAttr("disabled").removeClass("icoNone");
            };
            //重写重置按扭
    		$("#resetService").click(function(){
    			document.mainForm.reset();
    			vaildSelect.select($("#hidisValid").val());
    			return false;
    		});
            bindEvent();
            break;
        case "view" : 
        	currentTab.setTitle("查看服务配置",$("#serviceName").val());
        	$("#addFwpz").removeAttr("disabled").removeClass("icoNone");
            $("#modFwpz").removeAttr("disabled").removeClass("icoNone");
            $("#saveService,#resetService,#saveAddService").remove();
            $("#hidServiceName").val($("#serviceName").val());
//            $("#isValid").ySelect({width:95,isdisabled:true}).select($("#hidisValid").val());
            if($("#hidisValid").val()=='N'){
            	$("#bindJrzh").attr("disabled","true").addClass("icoNone");
            	$("#bindMkdz").attr("disabled","true").addClass("icoNone");
            }else{
            	$("#bindJrzh").removeAttr("disabled").removeClass("icoNone");
            	$("#bindMkdz").removeAttr("disabled").removeClass("icoNone");
            };
            
            $("#serviceName").parent().text($("#serviceName").val());
            $("#description").parent().text($("#description").val());
            $("#isValid").parent().text($("#hidisValid").val()=='Y' ? '有效':'无效');
            break;    
    };
}

//绑定事件
function bindEvent(){
	//保存
    $("#saveService").click(function(){
        saveServiceInfo(false);
    });
    //保存并新增
    $("#saveAddService").click(function(){
        saveServiceInfo(true);
    });
}

//保存信息
function saveServiceInfo(saveAndAdd){
	if($.validInput("serviceName", "服务名称", true)){
		return false;
	}

	if($.validInput("description", "服务描述", false, null, 500)){
		return false;
	}

	$.ajax({
	   type : "POST",
	   cache: false,
	   url  : "m/service/"+$("#hidAction").val(),
	   dataType : "json",
	   data : "serviceID=" + $("#serviceID").val()
	   			+"&serviceName="+$.trim($("#serviceName").val())
	   			+"&isValid="+vaildSelect.getValue()
	   			+"&description="+$.trim($("#description").val()),
      success : function(data){
			if ($.checkErrorMsg(data) ) {
				$.alert("保存成功");
		    	 //刷新父列表
               	currentTab.doCallback();
		    	//进行后续处理
		        if(saveAndAdd){
		        	//保存并新增
		            $("#addFwpz").removeAttr("disabled").click();
		        }else{
		        	//如果是新增，保存之后应转为修改
		        	var action = $("#hidAction").val();
		        	if (action == "add"){
		        		$("#hidServiceId").val(data.msg.text);
		        	}
		        	$("#hidAction").val("view");
		        	getLoadTypeAndModule();
		        };
			}
		}
	});
}