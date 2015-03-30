var mainFrame = $.getMainFrame();
var dialogWin;
var deviceTypeSel;
var deviceClassSel;
var areaSel;//区域下拉列表
var allotTypeSel;//调拨类型
//var purposeOptionSel;//设备工作用途
var allotTypeFlag =false;
var validMainDevFlag = false;//是否验证主设备
$(initEquipmentInfoAllot);

/**
 * 设备调拨
 */
function initEquipmentInfoAllot() {
	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	var deviceClassCode = $.trim($("#deviceClassCode").val());
	var areaCode = $.trim($("#areaCode").val());
	initDeviceTypeSel(deviceTypeCode, deviceClassCode);
	initAreaSel();
	initAllotTypeSel(deviceTypeCode,"");
	
	$("#btnSelectDevice").click(function(){
		selectDevice();
	});
	$("#openInAccountSelect").click(function(){
		selectSingleInAccount();
	});
	$("#openOutAccountSelect").click(function(){
		selectSingleOutAccount();
	});
	if ($("#deviceID").val() != "") {
		$("#btnSelectDevice").hide();
		$("#openOutAccountSelect").hide();
	}
//	if (areaCode != "" && deviceClassCode != "") {
//		initPurposeOptionSel(areaCode, deviceClassCode);
//	}else{
//		purposeOptionSel = $("#purposeOptionSel").ySelect({width : 107}); 
//		purposeOptionSel.addOption("", "---请选择--", 0);
//		purposeOptionSel.select(0);
//	}
}


function doclose(){
	 //关闭
	mainFrame.getCurrentTab().close();
}

/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode, deviceClassCode) {
	bPrompt = true;
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
			initDeviceClassSel(value, deviceClassCode);
		}
	});     
}

/**
 * 初始化所属区域下拉列表
 * @param {} areaCode 所属区域代码
 */
function initAreaSel(areaCode) {
	areaSel = $("#areaSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			areaSel.addOption("", "---请选择--", 0);
			if (areaCode != null && areaCode != "") {
				areaSel.select(areaCode);
			} else {
				areaSel.select(0);
			}
		}
	}); 
}

/**
 * 初始化调拨类型下拉列表
 * @param {} allotType 调拨类型
 */
function initAllotTypeSel(deviceType,allotType) {
	if(deviceType==null || deviceType==""){
		allotTypeSel = $("#allotTypeSel").ySelect({
		width : 107
		});   
		allotTypeSel.addOption("", "---请选择---", 0);
		allotTypeSel.select(0);
		return;
	}
	allotTypeSel = $("#allotTypeSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=selectallottype&deviceType="+deviceType,
		afterLoad : function() {
			allotTypeSel.addOption("", "---请选择--", 0);
			if (allotType != null && allotType != "") {
				allotTypeSel.select(allotType);
			} else {
				allotTypeSel.select(0);
			}
		},onChange:function() {
				if(allotTypeSel.getValue()=="IN_STORAGE" || allotTypeSel.getValue()=="ALLOT_BORROW"){
					$("#showInfo").hide();
					allotTypeFlag = false;
				}else {
					allotTypeFlag = true;
					$("#showInfo").show();
					
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
	args.isUsing = 1;
	if (allotTypeSel.getValue() == "ALLOT_INSIDE" || allotTypeSel.getValue() == "ALLOT_DEPT") {
		//部门之间、内部之间
		args.excludeSubBuyFlag = true;//过滤掉比例购买
	}
	args.excludeSelfBuyFlag = true;//过滤掉个人全额
//	args.buyType = "BUY-TYPE-SELF,BUY-TYPE-SUB";
	
	window.dialogParams = args;
	dialogWin = $.showModalDialog("设备选择", sURL, 660, 378, function(){
		if (window.returnValue1) {
			var selectDeviceID = args.selectDeviceID;//已选择的设备ID列表
			var selectDeviceName = args.selectDeviceName;
			var selectDeviceModel = args.selectDeviceModel;
			var selectDeviceNo = args.selectDeviceNo;
			var areaNameBef = args.areaNameBef;
			var groupName = args.groupName;
			var userId = args.userId;
			var areaCodeBef = args.areaCodeBef;
			var userName = args.userName;
			var buyTime = args.buyTime;
			var deviceClassID = args.deviceClassID;
			var purposeBef = args.purposeBef;
	//		var purposeNameBef = args.purposeNameBef;
			var deviceClassName= args.deviceClassName;
			var areaName= args.areaName;
			var areaCode= args.areaCode;
			var buyType= args.buyType;
			var buyTypeStr= args.buyTypeStr;
			$("#deviceID").val(selectDeviceID);
	        $("#deviceName").val(selectDeviceName);
	        $("#deviceNO").text(selectDeviceNo);
	        $("#deviceModel").text(selectDeviceModel);
			$("#areaNameBef").text(areaNameBef);
			$("#applyGroupName").text(groupName);
			$("#applicantID").val(userId);
			$("#applicantName").text(userName);
			$("#areaCodeBef").text(areaCodeBef);
			$("#buyTime").val(buyTime);
			$("#purposeBef").text(purposeBef);
	//		$("#purposeNameBef").text(purposeNameBef);
			$("#deviceClassCode").val(deviceClassID);
			
			$("#listDevBuyType").val(buyType);
			$("#listDevBuyTypeName").val(buyTypeStr);
			$("#listAreaName").val(areaName);
			$("#listAreaCode").val(areaCode);
			$("#listDeviceClassName").val(deviceClassName);
			$("#listDeviceClassID").val(deviceClassID);
	//		initPurposeOptionSel(args.areaCode, args.deviceClassID);
		}
	});
	
	
//	var result = $.showModalDialog(sURL, args, sFeature);
//	if (result) {
//	}
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
		}
	});     
}
/**
 * 初始化设备工作用途
 * @param {} deviceClassCode 设备类别代码
 */
//function initPurposeOptionSel(areaCode,deviceClassID) {
//	purposeOptionSel = $("#purposeOptionSel").ySelect({
//		width : 107, 
//		url:BASE_PATH+"m/it_dev_man?act=getareadevusedivtype&areaCode=" + areaCode + "&deviceClassID=" + deviceClassID,
//		afterLoad : function() {
//			purposeOptionSel.addOption("", "---请选择--", 0);
//			purposeOptionSel.select(0);
//		}
//	});     
//}

function selectSingleInAccount(){
	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	selector.setCallbackFun(function(user){
		if (user != null) {
			$("#inAccountID").val(user.id);
			$("#inAccountName").val(user.name);
			$("#inGroupName").text(user.deptName);
		}
	});
	selector.openDialog("single");
}

function selectSingleOutAccount(){
	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	var user = selector.openDialog("single");
	if (user != null){
		$("#applicantID").val(user.id);
		$("#applicantName").val(user.name);
		$("#applyGroupName").text(user.deptName);
	}
}

function saveInfo(){
	var deviceTypeCode = $("#deviceTypeCode").val();
	var allotType = allotTypeSel.getValue();
	var applicantID =$("#applicantID").val();
	if(allotType==""){
		$.alert("请选设备调拨类型！");
		return false;
	}
	var buyTime = $("#buyTime").val();
	var moveDate = $("#moveDate").val();
	if(moveDate==""){
		$.alert("“调拨日期”不能为空！");
		return false;
	}
	if(moveDate<buyTime){
		$.alert("“调拨日期”不能小于设备“购买日期”"+buyTime+"！");
		return false;
	}
	var  deviceName =$("#deviceName").val();
	var  deviceId =$("#deviceID").val();
	if(deviceName==""){
		$.alert("“设备名称”不能为空,请选择设备！");
		$("#deviceName").focus();
		return false;
	}
	var inAccountID ="";
//	var purposeOption ="";
	var areaCode="";
	if(allotTypeFlag){
		inAccountID =$("#inAccountID").val();
		if(inAccountID==""){
			$.alert("设备“调入经办人”不能为空！");
			$("#inAccountID").focus();
			return false;
		}
//		if(inAccountID==applicantID){
//			$.alert("设备调入经办人不能与调出经办人相同！");
//			return false;
//		}
//		purposeOption = purposeOptionSel.getValue();
//		if(purposeOption==""){
//			$.alert("请选择设备用途！");
//			return false;	
//		}
		areaCode =areaSel.getValue();
		if(areaCode==""){
			$.alert("请选择工作所在地！");
			return false;
		}
		
	}
	
	if(applicantID==""){
		$.alert("设备“调出经办人”不能为空");
		$("#applicantID").focus();
		return false;
	}
	var remark = $.trim($("#reason").val());
	result = $.validChar(remark,"\"<>");
	if (result){
		$.alert("“调拨原因”不能输入非法字符：" + result);
		$("#reason").focus();
		return false;
	}
	if(remark.length>300){
		$.alert("“调拨原因”不能超过300个字符！");
		$("#reason").focus();
		return false;
	}
	var passList = true;
	if(allotType != "IN_STORAGE"){
		//入库
		if ($("#listDevBuyType").val() == "BUY-TYPE-SELF" || 
				$("#listDevBuyType").val() == "BUY-TYPE-SUB") {
			//比例的和个人全额的不能
			$.alert("购买方式为“" + $("#listDevBuyTypeName").val() + "”的设备" + $("#deviceName").val() + 
					"不能用于" + allotTypeSel.getDisplayValue() + "调拨！");
			passList = false; 
			return false;
		}
	} else if (allotType == "IN_STORAGE") {
		if ($("#listDevBuyType").val() == "BUY-TYPE-SELF") {
			//个人全额的不能
			$.alert("购买方式为“" + $("#listDevBuyTypeName").val() + "”的设备" + $("#deviceName").val() + 
					"不能用于" + allotTypeSel.getDisplayValue() + "调拨！");
			passList = false; 
			return false;
		}
	}
	if (!passList) {
		return false;
	}
	if (!validListMainDev()) {
		return false;
	}
	var msg = msgSuccess(deviceId);
	if(msg!=""){
//		showMsg="该设备目前正在"+msg+"，如果进行“调拨登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//		if(!confirm(showMsg)){
//			return ;
//		}
		showMsg="该设备目前正在"+msg+"，您当前不能进行“调拨登记”操作。";
		$.alert(showMsg);
		return;
	}
	var workAreaCodeBef = $.trim($("#areaCodeBef").text());
	var inGroupName = $.trim($("#inGroupName").text());
	var groupName = $.trim($("#applyGroupName").text());
	var purposeBef = $.trim($("#purposeBef").text());
//	var purpose = $.trim(purposeOption);
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/it_dev_man",
		data : {
			act: "allocation",
			deviceTypeCode : deviceTypeCode,
			deviceId : deviceId,
			allotType : allotType,
			moveDate:moveDate,
			inAccountID:inAccountID,//调入经办人
			inGroupName:inGroupName,
			applicantID:applicantID,
			groupName:groupName,
			workAreaCode:areaCode,
			workAreaCodeBef:workAreaCodeBef,
			purposeBef:purposeBef,
//			purpose:purpose,
			remark:remark,
			validMainDevFlag : validMainDevFlag
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

function getSelectedDeviceIDs() {
	var ids = "";
	$("#deviceChooseResultBody tr").each(function(i){
		if (ids != "") {
			ids += ",";
		}
		ids += $(this).attr("id");
	});
	return ids;
}
function msgSuccess(id){
	var deviceType = $("#deviceTypeCode").val();
	var applicantID =$("#applicantID").val();
	var deviceClass = $("#deviceClassCode").val();
	var areaCode =areaSel.getValue();
//	var purposeOption = purposeOptionSel.getValue();
//	var purposeOptionName = purposeOptionSel.getDisplayValue();
	var  deviceId =$("#deviceID").val();
	var msgSuccess="";
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/it_dev_man?act=getstatus",//
		data:{
			id : id,
			purposeFlag:allotTypeFlag,
			deviceType:deviceType,
			areaCode:areaCode,
			deviceId : deviceId,
			deviceClass:deviceClass,
//			puerpose:purposeOption,
//			puerposeName:purposeOptionName,
			userId:applicantID,
			validMainDevFlag : validMainDevFlag
			
		},
		success:function saveSuccess(xml){
			//$.alert($(xml).get(0).xml)
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				msgSuccess = message.text();
			}else if(messageCode == "2"){
				msgSuccess = message.text();
			} else{
				$.alert(message.text());
				return false;
			}
		},
        error:$.ermpAjaxError
    }); 
    return msgSuccess;
}

/**
 * 验证主设备是否冲突
 * @return {Boolean} true:验证成功；false:验证失败
 */
function validListMainDev() {
	var mainDevCfgs = $("#mainDevCfgs").val();//
	var listAreaCode = $("#listAreaCode").val();
	var listDeviceClassID = $("#listDeviceClassID").val();
	if (mainDevCfgs.indexOf(listAreaCode + "," + listDeviceClassID) != -1) {
		if (!validMainDevFlag) {
			//当有主设备的时候才要去验证主设备，当都没有主设备的时候就不用去验证主设备
			validMainDevFlag = true;
		}
	}
	return true;
}
