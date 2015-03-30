/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

$(initEditProdFaq);
function initEditProdFaq(){
	$.handleRights({
        "managerTakeRatioTR" : "viewtackscale",
        "partnerTakeRatioTR" : "viewtackscale"
    },"prodIssuePlanHidModuleRights");
    
	//取消
	$("#cancel").click(function() {
		args.returnValue = false;
		args.callback();
	});
	
	//默认选中排序规则
	var orderRule = $("#orderRule").val();
	if (orderRule) {
		var arr = orderRule.split(';');
		for(var i = 0; i < arr.length; i++) {
			if (arr[i]) {
				$("input[name='orderRuleCB']").each(function(){
					if ($(this).val() ==  arr[i]) {
						$(this).attr("checked", true);
					}
				});
			}
		}
	}
}

function hideTbody(flagId, dataId) {
	var flag = $("#" + flagId).text();
	if (flag == "-") {
		$("#" + flagId).text("+");
		$("#" + dataId).hide();
	} else {
		$("#" + flagId).text("-");
		$("#" + dataId).show();
	}
}
