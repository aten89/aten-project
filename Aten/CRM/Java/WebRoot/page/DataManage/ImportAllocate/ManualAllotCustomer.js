/*************************
 数据管理--导入与分配--手动分配
*************************/ 

/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 销售部门下拉选择框
 */
var saleGroupSel;
/**
 * 销售员工下拉选择框
 */
var saleStaffSel;

$(initManualAllotCustomer);
function initManualAllotCustomer(){
	
	// 初始化销售部门
	initSaleGroupSel();
	
	// 保存
	$("#save").click(function() {
		saveManualAllotimportCustomer();
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
function saveManualAllotimportCustomer() {
	// 设置按钮
	setCommitButtonDisabled(true);
	// 要分配的导入客户ID
	var importCustomerIds = args.importCustomerIds;
	// 销售人员账号
	var serviceAccountId = saleStaffSel.getValue();
	if (serviceAccountId == "") {
		$.alert("请选择销售人员");
		setCommitButtonDisabled(false);
		return;
	}
	// 
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH + "m/importCustomer/save_manualAllotImportCust",
		dataType : "json",
		data : {
			importCustIdsJson : importCustomerIds,
			serviceAccountId : serviceAccountId
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
 * 初始化下拉选择框
 */
function initSaleGroupSel() {
	if ($("#saleGroupSel").size() > 0) {
		saleGroupSel = $("#saleGroupSel").ySelect({
			width : 160,
			height: 150,
			url : BASE_PATH + "/m/group_ext/query_saleGroupSel",
			afterLoad : function() {
				saleGroupSel.addOption("", "请选择...", 0);
				// 设置默认值
				saleGroupSel.select(0);
			},
			onChange : function(value, name) {
				if (value) {
					initSaleStaffSel(name);
				} else {
					saleStaffSel.select(0);
					saleStaffSel.disable(true);
				}
			}
		});
		saleStaffSel = $("#saleStaffSel").ySelect({width: 160, isdisabled:true});
	}
}

/**
 * 初始化销售员工下拉选择框
 * @param groupId 销售部门ID
 */
function initSaleStaffSel(name) {
	if ($("#saleStaffSel").size() > 0) {
		saleStaffSel = $("#saleStaffSel").ySelect({
			width : 160,
			height: 150,
			url : BASE_PATH + "/m/group_ext/query_groupStaffByGroup",
			data:"groupName="+name,
			afterLoad : function() {
				saleStaffSel.addOption("", "请选择...", 0);
				// 设置默认值
				saleStaffSel.select(0);
			}
		});
	}
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
