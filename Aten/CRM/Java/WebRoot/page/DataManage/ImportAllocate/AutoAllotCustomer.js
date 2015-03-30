/*************************
 数据管理--导入与分配--手动分配
*************************/ 

/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

$(initAutoAllotCustomer);
function initAutoAllotCustomer(){
	
	var totalToAllotNum = args.totalToAllotNum;
	$("#totalToAllotNumSpan").html(totalToAllotNum);
	$("#totalToAllotNum").val(totalToAllotNum);
	
	// 保存
	$("#save").click(function() {
		saveAutoAllotCustomer();
	});
	
	//取消
	$("#cancel").click(function() {
		args.returnValue = false;
		args.callback();
	});
}

/**
 * 保存
 * @returns {Boolean}
 */
function saveAutoAllotCustomer() {
	// 设置按钮
	setCommitButtonDisabled(true);
	// 判断带分配的客户数是否大于0
	var allToAllotCustNum = $("#totalToAllotNum").val();
	if (allToAllotCustNum == undefined || allToAllotCustNum == 0) {
		alert("待分配的客户数为0");
		setCommitButtonDisabled(false);
		return;
	}
	var autoAllotObj = new Object();
	// 所有的销售人员
	var saleStaffs = new Array();
	var totalAllotCustNum = 0;
	// 循环遍历所有的销售人员
	$("#saleStaffTBody tr").each(function(i,value) {
		var accountId = $(this).find("td").eq(0).find("input").eq(0).val();
		var allotCustomerNum = $(this).find("td").eq(1).find("input").eq(0).val();
		//
		var userAccountExt = new Object();
		userAccountExt.accountId = accountId;
		userAccountExt.allotCustomerNum = allotCustomerNum;
		saleStaffs.push(userAccountExt);
		totalAllotCustNum += allotCustomerNum;
	});
	autoAllotObj.userAccountExts = saleStaffs;
	// 判断是否有分配
	if (totalAllotCustNum == 0) {
		alert("您没有分配客户给销售人员");
		setCommitButtonDisabled(false);
		return;
	}
	
	// 
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH + "m/importCustomer/save_autoAllotImportCust",
		dataType : "json",
		data : {
			multipleUserAccountExtJson : $.toJSON(autoAllotObj)
		},
	    success : function(data,i){
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
