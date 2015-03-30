var mainFrame = $.getMainFrame();

/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

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
}

/**
 * 保存/修改
 */
function saveDetail(){
	if ($.validNumber("signNums", "签署合同数", true)) {
		return false;
	}
	if ($.validNumber("blankNums", "空白合同数", true)) {
		return false;
	}
	if ($.validNumber("invalidNums", "签废合同数", true)) {
		return false;
	}
	if ($.validNumber("unPassNums", "不通过合同数", false)) {
		return false;
	}
	if ($.validInput("checkRemark", "审核备注", false,"",1000)) {
		return false;
	}
	var id = $.trim($("#reqId").val());
	
	
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/cont_hand/check", 
		   dataType : "json",
		   data : {
				id : id,
				signNums : $("#signNums").val(),
				blankNums : $("#blankNums").val(),
				invalidNums : $("#invalidNums").val(),
				unPassNums : $("#unPassNums").val(),
				checkRemark : $("#checkRemark").val()
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

function viewProdInfo(id) {
	mainFrame.addTab({
		id:"view_prodInfo_"+id,
		title:"查看产品详情",
		url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + id
	});
}


