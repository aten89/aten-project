var mainFrame = $.getMainFrame();
var dialogWin;
$(initEquipmentInfoScrap);
/**
 * 设备报废
 */
function initEquipmentInfoScrap() {
	$("#bflx").ySelect({width:99});
	$("#btnSelectDevice").click(function(){
  		selectDevice();
	});
	 //打开用户帐号
	$("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#userId").val(user.id);
					$("#applicantName").val(user.name);
					$("#deptName").text(user.deptName);
				}
			});
			dialog.openDialog("single");
		}
	);
	if ($("#deviceID").val() != "") {
		$("#btnSelectDevice").hide();
		if ($("#owner").val() != "") {
			$("#openUserSelect").hide();
		} else {
			$("#openUserSelect").show();
		}
	}
}

function saveInfo(){
	var  applyTime =$("#applyDate").val();
	var  deviceName =$("#deviceName").val();
	var  deviceId =$("#deviceID").val();
	if(deviceName==""){
		$.alert("”设备名称“不能为空,请选择设备！");
		$("#deviceName").focus();
		return false;
	}
	var budgetMoney = $("#budgetMoney").val();
	if(validatePriceField("budgetMoney", "预算费用", true)){
  		return false;
  	}
	var  userId =$("#userId").val();
	if(userId==""){
		$.alert("请选择申请人！");
		$("#userId").focus();
		return false;
	}
	var  applyTime =$("#applyTime").val();
	if(applyTime==""){
		$.alert("”申请日期“不能为空！");
		$("#applyTime").focus();
		return false;
	}
	var  buyTime =$("#buyTime").val();
	if(applyTime<buyTime){
		$.alert("”申请日期“不能小于设备”购买日期“"+buyTime+"！");
		$("#applyTime").focus();
		return false;
	}
	var groupName = $("#deptName").text();
	var remark = $.trim($("#remark").val());
	result = $.validChar(remark,"\"<>");
	if (result){
		$.alert("”损坏原因“不能输入非法字符：" + result+"！");
		$("#remark").focus();
		return true;
	}
	if(remark.length>300){
		$.alert("“损坏原因”不能超过300个字符");
		$("#remark").focus();
		return true;
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/it_dev_man",
		data : {
			act: "maintain",
			deviceId : deviceId,
			userId:userId,
			groupName:groupName,
			budgetMoney:budgetMoney,
			applyTime:applyTime,
			remark:remark
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("保存成功", function(){
			    	 //刷新父列表
					mainFrame.getCurrentTab().doCallback();
			       //关闭
					mainFrame.getCurrentTab().close();
		    	});
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    });  
}

function doclose(){
	//关闭
	mainFrame.getCurrentTab().close();
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

/**
 * 设备选择
 */
function selectDevice() {
	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	var sURL = BASE_PATH + "m/it_dev_man?act=initsingledevselpage";
//	var sFeature = "dialogHeight:430px;dialogWidth:608px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:616px;status:no;scroll:auto;help:no";
//	}
	var args = new Object();
	args.assignFlag = true;
	args.assignType = 0;
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.areaCode = $.trim($("#areaCode").val());
	args.status = 1;
	
	window.dialogParams = args;
	dialogWin = $.showModalDialog("设备选择", sURL, 660, 378, function(){
		if (window.returnValue1) {
			var selectDeviceID = args.selectDeviceID;//已选择的设备ID列表
			var selectDeviceName = args.selectDeviceName;
			var selectDeviceModel = args.selectDeviceModel;
			var selectDeviceNo = args.selectDeviceNo;
			var buyTime = args.buyTime;
			var userId = args.userId;
			var applicantName = args.userName;
			$("#deviceID").val(selectDeviceID);
	        $("#deviceName").val(selectDeviceName);
	        $("#deviceNO").text(selectDeviceNo);
			$("#buyTime").val(buyTime);
			$("#applicantName").val(applicantName);
			$("#userId").val(userId);
			
			if (args.userId != "") {
	        	$("#openUserSelect").hide();
	        } else {
	        	$("#openUserSelect").show();
	        }
		}
	});
//	var result = $.showModalDialog(sURL, args, sFeature);
//	if (result) {
//		
//	}
}

/**
 * 验证价格字段格式正确与否。正确格式为：最多9位整数，如有小数，小数位数最多不能超过2位
 * @param {} input　输入框name
 * @param {} label　字段名
 * @param {} required　是否必填
 * @return {Boolean}　false:验证通过;true:验证不通过
 */
function validatePriceField(input, label, required) {
	var v = $("#" + input).val();
	if(typeof(required) == "undefined" || !required) {
		required = false;
	} else {
		required = true;
	}
	if(required && v == "") {
		//必填
		$.alert("“" + label + "”不能为空");
		$("#" + input).focus();
		return true;
	}
	//整数位数最多9位，小数位数最多2位
	var re = /^\d{1,9}(\.\d{1,2})?$/;
	if(v != "" && !re.test(v)) {
		$.alert("“" + label + "”格式不正确：最多9位整数，如有小数，小数位数最多不能超过2位！");
		$("#" + input).focus();
		return true;
	}
}







