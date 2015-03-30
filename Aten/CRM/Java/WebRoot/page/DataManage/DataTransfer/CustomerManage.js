var mainFrame = $.getMainFrame();

/**
 * 客户经理下拉框
 */
var customerManageSel;

$(initPage);
//父页面传过来的参数信息
var args = parent.window.dialogParam;

function initPage(){
	
	//初始化客户经理下拉框
	initCustomerManageSel();
	
	$("#saveBtn").click(function(){
		selectCustomerManage();
	});
	$("#doclose").click(function(){
		args.returnValue = false;
		args.callback();
	});
}

function initCustomerManageSel(){
	customerManageSel = $("#customerManageSel").ySelect({
		width : 220,
		height: 150,
		url : BASE_PATH + "/m/group_ext/query_allSaleStaff",
		afterLoad : function() {
			customerManageSel.addOption("", "请选择...", 0);
			// 设置默认值
			customerManageSel.select(0);
		},
		onChange : function(value, name) {
			
		}
	});
}

function selectCustomerManage(){
	args.customerManage = customerManageSel.getValue();
	if(args.customerManage==null || args.customerManage==""){
		alert("请选择客户经理");
		return;
	}
	
	args.returnValue = true;
	args.callback();
}
