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
            currentTab.setTitle("新增流程变量", "创建新流程变量");
            $("#addAction,#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#name,#displayName").removeAttr("readonly");
            typeSelect.select(0);
            bindEvent();
            break;
        case "modify" : 
            currentTab.setTitle("修改流程变量",$("#displayName").val());
            $("#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#saveAddAction").removeAttr("disabled").removeClass("icoNone");
           	$("#name,#displayName").removeAttr("readonly");
            $("#saveAddAction").attr("disabled","true").addClass("icoNone");
            if ($("input:checked", "#globalFlagTD").val() =="true") {
            	flowClassSelect.disable(true);
            }
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看流程变量",$("#displayName").val());
            $("#addAction,#modifyAction").removeAttr("disabled").removeClass("icoNone");
            $("#saveAction,#resetAction,#saveAddAction").remove();
            
            $("#notNullTD").text(($("input:checked", "#notNullTD").val() =="true" ? "是" : "否"));
            $("#globalFlagTD").text(($("input:checked", "#globalFlagTD").val() =="true" ? "是" : "否"));
            $("#name").parent().text($("#name").val());
 			$("#displayName").parent().text($("#displayName").val());
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
	if($.validInput("name", "变量名称", true)){
		return false;
	}
	
	if($.validInput("displayName", "显示名称", true)){
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
	   url  : "m/flow_var/" + $("#hidAction").val(),
	   dataType : "json",
	   data : {varId : $("#varId").val(),
	   			flowClass : flowClassSelect.getValue(),
	   			name : $.trim($("#name").val()),
	   			displayName : $.trim($("#displayName").val()),
	   			type : typeSelect.getValue(),
	   			notNull : $("input:checked", "#notNullTD").val(),
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
			        	currentTab.loadURL("m/flow_var/initframe?act=view&varId=" + data.msg.text);
					}
				});
			}
		}
	});
}