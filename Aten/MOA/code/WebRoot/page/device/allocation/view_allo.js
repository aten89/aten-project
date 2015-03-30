//设备调拨--详情
$(initEquipmentInfoAllotView);
function initEquipmentInfoAllotView() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
}











