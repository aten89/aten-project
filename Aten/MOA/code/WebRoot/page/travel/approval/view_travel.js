/**********************
出差管理--出差审批单--详情
*********************/ 
var mainFrame = $.getMainFrame();
$(initTripApproval);
function initTripApproval(){
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
}
