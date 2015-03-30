//添加设备信息
var classSelect;//设备类别
var args = parent.window.dialogParams;
var optionMap = new Map();//设备属性
var devCalssMap = args.getDevCalssMap;
var optionItem="";
var deviceClassSel;
var areaCode;
var propertyDetailsFlag = false;

/**
 * 最近一次选择的设备类别值
 * @type 
 */
var lastDeviceClassSelValue;
$(initEquipmentInfoEdit);

function initEquipmentInfoEdit() {
	
	$("#deviceName").val(args.deviceName);
	$("#deviceModel").val(args.deviceModel);
	$("#deviceClassId").val(args.deviceClass);
	$("#deviceClassName").html(args.deviceClassName);
	$("#buyTime").val(args.buyTime);
	$("#description").val(args.description);
	$("#optionLists").val(args.optionLists);
	$("#price").val(args.price);
	areaCode = args.areaCode;
	$("#deviceClassOld").val(args.deviceClass);
	$("#deviceTypeName").html(args.deviceTypeName);
	$("#purposes").val(args.purpose+";");
	//提交保存
	$("#saveBtn").click(function(){
		saveDevice();
	});
	initDeviceClassSel();
	
}
function saveInfo(){
	parent.closeDialog();	
}

function doclose(){
	parent.closeDialog();	
}
/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel() {
	deviceTypeCode = args.deviceType;
	var areaCode = args.areaCode;
	var deviceClassCode = args.deviceClass;
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
			queryOptions(value);
			lastDeviceClassSelValue = value;
		}
	});     
}
//查询
function queryOptions(value){
	$("#optionTab").html("");
	var optionLists = $("#optionLists").val();
	$("#optionLists").val("");
	if(optionLists!=null && optionLists!=""){
		var optionStr= new Array();   
		optionStr=optionLists.split(";");
		for (i=0;i<optionStr.length;i++ )   
	   	{   
	   	   var str= new Array();
	   	   str=optionStr[i].split(":");
		   optionMap.put(str[0],str[1]);
	     }
	}
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_class",
		data : {
				act:"getoptionlistbyclassid",
				classId: value
			},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("config-items",xml).each(
	                    function(index){
	                        var option = $(this);
	                        var itemNameOld=$("item-name",option).text();
	                        var flag= true;
	                        //属性明细
	                        var value = optionMap.get(itemNameOld);
	                        optionMap.remove(itemNameOld);
							bodyHTML += createTR(option.attr("id"),$("item-name",option).text(),value);
							
	                });
	                 optionMap.each( function(key,value,index){
			               bodyHTML += createTR(1,key,value);
			        });
			        if(bodyHTML!=""){
	                	$("#configInfo").show();
	                	propertyDetailsFlag = true;
	                	$("#optionTab").append(bodyHTML);
	                }else{
	                	propertyDetailsFlag = false;
	                	$("#configInfo").hide();
	                }
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}
//创建行
function createTR(id,itemName,value){
	
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		+ "<td>"+itemName+"</td>"
		+"<td><input id=\"sbmc5\" type=\"text\" maxlength=\"50\" value=\""+value+"\" class=\"ipt05\" style=\"width:260px\"/></td>"
    html += "</tr>";
    return html;
}
/**
 * 提交保存
 */
function saveDevice(){   
    if (checkRegInfo()){
    	return false;
    }
    //------设备入库单属性-------
    var deviceClassName = deviceClassSel.getDisplayValue();
    var deviceClass = deviceClassSel.getValue();
    var deviceName = $.trim($("#deviceName").val());
    var deviceModel = $.trim($("#deviceModel").val());
    var buyTime = $.trim($("#buyTime").val());
    var description = $("#description").val();
    var price = $.trim($("#price").val());
    //设备属性
    var optionLists=[];
    if(propertyDetailsFlag){
    	optionLists=initOptionTiemInfo();
    	if(!optionLists){
			return false;
		}
    }
	
	
	
	 var purpose = $("input[@type='radio'][name='_doc_chk'][@checked]").val();
//	 if(purpose=="" || typeof(purpose)=="undefined"){
//	 	$.alert("请选择申购设备工作用途！");
//	 	return false;
//	 }
	var purposeName=$("#name_"+purpose).text();
	var areaCodePurposeName=$("#select_"+purpose+" option:selected").text();
	var areaCodePurpose = $("#select_"+purpose).val();
	var purposeFlag = $("#flag_"+purpose).text();
//	if(areaCodePurpose=="" || typeof(areaCodePurpose)=="undefined"){
//	 	$.alert("请选择申购设备工作所在地！");
//	 	return false;
//	 }
	 var deviceClassAndMainMap = args.deviceClassAndMainMap;
	 var mainFlag =  $.trim($("#main_"+deviceClass).val());
	 args.deviceClass
	 if(mainFlag=="true" && deviceClassAndMainMap.size()>0){
	 	//判断是否已添加其他主设备类别
	 	if(args.deviceClassStr!="" && args.deviceClassStr!=deviceClass){
	 		var mainDeviceNum = deviceClassAndMainMap.get(args.deviceClassStr);
		 	if(mainDeviceNum>1){
		 		$.alert("该员工名下无法添加“"+deviceClassName+"”的设备");
		 		return false;
		 	}
	 	}
	 	
	 	
	 	
	 	
	 }else{
	 	if($.trim(purposeFlag)!="true"){
	 		if(devCalssMap.get(deviceClass)==purpose){
	 			$.alert("设备类别“"+deviceClassName+"”的工作用途“"+purposeName+"”只能申购一台！");
	 			return false;
	 		}
	 	}
	 	
	 }
	
	args.deviceClassName = deviceClassName;//
    args.deviceClass = deviceClass;//
    args.deviceName = deviceName;//
    args.deviceModel = deviceModel;//
    args.buyTime = buyTime;//
    args.description = description;//
    args.optionLists = optionLists;
    args.optionItem = optionItem;
    args.purposeName = purposeName;
    args.purpose = purpose;
    args.areaCodePurpose = areaCodePurpose;
    args.areaCodePurposeName = areaCodePurposeName;
    args.price = price;//
    args.mainFlag = mainFlag;
	checkMainAndManyTime(args);
	
   
}
/**
 * 验证信息
 **/
function checkRegInfo(){
	
	var deviceName = $.trim($("#deviceName").val());
	var deviceType = deviceClassSel.getValue();
	if(deviceType == ""){
		$.alert("请选择设备类别!");
		return true;
	}
	
	if(deviceName==""){
		$.alert("“设备名称”不能为空！");
		$("#deviceName").focus();
		return true;
	}
	var result = $.validChar(deviceName,"\"<>'");
	if (result){
		$.alert("“设备名称”不能输入非法字符：" + result+"！");
		$("#deviceName").focus();
		return true;
	}
	
	if(deviceName.length>36){
		$("#deviceName").focus();
		$.alert("“设备名称”长度不能超过36个字符!");
		return true;
	}
	
	var deviceModel = $.trim($("#deviceModel").val());
	if(deviceModel==""){
		$.alert("“设备型号”不能为空！");
		$("#deviceModel").focus();
		return true;
	}
	if (result){
		$.alert("“设备型号”不能输入非法字符：" + result+"！");
		$("#deviceModel").focus();
		return true;
	}
	
	if(deviceModel.length>36){
	
		$("#deviceModel").focus();
		$.alert("“设备型号”长度不能超过36个字符!");
		return true;
	}
	if($.trim($("#buyTime").val()) == ""){
		$.alert("“购买日期”不能为空！");
		return true;
	}
	
	if(validatePriceField("price", "金额", true)){
  		return true;
  	}
	if(parseFloat($.trim($("#price").val()))>parseFloat(args.budgetMoney)){
		$.alert("“购买价格”不能超过“预算金额”");
		return true;
	}
	var description = $.trim($("#description").val());
	result = $.validChar(description,"\"<>");
	if (result){
		$.alert("“设备描述”不能输入非法字符：" + result+"！");
		$("#description").focus();
		return true;
	}
	if(description.length>300){
		$.alert("“设备描述”不能超过300个字符！");
		$("#description").focus();
		return true;
	}
		
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

//组装属性明细
function initOptionTiemInfo(){
	
	var optionTiems=""; 
	var flag=true;
	var indexFlag=true;
	$("#optionTab").find("tr").each(function(){
		var tdobj=$("td",this).eq(0);
		var itemName = $.trim($("td",this).eq(0).text());//配置项
		var remark=$.trim($("input",$("td",this).eq(1)).eq(0).val());
		remark = remark.replace(new RegExp(";","gm"),"；");    
		remark = remark.replace(new RegExp(":","gm"),"：");    
		var result = $.validChar(remark);
		if (remark.length==0) {
			$.alert("设备“配置信息”的“配置项”为"+itemName+"的“信息”不能为空！");
			flag =false;
			return false;
			
		}
		if (result) {
			$.alert("设备“配置信息”的“配置项”为"+itemName+"的“信息”不能包含非法字符：" + result+"！");
			flag =false;
			return false;
			
		}
		if (remark.length >100) {
			$.alert("设备“配置信息”的“配置项”为"+itemName+"的“信息”长度不能大于100个字符！");
			flag =false;
			return false;
		}
		if(indexFlag){
			
			optionItem+=itemName+":"+remark;
			optionTiems+=itemName+":"+remark;
		}else {
			optionTiems+=";"+itemName+":"+remark;
			optionItem+="\r\n"+itemName+":"+remark;
		}
		indexFlag = false;
	});
	if(!flag){
		return false;
	}
	return optionTiems;
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

function showAreaCode(obj){
	$(":radio[name='_doc_chk']").each(function(){
		var purpose = $(this).val();
		$("#span_"+purpose).hide();
	});
	$("#span_"+obj).show();
}

function checkMainAndManyTime(args){
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_apply_start", 
		data:{
			act:"checkmainandmanytime",
			deviceTypeCode : args.deviceType,
			userId : args.applicant,
			areaCode:areaCode,
			deviceClass:args.deviceClass,
			purpose : args.purpose
		},
		success:function saveSuccess(xml){
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				parent.window.returnValue1 = true;
				parent.closeDialog();		
			}else{
				$.alert( message.text());
			}
		},
        error:$.ermpAjaxError
    });  
}

