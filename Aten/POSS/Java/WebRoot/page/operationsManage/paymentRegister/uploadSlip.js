/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;
var mainPanel = $.getMainFrame();

var janeEditionObj;

$(initPage);
function initPage(){
	
	
	janeEditionObj = $("#janeEditionAttchDIV").singleAttaUpLoad({
		divId : "janeEditionAttchDIV",
		oldShowId : "janeEditionAttchOldShow",
//		fileType : "xls",
		url : "m/operationsManage/save_attachment"
	});
	
	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			$("#transitionName").val($(this).val());
  			var commId = "";//处理意见id
		  	if($("#commId").size() != 0){
				commId = $("#commId").val();
		  	}
	  		var tiid = $("#taskInstanceID").val();//任务实例ID
			var comment = $.trim($("#comment").val());//填写的意见
			var transitionName = $(this).val();
			if (transitionName == "作废") {
				endTransition(tiid, commId, comment, transitionName);
			} else {
				commitDispose(commId, tiid, comment, transitionName);
			}
  		});
  	});
	
	blur();
}

/*
 * 驳回/作废
 */
function endTransition (tiid, commId,comment, transitionName) {
	//设置保存按钮禁用
	setCommitButtonDisabled(true);
	
//	if($.validInput("comment", "处理意见", true, null, 1000)){
//		setCommitButtonDisabled(false);
//	  	return false;
//	  }
	
	var id = $("#custPaymentId").val();
	var identityNum = $.trim($("#identityNum").val());
//	if(identityNum != null && identityNum != ""){
//		if(!IdCardValidate(identityNum)){
//			alert("身份证号输入错误！");
//			setCommitButtonDisabled(false);
//			return false;
//		}
//	} else {
//		alert("请输入身份证号！");
//		setCommitButtonDisabled(false);
//		return false;
//	}
	
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
	
	var custPayment = new Object();
	custPayment.id = id;
	custPayment.identityNum = identityNum;
	if(janeEditionAttch != null){
		custPayment.paymentReceipt = janeEditionAttch;
	}
	
	
	if (transitionName == "") {
		alert("请选择转向");
		setCommitButtonDisabled(false);
		return false;
	}	
	
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       dataType : "json",
       url  : "m/operationsManage/txModifyDealTask",
       data : {
    	   		paymentJson : $.toJSON(custPayment),
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
 * 提交流程动作
 * @param {} commId　划款处理意见id
 * @param {} tiid 
 * @param {} comment
 * @param {} transitionName 转向的名称
 * @return {Boolean}
 */
function commitDispose(commId, tiid, comment, transitionName){
	//设置保存按钮禁用
	setCommitButtonDisabled(true);
	
//	if($.validInput("comment", "处理意见", true, null, 1000)){
//		setCommitButtonDisabled(false);
//		return false;
//	}

	var id = $("#custPaymentId").val();
	var identityNum = $.trim($("#identityNum").val());
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
	
	if (janeEditionAttch == null) {
		alert("请上传到款凭条");
		setCommitButtonDisabled(false);
		return false;
	}
	
	var custPayment = new Object();
	custPayment.id = id;
	custPayment.identityNum = identityNum;
	custPayment.paymentReceipt = janeEditionAttch;
	
	if (transitionName == "") {
		alert("请选择转向");
		setCommitButtonDisabled(false);
		return false;
	}	
	$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       dataType : "json",
	       url  : "m/operationsManage/approvePayment",
	       data : {	
	    	   		paymentJson : $.toJSON(custPayment),
	       			tiid:tiid,
	       			transition : transitionName,
	       			taskCommentId : commId,
	       			comment:comment
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
