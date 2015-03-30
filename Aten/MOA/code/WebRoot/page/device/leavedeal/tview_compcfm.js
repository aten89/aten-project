//离职设备处理单
var mainFrame = $.getMainFrame();
$(initEquipmentFlowApproval);

function initEquipmentFlowApproval() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	var id = $.trim($("#id").val());
//	$("#costsLog .mb").hide();
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			//处理意见必填
			if($.validInput("comment", "处理意见", true, "\<\>\'\"", 500)){
		  		return false;
		  	}
		  	var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment =$("#comment").val();
		  	$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					if ($(this).val() == "驳回修改") {
						saveTaskComm(tiid,transition,comment);
					} else {
						$("#transitionName").val($(this).val());
						commitDispose(id, tiid, transition, comment);
					}
				} else {
					return;
				}
			});
		});
	});			
}

/**
 * 驳回
 */
function dealTask() {
	
}

/**
 * 审批通过
 * @return {Boolean}
 */
function commitDispose(id, tiid, transitionName, comment){
	if (id == "") {
		return;
	} 
	var aDevList = [];
	var passList = true;//列表是否验证通过
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		var buyPrice=$.trim($("input[name='buyPrice']", this).val());
		var noBuyPrice = $.trim($("input[name='noBuyPrice']", this).val());
		var listID= $.trim($("span[name='listID']", this).text());
		var deviceName =$.trim( $("span[name='deviceName']", this).text());
		var planPayDate = $("input[name='planPayDate']", this).val();
		if($("input[name='planPayDate']", this).size() != 0 && $("input[name='planPayDate']", this).val() == ""){
			$.alert("预期付款日期不能为空！");
			passList = false;
			return false;
		}
		if($("input[name='buyPrice']", this).size() != 0 && validatePriceField(buyPrice,deviceName+"回购价格",true)){
			passList = false;
			return false;
		}
		if($("input[name='noBuyPrice']", this).size() != 0 && validatePriceField(noBuyPrice,deviceName+"不回购价格",true)){
			passList = false;
			return false;
		}
		devDetail.deviceID = $("span[name='deviceID']", this).text();
		devDetail.buyPrice = buyPrice;
		devDetail.noBuyPrice = noBuyPrice;
		devDetail.devDetailId=listID;
		devDetail.planPayDate=planPayDate;
		aDevList.push(devDetail);
	});
	if(!passList){
		return false;
	}
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_deal?act=deal_comp",//
		data:{
			id : id,
			aDevList : $.toJSON(aDevList),
			tiid:tiid,
   			transition:transitionName,
   			comment:comment
		},
		success:function saveSuccess(xml){
		   var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               $.alert("操作成功", function(){
	            	//刷新父列表
					mainFrame.getCurrentTab().doCallback();
					//关闭
					mainFrame.getCurrentTab().close();
            	});
           } else {
               $.alert($("message",xml).text());
           }
		},
        error:$.ermpAjaxError
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
/**
 * 验证价格字段格式正确与否。正确格式为：最多9位整数，如有小数，小数位数最多不能超过2位
 * @param {} input　输入框name
 * @param {} label　字段名
 * @param {} required　是否必填
 * @return {Boolean}　false:验证通过;true:验证不通过
 */
function validatePriceField(v, label, required) {
	if(typeof(required) == "undefined" || !required) {
		required = false;
	} else {
		required = true;
	}
	if(required && v == "") {
		//必填
		$.alert("“" + label + "”不能为空");
		return true;
	}
	//整数位数最多9位，小数位数最多2位
	var re = /^\d{1,9}(\.\d{1,2})?$/;
	if(v != "" && !re.test(v)) {
		$.alert("“" + label + "”格式不正确：最多9位整数，如有小数，小数位数最多不能超过2位");
		return true;
	}
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
		url  : "m/task_deal?act=deal_task",
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
