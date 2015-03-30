//离职设备处理单
var mainFrame = $.getMainFrame();
$(initEquipmentFlowApproval);

function initEquipmentFlowApproval() {
//	$("#costsLog .mb").hide();
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
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
	var aDevList = [];
	var passList = true;
	$("div.feesSp").each(function(){
		var listID= $.trim($("span[name='listID']", this).text());
		var devDetail = new Object();
		if ($(":radio[name='backBuyFlag"+listID+"']", this).size() > 0 && $(":radio[name='backBuyFlag"+listID+"']:checked", this).size() == 0) {
			$.alert("编号为“" + $("span[name='deviceNO']", this).text() + "”的设备请选择是否回购！");
			passList = false;
			return false;
		}
		var backBuyFlag = $.trim($(":radio[name='backBuyFlag"+listID+"']:checked", this).val());
		devDetail.deviceID = $("span[name='deviceID']", this).text();
		devDetail.devDetailId=listID;
		devDetail.backBuyFlag = backBuyFlag == "" ? "0" : backBuyFlag;
		aDevList.push(devDetail);
	});
	if (!passList) {
		return;
	}
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	var tiid = $("#taskInstanceID").val();
	var comment = "";
	var id = $("#id").val();
	if (id == "") {
		return;
	} 
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_deal?act=deal_backbuyconfirm",//
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