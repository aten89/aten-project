//设备申购--设备验收
var mainFrame = $.getMainFrame();
var dialogWin;
var bntFlag=true;
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
//					var tiid=$("#taskInstanceID").val();
//					var transition = $(this).val();
//					saveTaskComm(tiid,transition,"");
						bntFlag =false;
					} 
					commitDispose(tiid,transition,comment);
				} else {
					return;
				}
			});
			
		});
		
	});
	
}

//添加验收单
function equipmentAcceptAdd(self){
	var selfObj=$("table", $(self).parent().parent().parent().parent().parent().get(0));
	var args = new Object();
	var deviceClassId=$("span[name=deviceClass]",selfObj).text();
	var remark =	$("span[name=remark]",selfObj).html(args.valiHtml);
	if(remark==null){
		remark="";
	}
	args.remark=remark;
	
	if(	$("span[name=valiSpan]",selfObj).text()!=null && $("span[name=valiSpan]",selfObj).text()!=""){
		var valiTiems = new Array(); 
		$("span[name=valiSpan]",selfObj).each(function(){
			var obj = new Object();
			var itemName = $("span[name=itemName]",this).text();
			var itemRemark = $("span[name=itemRemark]",this).text();
			var isEligibility = $("span[name=isEligibility]",this).text();
			obj.itemName=itemName;
			obj.itemRemark=itemRemark;
			obj.isEligibility=isEligibility;
			valiTiems.push(obj);
		});
		
		args.valiTiems=valiTiems;
	}
	
	args.deviceClass = $("span[name=deviceClass]",selfObj).text();
//	var sFeature = "dialogHeight:402px;dialogWidth:808px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:457px;dialogWidth:816px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备验收单", BASE_PATH + "m/dev_deal?act=initvalidate&deviceClassId="+deviceClassId, 750, 400, function(){
		if (window.returnValue1) {
			$("span[name=validateForm]",selfObj).html(args.valiHtml);
			$("span[name=flag]",selfObj).html("<div class=\"jjC04\">已验收</div>");
			$("span[name=bntName]",selfObj).html("<img src=\"themes/comm/images/customEdit.gif\" />修改验收单");
		}
	});
//	var ok = window.showModalDialog(BASE_PATH + "m/dev_deal?act=initvalidate&deviceClassId="+deviceClassId,args,sFeature);
//	if(ok){
//	}
}

function commitDispose(tiid,transition,comment){
	if ($("div.feesSp").size() == 0) {
		$.alert("当前没有设备信息 ！");
		return false;
	}
	var areaCode = $.trim($("#areaCode").val());
  	var flag=false;
	var aDevList = [];
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		var deviceName = $("span[name=deviceName]",this).text();
		var valiSpan = $.trim($("span[name=validateForm]",this).text());
		if(bntFlag && valiSpan==""  ){
			$.alert("设备名称为“"+deviceName+"”没有添加验收单!");
			flag = true;
			return false;
		}
		var valiTiems = new Array(); 
		$("span[name=valiSpan]",this).each(function(){
			var obj = new Object();
			var itemName = $("span[name=itemName]",this).text();
			var itemRemark = $("span[name=itemRemark]",this).text();
			var isEligibility = $("span[name=isEligibility]",this).text();
			obj.itemName=itemName;
			obj.itemRemark=itemRemark;
			obj.isEligibility=isEligibility;
			valiTiems.push(obj);
		
		});
		var userId = $.trim($("span[name=applicant]",this).text());
		var valiDate = $.trim($("span[name=valiDate]",this).text());
		var valiFormId = $.trim($("span[name=valiFormId]",this).text());
		var remark = $.trim($("span[name=remark]",this).text());
		var deviceNo = $.trim($("span[name=deviceNo]", this).text());
		devDetail.id = $.trim($("span[name=purchaseId]",this).text());
		var deviceClass = $.trim($("span[name=deviceClass]", this).text());
		
		devDetail.valiTiems=valiTiems;
		devDetail.userId=userId;
		devDetail.valiDate=valiDate;
		devDetail.remark=remark;
		devDetail.valiFormId=valiFormId;
		devDetail.deviceClass=deviceClass;
		devDetail.deviceNo = deviceNo;
		devDetail.areaCode = areaCode;
		aDevList.push(devDetail);
	});
	if(bntFlag&& flag){
		return false;
	}
	if(bntFlag && ($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000))){
		return false;
  	}
	
	var id = $("#id").val();
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/dev_deal?act=deal_accept",//
		data:{
			id : id,
			tiid : tiid,
   			transition : transition,
   			comment : comment,
   			bntFlag : bntFlag,//是否是驳回修改
			aDevList : $.toJSON(aDevList)
		},
		 success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("操作成功", function(){
            		promptDeviceNoTheSame(id);
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
 * 对设备最终编号与预编号不一致的情况进行提示
 * @param {} id 表单ID
 */
function promptDeviceNoTheSame(id) {
	var promptStr = "";
	$.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/dev_apply_start",//
		data:{
			id : id,
			act : "getinfo"
		},
		 success : function(xml){
            //解析XML中的返回代码
		 	//$.alert($(xml).get(0).xml);
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$(".feesSp").each(function(){
            		var listID = $("span[name='listID']", this).text();
            		var listNO = $("span[name='deviceNO']", this).text();
            		var existNO = "";//被占用的编号列表
            		var changeInfo = "";
            		$(xml).find('document').each(function(index){
			    		var curELe = $(this);
	                    var id = $.trim(curELe.attr("id"));
	                    var deviceNO = $.trim($("device-no",curELe).text());
	                    if (listID == id && listNO != deviceNO) {
	                    	if (existNO != "") {
	                    		existNO += "、";
	                    	}
	                    	existNO += "“" + listNO + "”";
	                    	if (changeInfo != "") {
	                    		changeInfo += "\n";
	                    	}
	                    	changeInfo += listNO + "->" + deviceNO;
	                    }
					});
					if (existNO != "") {
						promptStr = existNO + "编号已被系统中的其它设备所占用！系统进行了重新编号（只针对本次申购中的编号已被占用的设备），重新编号情况如下：\n" + changeInfo;
						$.alert(promptStr);
					}
            	});
            } 
        },
        error : $.ermpAjaxError
    }); 
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




