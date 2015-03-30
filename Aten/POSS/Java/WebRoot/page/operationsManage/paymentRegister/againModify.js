
/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;
var mainPanel = $.getMainFrame();

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

var janeEditionObj;


$(initPage);
function initPage(){
	
	janeEditionObj = $("#janeEditionAttchDIV").singleAttaUpLoad({
		divId : "janeEditionAttchDIV",
		oldShowId : "janeEditionAttchOldShow",
	//	fileType : "xls",
		url : "m/operationsManage/save_attachment"
	});
	
	initCustomerInfoSel();
//	initCustomerNatureSel($("#custProperty").val());
	
	// 初始化客户
  	loadCusts($("#saleManId").val());
	
	//打开用户帐号
//    $('#openDispacher').click(
//	   	function(e){
//	   		dialog = new UserDialog(ERMP_PATH, BASE_PATH);
//	   		dialog.setCallbackFun(callbackFun);
//	   		var user = dialog.openDialog();
//		}
//	);

    //客户经理默认为当前登入者
	$("#custManage").val($("#cusManageName").val());
	$("#custManageId").val($("#saleManId").val());
	
	initProInfoSel();
	initProInfoSel();
	//初始化有无定金下拉框
	initEarnestSel();
	
	$("#commitBut").find(".allBtn").each(function(){
		$(this).click(function(){
			var operator = $(this).val();
			var commId = "";//处理意见id
			if($("#commId").size() != 0){
				commId = $("#commId").val();
		  	}
		  	//任务实例ID
		  	var tiid = $("#taskInstanceId").val(); 
		  	//填写的意见
			var comment = $.trim($("#comment").val());
			
			if (operator=="作废") {
				endTransition(tiid, commId,comment,operator);
			}else {
				saveAndEndTransition(tiid, commId,comment,operator);
			}
  		});
	});
	
	blur();
}

function endTransition (tiid, commId,comment, transitionName) {
	
	//设置保存按钮禁用
	setCommitButtonDisabled(true);
	
	if($.validInput("comment", "处理意见", true, null, 1000)){
		setCommitButtonDisabled(false);
	  	return false;
	  }
	
	var custPaymentId = $("#custPaymentId").val();
	
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       dataType : "json",
       url  : "m/operationsManage/dealPayment",
       data : {
    	   		id : custPaymentId,
       			tiid:tiid,
       			transition : transitionName,
       			comment:comment,
       			taskCommentId : commId
       		},
       success : function(data){
	       	if ($.checkErrorMsg(data)) {
	       		// 刷新父列表
	    		mainPanel.getCurrentTab().doCallback();
	    		// 关闭
	    		mainPanel.getCurrentTab().close();
	       	} else {
        		setCommitButtonDisabled(false);
        	}
       },
       error : $.ermpAjaxError
   	});
}	

/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){
	
	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?pageSize=500&multipleCustomerStatus=5" + "&format=json&jsoncallback=?";

	$.getJSON(url,function(data){
		$("#customerInfoSel").html(data.htmlValue);
		
		customerInfoSel = $("#customerInfoSel").ySelect({
			width:107,
			popwidth:140,
			afterLoad : function() {
				var custId = $("#customerId").val();
				if (customerInfoSel != null && custId != null && custId != "") {
					customerInfoSel.select(custId);
				} 
				initCustomerNatureSel($("#custProperty").val());
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
					var prodId = $("#prodId").val();
					if(prodInfoSel != null && prodId != null && prodId != ""){
						prodInfoSel.select(prodId);
						synchronousProdInfo(prodId);
					}
				},
				onChange : function(value, name) {
					//同步剩余额度，已划款总额度
					synchronousProdInfo(value);
				}
			});
	    }
	});
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
					customerNatureSel.select(custProperty + "");
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
	var payDepositFlag = $("#payDepositFlag").val();
	
	if(earnestSel != null && payDepositFlag != null && payDepositFlag == "true"){
		earnestSel.select("1");
	} else if(earnestSel != null && payDepositFlag != null && payDepositFlag == "false"){
		earnestSel.select("0");
	} else {
		earnestSel.select(0);
	}
}


/**
 * 封装数据
 * return paymentJson
 */
function getPaymentJson(payType){
	var paymentJson = null;
	
	var custId = null;
	var custName = null;
	if(payType == "1"){
		 custId = customerInfoSel.getValue();
		 custName = customerInfoSel.getDisplayValue();
	} else if(payType == "0"){
		 custId = $.trim($("#custId").val());
		 custName = $.trim($("#custName").val());
	}
	
	var payment = new Object;
	payment.id = $("#custPaymentId").val();
	payment.saleManId = $.trim($("#custManageId").val());
	payment.transferDate = $("#transferDate").val();
	payment.custId = custId;
	payment.custName = custName;
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
	
	var identityNum = $("#identityNum").val();
	var janeEditionAttch = null;
	if (janeEditionObj && janeEditionObj.getHasValueFlag()) {
		if (janeEditionObj.getTempDir() != "") {
			janeEditionAttch = new Object();
			janeEditionAttch.filePath = janeEditionObj.getTempDir();
			janeEditionAttch.displayName = janeEditionObj.getFileName();
		} else {
			janeEditionAttch = new Object();
		}
	}
	payment.identityNum = identityNum;
	payment.paymentReceipt = janeEditionAttch;
	
	paymentJson = $.toJSON(payment);
	
	return paymentJson;
}

function saveAndEndTransition (tiid,commId, comment, transitionName) {
	
	var payType = $("#payType").val();
	
	//保存之前判断,验证非空、长度、规则判断
	if(!__OnBeforeSave(payType)){
		return;
	}
	
	var paymentJson = getPaymentJson(payType);
	
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       dataType : "json",
       url  : "m/operationsManage/modifyPaymentAndEndTransition",
       data : {
    	   		paymentJson : paymentJson,
       			tiid : tiid,
       			transition : transitionName,
       			comment : comment,
       			taskCommentId : commId
       		},
       success : function(data){
	       	if ($.checkErrorMsg(data)) {
	       		// 刷新父列表
	    		mainPanel.getCurrentTab().doCallback();
	    		// 关闭
	    		mainPanel.getCurrentTab().close();
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
function __OnBeforeSave(payType){
	
	//设置保存按钮禁用
	setCommitButtonDisabled(true);
	
	var custId = null;
	var custName = null;
	
	if(payType == "1"){
		 custId = customerInfoSel.getValue();
		 custName = customerInfoSel.getDisplayValue();
	} else if(payType == "0"){
		 custId = $.trim($("#custId").val());
		 custName = $.trim($("#custName").val());
	}
	
	var transferDate = $("#transferDate").val();
	var custProperty = customerNatureSel.getValue();
	var prodId = prodInfoSel.getValue();
	var payDepositFlag = earnestSel.getValue();
	var appointmentAmount = $.trim($("#appointmentAmount").val());
	var appointmentAmountCapital = $("#appointmentAmountCapital").text();
//	var approver = $.trim($("#dispacher").val());
	var janeEditionAttch = null;
	if (janeEditionObj && janeEditionObj.getHasValueFlag()) {
		if (janeEditionObj.getTempDir() != "") {
			janeEditionAttch = new Object();
			janeEditionAttch.filePath = janeEditionObj.getTempDir();
			janeEditionAttch.displayName = janeEditionObj.getFileName();
		} else {
			janeEditionAttch = new Object();
		}
	}
	var identityNum = $.trim($("#identityNum").val());
	
	//客户
	if (custName == "") {
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
//	if($("#standardFlag").val() == "false"){
//		if (approver == "") {
//			alert("请选择审批人");
//			setCommitButtonDisabled(false);
//	  		return false;
//		}
//	}
	if($.validInput("remark", "备注", false, "\<\>\'\"", 256)){
		setCommitButtonDisabled(false);
		return false;
	}
	if (janeEditionAttch == null) {
		alert("请上传到款凭条");
		setCommitButtonDisabled(false);
		return false;
	}
	if(identityNum != null && identityNum != ""){
		if(!IdCardValidate(identityNum)){
			alert("身份证号输入错误！");
			setCommitButtonDisabled(false);
			return false;
		} 
	} else {
		alert("请输入身份证号！");
		setCommitButtonDisabled(false);
		return false;
	}
	if($.validInput("comment", "处理意见", true, null, 1000)){
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



function callbackFun() {
	var selected = dialog.selected;
		if (selected!=null){
			$("#dispacher").val(selected[0].id);
			$("#dispacherDis").val(selected[0].name);
		}
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
	alert("Invalid characters in the input string!");
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

/**
 * 加载客户数据
 */
function loadCusts(userAccountID) {
//	$.ajax({
//		type : "POST",
//		cache : false,
//		url : CRM_PATH + "m/customer/queryCustomerInfoList?customerManage=" + userAccountID + "&customerStatus=3",
//		dataType : "json",
//		success : function(data) {
//			initAutoComp(data);
//		}
//	});
	
	var url = CRM_PATH + "m/customer/queryCustomerInfoList?customerManage=" + userAccountID + "&customerStatus=3" + "&format=json&jsoncallback=?";
	$.getJSON(url,function(data){
		initAutoComp(data);
	});
}

/**
 * 自动补全
 * @param data 数据
 */
function initAutoComp(data) {
	if(data.listPage.dataList){
		$("#custName").autocomplete(data.listPage.dataList, {
			minChars : 0,
			width : 135,
			matchContains : true,
			autoFill : false,
			max : data.listPage.dataList.length,
			formatItem : function(row, i, max) {
				return row.custName;
			},
			formatResult : function(row) {
				return row.custName;
			}
		});
		$("#custName").result(callback).next().click(function() {
			$(this).prev().search();
		});
		// 绑定事件
		$("#custName").blur(function() {
			if ($(this).val() != $("#oriSelected").val()) {
//				$("#custName").val("");
				$("#oriSelected").val("");
				$("#custId").val("");
			}
		});
	}
}

/**
 * 客户数据回调方法
 * @param event 时间
 * @param data 客户
 * @param formatted 格式
 */
function callback(event, data, formatted) {
	var custId = (data != null && data.id != null ? data.id : "");
	if (data != null && data.id != null) {
		$("#custName").val(data.custName);
		$("#oriSelected").val(data.custName);
		$("#custId").val(custId);
		//同步客户性质 
		synchronousCustInfo(custId);
	} else {
		$("#custName").val("");
		$("#oriSelected").val("");
		$("#custId").val("");
		//同步客户性质 
		synchronousCustInfo("");
	}
}

//身份证校验
var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X   
function IdCardValidate(idCard) { 
    idCard = trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
    if (idCard.length == 18) {
        var a_idCard = idCard.split("");                // 得到身份证数组   
        if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
            return true;   
        }else {   
            return false;   
        }   
    } else {   
        return false;   
    }   
}   
/**  
 * 判断身份证号码为18位时最后的验证位是否正确  
 * @param a_idCard 身份证号码数组  
 * @return  
 */  
function isTrueValidateCodeBy18IdCard(a_idCard) {   
    var sum = 0;                             // 声明加权求和变量   
    if (a_idCard[17].toLowerCase() == 'x') {   
        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
    }   
    for ( var i = 0; i < 17; i++) {   
        sum += Wi[i] * a_idCard[i];            // 加权求和   
    }   
    valCodePosition = sum % 11;                // 得到验证码所位置   
    if (a_idCard[17] == ValideCode[valCodePosition]) {   
        return true;   
    } else {   
        return false;   
    }   
}   
/**  
  * 验证18位数身份证号码中的生日是否是有效生日  
  * @param idCard 18位书身份证字符串  
  * @return  
  */  
function isValidityBrithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
    // 这里用getFullYear()获取年份，避免千年虫问题   
    if(temp_date.getFullYear()!=parseFloat(year)   
          ||temp_date.getMonth()!=parseFloat(month)-1   
          ||temp_date.getDate()!=parseFloat(day)){   
            return false;   
    }else{   
        return true;   
    }   
}   
  /**  
   * 验证15位数身份证号码中的生日是否是有效生日  
   * @param idCard15 15位书身份证字符串  
   * @return  
   */  
  function isValidityBrithBy15IdCard(idCard15){   
      var year =  idCard15.substring(6,8);   
      var month = idCard15.substring(8,10);   
      var day = idCard15.substring(10,12);   
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
      if(temp_date.getYear()!=parseFloat(year)   
              ||temp_date.getMonth()!=parseFloat(month)-1   
              ||temp_date.getDate()!=parseFloat(day)){   
                return false;   
        }else{   
            return true;   
        }   
  }   
//去掉字符串头尾空格   
function trim(str) {   
    return str.replace(/(^\s*)|(\s*$)/g, "");   
}