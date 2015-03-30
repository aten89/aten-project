var mainFrame = $.getMainFrame();
var flowCategorySel = null;
var flowEditObj = null;
var noSaveFlag = false;//是否未保存
var flowKey = null;//为空是新增，不为空是保存

var flowInfoData = null;//流程基本信息

//窗口大小改变监听
window.onresize = reSizeEditArea;

$(initEditFlow);
function initEditFlow(){
	//页面传过来的参数
	flowKey = $("#flowKey").val();
	
	//加载流程类别
	flowCategorySel = $("#flowCategory").ySelect({width: 110,json:true,url : BASE_PATH + "m/datadict/dictsel?dictType=FLOW_CLASS"});
	
	if (flowKey) {//修改
		flowCategorySel.disable(true);	
		$("#startEditFlow").hide();
		//通过flowKey从后台加载
		loadFlowJsonData(flowKey);
	} else {//新增
		//开始按钮事件
		$("#startEditFlow").click(function() {
			var flowCategory = flowCategorySel.getValue();
			if(!flowCategory){
				$.alert("请选择流程类别！");
				return;
			}
			$.confirm("流程开始编辑后，流程类别将无法修改，确定开始编辑？", function(r){
				if (r) {
					flowCategorySel.disable(true);	
					$("#startEditFlow").hide();
					//默认初始化开始与结束结点
					flowInfoData = {category : flowCategory, description : "", metas : [], events : {}, title:"新建流程",nodes:{START_NODE:{name:"开始",left:300,top:50,type:"start round",width:30,height:30,alt:true},END_NODE:{name:"结束",left:300,top:300,type:"end round",width:30,height:30,alt:true,endFlow:"false"}},lines:{},areas:{}}
					//加载变量
					reloadFlowVars(true);
					//初始化流程设计器
					initGooFlow();
				}
			});
		});
	}
	
	
	try {
		//关闭提醒
		mainFrame.getCurrentTab().onClose = function() {
			//无未保存直接关闭
			if (!noSaveFlag) {
				return true;
			}
			$.confirm("流程还未保存，是否继续关闭？", function(r){
				if (r) {
					noSaveFlag = false;
					mainFrame.getCurrentTab().close();
				}
			});
			return !noSaveFlag;//有未保存返回FALSE不关闭，在弹出确认后再手动关闭
		};
		//刷新提醒
		mainFrame.getCurrentTab().onReload = function() {
			//无未保存直接刷
			if (!noSaveFlag) {
				return true;
			}
			$.confirm("流程还未保存，是否继续刷新？", function(r){
				if (r) {
					noSaveFlag = false;
					mainFrame.getCurrentTab().reload();
				}
			});
			return !noSaveFlag;//有未保存返回FALSE不关闭，在弹出确认后再手动关闭
		};
	} catch(e) {
	}
}

///窗口大小改变时重载流程编辑区域
function reSizeEditArea() {
	var wHeight = $(window).height();
	$("#attrEditDiv").height(wHeight-78);
	
	flowEditObj.reinitSize($("#flowEditDiv").parent().width(), wHeight - 53);
}

//加载流程的JSON数据
function loadFlowJsonData(flowKey) {
	// 通过flowKey从后台加载
	$.ajax({
		type : "POST",
		cache: false, 
		url  : BASE_PATH + "m/flow_draft/loadflowstr",
		dataType : "json",
		data : {flowKey : flowKey,
			draftFlag : 1},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				flowInfoData = eval('('+data.flowJson+')');
				//初始化流程设计器
				initGooFlow();
			}
        }
	});
//	return {category : "BXLC", description : "DESC", metas : [], events : {}, title:"编辑流程", nodes:{START_NODE:{name:"开始",left:300,top:50,type:"start round",width:30,height:30},END_NODE:{name:"结束",left:300,top:300,type:"end round",width:30,height:30},NODE1:{name:"节点1",left:300,top:200,type:"task",width:90,height:40}},lines:{},areas:{}};
}

//初始化流程设计器
function initGooFlow(){
	//禁用流程类别选择框与按钮
	flowCategorySel.select(flowInfoData.category);
	noSaveFlag = true;
	
	var wHeight = $(window).height();
	$("#attrEditDiv").height(wHeight-78).parent().show();
	
   	var property={
		width: $("#flowEditDiv").parent().width(),
		height: wHeight - 53,
		toolBtns:["task","node","chat rhombus","fork","join","complex"],
		haveHead:true,
		headBtns:["save","open","reload","del","undo","redo","expand","shrink"],
		haveTool:true,
		haveGroup:true,
		useOperStack:true
	};
	var remark={
		cursor:"选择",
		direct:"连接线",
		task:"任务节点",
		node:"机器节点",
		chat:"判断节点",
		fork:"分支节点",
		join:"联合节点",
		complex:"子流程",
		group:"分组管理",
	
		save:"保存",
		open:"打开流程数据",
		reload:"更新环境变量",
		del:"删除",
		undo:"撤销",
		redo:"重做",
		expand:"扩大工作区域",
		shrink:"缩小工作区域"
	};
	//创建对象
	flowEditObj=$.createGooFlow($("#flowEditDiv"),property);
	flowEditObj.setNodeRemarks(remark);
	//加载新增时默认数据
	flowEditObj.loadData(flowInfoData);

	//保存
	flowEditObj.onBtnSaveClick=saveFlow;
	//打开描述文本
	flowEditObj.onBtnOpenClick=openFlowJson;
	//更新环境变量
	flowEditObj.onFreshClick=reloadFlowVars;
	//删除事件
	flowEditObj.onItemDel=onDeleteItem;
	
	//点击标题事件
	flowEditObj.onClickTitle=function(title) {
		loadAttrView(flowInfoData);
		return true;
	};
	//选中事件
	flowEditObj.onItemFocus=function(id, type) {
		var info = flowEditObj.getItemInfo(id, type);
		loadAttrView(info, id, type);
		return true;
	};
	//失去焦点事件
	flowEditObj.onItemBlur=function(id, type) {
		loadAttrView(flowInfoData);//默认显示流程属性
		return true;
	};
	//默认加载流程属性编辑
	loadAttrView(flowInfoData);
}

//加载属性编辑视图
function loadAttrView(nodeObj, eleId, eleType) {
	var showDiv = $("#attrEditDiv");
	$("input, textarea", showDiv).blur();//加载新页面时，使上一节点所有输入框失去焦点，以防最后一个修改的内容不触发change事件
	//加载页面
	if (!eleId) {//eleId为空说明为流程信息
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_flow.jsp");
		initFlowInfo(nodeObj);
	} else if (eleType == "line") {//所有线的属性是一样的
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_line.jsp");
		initLineInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "start" || nodeObj.type.indexOf("start ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_start.jsp");
		initStartInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "end" || nodeObj.type.indexOf("end ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_end.jsp");
		initEndInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "node" || nodeObj.type.indexOf("node ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_node.jsp");
		initNodeInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "fork" || nodeObj.type.indexOf("fork ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_fork.jsp");
		initForkInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "join" || nodeObj.type.indexOf("join ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_join.jsp");
		initJoinInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "complex" || nodeObj.type.indexOf("complex ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_complex.jsp");
		initComplexInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "chat" || nodeObj.type.indexOf("chat ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_chat.jsp");
		initChatInfo(nodeObj, eleId, eleType);
	} else if (nodeObj.type == "task" || nodeObj.type.indexOf("task ") == 0) {
		$.ajaxLoadPage(showDiv, BASE_PATH + "page/flowmanage/draft/attrview/view_task.jsp");
		initTaskInfo(nodeObj, eleId, eleType);
	} else {
		$.alert("未知节点：" + nodeObj.type);
		return;
	}
}

//更新环境变量
function reloadFlowVars(noAlert) {
	//从后台加载流程类别的元数据
	$.ajax({
		type : "POST",
		cache: false, 
		url  : BASE_PATH + "m/flow_var/loadvars",
		dataType : "json",
		data : {flowClass : flowInfoData.category,
			draftFlag : 1},
		success : function(data){
			if ($.checkErrorMsg(data)) {
				var flowVars = data.flowVars;
                if (flowVars) {
                	flowInfoData.metas = [];
                	$(flowVars).each(function(i) {
                		flowInfoData.metas.push({name:flowVars[i].name, displayName:flowVars[i].displayName, type:flowVars[i].type, notNull:flowVars[i].notNull});
                	});
                }
                if (!noAlert) {
                	$.alert("环境变量更新成功");
                }
			}
//			$(flowInfoData.metas).each(function(i) {
//				alert(flowInfoData.metas[i].name + "--" + flowInfoData.metas[i].displayName + "--" + flowInfoData.metas[i].type + "--" + flowInfoData.metas[i].notNull);
//			});
        }
	});
}

var dialogWin = null;
/**
 * 关闭对话框
 */
function closeWin(jsonStr) {
	if (jsonStr) {
		flowInfoData = eval('('+jsonStr+')');
		//重新初始化流程设计器
		$("#flowEditDiv").empty();
		initGooFlow();
	}
	if (dialogWin){
		dialogWin.close();
	}
}

//弹跳出显示JSON数据
function openFlowJson() {
	param = new Object();
	param.callback = closeWin;
	param.jsonStr = getComitJsonStr();
	window.dialogParam = param;
	var title = "加载流程数据";
	var url = BASE_PATH + "page/flowmanage/draft/dialog_json.jsp";
	
	dialogWin = $.showModalDialog(title, url, 640, 360);
}

//flowInfoData对像合并到GOOFLOW的数据
function getComitJsonStr() {
	var expData = flowEditObj.exportData();
	expData.category = flowInfoData.category;
	expData.description = flowInfoData.description;
	expData.metas = flowInfoData.metas;
	expData.events = flowInfoData.events;
	return $.toJSON(expData);
}

//保存流程
function saveFlow() {
	flowKey = $("#flowKey").val();
	//提交
	$.ajax({
		type : "POST",
		cache: false, 
		url  : BASE_PATH + "m/flow_draft/" + (flowKey ? "modify" : "add"),
		dataType : "json",
		data : {flowKey : flowKey,
			flowJson: getComitJsonStr()},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				//刷新父列表
				$.getMainFrame().getCurrentTab().doCallback();
				
				if (!flowKey) {//为空是新增操作
					//flowKey赋值，下次再提交则为修改
					$("#flowKey").val(data.msg.text);
				}
                $.alert("保存存成功！");
                noSaveFlag = false;
			}
        }
	});
}

//节点或线删除事件
function onDeleteItem(id, type) {
	var info = flowEditObj.getItemInfo(id, type);
	if (info.type.indexOf("start ") == 0 || info.type.indexOf("end ") == 0) {
		$.alert("不能删除开始与结束节点");
		return false;
	}
	loadAttrView(flowInfoData);//默认显示流程属性
	
	//删除线时删除分支节点的条件表达式对应的线
	try {
		if(type == "line") {
			var nodeData = flowEditObj.$nodeData;
			if (nodeData) {
				$.each(nodeData, function(k, v) {
					if (v.conditions) {//有些属性说明是分支节点
						delete v.conditions[id];
					}
				});
			}
		}
	} catch(e){}
	
	return true;
}
	


