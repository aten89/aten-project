/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 附件控件
 */
var ocx ;

$(initPage);
function initPage(){
	
	// 初始化附件控件
//	initControl();
	
	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			$("#transitionName").val($(this).val());
  			var commId = "";//处理意见id
		  	if($("#commId").size() != 0){
				commId = $("#commId").val();
		  	}
	  		var tiid = $("#taskInstanceID").val();//任务实例ID
			var comment = $.trim($("#comment").val());//填写的意见
			
			if ($("#transitionName").val() == "作废") {
				if(endTransition(tiid, commId,comment,$("#transitionName").val())){
					args.returnValue = true;
		    		args.callback();
				}
			}else if ($("#transitionName").val() == "驳回修改"){
				if(endTransition(tiid, commId,comment,$("#transitionName").val())){
					args.returnValue = true;
		    		args.callback();
				}
			}else {
				if(commitDispose(commId, tiid, comment, $("#transitionName").val())){
	  				args.returnValue = true;
		    		args.callback();
	  			}
			}
  		});
  	});
	
	blur();
}

/*
 * 驳回/作废
 */
function endTransition (tiid, commId,comment, transitionName) {
	if($.validInput("comment", "处理意见", true, null, 1000)){
	  			return false;
	  }
	var successStatus = false;
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       dataType : "json",
       url  : "m/operationsManage/dealPayment",
       data : {
       			tiid:tiid,
       			transition : transitionName,
       			comment:comment,
       			taskCommentId : commId
       		},
       success : function(data){
	       	if ($.checkErrorMsg(data)) {
	       		successStatus = true;
	       	}
       },
       error : $.ermpAjaxError
   	});
	return successStatus;
}	

/**
 * 提交流程动作
 * @param {} commId　划款处理意见id
 * @param {} tiid 
 * @param {} comment
 * @param {} transitionName 转向的名称
 * @return {Boolean}
 */
function commitDispose(commId, tiid, comment, transitionName){
	if($.validInput("comment", "处理意见", true, null, 1000)){
		return false;
	}
	
	var successStatus = false;

	if (transitionName == "") {
		alert("请选择转向");
		return false;
	}	
	$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       dataType : "json",
	       url  : "m/operationsManage/approvePaymentLeader",
	       data : {	
	       			tiid:tiid,
	       			transition : transitionName,
	       			taskCommentId : commId,
	       			comment:comment
	       		},
	       success : function(data){
		       	if ($.checkErrorMsg(data)) {
					successStatus = true;
//					__OnAfterSave(id);
		       	}
	       },
	       error : $.ermpAjaxError
	   	});
	return successStatus;
}

/**
 * 初始化附件控件
 */
function initControl(){
	//获取控件实例,需要提供容器的ID
	ocx = $.getNewAttachmentControl("NTKO_AttachmentCtrl", "web");
	ocx.setPermission({view:true,add:true,update:true,del:true,saveas:true});
	ocx.setBasePath(BASE_PATH);
	ocx.setSaveURL(BASE_PATH + "m/issue/uploadIssueattachments");
	//定义保存成功或失败后执行的回调函数，执行时将自动带上code和message
	ocx.setAfterSaveEvent("saveAttachmentEvent");
	ocx.init("NTKO_AttachmentCtrl");
	var issueId = $("#id").val();
	if (issueId) {
	    //加载文件列表
	    var sUrl =  BASE_PATH+ "m/issue/load_issueAttachments";
	    $.post(sUrl, {
			"id" : issueId
		}, callbackfun, "json");
	}
}

//失去焦点事件
function blur() {
	$("#payAmount").blur(function (){ 
		var payAmount = $("#payAmount").val();
		//金钱转为大写
		var chineseMoney = convertCurrency(payAmount * 10000);
		$("#payAmountCapital").html(chineseMoney);
       });
}

//金钱数字转为大写
function convertCurrency(currencyDigits) {
	// Constants:
	var MAXIMUM_NUMBER = 99999999999.99;
	// Predefine the radix characters and currency symbols for output:
	var CN_ZERO = "零";
	var CN_ONE = "壹";
	var CN_TWO = "贰";
	var CN_THREE = "叁";
	var CN_FOUR = "肆";
	var CN_FIVE = "伍";
	var CN_SIX = "陆";
	var CN_SEVEN = "柒";
	var CN_EIGHT = "捌";
	var CN_NINE = "玖";
	var CN_TEN = "拾";
	var CN_HUNDRED = "佰";
	var CN_THOUSAND = "仟";
	var CN_TEN_THOUSAND = "万";
	var CN_HUNDRED_MILLION = "亿";
	var CN_DOLLAR = "元";
	var CN_TEN_CENT = "角";
	var CN_CENT = "分";
	var CN_INTEGER = "整";

	// Variables:
	var integral; // Represent integral part of digit number.
	var decimal; // Represent decimal part of digit number.
	var outputCharacters; // The output result.
	var parts;
	var digits, radices, bigRadices, decimals;
	var zeroCount;
	var i, p, d;
	var quotient, modulus;

	// Validate input string:
	currencyDigits = currencyDigits.toString();
	if (currencyDigits == "") {
	alert("Empty input!");
	return "";
	}
	if (currencyDigits.match(/[^,.\d]/) != null) {
	alert("预约金额只能是整数!");
	return "";
	}
	if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
	alert("Illegal format of digit number!");
	return "";
	}

	// Normalize the format of input digits:
	currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma delimiters.
	currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the beginning.
	// Assert the number is not greater than the maximum number.
	if (Number(currencyDigits) > MAXIMUM_NUMBER) {
	alert("Too large a number to convert!");
	return "";
	}

	// Process the coversion from currency digits to characters:
	// Separate integral and decimal parts before processing coversion:
	parts = currencyDigits.split(".");
	if (parts.length > 1) {
	integral = parts[0];
	decimal = parts[1];
	// Cut down redundant decimal digits that are after the second.
	decimal = decimal.substr(0, 2);
	}
	else {
	integral = parts[0];
	decimal = "";
	}
	// Prepare the characters corresponding to the digits:
	digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
	radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
	bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
	decimals = new Array(CN_TEN_CENT, CN_CENT);
	// Start processing:
	outputCharacters = "";
	// Process integral part if it is larger than 0:
	if (Number(integral) > 0) {
	zeroCount = 0;
	for (i = 0; i < integral.length; i++) {
	p = integral.length - i - 1;
	d = integral.substr(i, 1);
	quotient = p / 4;
	modulus = p % 4;
	if (d == "0") {
	zeroCount++;
	}
	else {
	if (zeroCount > 0)
	{
	outputCharacters += digits[0];
	}
	zeroCount = 0;
	outputCharacters += digits[Number(d)] + radices[modulus];
	}
	if (modulus == 0 && zeroCount < 4) {
	outputCharacters += bigRadices[quotient];
	}
	}
	outputCharacters += CN_DOLLAR;
	}
	// Process decimal part if there is:
	if (decimal != "") {
	for (i = 0; i < decimal.length; i++) {
	d = decimal.substr(i, 1);
	if (d != "0") {
	outputCharacters += digits[Number(d)] + decimals[i];
	}
	}
	}
	// Confirm and return the final output string:
	if (outputCharacters == "") {
	outputCharacters = CN_ZERO + CN_DOLLAR;
	}
	if (decimal == "") {
	outputCharacters += CN_INTEGER;
	}
	outputCharacters =outputCharacters;
	return outputCharacters;
	}