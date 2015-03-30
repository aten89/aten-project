var mainFrame = $.getMainFrame();
var dialogWin;
//设备调拨--新增
var deviceTypeSel;
var deviceClassSel;
var areaSel;//区域下拉列表
var allotTypeSel;//调拨类型
var purposeOptionSel;//设备工作用途
var allotTypeFlag =false;
var bIniting = true;
var validMainDevFlag = false;//是否验证主设备
/**
 * 最近一次选择的设备类型值
 * @type 
 */
var lastDeviceTypeSelValue;
$(initEquipmentInfoAllotEdit);

function initEquipmentInfoAllotEdit() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
  	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	var deviceClassCode = $.trim($("#deviceClassCode").val());
	var areaCode = $.trim($("#areaCode").val());
	initDeviceTypeSel(deviceTypeCode, deviceClassCode);
//	initAllotTypeSel($.trim($("#moveType").val()));
	
	$("#gmcpAdd").click(function(){
		selectDevice();
	});
	$("#openInAccountSelect").click(function(){
		selectSingleInAccount();
	});
	
	$("#saveBtn").click(function(){
 		saveInfo(true);
 	});
 	
 	$("#draftBtn").click(function(){
 		saveInfo(false);
 	});
 	
 	$("#closeBtn").click(function(){
 		//关闭
		mainFrame.getCurrentTab().close();
 	});
 	
}

function saveInfo(){
	window.close();	
}

function doclose(){
	window.close();	
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
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				//deviceTypeSel.disable(true);
			} else {
				deviceTypeSel.select(0);
			}
		},
		onChange : function(value, text) {
			if (value == lastDeviceTypeSelValue) {
				return;
			}
			if (hasList() && !clearDeviceChooseResult("资产类别")) {
				deviceTypeSel.select(lastDeviceTypeSelValue);//
				if (bIniting) {
					bIniting = false;
				}
				return;
			}
			initAllotTypeSel(value,$.trim($("#moveType").val()));
			if (bIniting) {
				bIniting = false;
			}
			lastDeviceTypeSelValue = value;
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
	return true;
}

/**
 * 初始化所属区域下拉列表
 * @param {} areaCode 所属区域代码
 */
function initAreaSel(areaCode) {
	areaSel = $("#areaSel").ySelect({
		width : 85, 
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
		width : 85
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
		},
		onChange:function() {
			if(allotTypeSel.getValue() == "IN_STORAGE" || allotTypeSel.getValue() == "ALLOT_BORROW"){
				$("#trInInfo").hide();
			}else {
				$("#trInInfo").show();
			}
		}
	}); 
}
//
////设备选择
//function equipmentChoose(){
//	window.showModalDialog(BASE_PATH + "page/EquipmentManagement/EquipmentInformation/EquipmentChoose.jsp","","dialogHeight:330px;dialogWidth:508px;status:no;scroll:auto;help:no");
//}

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
	if (deviceTypeCode == "") {
		$.alert("请选择资产类别！");
		return;
	}
	var excludeDeviceIDs = getSelectedDeviceIDs();//要排除的设备ID
//	var sURL = BASE_PATH + "m/it_dev_man?act=initdevselpage&disableMyDeviceFlag=false";
	var sURL = BASE_PATH + "m/it_dev_man?act=initdevselpage&devAttachFlag=true&disableMyDeviceFlag=true";//devAttachFlag：是否显示出设备归属搜索项
	sURL += "&defaultMyDeviceFlag=true&disUsingFlag=false";//defaultMyDeviceFlag:我名下的设备默认为已打勾
//	var sFeature = "dialogHeight:430px;dialogWidth:908px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:916px;status:no;scroll:auto;help:no";
//	}
	
	var args = new Object();
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.excludeScrapFlag = true;
	args.isUsing = 1;
	args.status = 1;
	if (allotTypeSel.getValue() == "ALLOT_INSIDE" || allotTypeSel.getValue() == "ALLOT_DEPT") {
		//部门之间、内部之间
		args.excludeSubBuyFlag = true;//过滤掉比例购买
	}
	args.excludeSelfBuyFlag = true;//过滤掉个人全额
	args.excludeDeviceIDs = excludeDeviceIDs;
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备选择", sURL, 735, 378, function(){
		if (window.returnValue1) {
			$("#saveBtn").attr("disabled","true");
			$("#draftBtn").attr("disabled","true");
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
					//是否有数据返回
				    if (message.attr("code") == "1") {
				    	var purposeOptionsHTML = "";
				    	var areaOptionsHTML = "";
				    	var aAllotDevice = [];
				    	$(xml).find('device').each(function(index){
				    		var curELe = $(this);
		                    var id = $.trim(curELe.attr("id"));
				    		var allotDevice = new Object();
				    		allotDevice.deviceID = $(this).attr("id");
				    		allotDevice.deviceNO = $("device-no", this).text();
				    		allotDevice.deviceModel = $("device-model", this).text();
				    		allotDevice.deviceName = $("device-name", this).text();
				    		allotDevice.deviceClassName = $("device-class-str", this).text();
				    		allotDevice.deviceClassID = $("device-class", this).text();
				    		allotDevice.buyType = $("buy-type", this).text();
				    		allotDevice.buyTypeName = $("buy-type-str", this).text();
				    		allotDevice.devCfgDesc = $("config-list", this).text();
				    		allotDevice.areaNameBef = $("work-areaName", this).text();
				    		allotDevice.areaCodeBef = $("work-areaCode", this).text();
				    		allotDevice.purposeBef = $("work-purpose", this).text();
				    		allotDevice.purposeNameBef = $("work-purposeName", this).text();
				    		allotDevice.areaName = $("area-name", this).text();
				    		allotDevice.areaCode = $("area-code", this).text();
				    		aAllotDevice.push(allotDevice);
						});
						var sHtml = createAllotDeviceDetailHTML(aAllotDevice);
						$("#devListBody").append(sHtml);
						$("div.feesSp").each(function(i){
							$("span[name='orderNum']", this).text(i+1);
						});
						$("#saveBtn").removeAttr("disabled");
						$("#draftBtn").removeAttr("disabled");
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
		ids += $("input[name='deviceID']", this).val();
	});
	return ids;
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
			deviceClassSel.addOption("", "---请选择---", 0);
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
function initPurposeOptionSel(areaCode,deviceClassID) {
	purposeOptionSel = $("#purposeOptionSel").ySelect({
		width : 85, 
		url:BASE_PATH+"m/it_dev_man?act=getareadevusedivtype&areaCode=" + areaCode + "&deviceClassID=" + deviceClassID,
		afterLoad : function() {
			purposeOptionSel.addOption("", "---请选择--", 0);
			purposeOptionSel.select(0);
		}
	});     
}

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

//function selectSingleOutAccount(){
//	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
//	var user = selector.openDialog("single");
//	if (user != null){
//		$("#applicantID").val(user.id);
//		$("#applicantName").val(user.name);
//		$("#applyGroupName").text(user.deptName);
//	}
//}

function validateForm() {
	var deviceTypeCode = deviceTypeSel.getValue();
	if (deviceTypeCode == "") {
		$.alert("请选择资产类别！");
		return false;
	}
	var allotType = allotTypeSel.getValue();
	if(allotType==""){
		$.alert("请选设备调拨类型！");
		return false;
	}
	var moveDate = $("#moveDate").val();
	if(moveDate==""){
		$.alert("“调拨日期”不能为空！");
		return false;
	}
	var newDate = $.trim($("#nowDate").text())
	if (moveDate <newDate) {
		$.alert("“调拨日期”不能小于调拨“申请日期”！");
		return false;
	}
	/*
	if(moveDate<buyTime){
		$.alert("调拨日期不能小于设备购买日期"+buyTime);
		return false;
	}
	*/
	var passList = true;
	if(allotType != "IN_STORAGE"){
		//入库
		var inAccountID = $("#inAccountID").val();
		if(inAccountID == ""){
			$.alert("“调入经办人”不能为空！");
			$("#inAccountID").focus();
			return false;
		}
		var inGroupName = $("#inGroupName").text();
		if(inGroupName == ""){
			$.alert("“调入部门”不能为空！");
			return false;
		}
		$(".feesSp").each(function(){
			if ($("input[name='listDevBuyType']", this).val() == "BUY-TYPE-SELF" || 
					$("input[name='listDevBuyType']", this).val() == "BUY-TYPE-SUB") {
				//比例的和个人全额的不能
				$.alert("购买方式为“" + $("input[name='listDevBuyTypeName']", this).val() + "”的设备名称为”" + $("span[name='deviceName']", this).text() + 
						"“不能用于" + allotTypeSel.getDisplayValue() + "调拨！");
				passList = false; 
				return false;
			}
		});
	} else if (allotType == "IN_STORAGE") {
		$(".feesSp").each(function(){
			if ($("input[name='listDevBuyType']", this).val() == "BUY-TYPE-SELF") {
				//个人全额的不能
				$.alert("购买方式为“" + $("input[name='listDevBuyTypeName']", this).val() + "”的设备" + $("span[name='deviceName']", this).text() + 
						"不能用于" + allotTypeSel.getDisplayValue() + "调拨！");
				passList = false; 
				return false;
			}
		});
	}
	if (!passList) {
		return false;
	}
	if($.validInput("reason", "调拨原因", true, "\<\>\'\"", 500)){
		return false;
	}
	if ($("div.feesSp").size() == 0) {
		$.alert("你还未选择要调拨的设备！");
		return false;
	}
	if (!validListMainDev()) {
		return false;
	}
	return true;
}

function saveInfo(isStartFlow){
	if (isStartFlow && !validateForm()) {
		return;
	}
	
	var deviceTypeCode = deviceTypeSel.getValue();
	var allotType = allotTypeSel.getValue();
	var moveDate = $("#moveDate").val();
	var inAccountID = $("#inAccountID").val();
	var inGroupName = $("#inGroupName").text();
	var reason = $("#reason").val();
	var aDevList = [];
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		devDetail.deviceID = $("input[name='deviceID']", this).val();
		devDetail.areaCodeBef = $("span[name='areaCodeBef']", this).text();
		devDetail.purposeBef = $("span[name='purposeBef']", this).text();
		devDetail.orderNum = $("span[name='orderNum']", this).text();
		var deviceNo = $("span[name='deviceNO']", this).text();
		aDevList.push(devDetail);
	});
	var id = $.trim($("#id").val());
	var act = "";
	if (id == "") {
		act = "addallot";
	} else {
		act = "modifyallot";
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_alc_start",
		data : {
			act : act,
			id : id,
			deviceTypeCode : deviceTypeCode,
			allotType : allotType,
			moveDate : moveDate,
			inAccountID : inAccountID,//调入经办人
			inGroupName : inGroupName,
			reason : reason,
			aDevList : $.toJSON(aDevList),
			isStartFlow : isStartFlow
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            //$.alert($(xml).get(0).xml)
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
	$("div.feesSp").each(function(i){
		if (ids != "") {
			ids += ",";
		}
		ids += $("input[name='deviceID']", this).val();
	});
	return ids;
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


/**
 * 生成调拨设备详情HTML
 */
function createAllotDeviceDetailHTML(allotDevices) {
	var sHtml = "";
	$.each(allotDevices, function(i, allotDevice) {
		sHtml += "<div class=\"feesSp fsMar\">";
	    sHtml += "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" >";
	    sHtml += "<tr>";
		sHtml += "<td class=\"spNum\"><span name=\"orderNum\"></span></td>";
		sHtml += "<td colspan=\"6\" class=\"spOpW opw2\">";
		sHtml += "<div>";
		sHtml += "  <a class=\"linkOver\" name=\"delLink\" onclick=\"delItem(this)\"><img src=\"themes/comm/images/spDel.gif\" />删除</a>";
		sHtml += "</div>";
		sHtml += "</td>";
		sHtml += "</tr>";
	    sHtml += "  <tr>";
	    sHtml += "    <td height=\"27\" class=\"spTit\" style=\"width:auto\">设备编号：</td>";
	    sHtml += "    <td width=\"128\"><span name=\"deviceNO\">" + allotDevice.deviceNO + "</span></td>";
	    sHtml += "    <td  class=\"spTit\" style=\"width:auto\">设备名称：</td>";
	    sHtml += "    <td width=\"150\"><span name=\"deviceName\">" + allotDevice.deviceName + "</span><input type=\"hidden\" name=\"deviceID\" value=\"" + allotDevice.deviceID + "\"/>";
	    sHtml += "		<input type=\"hidden\" name=\"listDevBuyType\" value=\"" + allotDevice.buyType + "\"/>";
	    sHtml += "		<input type=\"hidden\" name=\"listDevBuyTypeName\" value=\"" + allotDevice.buyTypeName + "\"/>";
	    sHtml += "		<input type=\"hidden\" name=\"listAreaName\" value=\"" + allotDevice.areaName + "\"/>";
        sHtml += "		<input type=\"hidden\" name=\"listAreaCode\" value=\"" + allotDevice.areaCode + "\"/></td>";
	    sHtml += "    <td  class=\"spTit\"  style=\"width:auto\">设备型号：</td>";
	    sHtml += "    <td width=\"180\">" + allotDevice.deviceModel + "</td>";
	    sHtml += "    <td rowspan=\"4\"  class=\"spOpW\">&nbsp;</td>";
	    sHtml += "  </tr>";
	   
		if(allotDevice.devCfgDesc!=null && allotDevice.devCfgDesc!=""){
		  sHtml += "  <tr>";
		  sHtml += "    <td class=\"spTit\" height=\"27\" style=\"width:auto\">配置信息：</td>";
	      sHtml += "    <td colspan=\"5\" width=\"360\"><span name=\"devCfgDesc\">" + allotDevice.devCfgDesc + "</span></td>";
	      sHtml += "  </tr>";
		}
	    sHtml += "  <tr>";
	    sHtml += "    <td class=\"spTit\">设备类别：</td>";
	    sHtml += "    <td width=\"180\"><span name=\"listDeviceClassName\">" + allotDevice.deviceClassName + "</span><input type=\"hidden\" name=\"listDeviceClassID\" value=\"" + allotDevice.deviceClassID + "\"/></td>";
	//    sHtml += "    <td class=\"spTit\" style=\"width:120px\">调拨前设备用途：</td>";
	//    sHtml += "    <td ><span name=\"purposeBef\" style=\"display: none\">" + allotDevice.purposeBef + "</span>" + allotDevice.purposeNameBef + "</span></td>";
	    sHtml += "    <td  class=\"spTit\" style=\"width:136px\">调拨前工作所在地：</td>";
	    sHtml += "    <td colspan='3'> <span name=\"areaCodeBef\" style=\"display: none\">" + allotDevice.areaCodeBef + "</span>" + allotDevice.areaNameBef + "</td>";
	    sHtml += "  </tr>";
	    sHtml += "</table>";
	    sHtml += " <div class=\"blank\" style=\"height:5px\"></div>";
	  	sHtml += "</div>";
	});
  	return sHtml;
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
	$(".feesSp").each(function(i){
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
