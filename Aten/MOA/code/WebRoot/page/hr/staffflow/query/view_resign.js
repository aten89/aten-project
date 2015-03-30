var mainFrame = $.getMainFrame();

$(initVacationApprovalView);

function initVacationApprovalView() {
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

function hideTbody(flagId, dataId) {
	var flag = $("#" + flagId).text();
	if (flag == "-") {
		$("#" + flagId).text("+");
		$("#" + dataId).hide();
	} else {
		$("#" + flagId).text("-");
		$("#" + dataId).show();
	}
}
