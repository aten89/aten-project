//设备申购详情
$(initEquipmentInfoUseView);
function initEquipmentInfoUseView() {
	var id = $("#id").val();
//	initDevPuerpose($("#deviceTypeCode").val(),$("#areaCode").val(), $("#deviceClassCode").val());
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	
//	if ($("#hidModuleRights").val().indexOf("," + "printvalidform" + ",") == -1) {
//		$("input[name='btnPrintValidForm']").remove();//没有打印验收单权限，屏蔽掉打印验收单按钮
//	}
//	
//	if ($("#hidModuleRights").val().indexOf("," + "printpurchform" + ",") == -1) {
//		$("input[name='btnPrintPurchaseForm']").remove();//没有打印申购单权限，屏蔽掉打印申购单按钮
//	}
//	
//	if ($("#hidModuleRights").val().indexOf("," + "printfundform" + ",") == -1) {
//		$("input[name='btnPrintFundForm']").remove();//没有打印请款单权限，屏蔽掉打印请款单按钮
//	}
	
//	//打印请款单
//	$("#btnPrintFundForm").click(function(){
//		printFundForm(id);
//	});
//	//打印验收单
//	$("input[name='btnPrintValidForm']").each(function(i){
//		$(this).click(function(){
//			var purchaseDeviceID = $(this).attr("id").split("_")[1];
//			printValidForm(purchaseDeviceID);
//		});
//		
//	});
//	//打印申购单
//	$("#btnPrintPurchaseForm").click(function(){
//		printPurchaseForm(id);
//	});
}

///**
// * 打印申购单
// */
//function printPurchaseForm(id) {
//	$.showModalDialog(BASE_PATH + "m/dev_deal?act=print_purchase_form&id=" + id, "", "dialogHeight:568px;dialogWidth:793px;status:no;scroll:auto;help:no");
//}

/**
 * 打印验收单
 */
//function printValidForm(purchaseDeviceID) {
//	var sURL = BASE_PATH + "m/dev_deal?act=print_dev_valid_form&purchaseDeviceID=" + purchaseDeviceID;
//	$.showModalDialog(sURL, "", "dialogHeight:568px;dialogWidth:793px;status:no;scroll:auto;help:no");
//}

///**
// * 打印请款单
// */
//function printFundForm(id) {
//	$.showModalDialog(BASE_PATH + "m/dev_deal?act=print_request_fund_form&id=" + id, "", "dialogHeight:638px;dialogWidth:793px;status:no;scroll:auto;help:no");
//}





