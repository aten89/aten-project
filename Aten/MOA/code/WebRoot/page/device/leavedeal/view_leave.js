//设备报废--详情
$(initEquipmentInfoDealView);
function initEquipmentInfoDealView() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	
	//打印回购单
	$("input[name='btnPrintBuyBackForm']").click(function(){
	  	var comment = $("#comment").val();
	  	var buyBackDeviceID = $(this).attr("id").split("_")[1];
//	  	printBuyBackForm(buyBackDeviceID, comment);
	});
}
