var mainFrame = $.getMainFrame();

$(initVacationApprovalView);

function initVacationApprovalView() {
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
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