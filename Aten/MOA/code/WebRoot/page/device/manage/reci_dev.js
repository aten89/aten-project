var mainFrame = $.getMainFrame();
var dialogWin;
var TYPE_TITLE = "资产类别";
var CLASS_TITLE = "设备类别";
var deviceTypeSel;//设备类型下拉列表
var deviceClassSel;
var areaSel;//所属区域
var bPrompted = true;
var applyTypeFlag =true;
var validMainDevFlag = false;//是否验证主设备
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

$(initEquipmentInfoUse);
/**
 * 设备领用
 */
function initEquipmentInfoUse() {
	//initDeviceTypeSel($.trim($("#deviceTypeCode").val()), $.trim($("#deviceClassCode").val()));
	//initAreaSel();
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
}

/**
 * 初始化所属区域下拉列表
 * @param {} areaCode 所属区域代码
 */
function initAreaSel(areaCode) {
	areaSel = $("#areaSel").ySelect({
		width : 35, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			if (areaCode != null && areaCode != "") {
				areaSel.select(areaCode);
			} else {
				areaSel.select(0);
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
			//$.alert("afterLoad");
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				deviceTypeSel.disable(true);
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
				bPrompted = false;
				deviceTypeSel.select(lastDeviceTypeSelValue);//
				return;
			}
			lastDeviceTypeSelValue = value;
			initDeviceClassSel(value, deviceClassCode);
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
			//$.alert("initDeviceClassSel>>afterLoad")
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
				deviceClassSel.disable(true);
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
			lastDeviceClassSelValue = value;
			if (bIniting == true) {
				bIniting = false;
			}
		}
	});     
}

function hasList() {
	return ($("#deviceChooseResultBody tr").size() > 0);
}

/**
 * 清空设备选择结果列表
 */
function clearDeviceChooseResult(typeName) {
	if (bIniting) {
		return false;
	}
	//只有不是正在进行页面初始化，才能清空领用列表
	$.confirm("更改“" + typeName + "”会清空已选择的设备列表，是否继续？", function(r){
		if (r) {
			$("#deviceChooseResultBody").html("");
			return true;
		} else {
			return false;
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
	//var deviceTypeCode = deviceTypeSel == null ? "" : deviceTypeSel.getValue();
	//var deviceClassCode = deviceClassSel == null ? "" : deviceClassSel.getValue();
	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	var deviceClassCode = $.trim($("#deviceClassCode").val());
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
	args.assignFlag = true;
	args.assignType = 0;
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.isUsing = 0;
	args.status = 1;
	args.areaCode = $("#areaCode").val();
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
				url : BASE_PATH + "m/it_dev_man?act=loaddevicelist",
				data : {
					deviceIDs : selectDeviceIDs
				},
				success : function(xml){
					var message = $("message",xml);
					var content = $("content",xml);
					var listData = $.trim($("#deviceChooseResultBody").html(listData));
					//是否有数据返回
				    if (message.attr("code") == "1") {
//				    	var purposeOptionsHTML = "";
				    	var areaOptionsHTML = "";
				    	$(xml).find('device').each(function(index){
				    		var curELe = $(this);
		                    var id = $.trim(curELe.attr("id"));
//		                    purposeOptionsHTML = createOption("m/it_dev_man?act=getareadevusetype&areaCode=" +  $("area-code",curELe).text()  + "&deviceClassID=" +  $("device-class",curELe).text() );
		                    areaOptionsHTML = createAreaOption("m/data_dict?act=ereasel");
		                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
									+ "<td><span name=\"listDeviceNO\">" + $("device-no",curELe).text() + "</span><input type=\"hidden\" name=\"listAreaName\" value=\"" + $("area-name",curELe).text() + "\"/>"
	                          		+ "<input type=\"hidden\" name=\"listAreaCode\" value=\"" + $("area-code",curELe).text() + "\"/></td>"
					  				+ "<td>" + $("device-name",curELe).text() + "</td>"
									+ "<td>" + $("device-model",curELe).text() + "</td>"
					  				+ "<td><span name=\"listDeviceClassName\">" + $("device-class-str",curELe).text() + "</span><input type=\"hidden\" name=\"listDeviceClassID\" value=\"" + $("device-class",curELe).text() + "\"/></td>"
									+ "<td>" + $("buy-time",curELe).text() + "</td>"
//									+ "<td><select name=\"devicePurposeSel\" style=\"width:100px;height:20px\">" + purposeOptionsHTML + "</select></td>"
									+ "<td><select name=\"areaSel\" style=\"width:60px;height:20px\">" + areaOptionsHTML + "</select></td>"
									+ "<td><a class=\"linkOver\" name=\"delLink\" onclick=\"delItem(this)\">删除</a><input id=\"buyTime_"+id+"\" type=\"hidden\" value=\"" + $("buy-time",curELe).text() + "\"/></td>"
									+ "</tr>";
		
						});
						$("#deviceChooseResultBody").html(listData);
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
	$("#deviceChooseResultBody tr").each(function(i){
		if (ids != "") {
			ids += ",";
		}
		ids += $(this).attr("id");
	});
	return ids;
}
function getSelectedDeviceMinBuyTime() {
	var buyTime = "";
	$("#deviceChooseResultBody tr").each(function(i){
		var id = $(this).attr("id");
		if(buyTime==""){
			buyTime = $.trim($("#buyTime_"+id).val());
		}else{
			if($.trim($("#buyTime_"+id).val())>buyTime){
				buyTime = $.trim($("#buyTime_"+id).val());
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
	$.confirm("是否确认删除？",function(r){
		if (r) {
			var oTR = $(o).parent().parent()
			oTR.remove();
		}
	});
}

function saveInfo(){
	if (!validateForm()) {
		return;
	}
	/*
 	var applyForm = new Object();
 	applyForm.id = $.trim($("#id").val());
	applyForm.deviceTypeCode = deviceTypeSel.getValue();
	applyForm.deviceClassCode = deviceClassSel.getValue();
	applyForm.regAccountID = $.trim($("#regAccountID").val());
	applyForm.regTime = $.trim($("#regTime").val());
	applyForm.applicant = $.trim($("#applicant").val());
	applyForm.applyGroupName = $.trim($("#applyGroupname").text());
	applyForm.remark = $.trim($("#remark").val());
	*/
	var id = $.trim($("#id").val());
	//var deviceTypeCode = deviceTypeSel.getValue();
	var deviceTypeCode = $("#deviceTypeCode").val();
	var regAccountID = $.trim($("#regAccountID").val());
	var regTime = $.trim($("#regTime").val());
	var applicantID = $.trim($("#applicantID").val());
	var applyGroupName = $.trim($("#applyGroupName").text());
	var remark = $.trim($("#remark").val());
	var applyType = "0";//0:领用;1:申购 
	var formStatus = "2";//已发布 
	var passed = true;//是否通过
	var aDevPurchaseList = [];
	var deviceIds="";
	$("#deviceChooseResultBody tr").each(function(i){
		var devPurchaseDetail = new Object();
		devPurchaseDetail.deviceID = $(this).attr("id");
		devPurchaseDetail.purpose = $("select[name='devicePurposeSel']", this).val();
		devPurchaseDetail.areaCode = $("select[name='areaSel']", this).val();
		devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
		aDevPurchaseList.push(devPurchaseDetail);
		deviceIds+=$(this).attr("id")+",";
		
	});
	var msg = msgSuccess(deviceIds);
	if(msg == "-1"){
		return;
	}
	if(msg!=""){
//		showMsg=msg+"如果进行“领用登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//		if(!confirm(showMsg)){
//			return ;
//		}
		showMsg=msg+"您当前不能进行“领用登记”操作。";
		$.alert(showMsg);
		return;
	}
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/it_dev_man?act=recipients",//
		data:{
			deviceTypeCode : deviceTypeCode,
			areaCode : $.trim($("#areaCode").val()),
			regAccountID : regAccountID,
			regTime : regTime,
			applicant : applicantID,
			applyGroupName : applyGroupName,
			remark : remark,
			applyType : applyType,
			formStatus : formStatus,
			passed : passed,
			devPurchaseList : $.toJSON(aDevPurchaseList),
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

function validateForm() {
	var deviceTypeCode = $("#deviceTypeCode").val();
	var applicantID = $.trim($("#applicantID").val());
	var applyGroupName = $.trim($("#applyGroupName").text());
	var planUseDate = $.trim($("#planUseDate").val());
	if (deviceTypeCode == "") {
		$.alert("“资产类别”不能为空！");
		return false;
	}
	if (applicantID == "") {
		$.alert("“申请人”不能为空！");
		return false;
	}
	if (applyGroupName == "") {
		$.alert("申请人的“所在部门”未配置！");
		return false;
	}
	if (planUseDate == "") {
		$.alert("“预计使用日期”不能为空！");
		return false;
	}
	
	if ($("#deviceChooseResultBody tr").size() == 0) {
		$.alert("你还未选择设备！");
		return false;
	}
	
	if($.validInput("remark", "领用说明", false, "\<\>\'\"", 500)){
		return false;
	}
	if (!validListMainDev()) {
		return false;
	}
	var buyTime = getSelectedDeviceMinBuyTime();
	if(planUseDate<buyTime){
		$.alert("“预计使用日期”不能小于设备”购买日期“"+buyTime+"！");
		return false;
	}
	var passed = true;
	$("#deviceChooseResultBody tr").each(function(){
		var purpose = $("select[name='devicePurposeSel']", this).val();
		if (purpose == "") {
			$.alert("编号为“" + $("td", this).eq(0).text() +  "”设备的“设备用途”不能为空！");
			passed = false;
			return false;
		}
		var areaCode = $("select[name='areaSel']", this).val();
		if (areaCode == "") {
			$.alert("编号为”" + $("td", this).eq(0).text() +  "“设备的“工作所在地“不能为空！");
			passed = false;
			return false;
		}
		
	});
  	return passed;
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
						selectHTML = " selected"
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
				$("div", "<div>" + html + "</div>").each(function(){
					var str = $(this).html().split("**");
					options += "<option value='" + str[0] + "' title='" + str[1] + "'>" + str[1] + "</option>";
				});
			},
		error : $.ermpAjaxError
	});
	return options;
}
function msgSuccess(ids){
	var msgSuccess="";
	var deviceType = $("#deviceTypeCode").val();
	var applicantID =$("#applicantID").val();
	var deviceClass = $("#deviceClassCode").val();
	var aDevPurchaseList = [];
	$("#deviceChooseResultBody tr").each(function(i){
		var devPurchaseDetail = new Object();
		devPurchaseDetail.deviceID = $(this).attr("id");
		devPurchaseDetail.purpose = $("select[name='devicePurposeSel']", this).val();
		devPurchaseDetail.areaCode = $("select[name='areaSel']", this).val();
		devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
		aDevPurchaseList.push(devPurchaseDetail);
	});
	var areaCode = $("#areaCode").val();
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/it_dev_man?act=getmanystatus",//
		data:{
			deviceIDs : ids,
			purposeFlag:true,
			deviceType:deviceType,
			areaCode:areaCode,
			devPurchaseList : $.toJSON(aDevPurchaseList),
			userId:applicantID,
			validMainDevFlag : validMainDevFlag
		},
		success:function saveSuccess(xml){
			//$.$.alert($(xml).get(0).xml)
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				msgSuccess = message.text();
			} else if(messageCode == "2"){
				msgSuccess = message.text();
			} else{
				$.alert(message.text());
				msgSuccess = "-1";
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
	var mainDevClassID = "";
	var prompt = "";
	var count = 0;
	$("#deviceChooseResultBody tr").each(function(i){
		var listAreaCode = $("input[name='listAreaCode']", this).val();
		var listDeviceClassID = $("input[name='listDeviceClassID']", this).val();
		if (mainDevCfgs.indexOf(listAreaCode + "," + listDeviceClassID) != -1) {
			if (!validMainDevFlag) {
				//当有主设备的时候才要去验证主设备，当都没有主设备的时候就不用去验证主设备
				validMainDevFlag = true;
			}
			//找到
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
	
	return true;
}