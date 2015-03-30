
var dialogWin;
var flowNotify = null;
function addFlowNotify(user, name, flowClass, viewDetailURL) {
	if (flowNotify == null) {
		flowNotify = {};
		flowNotify.subject =  user + "于" + new Date().format("yyyy-MM-dd hh:mm:ss") + name;
		flowNotify.flowClass = flowClass;
		flowNotify.viewDetailURL = viewDetailURL;
	}
	
	var win = $(window);
	var l = (win.width() - 650) / 2;
	var t = (win.height() - 500) / 2;
	
	var url = BASE_PATH + "m/flow_notify?act=initadd";
	dialogWin = $.showModalDialog("添加知会人", url, 640, 450, function() {
		if (window.returnValue) {
			if (flowNotify.notifyUser) {
				$("#notifyUserNames").html("已添加知会人：" + flowNotify.notifyUserName).show();
			} else {
				$("#notifyUserNames").html("").hide();
				flowNotify = null;
			}
		} 
//		else {
//			$("#notifyUserNames").html("").hide();
//			flowNotify = null;
//		}
	},{left: l, top: t});
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

function saveFlowNotify(refFormID) {
	if (!flowNotify) {
		return;
	}
	if (!refFormID) {
		alert("知会关联ID不能为空");
		return;
	}
	flowNotify.refFormID = refFormID;
	flowNotify.viewDetailURL = flowNotify.viewDetailURL + refFormID;
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       url  : "m/flow_notify?act=add",
       data : {json : $.toJSON(flowNotify)},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
               $.alert("添加知会人成功");
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
           }
           $("#startFlow,#saveAsDraft").removeAttr("disabled");
       },
       error : $.ermpAjaxError
   	});
}