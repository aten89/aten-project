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
			width : 405,
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
			},
			onChange : function(val, name) {
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
				        		$("#latestDas").val(data.blankContract.latestDas);
								$("input[name='returnFlag'][value='"+data.blankContract.returnFlag+"']").attr("checked",true);
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
	if ($.validNumber("contractNums", "合同数", true)) {
		return false;
	}
	if ($.validNumber("latestDas", "最迟上交天数", true)) {
		return false;
	}
	var contractNums = $.trim($("#contractNums").val());
	var latestDas = $.trim($("#latestDas").val());
	var returnFlag = $("input[name=returnFlag][checked]").val();
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/cont_blank/add", 
		   dataType : "json",
		   data : {
				prodId : prodId,
				contractNums : contractNums,
				latestDas : latestDas,
				returnFlag : returnFlag
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


