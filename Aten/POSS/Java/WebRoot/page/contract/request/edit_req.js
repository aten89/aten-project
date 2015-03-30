var mainFrame = $.getMainFrame();

/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 产品下拉选择框
 */
var prodSel;

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
	initProdSel();
}

/**
 * 初始化性别下拉选择框
 */
function initProdSel() {
	if ($("#prodSel").size() > 0) {
		prodSel = $("#prodSel").ySelect({
			width : 400,
			url : BASE_PATH + "/m/cont_blank/prodsel",
			afterLoad : function() {
				prodSel.addOption("", "请选择...", 0);
				// 设置默认值
				var prodId = $("#prodId").val();
				prodId = prodId ? prodId : $("#prodId_p").val();
				if (prodId) {
					prodSel.select(prodId);
					prodSel.disable(true)
				} else {
					prodSel.select(0);
				}
			},
			onChange : function(val, name) {
				$("#remainNums").text("");
				if (val) {
					$.ajax({
				        type : "POST",
						cache: false,
						async: true,
						url : BASE_PATH + "/m/cont_blank/loadbcon",
						dataType : "json",
						data:{
							prodId : val
						},
				        success : function(data){
				        	if ($.checkErrorMsg(data) && data.blankContract) {
				        		$("#remainNums").text(data.blankContract.remainNums);
					    	}
				        },
				        error : $.ermpAjaxError
				    });
				}
			}
		});
	}
}

/**
 * 保存/修改
 */
function saveDetail(){

	var prodId = prodSel.getValue() == "" ? "" : prodSel.getValue();
	if (!prodId) {
		$.alert("请选择产品项目");
		return;
	}
	if ($.validNumber("reqNums", "所需合同数", true)) {
		return false;
	}
	if ($.validInput("reqRemark", "需求备注", false,"",1000)) {
		return false;
	}
	var id = $.trim($("#reqId").val());
	
	
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/cont_request/"+ (id ? "modify":"add"), 
		   dataType : "json",
		   data : {
				prodId : prodId,
				id : id,
				reqNums : $("#reqNums").val(),
				reqRemark : $("#reqRemark").val()
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


