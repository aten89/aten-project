/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 性别下拉选择框
 */
var sexSel;
/**
 * 客户性质下拉选择框
 */
var customerNatureSel;
/**
 * 沟通类型下拉选择框
 */
var commTypeSel;
//产品下拉框
var prodInfoSel;

$(initEditcustomerinfo);
function initEditcustomerinfo(){
	// 初始化下拉框
	initSexSel();
	initCustomerNatureSel();
	initCommTypeSel();
	initProdInfoSel();
	
	if($("#commit").size() > 0){
		$("#commit").click(function(){
			$.confirm("是否提交回访？", function(r){
				if (r) {
					commitCustomer("returnVisit");
				}
			});
		});
	}
	//转正式
	if($("#toFomal").size() > 0){
		$("#toFomal").click(function(){
			commitCustomer("fomal");
		});
	}
	// 保存
	$("#save").click(function() {
		saveCustomer();
	});
	
	//取消
	$("#close").click(function() {
		args.returnValue = false;
		args.callback();
	});
}

/**
 * 初始化性别下拉选择框
 */
function initSexSel() {
	var sex = $("#sex").val();
	if ($("#sexSel").size() > 0) {
		sexSel = $("#sexSel").ySelect({
			width : 100,
			url : BASE_PATH + "/l/dict/initSexSel",
			afterLoad : function() {
				sexSel.addOption("", "请选择...", 0);
				// 设置默认值
				if(sex == ""){
					sexSel.select(0);
				}else{
					sexSel.select(sex);
				}
			}
		});
	}
}

/**
 * 初始化客户性质下拉选择框
 */
function initCustomerNatureSel() {
	var custProperty = $("#custProperty").val();
	if ($("#customerNatureSel").size() > 0) {
		customerNatureSel = $("#customerNatureSel").ySelect({
			width : 100,
			url : BASE_PATH + "/l/dict/initCustomerNatureSel",
			afterLoad : function() {
				customerNatureSel.addOption("", "请选择...", 0);
				// 设置默认值
				if(custProperty == ""){
					customerNatureSel.select(1);
				}else{
					customerNatureSel.select(custProperty);
				}
			}
		});
	}
}

/**
 * 初始化沟通类型下拉选择框
 */
function initCommTypeSel(){
	var communicationType = $("#communicationType").val();
	if ($("#commTypeSel").size() > 0) {
		commTypeSel = $("#commTypeSel").ySelect({
			width : 100,
			url : BASE_PATH + "/l/dict/initCommTypeSel",
			afterLoad : function() {
				commTypeSel.addOption("", "请选择...", 0);
				// 设置默认值
				if(communicationType == ""){
					commTypeSel.select("TEL");
				}else{
					commTypeSel.select(communicationType);
				}
			}
		});
	}
}

function initProdInfoSel() {
	var recommendProduct = $("#recommendProduct").val();
	if ($("#prodInfoSel").size() > 0) {
		prodInfoSel = $("#prodInfoSel").ySelect({
			width : 107,
			height: 150,
			url : POSS_PATH + "/m/prod_info/initProdInfoSel",
			afterLoad : function() {
				prodInfoSel.addOption("", "请选择...", 0);
				// 设置默认值
				if (recommendProduct != "") {
					prodInfoSel.selectByName(recommendProduct);
				} else {
					prodInfoSel.select(0);
				}
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

/**
 * 保存/修改
 */
function saveCustomer(){
	if(!validateForm()){
		return;
	}
	var customerJson = getCustomerJson;	
	var act = $("#customerId").val() == ""  ? "add" : "modify";
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/customer/" + act, 
		   dataType : "json",
		   data : {
				customerJson : customerJson
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
 * 提交
 */
function commitCustomer(flag){
	if(!validateForm()){
		return;
	}
	var customerJson = getCustomerJson;
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/customer/modify_commit", 
		   dataType : "json",
		   data : {
				customerJson : customerJson,
				flag : flag
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
 * 封装数据
 * @return {Boolean}
 */
function getCustomerJson(){
	var customer = {};
	var customerId = $("#customerId").val();
   	if (customerId) {
   		customer.id = customerId;
   	} else {
   		customer.id = "";
   	}
   	
   	customer.custName = $.trim($("#custName").val());
   	customer.sex = sexSel.getValue() == "" ? "" : sexSel.getValue();
   	customer.custProperty = customerNatureSel.getValue() == "" ? "" : customerNatureSel.getValue();
   	customer.birthday = $("#birthday").val();	
   	customer.age = $.trim($("#age").val());
   	customer.tel = $.trim($("#tel").val());
   	customer.email = $.trim($("#email").val());
   	customer.address = $.trim($("#address").val());
   	customer.saleMan = $.trim($("#saleMan").val());
   	customer.financingAmount = $.trim($("#financingAmount").val());
   	customer.expectedInvestAgelimit = $.trim($("#expectedInvestAgelimit").val());
   	customer.investExperience = $.trim($("#investExperience").val());
   	customer.expectedProduct = $.trim($("#expectedProduct").val());
   	if (prodInfoSel.getValue() != "") {
   		customer.recommendProduct = prodInfoSel.getDisplayValue() == "" ? "" : prodInfoSel.getDisplayValue();
   	}
   	customer.communicationType = commTypeSel.getValue() == "" ? "" : commTypeSel.getValue();
   	customer.communicationResult = $.trim($("#communicationResult").val());
//   	var identityNum = $.trim($("#identityNum").val());
//   	customer.identityNum = identityNum;
   	var status = $.trim($("#status").val());
   	if (status == "4" || status == "5") {
   		customer.bankName = $.trim($("#bankName").val());
   		customer.bankBranch = $.trim($("#bankBranch").val());
   		customer.bankAccount = $.trim($("#bankAccount").val());
   	}
   	
	var customerJson = $.toJSON(customer);
	return customerJson;
}

/**
 * 表单验证
 * @return {Boolean}
 */
function validateForm(){
	if($.validInput("custName", "客户名称", true, "\<\>\'\"", 128)){
		return false;
	}
	
  	if(sexSel.getValue() == ""){
  		$.alert("性别不能为空");
  		return false;
  	}
  	if(customerNatureSel.getValue() == ""){
  		$.alert("客户性质不能为空");
  		return false;
  	}
  	
  	var regPartton=/^1\d{10}$/;
  	var str = $("#tel").val();
	if(!str || str==null){
		$.alert("电话号码不能为空");
		return false;
//	}else if(!regPartton.test(str)){
//		$.alert("手机号码格式不正确");
//		return false;
	}
	
	var status = $.trim($("#status").val());
	if (status == "4" || status == "5") {
		if($.validInput("bankName", "银行名称", true, "\<\>\'\"", 128)){
			return false;
		}
		
		if($.validInput("bankBranch", "开户行", true, "\<\>\'\"", 128)){
			return false;
		}
		
		if($.validInput("bankAccount", "银行账户", true, "\<\>\'\"", 128)){
			return false;
		}
	}
	
//	var identityNum = $.trim($("#identityNum").val());
//	if(identityNum != null && identityNum != ""){
//		if(!IdCardValidate(identityNum)){
//			alert("身份证号输入错误！");
//			return false;
//		}
//	}
	if($.validNumber("age", "年龄", false, 200)){
		return false;
	}
	if($.validNumber("financingAmount", "理财金额(万)", false)){
		return false;
	}
	if($.validNumber("expectedInvestAgelimit", "期望投资年限", false, 1000)){
		return false;
	}

	return true;
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


