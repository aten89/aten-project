//设备申购审批
var mainFrame = $.getMainFrame();
$(initEquipmentInfoUseHandle);
function initEquipmentInfoUseHandle() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment ="领用确认";
			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					saveTaskComm(tiid,transition,comment);
				} else {
					return;
				}
			});
		});
	});
//	initDevPuerpose($("#deviceType").val(),$("#areaCode").val(), $("#deviceClass").val());
}

/**
 * 保存处理意见
 * @param {} tiid
 * @param {} transition
 * @param {} comment
 * @return {Boolean}
 */
function saveTaskComm(tiid,transition,comment){
	var passedList= false
	var aDevList = [];
//	if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
//  		return false;
//  	}
	var id = $("#id").val();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=deal_approve",
		data : {
			id:id,
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
//处理意见自动填值
function setTextAreaValue(){
	$("#noteInfo4").html(_cjcl.getValue());
	$("#noteInfo4").focus();
}

//function deviceStock(){
//	var args = new Object();
//	args.deviceType = $("#deviceType").val();
//	args.deviceClass = $("#deviceClass").val();
//	args.areaCode = $("#areaCode").val();
//	var sFeature = "dialogHeight:430px;dialogWidth:848px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:854px;status:no;scroll:auto;help:no";
//	}
//	window.showModalDialog(BASE_PATH + "page/EquipmentManagement/EquipmentUse/ToDo/DeviceStock.jsp",args,sFeature);
//}


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

