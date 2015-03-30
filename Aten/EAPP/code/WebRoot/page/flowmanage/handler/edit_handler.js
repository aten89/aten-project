var currentTab = null;
var flowClassSelect;
var typeSelect;
$(initActionManage);
function initActionManage(){
	currentTab = $.getMainFrame().getCurrentTab();
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    flowClassSelect = $("#flowClassDiv").ySelect({width: 130,json:true,async:false,url : "m/datadict/dictsel?dictType=FLOW_CLASS"});
    flowClassSelect.addOption("", "请选择...", 0);
    flowClassSelect.select($("#flowClass").val());
    
	typeSelect = $("#typeDiv").ySelect({width: 130});
	typeSelect.select($("#type").val());
    switch(action){
        case "add" : 
            currentTab.setTitle("新增处理程序", "创建新处理程序");
            $("#addAction,#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#name,#handlerClass").removeAttr("readonly");
            typeSelect.select(0);
            bindEvent();
            break;
        case "modify" : 
            currentTab.setTitle("修改处理程序",$("#name").val());
            $("#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#saveAddAction").removeAttr("disabled").removeClass("icoNone");
           	$("#name,#handlerClass").removeAttr("readonly");
            $("#saveAddAction").attr("disabled","true").addClass("icoNone");
            if ($("input:checked", "#globalFlagTD").val() =="true") {
            	flowClassSelect.disable(true);
            }
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看处理程序",$("#name").val());
            $("#addAction,#modifyAction").removeAttr("disabled").removeClass("icoNone");
            $("#saveAction,#resetAction,#saveAddAction").remove();
            
            $("#globalFlagTD").text(($("input:checked", "#globalFlagTD").val() =="true" ? "是" : "否"));
            $("#name").parent().text($("#name").val());
 			$("#handlerClass").parent().text($("#handlerClass").val());
 			$("#flowClassDiv").parent().text(flowClassSelect.getValue() == "" ? "" : flowClassSelect.getDisplayValue());
			$("#typeDiv").parent().text(typeSelect.getValue() == "" ? "" : typeSelect.getDisplayValue());
            break;
    };
    $("#mainForm").show();//页面处理完后再显示
}

//绑定事件
function bindEvent(){
    $("#saveAction").click(function(){
        	saveActionInfo(false);
        }
    );
    $("#saveAddAction").click(function(){
        	saveActionInfo(true);
        }
    );
    $("#globalTrue").click(function(){
        	flowClassSelect.select(0);
        	flowClassSelect.disable(true);
        }
    );
    $("#globalFalse").click(function(){
        	flowClassSelect.disable(false);
        }
    );
};


//保存信息
function saveActionInfo(saveAndAdd){
	if($.validInput("name", "显示名称", true)){
		return false;
	}
	
	if($.validInput("handlerClass", "程序类名", true)){
		return false;
	}
	var globalFlag = $("input:checked", "#globalFlagTD").val();
	
	if (globalFlag == "false" && flowClassSelect.getValue() == "") {
		$.alert("非全局变量的流程类别不能为空");
		return false;
	}
	
	$.ajax({
	   type : "POST",
	   cache: false, 
	   url  : "m/flow_handler/" + $("#hidAction").val(),
	   dataType : "json",
	   data : {handId : $("#handId").val(),
	   			flowClass : flowClassSelect.getValue(),
	   			name : $.trim($("#name").val()),
	   			handlerClass : $.trim($("#handlerClass").val()),
	   			type : typeSelect.getValue(),
	   			globalFlag : globalFlag
	   	},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
	             //刷新父列表
				currentTab.doCallback();
				$.alert("保存成功！", function(){
					 //保存成功时，进行下一步操作
					if(saveAndAdd){
						$("#addAction").removeAttr("disabled").click();
					}else{
			        	var action = $("#hidAction").val();
			        	currentTab.loadURL("m/flow_handler/initframe?act=view&handId=" + data.msg.text);
					}
				});
			}
		}
	});
}