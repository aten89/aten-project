//公用的初始化事件下拉动作
function initEventActionSel(selDivId, dataEvents, eventKey, defaultVal, isTaskEvent) {
	var actionSel = $("#" + selDivId).ySelect({width: $("#" + selDivId).parent().width() - 29,
		json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=ACTION" + (isTaskEvent ? "_TASKEVENT" : "") + "&flowClass=" + flowInfoData.category, 
		onChange:function(val,name){
			dataEvents[eventKey] = val;
		},
		afterLoad:function() {
			actionSel.addOption("", "请选择...", 0);
			if(dataEvents[eventKey]) {
				actionSel.select(dataEvents[eventKey]);
			} else if (defaultVal) {
				actionSel.select(defaultVal);
				dataEvents[eventKey] = actionSel.getValue();
			}
		}
	});
}

var expDialogWin = null;
function closeExpWin(expStr, inputId) {
	$("#" + inputId).val(expStr).change().focus();
	if (expDialogWin){
		expDialogWin.close();
	}
}
//公用的弹出编辑表达式
function editExpression(expInputId, returnType) {
	param = new Object();
	param.callback = closeExpWin;
	param.expInputId = expInputId;
	param.returnType = returnType;
	param.metas = flowInfoData.metas;
	window.dialogParam = param;
	var title = "编辑表达式";
	var url = BASE_PATH + "page/flowmanage/draft/attrview/dialog_exp.jsp";
	
	expDialogWin = $.showModalDialog(title, url, 530, 240);
}

//初始化流程信息
function initFlowInfo(data) {
	$("#attr_name").val(data.title).change(function(){
		if ($.validInput("attr_name", "流程名称", true)) {
			$("#attr_name").val(data.title);//还原旧值
			return;
		}
		data.title = $("#attr_name").val();
		flowEditObj.setTitle(data.title);//流程信息的data在外部在同步在gooflow里
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_flowStartEvent", data.events, "FLOW_START");
	initEventActionSel("attr_flowEndEvent", data.events, "FLOW_END");
}

//初始化开始节点
function initStartInfo(data, eleId, eleType) {
	$("#attr_name").text(data.name);//只读
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化结束节点
function initEndInfo(data, eleId, eleType) {
	$("#attr_name").text(data.name);//只读
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	
	//默认选中
	var exeType = data.action ? "action" : "exp";
//	$("input[type=radio][value='" + exeType + "']","#exeTypeTD ").click();
	$("input[type=radio]","#endFlowTD ").change(function(){
		data.endFlow = $(this).val();
	});
	if (data.endFlow) {
		$("input[type=radio][value='" + data.endFlow + "']","#endFlowTD ").click();
	} else {
		//第一次未设置时，保存默认值
		data.endFlow = $("input:checked", "#endFlowTD").val();
	}
	
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
}

//初始化机器节点
function initNodeInfo(data, eleId, eleType) {
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});

	var actionSel = $("#attr_action").ySelect({width: $("#attr_action").parent().width() - 29,
		json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=ACTION&flowClass=" + flowInfoData.category, 
		onChange:function(val,name){
			data.action = val;
		},
		afterLoad:function() {
			if(data.action) {
				actionSel.select(data.action);
			} else {
				actionSel.select(0);
				data.action = actionSel.getValue();
			}
		}
	});
	
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化分支节点
function initForkInfo(data, eleId, eleType) {
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	
	//读取该分支出去的线名
	var lineData = flowEditObj.$lineData;
	var html = "";
	if (lineData) {
		$.each(lineData, function(k, v) {
			if (v.from == eleId) {
				html +="<div>" + k + "**" + v.name + "</div>"
			}
		});
	}
	var toLineSel = $("#attr_toLineName").html(html).ySelect({width: $("#attr_toLineName").parent().width() - 29, onChange:function(val,name){
		$("#attr_toLineExpression").val(data.conditions[val]);
	}});
	if (!data.conditions) {
		data.conditions = {};
	}
	$("#attr_toLineExpression").change(function(){
		if (toLineSel.getValue()) {
			data.conditions[toLineSel.getValue()] = $(this).val();
		} else {
			$.alert("请选择分支线名");
		}
	});
	
	
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化联合节点
function initJoinInfo(data, eleId, eleType) {
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化子流程
function initComplexInfo(data, eleId, eleType) {
	var initialSel = $("#attr_subFlowInitial").ySelect({width: $("#attr_subFlowInitial").parent().width() - 29, onChange:function(val,name){
		data.subFlowInitial = val;
	}});
	if (data.subFlowInitial) {
		initialSel.select(data.subFlowInitial);
	} else {
		//第一次未设置时，保存默认值
		data.subFlowInitial = initialSel.getValue();
	}
	
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_subFlowName").val(data.subFlowName).change(function(){
		if ($.validInput("attr_subFlowName", "子流程名称", true)) {
			$("#attr_subFlowName").val(data.subFlowName);//还原旧值
			return;
		}
		data.subFlowName = $("#attr_subFlowName").val();
	});
	$("#attr_subFlowVersion").val(data.subFlowVersion).change(function(){
		data.subFlowVersion = $("#attr_subFlowVersion").val();
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化判断节点
function initChatInfo(data, eleId, eleType) {
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	
	//默认选中
	var exeType = data.action ? "action" : "exp";
	$("input[type=radio][value='" + exeType + "']","#exeTypeTD ").click();
	
	$("#attr_expression").val(data.expression).change(function(){
		if ("exp" == $("input:checked", "#exeTypeTD").val()) {//选表达式时有效
			if ($.validInput("attr_expression", "执行表达式", true)) {
				$("#attr_expression").val(data.expression);//还原旧值
				return;
			}
			data.expression = $("#attr_expression").val();
			data.action = "";
		} else {
			data.expression = "";
		}
	});

	var actionSel = $("#attr_action").ySelect({width: $("#attr_name").parent().width() - 29,
		json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=DECISION&flowClass=" + flowInfoData.category, 
		onChange:function(val,name){
			if ("action" == $("input:checked", "#exeTypeTD").val()) {//选程序类时有效
				data.action = val;
				data.expression = "";
			} else {
				data.action = "";
			}
		},
		afterLoad:function() {
			if(data.action) {
				actionSel.select(data.action);
			}
		}
	});
	
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_nodeEnterEvent", data.events, "NODE_ENTER");
	initEventActionSel("attr_nodeLeaveEvent", data.events, "NODE_LEAVE");
}

//初始化任务节点
function initTaskInfo(data, eleId, eleType) {
	//基本信息
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	
	//任务授权
	var assignTypeSel = $("#attr_assignType").ySelect({width: $("#attr_assignType").parent().width() - 29, onChange:function(val,name){
		if (data.assignType != val) {
			//清除原值
			data.assignAction = "";
			data.assignExpression = "";
			
			data.assignType= val;//前端记录，后台解析时不需要
		}
		
		//隐藏其它编辑框
		$("#TR_ACTORS, #TR_ROLES, #TR_EXP, #TR_ACTION").hide();
		$("#TR_" + val).show();
	}});
	if (data.assignType) {
		assignTypeSel.select(data.assignType);
	} else {
		//第一次未设置时，保存默认值
		data.assignType = assignTypeSel.getValue();
	}
	
	if (data.assignExpression) {//表达式值不为空时，默认拆解显示
		if (data.assignType == "ACTORS") {
			$("#attr_assignActors").val(data.assignExpression.substring(8, data.assignExpression.length - 1));//赋值
		} else if (data.assignType == "ROLES") {
			$("#attr_assignRoles").val(data.assignExpression.substring(7, data.assignExpression.length - 1));//赋值
		} else if (data.assignType == "EXP") {
			$("#attr_assignExpression").val(data.assignExpression);//赋值，不处理
		}
	}
	
	$("#attr_assignActors").change(function(){
		if ("ACTORS" == assignTypeSel.getValue()) {
			if ($.validInput("attr_assignActors", "用户帐号", true)) {
				$("#attr_assignActors").val(data.assignExpression.substring(8, data.assignExpression.length - 1));//还原旧值
				return;
			}
			data.assignExpression = "\"ACTORS:" + $("#attr_assignActors").val() +"\"";
			data.assignAction = "";
		} else {
			data.assignExpression = "";
		}
	});
	
	$("#attr_assignRoles").change(function(){
		if ("ROLES" == assignTypeSel.getValue()) {
			if ($.validInput("attr_assignRoles", "角色名称", true)) {
				$("#attr_assignRoles").val(data.assignExpression.substring(7, data.assignExpression.length - 1));//还原旧值
				return;
			}
			data.assignExpression = "\"ROLES:" + $("#attr_assignRoles").val() +"\"";
			data.assignAction = "";
		} else {
			data.assignExpression = "";
		}
	});
	$("#attr_assignExpression").change(function(){
		if ("EXP" == assignTypeSel.getValue()) {
			if ($.validInput("attr_assignExpression", "执行表达式", true)) {
				$("#attr_assignExpression").val(data.assignExpression);//还原旧值
				return;
			}
			data.assignExpression = $("#attr_assignExpression").val();
			data.assignAction = "";
		} else {
			data.assignExpression = "";
		}
	});
	
	
	var removeRole = false;
	//任务类型
	var multiTypeSel = $("#attr_multiType").ySelect({width: $("#attr_multiType").parent().width() - 29, onChange:function(val,name){
		if (data.multiType != val) {
			//类型改变时清空授权数据
			data.assignAction = "";
			data.assignExpression = "";
		}
		//记录前前值
		data.multiType = val;
		
		//单任务时才有角色授权
		if("single" == val) {
			if (removeRole) {//防止多次添加
				assignTypeSel.addOption("ROLES", "角色", 1);
			}
			removeRole = false;
		} else {
			assignTypeSel.removeOption("ROLES");
			removeRole = true;
		}
		
		
		//加载不同类型的授权动作
		var assignActionSel = $("#attr_assignAction").ySelect({width: $("#attr_name").parent().width() - 29,
			json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=" + (removeRole ? "MUTIASSIGN" : "ASSIGN") + "&flowClass=" + flowInfoData.category, 
			onChange:function(val,name){
				if ("ACTION" == assignTypeSel.getValue()) {
					data.assignAction = val;
					data.assignExpression = "";
				} else {
					data.assignAction = "";
				}
			},
			afterLoad:function() {
				if(data.assignAction) {
					assignActionSel.select(data.assignAction);
				}
			}
		});
	}});
	if (data.multiType) {
		multiTypeSel.select(data.multiType);
	} else {
		//第一次未设置时，保存默认值
		multiTypeSel.select(0);
		data.multiType = multiTypeSel.getValue();
	}
	
	

	//任务表单
	var viewType = data.viewAction ? "action" : "exp";
	$("input[type=radio][value='" + viewType + "']","#viewTypeTD ").click();
	$("#attr_viewExpression").val(data.viewExpression).change(function(){
		if ("exp" == $("input:checked", "#viewTypeTD").val()) {
			if ($.validInput("attr_viewExpression", "执行表达式", true)) {
				$("#attr_viewExpression").val(data.viewExpression);//还原旧值
				return;
			}
			data.viewExpression = $("#attr_viewExpression").val();
			data.viewAction = "";
		} else {
			data.viewExpression = "";
		}
	});

	var viewActionSel = $("#attr_viewAction").ySelect({width: $("#attr_name").parent().width() - 29,
		json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=VIEW&flowClass=" + flowInfoData.category, 
		onChange:function(val,name){
			if ("action" == $("input:checked", "#viewTypeTD").val()) {//选程序类时有效
				data.viewAction = val;
				data.viewExpression = "";
			} else {
				data.viewAction = "";
			}
		},
		afterLoad:function() {
			if(data.viewAction) {
				viewActionSel.select(data.viewAction);
			}
		}
	});
	
	//事件监听
	if (!data.events) {
		data.events = {};
	}
	initEventActionSel("attr_taskCreateEvent", data.events, "TASK_CREATE", "org.eapp.flow.handler.task.TaskCreateEvent", true);
	initEventActionSel("attr_taskNotifyEvent", data.events, "TASK_NOTIFY", "org.eapp.flow.handler.task.TaskNotifyEvent", true);
	initEventActionSel("attr_taskAssignEvent", data.events, "TASK_ASSIGN", "org.eapp.flow.handler.task.TaskAssignEvent", true);
	initEventActionSel("attr_taskStartEvent", data.events, "TASK_START", "org.eapp.flow.handler.task.TaskStartEvent", true);
	initEventActionSel("attr_taskGiveupEvent", data.events, "TASK_GIVEUP", "org.eapp.flow.handler.task.TaskGiveUpEvent", true);
	initEventActionSel("attr_taskEndEvent", data.events, "TASK_END", "org.eapp.flow.handler.task.TaskEndEvent", true);
}

//初始化路径
function initLineInfo(data, eleId, eleType) {
	$("#attr_name").val(data.name).change(function(){
		if ($.validInput("attr_name", "节点名称", true)) {
			$("#attr_name").val(data.name);//还原旧值
			return;
		}
		flowEditObj.setName(eleId, $("#attr_name").val(), eleType);
	});
	$("#attr_description").val(data.description).change(function(){
		data.description = $("#attr_description").val();
	});
	
	var actionSel = $("#attr_action").ySelect({width: $("#attr_action").parent().width() - 29,
		json:true,url : BASE_PATH + "m/flow_handler/loadhandsel?type=ACTION&flowClass=" + flowInfoData.category, 
		onChange:function(val,name){
			data.action = val;
		},
		afterLoad:function() {
			actionSel.addOption("", "请选择...", 0);
			if(data.action) {
				actionSel.select(data.action);
			}
		}
	});
	
	$("#attr_condition").val(data.condition).change(function(){
		data.condition = $("#attr_condition").val();
	});
}
