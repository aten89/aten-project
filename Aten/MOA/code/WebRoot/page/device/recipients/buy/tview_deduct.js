//设备申购--设备验收
var mainFrame = $.getMainFrame();
var dialogWin;
$(initEquipmentInfoUseAccept);
var _cjcl;
function initEquipmentInfoUseAccept() {
	var id = $("#id").val();
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
					if (transition == "驳回修改") {
						valiFlag=false;
						saveTaskComm(tiid,transition,comment);
					} else {
						saveTaskComm(tiid,transition,comment);
					}
				} else {
					return;
				}
			});
		});
	});
}


function saveTaskComm(tiid,transition,comment){
	if ($("div.feesSp").size() == 0) {
		$.alert("当前没有设备信息 ！");
		return false;
	}
	
  	var flag=false;
	var aDevList = [];
	var deductTotMoney=0;
	$("div.feesSp").each(function(){
		if($.trim($("span[name=purchaseId]",this).text()) != ""){
			var devDetail = new Object();
			var id = $("span[name=purchaseId]",this).text();
			var price = $("span[name=price]",this).text();
			var deviceName = $("span[name=deviceName]",this).text();
			var deductFlag = false;
			if($("#yk_"+id).attr('checked')!="undefined" && $("#yk_"+id).attr('checked')){
				deductFlag =true;
			}
			var deductMoney ="";
			if(deductFlag){
				deductMoney = $.trim($("#deductMoney_"+id).val());
				if(validatePriceField("deductMoney_"+id, "设备名称为【"+deviceName+"】的扣款金额", true)){
					flag = true;
			  		return false;
			  	}
			  	if(parseFloat(deductMoney)>parseFloat(price)){
			  		$.alert("设备名称为【"+deviceName+"】的扣款金额不能大于购买金额！");
			  		flag = true;
			  		return false;
			  		
			  	}
			}
			deductTotMoney = parseFloat(deductTotMoney)+parseFloat(deductMoney);
			devDetail.id = id;
			devDetail.deductFlag=deductFlag?'1':'0';
			devDetail.deductMoney=deductMoney;
			aDevList.push(devDetail);
		}
	});
	if(flag){
		return false;
	}
	if(parseFloat(deductTotMoney)>parseFloat($("#budgetMoney").text())){
		$.alert("申购设备总扣款金额不能大于设备预算金额 ！");
		return false;
	}
	if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
  		return false;
  	}
	var id = $("#id").val();
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/dev_deal?act=deal_purchasecomp",//
		data:{
			id : id,
			tiid : tiid,
   			transition : transition,
   			comment : comment,
			aDevList : $.toJSON(aDevList)
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

function displayOrhide(vv,id){
	if(vv.indexOf("yk")!=-1){
			$("#je_"+id).show();
	}else{
		$("#je_"+id).hide();
	}
	
}	

function viewValiForm(formId){
	mainFrame.addTab({
		id:"oa_valiform"+formId,
		title:"查看验收单",
		url:BASE_PATH +"m/dev_deal?act=toviewvaliform&id="+formId
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
	var v = $("#"+input).val();
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
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备库存", BASE_PATH + "page/device/recipients/buy/stock_dev.jsp", 600, 378);
}

