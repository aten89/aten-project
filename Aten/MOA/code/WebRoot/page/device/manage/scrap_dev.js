var mainFrame = $.getMainFrame();
var dialogWin;
var deviceTypeSel;
var deviceClassSel;
var scrapTypeSel;
var scrapDisposeTypeSel;
$(initEquipmentInfoScrap);

/**
 * 设备报废
 */
function initEquipmentInfoScrap() {
	var deviceTypeCode = $("#deviceTypeCode").val();
	initScrapTypeSel(deviceTypeCode,"");
	initScrapDisposeTypeSel(deviceTypeCode,"", $("#buyType").val());
	initDeviceTypeSel(deviceTypeCode);
	$("#openUserSelect").click(function(){
		selectSingleUser();
	});
	$("#btnSelectDevice").click(function(){
		selectDevice();
	});
	if ($("#deviceID").val() != "") {
		$("#btnSelectDevice").hide();
		if ($("#owner").val() != "") {
			$("#openUserSelect").hide();
		} else {
			$("#openUserSelect").show();
		}
	}
}

/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				deviceTypeSel.disable(true);
			} else {
				deviceTypeSel.select(0);
			}
		},
		onChange : function(value, text) {
			initDeviceClassSel(value, $.trim($("#deviceClassCode").val()));
		}
	});     
}

/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode, deviceClassCode) {
	deviceClassSel = $("#deviceClassSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/device_class?act=classselect&deviceType=" + deviceTypeCode,
		afterLoad : function() {
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
				deviceClassSel.disable(true);
			} else {
				deviceClassSel.select(0);
			}
		}, 
		onChange : function(value, text) {
		}
	});     
}

/**
 * 初始化报废类型下拉列表
 * @param {} scrapType 报废类型
 */
function initScrapTypeSel(deviceType,scrapType) {
	if(deviceType==null || deviceType==""){
		allotTypeSel = $("#allotTypeSel").ySelect({
		width : 107
		});   
		allotTypeSel.addOption("", "---请选择---", 0);
		allotTypeSel.select(0);
		return;
	}
	scrapTypeSel = $("#scrapTypeSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=selectscraptype&deviceType="+deviceType,
		afterLoad : function() {
			scrapTypeSel.addOption("", "---请选择---", 0);
			if (scrapType != null && scrapType != "") {
				scrapTypeSel.select(scrapType);
			} else {
				scrapTypeSel.select(0);
			}
		}
	}); 
}

/**
 * 初始化报废类型处理方式下拉列表
 * @param {} scrapDisposeType 报废类型处理方式
 */
function initScrapDisposeTypeSel(deviceType,scrapDisposeType, buyType) {
	scrapDisposeTypeSel = $("#scrapDisposeTypeSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=selectscrapdisposetype&deviceType="+deviceType,
		afterLoad : function() {
			scrapDisposeTypeSel.addOption("", "---请选择---", 0);
			if (scrapDisposeType != null && scrapDisposeType != "") {
				scrapDisposeTypeSel.select(scrapDisposeType);
			} else {
				scrapDisposeTypeSel.select(0);
			}
			if (buyType == null || buyType == "") {
				scrapDisposeTypeSel.disable(true);
			} else if (buyType == "BUY-TYPE-COMP") {
				//公司全额，应过滤掉离职回购
        		$("#scrapDisposeTypeSel input[value='SCRAP_DISPOSE_TAKE']").parent().hide();
			}
		},
		onChange : function(value,text) {
			if (value == "SCRAP_DISPOSE_TAKE") {
				$("#trBackBuy").show();
			} else {
				$("#trBackBuy").hide();
			}
		}
	}); 
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
//	var sFeature = "dialogHeight:430px;dialogWidth:748px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:754px;status:no;scroll:auto;help:no";
//	}
	var args = new Object();
	args.assignFlag = true;
	args.assignType = 0;
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.areaCode = $.trim($("#areaCode").val());
	args.status = 1;
	args.buyType = "BUY-TYPE-SELF";
	
	window.dialogParams = args;
	dialogWin = $.showModalDialog("设备选择", sURL, 660, 378, function(){
		if (window.returnValue1) {
			var selectDeviceID = args.selectDeviceID;//已选择的设备ID列表
			var selectDeviceName = args.selectDeviceName;
			var selectDeviceModel = args.selectDeviceModel;
			var selectDeviceNo = args.selectDeviceNo;
			var buyTime = args.buyTime;
			var applicantID = args.userId;
			var applicantName = args.userName;
			var buyType = args.buyType;
			var buyTypeStr = args.buyTypeStr;
			$("#deviceID").val(selectDeviceID);
	        $("#deviceName").val(selectDeviceName);
	        $("#deviceNO").text(selectDeviceNo);
	        $("#deviceModel").text(selectDeviceModel);
	        $("#buyTime").val(buyTime);
	        $("#applicantName").val(applicantName);
	        $("#applicantID").val(applicantID);
	        $("#buyType").val(buyType);
	        $("#buyTypeStr").text(buyTypeStr);
	        if (buyTypeStr != null || buyTypeStr != "") {
	        	scrapDisposeTypeSel.disable(false);
	        }
	        initScrapDisposeTypeSel($("#deviceTypeCode").val(),"", $("#buyType").val());
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

function saveInfo(){
	var buyTime = $("#buyTime").val();
	var applyTime =$("#applyDate").val();
	var deviceName =$("#deviceName").val();
	var deviceId =$("#deviceID").val();
	if(deviceName==""){
		$.alert("“设备名称”不能为空,请选择设备！");
		$("#deviceName").focus();
		return false;
	}
	var remaining = $("#remaining").val();
	if(validatePriceField(remaining,deviceName+"设备余值",true)){
		return false;
	}
	var depreciation = $("#depreciation").val();
	if(validatePriceField(depreciation,deviceName+"已提折旧",true)){
		return false;
	}
	var deviceType = $("#deviceTypeCode").val();
	var scrapType = scrapTypeSel.getValue();
	if(scrapType==""){
		$.alert("请选择报废类型！");
		return false;
	}
	var scrapDisposeType = scrapDisposeTypeSel.getValue();
	if(scrapDisposeType==""){
		$.alert("请选择设备处理方式！");
		return false;
	}
	var discardDate = $("#discardDate").val();
	if(discardDate==""){
		$.alert("设备“报废日期”不能为空！");
		$("#discardDate").focus();
		return false;
	}
	var workYear=$.trim($("#workYear").val());
	var enterCompanyDate=$.trim($("#enterCompanyDate").val());
   	var buyPrice = $("#buyPrice").val();
	if (scrapDisposeType == "SCRAP_DISPOSE_TAKE") {
   		if(enterCompanyDate==""){
			$.alert("入司时间不能为空")
	  		return false;
	  	}
	  	if(workYear==""){
			$.alert("员工工龄不能为空");
			$("#workYear").focus();
	  		return false;
	  	}
	  	var re = new RegExp("^([-]){0,1}([0-9]){1,}([.]){0,1}([0-9]){0,}$","igm");
		if(!re.test(workYear)){
	   		$.alert("员工工龄不是有效的数字类型");
	   		$("#workYear").focus();
	   		return true;
	   	}
   		//处理方式是回购的，要输入回购价格
   		if(validatePriceField(buyPrice,deviceName+"回购价格",true)){
			return false;
		}
   	} else {
   		$("#enterCompanyDate").val("");
   		$("#workYear").val("");
   		$("#buyPrice").val("");
   	}
   	
	var  userId =$("#applicantID").val();
	if(userId==""){
		$.alert("“申请人”不能为空，请选择申请人！");
		$("#applicantID").focus();
		return false;
	}
	
	if(applyTime==""){
		$.alert("“申请日期”不能为空！");
		$("#applyTime").focus();
		return false;
	}
	var remark = $.trim($("#reason").val());
	result = $.validChar(remark,"\"<>");
	if (result){
		$.alert("“报废原因”不能输入非法字符：" + result+"！");
		$("#reason").focus();
		return true;
	}
	if(remark.length>300){
		$.alert("“报废原因”不能超过300个字符！");
		$("#reason").focus();
		return true;
	}
	if(applyTime<buyTime){
		$.alert("“申请日期”不能小于设备“购买日期”"+buyTime+"！");
		$("#applyTime").focus();
		return false;
	}
	var msg = msgSuccess(deviceId);
	if(msg!=""){
//		showMsg="该设备目前正在"+msg+"，如果进行“报废登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//		if(!confirm(showMsg)){
//			return ;
//		}
		showMsg="该设备目前正在"+msg+"，您当前不能进行“报废登记”操作。";
		$.alert(showMsg);
		return;
	}
	var groupName = $("#applyGroupName").text();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/it_dev_man",
		data : {
			act: "scrap",
			deviceId : deviceId,
			deviceType : deviceType,
			userId:userId,
			scrapType:scrapType,
			scrapDisposeType:scrapDisposeType,
			groupName:groupName,
			discardDate:discardDate,
			depreciation : depreciation,
			remaining : remaining,
			workYear : workYear,
			enterCompanyDate : enterCompanyDate,
			buyPrice : buyPrice,
			applyTime:applyTime,
			remark:remark,
			formType : 0//正常报废
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


function selectSingleUser(){
	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	selector.setCallbackFun(function(user){
		if (user != null) {
			$("#applicantID").val(user.id);
			$("#applicantName").val(user.name);
			$("#applyGroupName").text(user.deptName);
		}
	});
	selector.openDialog("single");
}
function msgSuccess(id){
	var msgSuccess="";
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/it_dev_man?act=getstatus",//
		data:{
			id : id
		},
		success:function saveSuccess(xml){
			//$.$.alert($(xml).get(0).xml)
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				msgSuccess = message.text();
			}
			else{
				$.alert(message.text());
				return;
			}
		},
        error:$.ermpAjaxError
    }); 
    return msgSuccess;
}

/**
 * 验证价格字段格式正确与否。正确格式为：最多9位整数，如有小数，小数位数最多不能超过2位
 * @param {} input　输入框name
 * @param {} label　字段名
 * @param {} required　是否必填
 * @return {Boolean}　false:验证通过;true:验证不通过
 */
function validatePriceField(v, label, required) {
	if(typeof(required) == "undefined" || !required) {
		required = false;
	} else {
		required = true;
	}
	if(required && v == "") {
		//必填
		$.alert("“" + label + "”不能为空");
		return true;
	}
	//整数位数最多9位，小数位数最多2位
	var re = /^\d{1,9}(\.\d{1,2})?$/;
	if(v != "" && !re.test(v)) {
		$.alert("“" + label + "”格式不正确：最多9位整数，如有小数，小数位数最多不能超过2位");
		return true;
	}
}

