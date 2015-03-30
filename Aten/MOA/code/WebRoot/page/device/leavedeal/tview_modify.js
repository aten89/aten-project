//设备报废--驳回修改
var mainFrame = $.getMainFrame();
var deviceTypeSel;
var lastDeviceTypeSelValue;
var bIniting = true;//是否正在进行页面初始化
$(initEquipmentInfoScrapAmend);
function initEquipmentInfoScrapAmend() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
  	//initDeviceTypeSel($.trim($("#deviceTypeCode").val()), $.trim($("#deviceClassCode").val()));
	$("#btnSelectDevice").click(function(){
		selectDevice();
	});
 	$("#saveBtn").click(function(){
 		saveInfo(true);
 	});
 	
 	$("#draftBtn").click(function(){
 		saveInfo(false);
 	});
 	
 	$("#closeBtn").click(function(){
 		window.returnValue = true;
		window.close();
 	});
 	
 	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					var bSuccess = saveInfo(false);
					if (bSuccess) {
						saveTaskComm(tiid,transition,"");
					}
				} else {
					return;
				}
			});
			
		});
	});
}

/**
 * 保存处理意见
 * @param {} tiid
 * @param {} transition
 * @param {} comment
 * @return {Boolean}
 */
function saveTaskComm(tiid,transition,comment){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=deal_approve",
		data : {
   			tiid : tiid,
   			transition : transition,
   			comment : comment
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("操作成功", function(){
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

/**
 * 购买类型
 * @param {} buyType
 */
function initBuyType(buyType) {
	if (!bLoadedBuyTypeSel) {
		buyTypeSel = $("#buyTypeSel").ySelect({width: 75,url: "m/it_dev_man?act=initBuyType",
			afterLoad:function() {
				buyTypeSel.addOption("", "---请选择---", 0);
				buyTypeSel.select(0);
				if (buyType != null && buyType != "") {
					buyTypeSel.select(buyType);
				}
				bLoadedBuyTypeSel = true;
			}
		});
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
		width : 35, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			if (areaCode != null && areaCode != "") {
				belongtoAreaSel.select(areaCode);
			} else {
				belongtoAreaSel.select(0);
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
			//$.$.alert("afterLoad");
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				deviceTypeSel.disable(true);
			} else {
				deviceTypeSel.select(0);
			}
			lastDeviceTypeSelValue = deviceTypeSel.getValue();
		},
		onChange : function(value, text) {
			if (lastDeviceTypeSelValue == value) {
				return;
			}
			if (hasList() && !clearDeviceChooseResult("资产类别")) {
				deviceTypeSel.select(lastDeviceTypeSelValue);//
				if (bIniting) {
					bIniting = false;
				}
				return;
			}
			lastDeviceTypeSelValue = value;
			if (bIniting) {
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

var dialogWin;
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
	var deviceClassCode = $.trim($("#deviceClassCode").val());
	if (deviceTypeCode == "") {
		$.alert("请选择资产类别！");
		return;
	}
	var excludeDeviceIDs = getSelectedDeviceIDs();//要排除的设备ID
	var sURL = BASE_PATH + "m/it_dev_man?act=initdevselpage&devAttachFlag=true&disableMyDeviceFlag=true";//devAttachFlag：是否显示出设备归属搜索项
	sURL += "&defaultMyDeviceFlag=true";//defaultMyDeviceFlag:我名下的设备默认为已打勾
	var sFeature = "dialogHeight:430px;dialogWidth:908px;status:no;scroll:auto;help:no";
	if($.browser.msie && $.browser.version==6.0){
		sFeature = "dialogHeight:485px;dialogWidth:916px;status:no;scroll:auto;help:no";
	}
	
	var args = new Object();
	//args.assignFlag = true;
	//args.assignType = 0;
	args.deviceTypeCode = deviceTypeCode;
	args.approvingFlag = false;
	args.excludeScrapFlag = true;
	args.deleteDeviceIDs = $("#deleteDeviceIDs").val();
	//args.isUsing = 0;
	args.status = 1;
	//args.areaCode = $("#areaCode").val();
	//args.deviceClassCode = deviceClassCode;
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
				url : BASE_PATH + "m/it_dev_man?act=loaddevicelist&initDevCfgList=false",//initDevCfgList:是否初始化设备配置项
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
				    	var aScrapDevice = [];
				    	$(xml).find('device').each(function(index){
				    		var scrapDevice = new Object();
				    		scrapDevice.deviceID = $(this).attr("id");
				    		scrapDevice.deviceNO = $("device-no", this).text();
				    		scrapDevice.deviceModel = $("device-model", this).text();
				    		scrapDevice.deviceName = $("device-name", this).text();
				    		scrapDevice.buyTypeName = $("buy-type-str", this).text();
				    		scrapDevice.buyType = $("buy-type", this).text();
				    		aScrapDevice.push(scrapDevice);
				    		
				    		//操作deleteIDs字段
							var re;
				    		eval("re=/" + $(this).attr("id") + "/ig");
							var deleteDeviceIDs = $("#deleteDeviceIDs").val().replace(/re/ig,"").replace(/,,/ig,",");
							$("#deleteDeviceIDs").val(deleteDeviceIDs);
		
						});
						var sHtml = createScrapDeviceDetailHTML(aScrapDevice, $.trim($("#formType").val()));
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
}

function getSelectedDeviceIDs() {
	var ids = "";
	$(".feesSp").each(function(i){
		if (ids != "") {
			ids += ",";
		}
		ids += $("span[name='deviceID']", this).text();
	});
	return ids;
}
function getSelectedDeviceMinBuyTime() {
	var buyTime = "";
	$("#deviceChooseResultBody tr").each(function(i){
		var id = $(this).attr("id");
		var tempBuyTime = $.trim($("span[name='buyTime']", this).text());
		if(buyTime==""){
			buyTime = tempBuyTime;
		}else{
			if(tempBuyTime<buyTime){
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
			//追加deleteIDs
			var deleteDeviceID = $("span[name='deviceID']", oDel.get(0)).text();
			var deleteDeviceIDs = $("#deleteDeviceIDs").val();
			if (deleteDeviceIDs != "") {
				deleteDeviceIDs += ",";
			}
			deleteDeviceIDs += deleteDeviceID;
			$("#deleteDeviceIDs").val(deleteDeviceIDs);
			oDel.remove();
			$("div.feesSp").each(function(i){
				$("span[name='orderNum']", this).text(i+1);
			});
		}
	});
}

function saveInfo(startFlow){
	var bSuccess = false;
	if (!validateForm()) {
		return;
	}
	var aDevList = [];
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		devDetail.deviceID = $("span[name='deviceID']", this).text();
		devDetail.reason = $("input[name='reason']", this).val();
		devDetail.dealType = $("select[name='dealTypeSel']", this).val();
		devDetail.orderNum = $("span[name='orderNum']", this).text();
		aDevList.push(devDetail);
	});
	var id = $("#id").val();
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/dev_discard_start?act=amend",//
		data:{
			id : id,
			aDevList : $.toJSON(aDevList),
			isStartFlow : startFlow
		},
		success:function saveSuccess(xml){
			//$.alert($(xml).get(0).xml)
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				bSuccess = true;
			}
			else{
				$.alert(message.text());
			}
		},
        error:$.ermpAjaxError
    }); 
    return bSuccess;
}

function validateForm() {
	var passedList = true;
	if ($("div.feesSp").size() == 0) {
		$.alert("你还未选择要报废的设备！");
		return false;
	}
	$("div.feesSp").each(function(){
		var dealType = $("select[name='dealTypeSel']", this).val();
		if (dealType == "") {
			$.alert("编号为”" + $("span[name='deviceNO']", this).eq(0).text() +  "“设备的“处理方式“不能为空！");
			passedList = false;
			return false;
		}
		
		if ($.validInput("input[name='reason']", "编号为“" + $("span[name='deviceNO']", this).eq(0).text() +  "”设备的“报废原因”", 
				false, "\<\>\'\"", 100)) {
			return false;
		}
		/*
		var reason = $("input[name='reason']", this).val();
		if (reason == "") {
			$.alert("编号为“" + $("span[name='deviceNO']", this).eq(0).text() +  "”设备的“报废原因”不能为空！");
			passedList = false;
			return false;
		}
		*/
	});
  	return passedList && true;
}
function doclose(){
	window.close();	
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
function createDealTypeOption(url,data, defaultName, selectKey, isCompBuy) {
	if (!defaultName) {
		defaultName = "请选择...";
	}
	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
	url=url+"&deviceType="+deviceTypeCode;
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
					if (isCompBuy && key == "SCRAP_DISPOSE_TAKE") {
						//如果购买方式为公司全额的设备，处理方式的下拉框要过滤掉回购选项
						return true;
					}
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

/**
 * 初始化调拨类型下拉列表
 * @param {} allotType 调拨类型
 */
function initAllotTypeSel(allotType) {
	allotTypeSel = $("#allotTypeSel").ySelect({
		width : 85, 
		url:BASE_PATH+"m/data_dict?act=selectallottype",
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
 * 生成报废设备详情HTML
 */
function createScrapDeviceDetailHTML(scrapDevices, formType) {
	var sHtml = "";
	$.each(scrapDevices, function(i, scrapDevice) {
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
	    sHtml += "    <td width=\"70\" height=\"27\" class=\"spTit\">设备名称：</td>";
	    sHtml += "    <td width=\"128\"><span name='deviceName'>" + scrapDevice.deviceName + "</span><span name='deviceID' style=\"display:none;\">" + scrapDevice.deviceID + "</span></td>";
	    sHtml += "    <td width=\"70\" class=\"spTit\">设备编号：</td>";
	    sHtml += "    <td width=\"100\"><span name='deviceNO'>" + scrapDevice.deviceNO + "</span></td>";
	    sHtml += "    <td width=\"70\" class=\"spTit\">购买方式：</td>";
	    sHtml += "    <td width=\"290\"><span name='buyTypeName'>" + $.trim(scrapDevice.buyTypeName) + "</span><span name='buyType' style=\"display:none;\">" + $.trim(scrapDevice.buyType) + "</span></td>";
	    sHtml += "    <td rowspan=\"2\" class=\"spOpW\">&nbsp;</td>";
	    sHtml += "  </tr>";
	    sHtml += "  <tr>";
	    
	    var dealTypeOptionsHTML = "";
	    if (formType == "0") {
	    	dealTypeOptionsHTML = createDealTypeOption("m/data_dict?act=selectscrapdisposetypexml", "", "", "", $.trim(scrapDevice.buyType) == "BUY-TYPE-COMP");
	    } else if (formType == "1") {
	    	dealTypeOptionsHTML = createLeaveDealTypeOption("m/data_dict?act=leavedealtypesel", "", "", "", $.trim(scrapDevice.buyType));
	    }
	    
	    sHtml += "    <td class=\"spTit\"><span class=\"cRed\">*</span>处理方式：</td>";
	    sHtml += "    <td><select name=\"dealTypeSel\" style=\"width:100px;height:20px\">" + dealTypeOptionsHTML + "</select></td>";
	    sHtml += "    <td class=\"spTit\">原因：</td>";
	    sHtml += "    <td colspan=\"3\" width=\"500\"><input type=\"text\" name=\"reason\" class=\"ipt01\" style=\"width:360px\"/></td>";
	    sHtml += "  </tr>";
	    sHtml += "  <tr>";
	    sHtml += "    <td colspan=\"7\" style=\"height:6px;\">&nbsp;</td>";
	    sHtml += "  </tr>";
	    sHtml += "</table>";
	    sHtml += " <div class=\"blank\" style=\"height:5px\"></div>";
	  	sHtml += "</div>";
	});
  	return sHtml;
}

function createLeaveDealTypeOption(url,data, defaultName, selectKey, buyType) {
	if (!defaultName) {
		defaultName = "请选择...";
	}
	var deviceTypeCode =  $.trim($("#deviceTypeCode").val());
	url=url+"&deviceType="+deviceTypeCode;
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
//					if (buyType == "BUY-TYPE-SELF") {
//						if (str[0] == "LEAVE_DISPOSE_BACKBUY" || str[0] == "LEAVE_DISPOSE_TOSTORAGE") {
//							//如果购买方式为个人全额的设备，处理方式的下拉框要过滤掉离职回购，退库选项
//							return true;
//						}
//					} else if (buyType == "BUY-TYPE-SUB" || buyType == "BUY-TYPE-COMP") {
//						if (str[0] == "LEAVE_DISPOSE_TAKE" || str[0] == "LEAVE_DISPOSE_TOSTORAGE") {
//							//如果购买方式为公司全额或比例购买的设备，处理方式的下拉框要过滤掉拿走、退库选项
//							return true;
//						}
//					} 
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