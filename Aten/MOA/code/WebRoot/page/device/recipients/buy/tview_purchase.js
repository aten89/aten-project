//设备申购--设备采购
var mainFrame = $.getMainFrame();
var dialogWin;
var deviceClassAndMainFlag;//设备类别加是否主设备标识
var deviceClassAndMainMap;
var bntFlag=true;
$(initEquipmentInfoUsePurchase);
function initEquipmentInfoUsePurchase() {
	var id = $("#id").val();
	$("div.feesSp").each(function(){
		var price = $("span[name=price]",this).text();
		$("#totPrice").val(parseFloat($("#totPrice").val())-parseFloat(price));
	});
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
					
					//			if ($(this).val() == "驳回修改") {
					//				bntFlag =false;
					//			} 
					saveTaskComm(tiid,transition,comment);
				} else {
					return;
				}
			});
		});
	});
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

//添加设备信息
function equipmentAdd(){
	var args = new Object();
	args.deviceType=$("#deviceType").val();
	args.deviceTypeName = $("#deviceTypeName").text();
	args.deviceName = "";
	args.deviceModel = "";
	args.deviceClass = $("#deviceClass").val();
	args.deviceClassName = $("#deviceClassName").text();
	args.areaCode = $("#areaCode").val();
	args.areaCodePurpose = "";
	args.purpose = "";
	args.applicant = $("#applicant").val();
	args.buyTime = "";
	args.description = "";
	args.optionLists = "";
	args.deviceClassStr = "";
	args.budgetMoney = $("#totPrice").val();
	args.price = "";
	args.getDevCalssMap =getDevCalss();
	args.deviceClassAndMainMap=deviceClassAndMainMap;
//	var sFeature = "dialogHeight:432px;dialogWidth:678px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:487px;dialogWidth:636px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备信息", BASE_PATH + "page/device/recipients/buy/edit_buy.jsp", 580, 393, function(){
		if (window.returnValue1) {
			var  deviceClassName = args.deviceClassName;//
			var deviceClass = args.deviceClass;//
			var deviceName= args.deviceName;//
			var deviceModel = args.deviceModel ;//
			var buyTime = args.buyTime;//
			var description = args.description;//
			var optionLists = args.optionLists;
			var price = args.price;//
			var sHtml = createDeviceHTML(args);
			$("#devListBody").append(sHtml);
			$("div.feesSp").each(function(i){
				$("span[name='orderNum']", this).text(i+1);
			});
		}
	});
	
//	var ok =window.showModalDialog(BASE_PATH + "page/device/recipients/buy/edit_buy.jsp",args,sFeature);
//	if(ok){
//		
//	}
}

/**
 * 生成报废设备详情HTML
 */
function createDeviceHTML(args) {
	var sHtml = "";
	sHtml+="<div class=\"feesSp fsMar\">";
    sHtml+="<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" >";
    sHtml += "<tr>";
	sHtml += "<td class=\"spNum\"><span name=\"orderNum\"></span></td>";
	sHtml += "<td colspan=\"6\" class=\"spOpW opw2\">";
	sHtml += "<div>";
	sHtml += "  <a href=\"javascript:void(0)\" onclick=\"equipmentEdit(this)\"><img src=\"themes/comm/images/spEdit.gif\" />修改</a><a href=\"javascript:void(0)\" onclick=\"delItem(this,'"+args.price+"')\"><img src=\"themes/comm/images/spDel.gif\" />删除</a>";
	sHtml += "</div>";
	sHtml += "</td>";
	sHtml += "</tr>";
    sHtml+="<tr>";
    sHtml+="<td width=\"100\" height=\"27\" class=\"spTit\">设备名称：</td>";
    sHtml+="<td width=\"128\"><span name=\"deviceName\">"+args.deviceName+"</span></td>";
    sHtml+="<td width=\"100\" class=\"spTit\">设备类别：</td>";
    sHtml+="<td width=\"100\"><span name=\"purpose\" style=\"display: none\">"+args.purpose+"</span><span name=\"main_flag\" style=\"display: none\">"+args.mainFlag+"</span><span name=\"areaCodePurpose\" style=\"display: none\">"+args.areaCodePurpose+"</span><span name=\"deviceClassName\">"+args.deviceClassName+"</span><span name=\"deviceClass\" style=\"display: none\">"+args.deviceClass+"</span></td>";
    sHtml+="<td width=\"70\" class=\"spTit\">设备型号：</td>";
    sHtml+="<td width=\"290\"><span name=\"deviceModel\">"+args.deviceModel+"</span></td>";
    sHtml+="<td rowspan=\"4\"  class=\"spOpW\">&nbsp;</td>";
    sHtml+=" </tr>";
    if(args.optionItem!=""){
    	sHtml+=" <tr>";
	    sHtml+="<td width=\"70\" class=\"spTit\">配置信息：</td>";
	    sHtml+="<td colspan=\"5\" width=\"500\"><span name=\"optionItem\">"+args.optionItem+"</span><span name=\"optionLists\" style=\"display: none\">"+args.optionLists+"</span></td>";
	    sHtml+="</tr>";
    }
    sHtml+="<tr>";
    sHtml+="<td width=\"70\" height=\"27\" class=\"spTit\">购买金额：</td>";
    sHtml+="<td width=\"128\"><span name=\"price\">"+args.price+"</span>元</td>";
    sHtml+="<td width=\"70\" class=\"spTit\">购买日期：</td>";
    sHtml+="<td width=\"\" colspan=\"3\"><span name=\"buyTime\">"+args.buyTime+"</span></td>";
    sHtml+="</tr>";
//     sHtml+=" <tr>";
//    sHtml+="<td width=\"70\" class=\"spTit\">设备用途：</td>";
//    sHtml+="<td><span name=\"purposeName\">"+args.purposeName+"</span></td>";
//    sHtml+="<td width=\"70\" class=\"spTit\">工作所在地：</td>";
//    sHtml+="<td colspan=\"3\" width=\"500\"><span name=\"areaCodePurposeName\">"+args.areaCodePurposeName+"</span></td>";
//    sHtml+="</tr>";
    sHtml+=" <tr>";
    sHtml+="<td width=\"70\" class=\"spTit\">设备描述：</td>";
    sHtml+="<td colspan=\"5\" width=\"500\"><span name=\"description\">"+args.description+"</span></td>";
    sHtml+="</tr>";
    sHtml+="</table>";
    sHtml+= " <div class=\"blank\" style=\"height:5px\"></div>";
    sHtml+="</div>";
    $("#totPrice").val($("#totPrice").val()-args.price);
  	return sHtml;
}

/**
 * 删除设备选择结果
 * @param {} o
 */
function delItem(o,price) {
	$.confirm("是否确认删除？", function(r){
		if (r) {
			$("#totPrice").val(parseFloat($("#totPrice").val())+parseFloat(price));
			var oDel = $(o).parent().parent().parent().parent().parent().parent();
			oDel.remove();
			$("div.feesSp").each(function(i){
				$("span[name='orderNum']", this).text(i+1);
			});
		}
	});
}

//修改设备信息
function equipmentEdit(self){
	var selfObj=$("table", $(self).parent().parent().parent().parent().parent().parent().get(0));
	var args = new Object();
	$("#totPrice").val(parseFloat($("#totPrice").val())+parseFloat($("span[name=price]",selfObj).text()));
	args.deviceName = $("span[name=deviceName]",selfObj).text();
	args.deviceModel = $("span[name=deviceModel]",selfObj).text();
	args.deviceClass = $("span[name=deviceClass]",selfObj).text();
	args.deviceClassStr = $("span[name=deviceClass]",selfObj).text();
	args.deviceClassName = $("span[name=deviceClassName]",selfObj).text();
	args.buyTime = $("span[name=buyTime]",selfObj).text();
	args.description = $("span[name=description]",selfObj).text();
	args.optionLists = $("span[name=optionLists]",selfObj).text();
	args.price = $("span[name=price]",selfObj).text();
	args.deviceType=$("#deviceType").val();
	args.deviceTypeName = $("#deviceTypeName").text();
	args.budgetMoney = $("#totPrice").val();
	args.areaCode = $("#areaCode").val();
	args.purpose =  $("span[name=purpose]",selfObj).text();
	args.areaCodePurpose =  $("span[name=areaCodePurpose]",selfObj).text();
	args.applicant = $("#applicant").val();
	args.getDevCalssMap =getDevCalss();
	args.deviceClassAndMainMap=deviceClassAndMainMap;
//	var sFeature = "dialogHeight:432px;dialogWidth:678px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:487px;dialogWidth:636px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = args;
	
	dialogWin = $.showModalDialog("设备信息", BASE_PATH + "page/device/recipients/buy/edit_buy.jsp", 580, 393, function(){
		if (window.returnValue1) {
			$("span[name=deviceName]",selfObj).text(args.deviceName);
			$("span[name=deviceModel]",selfObj).text(args.deviceModel);
			$("span[name=deviceClass]",selfObj).text(args.deviceClass);
			$("span[name=buyTime]",selfObj).text(args.buyTime);
			$("span[name=description]",selfObj).text(args.description);
			if(args.optionItem!=""){
				$("span[name=optionLists]",selfObj).text(args.optionLists);
				$("span[name=optionItem]",selfObj).text(args.optionItem);
			}
			$("span[name=price]",selfObj).text(args.price);
			$("span[name=deviceClassName]",selfObj).text(args.deviceClassName);
			$("span[name=purpose]",selfObj).text(args.purpose);
			$("span[name=purposeName]",selfObj).text(args.purposeName);
			$("span[name=areaCodePurpose]",selfObj).text(args.areaCodePurpose);
			$("span[name=areaCodePurposeName]",selfObj).text(args.areaCodePurposeName);
			$("span[name=mainFlag]",selfObj).text(args.mainFlag);
		}
	});
	
//	var ok =window.showModalDialog(BASE_PATH + "page/EquipmentManagement/EquipmentUse/ToDo/EquipmentInfoEdit.jsp",args,sFeature);
//	if(ok){
//	}
	$("#totPrice").val(parseFloat($("#totPrice").val())-parseFloat($("span[name=price]",selfObj).text()));
	
}

function saveTaskComm(tiid,transition,comment){
	if (bntFlag && ($("div.feesSp").size() == 0)) {
		$.alert("你还未填写要申购的设备信息 ！");
		return false;
	}
	if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
  		return false;
  	}
	var aDevList = [];
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		devDetail.belongtoAreaCode = $("#areaCode").val();
		devDetail.deviceName = $("span[name=deviceName]",this).text();
		devDetail.deviceModel = $("span[name=deviceModel]",this).text();
		devDetail.deviceClass = $("span[name=deviceClass]",this).text();
		devDetail.buyTime = $("span[name=buyTime]",this).text();
		devDetail.description = $("span[name=description]",this).text();
		devDetail.optionLists = $("span[name=optionLists]",this).text();
		devDetail.price = $("span[name=price]",this).text();
		devDetail.valiFormId = $("span[name=valiFormId]",this).text();
		devDetail.orderNum = $("span[name=orderNum]",this).text();
		devDetail.areaCodePurpose = $("span[name=areaCodePurpose]",this).text();
		devDetail.purpose = $("span[name=purpose]",this).text();
		aDevList.push(devDetail);
	});
	var id = $("#id").val();
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/dev_deal?act=deal_procure",//
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
}
function viewValiForm(formId){
	mainFrame.addTab({
		id:"oa_valiform"+formId,
		title:"查看验收单",
		url:BASE_PATH +"m/it_dev_man?act=toviewvaliform&id="+formId
	});
}


function getDevCalss(){
	var optionMap = new Map();//设备属性
	deviceClassAndMainMap = new Map();//设备属性
	var index=1;
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		var deviceClass = $("span[name=deviceClass]",this).text();
		var deviceClassName = $("span[name=deviceClassName]",this).text();
		var purpose = $("span[name=purpose]",this).text();
		var mainFlag = $.trim($("span[name=main_flag]",this).text());
		optionMap.put(deviceClass,purpose);
		
		if(mainFlag=="true"){
			deviceClassAndMainMap.put(deviceClass,index);
			index+=1;
		}
	});
	return optionMap;
}

