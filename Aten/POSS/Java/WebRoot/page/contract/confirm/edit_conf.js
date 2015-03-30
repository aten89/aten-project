
/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 产品下拉选择框
 */
var prodSel;
var orgSel;

$(initPage);
function initPage(){
	// 保存
	$("#save").click(function() {
		saveDetail();
	});
	
	//取消
	$("#close").click(function() {
		//刷新父列表
		args.returnValue = false;
		args.callback();
	});
	// 初始化下拉框
	initSelect();
}

/**
 * 初始化性别下拉选择框
 */
function initSelect() {
	prodSel = $("#prodSel").ySelect({
		width : 400,
		url : BASE_PATH + "/m/prod_info/initProdInfoSel?excProdStatus=STATUS_FOUND",
		afterLoad : function() {
			prodSel.addOption("", "请选择...", 0);
			// 设置默认值
			var prodId = $("#prodId").val();
			if (prodId) {
				prodSel.select(prodId);
				prodSel.disable(true)
			} else {
				prodSel.select(0);
			}
		}
	});
	
	orgSel = $("#orgSel").ySelect({
		width : 400,
		url : BASE_PATH + "m/confirm_ext/orgsel",
		afterLoad : function() {
			orgSel.addOption("", "请选择...", 0);
			// 设置默认值
			var orgName = $("#orgName").val();
			if (orgName) {
				orgSel.select(orgName);
				orgSel.disable(true)
			} else {
				orgSel.select(0);
			}
		}
	});
}

/**
 * 保存/修改
 */
function saveDetail(){
	var prodId = prodSel.getValue();
	if (!prodId) {
		$.alert("请选择产品项目");
		return;
	}
	var orgName = orgSel.getValue();
	if (!orgName) {
		$.alert("所属机构");
		return;
	}
	if ($.validNumber("custNums", "客户数", true)) {
		return false;
	}
	if ($.validNumber("confirmNums", "确认书数目", true)) {
		return false;
	}
	var id = $("#confId").val();
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/confirm_ext/" + (id ? "modify" : "add"), 
		   dataType : "json",
		   data : {
				id : id,
				prodId : prodId,
				orgName : orgName,
				custNums : $("#custNums").val(),
				confirmNums : $("#confirmNums").val(),
				expressName : $("#expressName").val(),
				expressNo : $("#expressNo").val(),
				remark : $("#remark").val()
		   },
		   success : function(data,i){
		    	if ($.checkErrorMsg(data)) {
		    		//刷新父列表
		    		args.returnValue = true;
		    		args.callback();
		    	} 
		    },
		    error : $.ermpAjaxError
		});
}


