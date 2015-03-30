
/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 客户下拉框
 */
var customerInfoSel = null;
/**
 * 客户性质下拉选择框
 */
var customerNatureSel;
/**
 * 有无定金下拉框
 */
var earnestSel = null;
/**
 * 产品下拉框
 */
var prodInfoSel = null;

var dialog = null;

$(initPage);
function initPage(){
	
	// 提交
	$("#save").click(function() {
		savePayment();
	});
	
	//取消
	$("#close").click(function() {
		args.returnValue = false;
		args.callback();
	});
	
	//打开用户帐号
//    $('#openDispacher').click(
//	   	function(e){
//	   		dialog = new UserDialog(ERMP_PATH, BASE_PATH);
//	   		dialog.setCallbackFun(callbackFun);
//	   		var user = dialog.openDialog();
//		}
//	);
	
	//客户经理默认为当前登入者
	$("#custManage").val(args.userDisplayName);
	$("#custManageId").val(args.userAccountID);
	
	//初始化客户下拉框
	initCustomerInfoSel();
	
//	initCustomerNatureSel();
	initProInfoSel();
	//初始化有无定金下拉框
	initEarnestSel();
	
	blur();
}

function callbackFun() {
	var selected = dialog.selected;
		if (selected!=null){
			$("#dispacher").val(selected[0].id);
			$("#dispacherDis").val(selected[0].name);
		}
}

/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){
	
	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?pageSize=500&multipleCustomerStatus=5,4" + "&format=json&jsoncallback=?";
	/*$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#customerInfoSel").html(html);
	   		customerInfoSel = $("#customerInfoSel").ySelect({
				width:107,
				popwidth:140,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					//同步客户性质 
					synchronousCustInfo(value);
				}
			});
  	}});*/
	
	$.getJSON(url,function(data){
		$("#customerInfoSel").html(data.htmlValue);
		
		customerInfoSel = $("#customerInfoSel").ySelect({
			width:107,
			popwidth:140,
			afterLoad : function() {
				initCustomerNatureSel(null);
			},
			onChange : function(value, name) {
				//同步客户性质 
				synchronousCustInfo(value);
			}
		});
	});
}

/**
 * 初始化产品下拉选择框
 */
function initProInfoSel(){
	var html=" <div>**请选择...</div> ";
	var url = BASE_PATH + "m/prod_info/initProdInfoSel?excProdStatus=STATUS_FOUND";
	$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#prodInfoSel").html(html);
	   		prodInfoSel = $("#prodInfoSel").ySelect({
				width:470,
				popwidth:500,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					//同步剩余额度，已划款总额度
					synchronousProdInfo(value);
				}
			});
  	}});
}

/**
 * 同步剩余额度，已划款总额度
 */
function synchronousProdInfo(value) {
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH +"m/prod_info/getProdInfoById",
		dataType : "json",
		data : {
			id : value //产品ID
		},
        success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		var prodInfo = data.prodInfo;
        		if(prodInfo){
        			if(!prodInfo.sellAmount){
        				prodInfo.sellAmount = 0;
        			}
        			if(!prodInfo.transferAmount){
        				prodInfo.transferAmount = 0;
        			}
        			$("#remainingAmount").val(prodInfo.sellAmount-prodInfo.transferAmount);
        			$("#transferAmount").val(prodInfo.transferAmount);
        		} else {
        			$("#remainingAmount").val("");
        			$("#transferAmount").val("");
        		}
        	}
        },
        error : $.ermpAjaxError
    });
	
}

/**
 * 同步客户性质
 */
function synchronousCustInfo(value) {
	var custProperty = null;
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : CRM_PATH +"m/customer/getCustomerInfo",
		dataType : "json",
		data : {
			customerId : value
		},
        success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		var customer = data.customer;
        		if(customer){
        			custProperty = customer.custProperty;
					initCustomerNatureSel(custProperty + "");
        		} else {
        			initCustomerNatureSel(null);
        		}
        	}
        },
        error : $.ermpAjaxError
    });
	
}

/**
 * 初始化客户性质下拉选择框
 */
function initCustomerNatureSel(custProperty) {
	
	if ($("#customerNatureSel").size() > 0) {
		customerNatureSel = $("#customerNatureSel").ySelect({
			width : 100,
			url : CRM_PATH + "/l/dict/initCustomerNatureSel",
			afterLoad : function() {
				customerNatureSel.addOption("", "请选择...", 0);
				// 设置默认值
				if(custProperty != null){
					customerNatureSel.select(custProperty);
				}else{
					customerNatureSel.select(0);
				}
			}
		});
	}
}

//有无定金下拉框
function initEarnestSel(){
	earnestSel = $("#earnestSel").ySelect({
		name : "earnestSel",
		width:107,
		popwidth:140
	});
}

/**
 * 保存/修改
 */
function savePayment(){
	//保存之前判断,验证非空、长度、规则判断
	if(!__OnBeforeSave()){
		return;
	}
	
	var paymentJson = getPaymentJson();
	
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "m/operationsManage/addCustPayment", 
		   dataType : "json",
		   data : {
			   paymentJson : paymentJson
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
 * 保存之前判断,验证非空、长度、规则判断
 */
function __OnBeforeSave(){
	
	//设置保存按钮禁用
	setCommitButtonDisabled(true);
	
	var transferDate = $("#transferDate").val();
	var custId = customerInfoSel.getValue();
	var custName = customerInfoSel.getDisplayValue();
	var custProperty = customerNatureSel.getValue();
	var prodId = prodInfoSel.getValue();
	var payDepositFlag = earnestSel.getValue();
	var appointmentAmount = $.trim($("#appointmentAmount").val());
	var appointmentAmountCapital = $("#appointmentAmountCapital").text();
//	var approver = $.trim($("#dispacher").val());
	
	//客户
	if (custId == "") {
		alert("请选择客户");
		setCommitButtonDisabled(false);
  		return false;
	}
	if (transferDate == "") {
		alert("请选择划款时间");
		setCommitButtonDisabled(false);
  		return false;
	}
	if (custProperty == "") {
		alert("请选择客户性质");
		setCommitButtonDisabled(false);
  		return false;
	}
	if (prodId == "") {
		alert("请选择产品项目");
		setCommitButtonDisabled(false);
  		return false;
	}
	if (payDepositFlag == "") {
		alert("请选择有无定金");
		setCommitButtonDisabled(false);
  		return false;
	}
	if (appointmentAmount == "") {
		alert("请填写预约金额");
		setCommitButtonDisabled(false);
  		return false;
	}
//	if (approver == "") {
//		alert("请选择审批人");
//		setCommitButtonDisabled(false);
//  		return false;
//	}
	if($.validInput("remark", "备注", false, "\<\>\'\"", 256)){
		setCommitButtonDisabled(false);
		return false;
	}
	
	return true;
}

/**
 * 设置按钮状态
 * @param disabled true:不可用。false:可用。
 */
function setCommitButtonDisabled(disabled) {
	if (disabled) {
		$("#save").attr("disabled", "true");
		$("#close").attr("disabled", "true");
	} else {
		$("#save").removeAttr("disabled");
		$("#close").removeAttr("disabled");
	}
}


/**
 * 封装数据
 * return paymentJson
 */
function getPaymentJson(){
	
	var paymentJson = null;
	
	var payment = new Object;
	payment.saleManId = $.trim($("#custManageId").val());
	payment.transferDate = $("#transferDate").val();
	payment.custId = customerInfoSel.getValue();
	payment.custName = customerInfoSel.getDisplayValue();
	payment.custProperty = customerNatureSel.getValue();
	payment.prodId = prodInfoSel.getValue();
	payment.payDepositFlag = earnestSel.getValue();
	if(payment.payDepositFlag == "0"){
		payment.payDepositFlag = false;
	}
	payment.appointmentAmount = $.trim($("#appointmentAmount").val());
	payment.appointmentAmountCapital = $("#appointmentAmountCapital").text();
	payment.remark = $.trim($("#remark").val());
//	payment.approver = $.trim($("#dispacher").val());
	payment.standardFlag = false;
	payment.payType = "1";
	
	paymentJson = $.toJSON(payment);
	
	return paymentJson;
}

//失去焦点事件
function blur() {
	$("#appointmentAmount").blur(function (){ 
		var appointmentAmount = $("#appointmentAmount").val();
		//金钱转为大写
		var chineseMoney = convertCurrency(appointmentAmount * 10000);
		$("#appointmentAmountCapital").html(chineseMoney);
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
	alert("预约金额只能是数字!");
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

