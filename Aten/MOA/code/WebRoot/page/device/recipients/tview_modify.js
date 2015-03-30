//设备领用申购--驳回修改
var mainFrame = $.getMainFrame();
var dialogWin;
var TYPE_TITLE = "资产类别";
var CLASS_TITLE = "设备类别";
var deviceTypeSel;//设备类型下拉列表
var deviceClassSel;
var belongtoAreaSel;//所属区域
var bLoadedBelongtoAreaSel = false;
var bLoadedDeviceClassSel = false;
var flag=false;
var allotTypeSel;
var buyTypeSel;
var bLoadedBuyTypeSel = false;
var bIniting = true;//是否正在进行页面初始化
var validMainDevFlag = false;//是否验证主设备
var purposePurpose;
var purposeAreaCode;
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
$(initEquipmentInfoUseAmend);

function initEquipmentInfoUseAmend() {
	if ($("#applyType").val() == "1") {
		initBuyType($.trim($("#deviceTypeCode").val()),$("#buyType").val());//购买方式
	}
	//initDeviceTypeSel($.trim($("#deviceTypeCode").val()), $.trim($("#deviceClassCode").val()));
	//initAreaSel($("#areaCode").val());
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
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
//	initDevPuerpose($("#deviceTypeCode").val(),$("#areaCode").val(), $("#deviceClassCode").val());
 	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			var id = $("#id").val();
			var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					var bSuccess = saveInfo(true, $.trim($("#applyType").val()));
					if (bSuccess) {
						saveTaskComm(id,tiid,transition,"");
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
function saveTaskComm(id,tiid,transition,comment){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=reject_deal_approve",
		data : {
			id : id,
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
function initBuyType(deviceType,buyType) {
//	if (!bLoadedBuyTypeSel) {
		buyTypeSel = $("#buyTypeSel").ySelect({width: 75,url: "m/it_dev_man?act=initBuyType&deviceType="+deviceType,
			afterLoad:function() {
				buyTypeSel.addOption("", "---请选择---", 0);
				buyTypeSel.select(0);
				if (buyType != null && buyType != "") {
					buyTypeSel.select(buyType);
				}
				bLoadedBuyTypeSel = true;
			}
		});
//	}
}

function acceptanceList(){
	$("#ysd").toggle();	
	$("#ysdtd").toggle();	
}

//设备选择
function equipmentChoose(){
	selectDevice();
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
	var deviceClassCode = $.trim($("#deviceClassCode").val());
	if (deviceTypeCode == "") {
		$.alert("资产类别为空！");
		return;
	}
	var areaCode = $.trim($("#areaCode").val());
	if (areaCode == "") {
		$.alert("所属区域为空！");
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
	args.areaCode = areaCode;
	args.deviceClassCode = deviceClassCode;
	args.deleteDeviceIDs = $("#deleteDeviceIDs").val();
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
					var listData="";
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
							//操作deleteIDs字段
							var re;
				    		eval("re=/" + id + "/ig");
							var deleteDeviceIDs = $("#deleteDeviceIDs").val().replace(/re/ig,"").replace(/,,/ig,",");
							$("#deleteDeviceIDs").val(deleteDeviceIDs);
						});
						listData = createUseDeviceDetailHTML(aUseDevice, $.trim($("#areaCode").val()));
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
			var selfObj=$("table", $(o).parent().parent().parent().parent().parent().parent().get(0));
			//追加deleteIDs
			var deleteDeviceID = $("span[name=deviceID]",selfObj).text();
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

function saveInfo(startFlow, formType){
	var bSuccess = false;
	if (startFlow && !validateForm(formType)) {
		return;
	}
	var remark = $.trim($("#remark").val());
	var applyType = $("#applyType").val();//0:领用;1:申购
	var aDevPurchaseList = [];//领用
	var aPurchaseDevices = [];//申购
	var purposeValues =[];
	var buyType = "";
	var budgetMoney = "";
	var workAreaCode="";
	if (applyType == "1") {
		//申购
		buyType = $.trim(buyTypeSel.getValue());
		budgetMoney = $("#budgetMoney").val();
		purposeValues = getPurposeValues();
		var devPurchaseDetail = new Object();
		devPurchaseDetail.devCfgDesc = $("#devCfgDesc").val();
		devPurchaseDetail.belongtoAreaCode = $.trim($("#areaCode").val());
		devPurchaseDetail.deviceClass = $.trim($("#deviceClassCode").val());
		devPurchaseDetail.planUseDate = $.trim($("#planUseDate").val());
		devPurchaseDetail.purpose = purposePurpose;
		devPurchaseDetail.areaCode = purposeAreaCode;
		aPurchaseDevices.push(devPurchaseDetail);
		workAreaCode =purposeAreaCode
	} else {
		//领用
		if ($("div.feesSp").size() > 0) {
			$("div.feesSp").each(function(i){
				var devPurchaseDetail = new Object();
				devPurchaseDetail.deviceID = $("span[name=deviceID]",this).text();
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
    $.ajax({
        type:"POST",
        async : false,
		cache:false,
		url:"m/dev_apply_start?act=draftmanamend",//
		data:{
			id : id,
			remark : remark,
			workAreaCode:workAreaCode,
			devPurchaseList : $.toJSON(aDevPurchaseList),//领用设备列表
			purchaseDevices : $.toJSON(aPurchaseDevices),//申购设备列表
			purposeValues : $.toJSON(purposeValues),//设备用途
			buyType : buyType,
			budgetMoney : budgetMoney,
			startFlow : false,
			validMainDevFlag : validMainDevFlag
		},
		success:function saveSuccess(xml){
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

function validateForm(formType) {
	var planUseDate = $.trim($("#planUseDate").val());
	if (planUseDate == "") {
		$.alert("“预计使用日期”不能为空！");
		return false;
	}
	var applyType = $("#applyType").val();
	var arrFormTypeDisplayName = ["领用", "申购"];
	var flag = true;
	if(applyType=="0"){
		flag = false;
	}
	if($.validInput("remark", arrFormTypeDisplayName[parseInt(applyType)] + "说明", flag, "\<\>\'\"", 2000)){
		return false;
	}
	if (!validListMainDev(formType)) {
		return false;
	}
	var passedList = true;
	if (applyType == "0") {
		//领用
		if ($("div.feesSp").size() == 0) {
			$.alert("你还未选择设备！");
			return false;
		}
		$("div.feesSp").each(function(){
			var areaCode = $("select[name='areaSel']", this).val();
			if (areaCode == "") {
				$.alert("编号为”" +$("span[name=deviceNO]",this).text()+  "“设备的“工作所在地“不能为空！");
				passedList = false;
				return false;
			}
			var purpose = $("select[name='devicePurposeSel']", this).val();
			if (purpose == "") {
				$.alert("编号为“" +$("span[name=deviceNO]",this).text() +  "”设备的“设备用途”不能为空！");
				passedList = false;
				return false;
			}
		});
		
		var buyTime = getSelectedDeviceMinBuyTime();
		if(planUseDate<buyTime){
			$.alert("“预计使用日期”不能小于设备购买日期"+buyTime+"！");
			return false;
		}
	} else {
		if ($.trim(buyTypeSel.getValue()) == "") {
			$.alert("请选择购买方式！");
			return false;
		}
		//申购
		if (validatePriceField("budgetMoney", "预算金额", true)) {
			return false;
		}
//		if(!getcheckSelected()){
//			$.alert("请选申购设备用途！");
//			return false;
//		}
		if($.validInput("devCfgDesc", "配置要求", true, "\<\>\'\"", 2000)){
			return false;
		}
	}
  	return passedList && true;
}
function doclose(){
	window.close();	
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
		listData += "   <td width=\"70\" height=\"27\" class=\"spTit\">设备名称：</td>";
		listData += "    <td width=\"128\">" + useDevice.deviceName + "<span name=\"deviceID\" style=\"display: none\">" + useDevice.deviceID + "</span>";
		listData += "		<input type=\"hidden\" name=\"listAreaName\" value=\"" + useDevice.areaName + "\"/>";
        listData += "		<input type=\"hidden\" name=\"listAreaCode\" value=\"" + useDevice.areaCode + "\"/></td>";
		listData += "   <td width=\"70\" class=\"spTit\">设备编号：</td>";
		listData += "   <td width=\"100\"><span name=\"deviceNO\">" + useDevice.deviceNO + "</span></td>";
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
		listData += "   <tr>";
		listData += "    <td width=\"70\" height=\"27\" class=\"spTit\">设备类别：<span name=\"buyTime\" style=\"display: none\">" + useDevice.buyTime + "</span></td>";
		listData += "    <td width=\"128\"><span name=\"listDeviceClassName\">" + useDevice.deviceClassName + "</span><input type=\"hidden\" name=\"listDeviceClassID\" value=\"" + useDevice.deviceClassID + "\"/></td>";
//		listData += "     <td width=\"70\" class=\"spTit\">设备用途：</td>";
//		listData += "     <td width=\"100\"><select name='devicePurposeSel' style='width:100px;height:20px'>" + purposeOptionsHTML + "</select></td>";
		listData += "<td width=\"70\" class=\"spTit\">工作所在地：</td>";
		listData += " <td width=\"290\"colspan=\"3\"><select name='areaSel' style='width:60px;height:20px'>" + areaOptionsHTML + "</select></td>";
		listData += "   </tr>";
		listData += "  </table>";
		listData += " <div class=\"blank\" style=\"height:5px\"></div>";
		listData += " </div>		";
	});
  	return listData;
}

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
		purPoseItems.push(obj)
	});
	return purPoseItems;
}

/**
 * @return {}
 */
function getcheckSelected() {
	var checkFlag = false;
	$(":radio[name='_doc_chk']:checked").each(function(){
		checkFlag=true;
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
	} else if (formType == "1") {
		var deviceClassID = $.trim($("#deviceClassCode").val());
		var areaCode = $.trim($("#areaCode").val());
		if (mainDevCfgs.indexOf(areaCode + "," + deviceClassID) != -1) {
			if (!validMainDevFlag) {
				//当有主设备的时候才要去验证主设备，当都没有主设备的时候就不用去验证主设备
				validMainDevFlag = true;
			}
		}
	}
	return true;
}
