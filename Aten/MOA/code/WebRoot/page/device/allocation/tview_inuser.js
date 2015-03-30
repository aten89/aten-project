//设备调拨--办理
var mainPanel = $.getMainFrame();
var valiFlag=true;
$(initEquipmentInfoAllotHandle);
function initEquipmentInfoAllotHandle() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	
	 $(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment =$("#comment").val();
			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					
	//				if ($(this).val() == "驳回修改") {
	//					valiFlag=false;
	//				}else{
	//					valiFlag = true;
	//				}
					saveTaskComm(tiid,transition,comment);
				}else {
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
	var passedList= false
	var aDevList = [];
	if(!($.trim($("#moveType").val())=="IN_STORAGE" || $.trim($("#moveType").val())=="ALLOT_BORROW" )){
		$("div.feesSp").each(function(){
			var devDetail = new Object();
			var deviceName = $("span[name='deviceName']", this).text();
			devDetail.deviceName = deviceName;
			var id = $("span[name='purchaseId']", this).text();
			var purpose = $("select[name='devicePurposeSel']", this).val();
			if(valiFlag){
				if (purpose == "") {
					$.alert("设备名称为“" + deviceName +  "”的“设备用途”不能为空！");
					passedList = true;
					return false;
				}
				var areaCode = $("select[name='areaSel']", this).val();
				if (areaCode == "") {
					$.alert("设备名称为”" + deviceName +  "“的“工作所在地“不能为空！");
					passedList = true;
					return false;
				}
			}
			devDetail.id=id;
			devDetail.purpose=purpose;
			devDetail.areaCode=areaCode;
			aDevList.push(devDetail);
		});
	}
	if(passedList){
		return false;
	}
	if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
  		return false;
  	}
	
	var id = $("#id").val();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=deal_incheckapprove",
		data : {
			id:id,
			aDevList : $.toJSON(aDevList),//调拨设备列表
   			tiid : tiid,
   			valiDevFlag:false,
   			transition : transition,
   			comment : comment
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("操作成功", function(){
	            	//刷新父列表
					mainPanel.getCurrentTab().doCallback();
					//关闭
					mainPanel.getCurrentTab().close();
            	});
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    }); 
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








