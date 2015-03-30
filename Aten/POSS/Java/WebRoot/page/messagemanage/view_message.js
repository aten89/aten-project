var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	// 初始化下拉框
	initProdSel();

	// 保存
	$("#save").click(function() {
		saveMessage();
	});
	
	//取消
	$("#close").click(function() {
		//刷新父列表
		mainFrame.getCurrentTab().close();
	});
}

/**
 * 初始化性别下拉选择框
 */
function initProdSel() {
	if ($("#prodSel").size() > 0) {
		prodSel = $("#prodSel").ySelect({
			width : 100,
			url : BASE_PATH + "/m/prod_info/initProdInfoSel",
			afterLoad : function() {
				prodSel.addOption("", "请选择...", 0);
				// 设置默认值
				prodSel.select(0);
			}
		});
	}
}

/**
 * 保存/修改
 */
function saveMessage(){
	if(!validateForm()){
		return;
	}
	
	var title = $.trim($("#title").val());
	var prodId = prodSel.getValue() == "" ? "" : prodSel.getValue();
	var prodName = prodSel.getDisplayValue() == "" ? "" : prodSel.getDisplayValue();
	var receiverNo = $.trim($("#receiverNo").val());
	var content = $.trim($("#content").val());
	
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/message/add", 
		   dataType : "json",
		   data : {
				title : title,
				prodId : prodId,
				prodName : prodName,
				receiverNo : receiverNo,
				content : content
		   },
		   success : function(data,i){
		    	if ($.checkErrorMsg(data)) {
		    		args.returnValue = true;
		    		args.callback();
		    	} 
		    },
		    error : $.ermpAjaxError
		});
}

/**
 * 表单验证
 * @return {Boolean}
 */
function validateForm(){
	return true;
}
