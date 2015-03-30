var mainFrame = $.getMainFrame();
$(initFinanceHandle);

function initFinanceHandle() {
	//添加权限约束
//    $.handleRights({
//        "commitBut" : $.OaConstants.DISPOSE
//    });
    $("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
//	$("#costsLog .mb").hide();
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
				
	 //提交
  	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			$("#transitionName").val($(this).val());
  			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
  					commitDispose();
				} else {
					return;
				}
			});
  		});
  	});
	
	
}
function commitDispose(){
	var transitionName = $("#transitionName").val();
	var aDevList = [];
	var flag=false;
	if(transitionName!="驳回修改"){
		$("div.feesSp").each(function(){
			var devDetail = new Object();
			var remaining=$.trim($("input[name='remaining']", this).val());
			var listID= $.trim($("span[name='listID']", this).text());
			var depreciation=$.trim($("input[name='depreciation']", this).val());
			var deviceName =$.trim( $("span[name='deviceName']", this).text());
			if($("input[name='remaining']", this).size() > 0 && validatePriceField(remaining,deviceName+"设备余值",true)){
					flag=true;
					return false;
			}
			if($("input[name='depreciation']", this).size() > 0 && validatePriceField(depreciation,deviceName+"已提折旧",true)){
					flag=true;
					return false;
			}
			devDetail.deviceID = $("span[name='deviceID']", this).text();
			devDetail.remaining = remaining;
			devDetail.depreciation = depreciation;
			devDetail.devDetailId=listID;
			aDevList.push(devDetail);
		});
		if(flag){
			return false;
		}
	}
	
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	var taskid = $("#taskInstanceID").val();
	var comment =  $.trim($("#comment").val());
	//驳回修改 一定要写意见
	if (transitionName == "驳回修改" && comment=="") {
		$.alert("请填写驳回意见");
		$("#comment").focus();
		return;
	}else{
		if (comment=="") {
			$.alert("请填写审批意见");
			$("#comment").focus();
			return;
		}
	}
	if(comment.length > 500){
		$.alert("填写意见请不要超过500个字符");
		return;
	}
	var result = $.validChar(comment,"<>");
	if (result) {
		$.alert("审批意见不能包含非法字符：" + result);
		$("#comment").focus();
		return;
	}
	var id = $("#formId").val();
	if (id == "") {
		return;
	} 
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_deal?act=deal_finance",//
		data:{
			id : id,
			aDevList : $.toJSON(aDevList),
			tiid:taskid,
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

