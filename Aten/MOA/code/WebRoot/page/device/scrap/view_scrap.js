//设备报废--详情
$(initEquipmentInfoScrapView);
var _cjcl;
function initEquipmentInfoScrapView() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
}












