
//初始化页面
$(initPage);

function initPage(){
	var param = parent.window.dialogParam;
	$("#flowJsonData").val(param.jsonStr).focus();
	$("#cancelBtn").click(function(){
		param.callback();
	});
	$("#comitBtn").click(function(){
		param.callback($("#flowJsonData").val());
	});
};
