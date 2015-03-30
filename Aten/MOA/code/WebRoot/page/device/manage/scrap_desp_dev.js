var mainFrame = $.getMainFrame();
var dialogWin;
$(initEquipmentInfoUse);
/**
 * 设备领用
 */
function initEquipmentInfoUse() {
	var deviceTypeCode = $("#deviceTypeCode").val();
  	$("#btnSelectDevice").click(function(){
		selectDevice(deviceTypeCode);
	});
	if ($("#deviceID").val() != "") {
		//已选择了设备
		$("#btnSelectDevice").hide();
	}
	$("#saveBtn").click(function(){
		saveInfo();
	});
	$("#closeBtn").click(function(){
		//关闭
		mainFrame.getCurrentTab().close();
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
function selectDevice(deviceTypeCode) {
	var deviceTypeCode = deviceTypeCode == null ? "" : deviceTypeCode;
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
	args.status = 7;
	args.areaCode = $("#areaCode").val();
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
				    	$(xml).find('device').each(function(index){
				    		var curELe = $(this);
		                    var id = $.trim(curELe.attr("id"));
		                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
									+ "<td>" + $("device-no",curELe).text() + "</td>"
					  				+ "<td>" + $("device-name",curELe).text() + "</td>"
									+ "<td>" + $("device-model",curELe).text() + "</td>"
					  				+ "<td>" + $("device-class-str",curELe).text() + "</td>"
									+ "<td>" + $("buy-time",curELe).text() + "</td>"
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
function getSelectedDeviceMinBuyTime() {
	var buyTime = "";
	$("#deviceChooseResultBody tr").each(function(i){
		var id = $(this).attr("id");
		if(buyTime==""){
			buyTime = $.trim($("#buyTime_"+id).val());
		}else{
			if($.trim($("#buyTime_"+id).val())<buyTime){
				buyTime = $.trim($("#buyTime_"+id).val());
			}
		}
		
	});
	return buyTime;
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

/**
 * 删除设备选择结果
 * @param {} o
 */
function delItem(o) {
	$.window.confirm("是否确认删除？",function(r){
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
	var deviceTypeCode = $("#deviceTypeCode").val();
	var regAccountID = $.trim($("#regAccountID").val());
	var regTime = $.trim($("#regTime").text());
	var salePrice = $.trim($("#salePrice").val());
	var saleDate = $.trim($("#saleDate").val());
	var aDevDiscardDealList = [];
	$("#deviceChooseResultBody tr").each(function(i){
		var devDiscardDealDetail = new Object();
		devDiscardDealDetail.deviceID = $(this).attr("id");
		aDevDiscardDealList.push(devDiscardDealDetail);
	});
	
    var act;
    if(id == "") {
		act = "scrapdeal";
	} else {
		act = "modify_scrapdeal";
	}
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/it_dev_man?act="+act,//
		data:{
			deviceTypeCode : deviceTypeCode,
			regAccountID : regAccountID,
			regTime : regTime,
			salePrice : salePrice,
			saleDate : saleDate,
			devDiscardDealList : $.toJSON(aDevDiscardDealList)
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
	var saleDate = $.trim($("#saleDate").val());
	var buyTime = getSelectedDeviceMinBuyTime();
	if(validatePriceField("salePrice","变卖价格",true)){
		return false;
	}
	if (saleDate == "") {
		$.alert("“变卖日期”不能为空！");
		return false;
	}
	if(saleDate<buyTime){
		$.alert("“变卖日期”不能小于设备“购买日期”"+buyTime+"！");
		return false;
	}
	if ($("#deviceChooseResultBody tr").size() == 0) {
		$.alert("你还未选择设备！");
		return false;
	}
  	return true;
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