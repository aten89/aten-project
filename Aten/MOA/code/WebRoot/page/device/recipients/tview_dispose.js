//设备申购审批
var mainFrame = $.getMainFrame();
var dialogWin;
$(initEquipmentInfoUseHandle);
function initEquipmentInfoUseHandle() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
		  		return false;
		  	}
		  	var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment =$("#comment").val();
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
//处理意见自动填值
function setTextAreaValue(){
	$("#noteInfo4").html(_cjcl.getValue());
	$("#noteInfo4").focus();
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}


function deviceStock(){
	var args = new Object();
	args.deviceType = $("#deviceType").val();
	args.deviceClass = $("#deviceClass").val();
	args.areaCode = $("#areaCode").val();
//	var sFeature = "dialogHeight:430px;dialogWidth:848px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:485px;dialogWidth:854px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备库存", BASE_PATH + "page/device/recipients/buy/stock_dev.jsp", 600, 378);
//	window.showModalDialog(BASE_PATH + "page/device/recipients/buy/DeviceStock.jsp",args,sFeature);
}

