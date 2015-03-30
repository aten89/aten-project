$(initEquipmentInfoView);
/**
 * 设备详情
 */
function initEquipmentInfoView() {
	//tab标签
    $("#crmTab li").each(function(i){
		$(this).click(function(){
		  $(this).addClass("current").siblings().removeClass("current");
		  $("#tab0" + i).show().siblings().hide();
		 })
		
	})
}











