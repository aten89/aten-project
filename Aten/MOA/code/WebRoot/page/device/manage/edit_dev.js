var mainFrame = $.getMainFrame();
var classSelect;//设备类别
var buyTypeSel;//购买类型
var deviceStatusSel;//设备状态
var optionSelect;//设备属性下拉框
var valOptionSelect;//检测项下拉框
var editVal=false;//设备检查项标识
var validateFlag=false;//是否添加检查项
var flag=true;
var changeFlag=false;
var optionLists = new Array();
var checkItemName;
var checkItemValue;
var bLoaded = false;//是否页面已加载完毕
var propertyDetailsFlag=false;
$(initEquipmentInfoEdit);
/**
 * 新增设备
 */
 var _eqSel;
function initEquipmentInfoEdit() {
	_eqSel = $("#gmfs").ySelect({width: 99,onChange:choosePurchaseWay});
	$("#sblx").ySelect({width:99});
  	$("#sbxh").ySelect({width:99});
  	deviceStatusSel = $("#sbzt").ySelect({width:99,isdisabled: true});
  	var status = $.trim($("#status").val());
	if (status != "") {
		deviceStatusSel.select(status);
	}else{
		deviceStatusSel.select(0);
	}
	
 	initClassDiv();
  	$("#peizi").ySelect({width:69});
  	$("#jcx").ySelect({width:69});
	if($("#valiFormFlag").val()=="true"){
		validateFlag=true;
	  	acceptanceList();
	}
	var deductFlag = $("#deductFlag").val();
	if(deductFlag=="true"){
	$('input[@name=sfjk]').get(0).checked = true;
	}else{
  		$("#je").hide();
  		$('input[@name=sfjk]').get(1).checked = true;
	}
  
  //购买类型
  		var deviceTypeCode = $.trim($("#deviceTypeCode").val());
		buyTypeSel = $("#buyTypeSel").ySelect({width: 100,url: "m/it_dev_man?act=initBuyType&deviceType="+deviceTypeCode,
			afterLoad:function() {
				buyTypeSel.addOption("", "---请选择---", 0);
				buyTypeSel.select(0);
				var buyType = $.trim($("#buyTypeValue").val());
				if (buyType != "") {
					buyTypeSel.select(buyType);
				}
			},onChange:function() {
				if(buyTypeSel.getValue()=="BUY-TYPE-SUB"){
					$("#sfkk").show();
				}else{
					$("#sfkk").hide();
				}
			}
			
			
		});
		//提交保存
		$("#saveBtn").click(function(){
			runReg();
		});
		
		//取消
		$("#closeBtn").click(function(){
			 //关闭
			mainFrame.getCurrentTab().close();
		});
		//新增设备属性
//		$("#addOption").click(function(){
//			addDeviceOption();	
//		});
		//添加验收单
		$("#addValidate").click(function(){
			validateFlag=true;
			acceptanceList();
		});
		//删除验收单
		$("#deleteValidate").click(function(){
			$.confirm("确认要删除验收单吗？",function(r){
				if (r) {
					acceptanceList();
					editVal=false;
					validateFlag=false;
				}
			});
		});
		//添加验收单
		$("#addValidateBnt").click(function(){
			var deviceType = classSelect.getValue();
			if(deviceType == ""){
				$.alert("请选择设备类别!");
				return true;
			}
			addValidateOption();	
		});
		 //打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#applicant").val(user.id);
	   				$("#applicantName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
}

//新增验收单
function addValidateOption(){
	if (editVal) {
		$.alert("请先保存");
		return;
	} else {
		editVal = true;
	}
    var chargeList ="<tr class=\"valilist\"><td>"
                  		+"<div style=\"width:150px\"> <div id=\"validateOption\" name=\"kinds\"></div></div></td>"
                		+"<td><input name=\"checkItem\" type=\"radio\" id=\"yes\" checked=\"checked\" value=\"1\"/><label for=\"yes\" >是</label>&nbsp;&nbsp;<input name=\"checkItem\" type=\"radio\" id=\"no\"  value=\"0\"/><label for=\"no\">否</label></td>"
                		+"<td><input type='text' id=\"checkRemark\"  class='ipt05' style=\"width:250px\"/></td>"
               			+"<td><a href=\"#\" class=\"opLink\" onclick=\"saveValOption(this);return false;\">确定</a>&nbsp;&nbsp;<a href=\"#\" class=\"opLink\" onclick=\"cancelValSave(this);return false;\">取消</a></td>"
              			+"</tr>";
	if($("#valiTab  tr").length==0){
	  $("#valiTab").append(chargeList)
	}else{
	  $(chargeList).insertBefore($("#valiTab  tr:eq(0)"));
	}
	initValOptionDiv();
	
}

/**
 * 设备检测项
 */
function initValOptionDiv() {
	valOptionSelect = $("#validateOption").ySelect({width:119, url:"m/device_check_item?act=getcheckitem&classId="+classSelect.getValue(),
		afterLoad:function(){
			valOptionSelect.addOption("", "---请选择---", 0);
			valOptionSelect.select(0);
		}, onChange:function(value,text){
			checkItemName=text;
			checkItemValue =value;
		}
	});
}

//保存检测项
function saveValOption(self){
	var selfObj=$(self).parent().parent().find("td");
	if(checkItemValue == ""){
		$.alert("请选择设备检查项!");
		return true;
	}
	var checkFlag=$("input[name=checkItem][@type=radio][@checked]").val();
	var flagName="是";
	if(checkFlag!="1"){
		flagName="否";
	}
	
	var itemRemark = $.trim($("input",selfObj.eq(2)).eq(0).val());
	var result = $.validChar(itemRemark);
	//if (itemRemark.length <=0) {
		//$.alert("备注不能为空");
		//$("#checkRemark").focus();
		//return;
	//}
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#checkRemark").focus();
		return;
	}
	if (itemRemark.length >300) {
		$.alert("备注长度不能大于300个字符");
		$("#checkRemark").focus();
		return;
	}
	
  	var flag=false;
  	$("#valiTab").find("tr").each(function(){
		var tdobj=$("td",this).eq(0);
		var itemName = $("td",this).eq(0).text();//配置项
		if(itemName == checkItemName){
			flag=true;
			return;
		}
	});
	if(flag){
		$.alert("验收单设备检查项已经存在");
		return;
  	}
	selfObj.eq(0).html(checkItemName);
	selfObj.eq(1).html(flagName+"<input type=\"hidden\" name=\"checkItemFlag\" id=\"is"+checkItemName+"\"  value=\""+checkFlag+"\"/>");
	selfObj.eq(2).html(itemRemark);
	selfObj.eq(3).html("<a href=\"#\" class=\"opLink\" onclick=\"deleteValOption(this);return false;\">删除</a>");
	editVal=false;
}
function deleteValOption(self){
	if (editVal) {
		$.alert("请先保存");
		return;
	} 
	$.confirm("确认要删除吗？",function(r){
		if (r) {
			editVal = false;
			$(self).parent().parent().remove();
		}
	});
}
/**
 * 设备类别
 */
function initClassDiv() {
	var areaCode = $.trim($("#areaCode").val());
	var deviceNO = $.trim($("#deviceNO").val());
	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
		classSelect = $("#deviceClassDiv").ySelect({width:99, url:"m/device_class?act=classsselassign&deviceType="+deviceTypeCode+"&assignType=0&areaCode="+areaCode,
			afterLoad:function(){
				classSelect.addOption("", "---请选择---", 0);
				if($("#deviceClassOld").val()!=""){
					classSelect.select($("#deviceClassOld").val());
				}else{
					classSelect.select(0);
				}
				classID=classSelect.getValue();
			},onChange:function(value, text) {
					if (bLoaded && $("#deviceClassOld").val() == value) {
						return;
					}
					bLoaded = true;
					if($("#deviceClassOld").val()!=value && $("#deviceClassOld").val()!=""){
						$.confirm("确定要修改设备类别吗？",function(r){
							if (r) {
								if($("#deviceClassOld").val()==""){
									if(value!=""){
										getDeviceNo(value);
									}else{
										$("#deviceNOSpan").html("");
									}
								}else{
									if($("#deviceClassOld").val()==$("#deviceClass").val()){
										$("#deviceNOSpan").html(deviceNO);
									}else{
										if(value!=""){
											getDeviceNo(value);
										}else{
											$("#deviceNOSpan").html("");
										}
									}
								}
								if(changeFlag){
									editVal=false;//设备检查项标识
								}
								changeFlag=true;
								$("#valiTab").html("");
								$("#optionTab").html("");
								$("#optionTab").html("");
								queryOptions();
							} else {
								editVal = false;
								$(self).parent().parent().remove();
							}
						});
	
					}else{
						queryOptions();
						if($("#deviceClassOld").val()==""){
							if(classSelect.getValue()!=""){
										getDeviceNo(classSelect.getValue());
									}else{
										$("#deviceNOSpan").html("");
									}
						}
						$("#deviceClassOld").val(value);
					}
					$("#deviceClassOld").val(value);
					}
					
		});
}
//购买方式
function choosePurchaseWay(){
	if(_eqSel.getValue() == "blgm"){
		$("#sfkk").show();
	}else{
		$("#sfkk").hide();
	}
}

function getDeviceNo(classID) {
	var areaCode = $.trim($("#areaCode").val());
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/it_dev_man", 
		 data:{
			 act:"getnuber",
			 classID:classID,
			 areaCode :areaCode
		 }, 
		 success:function (xml) {
            //解析XML中的返回代码
			var message = $("message", xml);
			var deviceStr=$("message", xml).text();
			if(deviceStr!=""){
				var arrStr=deviceStr.split(";");
				$("#deviceNOSpan").html(arrStr[0]);
				$("#sepNum").val(arrStr[1]);
				$("#orderLength").val(arrStr[2]);//编号长度
			}else{
				$("#deviceNOSpan").html("");
				$("#sepNum").val("");
				$("#orderLength").val("");
			}
			
		}, 
		error:$.ermpAjaxError
	});
}
//取消新增
function cancelValSave(self){
	editVal = false;
	$(self).parent().parent().remove();
}

function displayOrhide(vv){
	if(vv == "yk" && $("#" + vv)[0].checked){
		$("#inDate").val("");
		$("#deductMoney").val("");
			$("#je").show();
	}
	if(vv == "wk" && $("#" + vv)[0].checked){
			$("#je").hide();
	}
}	

function acceptanceList(){
	
	$("#ysd").toggle();	
	$("#ysdtd").toggle();	
}


/**
 * 提交保存
 */
function runReg(){
	if(editVal){
		$.alert("请先保存设备验收单检查项");
		return false;
	}
    if (checkRegInfo()){
    	return false;
    }
//    if($("#deviceClassId").val()!="" && classSelect.getValue()!=$("#deviceClassId").val()){
//    	if (!confirm("修改设备状态将会影响该设备的相关审批流程，并且会自动终止已发起的相关流程！是否继续？")){
//    		return false;
//    	}
//    }
    //------设备入库单属性-------
    var deviceNO = "";
    var buyType = "";
	deviceNO = $.trim($("#deviceNOSpan").html()) + formatString($("#sepNum").val(), "0", parseInt($("#orderLength").val(), 10));
	buyType = buyTypeSel.getValue();
    var deviceId = $.trim($("#deviceId").val());
    var areaCode = $.trim($("#areaCode").val());
    var deviceClass = classSelect.getValue();
    var deviceName = $.trim($("#deviceName").val());
    var deviceModel = $.trim($("#deviceModel").val());
    var buyTime = $.trim($("#buyTime").val());
    var description = $("#description").val();
    var price = $.trim($("#price").val());
    var financeOriginalVal = $("#financeOriginalVal").val();//财务原值
    var sepNum = $.trim($("#sepNum").val());
    var deductFlag = "";
    var deductMoney = "";
    var inDate = "";
  	 if(buyType=="BUY-TYPE-SUB"){
		deductFlag=$("input[@type=radio][@checked]").val();
		if(deductFlag=="1"){
			inDate=$.trim($("#inDate").val());
			deductMoney=$.trim($("#deductMoney").val());
				if(validatePriceField("deductMoney", "扣款金额", true)){
			  		return false;
			  	}
			  	if(parseFloat(deductMoney)>parseFloat(price)){
						$.alert("扣款金额不能大于设备购买金额");
						$("#deductMoney").focus();
						return false;
				}
//			if($.trim($("#inDate").val()) == ""){
//				$.alert("到账日期不能为空");
//				return false;
//			}
//			if(inDate<buyTime){
//				$.alert("到账日期不能小于购买日期");
//				return false;
//			}
			deductFlag=true;
		}else{
			deductFlag=false;
		}
		
	}
	var applicant = $.trim($("#applicant").val());
	var valiDate = $.trim($("#valiDate").val());
	var remark = $.trim($("#remark").val());
	 //设备检查项
	var checkVal = initCheckFormTiemInfo()
	
  	if(validateFlag){
  		if(applicant==""){
  			$.alert("请选择设备验收单检验人");
  			return;
  		}
  		if(valiDate==""){
  			$.alert("设备验收单检验日期不能为空");
  			return;
  		}
  		if(valiDate<buyTime){
  			$.alert("设备验收单检验日期不能小于设备购买日期");
  			return;
  		}
  		if(flag){
  			$.alert("设备验收单检验项不能为空");
  			return;
  		}
  		result = $.validChar(remark,"\"<>");
		if (result){
			$.alert("检查项备注不能输入非法字符：" + result);
			$("#description").focus();
			return false;
		}
		if(remark.length>300){
			$.alert("检查项备注不能超过300个字符");
			$("#description").focus();
			return false;
		}
  		
  	}	
  	var deviceTypeCode = $.trim($("#deviceTypeCode").val());
    //设备属性
  	var optionLists=[];
  	if(propertyDetailsFlag){
		optionLists=initOptionTiemInfo();
		if(!optionLists){
			return false;
		}
	}
//	if (!window.confirm("请确认设备编号是否正确？")) {
//		return;
//	}
	if(deviceId!=null && deviceId!=""){
		var msg = msgSuccess(deviceId);
		if(msg!=""){
			showMsg="该设备目前正在"+msg+"，您当前不能进行“修改”操作。";
			$.alert(showMsg);
			return;
		}
	}
	
    $.ajax({
        type : "POST",
		cache: false,
		url  : "m/it_dev_man",
		data : {
			act: deviceId=="" ? "add" : "modify",
			deviceTypeCode:deviceTypeCode,
			deviceId : deviceId,
			deviceNO:deviceNO,
			deviceClass:deviceClass,
			areaCode:areaCode,
			deviceModel:deviceModel,
			deviceName:deviceName,
			buyTime:buyTime,
			buyType:buyType,
			description:description,
			price:price,
			sepNum:sepNum,
			optionLists:$.toJSON(optionLists),
			checkFormFlag:validateFlag,
			checkForm:$.toJSON(checkVal),
			deductFlag:deductFlag,
   			deductMoney:deductMoney,
    		inDate:inDate,
    		financeOriginalVal : financeOriginalVal//财务原值
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
//组装属性明细
function initOptionTiemInfo(){
	var optionTiems = new Array(); 
	var flag=true;
	$("#optionTab").find("tr").each(function(){
		var tdobj=$("td",this).eq(0);
		var itemName = $("td",this).eq(0).text();//配置项
		var remark=$("input",$("td",this).eq(1)).eq(0).val();
		var obj = new Object();
		obj.itemName=itemName;
		obj.remark =remark;
		var result = $.validChar(remark);
		if (remark.length==0) {
			$.alert("设备“配置信息”的"+itemName+"不能为空！");
			flag =false;
			return false;
			
		}
		if (result) {
			$.alert("设备“配置信息”的"+itemName+"不能包含非法字符：" + result+"！");
			flag =false;
			return false;
			
		}
		if (remark.length >100) {
			$.alert("设备“配置信息”的"+itemName+"长度不能大于100个字符！");
			flag =false;
			return false;
		}
		optionTiems.push(obj);
	});
	if(!flag){
		return false;
	}
	return optionTiems;
}

//组装验收单明细
function initCheckFormTiemInfo(){
	var applicant = $.trim($("#applicant").val());
	var valiDate = $.trim($("#valiDate").val());
	var remark = $.trim($("#remark").val());
	var checkForms = new Array();
	var checkLists = new Array();
	$("#valiTab").find("tr").each(function(){
		var tdobj=$("td",this).eq(0);
		var itemName = $("td",this).eq(0).text();
		var itemRemark = $("td",this).eq(2).text();
		var isEligibility=$("input",$("td",this).eq(1)).eq(0).val();
		var obj = new Object();
		obj.itemName=itemName;
		obj.remark =itemRemark;
		obj.isEligibility =isEligibility;
		checkLists.push(obj);
		flag =false;
	});
	var checkVal = new Array();
	var objVal = new Object();
	objVal.applicant=applicant;
	objVal.valiDate=valiDate;
	objVal.remark=remark;
	objVal.valDetail=checkLists;
	objVal.valiType="2";
	checkForms.push(objVal);
	return checkForms;
}


/**
 * 验证信息
 **/
function checkRegInfo(){
	
	var deviceName = $.trim($("#deviceName").val());
	var result = $.validChar(deviceName,"\"<>'");
	if(deviceName==""){
		$.alert("“设备名称”不能为空！");
		$("#deviceName").focus();
		return true;
	}
	if (result){
		$.alert("“设备名称”不能输入非法字符：" + result+"！");
		$("#deviceName").focus();
		return true;
	}
	
	if(deviceName.length>36){
		$("#deviceName").focus();
		$.alert("“设备名称”长度不能超过36个字符！");
		return true;
	}
	var deviceModel = $.trim($("#deviceModel").val());
	if(deviceModel==""){
		$.alert("“设备型号”不能为空！");
		$("#deviceModel").focus();
		return true;
	}
	result = $.validChar(deviceModel,"\"<>");
	if (result){
		$.alert("“设备型号”不能输入非法字符：" + result+"！");
		$("#deviceModel").focus();
		return true;
	}
	if(deviceModel.length>36){
	
		$("#deviceModel").focus();
		$.alert("“设备型号”长度不能超过36个字符！");
		return true;
	}
	var deviceType = classSelect.getValue();
	if(deviceType == ""){
		$.alert("请选择设备类别！");
		return true;
	}
	if ($("#sepNum").val() == "") {
		$.alert("设备编号的流水号不能为空！");
		$("#sepNum").focus();
		return true;
	}
	var orderLength = parseInt($("#orderLength").val(), 10);
	if($("#sepNum").val().length > orderLength){
		$.alert("设备编号的流水号位数不能超过" + orderLength);
		return true;
	}
	var re =  /^[0-9]*[1-9][0-9]*$/;
	if(!re.test($("#sepNum").val())){
		$.alert("设备编号的流水号必须是正整数");
		return true;
	}
	if (deviceStatusSel.getValue() == "") {
		$.alert("请选择设备状态！");
		return true;
	}
	if (buyTypeSel.getValue() == "") {
		$.alert("请选择购买方式！");
		return true;
	}
	if($.trim($("#buyTime").val()) == ""){
		$.alert("“购买日期”不能为空！");
		return true;
	}
	
	if(validatePriceField("price", "购买金额", true)){
  		return true;
  	}
  	
	var description = $.trim($("#description").val());
	result = $.validChar(description,"\"<>");
	if (result){
		$.alert("“设备描述”不能输入非法字符：" + result);
		$("#description").focus();
		return true;
	}
	if(description.length>300){
		$.alert("“设备描述”不能超过300个字符！");
		$("#description").focus();
		return true;
	}
		
}

function initOptionDiv() {
	optionSelect = $("#deviceOption").ySelect({width:69, url:"m/device_class?act=getoptionbyclassid&classId="+classSelect.getValue(),
		afterLoad:function(){
			optionSelect.addOption("", "---请选择---", 0);
			optionSelect.select(0);
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

//查询
function queryOptions(){
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_class",
		data : {
				act:"getoptionlistbyclassid",
				classId: classSelect.getValue()
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
							$("#optionTab").find("tr").each(function(){
								var tdobj=$("td",this).eq(0);
								var itemName = $("td",this).eq(0).text();//配置项
								if(itemNameOld==itemName){
									flag = false;
									return;
								}
							});
							if(flag){
								bodyHTML += createTR(option.attr("id"),$("item-name",option).text());
							}
	                });
	                if(bodyHTML!=""){
	                	$("#configInfo").show();
	                	propertyDetailsFlag = true;
	                	$("#optionTab").append(bodyHTML);
	                }else{
	                	if($("#propertyDetails").val()=="1"){
							$("#configInfo").show();
							propertyDetailsFlag = true;
							$("#propertyDetails").val("0")
						}else{
							$("#propertyDetails").val("0")
							propertyDetailsFlag = false;
							$("#configInfo").hide();
						}
	                	
	                }
	                
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}

//创建行
function createTR(id,itemName){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		+ "<td>"+itemName+"</td>"
		+"<td><input id=\"sbmc5\" type=\"text\" maxlength=\"50\" class=\"ipt05\" style=\"width:215px\"/></td>"
    html += "</tr>";
    return html;
}

/**
 * 格式化字符
 * @param {} str 待格式化的字符
 * @param {} padChar 不足位数用于填充的字符
 * @param {} len 目标字符串的位数
 * @return {} 格式化后的字符串
 */
function formatString(str, padChar, len) {
	var result = "";
	for(var i = 0; i < len - str.length; i++){
		result += padChar;
	}
	result += str;
	return result;
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