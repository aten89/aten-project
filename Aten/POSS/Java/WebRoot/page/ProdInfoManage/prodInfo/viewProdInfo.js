var mainFrame = $.getMainFrame();
var dialogWin = null;
var param = null;


$(initProdInfo);
function initProdInfo(){
	//添加权限约束
	$.handleRights({
        "modify" : $.SysConstants.MODIFY
    },"prodDetailHidModuleRights");
    $.handleRights({
        "addProdFaq" : $.SysConstants.ADD
    },"prodFQAHidModuleRights");
    
    $.handleRights({
        "editIssuePlan" : $.SysConstants.MODIFY,
        "viewIssuePlan" : $.SysConstants.VIEW
    },"prodIssuePlanHidModuleRights");

	//快捷工具栏中没有任何按钮，则清除该DIV
//    if($("#divAddTool input").length == 0){
//    	$("#divAddTool").remove();
//    }
	
	$("#modify").click(function(){
		modify();
	});
	
	$("#addProdFaq").click(function(){
		addProdFaq();
	});
	
	$("#editIssuePlan").click(function(){
		editProdPlan();
	});
	
	$("#viewIssuePlan").click(function(){
		viewProdPlan();
	});
	
}

//修改
function modify(){
	var prodInfoId = $("#prodInfoId").val();
	mainFrame.addTab({
		id:"modify_prodInfo_"+prodInfoId,
		title:"修改产品信息",
		url:BASE_PATH + "/m/prod_info/init_updateProdInfo?id=" + prodInfoId,
		callback : refresh
	});
}

function addProdFaq() {
	var prodInfoId = $("#prodInfoId").val();
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "新增问题";
	var url = BASE_PATH + "m/prod_faq/init_editProdFaqPage?prodInfoId=" + prodInfoId;
	var width = 600;
	var height = 310;
	dialogWin = $.showModalDialog(title, url, width, height);
}

/**
 * 关闭对话框
 */
function closeWin(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
		});
	}else{
		dialogWin.close();
	}
}

function editProdPlan() {
	var prodInfoId = $("#prodInfoId").val();
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "修改发行方案";
	var url = BASE_PATH + "m/prod_plan/init_modify?prodId=" + prodInfoId;
	
	var win = $(window);
	var width = win.width() - 30;
	var height = win.height() - 58;
	var position = {left: 0, top: 0};
	
//	var width = 850;
//	var height = 480;
	dialogWin = $.showModalDialog(title, url, width, height, null, position);
}

function viewProdPlan() {
	var prodInfoId = $("#prodInfoId").val();
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "查看发行方案";
	var url = BASE_PATH + "m/prod_plan/view?prodId=" + prodInfoId;
	
	var win = $(window);
	var width = win.width() - 30;
	var height = win.height() - 58;
	var position = {left: 0, top: 0};
	
//	var width = 850;
//	var height = 480;
	dialogWin = $.showModalDialog(title, url, width, height, null, position);
}
function refresh(){
	window.location.reload();
//	$("#prod_info").removeClass("current").click();
}
