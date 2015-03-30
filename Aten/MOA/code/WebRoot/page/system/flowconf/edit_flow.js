var mainFrame = $.getMainFrame();

$(initPage);

//全局流程类别下拉框组件对象
var sysSelect = null;

//初始化页面
function initPage(){
	//添加权限约束
	if(action.toLowerCase() == "modify"){
	    $.handleRights({
	        "btnSave" : $.SysConstants.MODIFY
	    });
	} else {
		$.handleRights({
	        "btnSave" : $.SysConstants.ADD,
	        "btnCreateFlow" : $.SysConstants.ADD
	    });
	}
	    
	//初始禁用保存按钮
	$("#btnSave").attr("disabled","true");
	$("#btnSave").click(saveFlow);
	
	$("#btnUpdateMeta").attr("disabled","true");
	$("#btnUpdateMeta").click(updateMeta);
	
	$("#btnEncode").click(function(){
		var str = window.prompt("请输入汉字：","");
		if (str != null && str != ""){
			//清楚剪贴版内容
      		window.clipboardData.clearData("Text");   
   			//变量值写入剪贴板
   			window.clipboardData.setData("Text",encodeURI(str));
		}
	});
	
	//如果是修改动作，则加载工作流
	if(action.toLowerCase() == "modify"){
		initWorkflowDesigner();//初始化界面
		loadFlow();//加载流程
	}
	
	sysSelect = $("#flowCategory").ySelect({
		width: 110,
		url : "m/flow_draft?act=flow_class",
		afterLoad:function(){
			sysSelect.addOption("", "请选择...", 0);
			//为修改时，定位到当前流程的类别
			if(action.toLowerCase() == "modify"){
				sysSelect.select(gFlowCategory);
				sysSelect.disable(true);//禁用
			}
		}
	});
 

	
	$("#btnCreateFlow").click(function(){
		
		var flowClass = $("#flowClass").val();
		if(!flowClass){
			alert("请选择流程类别！");
			return false;
		}
		
		if(!confirm("友情提示：流程创建后，流程类别将无法修改！\n\t  按“确定”按钮继续！\n\t  按“取消”按钮重新选择流程类别！")){
			return false;
		}
		
		initWorkflowDesigner();
		
		//禁用流程类别选择框
		sysSelect.disable(true);		
		$("#btnCreateFlow").attr("disabled","true");
		$("#btnSave").removeAttr("disabled");
		$("#btnUpdateMeta").removeAttr("disabled");
		
	});
	
}

//工作流对象全局变量
var gWorkflow = null;
	
//初始化流程编辑器
function initWorkflowDesigner(){
	gWorkflow = new EAPP.Workflow.Factory().getInstance();
	gWorkflow.basePath = BASE_PATH + "commui/workflow/";
	
	if(action.toLowerCase() != "modify"){//在新建时，才从数据库读取meta
		//加载meta定义
		var flowCategory = $("#flowClass").val();
		var queryString = "&flowCategory=" + flowCategory;	
		$.ajax({
			async:false,
			type:"POST",
			data:"act=get_meta_xml" + queryString,
			url:"m/flow_draft",
			success : function(xml){
				gWorkflow.setMetadata(xml);
			},
			error : $.ermpAjaxError
		});	
	}
	
	//注册验证表达式的方法
	gWorkflow.validExpressionMethod = validExpression;
	gWorkflow.init($("#workspace"));
	
	mainFrame.getCurrentTab().onClose = isChange;

}


function isChange(){
	if(gWorkflow.hasChanged){
		var flag = confirm("警告：当前流程已修改，但未进行保存，建议点击“取消”进行保存!");
		if(flag){
			//取消关闭重复提醒
			gWorkflow.unloadWindow = function(){};
		}
		return flag;
	}
	return true;
}

function validExpression(metaXml){
	
	var flag = false;
	 
	$.ajax({
		async:false,
		type:"POST",
		data:{act:"valid_expression",metaXml: metaXml},
		url:"m/flow_draft",
		success : function(xml){
		    var messageCode = $("message",xml).attr("code");
			flag =messageCode == "1"?true:false; 
		},
		error : $.ermpAjaxError
	});	
	return flag;	
}

//加载工作流
function loadFlow(){
	
	var queryString = "&flowKey=" +  flowKey; 
	
	$.ajax({
		async:false,
		type:"POST",
		data:"act=flowxml" + queryString,
		url:"m/flow_draft",
		success : loadFlowSuccess,
		error : $.ermpAjaxError
	});	
}


//加载工作流成功
function loadFlowSuccess(xml){
	try{
		gWorkflow.parseXml(xml);
		
		$("#btnCreateFlow").attr("disabled","true");
		$("#btnSave").removeAttr("disabled");
		$("#btnUpdateMeta").removeAttr("disabled");
		
  		//alert("加载流程成功！");
  		return true;
  	}catch(e){
  		alert("加载流程失败！");
  		//alert("解析异常原因：" + e);
		//throw e;
	}
	
	alert("加载流程失败！原因：" + $("message",xml).text());
	return false;
}

//保存工作流
function saveFlow(){

	if(action.toLowerCase() == "add"){
		createFlowDraft();
	}else if(action.toLowerCase() == "modify"){
		modifyFlowDraft();
	}
}

//-----------------------------新建-----------------------------
//新建工作流程
function createFlowDraft(){

	var flowClass = $("#flowClass").val();
	if(!flowClass){
		alert("请选择流程类别！");
		return false;
	}
	
	var workflowXml = gWorkflow.saveAsXml();
	if(!workflowXml){return;}
	
 	var workData = {"workflowXml":workflowXml};
 	var queryString = "&flowClass=" + flowClass;
 	
	$.ajax({
		async:false,
		type:"POST",
		data:workData,
		url:"m/flow_draft?act=add" + queryString,
		success : createFlowDraftSuccess,
		error : $.ermpAjaxError
	});
	
	// 保存同步提交时，界面锁定
}

//新建工作流程成功
function createFlowDraftSuccess(xml){

	//解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    if(messageCode == "1"){
        alert("保存工作流程成功！");
        //把当前状态更正为修改状态
        action = "modify";
        flowKey = $("flow-key",xml).text();
		
		mainFrame.getCurrentTab().doCallback();
    }else {
    	alert("保存工作流程失败！原因：" + $("message",xml).text());
    }
    
    // 保存同步提交时，界面锁定
}
//-----------------------------新建 end-----------------------------


//-----------------------------修改----------------------------
//修改流程草稿
function modifyFlowDraft(){
	
	if(!flowKey || $.trim(flowKey) === ""){
		alert("参数传入出错！");
		return;
	}
	
	var flowClass = $("#flowClass").val();
	if(!flowClass){
		alert("保存出错！流程类别为空！");
		return false;
	}
	
	//获取流程定义的xml数据
	var workflowXml = gWorkflow.saveAsXml();
	if(!workflowXml){return;}	
 	var workData = {"workflowXml":workflowXml};
 	
 	var queryString = "&flowKey=" +  flowKey + "&flowClass=" + flowClass; 
	$.ajax({
		async:false,
		type:"POST",
		data:workData,
		url:"m/flow_draft?act=modify" + queryString,
		success : modifyFlowDraftSuccess,
		error : $.ermpAjaxError
	});
}


//修改流程草稿成功
function modifyFlowDraftSuccess(xml){
	//解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    if(messageCode == "1"){
        alert("保存工作流程成功！");
        
		mainFrame.getCurrentTab().doCallback();
    }else {
    	alert("保存工作流程失败！原因：" + $("message",xml).text());
    }
}
//-----------------------------修改 end-----------------------------

/**
 * 更新meta环境变量
 */
function updateMeta(){
	
	//加载meta定义
	var flowCategory = $("#flowClass").val();
	var queryString = "&flowCategory=" + flowCategory;	
	$.ajax({
		async:false,
		type:"POST",
		data:"act=get_meta_xml" + queryString,
		url:"m/flow_draft",
		success : function(xml){
			gWorkflow.setMetadata(xml);
			alert("操作成功");
		},
		error : $.ermpAjaxError
	});	
}

