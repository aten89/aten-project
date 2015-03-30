/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

$(initEditProdFaq);
function initEditProdFaq(){
	// 保存
	$("#save").click(function() {
		saveProdPlan();
	});
	
	//取消
	$("#cancel").click(function() {
		args.returnValue = false;
		args.callback();
	});
	
	//点击时间选择框，滚动条到最底部，避免弹出层被挡住
	$(".invokeBoth").focus(function(){
		$("#scrollDIV").scrollTop($("#scrollDIV").height()); 
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

/**
 * 保存
 * @returns {Boolean}
 */
function saveProdPlan() {
	// 设置按钮
	setCommitButtonDisabled(true);
	
	var prodInfo = new Object();
	prodInfo.id = $("#prodId").val();
	
	var planObj = new Object();
	planObj.id = $("#id").val();
	planObj.prodInfo = prodInfo;
	
	planObj.stdOrderFlag = $("input[name='stdOrderFlag'][@checked]").val();
	planObj.salesModel = $("#salesModel").val();
	planObj.lowestStd = $("#lowestStd").val();
	planObj.remainAmountWarn = $("#remainAmountWarn").val();
	planObj.remainAmountStop = $("#remainAmountStop").val();
	planObj.smallCaps = $("#smallCaps").val();
	planObj.smallNumCaps = $("#smallNumCaps").val();
	planObj.description = $("#description").val();
	planObj.managerTakeRatio = $("#managerTakeRatio").val();
	planObj.partnerTakeRatio = $("#partnerTakeRatio").val();
	planObj.bigAmountPoint = $("#bigAmountPoint").val();
	planObj.lowestPayAmount = $("#lowestPayAmount").val();
	planObj.sizeVolumeRatio = $("#sizeVolumeRatio").val();
	planObj.bigToSmallFlag = $("input[name='bigToSmallFlag'][@checked]").val();
	planObj.smallAppointFlag = $("input[name='smallAppointFlag'][@checked]").val();
	planObj.preheatStartTime = $("#preheatStartTime").val();
	planObj.preheatEndTime = $("#preheatEndTime").val();
	planObj.estimateEndTime = $("#estimateEndTime").val();
	planObj.contractSendTime = $("#contractSendTime").val();
	planObj.dataSendTime = $("#dataSendTime").val();
	planObj.prodTrainTime = $("#prodTrainTime").val();
	
	planObj.nonStdStartTime = $("#nonStdStartTime").val();
	planObj.nonStdAmount = $("#nonStdAmount").val();
	planObj.nonStdLimitRatio = $("#nonStdLimitRatio").val();
	planObj.orderTimeLimit = $("#orderTimeLimit").val();
	planObj.smallNumLimit = $("#smallNumLimit").val();
	planObj.nonStdCfmMin = $("#nonStdCfmMin").val();
	
	var orderRule = "";
	$("input[name='orderRuleCB'][@checked]").each(function(){
		orderRule += $(this).val() + ";";
	});
	planObj.orderRule = orderRule;
	
	var jsonStr = $.toJSON(planObj);

	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/prod_plan/modify",
		dataType : "json",
		data : {
			jsonStr : jsonStr
		},
	    success : function(data){
        	if ($.checkErrorMsg(data)) {
        		args.returnValue = true;
        		args.callback();
        	} else {
        		setCommitButtonDisabled(false);
        	}
        },
        error : $.ermpAjaxError
    });
}


/**
 * 设置按钮状态
 * @param disabled true:不可用。false:可用。
 */
function setCommitButtonDisabled(disabled) {
	if (disabled) {
		$(".allBtn").attr("disabled","true");
	} else {
		$(".allBtn").removeAttr("disabled");
	}
}
