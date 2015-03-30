$(initPage);

//初始化页面
function initPage(){
	
	/**
	//添加权限约束
	if(action.toLowerCase() == "modify"){
	    $.handleRights({
	        "btnSave" : $.SysConstants.MODIFY
	    });
	} else {
		$.handleRights({
	        "btnSave" : $.SysConstants.ADD
	    });
	}
	**/	 
	
	loadFlow();	
}


//加载工作流
function loadFlow(){
	
	var queryString = "&flowKey=" +  flowKey; 
	var url = flowType.toLowerCase() == "draft" ? "m/flow_draft":"m/flow_man";
	$.ajax({
		async:false,
		type:"POST",
		data:"act=flowxml" + queryString,
		url:url,
		success : loadFlowSuccess,
		error : $.ermpAjaxError
	});	
}


//加载工作流成功
function loadFlowSuccess(xml){
	try{
		var message = $("message",xml);
	    if(typeof(message[0]) != "undefined"){
	    	alert("查看流程详情失败！原因：" + $("message",xml).text());
			return false;
	    }
		
		new EAPP.Workflow.Graphic.ViewFlow(xml,"workspace"); 
  		return true;
  	}catch(e){
  		alert("加载流程失败！");
 
	}
	
	alert("加载流程失败！原因：" + $("message",xml).text());
	return false;
}

