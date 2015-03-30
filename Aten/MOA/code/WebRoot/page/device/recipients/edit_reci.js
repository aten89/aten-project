var mainFrame = $.getMainFrame();
var dialogWin;
//设备领用申购--新增
var TYPE_TITLE = "资产类别";
var CLASS_TITLE = "设备类别";
var deviceTypeSel;//设备类型下拉列表
var deviceClassSel;
var belongtoAreaSel;//所属区域
var bLoadedBelongtoAreaSel = false;
var bLoadedDeviceClassSel = false;
var validMainDevFlag = false;//是否验证主设备
var purposePurpose;
var allotTypeSel;
var buyTypeSel;
var purposeAreaCode;
var bLoadedBuyTypeSel = false;
var bIniting = true;//是否正在进行页面初始化
/**
 * 最近一次选择的设备类型值
 * @type 
 */
var lastDeviceTypeSelValue;

/**
 * 最近一次选择的设备类别值
 * @type 
 */
var lastDeviceClassSelValue;

/**
 * 最近一次选择的所属地区
 * @type 
 */
var lastAreaCodeValue;

$(initEquipmentInfoEdit);

function initEquipmentInfoEdit() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	initDeviceTypeSel($.trim($("#deviceTypeCode").val()), $.trim($("#deviceClassCode").val()));
	initAreaSel($("#areaCode").val());
	initAllotTypeSel($.trim($("#deviceTypeCode").val()),"");
	$("#btnSelectDevice").click(function(){
		selectDevice();
	});
	$("#openUserSelect").click(function(){
		selectSingleUser();
	});
	if ($("#deviceID").val() != "") {
		//已选择了设备
		$("#btnSelectDevice").hide();
	}
	
 	$("#saveBtn").click(function(){
 		saveInfo(true, $(":radio[name='formType']:checked").val());
 	});
 	
 	$("#draftBtn").click(function(){
 		saveInfo(false, $(":radio[name='formType']:checked").val());
 	});
 	
 	$("#closeBtn").click(function(){
 		//关闭
		mainFrame.getCurrentTab().close();
 	});
 	if ($("#applyType").val() == "0") {
 		//领用
 		$("#radDevUse").click();
 	} else if ($("#applyType").val() == "1") {
 		//申购
 		$("#radDevPurchase").click();
 	}
 	//全选
	$("#selectAll").click(function(){
		$("input[name='_doc_chk']", "#devUseType").attr("checked", this.checked);
	});
}

/**
 * 购买类型
 * @param {} buyType
 */
function initBuyType(deviceTypeCode,buyType) {
	if(deviceTypeCode==null ||  deviceTypeCode==""){
		buyTypeSel = $("#buyTypeSel").ySelect({
		width : 107
		});   
		buyTypeSel.addOption("", "---请选择---", 0);
		buyTypeSel.select(0);
		return;
	}
//	if (!bLoadedBuyTypeSel) {
		buyTypeSel = $("#buyTypeSel").ySelect({width: 107,url: "m/it_dev_man?act=initBuyType&deviceType="+deviceTypeCode,
			afterLoad:function() {
				buyTypeSel.addOption("", "---请选择---", 0);
				buyTypeSel.select(0);
				if (buyType != null && buyType != "") {
					buyTypeSel.select(buyType);
				}
//				bLoadedBuyTypeSel = true;
			}
		});
//	}
}


//选择申购或领用
function displayOrhide(vv){
	//领用
	if(vv == "radDevUse" && $("#" + vv)[0].checked){
		$("#descriptTitle").html("领用说明");
		$("#devListBody").show();
		$("#purchaseDiv").hide();
		$("#devPuerpose").hide();
		$("#devCfgDescTR").hide();
		$("#areaDeviceTR").hide();
		$("#red").hide();
		$("#msg").hide();
	}
	//申购
	if(vv == "radDevPurchase" && $("#" + vv)[0].checked){
		$("#descriptTitle").html("申购说明");
		$("#devListBody").hide();
		$("#devCfgDescTR").show();
		$("#devPuerpose").show();
		$("#areaDeviceTR").show();
		$("#red").show();
		$("#msg").show();
//		initBuyType($("#buyType").val());
		initDeviceClassSel($.trim(deviceTypeSel.getValue()),$.trim(belongtoAreaSel.getValue()));
	}
}	

function acceptanceList(){
	$("#ysd").toggle();	
	$("#ysdtd").toggle();	
}

//设备选择
function equipmentChoose(){
	selectDevice();
}

/**
 * 初始化所属区域下拉列表
 * @param {} areaCode 所属区域代码
 */
function initAreaSel(areaCode) {
	belongtoAreaSel = $("#belongtoAreaSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			belongtoAreaSel.addOption("", "---请选择---", 0);
			if (areaCode != null && areaCode != "") {
				belongtoAreaSel.select(areaCode);
			} else {
				belongtoAreaSel.select(0);
			}
		},onChange : function(value, text) {
			if (lastAreaCodeValue == value) {
				return;
			}
			if (hasList() && !clearDeviceChooseResult("所属地区")) {
				belongtoAreaSel.select(lastAreaCodeValue);
				return;
			}
			lastAreaCodeValue = value;
			initDeviceClassSel(deviceTypeSel.getValue(),value);
			if (bIniting == true) {
				bIniting = false;
			}
		}
	}); 
}

/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode, deviceClassCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "---请选择---", 0);
			//$.alert("afterLoad");
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				//deviceTypeSel.disable(true);
			} else {
				deviceTypeSel.select(0);
			}
			//lastDeviceTypeSelValue = deviceTypeSel.getValue();
		},
		onChange : function(value, text) {
			
			if (lastDeviceTypeSelValue == value) {
				return;
			}
			if (hasList() && !clearDeviceChooseResult(TYPE_TITLE)) {
				deviceTypeSel.select(lastDeviceTypeSelValue);//
				return;
			}
			initBuyType(value,$("#buyType").val());
			initDeviceClassSel(value,belongtoAreaSel.getValue());
			lastDeviceTypeSelValue = value;
			if (bIniting == true) {
				bIniting = false;
			}
		}
	});  
}

/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode,areaCode, deviceClassCode) {
	deviceClassCode = $("#deviceClass").val();
	if(deviceTypeCode==null || areaCode==null || deviceTypeCode=="" || areaCode==""){
		deviceClassSel = $("#deviceClassSel").ySelect({
		width : 107
		});   
		deviceClassSel.addOption("", "---请选择---", 0);
		deviceClassSel.select(0);
		return;
	}
	deviceClassSel = $("#deviceClassSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/device_class?act=classselect&deviceType=" + deviceTypeCode+"&areaCode="+areaCode,
		afterLoad : function() {
			//$.alert("initDeviceClassSel>>afterLoad")
			deviceClassSel.addOption("", "---请选择---", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
				$("#deviceClass").val("");
//				deviceClassSel.disable(true);
			} else {
				deviceClassSel.select(0);
			}
		}, 
		onChange : function(value, text) {
			//$.alert("initDeviceClassSel>>onChange")
			if (lastDeviceClassSelValue == value) {
				return;
			}
			if (hasList() && !clearDeviceChooseResult(CLASS_TITLE)) {
				deviceClassSel.select(lastDeviceClassSelValue);
				return;
			}
//			initDevPuerpose(deviceTypeCode,areaCode, value);
			lastDeviceClassSelValue = value;
			if (bIniting == true) {
				bIniting = false;
			}
		}
	});     
}

function hasList() {
	return ($("div.feesSp").size() > 0);
}

/**
 * 清空设备选择结果列表
 */
function clearDeviceChooseResult(typeName) {
	//只有不是正在进行页面初始化，才能清空领用列表
	if (!bIniting) {
		$.confirm("更改“" + typeName + "”会清空已选择的设备列表，是否继续？", function(r){
			if (r) {
				$("div.feesSp").remove();
				return true;
			} else {
				return false;
			}
		});
	}
	return false;
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
	var deviceTypeCode = deviceTypeSel == null ? "" : deviceTypeSel.getValue();
	//var deviceClassCode = deviceClassSel == null ? "" : deviceClassSel.getValue();
	//var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	var deviceClassCode = $.trim($("#deviceClassCode").val());
	if (belongtoAreaSel == null || belongtoAreaSel.getValue() == "") {
		$.alert("请选择所属区域！");
		return;
	}
	if (deviceTypeCode == "") {
		$.alert("请选择资产类别！");
		return;
	}
	
	var excludeDeviceIDs = getSelectedDeviceIDs();//要排除的设备ID
	var sURL = BASE_PATH + "m/it_dev_man?act=initdevselpage";
//	var sFeature = "dialogHeight:430px;dialogWidth:908px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:916px;status:no;scroll:auto;help:no";
//	}
	
	var args = new Object();
	//args.assignFlag = true;
	//args.assignType = 0;
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.excludeScrapFlag = true;
	args.isUsing = 0;
	args.status = 1;
	args.areaCode = belongtoAreaSel == null ? "" : belongtoAreaSel.getValue();
	args.deviceClassCode = deviceClassCode;
	args.excludeDeviceIDs = excludeDeviceIDs;
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备选择", sURL, 660, 378, function(){
		if (window.returnValue1) {
			$("#saveBtn").attr("disabled","true");
			$("#closeBtn").attr("disabled","true");
			var selectDeviceIDs = args.selectDeviceIDs;//已选择的设备ID列表
			$.ajax({
				type : "post",
				cache : false,
				url : BASE_PATH + "m/it_dev_man?act=loaddevicelist&initDevCfgList=true",
				data : {
					deviceIDs : selectDeviceIDs
				},
				success : function(xml){
					var message = $("message",xml);
					var content = $("content",xml);
					var listData = "";
					//是否有数据返回
				    if (message.attr("code") == "1") {
				    	var aUseDevice = [];
				    	$(xml).find('device').each(function(index){
				    		var curELe = $(this);
		                    var id = $.trim(curELe.attr("id"));
		                    var useDevice = new Object();
				    		useDevice.deviceID = id;
				    		useDevice.deviceNO = $("device-no", this).text();
				    		useDevice.deviceModel = $("device-model", this).text();
				    		useDevice.deviceName = $("device-name", this).text();
				    		useDevice.deviceClassName = $("device-class-str", this).text();
				    		useDevice.deviceClassID = $("device-class", this).text();
				    		useDevice.buyTypeName = $("buy-type-str", this).text();
				    		useDevice.buyType = $("buy-type", this).text();
				    		useDevice.buyTime = $("buy-time", this).text();
				    		useDevice.deviceCfgDesc = $("config-list", this).text();
				    		useDevice.areaCode = $("area-code", this).text();
				    		useDevice.areaName = $("area-name", this).text();
				    		aUseDevice.push(useDevice);
						});
						listData = createUseDeviceDetailHTML(aUseDevice, belongtoAreaSel == null ? "" : belongtoAreaSel.getValue());
						$("#devListBody").append(listData);
						$("div.feesSp").each(function(i){
							$("span[name='orderNum']", this).text(i+1);
						});
						$("#saveBtn").removeAttr("disabled");
						$("#closeBtn").removeAttr("disabled");
				    }
				},
				error : $.ermpAjaxError
			});
		}
	});
	
//	var result = $.showModalDialog(sURL, args, sFeature);
//	if (result) {
//		
//	}
}

function getSelectedDeviceIDs() {
	var ids = "";
	$("div.feesSp").each(function(i){
		if (ids != "") {
			ids += ",";
		}
		ids += $("span[name='deviceID']", this).text();
	});
	return ids;
}
function getSelectedDeviceMinBuyTime() {
	var buyTime = "";
	$("div.feesSp").each(function(i){
		var tempBuyTime = $.trim($("span[name='buyTime']", this).text());
		if(buyTime==""){
			buyTime = tempBuyTime;
		}else{
			if(tempBuyTime>buyTime){
				buyTime = tempBuyTime;
			}
		}
	});
	return buyTime;
}
/**
 * 删除设备选择结果
 * @param {} o
 */
function delItem(o) {
	$.confirm("是否确认删除？", function(r){
		if (r) {
			var oDel = $(o).parent().parent().parent().parent().parent().parent();
			oDel.remove();
			$("div.feesSp").each(function(i){
				$("span[name='orderNum']", this).text(i+1);
			});
		}
	});
}

function saveInfo(startFlow, formType){
	if (startFlow && !validateForm(formType)) {
		return;
	}
	var oFormType = $(":radio[name='formType']:checked");
	var deviceTypeCode = deviceTypeSel.getValue();
	var remark = $.trim($("#remark").val());
	var applyType = oFormType.val();//0:领用;1:申购
	var aDevPurchaseList = [];//领用
	var aPurchaseDevices = [];//申购
	var purposeValues =[];
	var buyType = "";
	var budgetMoney = "";
	var areaCode = $.trim(belongtoAreaSel.getValue());
	var workAreaCode="";
	var deviceClass ="";
	if (oFormType.val() == "1") {
		//申购
		buyType = $.trim(buyTypeSel.getValue());
		budgetMoney = $("#budgetMoney").val();
		purposeValues = getPurposeValues();
		var devPurchaseDetail = new Object();
		devPurchaseDetail.devCfgDesc = $("#devCfgDesc").val();
		devPurchaseDetail.belongtoAreaCode = $.trim(belongtoAreaSel.getValue());
		devPurchaseDetail.deviceClass = $.trim(deviceClassSel.getValue());
		devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
		devPurchaseDetail.purpose = purposePurpose;
		devPurchaseDetail.areaCode = purposeAreaCode;
		aPurchaseDevices.push(devPurchaseDetail);
		workAreaCode = purposeAreaCode;
		deviceClass = $.trim(deviceClassSel.getValue());
	} else {
		//领用
		if ($("div.feesSp").size() > 0) {
			$("div.feesSp").each(function(i){
				var devPurchaseDetail = new Object();
				devPurchaseDetail.deviceID = $("span[name='deviceID']", this).text();
				devPurchaseDetail.orderNum = $("span[name='orderNum']", this).text();
				devPurchaseDetail.purpose = $("select[name='devicePurposeSel']", this).val();
				devPurchaseDetail.areaCode = $("select[name='areaSel']", this).val();
				devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
				devPurchaseDetail.devCfgDesc = $.trim($("span[name='devCfgDesc']", this).text());
				aDevPurchaseList.push(devPurchaseDetail);
			});
		} else {
			//没有选择要领用的设备，为了保存预计使用日期，须new一个对象
			var devPurchaseDetail = new Object();
			devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
			aDevPurchaseList.push(devPurchaseDetail);
		}
		
	}
	
	var id = $("#id").val();
	var act = "";
	if (id == "") {
		act = "adddevuse";
	} else {
		act = "modifydraftform";
	}
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_apply_start?act=" + act,//
		data:{
			id : id,
			deviceTypeCode : deviceTypeCode,
			remark : remark,
			applyType : applyType,
			areaCode:areaCode,
			workAreaCode:workAreaCode,
			deviceClass : deviceClass,
			devPurchaseList : $.toJSON(aDevPurchaseList),//领用设备列表
			purchaseDevices : $.toJSON(aPurchaseDevices),//申购设备列表
			purposeValues : $.toJSON(purposeValues),//设备用途
			buyType : buyType,
			budgetMoney : budgetMoney,
			startFlow : startFlow,
			validMainDevFlag : validMainDevFlag
		},
		success:function saveSuccess(xml){
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				$.alert("保存成功", function(){
			    	 //刷新父列表
					mainFrame.getCurrentTab().doCallback();
			       //关闭
					mainFrame.getCurrentTab().close();
		    	});
			}
			else{
				$.alert(message.text());
			}
		},
        error:$.ermpAjaxError
    });  
}

function validateForm(formType) {
	var deviceTypeCode = deviceTypeSel.getValue();
	var planUseDate = $.trim($("#planUseDate").val());
	var oFormType = $(":radio[name='formType']:checked");
	if (oFormType.size() == 0) {
		$.alert("请选择类型！");;//0:领用;1:申购
		return false;
	}
	if ($.trim(belongtoAreaSel.getValue()) == "") {
		$.alert("请选所属地区！");
		return false;
	}
	if (deviceTypeCode == "") {
		$.alert("“资产类别”不能为空！");
		return false;
	}
	
	if (planUseDate == "") {
		$.alert("“预计使用日期”不能为空！");
		return false;
	}
	var newDate = $.trim($("#nowDate").text())
	if (planUseDate <newDate) {
		$.alert("“预计使用日期”不能小于领用调拨“申请日期”！");
		return false;
	}
	var arrFormTypeDisplayName = ["领用", "申购"];
	var flag = true;
	if(oFormType.val()=="0"){
		flag = false;
	}
	if($.validInput("remark", arrFormTypeDisplayName[parseInt(oFormType.val(), 10)] + "说明", flag, "\<\>\'\"", 2000)){
		return false;
	}
	if (!validListMainDev(formType)) {
		return false;
	}
	var passedList = true;
	if (oFormType.val() == "0") {
		//领用
		if ($("div.feesSp").size() == 0) {
			$.alert("你还未选择设备！");
			return false;
		}
		var buyTime = getSelectedDeviceMinBuyTime();
		if(planUseDate<buyTime){
			$.alert("“预计使用日期”不能小于设备“购买日期”"+buyTime+"！");
			return false;
		}
		$("div.feesSp").each(function(){
			var areaCode = $("select[name='areaSel']", this).val();
			var deviceNO = $("span[name='deviceNO']", this).text();
			var purpose = $("select[name='devicePurposeSel']", this).val();
			if (purpose == "") {
				$.alert("编号为“" + deviceNO +  "”设备的“设备用途”不能为空！");
				passedList = false;
				return false;
			}
			if (areaCode == "") {
				$.alert("编号为”" + deviceNO +  "“设备的“工作所在地“不能为空！");
				passedList = false;
				return false;
			}
			
		});
		
	} else {
		if ($.trim(deviceClassSel.getValue()) == "") {
			$.alert("请选择设备类别！");
			return false;
		}
		if ($.trim(buyTypeSel.getValue()) == "") {
			$.alert("请选择购买方式！");
			return false;
		}
		//申购
		if (validatePriceField("budgetMoney", "预算金额", true)) {
			return false;
		}
//		if(getcheckSelected()=="0"){
//			$.alert("请选择申购设备用途！");
//			return false;
//		} else
//		if(getcheckSelected()=="1"){
//			$.alert("请选择该申购设备用途工作所在地！");
//			return false;
//		}
		if($.validInput("devCfgDesc", "配置要求", true, "\<\>\'\"", 2000)){
			return false;
		}
	}
  	return passedList;
}


function selectSingleUser(){
	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	var user = selector.openDialog("single");
	if (user != null){
		$("#applicantID").val(user.id);
		$("#applicantName").val(user.name);
		$("#applyGroupName").text(user.deptName);
	}
}

//创建选择框选项
function createOption(url,data, defaultName, selectKey) {
	if (!defaultName) {
		defaultName = "请选择...";
	}
	var options = "<option value=''>" + defaultName + "</option>";
	$.ajax({
		type : "POST",
		cache : false,
		async : false,
		url : url,
		data : data,
		success : function(xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			//是否有数据返回
			var selectHTML;
		    if (message.attr("code") == "1") {	
		    	$("dataDict", xml).each(function(){
		    		selectHTML = "";
					var key = $("data-value", this).text();
					var value = $("data-key", this).text();
					if (selectKey != null && selectKey == key) {
						selectHTML = " selected";
					}
					options += "<option value='" + key + "' title='" + value + "'" + selectHTML + ">" + value + "</option>";
				});
		    }	
		},
		error : $.ermpAjaxError
	});
	return options;
}

//创建选择框选项
function createAreaOption(url,data, defaultName, selectKey) {
	if (!defaultName) {
		defaultName = "请选择...";
	}
	var options = "<option value=''>" + defaultName + "</option>";
	$.ajax({
		type : "POST",
		cache : false,
		async : false,
		url : url,
		data : data,
		success : function(html){
				var selectHTML;
				$("div", "<div>" + html + "</div>").each(function(){
					selectHTML = "";
					var str = $(this).html().split("**");
					if (selectKey != null && selectKey == str[0]) {
						selectHTML = " selected";
					}
					options += "<option value='" + str[0] + "' title='" + str[1] + "'" + selectHTML + ">" + str[1] + "</option>";
				});
			},
		error : $.ermpAjaxError
	});
	return options;
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
		width : 85, 
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

/**
 * 初始化设备工作用途
 * @param {} deviceClassCode 设备类别代码
 */
function initPurposeOptionSel(areaCode,deviceClassID) {
	if (areaCode == null || deviceClassID == "") {
		return;
	}
	purposeOptionSel = $("#purposeOptionSel").ySelect({
		width : 85, 
		url:BASE_PATH+"m/it_dev_man?act=getareadevusedivtype&areaCode=" + areaCode + "&deviceClassID=" + deviceClassID,
		afterLoad : function() {
			purposeOptionSel.addOption("", "---请选择--", 0);
			purposeOptionSel.select(0);
		}
	});     
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
		$.alert("“" + label + "”格式不正确：最多9位整数，如有小数，小数位数最多不能超过2位");
		$("#" + input).focus();
		return true;
	}
}

/**
 * 生成领用设备详情HTML
 */
function createUseDeviceDetailHTML(useDevices, areaCode) {
	var listData = "";
	var areaOptionsHTML = createAreaOption("m/data_dict?act=ereasel");
	$.each(useDevices, function(i, useDevice) {
//		var purposeOptionsHTML = createOption("m/it_dev_man?act=getareadevusetype&areaCode=" + areaCode + "&deviceClassID=" + useDevice.deviceClassID);
		listData += "<div class=\"feesSp fsMar\">";
		listData += "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" >";
		listData += "<tr>";
		listData += "<td class=\"spNum\"><span name=\"orderNum\"></span></td>";
		listData += "<td colspan=\"6\" class=\"spOpW opw2\">";
		listData += "<div>";
		listData += "  <a class=\"linkOver\" name=\"delLink\" onclick=\"delItem(this)\"><img src=\"themes/comm/images/spDel.gif\" />删除</a>";
		listData += "</div>";
		listData += "</td>";
		listData += "</tr>";
		listData += "<tr>";
		listData += "   <td width=\"70\" height=\"27\" class=\"spTit\">设备编号：</td>";
		listData += "   <td width=\"128\"><span name=\"deviceNO\">" + useDevice.deviceNO + "</span></td>";
		listData += "   <td width=\"70\"  class=\"spTit\">设备名称：</td>";
		listData += "    <td width=\"100\">" + useDevice.deviceName + "<span name=\"deviceID\" style=\"display: none\">" + useDevice.deviceID + "</span>";
		listData += "<input type=\"hidden\" name=\"listAreaName\" value=\"" + useDevice.areaName + "\"/>";
        listData += "<input type=\"hidden\" name=\"listAreaCode\" value=\"" + useDevice.areaCode + "\"/></td>";
		listData += "     <td width=\"70\" class=\"spTit\">设备型号：</td>";
		listData += "    <td width=\"290\">" + useDevice.deviceModel + "</td>";
		listData += "   <td rowspan=\"4\"  class=\"spOpW\">&nbsp;</td>";
		listData += "   </tr>";
		if(useDevice.deviceCfgDesc!=null && useDevice.deviceCfgDesc!=""){
			listData += "  <tr>";
			listData += "    <td width=\"70\" class=\"spTit\">配置信息：</td>";
			listData += "    <td colspan=\"5\" width=\"500\"><span name=\"devCfgDesc\">" + useDevice.deviceCfgDesc + "</span></td>";
			listData += "   </tr>";
		}
		listData += "<tr>";
		listData += "    <td width=\"70\" height=\"27\" class=\"spTit\">设备类别：<span name=\"buyTime\" style=\"display: none\">" + useDevice.buyTime + "</span></td>";
		listData += "    <td width=\"128\"><span name=\"listDeviceClassName\">" + useDevice.deviceClassName + "</span><input type=\"hidden\" name=\"listDeviceClassID\" value=\"" + useDevice.deviceClassID + "\"/></td>";
	//	listData += "     <td width=\"70\" class=\"spTit\">设备用途：</td>";
	//	listData += "     <td width=\"100\"><select name='devicePurposeSel' style='width:100px;height:20px'>" + purposeOptionsHTML + "</select></td>";
		listData += "<td width=\"70\" class=\"spTit\">工作所在地：</td>";
		listData += "  <td width=\"290\" colspan='3'><select name='areaSel' style='width:60px;height:20px'>" + areaOptionsHTML + "</select></td>";
		listData += "</tr>";
		listData += "<tr>";
		listData += "  <td colspan=\"7\" style=\"padding:0; height:10px\">&nbsp;</td>";
		listData += "</tr>";
		listData += "</table>";
		listData += " <div class=\"blank\" style=\"height:5px\"></div>";
		listData += "</div>		";
		
	});
  	return listData;
}

//查询
//function initDevPuerpose(deviceType,areaCode,deviceClass){
//	if(deviceType==null || deviceType=="" || areaCode==null || areaCode=="" ||deviceClass==null ||deviceClass==""){
//		$("#devUseType").html("");
//		return;
//	}
//	var showFlag= false;
//	var areaOptionsHTML;
//	var devPuerpose = $("#purposes").val();
//	$.ajax({
//	    type : "POST",
//		cache: false,
//		url  : "m/it_dev_man",
//		data : {act:"getdevpurpose",
//			deviceType:deviceType,
//			areaCode:areaCode,
//			deviceClassID:deviceClass,
//			flag :"true"
//		},
//	       success : function(xml){
//	     		//解析XML中的返回代码
//				var messageCode = $("message",xml).attr("code");
//				if(messageCode == "1"){
//					var html = "";
//					$("dataDict",xml).each(
//	                    function(index){
//	                       var dateDict = $(this);
//	                       var count = $("count",dateDict).text();
//	                       var flag=$("many-times-flag",dateDict).text();
//	                       showFlag = false;
//	                       html += "<div>";
//	                       if(flag=="true"){
//	                       	 if(devPuerpose.indexOf($("data-value",dateDict).text()+";")!=-1){
//	                         	html +="<span>&nbsp;<input type=\"radio\" class=\"cBox\" onclick=\"showAreaCode('"+$("data-value",dateDict).text()+"');\" name=\"_doc_chk\" checked=\"true\" value=\""+$("data-value",dateDict).text()+"\"/></span>" ;
//	                         	showFlag = true;
//	                         }else{
//	                         	html +="<span>&nbsp;<input type=\"radio\"   class=\"cBox\" onclick=\"showAreaCode('"+$("data-value",dateDict).text()+"');\" name=\"_doc_chk\" value=\""+$("data-value",dateDict).text()+"\"/></span>" 
//	                         }
//	                       }else{
//	                       	  if(count==0){
//	                       	  	if(devPuerpose.indexOf($("data-value",dateDict).text()+";")!=-1){
//	                       	  		showFlag = true;
//		                         	html +="<span>&nbsp;<input type=\"radio\"  class=\"cBox\" name=\"_doc_chk\" onclick=\"showAreaCode('"+$("data-value",dateDict).text()+"');\" checked=\"true\" value=\""+$("data-value",dateDict).text()+"\"/></span>" ;
//		                         }else{
//		                         	html +="<span>&nbsp;<input type=\"radio\"  class=\"cBox\"  name=\"_doc_chk\" onclick=\"showAreaCode('"+$("data-value",dateDict).text()+"');\"  value=\""+$("data-value",dateDict).text()+"\"/></span>" 
//		                         }
//	                         }else{
//	                         	html +="<span>&nbsp;<input type=\"radio\"  class=\"cBox\" disabled=\"disabled\"/></span>" ;
//	                         }
//	                       }
//	                       html +="<span  style='color:#000'>" + $("data-key",dateDict).text()+ " </span>" ;
//	                       html +="<span style='padding-left:15px;color:#999'>您已领用"+$("count",dateDict).text()+"台，</span>";
//	                       var purposeAreaC;
//	                       if(flag=="true"){
//	                         html +="<span style='color:#999'>可继续领用多台；<input type=\"hidden\" id=\""+$("data-value",dateDict).text()+"\" name=\"_doc_chk\" value=\""+flag+"\"/></span>";
//	                         if(showFlag){
//	                         	 html +="<span id=\"span_"+$("data-value",dateDict).text()+"\">";
//	                         	 purposeAreaC = $("#areaCodePurpose").val();
//	                         }else{
//	                         	 html +="<span id=\"span_"+$("data-value",dateDict).text()+"\" style=\"display:none\">";
//	                         }
//	                         html += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"cRed\">*</span>工作所在地：";
//	                         areaOptionsHTML = createAreaOption("m/data_dict?act=ereasel","","",purposeAreaC);
//	                         html +="<select id=\"select_"+$("data-value",dateDict).text()+"\" name='areaSel' style='width:60px;height:20px;vertical-align:middle'>" + areaOptionsHTML + "</select></span>";
//	                       }else{
//	                       	  if(count==0){
//	                       	  	html +="<span style='color:#999'>可领用1台；</span>";
//	                       	  	if(showFlag){
//		                         	 html +="<span id=\"span_"+$("data-value",dateDict).text()+"\">";
//		                         	  purposeAreaC = $("#areaCodePurpose").val();
//		                         }else{
//		                         	 html +="<span id=\"span_"+$("data-value",dateDict).text()+"\" style=\"display:none\">";
//		                         }
//		                         html += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"cRed\">*</span>工作所在地：";
//		                         areaOptionsHTML = createAreaOption("m/data_dict?act=ereasel","","",purposeAreaC);
//	                       	  	html +="<select id=\"select_"+$("data-value",dateDict).text()+"\" name='areaSel' style='width:60px;height:20px;vertical-align:middle'>" + areaOptionsHTML + "</select></span>";
//	                       	  }else{
//	                       	  	html +="<span style='color:#999'>无法继续领用；</span>";
//	                       	  }
//	                       }
//	                       
//          				   html +="</div>"
//	                    });		        
//	                $("#devUseType").html(html);
//				}else{
//               		$.alert($("message",xml).text());
//           		}
//	       },//many-times-flag,count
//	       error : $.ermpAjaxError
//	});	
//}
function showAreaCode(obj){
	$(":radio[name='_doc_chk']").each(function(){
		var purpose = $(this).val();
		$("#span_"+purpose).hide();
	});
	$("#span_"+obj).show();
}
/**
 * 获取选择的设备工作用途
 * @return {}
 */
function getPurposeValues() {
	var purPoseItems = new Array(); 
	$(":radio[name='_doc_chk']:checked").each(function(){
		var obj = new Object();
		var purpose = $(this).val();
		obj.purpose=purpose;
		purposePurpose = purpose;
		obj.manyTimeFlag=$("#"+purpose).val();
		purposeAreaCode = $("#select_"+purpose).val();
		obj.areaCode = purposeAreaCode ;
		purPoseItems.push(obj);
	});
	return purPoseItems;
}

/**
 * @return {}
 */
function getcheckSelected() {
	var checkFlag = 0;
	$(":radio[name='_doc_chk']:checked").each(function(){
		var purpose = $(this).val();
		if($("#select_"+purpose).val()==null || $("#select_"+purpose).val()==""){
			checkFlag=1;
		}else{
			checkFlag=2;
		}
	});
	return checkFlag;
}

/**
 * 验证主设备是否冲突
 * @return {Boolean} true:验证成功；false:验证失败
 */
function validListMainDev(formType) {
	var mainDevCfgs = $("#mainDevCfgs").val();//
	if (formType == "0") {
		//领用
		var mainDevClassID = "";
		var prompt = "";
		var count = 0;
		$(".feesSp").each(function(i){
			var listAreaCode = $("input[name='listAreaCode']", this).val();
			var listDeviceClassID = $("input[name='listDeviceClassID']", this).val();
			if (mainDevCfgs.indexOf(listAreaCode + "," + listDeviceClassID) != -1) {
				if (!validMainDevFlag) {
					//当有主设备的时候才要去验证主设备，当都没有主设备的时候就不用去验证主设备
					validMainDevFlag = true;
				}
				var tempMainDevClassID = listAreaCode + "," + listDeviceClassID;
				if (mainDevClassID.indexOf(tempMainDevClassID) == -1) {
					++count;
					if (prompt != "") {
						prompt += "、";
					}
					var listAreaName = $("input[name='listAreaName']", this).val();
					var listDeviceClassName = $("span[name='listDeviceClassName']", this).text();
					prompt += "“" + listAreaName + listDeviceClassName + "”";
					if (mainDevClassID != "") {
						mainDevClassID += ";";
					}
					mainDevClassID += tempMainDevClassID;
				}
			}
		});
		if (count > 1) {
			$.alert("系统已将" + prompt + "设为主设备，不能领用不同种类的主设备");
			return false;
		}	
	} else if (formType == "1") {
		var deviceClassID = deviceClassSel.getValue();
		var areaCode = belongtoAreaSel.getValue();
		if (mainDevCfgs.indexOf(areaCode + "," + deviceClassID) != -1) {
			if (!validMainDevFlag) {
				//当有主设备的时候才要去验证主设备，当都没有主设备的时候就不用去验证主设备
				validMainDevFlag = true;
			}
		}
	}
	return true;
}
