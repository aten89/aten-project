var mainFrame = $.getMainFrame();
var holidayApply = null;

$(initVacationApprovalAdd);
function initVacationApprovalAdd(){
	
	//tab标签
    $("#crmTab li").each(function(i){
		$(this).click(function(){
		  	$(this).addClass("current").siblings().removeClass("current");
		  	$("#tab0" + i).show().siblings().hide();
		});
	});
	var val = $("#hidRecruitmentType").val();
  	if (val == "网络招聘") {
		$("#recommended").show();
		$("#recommendedTD").text("招聘人");
	} else if (val == "内部推荐") {
		$("#recommended").show();
		$("#recommendedTD").text("推荐人");
	} 
	
}
