var userSexSel;//性别
$(init);//程序入口


function init(){
	$.handleRights({
		"btnSave" : $.SysConstants.MODIFY,
		"btnReset" : $.SysConstants.MODIFY
	});	
	//绑定按钮点击事件
	bindEvent();
	initUserSex();
	
	//设置时间控件：当前选中时间往前60年
	$.datepicker.setDefaults({yearRange:'-60:+60'});

}


//初始化性别下拉选择框
function initUserSex(){
	//性别
	userSexSel = $("#userSexSel").ySelect({width: 100});
	var userSex = $.trim($("#userSex").val());
	if (userSex != "") {
		userSexSel.select(userSex);
	}
}


function bindEvent(){	
	
	//保存
	$("#btnSave").click(function(){
        	saveAddrList();
        }
    );
    
     //重置
    $("#btnReset").click(function(){
			document.addresslistForm.reset();
			userSexSel.select($("#userSex").val());
			
			var saveImgPath = $("#saveImgPath").val();
		    var fileName = "";
		    if(saveImgPath == ""){
		    	//数据库中没有头像时,显示默认头像
		    	fileName = BASE_PATH + "themes/comm/images/hd_default.jpg";//默认头像
		    } else {
		    	//数据库中有头像时显示数据库中的头像
		    	fileName = BASE_PATH + saveImgPath;
		    }  
		    $("#iframeHD").get(0).contentWindow.$("#imgDiv").html("<img src='"+fileName +"' width=102px height=103px>");	 
		    $("#iframeHD").get(0).contentWindow.$("#tempImgPath").val(""); 
		    $.post("m/addrlist?act=resetpic");
		}
    );
}

//表单验证
function validateForm(){
	if($.validInput("employeeNumber", "工号", false, "\<\>\'\"", 30)){
		return false;
	}
	
	if($.validInput("seatNumber", "座位号", false, "\<\>\'\"", 10)){
		return false;
	}	
	
	//验证移动电话格式(小灵通和手机)
  	var mobileRE = /^((\(\d{3}\))|(\d{3}\-))?\d{11}$/;
	var telRE = /^\d{3,4}-\d{7,9}$/;
	var mobileErrorMsg = "移动电话格式不正确。\n支持手机和小灵通号格式。\n是小灵通的，请按照\"区号-小灵通号\"的格式输入。";
	if ($("#userMobile").val() != "" && !mobileRE.test($("#userMobile").val()) && !telRE.test($("#userMobile").val())) {
		$.alert(mobileErrorMsg); 
		return false;
	} 
	
	//验证办公电话
	if($.validTel("userOfficeTel", "办公电话", false)){
		return false;
	}
	
	
	//验证E-mail格式
	if($.validInput("userEmail", "E-mail", false, "\<\>\'\"", 50)){//验证长度
		return false;
	}
	if($.validEmail("userEmail", "E-mail")){//验证格式
		return false;
	}
	
	//验证QQ
	if($.validNumber("userQQ", "QQ")){//验证格式
		return false;
	} else if ($.validInput("userQQ", "QQ", false, "", 20)) {//验证长度
		return false;
	}
	

	if($.validInput("userMSN", "msn", false, "\<\>\'\"", 50)){
		return false;
	}
	
	if($.validInput("userNickName", "昵称", false, "\<\>\'\"", 30)){
		return false;
	}
	
	if($.validInput("userNativePlace", "籍贯", false, "\<\>\'\"", 30)){
		return false;
	}
	
	if($.validInput("userNation", "民族", false, "\<\>\'\"", 30)){
		return false;
	}	
	
	if($.validInput("userCommAddr", "通讯地址", false, "\<\>\'\"", 100)){
		return false;
	}
	
	if($.validZipCode("zipCode", "通讯地址邮编", false)){
		return false;
	}
	
	if($.validInput("userHomeAddr", "家庭住址", false, "\<\>\'\"", 100)){
		return false;
	}
	
	if($.validTel("userHomeTel", "家庭电话", false)){
		return false;
	}
	if($.validInput("remark", "备注", false, "\<\>\'\"", 4000)){
		return false;
	}
	if($.validInput("userPhoto", "照片路径", false, "\<\>\'\"", 500)){
		return false;
	}
	return true;
}
	
//保存
function saveAddrList(){
	if(!validateForm()){
		return;
	}

	var sId = $("#id").val();
	var sUserAccountId = $("#userAccountId").text();
	var sEmployeeNumber = $.trim($("#employeeNumber").val());	
	var sSeatNumber = $.trim($("#seatNumber").val());
	var sUserEnterCorpDate = $("#userEnterCorpDate").val();	
	var sUserMobile = $.trim($("#userMobile").val());
	var sUserOfficeTel = $.trim($("#userOfficeTel").val());
	var sUserEmail = $.trim($("#userEmail").val());
	var sUserQQ = $.trim($("#userQQ").val());	
	var sUserMSN = $.trim($("#userMSN").val());
	var sUserNickName = $.trim($("#userNickName").val());	
	var sUserSex = "";
	if(userSexSel.getValue() != ""){
		sUserSex = userSexSel.getValue();//性别
	}
	
	var sUserBirthDate = $("#userBirthDate").val();
	var sUserNativePlace = $.trim($("#userNativePlace").val());
	var sUserNation = $.trim($("#userNation").val());
	var sUserCommAddr = $.trim($("#userCommAddr").val());
	var sZipCode = $.trim($("#zipCode").val());
	var sUserHomeAddr = $.trim($("#userHomeAddr").val());	
	var sUserHomeTel = $.trim($("#userHomeTel").val());	
	var sRemark = $.trim($("#remark").val());
	var sUserPhoto = $.trim($("#userPhoto").val());
	
	$.ajax({
		type : "POST",
       	cache: false,
       	url  : "m/myaddr?act=modify",
       	data : {
       			id : sId,
       			userAccountId : sUserAccountId,
       			employeeNumber : sEmployeeNumber,
       			seatNumber : sSeatNumber,   
       			userEnterCorpDate : sUserEnterCorpDate,			
       			userMobile : sUserMobile,
       			userOfficeTel : sUserOfficeTel,
       			userEmail : sUserEmail,
       			userQQ : sUserQQ,
       			userMSN : sUserMSN,
       			userNickName : sUserNickName,
       			userSex : sUserSex,
       			userBirthDate : sUserBirthDate,
       			userNativePlace : sUserNativePlace,
       			userNation : sUserNation,
       			userCommAddr : sUserCommAddr,
       			zipCode : sZipCode,
       			userHomeAddr : sUserHomeAddr,
       			userHomeTel : sUserHomeTel,
       			userNation : sUserNation,
       			remark : sRemark,
       			userPhoto : sUserPhoto
       		},
       	success : function _aft_saveAddrList(xml){
						var message = $("message", xml);
						var messageCode = message.attr("code");
						if(messageCode == "1"){
							$.alert("修改成功！", function(){
								window.location.href = BASE_PATH + "/m/myaddr?act=view"; 
							});
							
						}
						else{
							$.alert(message.text());
						}
					},
       	error : $.ermpAjaxError
   	});
}


