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
	
	initRemainNums();
}

/**
 * 初始化性别下拉选择框
 */
function initRemainNums() {
	$.ajax({
        type : "POST",
		cache: false,
		async: true,
		url : BASE_PATH + "/m/cont_blank/loadbcon",
		dataType : "json",
		data:{
			prodId : $("#prodId").val()
		},
        success : function(data){
        	if ($.checkErrorMsg(data) && data.blankContract) {
        		$("#remainNums").text(data.blankContract.remainNums);
	    	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 保存/修改
 */
function saveDetail(){
	if ($.validNumber("extendNums", "实际发放数", true)) {
		return false;
	}
	if ($.validInput("extendRemark", "发放备注", false,"",1000)) {
		return false;
	}
	var id = $.trim($("#reqId").val());
	
	
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/cont_request/check", 
		   dataType : "json",
		   data : {
				id : id,
				extendNums : $("#extendNums").val(),
				expressName : $("#expressName").val(),
				expressNo : $("#expressNo").val(),
				sendDate : $("#sendDate").val(),
				extendRemark : $("#extendRemark").val()
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


